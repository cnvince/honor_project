package com.parser;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import com.adapters.AdapterFactory;
import com.util.InsecureHttpClientFactory;


/**
 * @Author PengFei Li Parser class to parse returned result page
 * 
 */
public class Parser implements Runnable {
	private CountDownLatch countDownLatch;
	public String urlstr;
	public HashMap<Integer, Document> documentCollection;
	public int server;

	public Parser(CountDownLatch countDownLatch, String url,
			HashMap<Integer, Document> documentCollection, int server) {
		this.urlstr = url;
		this.countDownLatch = countDownLatch;
		this.documentCollection = documentCollection;
		this.server = server;
		// TODO Auto-generated constructor stub
	}
//parse the retrieved page and put back to the document collection
	public void parse() {
		System.out.println("start parsing:" + this.urlstr);
//		download result page
		this.downlaodPage(urlstr);
		Document document = null;
		long start = System.currentTimeMillis();
		DOMParser parser = new DOMParser();
		try {
			parser.setFeature("http://xml.org/sax/features/namespaces", false);
			InputStream byteStream =WebReader.OpenStream(urlstr);
			if(byteStream!=null)
			{
				parser.parse(new org.xml.sax.InputSource(byteStream));
				document=parser.getDocument();
			}
		} catch (SAXNotRecognizedException | SAXNotSupportedException e) {
			// TODO Auto-generated catch block
//			 e.printStackTrace();
			System.err.println(e.getMessage());
			System.err.println(urlstr + " skipped");
		} // IMPORTANT!!
		catch (SAXException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			System.err.println(urlstr + " skipped");
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			System.err.println(urlstr + " skipped");
		}
		long end = System.currentTimeMillis();
		System.out.println("server:"+server+"  "+urlstr + ":" + (end - start) / 1000 + "s");
		if(document!=null)
			documentCollection.put(server, document);
		countDownLatch.countDown();
	}
	public void downlaodPage(String link) {
		System.out.println("Downloading page:"+link);
		InsecureHttpClientFactory httpclientfactory=new InsecureHttpClientFactory();
		// Create an instance of HttpClient.
		HttpClient httpclient = httpclientfactory.buildHttpClient();
		HttpGet httpget = new HttpGet(link);
		try {

			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				OutputStream output = new FileOutputStream("results/" + AdapterFactory.query+"/"+server+".html");
				entity.writeTo(output);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Parser parser = new Parser(null,
				"https://studyat.anu.edu.au/search?search_terms=TEST", null, 0);
		parser.parse();

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.parse();
	}

}
