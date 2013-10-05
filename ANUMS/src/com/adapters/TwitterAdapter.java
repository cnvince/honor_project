package com.adapters;

//no duplication needed
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.resultpool.RankList;
import com.resultpool.ResultTable;
import com.resultpool.Server;
import com.results.TwitterResult;
import com.util.DocumentSet;


public class TwitterAdapter extends Adapter {


	public TwitterAdapter(CountDownLatch countDownLatch, Document document,
			ResultTable results, HashMap<Integer, Server> serverTable,
			String hostUrl, int source) {
		super(countDownLatch, document, results, serverTable, hostUrl, source);
		// TODO Auto-generated constructor stub
	}

	@Override
	// xpath version
	public RankList query() {
		// TODO Auto-generated method stub
		if (document == null)
			return null;
		RankList ranklist = new RankList();
		try {
			NodeList nodeList = (NodeList) xpath.evaluate(
					"//DIV[@class=\"stream search-stream\"]/OL/LI/DIV",
					document, XPathConstants.NODESET);
			int size = 0;
			if (nodeList != null)
				size = nodeList.getLength();
			Server server = new Server();
			server.setServer(source);
			server.setResult_size(size);
			sTable.put(source, server);
			System.out
					.println("=======================\n twitter size:" + size);
			int resultsize = 0;
			for (int i = 0; i < size; i++) {
				TwitterResult result = new TwitterResult();
				Element Node_tweet = (Element) nodeList.item(i);
				Element info_Ele=(Element) xpath.evaluate("DIV[@class=\"content\"]/DIV[@class=\"stream-item-header\"]/SMALL[@class=\"time\"]/A", Node_tweet, XPathConstants.NODE);
				if(info_Ele==null)
					continue;
				String link = this.hostUrl + info_Ele.getAttribute("href");
//				System.err.println(link);
				Node content = (Node) xpath.evaluate(
						"DIV[@class=\"content\"]/P", Node_tweet,
						XPathConstants.NODE);
				String title = content.getTextContent().trim();
//				System.err.println("Tweet:"+title);
				if (!DocumentSet.contains(link)) {
					result.setTitle(title);
					result.setLink(link);
					result.setSource(source);
					ranklist.addResult(result);
					resultsize++;
				}
				DocumentSet.AddDocument(link);
				if (resultsize >= 10)
					break;
			}

		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ranklist;
	}

	// @Override
	// public RankList query() {
	// RankList list = new RankList();
	// ConfigurationBuilder cb = new ConfigurationBuilder();
	// cb.setDebugEnabled(true)
	// .setOAuthConsumerKey("CLYDPzGfbZx77vvsflcEig")
	// .setOAuthConsumerSecret("osU7pwmEKanwIwnp8V7azkw0rNyozcEyVLVgRdIgzEY")
	// .setOAuthAccessToken("407380679-3ZGp2ZBAb5aWlvJqOKdOIMiWB8jOex6HszqbAlLB")
	// .setOAuthAccessTokenSecret("9uGp83ov4yTBbz1jRKiu3EcSb4O4y64cOJbCErlf4");
	// TwitterFactory tf = new TwitterFactory(cb.build());
	// Twitter twitter = tf.getInstance();
	// // The factory instance is re-useable and thread safe.
	// // Twitter twitter = TwitterFactory.getSingleton();
	// try {
	// String qString=Query+" from:ANUmedia";
	// Query query = new Query(qString);
	// System.out.println(query.getQuery());
	// QueryResult result;
	// query.setCount(3000);
	// result = twitter.search(query);
	// Server server = new Server();
	// server.setServer(source);
	// server.setResult_size(result.getTweets().size());
	// // sTable.put(source, server);
	// HashSet<String> tweets = new HashSet<String>();
	// System.out.println(result.getTweets().size());
	// // List<Status> tweets = result.getTweets();
	// for (Status tweet : result.getTweets()) {
	// if (!tweets.contains(tweet.getText())) {
	// tweets.add(tweet.getText());
	// TwitterResult tresult = new TwitterResult();
	// tresult.setTitle(tweet.getText());
	// tresult.setSource(source);
	// tresult.setDisplaySummary(tweet.getUser() + " "
	// + tweet.getSource());
	// long id=tweet.getId();
	// String user=tweet.getUser().getName();
	// String link="http://twitter.com/"+user.replaceAll(" ", "")+"/status/"+id;
	// tresult.setLink(link);
	// System.out.println("=======================\nTitle:"+tweet.getText()+"@"
	// + tweet.getUser() + ":"
	// + tweet.getText());
	// list.addResult(tresult);
	// if (tweets.size() >= 10)
	// break;
	// }
	// }
	// } catch (TwitterException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// return list;
	// }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TwitterAdapter twAdapter = new TwitterAdapter(null, null, null, null,
				null, 0);
		twAdapter.query();
	}

}
