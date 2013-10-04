package com.evaluation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ResultMap {

	private int method;
	private String path;
	private HashMap<String, Object> map;

	public ResultMap(int Method) {
		method = Method;
		path = "Evaluation/AlgResult/" + method + ".xml";
		map = new HashMap<String, Object>();
		initialMap();
	}
	public boolean contains(String query)
	{
		if(map.containsKey(query))
			return true;
		return false;
	}
	public int getSize()
	{
		return map.size();
	}

	// initial the result map for the method
	private void initialMap() {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();

			Document doc = docBuilder.parse(new File(path));
			if (doc != null) {
				// get the each query of the result list
				NodeList list = doc.getElementsByTagName("Result");
				int length = list.getLength();

				for (int i = 0; i < length; i++) {
					Element Equery = (Element) list.item(i);
					// get the query term
					String query = Equery.getAttribute("Query");
					// initial ScoreMap of the query
					if (query != null && !query.trim().equals("")) {
							NodeList docs = Equery.getElementsByTagName("Doc");
							ArrayList<String> ranking=new ArrayList<String>();
							for(int j=0;j<docs.getLength();j++)
							{
								Element Edoc = (Element) docs.item(j);
								String id = Edoc.getAttribute("ID");
								ranking.add(id);
							}
							map.put(query, ranking);
						}
					}
				}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ArrayList<String> getRanking(String id)
	{
		return (ArrayList<String>) map.get(id);
	}
	public Set<Entry<String, Object>> getEntry()
	{
		return map.entrySet();
	}
	public void printMap()
	{
		for(Map.Entry<String, Object> me:map.entrySet())
		{
			String id=me.getKey();
			ArrayList<String> list=(ArrayList<String>) me.getValue();
			System.out.println("=====================================");
			System.out.println("Query: "+id);
			for(String doc:list)
				System.out.println("    Doc: "+ list);
		}
	}
	public static void main(String[] args)
	{
//		ResultMap resultMap=new ResultMap(0);
//		resultMap.printMap();
		double a=0;
		double b=2;
		System.out.println(a/b);
	}

}
