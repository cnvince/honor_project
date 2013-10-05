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

public class ContactResult extends Result implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1897620677864216133L;
	private String Position="";
	private String Phone="";
	private String Email="";
	private String Address="";
	private String Summary="";
	public void setDsumary()
	{
		this.displaySummary=Position+" "+Phone+" "+Email+" "+Address+" "+Summary;
	}
	public String getPosition() {
		return Position;
	}

	public ContactResult(String position, String phone, String email,
			String address, String summary) {
		super();
		Position = position;
		Phone = phone;
		Email = email;
		Address = address;
		Summary = summary;
	}

	public void setPosition(String position) {
		Position = position;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getSummary() {
		return Summary;
	}

	public void setSummary(String summary) {
		Summary = summary;
	}

	public ContactResult() {
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
//		Element ePosition=doc.createElement("Position");
//		ePosition.appendChild(doc.createTextNode(Position==null?"":Position));
//		result.appendChild(ePosition);
//		Element ePhone=doc.createElement("Phone");
//		ePhone.appendChild(doc.createTextNode(Phone==null?"":Phone));
//		result.appendChild(ePhone);
//		Element eEmail=doc.createElement("Email");
//		eEmail.appendChild(doc.createTextNode(Email==null?"":Email));
//		result.appendChild(eEmail);
//		Element eAddress=doc.createElement("Adress");
//		eAddress.appendChild(doc.createTextNode(Address==null?"":Address));
//		result.appendChild(eAddress);
//		Element eSummary=doc.createElement("Summary");
//		eSummary.appendChild(doc.createTextNode(Summary==null?"":Summary));
//		result.appendChild(eSummary);
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
