package com.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class CsvUtility {

	private String csvPath;
	private PrintStream ps = null;

	public CsvUtility(String path) {
		this.csvPath = path;
		try {
			ps = new PrintStream(csvPath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public CsvUtility() {

	}

	public void writeMatrix(String[][] matrix) {
		for (int i = 0; i < matrix[0].length; i++) {
			String line[] = new String[matrix.length];
			for (int j = 0; j < matrix.length; j++)
				line[j] = matrix[j][i];
			this.append(line);

		}
	}

	@SuppressWarnings({ "resource", "null" })
	public static List<String[]> readAll(String path) {
		BufferedReader br = null;
		List<String[]> results = new ArrayList<String[]>();
		String sCurrentLine;
		try {
			br = new BufferedReader(new FileReader(path));
			while ((sCurrentLine = br.readLine()) != null) {
					String words[] = sCurrentLine.split(",");
					results.add(words);
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;
	}

	public void initialHead(int size) {
		String[] head = new String[size + 1];
		head[0] = "Method";
		for (int i = 1; i <= size; i++)
			head[i] = Integer.toString(i);
		append(head);
	}

	public void addLine(String head, double[] ndcg) {
		String[] line = new String[ndcg.length + 1];
		line[0] = head;
		for (int i = 1; i <= ndcg.length; i++)
			line[i] = Double.toString(ndcg[i - 1]);
		append(line);
	}

	public void append(String[] line) {
		for (int i = 0; i < line.length - 1; i++)
			ps.print(line[i] + ",");
		ps.print(line[line.length - 1] + "\n");
	}

	// return a hashmap for a query with method and its ndcg
	@SuppressWarnings("resource")
	public String[][] readNDCG(String path) {
		BufferedReader br = null;
		String[][] results = null;
		String sCurrentLine;
		// ArrayList<String[]> results=new ArrayList<String[]>();
		try {
			// csvPath="Evaluation/Results/CSV/NDCG/"+query+".csv";
			System.out.println(path);
			br = new BufferedReader(new FileReader(path));
			int lines = getLines(path);
			// read head
			if ((sCurrentLine = br.readLine()) != null) {
				String heads[] = sCurrentLine.split(",");
				results = new String[heads.length][lines];
				for (int i = 0; i < heads.length; i++) {
					results[i][0] = heads[i];
				}
			}
			int j = 1;
			while ((sCurrentLine = br.readLine()) != null) {
				String[] figs = sCurrentLine.split(",");
				for (int i = 0; i < figs.length; i++)
					results[i][j] = figs[i];
				j++;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;
	}

	private int getLines(String csvPath) {
		BufferedReader br = null;
		// csvPath="Evaluation/Results/CSV/NDCG/"+query+".csv";
		int length = 0;
		try {
			br = new BufferedReader(new FileReader(csvPath));
			while (br.readLine() != null)
				length++;
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return length;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
