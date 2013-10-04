package com.mergingalgorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.resultpool.RankList;
import com.resultpool.ResultTable;
import com.resultpool.Server;
import com.results.Result;

public class AGdsMerger implements Merger {

	// define field type
	private int field = Scoring.DTSS;
	public String query;

	public AGdsMerger(String query) {
		this.query = query;
	}

	@Override
	public ArrayList<Result> executeMerging(ResultTable results,
			HashMap<Integer, Server> serverTable) {
		this.initServerScore(results, serverTable);
		// TODO Auto-generated method stub
		ArrayList<Result> mergedList = Scoring.aScore(query, results,
				serverTable, field);
		Collections.sort(mergedList);
		return mergedList;
	}

	public void initServerScore(ResultTable results,
			HashMap<Integer, Server> serverTable) {
		HashMap<Integer, RankList> lists = results.getTable();
		for (Map.Entry<Integer, RankList> list : lists.entrySet()) {
			RankList ranklist = list.getValue();
			ArrayList<Result> clist = ranklist.getList();
			String title="";
			String summary = "";
			double score = 0;
			for (Result result : clist) {
				title=title+" "+result.getTitle();
				summary = summary + " "
						+ result.getDisplaySummary();
				 score+=Scoring.calScore(query, clist, result, field);
			}
//			score = 0.1*Scoring.getScore(summary, query)+0.9*Scoring.getScore(title, query);
			// get the average score
			score = score / (double) clist.size();
			Server server = serverTable.get(list.getKey());
			server.setScore(score);
		}
	}

}
