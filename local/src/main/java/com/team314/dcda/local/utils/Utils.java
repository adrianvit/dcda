package com.team314.dcda.local.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import javax.ws.rs.core.HttpHeaders;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.team314.dcda.local.dao.LoggedUserDAO;


public class Utils {

	public static final String local_ip_jndi_name = "LocalIp";
	public static final String local_name_jndi_name = "LocalName";
	public static final String central_ip = "localhost";
	public static final String central_path = "central/locate";
	public static final String central_path_register = "central/register";
	public static final String scheme  = "http";
	public static final int central_port = 8080;
	private static final Logger LOG = LoggerFactory.getLogger(Utils.class);
	
	public static DefaultHttpClient getThreadSafeClient() {
		  
	    DefaultHttpClient client = new DefaultHttpClient();
	    HttpParams params = client.getParams();
		ThreadSafeClientConnManager threadSafeClientConnectionManager = new ThreadSafeClientConnManager();
	    
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
	
	
	public static String http_get(URI uri) throws ClientProtocolException, IOException
	{
		try
		{
			HttpClient httpclient = Utils.getThreadSafeClient();
			HttpResponse response = null;
			HttpGet get = new HttpGet(uri);
			response = httpclient.execute(get);
			HttpEntity entity = response.getEntity();
			String result = Utils.readInputStream(entity.getContent());
			if(response.getStatusLine().getStatusCode()==200)
			{
				return result;			
			}
			else
			{
				return null;
			}
		}catch(Exception e)
		{
			return null;
		}
	}
	
	
	public static String checkRegistrationEmail(URI uri) throws ClientProtocolException, IOException, EmailException
	{
		HttpResponse response = null;
		String result = null;
		try
		{
			HttpClient httpclient = Utils.getThreadSafeClient();
			HttpPost get = new HttpPost(uri);
			response = httpclient.execute(get);
			HttpEntity entity = response.getEntity();
			result = Utils.readInputStream(entity.getContent());
		}catch(Exception e)
		{
			return null;
		}

		if(response.getStatusLine().getStatusCode()==200)
		{
			return result;			
		}
		else if(response.getStatusLine().getStatusCode()==500)
		{
			throw new EmailException();
		}
		else
		{
			return null;
		}
	}
	
	
	public static boolean validateToken(int id, HttpHeaders headers, LoggedUserDAO loggedUserDao, String role) throws UnauthorizedException, ForbiddenException
	{
		String token = null;
		try
		{
			token = headers.getRequestHeader("Authorization").get(0);
			
		}catch(Exception e)
		{
			LOG.error("Error on headers", e);
		}
		
		if(token != null)
		{
			String valid = null;
			try {
				valid = loggedUserDao.validateToken(id, token, role);
			} catch (UnauthorizedException e) {
				throw e;
			} catch (ForbiddenException e) {
				throw e;
			}
			if(valid != null && valid.equals(token))
			{					
				return true;
			}

		}
		else
		{
			return false;
		}
		return false;
	}
}
