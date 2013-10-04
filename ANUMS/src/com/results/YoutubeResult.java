package com.results;

import java.io.Serializable;

public class YoutubeResult extends Result implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1496807912431056123L;
	private String summary;
	private int viewCount;
	private String Time;
	public void setDsumary()
	{
		this.displaySummary=summary+" viewcount:"+viewCount+" Time:"+Time;
	}
	public YoutubeResult(String summary, int viewCount,
			String time) {
		super();
		this.summary = summary;
		this.viewCount = viewCount;
		Time = time;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public String getTime() {
		return Time;
	}

	public void setTime(String time) {
		Time = time;
	}

	public YoutubeResult() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
