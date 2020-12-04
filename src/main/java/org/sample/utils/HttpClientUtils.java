package org.sample.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * httpclient请求使用工具<br>
 *
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class HttpClientUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtils.class);
	private final static String UTF_8 = "utf-8";

	private final static int TIME_OUT = 30 * 1000;
	private final static int MAX_TOTAL = 200;
	private final static int MAX_PERROUTE = 40;
	private final static int MAX_ROUTE = 100;
	private final static int DEFAULT_HTTP_PORT = 80;

	private static CloseableHttpClient httpClient;
	private final static Object lock = new Object();

	private static void config(HttpRequestBase httpRequestBase) {
		// 配置请求的超时设置
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(TIME_OUT)
				.setConnectTimeout(TIME_OUT).setSocketTimeout(TIME_OUT).build();
		httpRequestBase.setConfig(requestConfig);
	}

	/**
	 * 获取HttpClient对象
	 */
	public static CloseableHttpClient getHttpClient(String url) {
		if(StringUtils.isBlank(url)){
			return null;
		}
		String hostname = url.split("/")[2];
		int port = DEFAULT_HTTP_PORT;

		if (hostname.contains(":")) {
			String[] arr = hostname.split(":");
			hostname = arr[0];
			port = Integer.parseInt(arr[1]);
		}
		if (null == httpClient) {
			synchronized (lock) {
				if (null == httpClient) {
					httpClient = createHttpClient(MAX_TOTAL, MAX_PERROUTE, MAX_ROUTE, hostname, port);
				}
			}
		}

		return httpClient;
	}

	/**
	 * 创建HttpClient对象
	 */
	public static CloseableHttpClient createHttpClient(int maxTotal, int maxPerRoute, int maxRoute, String hostname,
			int port) {
		ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
		LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", plainsf).register("https", sslsf).build();
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
		// 将最大连接数增加
		cm.setMaxTotal(maxTotal);
		// 将每个路由基础的连接增加
		cm.setDefaultMaxPerRoute(maxPerRoute);
		HttpHost httpHost = new HttpHost(hostname, port);
		// 将目标主机的最大连接数增加
		cm.setMaxPerRoute(new HttpRoute(httpHost), maxRoute);

		// 请求重试处理
		HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
			
			@Override
			public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
				if (executionCount >= 5) {// 如果已经重试了5次，就放弃
					return false;
				}

				if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
					return true;
				}
				if (exception instanceof SSLHandshakeException || exception instanceof InterruptedIOException
						|| exception instanceof UnknownHostException || exception instanceof ConnectTimeoutException
						|| exception instanceof SSLException) {
					return false;
				}

				HttpClientContext clientContext = HttpClientContext.adapt(context);
				HttpRequest request = clientContext.getRequest();
				// 如果请求是幂等的，就再次尝试
				if (!(request instanceof HttpEntityEnclosingRequest)) {
					return true;
				}
				return false;
			}
		};

		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm)
				.setRetryHandler(httpRequestRetryHandler).build();

		return httpClient;
	}

	private static void setPostParams(HttpPost httpost, Map<String, Object> params) {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for (Entry<String, Object> entry : params.entrySet()) {
			nvps.add(new BasicNameValuePair(entry.getKey(), null == params.get(entry.getKey()) ? "" : params.get(entry.getKey()).toString()));
		}
		try {
			httpost.setEntity(new UrlEncodedFormEntity(nvps, UTF_8));
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("setPostParams failed:::", e);
		}
	}

	/**
	 * GET请求URL获取内容
	 */
	public static String post(String url, Map<String, Object> params) {
		HttpPost httppost = new HttpPost(url);
		config(httppost);
		setPostParams(httppost, params);
		CloseableHttpResponse response = null;
		try {
			CloseableHttpClient client = getHttpClient(url);
			
			String result = "";
			
			if (null != client) {
				response = client.execute(httppost, HttpClientContext.create());
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity, UTF_8);
				EntityUtils.consume(entity);
			}
			
			return result;
		} catch (Exception e) {
			LOGGER.error("HttpClientUtils post failed:::", e);
			return "";
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				LOGGER.error("httpclientutils post failed:::", e);
			}
		}
	}

	/**
	 * GET请求URL获取内容
	 */
	public static String get(String url) {
		HttpGet httpget = new HttpGet(url);
		config(httpget);
		CloseableHttpResponse response = null;
		try {
			CloseableHttpClient client = getHttpClient(url);
			
			String result = "";
			
			if (null != client) {
				response = client.execute(httpget, HttpClientContext.create());
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity, UTF_8);
				EntityUtils.consume(entity);
			}
			
			return result;
		} catch (IOException e) {
			LOGGER.error("调用外部接口失败，url:{},异常：{}",url,e);
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				LOGGER.error("httpclientutils get failed:::", e);
			}
		}
		return null;
	}

	private static <T> T strToBean(Class<T> clz, String str) {
		if (StringUtils.isBlank(str)) {
			return null;
		}
		
		return JSON.parseObject(str, clz);
	}

	/**
	 * 发送get请求，将返回值解析成对应的bean对象
	 * 
	 * @param clazz
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static <T> T doGet(Class<T> clazz, String url) throws ClientProtocolException, IOException {
		return strToBean(clazz, get(url));
	}

	/**
	 * 发送post请求，将返回值解析成对应的String
	 *
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static <T> T doPost(Class<T> clazz, String url, Map<String, Object> params) throws IOException {
		return strToBean(clazz, post(url, params));
	}

}
