package com.mergingalgorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.tartarus.snowball.ext.englishStemmer;

import com.resultpool.RankList;
import com.resultpool.ResultTable;
import com.resultpool.Server;
import com.resultpool.ServerInfo;
import com.results.Result;

public class Scoring {

	// define field type
	public static final int TS = 0;
	public static final int SS = 1;
	public static final int TSS = 2;
	public static final int DTSS = 3;

	private static double k=0.9;
	public Scoring() {
		// TODO Auto-generated constructor stub
	}

	public static ArrayList<Result> initScore(String query,
			ResultTable results, int field) {
		ArrayList<Result> mergedList = new ArrayList<Result>();
		HashMap<Integer, RankList> resultLists = results.getTable();
		for (Map.Entry<Integer, RankList> entry : resultLists.entrySet()) {
			RankList rankList = entry.getValue();
			ArrayList<Result> list = rankList.getList();
			int length = list.size();
			for (int i = 0; i < length; i++) {
				Result result = list.get(i);
				result.setScore(calScore(query, list, result, field));
				mergedList.add(result);
			}
		}
		return mergedList;
	}
	
//	optimized score
	public static ArrayList<Result> oScore(String query,
			ResultTable results, int field) {
		ArrayList<Result> mergedList = new ArrayList<Result>();
		HashMap<Integer, RankList> resultLists = results.getTable();
		Map<Integer, Double> sortedServer=ServerInfo.sortServer(results, query);
		for (Map.Entry<Integer, RankList> entry : resultLists.entrySet()) {
			int sid=entry.getKey();
//			get server score
			double sscore=sortedServer.get(sid);
			
			RankList rankList = entry.getValue();
			ArrayList<Result> list = rankList.getList();
			int length = list.size();
			for (int i = 0; i < length; i++) {
				Result result = list.get(i);
				result.setScore(sscore*calScore(query, list, result, field));
				mergedList.add(result);
			}
		}
		return mergedList;
	}

//	optimized score
	public static ArrayList<Result> aScore(String query,
			ResultTable results, HashMap<Integer,Server> serverTable,int field ) {
//		LMS.initialSWeight(serverTable);
		ArrayList<Result> mergedList = new ArrayList<Result>();
		HashMap<Integer, RankList> resultLists = results.getTable();
		for (Map.Entry<Integer, RankList> entry : resultLists.entrySet()) {
			int sid=entry.getKey();
//			get server score
			double sscore=serverTable.get(sid).getScore();
			
			RankList rankList = entry.getValue();
			ArrayList<Result> list = rankList.getList();
			int length = list.size();
			for (int i = 0; i < length; i++) {
				Result result = list.get(i);
				double score=calScore(query, list, result, field);
				score=(score+0.4*sscore)/1.4;
				result.setScore(score);
				mergedList.add(result);
			}
		}
		return mergedList;
	}

	// use xx field to calculate the document score
	public static double calScore(String query, ArrayList<Result> results,
			Result result, int field) {
		double score = 0;
		switch (field) {
		case TS:
			// get the similarity of the title and the query
			score = getScore(result.getTitle(), query);
			break;
		case SS:
			// get the similarity of the summary and the query
			score = getScore(result.getDisplaySummary(), query);
			break;
		case TSS:
			score = getScore(result.getTitle(), query);
			if (score == 0)
				score = getScore(result.getDisplaySummary(), query);
			if (score == 0)
				score = getRS(result, results)/10000;
			break;
		case DTSS:
			score = k*getScore(result.getTitle(),query)+(1-k)*getScore(result.getDisplaySummary(),query);
			if(score==0)
				score= getRS(result,results)/10000;
			break;
		default:
			break;
		}
		return score;
	}
	public static double getScore(String fieldContent, String query) {
		if (fieldContent == null || fieldContent.trim().equals(""))
			return 0;
		int NQW = 0;
		HashMap<String, Integer> queryMap = getStemmer(query);
		HashMap<String, Integer> fieldMap = getStemmer(fieldContent);
		for (Map.Entry<String, Integer> entry : queryMap.entrySet()) {
			String term = entry.getKey();
			if (fieldMap.containsKey(term))
				NQW += fieldMap.get(term);
		}
		double score = NQW
				/ Math.sqrt(queryMap.size() * queryMap.size() + fieldMap.size()
						* fieldMap.size());
		return score;
	}

	// get the rank score
	public static double getRS(Result result, ArrayList<Result> results) {
		int index = results.indexOf(result);
		// RS=(10-RANK)/10(range:0.1-1)
		double score = (double) (10 - index) / 10;
		return score;
	}

	public static HashMap<String, Integer> getStemmer(String content) {
		englishStemmer _stemmer = new englishStemmer();
		HashMap<String, Integer> wordMap = new HashMap<String, Integer>();
		StringTokenizer st = new StringTokenizer(content);
		while (st.hasMoreTokens()) {
			_stemmer.setCurrent(st.nextToken());
			_stemmer.stem();
			String stem = _stemmer.getCurrent();
			if (wordMap.containsKey(stem)) {
				wordMap.put(stem, wordMap.get(stem) + 1);
			} else {
				wordMap.put(stem, 1);
			}
		}
		return wordMap;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
