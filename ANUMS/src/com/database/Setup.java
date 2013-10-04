package com.database;

import java.beans.XMLEncoder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;





import com.broker.Controller;
import com.datatype.ALGORITHM;
import com.results.Result;
import com.util.FileFinder;
import com.util.FileUtility;
import com.util.InsecureHttpClientFactory;
import com.util.ReadtoObject;

public class Setup {
	public Setup() {
		// TODO Auto-generated constructor stub
	}

	public void setupdb() {
		Datasource ds = new Datasource();
		ds.reset();
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
				.newInstance();
		Connector.Connect();
		Connection conn = Connector.getConnection();
		ArrayList<File> files = FileFinder.GetAllFiles("resultpool", ".xml",
				false);
		for (File file : files) {
			String query = file.getName().replaceAll(".xml", "");
			PreparedStatement statement = null;
			// initial query
			String Sql_query = "INSERT INTO QUERIES (Q_TERM,ASSESSED) VALUES (? ,0)";
			try {
				statement = conn.prepareStatement(Sql_query);
				statement.setString(1, query);
				statement.executeUpdate();
				// TODO Auto-generated catch block
				DocumentBuilder docBuilder = docBuilderFactory
						.newDocumentBuilder();

				Document doc = docBuilder.parse(file);
				doc.getDocumentElement().normalize();
				NodeList docs = doc.getElementsByTagName("Doc");

				int length = docs.getLength();
				System.out.println(length);
				for (int i = 0; i < length; i++) {
					Element docid = (Element)docs.item(i);
					String docNum = docid.getAttribute("ID");
					if (docNum.equals(""))
						continue;
					String docPath = "Documents/" + docNum + ".xml";
					Result result = ReadtoObject.ReadtoResult(docPath);
					String Sql_docs = "INSERT INTO QUERY_DOCUMENT (Q_TERM, TITLE,DOCID,SOURCE) VALUES (?,?,?,?)";
					statement = conn.prepareStatement(Sql_docs);
					statement.setString(1, query);
					String title = result.getTitle();
					if (title.length() > 50)
						title = title.substring(0, 50);
					statement.setString(2, title);
					statement.setString(3, docNum);
					statement.setString(4, result.getSourceName());
					statement.executeUpdate();
					statement.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void setupdocs() {
		clearUpAllFolders();
		String fileName = "Queries/queries.txt";
		// read from file
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(fileName));
			String cquery;
			while ((cquery = reader.readLine()) != null) {
				String query = cquery.trim();
				ArrayList<Result> results = new ArrayList<Result>();
				Controller controller = new Controller();
				results = controller.fetchResult(query, ALGORITHM.JUDGE);
				for (Result result : results) {
					String link = result.getLink();
					String docID = result.getDocID();
					String docsPath = "Documents/" + docID + ".xml";
					FileOutputStream os = new FileOutputStream(docsPath);
					XMLEncoder encoder = new XMLEncoder(os);
					encoder.writeObject(result);
					encoder.close();
					downlaodPage(link, docID);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void downlaodPage(String link, String num) {
		System.out.println("Downloading page:"+link);
		InsecureHttpClientFactory httpclientfactory=new InsecureHttpClientFactory();
		// Create an instance of HttpClient.
		HttpClient httpclient = httpclientfactory.buildHttpClient();
		HttpGet httpget = new HttpGet(link);
		try {

			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				OutputStream output = new FileOutputStream("pages/" + num
						+ ".html");
				entity.writeTo(output);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void clearUpAllFolders()
	{
		FileUtility.clearUpDirectory("results");
		FileUtility.clearUpDirectory("Documents");
		FileUtility.clearUpDirectory("pages");
		FileUtility.clearUpDirectory("resultpool");
		FileUtility.clearUpDirectory("server");
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Setup setup = new Setup();
		 setup.setupdb();
//		setup.setupdocs();
//		 setup.setupdb();
//		setup.downlaodPage("https://dragonlair.anu.edu.au/aggregator?page=2", 1);

	}

}
