package com.util;

import java.io.File;
import java.util.ArrayList;

public class FileUtility {

	public FileUtility() {
		// TODO Auto-generated constructor stub
	}

	public static void createDirectory(String dir) {
		File file = new File(dir);
		if (!file.exists()) {
			if (file.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		}
	}

	public static void clearUpDirectory(String dir) {
		File file=new File(dir);
		if(file.exists())
		{
		File[] files = file.listFiles();
		
			for (File cfile : files) {
				if(cfile.isDirectory())
				{
					clearUpDirectory(cfile.getPath());
				}
				cfile.delete();
				System.out.println("file:"+cfile.getPath()+" deleted...");
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileUtility.clearUpDirectory("Documents");
		FileUtility.clearUpDirectory("pages");
		FileUtility.clearUpDirectory("resultpool");
		FileUtility.clearUpDirectory("Server");
	}

}
