package com.mergingalgorithms;

import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.adapters.AdapterFactory;
import com.resultpool.RankList;
import com.resultpool.ResultTable;
import com.resultpool.Server;
import com.results.Result;
import com.util.FileUtility;

public class JMerger implements Merger {

	public JMerger() {
		// TODO Auto-generated constructor stub
	}

	public JMerger(String query) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public ArrayList<Result> executeMerging(ResultTable results,
			HashMap<Integer, Server> serverTable) {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		Document doc=null;
		ArrayList<Result> mergedList=new ArrayList<Result>();
		HashMap<Integer, RankList> lists=results.getTable();
		try {
			docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("ResultLists");
			for(Map.Entry<Integer, RankList> entry:lists.entrySet())
			{
				Element listElement=doc.createElement("ResultList");
				int source=entry.getKey();
				listElement.setAttribute("Server", Integer.toString(source));
				RankList ranklist=entry.getValue();
				ArrayList<Result> resultlist=ranklist.getList();
				for(Result result:resultlist)
				{
					Element resultElement=doc.createElement("Doc");
					resultElement.setAttribute("url", result.getLink());
					resultElement.setAttribute("ID", result.getDocID());
					listElement.appendChild(resultElement);
				}
				rootElement.appendChild(listElement);
				mergedList.addAll(entry.getValue().getList());
			}
			doc.appendChild(rootElement);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("resultpool/"+AdapterFactory.query+".xml"));
			transformer.transform(source, result);
			System.out.println("File saved!");
			
			FileUtility.createDirectory("server/"+AdapterFactory.query);
//			store the server information in xml file
			for(Map.Entry<Integer, Server> server:serverTable.entrySet())
			{
				int Server=server.getKey();
				Server servinfo=server.getValue();
				String serverPath = "server/" + AdapterFactory.query+"/"+Server + ".xml";
				FileOutputStream os;
				try {
					os = new FileOutputStream(serverPath);
					XMLEncoder encoder = new XMLEncoder(os);
					encoder.writeObject(servinfo);
					encoder.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return mergedList;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
