package com.util;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.datatype.ServerSource;
import com.resultpool.Server;
import com.results.ContactResult;
import com.results.DSpaceResult;
import com.results.LibcataResult;
import com.results.MapResult;
import com.results.ResearcherResult;
import com.results.Result;
import com.results.StudyAtResult;
import com.results.TwitterResult;
import com.results.WebResult;
import com.results.YoutubeResult;

public class ReadtoObject {

	public ReadtoObject() {
		
		}
	public static Result ReadtoResult(String path)
	{
		FileInputStream fis;
		Result result=null;
		try {
			fis = new FileInputStream(path);
			BufferedInputStream bis = new BufferedInputStream(fis);
			XMLDecoder xmlDecoder = new XMLDecoder(bis);
			result = (Result) xmlDecoder.readObject();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
		
	}
	public static Result ReadtoResult(String path,int server)
	{
		FileInputStream fis;
		Result result=null;
		
		try {
			fis = new FileInputStream(path);
			BufferedInputStream bis = new BufferedInputStream(fis);
			XMLDecoder xmlDecoder = new XMLDecoder(bis);
			switch(server)
			{
			case ServerSource.CONTACT:
				result = (ContactResult) xmlDecoder.readObject();
				break;
			case ServerSource.DSPACE:
				result = (DSpaceResult) xmlDecoder.readObject();
				break;
			case ServerSource.LIBRARY:
				result = (LibcataResult) xmlDecoder.readObject();
				break;
			case ServerSource.MAP:
				result = (MapResult) xmlDecoder.readObject();
				break;
			case ServerSource.RES_PROJECTS:
				result = (ResearcherResult) xmlDecoder.readObject();
				break;
			case ServerSource.RES_PUBLICATIONS:
				result = (ResearcherResult) xmlDecoder.readObject();
				break;
			case ServerSource.RESEARCHERS:
				result = (ResearcherResult) xmlDecoder.readObject();
				break;
			case ServerSource.STUDYAT:
				result = (StudyAtResult) xmlDecoder.readObject();
				break;
			case ServerSource.TWITTER:
				result = (TwitterResult) xmlDecoder.readObject();
				break;
			case ServerSource.WEB:
				result = (WebResult) xmlDecoder.readObject();
				break;
			case ServerSource.YOUTUBE:
				result = (YoutubeResult) xmlDecoder.readObject();
				break;
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	public static Server ReadtoServer(String path)
	{
		FileInputStream fis;
		Server server=null;
		try {
			fis = new FileInputStream(path);
			BufferedInputStream bis = new BufferedInputStream(fis);
			XMLDecoder xmlDecoder = new XMLDecoder(bis);
			server = (Server) xmlDecoder.readObject();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return server;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
