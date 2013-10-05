package com.evaluation;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.datatype.ALGORITHM;
import com.mergingalgorithms.LMS;
import com.mergingalgorithms.Scoring;
import com.resultpool.RankList;
import com.resultpool.ResultTable;
import com.resultpool.Server;
import com.results.Result;
import com.results.ResultReader;
import com.util.CsvUtility;
import com.util.FileUtility;
import com.util.QueryUtility;

public class CrossValidation {

	private final String foldPath="Evaluation/Data/Fold-";
	public ArrayList<String> testingData = new ArrayList<String>();

	public CrossValidation() {
		// TODO Auto-generated constructor stub
	}
//	initialize all data to file
	public void setAllData()
	{
		ArrayList<String> queries = QueryUtility.initialQueries();
		CsvUtility dataWriter = new CsvUtility("Evaluation/Data/alldata.csv");
		this.initHead(dataWriter);
		for(String query:queries)
			initData(query,dataWriter);
	}
	
	// initial training sets and testing data
	public void initialFolds(int k) {
		ArrayList<String> queries = QueryUtility.initialQueries();
		long seed = System.nanoTime();
		Collections.shuffle(queries, new Random(seed));
		@SuppressWarnings("unchecked")
		ArrayList<String>[] folds = new ArrayList[k];
		int length = queries.size();
		// the size per fold
		int size = length / k;
		for (int i = 0; i < k; i++) {
			System.out.println("Fold "+i+":");
			ArrayList<String> fold = new ArrayList<String>();
			for (int j = size * i; j < size * i + size; j++) {
				String Query = queries.get(j);
				System.out.print(Query);
				fold.add(Query);
			}
			System.out.println();
			folds[i] = fold;
		}
		for(int i=0;i<k;i++)
		{
			String cFoldPath=foldPath+i;
			FileUtility.createDirectory(cFoldPath);
			try {
				PrintStream ps=new PrintStream(cFoldPath+"/regression.R");
				ps.println("data<-read.csv(file=\"train.csv\",sep=\",\",head=TRUE)\n"+ 
						"fit<-lm(rel~-1+length+cs+sc+rs,data=data)\n"+
						"write.csv(coef(summary(fit)),file=\"regression.csv\")");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			CsvUtility trainWriter = new CsvUtility(cFoldPath+"/train.csv");
			this.initHead(trainWriter);
//			pick one as the testing data
			CsvUtility writer=new CsvUtility(cFoldPath+"/testQuery.csv");
			ArrayList<String> test=folds[i];
			for(int j=0;j<test.size();j++)
			{
				String query=test.get(j);
				String[] line={query};
				writer.append(line);
			}
//			keep 3/4 as the training data
			for(int j=0;j<k;j++)
			{
				if(j!=i)
				{
					ArrayList<String> train=folds[j];
					for(String query:train)
					{
						initData(query,trainWriter);
					}
				}
			}
		}
	}

	// initialize the head
	private void initHead(CsvUtility csu) {
		String head[] = { "length","cs","sc","rs","rel" };
		csu.append(head);
	}

	private void initData(String query, CsvUtility writer) {
		ResultTable results = ResultReader.getResultTable(query);
		HashMap<Integer, Server> serverTable = ResultReader
				.getServerInfo(query);
		HashMap<Integer, RankList> lists = results.getTable();
		// initial the length based weight
		LMS.initialSWeight(serverTable);
		for (Map.Entry<Integer, RankList> list : lists.entrySet()) {
			RankList ranklist = list.getValue();
			ArrayList<Result> clist = ranklist.getList();
			double score = 0;
			for (Result result : clist) {
				double cscore = Scoring.calScore(query, clist, result,
						Scoring.DTSS);
				result.setScore(cscore);
				score = score + cscore;
			}
			// get the average score
			score = score / (double) clist.size();
			Server server = serverTable.get(list.getKey());
			server.setScore(score);
			;
		}
		JudgeMap jMap = new JudgeMap();
		ScoreMap sMap = jMap.getScoreMap(query);

		for (Map.Entry<Integer, RankList> list : lists.entrySet()) {
			RankList ranklist = list.getValue();
			ArrayList<Result> clist = ranklist.getList();
			for (Result result : clist) {
				result.setRank(clist.indexOf(result) + 1);
				double rel = sMap.getScore(result.getDocID())/5;
				Server server = serverTable.get(result.getSource());
				double rs = (double) (10 - result.getRank()) / 10;
				String[] data = { Double.toString(server.getWeight()),
						Double.toString(server.getScore()),
						Double.toString(result.getScore()),
						Double.toString(rs), Double.toString(rel) };
				writer.append(data);
				;
			}
		}
	}
	public void runTest(int k)
	{
		ArrayList<Integer> Methods=new ArrayList<Integer>();
		CsvUtility csvut = new CsvUtility( "Evaluation/Data/NDCG@10.csv");
		String[][] matrix = new String[k*2][11];
		Methods.add(ALGORITHM.MW);
		for(int i=0;i<k;i++)
		{
			String cFoldPath=foldPath+i;
			List<String[]> data=CsvUtility.readAll(cFoldPath+"/testQuery.csv");
			ArrayList<String> queries=new ArrayList<String>();
			List<String[]> lines=CsvUtility.readAll(cFoldPath+"/regression.csv");
			int j=0;
			double[] paras=new double[4];
			for(int m=1;m<lines.size();m++)
			{
				String[] line=lines.get(m);
				double para=Double.parseDouble(line[1]);
				paras[j]=para;
				j++;
			}
			
			for(String[] query:data)
			{
				String cquery=query[0];
				queries.add(cquery);
			}
			Evaluator evaluator=new Evaluator(Methods,queries,paras,true);
			matrix[2*i]=evaluator.NDCGAtK(10, ALGORITHM.LOCAL);
			matrix[2*i+1]=evaluator.NDCGAtK(10, ALGORITHM.MW);
		}
		csvut.writeMatrix(matrix);
	}



	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CrossValidation cv = new CrossValidation();
//		cv.initialFolds(4);
		cv.runTest(4);
//		cv.setAllData();
//		ArrayList<Integer> algorithms = new ArrayList<Integer>();
//		// algorithms.add(ALGORITHM.LOCAL);
//		algorithms.add(ALGORITHM.MW);
//		Evaluator evaluator = new Evaluator(algorithms, cv.testingData, true);
//		evaluator.NDCGAtK(10);
	}

}
