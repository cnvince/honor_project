package com.evaluation;
//the class is used to store the score of documents for a particular query
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

public class ScoreMap {

	private HashMap<String,Double> map;
	public ScoreMap() {
		// TODO Auto-generated constructor stub
		map=new HashMap<String,Double>();
	}
//	get the size
	public int getSize()
	{
		return map.size();
	}
//	add a doc
	public void add(String id, Double score)
	{
		if(!map.containsKey(id))
		{
			map.put(id, score);
		}
	}
//	return the score of a doc
	public double getScore(String id)
	{
		return map.get(id);
	}
	public Set<Entry<String, Double>> getEntry()
	{
		return map.entrySet();
	}
//	get the average score of all documents
	public double getAveScore()
	{
		double score=0;
		for(Entry<String, Double> me:map.entrySet())
		{
			score+=me.getValue();
		}
		score=(double)score/map.size();
		return score;
	}
	public double getScoreDv()
	{
		double score=0;
		double[] cscore=new double[map.size()];
		int i=0;
		for(Entry<String, Double> me:map.entrySet())
		{
			cscore[i]=me.getValue();
			i++;
		}
		StandardDeviation std=new StandardDeviation();
		score=std.evaluate(cscore);
		System.out.println("Standard derviation:"+score);
		return score;
	}

}
