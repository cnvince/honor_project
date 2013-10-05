package com.results;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.resultpool.RankList;
import com.resultpool.ResultTable;
import com.resultpool.Server;
import com.util.FileFinder;
import com.util.ReadtoObject;

public class ResultReader {

	public ResultReader() {
		// TODO Auto-generated constructor stub
	}
	public static HashMap<Integer,Server> getServerInfo(String query)
	{
		HashMap<Integer, Server> serverinfo=new HashMap<Integer,Server>();
		String serverPath="server/"+query;
		ArrayList<File> files=FileFinder.GetAllFiles(serverPath, ".xml", false);
		for(File file:files)
		{
			String path=file.getPath();
			Server server=ReadtoObject.ReadtoServer(path);
			
			if(server.getResult_size()>0)
				serverinfo.put(server.getServer(), server);
		}
		return serverinfo;
	}
//	initial ResultTable
	public static ResultTable getResultTable(String query)
	{
		ResultTable resultTable=new ResultTable();
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
			String resultPath="resultpool/"+query+".xml";
			Document doc = docBuilder.parse (new File(resultPath));
			NodeList lists=doc.getElementsByTagName("ResultList");
			int length=lists.getLength();
			for(int i=0;i<length;i++)
			{
				Element resultlist=(Element)lists.item(i);
				int server=Integer.parseInt(resultlist.getAttribute("Server"));
				NodeList docs=resultlist.getElementsByTagName("Doc");
				int dlength=docs.getLength();
				RankList results=new RankList();
				for(int j=0;j<dlength;j++)
				{
					Element docElement=(Element)docs.item(j);
					String docID=docElement.getAttribute("ID");
					Result result=ReadtoObject.ReadtoResult("Documents/"+docID+".xml",server);
					results.addResult(result);
				}
				resultTable.AddRankList(server, results);
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
		return resultTable;
	}
	
}
