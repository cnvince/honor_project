package com.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.w3c.dom.Document;

import com.adapters.AdapterFactory;
import com.util.FileUtility;
import com.util.UrlMatcher;

public class ParserFactory{
	private String query;
	//Document Collection to store the parsed documents
	private HashMap<Integer,Document> DocumentCollection;
	private HashMap<Integer,String> UrlCollection;
	
	public ParserFactory(String query) {
		// TODO Auto-generated constructor stub
		this.query=query;
		
		setDocumentCollection(new HashMap<Integer,Document>());
	}
	private void initialUrlCollection()
	{
//		match search url of each server
		UrlCollection=UrlMatcher.match(query);
	}
	public void initialDocuments()
	{
		long start=System.currentTimeMillis();
		FileUtility.createDirectory("results/"+AdapterFactory.query);
		this.initialUrlCollection();
		//special case, twitter is not parsed by xpath
//		DocumentCollection.put(ServerSource.TWITTER, null);
		CountDownLatch countDownLatch = new CountDownLatch(UrlCollection.size());
//		ExecutorService executor = Executors.newSingleThreadExecutor();
		for(Map.Entry<Integer, String> entry:UrlCollection.entrySet())
		{
			String url=entry.getValue();
			Thread t=new Thread(new Parser(countDownLatch,url,DocumentCollection,entry.getKey()));
			t.start();
		}
		try {
			countDownLatch.await();
			long end=System.currentTimeMillis();
			System.out.println("TotalTime:"+(end-start)/1000+"s");
		} catch (InterruptedException e) {
//			 TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long start=System.currentTimeMillis();
		ParserFactory factory=new ParserFactory("Paul");
		factory.initialDocuments();
		long end=System.currentTimeMillis();
		System.out.println("TotalTime:"+(end-start)/1000+"s");
	}
	public HashMap<Integer,Document> getDocumentCollection() {
		return DocumentCollection;
	}
	public void setDocumentCollection(HashMap<Integer,Document> documentCollection) {
		DocumentCollection = documentCollection;
	}

}
