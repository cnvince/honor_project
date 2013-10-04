package com.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class ResultWritter {

	public static PrintStream ps;
	public static String output_file="docs/testResults.txt";
	
	public ResultWritter() {
		// TODO Auto-generated constructor stub
		if(ps==null)
			try {
				ps = new PrintStream(new FileOutputStream(output_file));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	public void write(Object str)
	{
		ps.println(str);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
