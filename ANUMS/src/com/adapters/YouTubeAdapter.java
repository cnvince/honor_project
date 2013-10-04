package com.adapters;

//no need to detect duplication
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.resultpool.RankList;
import com.resultpool.ResultTable;
import com.resultpool.Server;
import com.results.YoutubeResult;

public class YouTubeAdapter extends Adapter {

	public YouTubeAdapter(CountDownLatch countDownLatch, Document document,
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
		try {
			NodeList nodeList = (NodeList) xpath
					.evaluate(
							"//UL[@id=\"channels-browse-content-list\"]/LI",
							document, XPathConstants.NODESET);
			int length = nodeList.getLength();
			// no more than 10 results returned
			if (length > 10)
				length = 10;
			Server server = new Server();
			server.setServer(source);
			server.setResult_size(length);
			sTable.put(source, server);
			for (int i = 0; i < length; i++) {
				Element Node_SPAN = (Element) nodeList.item(i);
				Element TitleNode=(Element) xpath.evaluate(
						"DIV/H3[@class=\"yt-lockup-title\"]/A", Node_SPAN,
						XPathConstants.NODE);
				String Title = TitleNode.getAttribute("title");
//				System.out.println("Title:"+Title);
				String Link = hostUrl+TitleNode.getAttribute("href");
//				System.out.println("Link:"+Link);
				
				Element Summary = (Element) xpath.evaluate(
						"DIV/DIV[not(@class=\"yt-lockup-meta\")]", Node_SPAN,
						XPathConstants.NODE);
				String summary="";
				if(Summary!=null)
					summary=Summary.getTextContent().trim();
//				Node IMG = (Node) xpath.evaluate(
//						"A//SPAN[@class=\"yt-thumb-clip-inner\"]//IMG",
//						Node_SPAN, XPathConstants.NODE);
//				String imgLink = "";
//				if (IMG != null) {
//					imgLink = ((Element) IMG).getAttribute("src");
//					imgLink = imgLink.replaceAll("//", "");
//				}
//				Node VIEWCOUNT = (Node) xpath
//						.evaluate(
//								"DIV/DIV[@class=\"yt-lockup-meta\"]",
//								Node_SPAN, XPathConstants.NODE);
////				String count = VIEWCOUNT.getTextContent();
//				count = count.substring(0, count.indexOf("views") - 1).trim();
//				Node TIME = (Node) xpath
//						.evaluate(
//								"SPAN[@class=\"content-item-detail\"]//SPAN[@class=\"content-item-time-created\"]",
//								Node_SPAN, XPathConstants.NODE);
//				String time = TIME.getTextContent();
				YoutubeResult result = new YoutubeResult();
				result.setTitle(Title);
//				System.out.println(Title);
//				result.setImgLink("http://" + imgLink);
				result.setLink(Link);
				result.setSummary(summary);
//				result.setTime(time);
//				 result.setViewCount(Integer.parseInt(count.replaceAll("", "")));
				result.setSource(source);
				result.setDsumary();
				ranklist.addResult(result);
			}

		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ranklist;
	}

}
