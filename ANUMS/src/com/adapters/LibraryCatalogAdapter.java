package com.adapters;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.resultpool.RankList;
import com.resultpool.ResultTable;
import com.resultpool.Server;
import com.results.LibcataResult;
import com.util.DocumentSet;

public class LibraryCatalogAdapter extends Adapter {

	public LibraryCatalogAdapter(CountDownLatch countDownLatch,
			Document document, ResultTable results,
			HashMap<Integer, Server> serverTable, String hostUrl, int source) {
		super(countDownLatch, document, results, serverTable, hostUrl, source);
		// TODO Auto-generated constructor stub
	}

	@Override
	public RankList query() {
		// TODO Auto-generated method stub
		if (document == null)
			return null;
		// get the size of retrieved documents
		Pattern pattern = Pattern.compile("\\d+\\s+(results|result) found");
		Node body;
		Matcher matcher = null;
		try {
			body = (Node) xpath.evaluate("//BODY", document,
					XPathConstants.NODE);
			matcher = pattern.matcher(body.getTextContent());
		} catch (XPathExpressionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int size = 0;
		while (matcher.find()) {
			String match = matcher.group();
			size = Integer.parseInt(match.substring(0, match.indexOf("result"))
					.trim());
		}
		Server server = new Server();
		server.setServer(source);
		server.setResult_size(size);
		sTable.put(source, server);
		RankList ranklist = new RankList();
		try {
			NodeList nodeList = (NodeList) xpath.evaluate(
					"//TD[@class=\"briefCitRow\"]", document,
					XPathConstants.NODESET);
			int length = nodeList.getLength();
			// no more than 10 results returned
			int resultsize = 0;
			for (int i = 0; i < length; i++) {
				Element ROW = (Element) nodeList.item(i);
				LibcataResult result = new LibcataResult();
				// System.out.println("=================================================");
				Node Title = (Node) xpath.evaluate(
						"TABLE//SPAN[@class=\"briefcitTitle\"]/A", ROW,
						XPathConstants.NODE);
				if(Title==null)
					continue;
				String title = Title.getTextContent().trim();
				result.setTitle(title);
				String Link = hostUrl
						+ ((Element) Title).getAttribute("href").trim();
				Node Summary = (Node) xpath.evaluate(
						"TABLE//SPAN[@class=\"briefcitTitle\"]", ROW,
						XPathConstants.NODE);
				String textarea = Summary.getTextContent().trim();
				String summary = textarea.substring(title.length() + 1).trim();
				if (!DocumentSet.contains(Link)) {
					result.setLink(Link);
					result.setSummary(summary);
					result.setSource(source);
					result.setDsumary();
					ranklist.addResult(result);
					resultsize++;
				}
				DocumentSet.AddDocument(Link);
				if (resultsize >= 10)
					break;
			}

		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ranklist;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

	public void display() {
		System.out.println(this.source + "created...");
		for (int i = 0; i < 10; i++)
			System.out.println(this.source + ":" + i);
	}

}
