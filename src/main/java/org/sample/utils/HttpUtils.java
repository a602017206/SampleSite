/**
 * 
 */
package org.sample.utils;

import com.alibaba.fastjson.JSON;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.newSite.common.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;

/**
 * http请求工具类
 * 
 *
 */
public class HttpUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);
	/**
	 * 发送get请求，将返回值解析成对应的String
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String doGet(String url) throws ClientProtocolException, IOException {
		return doGet(url, null, null, null);
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
		String result = "";
		result = doGet(url);
		// 得到的返回值转换为对象
		return strToBean(clazz, result);
	}

	/**
	 * 发送post请求，将返回值解析成对应的String
	 * 
	 * @param url
	 * @param postObject
	 * @return
	 */
	public static <T> T doPost(Class<T> clazz, String url, Object postObject) {
		String result = "";
		try {
			result = doPost(url, postObject);
		} catch (Exception e) {
            logger.error("doPost###error{}", e);
		}
		// 得到的返回值转换为对象
		return strToBean(clazz, result);
	}

	/**
	 * 发送post请求，将返回值解析成对应的String
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String url) {
		CloseableHttpResponse response = null;
		CloseableHttpClient httpclient = null;
		String result;
		try {
			httpclient = HttpClients.createDefault();

			HttpPost httpPost = new HttpPost(url);
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(1000 * 30)
					.setConnectTimeout(1000 * 30).build();// 设置请求和传输超时时间
			httpPost.setConfig(requestConfig);
			response = httpclient.execute(httpPost);
			int status = response.getStatusLine().getStatusCode();

			switch (status) {
			case HttpStatus.SC_OK:
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity, Charset.forName("UTF-8"));
				EntityUtils.consume(entity);
				break;
			default:
				result = "";
			}
			if (HttpStatus.SC_OK != status) {
				// 不是OK抛出异常
				throw new BusinessException("url not found,url:{}" + url);
			}
			// 得到的返回值转换为对象
			return result;

		} catch (Exception e) {
			throw new BusinessException(e);
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
                    logger.error("doPost###error{}", e);
				}
			}
			// 关闭连接,释放资源
			try {
			    if (null != httpclient) {
                    httpclient.close();
                }
			} catch (IOException e) {
                logger.error("doPost###error{}", e);
			}
		}

	}

	/**
	 * 发送post请求，将返回值解析成对应的String
	 * 
	 * @param url
	 * @param postObject
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String doPost(String url, Object postObject) {
		CloseableHttpResponse response = null;
		CloseableHttpClient httpclient = null;
		String result = null;
		try {
			httpclient = HttpClients.createDefault();

			HttpPost httpPost = new HttpPost(url);
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(1000 * 30)
					.setConnectTimeout(1000 * 30).build();// 设置请求和传输超时时间
			httpPost.setConfig(requestConfig);
			
			String jstr = null;
            if (String.class.isInstance(postObject)) {
                jstr = (String) postObject;
            } else {
                jstr = JSON.toJSONString(postObject);
            }
			httpPost.setEntity(new StringEntity(jstr, ContentType.APPLICATION_JSON));
//			logger.info("post url ::::{},body:{}", url,jstr);
			response = httpclient.execute(httpPost);
			int status = response.getStatusLine().getStatusCode();
//			logger.info("post result status ::::{}", status);
//			switch (status) {
//			case HttpStatus.SC_OK:
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity, Charset.forName("UTF-8"));
				EntityUtils.consume(entity);
//				break;
//			default:
//				return "";
//			}
			// 得到的返回值转换为对象
			return result;

		} catch (Exception e) {
			e.printStackTrace();
            logger.error("doPost###error{}", e);
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
                    logger.error("doPost###error{}", e);
				}
			}
			// 关闭连接,释放资源
			try {
			    if (null != httpclient) {
                    httpclient.close();
                }
			} catch (IOException e) {
                logger.error("doPost###error{}", e);
			}
		}

		return result;
	}

	/**
	 * 发送post请求，将返回值解析成对应的String
	 * 
	 * @param url
	 * @param param
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String doPostForm(String url, Map<String, Object> param) {
		CloseableHttpResponse response = null;
		CloseableHttpClient httpclient = null;
		String result = null;
		try {
			httpclient = HttpClients.createDefault();

			HttpPost httpPost = new HttpPost(url);
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(1000 * 30)
					.setConnectTimeout(1000 * 30).build();// 设置请求和传输超时时间
			httpPost.setConfig(requestConfig);
			// ContentType contentType =
			// ContentType.create("application/x-www-form-urlencoded",
			// Consts.UTF_8);
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			for (Entry<String, Object> entry : param.entrySet()) {
				parameters.add(new BasicNameValuePair(entry.getKey(), Objects.toString(entry.getValue(), "utf-8")));
			}
			HttpEntity requestEntity = new UrlEncodedFormEntity(parameters, Consts.UTF_8);
			httpPost.setEntity(requestEntity);
			response = httpclient.execute(httpPost);
			int status = response.getStatusLine().getStatusCode();

			switch (status) {
			case HttpStatus.SC_OK:
				HttpEntity entity = response.getEntity();
				response.getAllHeaders();
				result = EntityUtils.toString(entity, Charset.forName("UTF-8"));
				EntityUtils.consume(entity);
				break;
			default:
				return "";
			}
			// 得到的返回值转换为对象
			return result;

		} catch (Exception e) {
            logger.error("doPostForm###error{}", e);
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
                    logger.error("doPostForm###error{}", e);
				}
			}
			// 关闭连接,释放资源
			try {
			    if (null != httpclient) {
                    httpclient.close();
                }
			} catch (IOException e) {
                logger.error("doPostForm###error{}", e);
			}
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	private static <T> T strToBean(Class<T> clazz, String str) {
		if (StringUtils.isBlank(str)) {
			return null;
		}
		JSONObject jsonStu = JSONObject.fromObject(str);

		return (T) JSONObject.toBean(jsonStu, clazz);
	}


	// public static void main(String[] args) throws ClientProtocolException,
	// IOException {
	// System.out.println(HttpUtils.doPost("http://10.24.74.40/odin-web-in/test/ok.do",
	// "{}"));
	//
	// }

	public static String postString(String url, String msg) throws ClientProtocolException, IOException {
		CloseableHttpResponse response = null;
		CloseableHttpClient httpclient = null;
		String result = null;
		try {
			httpclient = HttpClients.createDefault();

			HttpPost httpPost = new HttpPost(url);
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(1000 * 30)
					.setConnectTimeout(1000 * 30).build();// 设置请求和传输超时时间
			httpPost.setConfig(requestConfig);

			httpPost.setEntity(new StringEntity(msg, ContentType.create("text/xml", Consts.UTF_8)));
			response = httpclient.execute(httpPost);
			int status = response.getStatusLine().getStatusCode();

			switch (status) {
			case HttpStatus.SC_OK:
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity, Charset.forName("UTF-8"));
				EntityUtils.consume(entity);
				break;
			default:
				return "";
			}
			// 得到的返回值转换为对象
			return result;

		} catch (Exception e) {
            logger.error("postString###error{}", e);
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
                    logger.error("postString###error{}", e);
				}
			}
			// 关闭连接,释放资源
			try {
			    if (null != httpclient) {
                    httpclient.close();
                }
			} catch (IOException e) {
                logger.error("postString###error{}", e);
			}
		}

		return result;
	}

	public static String doGet(String url, String path, Map<String, String> headers, Map<String, String> querys) throws IOException {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		if (null != headers) {
			url = buildUrl(url, path, querys);
		}
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response = null;
		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(1000 * 120)
					.setConnectTimeout(1000 * 120).build();// 设置请求和传输超时时间
			httpGet.setConfig(requestConfig);

			if (headers != null) {
				Set<String> headerKeys = headers.keySet();

				for (String key : headerKeys) {
					httpGet.setHeader(key,headers.get(key));
				}
			}

			response = httpclient.execute(httpGet);

			int status = response.getStatusLine().getStatusCode();
			String result = null;
			switch (status) {
				case HttpStatus.SC_OK:
					HttpEntity entity = response.getEntity();
					result = EntityUtils.toString(entity, Charset.forName("UTF-8"));
					EntityUtils.consume(entity);
					break;
				default:
					return null;
			}
			// 得到的返回值转换为对象
			return result;
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					logger.error("doGet###error{}", e);
				}
			}
			// 关闭连接,释放资源
			try {
				if (null != httpclient) {
					httpclient.close();
				}
			} catch (IOException e) {
				logger.error("doGet###error{}", e);
			}
		}

	}

	private static String buildUrl(String host, String path, Map<String, String> querys)
			throws UnsupportedEncodingException {
		StringBuilder sbUrl = new StringBuilder();
		sbUrl.append(host);
		if (!StringUtils.isBlank(path)) {
			sbUrl.append(path);
		}
		if (null != querys) {
			StringBuilder sbQuery = new StringBuilder();
			for (Entry<String, String> query : querys.entrySet()) {
				if (0 < sbQuery.length()) {
					sbQuery.append("&");
				}
				if (StringUtils.isBlank(query.getKey()) && !StringUtils.isBlank(query.getValue())) {
					sbQuery.append(query.getValue());
				}
				if (!StringUtils.isBlank(query.getKey())) {
					sbQuery.append(query.getKey());
					if (!StringUtils.isBlank(query.getValue())) {
						sbQuery.append("=");
						sbQuery.append(URLEncoder.encode(query.getValue(), "utf-8"));
					}
				}
			}
			if (0 < sbQuery.length()) {
				sbUrl.append("?").append(sbQuery);
			}
		}

		return sbUrl.toString();
	}

}
