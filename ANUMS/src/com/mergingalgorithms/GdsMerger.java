package com.mergingalgorithms;

/*	
 * Author: PengFei Li
 * Date:11/04/2013
 * An algorithm from [1] Y. Rasolofo, D. Hawking, and J. Savoy, �Result merging strategies for a current news metasearcher,� Information Processing & Management, vol. 39, no. 4, pp. 581�609, Jul. 2003.
 * which use a generic document score function to calculate the score of each document,
 * using the Formula W=NQW/sqrt(L^2+LF^2) 
 * 
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.resultpool.ResultTable;
import com.resultpool.Server;
import com.results.Result;

public class GdsMerger implements Merger {

	// define field type
	private int field = Scoring.TS;

	public String query;

	// final list

	public GdsMerger(String query, int field) {
		this.query = query;
		this.field = field;
	}

	@Override
	public ArrayList<Result> executeMerging(ResultTable results,
			HashMap<Integer, Server> serverTable) {

		// TODO Auto-generated method stub
		ArrayList<Result> mergedList = Scoring.initScore(query, results, field);
		Collections.sort(mergedList);
		return mergedList;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
