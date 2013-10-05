package com.util;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;

import com.sun.istack.internal.logging.Logger;


public class InsecureHttpClientFactory {
	protected Logger log = Logger.getLogger(this.getClass());
	public DefaultHttpClient hc;
    public DefaultHttpClient buildHttpClient() {
    	hc = new DefaultHttpClient();
//		configureProxy();
		configureCookieStore();
		configureSSLHandling();
		return hc;
	}
       
        private void configureProxy() {
                HttpHost proxy = new HttpHost("proxy.example.org", 3182);
                hc.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }
 
	private void configureCookieStore() {
		CookieStore cStore = new BasicCookieStore();
		hc.setCookieStore(cStore);
	}
 
	private void configureSSLHandling() {
		Scheme http = new Scheme("http", 80, PlainSocketFactory.getSocketFactory());
		SSLSocketFactory sf = buildSSLSocketFactory();
		Scheme https = new Scheme("https", 443, sf);
		SchemeRegistry sr = hc.getConnectionManager().getSchemeRegistry();
		sr.register(http);
		sr.register(https);
	}
 
	private SSLSocketFactory buildSSLSocketFactory() {
		TrustStrategy ts = new TrustStrategy() {
			@Override
			public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
				return true; // heck yea!
			}
		};
 
		SSLSocketFactory sf = null;
 
		try {
			/* build socket factory with hostname verification turned off. */
			sf = new SSLSocketFactory(ts, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		}
 
		return sf;
	}
 

}
