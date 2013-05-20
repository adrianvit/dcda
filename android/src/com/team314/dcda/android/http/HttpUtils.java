package com.team314.dcda.android.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;

public class HttpUtils {

	public static final String central_ip = "192.168.0.101";
	public static final String central_path = "central/locate";
	public static final int central_port = 8080;
	public static final String local_login_path = "local/login";
	public static final String local_products= "local/products";
	public static final String local_users= "local/users";
	public static final int local_port = 8080;
	public static final String scheme  = "http";
	
	public static DefaultHttpClient getThreadSafeClient() {
		  
	    DefaultHttpClient client = new DefaultHttpClient();
	    HttpParams params = client.getParams();
	    ClientConnectionManager mgr = client.getConnectionManager();
		ThreadSafeClientConnManager threadSafeClientConnectionManager = new ThreadSafeClientConnManager(params, mgr.getSchemeRegistry());
	    
	    client = new DefaultHttpClient( threadSafeClientConnectionManager, params);
	  
	    return client;
	} 
	
	public static String readInputStream(InputStream input) throws IOException
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(input));
		StringBuilder temp = new StringBuilder();
		String line = null;
		try
		{
			while((line=in.readLine())!=null)
			{
				temp.append(line+"\n");
			}
		}catch(IOException e)
		{
			throw e;
		}finally
		{
			try
			{		
				in.close();
			}catch(IOException e)
			{
				throw e;
			}
		}
		return temp.toString();
	}
}
