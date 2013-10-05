package com.resultpool;

import java.util.HashMap;
import com.datatype.ServerSource;

public class ResultTable {
	private HashMap<Integer, RankList> ranking = new HashMap<Integer, RankList>();

	public ResultTable() {
		// TODO Auto-generated constructor stub

	}

	public RankList getRankList(ServerSource url) {
		return ranking.get(url);
	}

	public void AddRankList(int source, RankList list) {
		if (list != null&&list.getList().size()!=0)
			ranking.put(source, list);
	}

	public HashMap<Integer, RankList> getTable() {
		return ranking;
	}

	// to check if the server is already exist
	public Boolean isServerExist(String url) {
		if (ranking.containsKey(url))
			return true;
		else
			return false;

	}

	public void clearTable() {
		ranking.clear();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
