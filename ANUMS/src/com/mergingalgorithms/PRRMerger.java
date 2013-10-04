package com.mergingalgorithms;

/*	
 * Author: PengFei Li
 * Date:11/04/2013
 * A prioritized Round-Robin algorithm, rank the lists in a round-robin manner, 
 * the orders of server chosen in a cycle is ordered by the relevant document length.
 * 
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.resultpool.RankList;
import com.resultpool.ResultTable;
import com.resultpool.Server;
import com.results.Result;

public class PRRMerger implements Merger {

	public PRRMerger() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public ArrayList<Result> executeMerging(ResultTable results,
			HashMap<Integer, Server> serverTable) {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();
		System.out.println("Prioritized Merging ");
		// TODO Auto-generated method stub
		HashMap<Integer, RankList> table = results.getTable();
		ArrayList<Result> mergedList = new ArrayList<Result>();
		ArrayList<ArrayList<Result>> ranklists = new ArrayList<ArrayList<Result>>();

		int MaxSize = 0;
		int totalSize = 0;
		ArrayList<Server> serverlist = sortServer(serverTable);
//		System.err.println("$$$$$$$$$$$$$$$$$$$$$$$");
//		System.err.println("size:" + serverTable.size());
		for (Server server : serverlist) {
			if (server.getResult_size() > 0) {
//				System.out.println(server.getServer() + ":"
//						+ server.getResult_size());
//				System.out.println("===============================");
				if (table.get(server.getServer()) != null) {
					ArrayList<Result> list = table.get(server.getServer())
							.getList();
					ranklists.add(list);
					if (list.size() > MaxSize)
						MaxSize = list.size();
					totalSize += list.size();
				}
			}
		}
		for (int i = 0; i < MaxSize; i++) {
			for (int j = 0; j < ranklists.size(); j++) {
				ArrayList<Result> list = (ArrayList<Result>) ranklists.get(j);
				if (list.size() > i) {
					mergedList.add(list.get(i));
				}
			}
		}
//		System.out.println("Total:" + totalSize + " mergedSize:"
//				+ mergedList.size());
//		long end = System.currentTimeMillis();
//		System.out.println("merging: " + (end - start) + "s");
		
		return mergedList;

	}

	public ArrayList<Server> sortServer(HashMap<Integer, Server> serverTable) {
		ArrayList<Server> list = new ArrayList<Server>();
		for (Map.Entry<Integer, Server> entry : serverTable.entrySet()) {
			list.add(entry.getValue());
		}
		Collections.sort(list);
		return list;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
