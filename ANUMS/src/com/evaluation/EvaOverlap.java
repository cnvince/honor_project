package com.evaluation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import com.broker.Controller;
import com.datatype.ALGORITHM;
import com.results.Result;
import com.util.DocumentSet;

public class EvaOverlap {

	public EvaOverlap() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String fileName = "Queries/query.txt";
		//read from file
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(fileName));
			String cquery;
			while((cquery = reader.readLine())!= null){
				String query=cquery.trim();
				File file=new File("Documents/"+query+".txt");
				if (!file.exists()) {
					file.createNewFile();
				}
				else
				{
					file.delete();
					file.createNewFile();
				}
				FileWriter fw = new FileWriter(file.getAbsolutePath());
     			BufferedWriter bw = new BufferedWriter(fw);
     			ArrayList<Result> results = new ArrayList<Result>();
    			Controller controller = new Controller();
    			results = controller.fetchResult(query, ALGORITHM.JUDGE);
    			DocumentSet.recordOverlap();
    			long seed = System.nanoTime();
    			Collections.shuffle(results, new Random(seed));
    			for (Result result : results) {
    				String Title=result.getTitle().replaceAll("\\s+", " ").trim();
    				bw.append("[Title]"+Title + "[Link]" + result.getLink()+"[Source]"+result.getSourceName()+"\n");
    			}
    			bw.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
