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
import com.results.WebResult;
import com.util.DocumentSet;

public class WebAdapter extends Adapter {

	public WebAdapter(CountDownLatch countDownLatch, Document document,
			ResultTable results, HashMap<Integer, Server> serverTable,
			String hostUrl, int source) {
		super(countDownLatch, document, results, serverTable, hostUrl, source);
		// TODO Auto-generated constructor stub
	}

	public RankList query() {
		if (document == null)
			return null;
		// transform string
		RankList ranklist = new RankList();
		Pattern pattern = Pattern.compile("\\S+\\s+search results");
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
			System.out.println("webtrue");
			String match = matcher.group();
			match = match.replaceAll(",", "");
			size = Integer.parseInt(match.substring(0,
					match.indexOf("search results")).trim());
		}
		Server server = new Server();
		server.setServer(source);
		server.setResult_size(size);
		sTable.put(source, server);
		try {
			NodeList nodeList = (NodeList) xpath.evaluate(
					"//OL[@id=\"fb-results\"]/LI", document,
					XPathConstants.NODESET);
			int length = nodeList.getLength();
			// no more than 10 results returned
			for (int i = 0; i < length; i++) {
				Element Node_Li = (Element) nodeList.item(i);
				Node Title = (Node) xpath.evaluate("H3", Node_Li,
						XPathConstants.NODE);
				Node Summary = (Node) xpath.evaluate(
						"P/SPAN[@class=\"fb-result-summary\"]", Node_Li,
						XPathConstants.NODE);
				Node Link = (Node) xpath.evaluate("P/CITE", Node_Li,
						XPathConstants.NODE);
				if (Title != null) {
					WebResult result = new WebResult();
					String link = Link.getTextContent().trim();
					if(!link.startsWith("http"))
						link="http://" + link;
					if (!DocumentSet.contains(link)) {
						result.setLink(link);
						result.setTitle(Title.getTextContent().trim());
						// System.out.println(Title.getTextContent().trim());
						result.setSummary(Summary.getTextContent().trim());
						result.setSource(source);
						result.setDsumary();
						ranklist.addResult(result);
					}
					DocumentSet.AddDocument(link);
					if (ranklist.getList().size() >= 10)
						break;
				}
			}

		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
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

}
