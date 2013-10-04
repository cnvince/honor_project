package com.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

//maintain a document set to check if the document is already existed;

public class DocumentSet {
	public static HashMap<String, Integer> documents = new HashMap<String, Integer>();
	public DocumentSet() {
	}

	public static void AddDocument(String url) {
		if (documents.containsKey(url))
			documents.put(url, documents.get(url) + 1);
		else
			documents.put(url, 1);
	}
	public static String gengerateID()
	{
		String uniqueID = UUID.randomUUID().toString();
		return uniqueID;
	}
	// duplication detection
	public static boolean contains(String url) {
		if (documents.containsKey(url)) {
			System.out.println("url:" + url + "duplicated");
			return true;
		}
		return false;
	}

	public static void reset() {
		documents = new HashMap<String, Integer>();
	}

	public static void recordOverlap() {
		// TODO Auto-generated constructor stub
		 String path = "data/doclog.txt";
		 File file=new File(path);
		if(!file.exists())
		{
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		FileWriter fw;
		try {
			fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.append(documents.size()+"\n");
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
