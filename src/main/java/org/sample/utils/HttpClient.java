package org.sample.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * A utility class for HttpClient.
 * 
 */
@SuppressWarnings("rawtypes")
public final class HttpClient {

    private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);

    private static final int HTTP_RESPONSE_CODE_FLAG = 300;

    private static final String IBM_JSSE2_SOCKETFACTORY_CLASS = "com.ibm.jsse2.SSLSocketFactoryImpl";

    private static SSLSocketFactory jsse2SslSocketFactory;

    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";

    public static final String CONTENT_TYPE_XML = "text/xml; charset=UTF-8";
    public static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";

    private static final String HTTP_METHOD_POST = "POST";

    private static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";

    private static final String HTTP_HEADER_CONTENT_LENGTH = "Content-Length";

    private static final String USER_AGENT = "User-Agent";

    private static final String FAKE_USER_AGENT = "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)";

    static {
        if (System.getProperty("java.vm.vendor").toLowerCase().startsWith("ibm")) {
            try {
                Class jsse2SslSocketFactoryClass = Class.forName(IBM_JSSE2_SOCKETFACTORY_CLASS);
                jsse2SslSocketFactory = (SSLSocketFactory) jsse2SslSocketFactoryClass.newInstance();
            } catch (Exception e) {
                logger.warn("Error when create jsse2SslSocketFactory.", e);
            }
        }
    }

    public static String getStartStabilityUrl(String ip) {
        return String.format("http://%s:2015/stabilityMainTest", ip);

    }

    /**
     * Contacts the remote URL and returns the response.
     * 
     * @return the response.
     */
    public static String getResponseFromServer(final String url, int connectionTimeout, int readTimeout,
            boolean trustAllCerts) throws IOException {
        URLConnection conn = new URL(url).openConnection(Proxy.NO_PROXY);
        conn.setRequestProperty(USER_AGENT, FAKE_USER_AGENT);
        initSslSetting(conn, trustAllCerts);
        if (connectionTimeout >= 0) {
            conn.setConnectTimeout(connectionTimeout);
        }
        if (readTimeout >= 0) {
            conn.setReadTimeout(readTimeout);
        }
        if (conn instanceof HttpURLConnection) {
            validateResponse((HttpURLConnection) conn);
        }
        return readResult(conn.getInputStream());
    }

    private static void initSslSetting(URLConnection conn, boolean trustAllCerts) {
        if (conn instanceof HttpsURLConnection) {
            if (trustAllCerts) {
                try {
                    SSLContext sslCtx = SSLContext.getInstance("TLS");
                    sslCtx.init(null, new TrustManager[] { new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType)
                                throws CertificateException {
                            // do nothing
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType)
                                throws CertificateException {
                            // do nothing
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                    } }, new SecureRandom());
                    ((HttpsURLConnection) conn).setSSLSocketFactory(sslCtx.getSocketFactory());
                    ((HttpsURLConnection) conn).setHostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String s, SSLSession sslSession) {
                            return true;
                        }
                    });
                } catch (Exception ex) {
                    logger.warn("Error when set sslCtx.", ex);
                }
            } else {
                if (System.getProperty("java.vm.vendor").toLowerCase().startsWith("ibm")
                        && jsse2SslSocketFactory != null) {
                    ((HttpsURLConnection) conn).setSSLSocketFactory(jsse2SslSocketFactory);
                }
                ((HttpsURLConnection) conn).setHostnameVerifier(HttpsURLConnection.getDefaultHostnameVerifier());
            }
        }
    }

    public static String getResponseViaPost(final String url, String param, String contentType, int connectionTimeout,
            int readTimeout, boolean trustAllCerts, String charsetName) throws IOException {
        URLConnection connection = new URL(url).openConnection(Proxy.NO_PROXY);
        initSslSetting(connection, trustAllCerts);
        HttpURLConnection con = (HttpURLConnection) connection;
        if (connectionTimeout >= 0) {
            con.setConnectTimeout(connectionTimeout);
        }
        if (readTimeout >= 0) {
            con.setReadTimeout(readTimeout);
        }
        con.setInstanceFollowRedirects(true);
        con.setDoOutput(true);
        con.setRequestMethod(HTTP_METHOD_POST);
        con.setDoInput(true);
        con.setUseCaches(false);
        con.setRequestProperty(HTTP_HEADER_CONTENT_TYPE, contentType);
        con.setRequestProperty(HTTP_HEADER_CONTENT_LENGTH,
                Integer.toString(param.getBytes(Charset.defaultCharset()).length));
        con.setRequestProperty(USER_AGENT, FAKE_USER_AGENT);
        final DataOutputStream printout = new DataOutputStream(con.getOutputStream());
        printout.write(param.getBytes(charsetName));
        printout.flush();
        printout.close();
        validateResponse(con);
        return readResult(con.getInputStream());
    }

    public static String getResponseViaPost(final String url, String param, String contentType, int connectionTimeout,
            int readTimeout, boolean trustAllCerts) throws IOException {
        URLConnection connection = new URL(url).openConnection(Proxy.NO_PROXY);
        initSslSetting(connection, trustAllCerts);
        HttpURLConnection con = (HttpURLConnection) connection;
        if (connectionTimeout >= 0) {
            con.setConnectTimeout(connectionTimeout);
        }
        if (readTimeout >= 0) {
            con.setReadTimeout(readTimeout);
        }
        con.setInstanceFollowRedirects(true);
        con.setDoOutput(true);
        con.setRequestMethod(HTTP_METHOD_POST);
        con.setDoInput(true);
        con.setUseCaches(false);
        con.setRequestProperty(HTTP_HEADER_CONTENT_TYPE, contentType);
        con.setRequestProperty(HTTP_HEADER_CONTENT_LENGTH,
                Integer.toString(param.getBytes(Charset.defaultCharset()).length));
        con.setRequestProperty(USER_AGENT, FAKE_USER_AGENT);
        final DataOutputStream printout = new DataOutputStream(con.getOutputStream());
        printout.write(param.getBytes("UTF-8"));
        printout.flush();
        printout.close();
        validateResponse(con);
        return readResult(con.getInputStream());
    }

    /**
     * 功能描述: <br>
     * 封装报文，通过url post方式进行模拟请求；用于需要单独设置header报文头内容的请求需要；当前用于豆芽消息发送接口调用
     *
     * @param url 请求URL
     * @param headers 请求报文头
     * @param param 请求body体
     * @param contentType 内容类型
     * @param connectionTimeout 连接超时时常
     * @param readTimeout 返回超时时常
     * @param trustAllCerts
     * @return
     * @throws IOException
     * @since [产品/模块版本](可选)
     * @createTime 2016年7月1日 上午11:38:30
     */
    public static String getResponseViaPostForIsvrmessage(final String url, Map<String, String> headers, String param,
            String contentType, int connectionTimeout, int readTimeout, boolean trustAllCerts) throws IOException {
        URLConnection connection = new URL(url).openConnection(Proxy.NO_PROXY);
        initSslSetting(connection, trustAllCerts);
        HttpURLConnection con = (HttpURLConnection) connection;
        if (connectionTimeout >= 0) {
            con.setConnectTimeout(connectionTimeout);
        }
        if (readTimeout >= 0) {
            con.setReadTimeout(readTimeout);
        }
        con.setInstanceFollowRedirects(true);
        con.setDoOutput(true);
        con.setRequestMethod(HTTP_METHOD_POST);
        con.setDoInput(true);
        con.setUseCaches(false);
        // 设置公共header体
        con.setRequestProperty(HTTP_HEADER_CONTENT_TYPE, contentType);
        con.setRequestProperty(HTTP_HEADER_CONTENT_LENGTH,
                Integer.toString(param.getBytes(Charset.defaultCharset()).length));
        con.setRequestProperty(USER_AGENT, FAKE_USER_AGENT);
        if (headers != null && !headers.isEmpty()) { // 遍历map进行特殊header设置
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                con.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        final DataOutputStream printout = new DataOutputStream(con.getOutputStream());
        printout.write(param.getBytes("UTF-8"));
        printout.flush();
        printout.close();
        validateResponse(con);
        return readResult(con.getInputStream());
    }

    private static void validateResponse(HttpURLConnection con) throws IOException {
        if (con.getResponseCode() >= HTTP_RESPONSE_CODE_FLAG) {
            try {
                InputStream es = con.getErrorStream();
                if (es != null) {
                    logger.warn("Did not receive successful HTTP response," + "response content is " + readResult(es));
                }
            } catch (Exception ex) {
                logger.warn("Exception occur when process errorStream", ex);
            }
            throw new IOException("Did not receive successful HTTP response: status code = " + con.getResponseCode()
                    + ", status message = [" + con.getResponseMessage() + "]");
        }
    }

    public static String readResult(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
        try {
            StringBuilder temp = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                temp.append(line);
                line = reader.readLine();
            }
            return temp.toString();
        } finally {
            reader.close();
        }
    }

    /**
     * 
     * 功能描述: <br>
     * 〈功能详细描述〉
     * 
     * @param httpUrl
     * @param saveFile
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static boolean httpDownload(String httpUrl, String saveFile) {
        FileOutputStream fs = null;
        try {
            // 下载网络文件
            int byteread = 0;
            URL url = null;
            url = new URL(httpUrl);
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            fs = new FileOutputStream(saveFile);
            byte[] buffer = new byte[1024];
            while ((byteread = inStream.read(buffer)) != -1) {
                fs.write(buffer, 0, byteread);
            }
            return true;
        } catch (Exception ex) {
            logger.error("httpDownload###error{}", ex);
            return false;
        } finally {
            if (null != fs) {
                try {
                    fs.close();
                } catch (IOException e) {
                    logger.error("httpDownload###error{}", e);
                }
            }
        }
    }

    /*public static void main(String[] args) {
        try {
            Gson gson = new Gson();
            HttpClient.getResponseViaPost("http://127.0.0.1:8080/tea/device/regist", gson.toJson(""),
                    HttpClient.CONTENT_TYPE_JSON, 5000, 20000, false);
        } catch (Throwable ex) {
            logger.error("main###error{}", ex);
        }
    }*/
}
