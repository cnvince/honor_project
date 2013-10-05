package com.mergingalgorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.resultpool.RankList;
import com.resultpool.ResultTable;
import com.resultpool.Server;
import com.results.Result;

public class MultiWeightMerger implements Merger {
	private String query;
	private double[] paras;
	private int field=Scoring.DTSS;
	public MultiWeightMerger(String query,double[] paras) {
		this.query=query;
		this.paras=paras;
	}
	public MultiWeightMerger(String query) {
		this.query=query;
		
//		paras[0]=0.215856653636418;
//		paras[1]=0.112868148469925;
//		paras[2]=0.0151101969677256;
//		paras[3]=1.41650314927627;
//		paras[4]=0.722613763349553;
	}
	@Override
	public ArrayList<Result> executeMerging(ResultTable results,
			HashMap<Integer, Server> serverTable) {
		HashMap<Integer,RankList>lists=results.getTable();
//		initial the length based weight
		LMS.initialSWeight(serverTable);
		for(Map.Entry<Integer, RankList> list:lists.entrySet())
		{
			RankList ranklist=list.getValue();
			ArrayList<Result> clist=ranklist.getList();
			double score=0;
			for(Result result:clist)
			{
				double cscore=Scoring.calScore(query, clist, result, field);
				result.setScore(cscore);
				score=score+cscore;
			}
//			get the average score
			score=score/(double)clist.size();
			Server server=serverTable.get(list.getKey());
			server.setScore(score);;
		}
		
		ArrayList<Result> mergedList=new ArrayList<Result>();
		for(Map.Entry<Integer, RankList> list:lists.entrySet())
		{
			RankList ranklist=list.getValue();
			ArrayList<Result> clist=ranklist.getList();
			for(Result result:clist)
			{
				result.setRank(clist.indexOf(result)+1);
				Server server=serverTable.get(result.getSource());
				double rs=(double)(10-result.getRank())/10;
				double lt=server.getWeight();
				double cs=server.getScore();
				double sc=result.getScore();
				double score= paras[0]*lt+paras[1]*cs+paras[2]*sc+paras[3]*rs;
				// double score= -0.01038*Math.log(lt + cs )
				// +-0.02625*Math.log(sc + rs );
				//				double score=paras[0]*lt+paras[1]*cs+paras[2]*sc+paras[3]*rs;
//				double score=paras[0]+paras[1]*lt*sc+paras[2]*lt*rs+paras[3]*cs*sc+paras[4]*cs*rs;
//				double score=paras[0]*lt*sc+paras[1]*lt*rs+paras[2]*cs*sc+paras[3]*cs*rs;
				result.setScore(score);
				mergedList.add(result);
			}
		}
		Collections.sort(mergedList);
		return mergedList;
	}

}
