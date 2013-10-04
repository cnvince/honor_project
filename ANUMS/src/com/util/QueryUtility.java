package com.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.evaluation.JudgeMap;
import com.evaluation.ScoreMap;
import com.resultpool.Server;
import com.results.ResultReader;

public class QueryUtility {

	private final static String queryPath = "Queries/queries.txt";

	public QueryUtility() {
		// TODO Auto-generated constructor stub
	}

	public static ArrayList<String>[] groupQueries(int ct) {
		ArrayList<String>[] groups = new ArrayList[2];

		for (int i = 0; i < 2; i++)
			groups[i] = new ArrayList<String>();
		ArrayList<String> queries = QueryUtility.initialQueries();
		switch (ct) {
		case 1:
			double ave = 0;
			for (String query : queries) {
				HashMap<Integer, Server> servers = ResultReader
						.getServerInfo(query);
				int length = 0;
				for (Entry<Integer, Server> me : servers.entrySet()) {
					length += me.getValue().getResult_size();
				}
				ave += length;
			}
			ave = ave / queries.size();
			for (String query : queries) {
				HashMap<Integer, Server> servers = ResultReader
						.getServerInfo(query);
				int length = 0;
				for (Entry<Integer, Server> me : servers.entrySet()) {
					length += me.getValue().getResult_size();
				}
				if (length >= ave)
					groups[0].add(query);
				else
					groups[1].add(query);
			}
			break;
		case 2:
			double aveScore = 0;
			JudgeMap jMap=new JudgeMap(); 
			for (String query : queries) {
				ScoreMap sMap=jMap.getScoreMap(query);
				aveScore+=sMap.getAveScore();
			}
			aveScore=aveScore/queries.size();
			for (String query : queries) {
				ScoreMap sMap=jMap.getScoreMap(query);
				double score=sMap.getAveScore();
				if(score>=aveScore)
					groups[0].add(query);
				else
					groups[1].add(query);
			}
			break;
		case 3:
			double aveServer=0;
			for(String query:queries)
			{
				aveServer+=ResultReader.getResultTable(query).getTable().size();
			}
			aveServer=aveServer/queries.size();
			for(String query:queries)
			{
				double num=ResultReader.getResultTable(query).getTable().size();
				if(num>=aveServer)
					groups[0].add(query);
				else
					groups[1].add(query);
			}
			break;
		case 4:
			groups[0].add("Brian Schmidt");
			groups[0].add("Sir Mark Oliphant");
			groups[0].add("Desmond Ball");
			groups[0].add("Peter Doherty");
			groups[0].add("Rolf Zinkernagel");
			groups[0].add("John Eccles");
			groups[0].add("Sir Howard Florey");
			groups[0].add("John C Harsanyi");
			groups[0].add("H. C. Coombs");
			groups[0].add("A. D. Hope");
			groups[0].add("Judith Wright");
			groups[0].add("Ian Young");
			groups[0].add("Alistair Rendell");
			for(String query:groups[0])
			{
				queries.remove(query);
			}
			groups[1]=queries;
			break;
		}
		return groups;
	}

//	public static ArrayList<String> rankQueries()
//	{
//		ArrayList<String> rankedQueries=QueryUtility.initialQueries();
//		for(int i=1;i<)
//		return rankedQueries;
//	}
	public static ArrayList<String> initialQueries() {
		ArrayList<String> queries = new ArrayList<String>();
		try {
			String lineContent = null;
			BufferedReader reader = new BufferedReader(
					new FileReader(queryPath));
			while ((lineContent = reader.readLine()) != null) {
				if (!lineContent.trim().equals("")
						&& !lineContent.trim().equals("rsacs"))
					queries.add(lineContent.trim());
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return queries;
	}

}
