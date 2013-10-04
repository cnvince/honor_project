package com.mergingalgorithms;

import java.util.ArrayList;
import java.util.HashMap;

import com.resultpool.ResultTable;
import com.resultpool.Server;
import com.results.Result;


public interface Merger {
	public ArrayList<Result> executeMerging(ResultTable results,HashMap<Integer,Server> serverTable);
}
