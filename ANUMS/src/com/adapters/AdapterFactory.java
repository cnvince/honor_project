package com.adapters;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.w3c.dom.Document;

import com.datatype.ServerSource;
import com.parser.ParserFactory;
import com.resultpool.ResultTable;
import com.resultpool.Server;
import com.util.DocumentSet;
import com.util.StringFormat;

public class AdapterFactory {

	public ResultTable results = new ResultTable();
	public static String query=null;
	//a table describing the server information
	public HashMap<Integer, Server> ServerTable = new HashMap<Integer, Server>();
//	query for public use

	public ResearcherAdapter getRes_proAdapter() {
		return res_proAdapter;
	}

	public void setRes_proAdapter(ResearcherAdapter res_proAdapter) {
		this.res_proAdapter = res_proAdapter;
	}

	private ResearcherAdapter res_proAdapter;
	public AdapterFactory() {
		// TODO Auto-generated constructor stub
	}
	// initial instances of adapters
	public void allocateAdapters(String query) {
		this.query=query;
		query = StringFormat.toURL(query);
		ParserFactory parserFactory = new ParserFactory(query);
		parserFactory.initialDocuments();
		//reset DocumentSet
		DocumentSet.reset();
		HashMap<Integer, Document> documents = parserFactory
				.getDocumentCollection();
		CountDownLatch countDownLatch = new CountDownLatch(documents.size());
		for (Map.Entry<Integer, Document> entry : documents.entrySet()) {
			int server = entry.getKey();
			Document document=entry.getValue();
			Thread t=null;
			switch (server) {
			case ServerSource.CONTACT:
				t = new Thread(new ContactAdapter(countDownLatch,document, results, ServerTable,"http://www.anu.edu.au/dirs",server));
				break;
			case ServerSource.DSPACE:
				t = new Thread(new DspaceAdapter(countDownLatch,document,results, ServerTable,"https://digitalcollections.anu.edu.au",server));
				break;
			case ServerSource.LIBRARY:
				t = new Thread(new LibraryCatalogAdapter(countDownLatch,document, results,
						ServerTable,"http://library.anu.edu.au",server));
				break;
			case ServerSource.MAP:
				t = new Thread(new MapAdapter(countDownLatch,document, results, ServerTable,"http://campusmap.anu.edu.au",server));
				break;
			case ServerSource.RES_PROJECTS:
				t = new Thread(new ResearcherAdapter(countDownLatch,document, results, ServerTable,"https://researchers.anu.edu.au",server));
				break;
			case ServerSource.RES_PUBLICATIONS:
				t = new Thread(new ResearcherAdapter(countDownLatch,document, results, ServerTable,"https://researchers.anu.edu.au",server));
				break;
			case ServerSource.RESEARCHERS:
				t = new Thread(new ResearcherAdapter(countDownLatch,document, results, ServerTable,"https://researchers.anu.edu.au",server));
				break;
			case ServerSource.STUDYAT:
				t = new Thread(new StudyAtAdapter(countDownLatch,document, results, ServerTable,"http://studyat.anu.edu.au",server));
				break;
			case ServerSource.TWITTER:
				t = new Thread(new TwitterAdapter(countDownLatch,document, results, ServerTable,"https://twitter.com",server));
				break;
			case ServerSource.WEB:
				t = new Thread(new WebAdapter(countDownLatch,document, results, ServerTable,"http://search.anu.edu.au/search/",server));
				break;
			case ServerSource.YOUTUBE:
				t = new Thread(new YouTubeAdapter(countDownLatch,document, results, ServerTable,"http://www.youtube.com",server));
				break;
			default:
				break;
			}
			t.start();
		}
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ResultTable executeQuery(String query) {
		allocateAdapters(query);
		return results;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AdapterFactory factory = new AdapterFactory();
		factory.executeQuery("paul");
	}

}
