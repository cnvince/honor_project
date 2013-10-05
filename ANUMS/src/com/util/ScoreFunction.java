package com.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import org.tartarus.snowball.ext.englishStemmer;

public class ScoreFunction {

	public ScoreFunction() {
		// TODO Auto-generated constructor stub
	}
	public static double getCosSimilarityByTF(String query, String content)
	{
		HashMap<String, Integer> queryMap=getTF(query);
		HashMap<String, Integer> documentMap=getTF(content);
		double score=0;
		for(Entry<String, Integer> entry: queryMap.entrySet())
		{
			String key=entry.getKey();
			if(documentMap.containsKey(key))
			{
				score=entry.getValue()*documentMap.get(key);
			}
		}
		score=score/(queryMap.size()*documentMap.size());
		return score;
	}
//	initial vector space usung TF
	public static HashMap<String, Integer> getTF(String content)
	{
		HashMap<String, Integer> wordMap = new HashMap<String, Integer>();
		StringTokenizer st = new StringTokenizer(content);
		englishStemmer _stemmer=new englishStemmer();
		while (st.hasMoreTokens()) {
			_stemmer.setCurrent(st.nextToken());
			_stemmer.stem();
			String stem = _stemmer.getCurrent();
			if (wordMap.containsKey(stem)) {
				wordMap.put(stem, wordMap.get(stem) + 1);
			} else {
				wordMap.put(stem, 1);
			}
		}
		return wordMap;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
