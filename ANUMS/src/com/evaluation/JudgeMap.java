package com.evaluation;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

//store query and it score map
public class JudgeMap {

	private final String resultPath = "Evaluation/UserJudge/djudges.xml";
	private HashMap<String, ScoreMap> map;
	public JudgeMap() {
		map=new HashMap<String,ScoreMap>();
		initialMap();
	}
	public int getSize()
	{
		return map.size();
	}
//	initial the map from the User Judge File
	private void initialMap()
	{
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
			
			Document doc = docBuilder.parse(new File(resultPath));
			if (doc != null) {
				// get the each query of the result list
				NodeList list = doc.getElementsByTagName("Query");
				int length = list.getLength();
				
				for (int i = 0; i < length; i++) {
					Element Equery = (Element) list.item(i);
					// get the query term
					String query = Equery.getAttribute("value");
//					initial ScoreMap of the query
					if(query!=null&&!query.trim().equals(""))
					{
						ScoreMap scoremap=new ScoreMap();
//						get the doc nodes
						NodeList docs=Equery.getElementsByTagName("doc");
						for(int j=0;j<docs.getLength();j++)
						{
							Node Ndoc=docs.item(j);
							Element Edoc=(Element)Ndoc;
							String id=Edoc.getAttribute("id");
							double score=Double.parseDouble(Edoc.getAttribute("score"));
							scoremap.add(id, score);
						}
//						put the query and the scoremap into the result map
						map.put(query, scoremap);
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
//	return entryset
	public Set<Entry<String,ScoreMap>> getEntry()
	{
		return map.entrySet();
	}
//	check if a query has been assessed
	public boolean contains(String query)
	{
		if(map.containsKey(query))
			return true;
		return false;
	}
//	test
	public void printMap()
	{
		for(Map.Entry<String, ScoreMap> me:map.entrySet())
		{
			System.out.println("=============================================");
			String query=me.getKey();
			System.out.println(query);
			ScoreMap scoremap=me.getValue();
			for(Map.Entry<String, Double> docs:scoremap.getEntry())
			{
				String id=docs.getKey();
				double score=docs.getValue();
				System.out.println("       Doc: "+id+"   Score: "+score);
			}
		}
	}
//	return a particular score map for a query
	public ScoreMap getScoreMap(String query)
	{
		return map.get(query);
	}
	public static void main(String[] args)
	{
		JudgeMap results = new JudgeMap();
		results.printMap();
	}
	
}
