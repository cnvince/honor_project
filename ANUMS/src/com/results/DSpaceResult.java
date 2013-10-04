package com.results;

import java.io.Serializable;

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

public class DSpaceResult extends Result implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6347942517328012722L;
	private String date="";
	private String author="";
	
	public DSpaceResult(String date, String imgLink, String author) {
		super();
		this.date = date;
		this.imgLink = imgLink;
		this.author = author;
	}
	public void setDsumary()
	{
		this.displaySummary=date+" "+author;
	}
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}


	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public DSpaceResult() {
		// TODO Auto-generated constructor stub
	}

//	@Override
//	public void store(Document doc) {
//		String query=AdapterFactory.query;
//		Element root = doc.getDocumentElement();
//		Element result=doc.createElement("result");
//		result.setAttribute("Source", Integer.toString(this.Source));
//		result.setAttribute("Url",this.Link);
//		Element eTitle=doc.createElement("Title");
//		eTitle.appendChild(doc.createTextNode(Title==null?"":Title));
//		Element eDate=doc.createElement("date");
//		eDate.appendChild(doc.createTextNode(date==null?"":date));
//		result.appendChild(eDate);
//		Element eAuthor=doc.createElement("Author");
//		eAuthor.appendChild(doc.createTextNode(author==null?"":author));
//		result.appendChild(eAuthor);
//		Element escore=doc.createElement("score");
//		escore.appendChild(doc.createTextNode(Double.toString(this.score)));
//		root.appendChild(result);
//		DOMSource source = new DOMSource(doc);
//
//        TransformerFactory transformerFactory = TransformerFactory.newInstance();
//        Transformer transformer;
//		try {
//			transformer = transformerFactory.newTransformer();
//			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//			StreamResult sresult = new StreamResult("resultpool/"+query+".xml");
//			transformer.transform(source, sresult);
//		} catch (TransformerConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (TransformerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
