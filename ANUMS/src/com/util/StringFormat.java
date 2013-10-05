package com.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class StringFormat {

	public StringFormat() {
		// TODO Auto-generated constructor stub
	}
	public static String toURL(String query)
	{
		try {
			query=URLEncoder.encode(query,"ISO-8859-1");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return query;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
