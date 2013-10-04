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
import com.results.DSpaceResult;
import com.util.DocumentSet;

public class DspaceAdapter extends Adapter {

	public DspaceAdapter(CountDownLatch countDownLatch, Document document,
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
		// detect retrieved documents size
		Pattern pattern = Pattern.compile("Results \\d+-\\d+ of \\d+.");
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
		// get the size of retrieved documents
		int size = 0;
		while (matcher.find()) {
			String match = matcher.group();
			match = match.substring(match.lastIndexOf(" "), match.indexOf("."))
					.trim();
			// System.out.println("Dspace macthed text:"+match);
			size = Integer.parseInt(match);
		}
		// set server
		Server server = new Server();
		server.setServer(source);
		server.setResult_size(size);
		sTable.put(source, server);
		try {

			NodeList nodeList = (NodeList) xpath.evaluate(
					"//TABLE[@class=\"miscTable\"]//TR", document,
					XPathConstants.NODESET);
			int length = nodeList.getLength();
			// no more than 10 results returned
			int resultsize = 0;
			for (int i = 1; i < length; i++) {
				Element TR = (Element) nodeList.item(i);
				Node T1 = (Node) xpath.evaluate("TD[@headers=\"t1\"]", TR,
						XPathConstants.NODE);
//				System.out.println(T1);
//				Node IMG = (Node) xpath.evaluate("A/IMG", T1,
//						XPathConstants.NODE);
//				String imgLink = "";
//				if (IMG != null)
//					imgLink = hostUrl + ((Element) IMG).getAttribute("src");
//				System.out.println("DSPACE:IMG"+imgLink);
				Node T2 = (Node) xpath.evaluate("TD[@headers=\"t2\"]", TR,
						XPathConstants.NODE);
				if (T2 != null) {
					String date = T2.getTextContent().trim();
					Node T3 = (Node) xpath.evaluate("TD[@headers=\"t3\"]", TR,
							XPathConstants.NODE);
					Node Title=(Node) xpath.evaluate("A", T3,
							XPathConstants.NODE);
					String title = Title.getTextContent().trim();
					String link = hostUrl
							+ ((Element) Title).getAttribute("href");
					Node T4 = (Node) xpath.evaluate("TD[@headers=\"t4\"]", TR,
							XPathConstants.NODE);
					String author = T4.getTextContent().trim();
					DSpaceResult result = new DSpaceResult();
					if (!DocumentSet.contains(link)) {
//						result.setImgLink(imgLink);
						result.setAuthor(author);
						result.setDate(date);
						result.setLink(link);
						System.out.println("Dspace:"+link);
						result.setSource(source);
						result.setTitle(title);
						result.setDsumary();
						ranklist.addResult(result);
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
//		 TODO Auto-generated method stub
//		 DspaceAdapter adapter=new DspaceAdapter(null, null,
//		 null, null, null, ServerSource.DSPACE);
//		 try {
//		 adapter.query("paul");
//		 } catch (XPathExpressionException e) {
//		 // TODO Auto-generated catch block
//		 e.printStackTrace();
//		 }
	}

	public void display() {
		System.out.println(this.source + "created...");
		for (int i = 0; i < 10; i++)
			System.out.println(this.source + ":" + i);
	}

}
