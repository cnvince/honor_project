package com.results;

import java.io.Serializable;

public class LibcataResult extends Result implements Serializable{

	public LibcataResult(String summary) {
		super();
		this.summary = summary;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8623372095255896706L;
	private String summary;
	public void setDsumary()
	{
		this.displaySummary=summary;
	}
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public LibcataResult() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
