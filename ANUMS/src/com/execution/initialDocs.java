package com.execution;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.servlet.ServletContext;

import com.broker.Controller;
import com.datatype.ALGORITHM;
import com.results.Result;

public class initialDocs {

	public initialDocs() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				String fileName = "Queries/queries.txt";
				//read from file
				BufferedReader reader;
				try {
					reader = new BufferedReader(new FileReader(fileName));
					 String cquery;

				        while((cquery = reader.readLine())!= null){
				            String query=cquery.trim();
				            String filePath="Documents";     
				            File file = new File(filePath, query+".txt");     
				            if (!file.getParentFile().exists()) {  
				                System.out.println(String.format("Creating folder %s...", file.getParentFile().getAbsolutePath()));  
				                if (file.getParentFile().mkdirs())  
				                    System.out.println("Done");  
				                else  
				                    System.out.println("Unable to create folder");  
				            }  
				              
				            if (!file.exists()) {  
				                System.out.println(String.format("Creating file %s...", file.getAbsolutePath()));  
				                if (file.createNewFile())  
				                {
				                    System.out.println("Done");
				                }
				                else  
				                {
				                    System.out.println("Unable to create file");  
				                }
				            } 
				            //if the file is already existed, update
				            else  
				            {
				            	 FileWriter fw = new FileWriter(file.getAbsoluteFile());
				     			BufferedWriter bw = new BufferedWriter(fw);
				     			bw.write("");
				            }
				            FileWriter fw = new FileWriter(file.getAbsoluteFile());
							BufferedWriter bw = new BufferedWriter(fw);
							ArrayList<Result> results = new ArrayList<Result>();
							Controller controller = new Controller();
							results = controller.fetchResult(query, ALGORITHM.JUDGE);
							long seed = System.nanoTime();
							Collections.shuffle(results, new Random(seed));
							for (Result result : results) {
								String Title=result.getTitle().replaceAll("\\s+", " ").trim();
								bw.append("[Title]"+Title + "[Link]" + result.getLink()+"[Source]"+result.getSourceName()+"\n");
							}
							bw.close();
				           
				        }
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		       
	}

}
