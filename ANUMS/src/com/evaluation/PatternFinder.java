package com.evaluation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.datatype.ALGORITHM;
import com.datatype.ServerSource;
import com.resultpool.ResultTable;
import com.resultpool.Server;
import com.results.ResultReader;
import com.util.FileUtility;
import com.util.QueryUtility;

public class PatternFinder {
	public ArrayList<Integer> AllMethods;
	private final String qlPath="Evaluation/Results/GRAPHICS/PATTERNS/Query_Length/";
	private final String dlPath="Evaluation/Results/GRAPHICS/PATTERNS/Document_Length/";
	private final String scorePath="Evaluation/Results/GRAPHICS/PATTERNS/Score/";
	private final String serverPath="Evaluation/Results/GRAPHICS/PATTERNS/Server/";
	private final String collectionPath="Evaluation/Results/GRAPHICS/PATTERNS/Collection/";
	private final String sdPath="Evaluation/Results/GRAPHICS/PATTERNS/StandardDiv/";
	public PatternFinder() {
		AllMethods=new ArrayList<Integer>();
		AllMethods.add(ALGORITHM.GDS_SS);
		AllMethods.add(ALGORITHM.GDS_TS);
		AllMethods.add(ALGORITHM.GDS_TSS);
		AllMethods.add(ALGORITHM.GDS_DTSS);
		AllMethods.add(ALGORITHM.LMS);
		AllMethods.add(ALGORITHM.SRR);
		AllMethods.add(ALGORITHM.PRR);
		AllMethods.add(ALGORITHM.SPRR);
		AllMethods.add(ALGORITHM.MW);
		AllMethods.add(ALGORITHM.LOCAL);
		FileUtility.createDirectory(qlPath);
		FileUtility.createDirectory(dlPath);
		FileUtility.createDirectory(scorePath);
		FileUtility.createDirectory(serverPath);
		FileUtility.createDirectory(collectionPath);
		FileUtility.createDirectory(sdPath);
	}
	public void qLengthLine(int method)
	{
		XYSeries series = new XYSeries(ALGORITHM.getAlgorithm(method));
		XYSeries series2 =new XYSeries("Average");
		ArrayList<String> queries=QueryUtility.initialQueries();
		int max=9;
		for(int i=1;i<=max;i++)
		{
			double aveCurrent=0;
			double aveAve=0;
			int count=0;
			for(String query:queries)
			{
				if(query.length()==i)
				{
					aveCurrent+=Evaluator.calNDCGAtK(10, query, method);
					double aveN=0;
					for(int Method:AllMethods)
					{
						aveN+=Evaluator.calNDCGAtK(10, query, Method);
					}
					aveN=aveN/AllMethods.size();
					aveAve+=aveN;
					count++;
				}
			}
			if(count!=0)
			{
				aveCurrent=aveCurrent/count;
				aveAve=aveAve/count;
				series.add(i, aveCurrent);
				series2.add(i, aveAve);
				
			}
		}
		XYSeriesCollection data = new XYSeriesCollection();
		data.addSeries(series);
		data.addSeries(series2);
		JFreeChart chart = ChartFactory.createXYLineChart("Methods across different queries", "Query Length", "NDCG@10", data,
		                                                  PlotOrientation.VERTICAL, 
		                                                  true, true, false);

		// Create an NumberAxis
		NumberAxis xAxis = new NumberAxis();
		xAxis.setTickUnit(new NumberTickUnit(1));

		// Assign it to the chart
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setDomainAxis(xAxis);
		File lineChart=new File(qlPath+ALGORITHM.getAlgorithm(method)+".png");              
        try {
			ChartUtilities.saveChartAsPNG(lineChart,chart,800,600);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	public void dlLenghthLine(int method)
	{
		int tick=1000;
		XYSeries series = new XYSeries(ALGORITHM.getAlgorithm(method));
		XYSeries series2 =new XYSeries("Average");
		ArrayList<String> queries=QueryUtility.initialQueries();
		HashMap<Integer,ArrayList<String>> DLmap=new HashMap<Integer,ArrayList<String>>();
		for(String query:queries)
		{
			HashMap<Integer, Server> servers = ResultReader
					.getServerInfo(query);
			int length = 0;
			for (Entry<Integer, Server> me : servers.entrySet()) {
				length += me.getValue().getResult_size();
			}
			
			length=tick*(length/tick);
			if(DLmap.containsKey(length))
			{
				DLmap.get(length).add(query);
			}
			else
			{
				ArrayList<String> list=new ArrayList<String>();
				list.add(query);
				DLmap.put(length, list);
			}
		}
		for(Map.Entry<Integer, ArrayList<String>> me:DLmap.entrySet())
		{
			int length=me.getKey();
			ArrayList<String> subQueries=me.getValue();
			double aveNDCG=0;
			double aveAve=0;
			for(String query:subQueries)
			{
				aveNDCG+=Evaluator.calNDCGAtK(10, query, method);
				double aveN=0;
				for(int Method:AllMethods)
				{
					aveN+=Evaluator.calNDCGAtK(10, query, Method);
				}
				aveN=aveN/AllMethods.size();
				aveAve+=aveN;
			}
			aveNDCG=aveNDCG/subQueries.size();
			aveAve=aveAve/subQueries.size();
			series.add(length, aveNDCG);
			series2.add(length,aveAve);
		}
		
		
		
		XYSeriesCollection data = new XYSeriesCollection();
		data.addSeries(series);
		data.addSeries(series2);
		JFreeChart chart = ChartFactory.createXYLineChart("Methods across different queries", "Document Length", "NDCG@10", data,
		                                                  PlotOrientation.VERTICAL, 
		                                                  true, true, false);

		// Create an NumberAxis
		NumberAxis xAxis = new NumberAxis();
		xAxis.setTickUnit(new NumberTickUnit(tick));

		// Assign it to the chart
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setDomainAxis(xAxis);
		File lineChart=new File(dlPath+ALGORITHM.getAlgorithm(method)+".png");              
        try {
			ChartUtilities.saveChartAsPNG(lineChart,chart,800,600);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	public void scoreLine(int method)
	{
		XYSeries series = new XYSeries(ALGORITHM.getAlgorithm(method));
		XYSeries series2 =new XYSeries("Average");
		ArrayList<String> queries=QueryUtility.initialQueries();
		HashMap<Integer,ArrayList<String>> DLmap=new HashMap<Integer,ArrayList<String>>();
		JudgeMap jMap=new JudgeMap(); 
		for(String query:queries)
		{
			HashMap<Integer, Server> servers = ResultReader
					.getServerInfo(query);
			
			ScoreMap sMap=jMap.getScoreMap(query);
			double score=sMap.getAveScore();
			int key=0;
			if(score>=0&&score<=1)
				key=1;
			else if(score>1&&score<2)
				key=2;
			else if(score>2&&score<=3)
				key=3;
			else if(score>3&&score<=4)
				key=4;
				
			if(DLmap.containsKey(key))
			{
				DLmap.get(key).add(query);
			}
			else
			{
				ArrayList<String> list=new ArrayList<String>();
				list.add(query);
				DLmap.put(key, list);
			}
		}
		for(Map.Entry<Integer, ArrayList<String>> me:DLmap.entrySet())
		{
			double score=me.getKey();
			ArrayList<String> subQueries=me.getValue();
			double aveNDCG=0;
			double aveAve=0;
			for(String query:subQueries)
			{
				aveNDCG+=Evaluator.calNDCGAtK(10, query, method);
				double aveN=0;
				for(int Method:AllMethods)
				{
					aveN+=Evaluator.calNDCGAtK(10, query, Method);
				}
				aveN=aveN/AllMethods.size();
				aveAve+=aveN;
			}
			aveNDCG=aveNDCG/subQueries.size();
			aveAve=aveAve/subQueries.size();
			series.add(score, aveNDCG);
			series2.add(score,aveAve);
		}
		
		
		
		XYSeriesCollection data = new XYSeriesCollection();
		data.addSeries(series);
		data.addSeries(series2);
		JFreeChart chart = ChartFactory.createXYLineChart("Methods across different queries", "Average Document Score", "NDCG@10", data,
		                                                  PlotOrientation.VERTICAL, 
		                                                  true, true, false);
		// Create an NumberAxis
		NumberAxis xAxis = new NumberAxis();
		xAxis.setTickUnit(new NumberTickUnit(1));

		// Assign it to the chart
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setDomainAxis(xAxis);
		File lineChart=new File(scorePath+ALGORITHM.getAlgorithm(method)+".png");              
        try {
			ChartUtilities.saveChartAsPNG(lineChart,chart,800,600);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	
	public void serverLine(int method)
	{
		XYSeries series = new XYSeries(ALGORITHM.getAlgorithm(method));
		XYSeries series2 =new XYSeries("Average");
		ArrayList<String> queries=QueryUtility.initialQueries();
		HashMap<Integer,ArrayList<String>> DLmap=new HashMap<Integer,ArrayList<String>>();
		JudgeMap jMap=new JudgeMap(); 
		for(String query:queries)
		{
			int key=ResultReader.getServerInfo(query).size();
			if(DLmap.containsKey(key))
			{
				DLmap.get(key).add(query);
			}
			else
			{
				ArrayList<String> list=new ArrayList<String>();
				list.add(query);
				DLmap.put(key, list);
			}
		}
		for(Map.Entry<Integer, ArrayList<String>> me:DLmap.entrySet())
		{
			double score=me.getKey();
			ArrayList<String> subQueries=me.getValue();
			double aveNDCG=0;
			double aveAve=0;
			for(String query:subQueries)
			{
				aveNDCG+=Evaluator.calNDCGAtK(10, query, method);
				double aveN=0;
				for(int Method:AllMethods)
				{
					aveN+=Evaluator.calNDCGAtK(10, query, Method);
				}
				aveN=aveN/AllMethods.size();
				aveAve+=aveN;
			}
			aveNDCG=aveNDCG/subQueries.size();
			aveAve=aveAve/subQueries.size();
			series.add(score, aveNDCG);
			series2.add(score,aveAve);
		}
		
		
		
		XYSeriesCollection data = new XYSeriesCollection();
		data.addSeries(series);
		data.addSeries(series2);
		JFreeChart chart = ChartFactory.createXYLineChart("Methods across different queries", "Number of Collections", "NDCG@10", data,
		                                                  PlotOrientation.VERTICAL, 
		                                                  true, true, false);
		// Create an NumberAxis
		NumberAxis xAxis = new NumberAxis();
		xAxis.setTickUnit(new NumberTickUnit(1));

		// Assign it to the chart
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setDomainAxis(xAxis);
		File lineChart=new File(serverPath+ALGORITHM.getAlgorithm(method)+".png");              
        try {
			ChartUtilities.saveChartAsPNG(lineChart,chart,800,600);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	public void sdLine(int method)
	{
		XYSeries series = new XYSeries(ALGORITHM.getAlgorithm(method));
		XYSeries series2 =new XYSeries("Average");
		ArrayList<String> queries=QueryUtility.initialQueries();
		HashMap<Double,ArrayList<String>> DLmap=new HashMap<Double,ArrayList<String>>();
		JudgeMap jMap=new JudgeMap(); 
		for(String query:queries)
		{
			HashMap<Integer, Server> servers = ResultReader
					.getServerInfo(query);
			
			ScoreMap sMap=jMap.getScoreMap(query);
			double key=sMap.getScoreDv();
			key=0.2*(key/0.2+1);
				
			if(DLmap.containsKey(key))
			{
				DLmap.get(key).add(query);
			}
			else
			{
				ArrayList<String> list=new ArrayList<String>();
				list.add(query);
				DLmap.put(key, list);
			}
		}
		for(Map.Entry<Double, ArrayList<String>> me:DLmap.entrySet())
		{
			double score=me.getKey();
			ArrayList<String> subQueries=me.getValue();
			double aveNDCG=0;
			double aveAve=0;
			for(String query:subQueries)
			{
				aveNDCG+=Evaluator.calNDCGAtK(10, query, method);
				double aveN=0;
				for(int Method:AllMethods)
				{
					aveN+=Evaluator.calNDCGAtK(10, query, Method);
				}
				aveN=aveN/AllMethods.size();
				aveAve+=aveN;
			}
			aveNDCG=aveNDCG/subQueries.size();
			aveAve=aveAve/subQueries.size();
			series.add(score, aveNDCG);
			series2.add(score,aveAve);
		}
		
		
		
		XYSeriesCollection data = new XYSeriesCollection();
		data.addSeries(series);
		data.addSeries(series2);
		JFreeChart chart = ChartFactory.createXYLineChart("Methods across different queries", "Document Score Stnadard deviation", "NDCG@10", data,
		                                                  PlotOrientation.VERTICAL, 
		                                                  true, true, false);
		// Create an NumberAxis
		NumberAxis xAxis = new NumberAxis();
		xAxis.setTickUnit(new NumberTickUnit(1));

		// Assign it to the chart
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setDomainAxis(xAxis);
		File lineChart=new File(sdPath+ALGORITHM.getAlgorithm(method)+".png");              
        try {
			ChartUtilities.saveChartAsPNG(lineChart,chart,800,600);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	public void collectionLine(int method, int cid)
	{
		XYSeries series = new XYSeries(ALGORITHM.getAlgorithm(method));
		XYSeries series2 =new XYSeries("Average");
		ArrayList<String> queries=QueryUtility.initialQueries();
		HashMap<Integer,ArrayList<String>> DLmap=new HashMap<Integer,ArrayList<String>>();
		for(String query:queries)
		{
			int key=0;
			ResultTable table=ResultReader.getResultTable(query);
			if(table.getTable().get(cid)!=null)
				key=table.getTable().get(cid).getList().size();
			
			if(DLmap.containsKey(key))
			{
				DLmap.get(key).add(query);
			}
			else
			{
				ArrayList<String> list=new ArrayList<String>();
				list.add(query);
				DLmap.put(key, list);
			}
		}
		for(Map.Entry<Integer, ArrayList<String>> me:DLmap.entrySet())
		{
			double score=me.getKey();
			ArrayList<String> subQueries=me.getValue();
			double aveNDCG=0;
			double aveAve=0;
			for(String query:subQueries)
			{
				aveNDCG+=Evaluator.calNDCGAtK(10, query, method);
				double aveN=0;
				for(int Method:AllMethods)
				{
					aveN+=Evaluator.calNDCGAtK(10, query, Method);
				}
				aveN=aveN/AllMethods.size();
				aveAve+=aveN;
			}
			aveNDCG=aveNDCG/subQueries.size();
			aveAve=aveAve/subQueries.size();
			series.add(score, aveNDCG);
			series2.add(score,aveAve);
		}
		
		
		
		XYSeriesCollection data = new XYSeriesCollection();
		data.addSeries(series);
		data.addSeries(series2);
		JFreeChart chart = ChartFactory.createXYLineChart("Methods across different queries", "Number of Collections", "NDCG@10", data,
		                                                  PlotOrientation.VERTICAL, 
		                                                  true, true, false);
		// Create an NumberAxis
		NumberAxis xAxis = new NumberAxis();
		xAxis.setTickUnit(new NumberTickUnit(1));

		// Assign it to the chart
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setDomainAxis(xAxis);
		File lineChart=new File(collectionPath+ALGORITHM.getAlgorithm(method)+".png");              
        try {
			ChartUtilities.saveChartAsPNG(lineChart,chart,800,600);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	
	
	
	
	public static void main(String[] args) {

		PatternFinder pf=new PatternFinder();
		for(int method:pf.AllMethods)
		{
			pf.sdLine(method);
//			pf.collectionLine(method, ServerSource.LIBRARY);
//			pf.serverLine(method);
//			pf.scoreLine(method);
//			pf.dlLenghthLine(method);
//			pf.qLengthLine(method);
		}
	}

}
