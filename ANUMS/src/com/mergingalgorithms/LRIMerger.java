package com.mergingalgorithms;

import java.util.ArrayList;
import java.util.HashMap;

import com.index.IndexBuilder;
import com.resultpool.ResultTable;
import com.resultpool.Server;
import com.results.Result;

public class LRIMerger implements Merger {

	public String Query;
	public LRIMerger(String query) {
		this.Query=query;
	}

	@Override
	public ArrayList<Result> executeMerging(ResultTable results,
			HashMap<Integer, Server> serverTable) {
		// TODO Auto-generated method stub
		ArrayList<Result> resultlist=IndexBuilder.getResult(results, Query);
		return resultlist;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
