package com.evaluation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.datatype.ALGORITHM;
import com.resultpool.Server;
import com.results.ResultReader;
import com.util.FileUtility;
import com.util.QueryUtility;

public class QueryClustering {
	private Map<String,Double> sortedMap;
	private final String cvPath="Evaluation/Results/CSV/NDCG/ALL/PATTERN/";
	private final int 	QL=1;
	private final int DL=2;
	private final int AvScore=3;
	private final int SZ=4;
	private final int SD=5;
	private ArrayList<Integer> Factors=new ArrayList<Integer>();
	public QueryClustering() {
		for(int i=1;i<=5;i++)
			Factors.add(i);
	}
	
	public ArrayList<String>[] Clustering(int factor) {
		ArrayList<String> queries = QueryUtility.initialQueries();
		JudgeMap jMap=new JudgeMap(); 
		ArrayList<String>[] clusters=new ArrayList[4];
		TreeMap<String, Double> qMap = new TreeMap<String, Double>();
		for (String query : queries) {
			ScoreMap sMap=jMap.getScoreMap(query);
			double value = 0;
			switch (factor) {
//			query length
			case 1:
				value = query.length();
				break;
//				document length
			case 2:
				HashMap<Integer, Server> servers = ResultReader
						.getServerInfo(query);
				int length = 0;
				for (Entry<Integer, Server> me : servers.entrySet()) {
					length += me.getValue().getResult_size();
				}
				value=length;
				break;
//				average score
			case 3:
				value=sMap.getAveScore();
				break;
//				server size
			case 4:
				value=ResultReader.getServerInfo(query).size();
				break;
//				standard deviation
			case 5:
				value=sMap.getScoreDv();
				break;
			}
			qMap.put(query, value);
		}
		sortedMap=sortByComparator(qMap);
		int i=0;
		int j=-1;
		for(Map.Entry<String, Double> me:sortedMap.entrySet())
		{
			String query=me.getKey();
			if(i%11==0)
			{
				j++;
				ArrayList<String> cluster=new ArrayList<String>();
				clusters[j]=cluster;
			}
			clusters[j].add(query);
			i++;
		}
		return clusters;
	}
	@SuppressWarnings({ "unused", "unchecked" })
	private static Map<String, Double> sortByComparator(
			TreeMap<String, Double> servers) {
		List list = new LinkedList(servers.entrySet());

		// sort list based on comparator
		Collections.sort(list, new Comparator() {
			@SuppressWarnings("rawtypes")
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getValue())
						.compareTo(((Map.Entry) (o2)).getValue());
			}
		});
		// put sorted list into map again
		// LinkedHashMap make sure order in which keys were inserted
		Map sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	public void initialClusters(int factor)
	{
		ArrayList<Integer> algorithms = new ArrayList<Integer>();
//		 algorithms.add(ALGORITHM.OPRR);
		algorithms.add(ALGORITHM.GDS_SS);
		algorithms.add(ALGORITHM.GDS_TS);
		algorithms.add(ALGORITHM.GDS_TSS);
		algorithms.add(ALGORITHM.GDS_DTSS);
		algorithms.add(ALGORITHM.LMS);
		algorithms.add(ALGORITHM.SRR);
		algorithms.add(ALGORITHM.PRR);
		algorithms.add(ALGORITHM.SPRR);
		algorithms.add(ALGORITHM.MW);
		algorithms.add(ALGORITHM.LOCAL);
		ArrayList<String>[] clusters=this.Clustering(factor);
		for(int i=0;i<clusters.length;i++)
		{
			ArrayList<String> cluster=clusters[i];
			FileUtility.createDirectory(cvPath);
			FileUtility.createDirectory(cvPath+factor+"/");
			Evaluator eva=new Evaluator(algorithms,cluster,false);
			eva.drawNDCGAtK(10, "PATTERN/"+factor+"/"+i+".csv");
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		QueryClustering cluster=new QueryClustering();
//		cluster.Clustering(2);
		for(int factor:cluster.Factors)
			cluster.initialClusters(factor);
	}

}
