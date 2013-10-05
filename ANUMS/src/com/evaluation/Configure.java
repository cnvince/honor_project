package com.evaluation;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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

import com.database.Connector;
import com.datatype.ALGORITHM;
import com.mergingalgorithms.AGdsMerger;
import com.mergingalgorithms.GdsMerger;
import com.mergingalgorithms.JMerger;
import com.mergingalgorithms.LMS;
import com.mergingalgorithms.LRIMerger;
import com.mergingalgorithms.Merger;
import com.mergingalgorithms.MultiWeightMerger;
import com.mergingalgorithms.OGdsMerger;
import com.mergingalgorithms.OPRRMerger;
import com.mergingalgorithms.PRRMerger;
import com.mergingalgorithms.SPRRMerger;
import com.mergingalgorithms.Scoring;
import com.mergingalgorithms.SimpleRRMerger;
import com.results.Result;
import com.results.ResultReader;

public class Configure {
	// define GDS field type
	// private final int TS = 0;
	// private final int SS = 1;
	// private final int TSS = 2;
	private double[] paras = new double[4];

	public Configure() {
		// TODO Auto-generated constructor stub
		paras=new double[4];
		paras[0]=0.03768;
		paras[1]=0.78512;
		paras[2]=0.47606;
		paras[3]=0.25549;
	}

	public Configure(double[] paras) {
		this.paras = paras;
	}

	// get the result from the web
	public void fetchDataFromWeb() {
		Connector.Connect();
		Connection conn = Connector.getConnection();
		try {
			String sql_select = "SELECT Q_TERM FROM QUERIES WHERE ASSESSED=2;";
			Statement statement = conn.createStatement();
			ResultSet results = statement.executeQuery(sql_select);
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder;
			Document doc = null;
			docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("Results");
			while (results.next()) {
				String query = results.getString("Q_TERM");
				System.out.println("query:" + query);
				Element eQuery = doc.createElement("Query");
				eQuery.setAttribute("value", query);
				String sql_result = "SELECT DOCID,SCORE FROM RESULT WHERE Q_TERM=?;";
				PreparedStatement ps = conn.prepareStatement(sql_result);
				ps.setString(1, query);
				ResultSet doc_results = ps.executeQuery();
				while (doc_results.next()) {
					String docID = doc_results.getString("DOCID");
					int score = doc_results.getInt("SCORE");
					Element eDoc = doc.createElement("doc");
					eDoc.setAttribute("id", docID);
					eDoc.setAttribute("score", Integer.toString(score));
					eQuery.appendChild(eDoc);
				}
				rootElement.appendChild(eQuery);
			}
			doc.appendChild(rootElement);
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(
					"Evaluation/UserJudge/djudges.xml"));
			transformer.transform(source, result);
			System.out.println("File saved!");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	}

	private ArrayList<Result> getMergeResult(int Method, String Query) {
		Merger merger = null;
		switch (Method) {
		// Random Round-Robin
		case ALGORITHM.SRR:
			merger = new SimpleRRMerger();
			break;
		case ALGORITHM.PRR:
			merger = new PRRMerger();
			break;
		case ALGORITHM.GDS_TS:
			merger = new GdsMerger(Query, Scoring.TS);
			break;
		case ALGORITHM.GDS_SS:
			merger = new GdsMerger(Query, Scoring.SS);
			break;
		case ALGORITHM.GDS_TSS:
			merger = new GdsMerger(Query, Scoring.TSS);
			break;
		case ALGORITHM.GDS_DTSS:
			merger = new GdsMerger(Query, Scoring.DTSS);
			break;
		case ALGORITHM.LMS:
			merger = new LMS(Query);
			break;
		case ALGORITHM.JUDGE:
			merger = new JMerger(Query);
			break;
		case ALGORITHM.LOCAL:
			merger = new LRIMerger(Query);
			break;
		case ALGORITHM.SPRR:
			merger = new SPRRMerger(Query);
			break;
		case ALGORITHM.MW:
			merger = new MultiWeightMerger(Query, paras);
			break;
		case ALGORITHM.OPRR:
			merger = new OPRRMerger(Query);
			break;
		case ALGORITHM.OGDS:
			merger = new OGdsMerger(Query);
			break;
		case ALGORITHM.SGDS:
			merger = new AGdsMerger(Query);
			break;
		default:
			break;
		}

		return merger.executeMerging(ResultReader.getResultTable(Query),
				ResultReader.getServerInfo(Query));
	}

	public void outputResult(int Method, ArrayList<String> queries) {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = null;
		Document doc = null;
		try {
			docBuilder = docFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("Results");
		for (String cquery : queries) {
			// read from file
			String query = cquery.trim();
			System.out.println("********query:" + query + "**********");
			Element resultElement = doc.createElement("Result");
			resultElement.setAttribute("Query", query);
			ArrayList<Result> results = getMergeResult(Method, query);
			for (Result result : results) {
				Element docElement = doc.createElement("Doc");
				docElement.setAttribute("ID", result.getDocID());
				resultElement.appendChild(docElement);
			}
			rootElement.appendChild(resultElement);
		}
		doc.appendChild(rootElement);
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(
					"Evaluation/AlgResult/" + Method + ".xml"));
			transformer.transform(source, result);
			System.out.println("File saved!");
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public float evluate() {
		return 0;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Configure evaluator=new Configure();
		// evaluator.fetchDataFromWeb();
		// evaluator.outputResult(ALGORITHM.SRR);
		// evaluator.outputResult(ALGORITHM.PRR);
		// evaluator.outputResult(ALGORITHM.LMS);
		// evaluator.outputResult(ALGORITHM.GDS_SS);
		// evaluator.outputResult(ALGORITHM.GDS_TS);
		// evaluator.outputResult(ALGORITHM.GDS_TSS);
	}

}
