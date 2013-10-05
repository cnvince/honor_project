//mapping each urls of search result
package com.util;

import java.util.HashMap;

import com.datatype.ServerSource;

public class UrlMatcher {

	public UrlMatcher() {
		// TODO Auto-generated constructor stub
	}
	public static HashMap<Integer,String> match(String query)
	{
		HashMap<Integer,String> UrlCollection=new HashMap<Integer,String>();
		UrlCollection.put(ServerSource.CONTACT, "http://www.anu.edu.au/dirs/search.php?stype=Staff+Directory&querytext="+query);
		UrlCollection.put(ServerSource.LIBRARY, "http://library.anu.edu.au/search/Y?SEARCH="+query);
		UrlCollection.put(ServerSource.STUDYAT, "https://studyat.anu.edu.au/search?search_terms="+query);
		UrlCollection.put(ServerSource.WEB, "http://search.anu.edu.au/search/search.cgi?collection=anu_search&query="+query);
		UrlCollection.put(ServerSource.YOUTUBE, "http://www.youtube.com/user/ANUchannel/videos?query="+query);
		UrlCollection.put(ServerSource.MAP, "http://campusmap.anu.edu.au/search.asp?ss=1&school="+query);
		String twitterUrl=query+" from:ANUmedia";
		twitterUrl=StringFormat.toURL(twitterUrl);
		twitterUrl="https://twitter.com/search?src=typd&q="+twitterUrl+"&mode=realtime";
		UrlCollection.put(ServerSource.TWITTER, twitterUrl);
		UrlCollection.put(ServerSource.DSPACE, "https://digitalcollections.anu.edu.au/simple-search?query="+query);
		UrlCollection.put(ServerSource.RESEARCHERS, "https://researchers.anu.edu.au/search?query="+query+"&type=researcher");
		UrlCollection.put(ServerSource.RES_PUBLICATIONS, "https://researchers.anu.edu.au/search?query="+query+"&type=publication");
		UrlCollection.put(ServerSource.RES_PROJECTS, "https://researchers.anu.edu.au/search?query="+query+"&type=grant_project");
		return UrlCollection;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String twitterUrl="ACT from:ANUmedia";
		twitterUrl=StringFormat.toURL(twitterUrl);
		twitterUrl="https://twitter.com/search?src=typd&q="+twitterUrl+"&mode=realtime";
		System.out.println(twitterUrl);
	}

}
