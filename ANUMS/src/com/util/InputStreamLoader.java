//////////////////////////////////////////////////////////////////////
//
// File:     InputStreamLoader.java
// Author:   Scott Sanner, University of Toronto (ssanner@cs.toronto.edu)
// Date:     9/1/2003
// Requires: comshell package
//
// Description:
//
//   Use to load an input stream from a file (or URL if not file).
//
//////////////////////////////////////////////////////////////////////

// Package definition
package com.util;

// Packages to import
import java.io.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

/**
 * @version   1.0
 * @author    Scott Sanner
 * @language  Java (JDK 1.3)
 **/
public class InputStreamLoader {

    /* Returns null of OpenStream failed */
    public static InputStream OpenStream(String surl) {

        InputStream in = null;
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
        HttpGet httpget = new HttpGet(surl);
        HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			 if (entity != null)
				 in = entity.getContent();
			 httpclient.getConnectionManager().shutdown();
			 
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			System.err.println(e1.getMessage());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.err.println(e1.getMessage());
		}
   
        return in;
    }
}
//	
//        try {
//            File ff = new File(surl);
//            in = new FileInputStream(ff);
//        }
//        catch (Exception ignore) {
//            try {
//                URL url = new URL(surl);
//                in = url.openStream();
//            }
//            catch (Exception e) {
//                System.err.println("Failed to open: '" + surl + "'");
//                return null;
//            }
//        }

