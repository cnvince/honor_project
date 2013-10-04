package com.results;

import java.io.Serializable;

public class MapResult extends Result implements Serializable{

	public MapResult(String number, String summary, String acronym) {
		super();
		Number = number;
		Summary = summary;
		Acronym = acronym;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 2664639395826233074L;
	private String Number;
	private String Summary;
	private String Acronym;
	
	public void setDsumary()
	{
		this.displaySummary=Number+" "+Summary+" "+Acronym;
	}
	public String getNumber() {
		return Number;
	}

	public void setNumber(String number) {
		Number = number;
	}

	public String getSummary() {
		return Summary;
	}

	public void setSummary(String summary) {
		Summary = summary;
	}

	public String getAcronym() {
		return Acronym;
	}

	public void setAcronym(String acronym) {
		Acronym = acronym;
	}

	public MapResult() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
