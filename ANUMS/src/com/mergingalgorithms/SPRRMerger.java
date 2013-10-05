package com.mergingalgorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.resultpool.RankList;
import com.resultpool.ResultTable;
import com.resultpool.Server;
import com.results.Result;
import com.util.ServerScoreComparable;

public class SPRRMerger implements Merger {
	
	// define xx field
	private int field = Scoring.DTSS;
	public String query;
	public SPRRMerger(String query) {
		
		this.query=query;
	}

	@Override
	public ArrayList<Result> executeMerging(ResultTable results,
			HashMap<Integer, Server> serverTable) {
		HashMap<Integer,RankList>lists=results.getTable();
		for(Map.Entry<Integer, RankList> list:lists.entrySet())
		{
			RankList ranklist=list.getValue();
			ArrayList<Result> clist=ranklist.getList();
			double score=0;
			for(Result result:clist)
			{
				score+=Scoring.calScore(query, clist, result, field);
			}
//			get the average score
			score=score/(double)clist.size();
			Server server=serverTable.get(list.getKey());
			server.setScore(score);
		}
		ArrayList<Server> rankedServer=sortServer(serverTable);
		int loop=0;
		HashMap<Integer, RankList> table = results.getTable();
		int i=0;
		ArrayList<Result> mergedList=new ArrayList<Result>();
		boolean isContinue=true;
		while(isContinue)
		{
			isContinue=false;
			for(Server server:rankedServer)
			{
				if(table.get(server.getServer())!=null)
				{
					ArrayList<Result> list=table.get(server.getServer()).getList();
					if(list.size()>i)
					{
						mergedList.add(list.get(i));
						isContinue=true;
					}
				}
			}
			i++;
		}
		for(int j=0;j<mergedList.size();j++)
		{
			Result result=mergedList.get(j);
			System.out.println(result.getSource()+": score :"+serverTable.get(result.getSource()).getScore());
		}
		return mergedList;
	
	}
	
//	sort server by it's server score
	public ArrayList<Server> sortServer(HashMap<Integer,Server> servers)
	{
		ArrayList<Server> sids=new ArrayList<Server>();
		for(Map.Entry<Integer,Server> server:servers.entrySet())
		{
			Server cServer=server.getValue();
			sids.add(cServer);
		}
		Collections.sort(sids, new ServerScoreComparable());
		return sids;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
