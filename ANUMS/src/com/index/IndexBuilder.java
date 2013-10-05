/**
 * 
 */
package com.index;

//Index documents locally, some non-relevant documents may be reduced

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import com.resultpool.RankList;
import com.resultpool.ResultTable;
import com.results.Result;
import com.util.Configure;

/**
 * @author user
 * 
 */
public class IndexBuilder {

	// add doc to the indexweiter, document content as filed
	private static Document addDoc(IndexWriter w, String ID, String Title,
			String content) {

		Document doc = new Document();
		doc.add(new Field("ID", ID, TextField.TYPE_STORED));
		doc.add(new Field("Title", Title, TextField.TYPE_STORED));
		doc.add(new Field("Content", content, TextField.TYPE_STORED));
		try {
			w.addDocument(doc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doc;
	}

	private static Directory storeIndex(ArrayList<Result> results) {
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_44);
		Directory directory = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_44,
				analyzer);
		IndexWriter iwriter;
		BufferedReader br = null;
		try {
			iwriter = new IndexWriter(directory, config);
			for (Result result : results) {
				String Title = result.getTitle();
				String docid = result.getDocID();
				String docPath = Configure.PagePath + docid + ".html";
				String sCurrentLine;
				File file=new File(docPath);
				String content=null;
				StringBuilder sb = new StringBuilder();
				org.jsoup.nodes.Document doc=null;
				if(file.exists())
				{
					br = new BufferedReader(new FileReader(docPath));
					while ((sCurrentLine = br.readLine()) != null) {
						sb.append(sCurrentLine + "\n");
					}
					content = sb.toString();
				}
				else
				{
//					page not found, set to empty string
					content="";
//					System.out.println(result.getLink());
//					in some case pages are not downloaded in previous stages
//					doc = Jsoup.connect(result.getLink()).get();
				}
				sCurrentLine = Jsoup
						.clean(content, Whitelist.basicWithImages());
				 doc= Jsoup.parse(content);

				if (doc != null) {
					// System.out.println(doc.text());
					// System.out.println(bodyhtml.getTextContent());
					addDoc(iwriter, docid, Title, doc.text());

				}
			}
			iwriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}

		return directory;
	}

	public static ArrayList<Result> getResult(ResultTable results, String query) {
		HashMap<Integer, RankList> lists = results.getTable();
		ArrayList<Result> resultlist = new ArrayList<Result>();
		ArrayList<Result> mergedList = new ArrayList<Result>();
//	Used to store the documents retrieved by lucene
		HashSet<String> retrievedDocs = new HashSet<String>();
		for (Map.Entry<Integer, RankList> list : lists.entrySet()) {
			ArrayList<Result> clist = list.getValue().getList();
			resultlist.addAll(clist);
		}
		Directory directory = storeIndex(resultlist);
		DirectoryReader ireader;
		try {
			ireader = DirectoryReader.open(directory);
			IndexSearcher isearcher = new IndexSearcher(ireader);
			QueryParser parser = new QueryParser(Version.LUCENE_44, "Content",
					new StandardAnalyzer(Version.LUCENE_44));
			Query q = null;
			q = parser.parse(query);
			TopDocs docs = isearcher.search(q, 100);
			ScoreDoc[] filterScoreDosArray = docs.scoreDocs;
			for (int i = 0; i < filterScoreDosArray.length; ++i) {
				int docId = filterScoreDosArray[i].doc;
				Document d = isearcher.doc(docId);
				// System.out.println((i + 1) + ". " + d.get("ID") + " Score: "
				// + filterScoreDosArray[i].score);
				String docID=d.get("ID");
				retrievedDocs.add(docID);
				Result result = new Result();
				result.setDocID(docID);
				mergedList.add(result);
			}
//			fit the list with non-relevant documents
			for(Result result:resultlist)
			{
				if(!retrievedDocs.contains(result.getDocID()))
				{
					mergedList.add(result);
				}
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mergedList;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// IndexBuilder indexBuilder=new IndexBuilder();

	}

}
