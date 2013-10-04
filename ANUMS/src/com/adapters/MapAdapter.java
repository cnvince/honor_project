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
import com.results.MapResult;
import com.util.DocumentSet;

public class MapAdapter extends Adapter {

	public MapAdapter(CountDownLatch countDownLatch, Document document,
			ResultTable results, HashMap<Integer, Server> serverTable,
			String hostUrl, int source) {
		super(countDownLatch, document, results, serverTable, hostUrl, source);
		// TODO Auto-generated constructor stub
	}

	@Override
	public RankList query() {
		if (document == null)
			return null;
		RankList ranklist = new RankList();
		// set retrieved document size
		Pattern pattern = Pattern.compile("\\d+\\s+results");
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
			size = Integer.parseInt(match
					.substring(0, match.indexOf("results")).trim());
		}
		Server server = new Server();
		server.setServer(source);
		server.setResult_size(size);
		sTable.put(source, server);
		try {
			NodeList nodeList = (NodeList) xpath.evaluate(
					"//TABLE[@class=\"tbdr\"]//TR", document,
					XPathConstants.NODESET);
			int length = nodeList.getLength();
			// no more than 10 results returned
			int resultsize = 10;
			// System.out.println(source + "analysis");
			for (int i = 0; i < length; i++) {
				Element TR = (Element) nodeList.item(i);
				NodeList TD = (NodeList) xpath.evaluate("TD[@class=\"bdr\"]",
						TR, XPathConstants.NODESET);
				if (TD != null && TD.getLength() == 4) {
					MapResult mapResult = new MapResult();
					Element Number = (Element) TD.item(0);
					Element Name = (Element) TD.item(1);
					Element Summary = (Element) TD.item(2);
					Element Acronym = (Element) TD.item(3);
					String number = Number.getTextContent().trim();
					String link = hostUrl + "/"
							+ Number.getAttribute("href").trim();
					String name = Name.getTextContent().trim();
					String summary = Summary.getTextContent().trim();
					String acronym = Acronym.getTextContent().trim();
					if (!DocumentSet.contains(link)) {
						mapResult.setTitle(name);
						mapResult.setLink(link);
						mapResult.setNumber(number);
						mapResult.setAcronym(acronym);
						mapResult.setSummary(summary);
						mapResult.setSource(source);
						mapResult.setDsumary();
						ranklist.addResult(mapResult);
						resultsize++;
					}
					DocumentSet.AddDocument(link);
					if (resultsize >= 10)
						break;
				}
			}

		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// try {
		// Thread.sleep(500);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		return ranklist;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
