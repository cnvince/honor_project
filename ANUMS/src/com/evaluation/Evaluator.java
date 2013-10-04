package com.evaluation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.datatype.ALGORITHM;
import com.resultpool.Server;
import com.results.ResultReader;
import com.util.ArrayUtility;
import com.util.CsvUtility;
import com.util.FileFinder;
import com.util.FileUtility;
import com.util.QueryUtility;

public class Evaluator {
	// public PrintStream ps = null;
	private final String resultPath = "Evaluation/Results/CSV/RESULT/";
	private final String ndcgPath = "Evaluation/Results/CSV/NDCG/ALL/";
	private final String precPath = "Evaluation/Results/CSV/PREC/precision.csv";
	private final String rrPath = "Evaluation/Results/CSV/NDCG/RR/";
	private final String graphicPath = "Evaluation/Results/GRAPHICS/ALL/";
	private final String rr_gPath = "Evaluation/Results/GRAPHICS/RR/";
	private final String disPath = "Evaluation/Results/Q_A_DIS/";
	private ArrayList<String> queries;
	// private CSVWriter csWriter;
	private static JudgeMap judgeMap = new JudgeMap();
	ArrayList<Integer> Methods;
	private PrintStream ps;

	// private static String
	public Evaluator(ArrayList<Integer> Methods, Boolean initResult) {
		// initial ResutMap
		judgeMap = new JudgeMap();
		this.Methods = Methods;
		// running each methods to be used
		Configure configure = new Configure();
		if (initResult) {
			for (int method : Methods)
				configure.outputResult(method, QueryUtility.initialQueries());
		}
		queries = new ArrayList<String>();
		for (Entry<String, ScoreMap> list : judgeMap.getEntry()) {
			// iteration by query
			String query = list.getKey();
			if (query.equalsIgnoreCase("rsacs"))
				continue;
			queries.add(query);
		}
	}

	public Evaluator(ArrayList<Integer> Methods, ArrayList<String> queries,
			Boolean initResult) {
		// initial ResutMap
		judgeMap = new JudgeMap();
		this.Methods = Methods;
		if (initResult) {
			// running each methods to be used
			Configure configure = new Configure();
			for (int method : Methods)
				configure.outputResult(method, queries);
		}
		this.queries = queries;
	}

	public Evaluator(ArrayList<Integer> Methods, ArrayList<String> queries,
			double[] paras, Boolean initResult) {
		// initial ResutMap
		judgeMap = new JudgeMap();
		this.Methods = Methods;
		if (initResult) {
			// running each methods to be used
			Configure configure = new Configure(paras);
			for (int method : Methods)
				configure.outputResult(method, queries);
		}
		this.queries = queries;
	}

	// public void setPara(double[] paras) {
	// // running each methods to be used
	// Configure configure = new Configure(paras);
	// for (int method : Methods)
	// configure.outputResult(method, queries);
	// }

	public void outputResults() {

		HashMap<Integer, ResultMap> resultMaps = new HashMap<Integer, ResultMap>();
		for (int method : Methods) {
			ResultMap resultMap = new ResultMap(method);
			resultMaps.put(method, resultMap);
		}
		// initial result list returned by algorithm
		for (Entry<String, ScoreMap> list : judgeMap.getEntry()) {
			// iteration by query
			String query = list.getKey();
			ScoreMap scoreMap = judgeMap.getScoreMap(query);
			CsvUtility csWriter = null;
			// output the result to a csv file
			csWriter = new CsvUtility(resultPath + query + ".csv");
			int length = 0;
			String[][] results = new String[Methods.size()][];
			for (int k = 0; k < Methods.size(); k++) {
				int method = Methods.get(k);
				// get the result map
				ResultMap rMap = resultMaps.get(method);
				ArrayList<String> ranking = (ArrayList<String>) rMap
						.getRanking(query);
				int size = ranking.size();
				length = size + 1;
				String[] result = new String[length];
				result[0] = ALGORITHM.getAlgorithm(method);
				// double[] result = new double[size];
				int i = 1;
				for (String doc : ranking) {
					result[i] = Double.toString(scoreMap.getScore(doc));
					i++;
				}
				results[k] = result;
			}
			csWriter.writeMatrix(results);
		}
	}

	// Method: NDCG
	public void evaNDCG() {
		// initial result list returned by algorithm
		for (String query : queries) {
			CsvUtility csWriter = null;
			csWriter = new CsvUtility(ndcgPath + query + ".csv");
			String[][] matrix = new String[Methods.size()][];
			for (int k = 0; k < Methods.size(); k++) {
				int method = Methods.get(k);
				// get the result map
				ResultMap rMap = new ResultMap(method);
				ArrayList<String> ranking = (ArrayList<String>) rMap
						.getRanking(query);
				int size = ranking.size();
				String line[] = new String[size + 1];
				line[0] = ALGORITHM.getAlgorithm(method);

				double[] G = new double[size];
				ScoreMap scoreMap = judgeMap.getScoreMap(query);

				if (!judgeMap.contains(query)) {
					System.err.println("query: " + query
							+ "has not been assesed");
					continue;
				}

				// initial score list G[]
				for (int i = 0; i < size; i++) {
					String docId = null;
					docId = ranking.get(i);
					G[i] = scoreMap.getScore(docId);
				}
				double[] IG = new double[size];
				int j = 0;
				for (Map.Entry<String, Double> doc : scoreMap.getEntry()) {
					double score = doc.getValue();
					IG[j] = score;
					j++;
				}
				Arrays.sort(IG);
				IG = ArrayUtility.reverseArray(IG);
				// Collections.reverse(Arrays.asList(IG));
				double[] DCG = getDCG(G);
				double[] IDCG = getDCG(IG);
				double[] NDCG = new double[size];

				for (int i = 0; i < size; i++) {
					NDCG[i] = DCG[i] / IDCG[i];
					line[i + 1] = Double.toString(NDCG[i]);
				}
				matrix[k] = line;
			}
			System.out.println(query);
			csWriter.writeMatrix(matrix);
		}

	}

	// get the NDCG
	private static double[] getDCG(double[] G) {
		int size = G.length;
		// initial CG
		double[] CG = new double[size];
		CG[0] = G[0];
		for (int i = 1; i < G.length; i++) {
			CG[i] = CG[i - 1] + G[i];
		}
		// set b
		int b = 2;
		// initial DCG
		double[] DCG = new double[size];
		// DCG[0] = CG[0];
		for (int i = 0; i < size; i++) {
			if (i + 1 < b)
				DCG[i] = CG[i];
			else {
				// System.out.println("***"+(Math.log(i+1)/Math.log10(2)));
				DCG[i] = (double) DCG[i - 1] + (double) G[i]
						/ (Math.log(i + 1) / Math.log(2));
			}
		}
		return DCG;
	}

	private void drawNDCG(String query, String csvPath, String destPath) {
		CsvUtility csvUtility = new CsvUtility();
		String[][] ndcgs = csvUtility.readNDCG(csvPath);
		lineChart linechart = new lineChart(query);
		for (int i = 0; i < ndcgs.length; i++) {
			String[] result = ndcgs[i];
			linechart.addData(result);
		}
		linechart.output(query, destPath);

	}

	public void outputNDCG() {
		ArrayList<File> files = FileFinder.GetAllFiles(ndcgPath, "csv", false);
		for (File file : files) {
			String query = file.getName().replace(".csv", "");
			System.out.println(query);
			drawNDCG(query, file.getPath(), this.graphicPath + query + ".png");
		}
	}

	// this function is used to test Algorithms Query by Query
	public HashMap<String, Double> aveAlgorithm() {
		HashMap<String, Double> aveMap = new HashMap<String, Double>();
		for (String query : queries) {
			double aveNDCG = 0;
			for (int method : Methods) {
				aveNDCG += calNDCGAtK(10, query, method);
			}
			aveNDCG = (double) aveNDCG / Methods.size();
			aveMap.put(query, aveNDCG);
		}
		return aveMap;
	}

	public void testALLQM() {
		for (int method : Methods)
			testQM(method);
	}

	// test TS with respect to SS
	public void CompareMethod(int m1, int m2) {
		JudgeMap rMap = new JudgeMap();
		try {
			ps = new PrintStream(disPath + ALGORITHM.getAlgorithm(m1) + "_"
					+ ALGORITHM.getAlgorithm(m2) + ".txt");
			ArrayList<String> upSet = new ArrayList<String>();
			ArrayList<String> downSet = new ArrayList<String>();
			for (String query : queries) {
				double ndcg_ts = calNDCGAtK(10, query, m1);
				double ndcg_ss = calNDCGAtK(10, query, m2);
				if (ndcg_ts >= ndcg_ss) {
					upSet.add(query);
				} else
					downSet.add(query);
			}
			for (String query : upSet) {
				HashMap<Integer, Server> servers = ResultReader
						.getServerInfo(query);
				int length = 0;
				for (Entry<Integer, Server> me : servers.entrySet()) {
					length += me.getValue().getResult_size();
				}
				ScoreMap sMap = rMap.getScoreMap(query);
				ps.println(query + " " + length + " " + sMap.getAveScore());
			}
			ps.println("===============================================================");
			for (String query : downSet) {
				HashMap<Integer, Server> servers = ResultReader
						.getServerInfo(query);
				int length = 0;
				for (Entry<Integer, Server> me : servers.entrySet()) {
					length += me.getValue().getResult_size();
				}
				ScoreMap sMap = rMap.getScoreMap(query);
				ps.println(query + " " + length + " " + sMap.getAveScore());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// test particular algorithm
	public void testQM(int method) {
		try {
			FileUtility.createDirectory(disPath);
			ps = new PrintStream(disPath + ALGORITHM.getAlgorithm(method)
					+ ".txt");
			ArrayList<String> upSet = new ArrayList<String>();
			ArrayList<String> downSet = new ArrayList<String>();
			HashMap<String, Double> aveMap = aveAlgorithm();
			for (String query : queries) {
				ScoreMap smap=judgeMap.getScoreMap(query);
				double ndcg = calNDCGAtK(10, query, method);
				if (ndcg >= aveMap.get(query)) {
					upSet.add(query+" "+smap.getAveScore()+" "+smap.getScoreDv());
				} else
					downSet.add(query+" "+smap.getAveScore()+" "+smap.getScoreDv());
			}
			for (String query : upSet) {
				ps.println(query);
			}
			ps.println("===============================================================");
			for (String query : downSet) {
				ps.println(query);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void testRR() {
		Methods = new ArrayList<Integer>();
		Methods.add(ALGORITHM.LOCAL);
		Methods.add(ALGORITHM.OPRR);
		Methods.add(ALGORITHM.PRR);
		Methods.add(ALGORITHM.SPRR);
		int k = 10;
		for (int i = 0; i < 5; i++)
			Methods.add(ALGORITHM.SRR);

		CsvUtility csvut = new CsvUtility(this.rrPath + "RR_NDCG@" + k + ".csv");
		String[][] matrix = new String[Methods.size()][45];
		int cm = 0;
		for (int method : Methods) {
			if (method == ALGORITHM.SRR) {
				Configure configure = new Configure();
				configure.outputResult(method, queries);
			}
			ResultMap resultMap = new ResultMap(method);
			// get the number of queries
			int size = resultMap.getSize();
			String[] ndcgs = new String[size + 1];
			ndcgs[0] = ALGORITHM.getAlgorithm(method);
			int cq = 0;
			for (String query : queries) {

				ndcgs[cq + 1] = Double.toString(calNDCGAtK(k, query, method));
				matrix[cm] = ndcgs;
				cq++;
			}
			cm++;
			// csvut.addLine(ALGORITHM.getAlgorithm(method), ndcgs);
		}
		csvut.writeMatrix(matrix);

	}

	// test the round robin on a query
	public void RRsingle() {
		ArrayList<Integer> Methods = new ArrayList<Integer>();
		Methods.add(ALGORITHM.LOCAL);
		Methods.add(ALGORITHM.PRR);
		Methods.add(ALGORITHM.SPRR);
		for (int i = 0; i < 5; i++)
			Methods.add(ALGORITHM.SRR);
		for (Entry<String, ScoreMap> list : judgeMap.getEntry()) {
			// iteration by query
			String query = list.getKey();
			if (query.equalsIgnoreCase("rsacs"))
				continue;
			CsvUtility csWriter = null;
			csWriter = new CsvUtility(rrPath + query + ".csv");
			String[][] matrix = new String[Methods.size()][];
			int c = 0;
			for (int k = 0; k < Methods.size(); k++) {
				int method = Methods.get(k);
				// get the result map
				ResultMap rMap = new ResultMap(method);
				ArrayList<String> ranking = (ArrayList<String>) rMap
						.getRanking(query);
				int size = ranking.size();
				String line[] = new String[size + 1];
				line[0] = ALGORITHM.getAlgorithm(method);
				if (line[0].equals("SRR")) {
					c++;
					line[0] = line[0] + c;
				}
				double[] G = new double[size];
				ScoreMap scoreMap = judgeMap.getScoreMap(query);

				if (!judgeMap.contains(query)) {
					System.err.println("query: " + query
							+ "has not been assesed");
					continue;
				}

				// initial score list G[]
				for (int i = 0; i < size; i++) {
					String docId = null;
					docId = ranking.get(i);
					G[i] = scoreMap.getScore(docId);
				}
				double[] IG = new double[size];
				int j = 0;
				for (Map.Entry<String, Double> doc : scoreMap.getEntry()) {
					double score = doc.getValue();
					IG[j] = score;
					j++;
				}
				Arrays.sort(IG);
				IG = ArrayUtility.reverseArray(IG);
				// Collections.reverse(Arrays.asList(IG));
				double[] DCG = getDCG(G);
				double[] IDCG = getDCG(IG);
				double[] NDCG = new double[size];

				for (int i = 0; i < size; i++) {
					NDCG[i] = DCG[i] / IDCG[i];
					line[i + 1] = Double.toString(NDCG[i]);
				}
				matrix[k] = line;
			}
			System.out.println(query);
			csWriter.writeMatrix(matrix);
			this.drawNDCG(query, rrPath + query + ".csv", rr_gPath + query
					+ ".png");
		}
	}

	public void drawNDCGAtK(int k, String name) {
		CsvUtility csvut = new CsvUtility(this.ndcgPath + name + ".csv");
		String[][] matrix = new String[Methods.size()][45];
		int cm = 0;
		for (int method : Methods) {
			matrix[cm] = NDCGAtK(k, method);
			cm++;
		}
		csvut.writeMatrix(matrix);
	}

	public String[] NDCGAtK(int k, int method) {

		// get the number of queries
		int size = queries.size();
		String[] ndcgs = new String[size + 1];
		ndcgs[0] = ALGORITHM.getAlgorithm(method);
		int cq = 0;
		for (String query : queries) {

			ndcgs[cq + 1] = Double.toString(calNDCGAtK(k, query, method));
			cq++;
		}
		return ndcgs;
	}

	// get the NDCG at K of a query and a particular method
	public static double calNDCGAtK(int k, String query, int method) {
		ResultMap resultMap = new ResultMap(method);
		System.out.println(ALGORITHM.getAlgorithm(method));
		ArrayList<String> ranking = resultMap.getRanking(query);
		System.out.println(query);
		int rsize = ranking.size();
		// use ndcg at 10, preserve the top 10
		// if(ranking.size()>=k)
		// ranking.subList(0, k-1);
		double[] G = new double[rsize];
		ScoreMap scoreMap = judgeMap.getScoreMap(query);

		// initial score list G[]
		for (int i = 0; i < rsize; i++) {
			String docId = null;
			docId = ranking.get(i);
			G[i] = scoreMap.getScore(docId);
		}
		double[] IG = new double[scoreMap.getSize()];
		int j = 0;
		for (Map.Entry<String, Double> doc : scoreMap.getEntry()) {
			double score = doc.getValue();
			IG[j] = score;
			j++;
		}
		Arrays.sort(IG);
		IG = ArrayUtility.reverseArray(IG);

		// Collections.reverse(Arrays.asList(IG));
		double[] DCG = getDCG(G);
		double[] IDCG = getDCG(IG);
		double[] NDCG = new double[rsize];
		for (int i = 0; i < rsize; i++) {
			NDCG[i] = DCG[i] / IDCG[i];
		}
		return NDCG[k - 1];
	}

	// get the precision of each algorithm
	public void precisionAtK(int k) {
		JudgeMap judgeMap = new JudgeMap();
		String[][] results = new String[Methods.size()][judgeMap.getSize()];
		int cm = 0;
		for (int method : Methods) {
			// get the result
			ResultMap resultMap = new ResultMap(method);
			String[] line = new String[resultMap.getSize() + 1];
			line[0] = ALGORITHM.getAlgorithm(method);
			int j = 1;
			for (Map.Entry<String, Object> result : resultMap.getEntry()) {
				String query = result.getKey();
				// exclude racas
				if (query.equalsIgnoreCase("rsacs"))
					continue;
				@SuppressWarnings("unchecked")
				ArrayList<String> ranking = (ArrayList<String>) result
						.getValue();
				if (ranking.size() < k)
					k = ranking.size();
				int count = 0;
				ScoreMap scoreMap = judgeMap.getScoreMap(query);
				int threshold = 0;
				for (int i = 0; i < k; i++) {
					String id = ranking.get(i);
					double score = scoreMap.getScore(id);
					if (score > threshold)
						count++;
				}
				double precision = (double) count / (double) k;
				line[j] = Double.toString(precision);
				j++;
			}
			results[cm] = line;
			cm++;
		}
		CsvUtility csWriter = new CsvUtility(precPath);
		csWriter.writeMatrix(results);
	}

	public static void main(String[] args) {

		// configure.fetchDataFromWeb();
		ArrayList<Integer> algorithms = new ArrayList<Integer>();
//		 algorithms.add(ALGORITHM.OPRR);
		algorithms.add(ALGORITHM.GDS_SS);
		algorithms.add(ALGORITHM.GDS_TS);
		algorithms.add(ALGORITHM.GDS_TSS);
		algorithms.add(ALGORITHM.GDS_DTSS);
		algorithms.add(ALGORITHM.LMS);
		algorithms.add(ALGORITHM.SRR);
		algorithms.add(ALGORITHM.PRR);
		algorithms.add(ALGORITHM.SPRR);
		algorithms.add(ALGORITHM.MW);
		algorithms.add(ALGORITHM.LOCAL);
		algorithms.add(ALGORITHM.OGDS);
//		algorithms.add(ALGORITHM.SGDS);
		Evaluator evaluator = new Evaluator(algorithms, false);
		evaluator.drawNDCGAtK(10, "NDCG@10");
//		 Evaluator evaluator = new Evaluator(algorithms, true);
		// ArrayList<String>[] groups=QueryUtility.groupQueries(4);
		// Evaluator evaluator=new Evaluator(algorithms,groups[0],false);
		// evaluator.drawNDCGAtK(10,"People_NDCG");
		// evaluator=new Evaluator(algorithms,groups[1],false);
		// evaluator.drawNDCGAtK(10,"Np_NDCG");
//		 evaluator.testALLQM();
		// evaluator.evaNDCG(algorithms);
		// evaluator.outputNDCG();
		// evaluator.drawNDCGAtK(10);
//		 evaluator.precisionAtK(10);
//		 evaluator.testRR();
		// evaluator.RRsingle();
		// evaluator.CompareMethod(ALGORITHM.LMS,ALGORITHM.GDS_DTSS);
		// evaluator.CompareMethod(ALGORITHM.SGDS,ALGORITHM.LMS);

	}
}
