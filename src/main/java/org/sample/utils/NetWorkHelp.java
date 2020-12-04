package org.sample.utils;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class NetWorkHelp {
	@SuppressWarnings({ "unused"})
	public String getHttpsResponse(String hsUrl, String requestMethod) {
		InputStream is = null;
		String resultData = "";
		try {
			URL url = new URL(hsUrl);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			TrustManager[] tm = { this.xtm };

			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(null, tm, null);

			con.setSSLSocketFactory(ctx.getSocketFactory());
			con.setHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String arg0, SSLSession arg1) {
					return true;
				}
			});
			con.setDoInput(true);

			con.setDoOutput(false);
			con.setUseCaches(false);
			if ((requestMethod != null) && (!requestMethod.equals(""))) {
				con.setRequestMethod(requestMethod);
			} else {
				con.setRequestMethod("GET");
			}
			is = con.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader bufferReader = new BufferedReader(isr);
			String inputLine = "";
			while ((inputLine = bufferReader.readLine()) != null) {
				resultData = resultData + inputLine + "\n";
			}
			System.out.println(resultData);

			Certificate[] certs = con.getServerCertificates();

			int certNum = 1;
			for (Certificate cert : certs) {
				X509Certificate localX509Certificate = (X509Certificate) cert;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultData;
	}

	X509TrustManager xtm = new X509TrustManager() {
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		}

		public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		}
	};
}
