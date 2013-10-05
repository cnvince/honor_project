package com.adapters;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

public class twitterTest {

	public twitterTest() {
		// TODO Auto-generated constructor stub
	}
	public void search()
	{
		ConfigurationBuilder cb = new ConfigurationBuilder();
	    cb.setDebugEnabled(true)
	           .setOAuthConsumerKey("CLYDPzGfbZx77vvsflcEig")
		  .setOAuthConsumerSecret("osU7pwmEKanwIwnp8V7azkw0rNyozcEyVLVgRdIgzEY")
		  .setOAuthAccessToken("407380679-3ZGp2ZBAb5aWlvJqOKdOIMiWB8jOex6HszqbAlLB")
		  .setOAuthAccessTokenSecret("9uGp83ov4yTBbz1jRKiu3EcSb4O4y64cOJbCErlf4");
	    TwitterFactory tf = new TwitterFactory(cb.build());
	    Twitter twitter = tf.getInstance();
	        try {
	            Query query = new Query("Brian Schmidt from:ANUMEDIA");
	            QueryResult result;
	            result = twitter.search(query);
	            List<Status> tweets = result.getTweets();
	            for (Status tweet : tweets) {
	                System.out.println("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
	            }

	            System.exit(0);
	        } catch (TwitterException te) {
	            te.printStackTrace();
	            System.out.println("Failed to search tweets: " + te.getMessage());
	            System.exit(-1);
	        }
	}
	public static void main(String args[]) throws Exception{
		twitterTest ttest=new twitterTest();
		ttest.search();
	}
//	    // The factory instance is re-useable and thread safe.
//	    Twitter twitter = TwitterFactory.getSingleton();
//	    twitter.setOAuthConsumer("CLYDPzGfbZx77vvsflcEig", "osU7pwmEKanwIwnp8V7azkw0rNyozcEyVLVgRdIgzEY");
//	    RequestToken requestToken = twitter.getOAuthRequestToken();
//	    AccessToken accessToken = null;
//	    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//	    while (null == accessToken) {
//	      System.out.println("Open the following URL and grant access to your account:");
//	      System.out.println(requestToken.getAuthorizationURL());
//	      System.out.print("Enter the PIN(if aviailable) or just hit enter.[PIN]:");
//	      String pin = br.readLine();
//	      try{
//	         if(pin.length() > 0){
//	           accessToken = twitter.getOAuthAccessToken(requestToken, pin);
//	         }else{
//	           accessToken = twitter.getOAuthAccessToken();
//	         }
//	      } catch (TwitterException te) {
//	        if(401 == te.getStatusCode()){
//	          System.out.println("Unable to get the access token.");
//	        }else{
//	          te.printStackTrace();
//	        }
//	      }
//	    }
//	    //persist to the accessToken for future reference.
//	    storeAccessToken((int) twitter.verifyCredentials().getId() , accessToken);
//	    Status status = twitter.updateStatus("running api");
//	    System.out.println("Successfully updated the status to [" + status.getText() + "].");
//	    System.exit(0);
//	  }
//	  private static void storeAccessToken(int useId, AccessToken accessToken){
//	    //store accessToken.getToken()
//	    //store accessToken.getTokenSecret()
//	  }
}
