package com.results;

import java.io.Serializable;

public class ResearcherResult extends Result implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7906716228003849891L;
	private String summary;
	public void setDsumary()
	{
		this.displaySummary=summary;
	}
	public ResearcherResult(String summary) {
		super();
		this.summary = summary;
	}

	public ResearcherResult() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

}
