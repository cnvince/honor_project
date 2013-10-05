package com.mergingalgorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.resultpool.RankList;
import com.resultpool.ResultTable;
import com.resultpool.Server;
import com.resultpool.ServerInfo;
import com.results.Result;

public class OPRRMerger implements Merger {
	private String query;

	public OPRRMerger(String query) {
		this.query = query;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<Result> executeMerging(ResultTable results,
			HashMap<Integer, Server> serverTable) {
		HashMap<Integer, RankList> table = results.getTable();
		ArrayList<Result> mergedList = new ArrayList<Result>();
		ArrayList<ArrayList<Result>> ranklists = new ArrayList<ArrayList<Result>>();

		int MaxSize = 0;
		int totalSize = 0;
		Map<Integer, Double> serverlist = ServerInfo.sortServer(results, query);
		for (Map.Entry<Integer, Double> sid : serverlist.entrySet()) {
			Server server = serverTable.get(sid.getKey());
			if (server.getResult_size() > 0) {
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

		return mergedList;

	}


}
