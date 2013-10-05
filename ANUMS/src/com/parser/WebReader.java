package com.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class WebReader {

	public WebReader() {
		// TODO Auto-generated constructor stub
	}
	 /* Returns null of OpenStream failed */
    public static InputStream OpenStream(String surl) {

        InputStream in = null;
        if(!surl.startsWith("http"))
        	return null;
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpParams params = httpClient.getParams();
        //the result will abandoned in 15s
        HttpConnectionParams.setConnectionTimeout(params, 15000);
        HttpConnectionParams.setSoTimeout(params, 15000);
        HttpResponse response;
        URI uri;
        try {
			uri = new URI(surl);
			HttpGet method = new HttpGet(uri);
			response = httpClient.execute(method);
			in = response.getEntity().getContent();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
//			e.printStackTrace();
		} catch (ClientProtocolException e) {
			System.err.println(e.getMessage());
			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (IOException e) {
			System.err.println(e.getMessage());
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
   
        return in;
    }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
