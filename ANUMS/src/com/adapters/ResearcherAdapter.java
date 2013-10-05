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
import com.results.ResearcherResult;
import com.util.DocumentSet;

public class ResearcherAdapter extends Adapter {

	public ResearcherAdapter(CountDownLatch countDownLatch, Document document,
			ResultTable results, HashMap<Integer, Server> serverTable,
			String hostUrl, int source) {
		super(countDownLatch, document, results, serverTable, hostUrl, source);
		// TODO Auto-generated constructor stub
	}

	@Override
	public RankList query() {
		if (document == null)
			return null;

		int size=0;
		try {
			Node Page_NODE=(Node) xpath.evaluate("//DIV[@class=\"pagination\"]/A[last()-1]", document, XPathConstants.NODE);
//			System.err.println("=====================\n");
//			System.exit(0);
			if(Page_NODE!=null)
			{
				String resultPage=Page_NODE.getTextContent().trim();
				size=Integer.parseInt(resultPage)*10;
//				System.err.println("************************\n"+Page_NODE.getTextContent().trim());
////				System.out.println("*********nodes*************\n"+LI_NODES.getLength());
//				for(int i=0;i<LI_NODES.getLength();i++)
//				{
//					Node node=LI_NODES.item(i);
//					String sepResult=node.getTextContent().trim();
////					System.err.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$\n");
////					System.err.println(sepResult);
////					System.err.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$\n");
//					Pattern myPattern = Pattern.compile("(\\d+)");
//					Matcher matcher=myPattern.matcher(sepResult);
//					while(matcher.find())
//					{
//						
//						String psize=sepResult.substring(sepResult.indexOf("(")+1, sepResult.indexOf(")"));
////						System.err.println("=====================single size:\n"+psize);
//						if(!psize.trim().equals(""))
//						{
//						int pnsize=Integer.parseInt(psize.trim());
//						size+=pnsize;
//						}
//					}
//				}
////				System.out.println("**********************\n"+size);
			}
			else
			{
				size=10;
			}
		} catch (XPathExpressionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Server server = new Server();
		server.setServer(source);
		server.setResult_size(size);
		sTable.put(source, server);
		// TODO Auto-generated method stub
		RankList ranklist = new RankList();
		try {
			NodeList nodeList = (NodeList) xpath.evaluate(
					"//UL[@class=\"search_results\"]//DIV[@class=\"content\"]",
					document, XPathConstants.NODESET);
			int length = nodeList.getLength();
			// no more than 10 results returned
			int resultsize = 0;
			for (int i = 0; i < length; i++) {
				// System.out.println("running " + source + "....");
				Node Content = nodeList.item(i);
				Node Title = (Node) xpath.evaluate("H3/A", Content,
						XPathConstants.NODE);
				String title = Title.getTextContent().trim();
				String link = hostUrl
						+ ((Element) Title).getAttribute("href").trim();
				Node Summary = (Node) xpath.evaluate("UL", Content,
						XPathConstants.NODE);
				String summary = Summary.getTextContent().trim();
				ResearcherResult result = new ResearcherResult();
				if (!DocumentSet.contains(link)) {
					result.setLink(link);
					result.setSource(source);
					result.setSummary(summary);
					result.setTitle(title);
					result.setDsumary();
					ranklist.addResult(result);
					resultsize++;
				}
				DocumentSet.AddDocument(link);
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
		// TODO Auto-generated method stub

		// ResearcherAdapter adapter=new
		// ResearcherAdapter(ServerSource.RES_PROJECTS);
		// adapter.query("paul");
	}

}
