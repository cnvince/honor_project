package com.mergingalgorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.resultpool.ResultTable;
import com.resultpool.Server;
import com.results.Result;

public class OGdsMerger implements Merger {

	// define field type
	private int field = Scoring.DTSS;
	public String query;

	public OGdsMerger(String query) {
		this.query = query;
	}

	@Override
	public ArrayList<Result> executeMerging(ResultTable results,
			HashMap<Integer, Server> serverTable) {
		ArrayList<Result> mergedList = Scoring.oScore(query, results, field);
		Collections.sort(mergedList);
		return mergedList;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
