package com.broker;
// main controller of the broker
import java.util.ArrayList;

import com.adapters.AdapterFactory;
import com.datatype.ALGORITHM;
import com.mergingalgorithms.GdsMerger;
import com.mergingalgorithms.JMerger;
import com.mergingalgorithms.Merger;
import com.mergingalgorithms.PRRMerger;
import com.mergingalgorithms.SimpleRRMerger;
import com.mergingalgorithms.LMS;
import com.resultpool.ResultTable;
import com.results.Result;

public class Controller {
	// ResultWritter rw=new ResultWritter();
	// fields for METHODs.

	// define GDS field type
	private final int TS = 0;
	private final int SS = 1;
	private final int TSS = 2;

	public Controller() {
		// TODO Auto-generated constructor stub

	}
//	get the merged results of query according method
	public ArrayList<Result> fetchResult(String Query, int method) {
		AdapterFactory factory = new AdapterFactory();
		ResultTable results = factory.executeQuery(Query);
		Merger merger = null;
		switch (method) {
		// Random Round-Robin
		case ALGORITHM.SRR:
			merger = new SimpleRRMerger();
			break;
		case ALGORITHM.PRR:
			merger = new PRRMerger();
			break;
		case ALGORITHM.GDS_TS:
			merger = new GdsMerger(Query, TS);
			break;
		case ALGORITHM.GDS_SS:
			merger = new GdsMerger(Query, SS);
			break;
		case ALGORITHM.GDS_TSS:
			merger = new GdsMerger(Query, TSS);
			break;
		case ALGORITHM.LMS:
			merger = new LMS(Query);
		case ALGORITHM.JUDGE:
			merger = new JMerger(Query);
		default:
			break;
		}
		return merger.executeMerging(results, factory.ServerTable);

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
