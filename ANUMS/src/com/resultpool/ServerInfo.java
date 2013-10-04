package com.resultpool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.evaluation.JudgeMap;
import com.evaluation.ScoreMap;
import com.results.Result;

public class ServerInfo {

	public ServerInfo() {
		// TODO Auto-generated constructor stub
	}
	public static Map<Integer, Double> sortServer(ResultTable results,
			String query) {

		JudgeMap jMap = new JudgeMap();
		ScoreMap sMap = jMap.getScoreMap(query);
		HashMap<Integer, RankList> ranking = results.getTable();
		TreeMap<Integer, Double> servers = new TreeMap<Integer, Double>();
		for (Map.Entry<Integer, RankList> me : ranking.entrySet()) {
			RankList rankList = me.getValue();
			ArrayList<Result> list = rankList.getList();
			for (Result result : list) {
				double score = sMap.getScore(result.getDocID());
				// result.setScore(score);
				// idealRank.add(result);
				int sid = result.getSource();
				if (!servers.containsKey(sid))
					servers.put(sid, score);
				else {
					servers.put(sid, servers.get(sid) + score);
				}
			}
		}
		Map<Integer, Double> sortedMap = sortByComparator(servers);
		return sortedMap;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Map<Integer, Double> sortByComparator(
			TreeMap<Integer, Double> servers) {
		List list = new LinkedList(servers.entrySet());

		// sort list based on comparator
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return -((Comparable) ((Map.Entry) (o1)).getValue())
						.compareTo(((Map.Entry) (o2)).getValue());
			}
		});

		// put sorted list into map again
		// LinkedHashMap make sure order in which keys were inserted
		Map sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	
}
