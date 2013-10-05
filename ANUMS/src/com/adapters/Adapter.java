//base class of all the adapters
//implements 
package com.adapters;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

import com.resultpool.RankList;
import com.resultpool.ResultTable;
import com.resultpool.Server;

public abstract class Adapter implements Runnable {
	protected int source;
	protected String hostUrl;
	protected Document document;
	protected XPath xpath;
	public ResultTable results;
	public HashMap<Integer,Server> sTable=new HashMap<Integer,Server>();
	protected CountDownLatch countDownLatch;
	public Adapter(CountDownLatch countDownLatch,
			Document document, ResultTable results,
			HashMap<Integer, Server> serverTable,String hostUrl,int source) {
		this.hostUrl=hostUrl;
		this.results=results;
		this.sTable=serverTable;
		setXpath(XPathFactory.newInstance().newXPath());
		this.setDocument(document);
		this.countDownLatch=countDownLatch;
		this.results=results;
		this.sTable=serverTable;
		this.source=source;
	}
	public abstract RankList query(); 
	public void run() {
		results.AddRankList(source, query());
		this.countDownLatch.countDown();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public  String getHostUrl() {
		return hostUrl;
	}
	public void setHostUrl(String hostUrl) {
		this.hostUrl = hostUrl;
	}
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
	public XPath getXpath() {
		return xpath;
	}
	public void setXpath(XPath xpath) {
		this.xpath = xpath;
	}

}
