package com.evaluation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.util.FileFinder;

public class ExamDocs {
	public void docDistribution()
	{
		File servers=new File("server");
		if(!servers.exists())
		{
			System.err.println("server file not exiist");
			System.exit(0);
		}
		File[] files=servers.listFiles();
		String content="% !TEX TS-program = latex\n";
		content=content+"\\documentclass[5pt]{article}\n";
		content=content+"\\usepackage{lscape}\n";
		content=content+"\\begin{document}\n";
		content=content+"\\begin{table}\n";
		content=content+"\\begin{tabular*}{0.5\\textwidth}{|p{5cm}|l|l|l|l|l|l|l|l|l|l|l|}\n";
		content=content+"\\cline{1-12}\n";
		content=content+"Query	& 0 & 1 & 2 & 3 & 4 & 5 & 6 & 7 & 8 & 9 & 10 \\\\";
		content=content+"\\cline{1-12}\n";
		
		for (File file : files) {
			System.out.println(file.getName());
			if(file.isDirectory())
			{
				System.err.println("true");
			String query = file.getName();
			XPath xpath=XPathFactory.newInstance().newXPath();
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		    domFactory.setNamespaceAware(true); // never forget this!
		    DocumentBuilder builder;
		    
		    content=content+query;
			try {
				builder = domFactory.newDocumentBuilder();
				for(int i=0;i<=10;i++)
				{
					int num=0;
					Document doc = builder.parse(file.getPath()+"/"+i+".xml");
					if(doc!=null)
					{
						Node doc_size=(Node)xpath.evaluate("//void[@property=\"result_size\"]/int", doc, XPathConstants.NODE);
						if(doc_size!=null)
							num=Integer.parseInt(doc_size.getTextContent().trim());
					}
					content=content+" &"+num;
				}
				content=content+"\\\\\n";
				content=content+"\\cline{1-12}\n";
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		}
		content=content+"\\end{tabular*}\n\\end{table}\n\\end{document}\n";
		try {
			System.out.println(content);
			@SuppressWarnings("resource")
			PrintStream ps=new PrintStream(new FileOutputStream("distribution_stat.tex"));
			ps.print(content);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void outputStatistic()
	{
		ArrayList<File> files = FileFinder.GetAllFiles("resultpool", ".xml",
				false);
		String content="% !TEX TS-program = latex\n";
		content=content+"\\documentclass[5pt]{article}\n";
		content=content+"\\usepackage{lscape}\n";
		content=content+"\\begin{document}\n";
		content=content+"\\begin{table}\n";
		content=content+"\\begin{tabular*}{0.5\\textwidth}{|p{5cm}|l|l|l|l|l|l|l|l|l|l|l|}\n";
		content=content+"\\cline{1-12}\n";
		content=content+"Query	& 0 & 1 & 2 & 3 & 4 & 5 & 6 & 7 & 8 & 9 & 10 \\\\";
		content=content+"\\cline{1-12}\n";
		
		for (File file : files) {
			String query = file.getName().replaceAll(".xml", "");
			XPath xpath=XPathFactory.newInstance().newXPath();
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		    domFactory.setNamespaceAware(true); // never forget this!
		    DocumentBuilder builder;
		    
		    content=content+query;
			try {
				builder = domFactory.newDocumentBuilder();
				Document doc = builder.parse(file.getAbsolutePath());
				for(int i=0;i<=10;i++)
				{
					int num=0;
					NodeList docs=(NodeList)xpath.evaluate("//ResultList[@Server="+"\""+i+"\""+"]/Doc", doc, XPathConstants.NODESET);
					if(docs!=null)
						num=docs.getLength();
					content=content+" &"+num;
				}
				content=content+"\\\\\n";
				content=content+"\\cline{1-12}\n";
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		content=content+"\\end{tabular*}\n\\end{table}\n\\end{document}\n";
		try {
			System.out.println(content);
			@SuppressWarnings("resource")
			PrintStream ps=new PrintStream(new FileOutputStream("docs_stat.tex"));
			ps.print(content);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args)
	{
		ExamDocs examdocs=new ExamDocs();
		examdocs.outputStatistic();
		examdocs.docDistribution();
		
	}
}
