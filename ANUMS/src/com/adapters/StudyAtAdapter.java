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
import com.results.StudyAtResult;
import com.util.DocumentSet;

public class StudyAtAdapter extends Adapter {

	public StudyAtAdapter(CountDownLatch countDownLatch, Document document,
			ResultTable results, HashMap<Integer, Server> serverTable,
			String hostUrl, int source) {
		super(countDownLatch, document, results, serverTable, hostUrl, source);
		// TODO Auto-generated constructor stub
	}

	@Override
	public RankList query() {
		if (document == null)
			return null;

		// TODO Auto-generated method stub
		RankList ranklist = new RankList();
		Pattern pattern = Pattern.compile("\\d+\\s+matches");
		Node body;
		Matcher matcher = null;
		try {
			body = (Node) xpath.evaluate("//BODY", document,
					XPathConstants.NODE);
			if (body != null)
				matcher = pattern.matcher(body.getTextContent());
		} catch (XPathExpressionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int size = 0;
		while (matcher.find()) {
			String match = matcher.group();
			size += Integer.parseInt(match.substring(0,
					match.indexOf("matches")).trim());
		}
		Server server = new Server();
		server.setServer(source);
		server.setResult_size(size);
		sTable.put(source, server);

		try {
			NodeList nodeList = (NodeList) xpath.evaluate(
					"//DIV[@class=\"search_result_set\"]", document,
					XPathConstants.NODESET);
			int length = nodeList.getLength();
			// set returned Size
			// int reNum=0;
			for (int i = 0; i < length; i++) {
				Element Node_Li = (Element) nodeList.item(i);
				Node Area = (Node) xpath.evaluate("H3", Node_Li,
						XPathConstants.NODE);
				String area = Area.getTextContent().trim();
				NodeList ResultLink = (NodeList) xpath.evaluate(
						"DIV/P[@class=\"result_link\"]/B/A", Node_Li,
						XPathConstants.NODESET);
				for (int j = 0; j < ResultLink.getLength(); j++) {
					Element Link = (Element) ResultLink.item(j);
					String title = Link.getTextContent().trim();
					String link =Link.getAttribute("href").trim();
					if(!link.startsWith("http"))
						link="http://studyat.anu.edu.au"
							+ link;
					StudyAtResult result = new StudyAtResult();
					// reNum++;
					if (!DocumentSet.contains(link)) {
						result.setTitle(title);
						result.setLink(link);
						result.setCategory(area);
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
		return ranklist;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

}
