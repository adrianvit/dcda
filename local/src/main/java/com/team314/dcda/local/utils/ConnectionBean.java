package com.team314.dcda.local.utils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.WebApplicationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Startup
@Singleton
public class ConnectionBean {
	

	private static final Logger LOG = LoggerFactory.getLogger(ConnectionBean.class);
	private static final String ip_url_1 = "http://api.externalip.net/ip/";
	private static final String ip_url_2 = "http://automation.whatismyip.com/n09230945.asp";


	
	@SuppressWarnings("unused")
	@PostConstruct
	private void connect()
	{
		URI uri = null;
		HttpClient httpclient = Utils.getThreadSafeClient();
		
		//get external ip (try two sources if necessary)
		String local_ip=getIP(httpclient, ip_url_1);
		if(local_ip == null)
		{
			local_ip = getIP(httpclient, ip_url_2);
		}
		
		if(local_ip != null)
		{
			
			//set jndi local ip
			try {
				Context ctx = new InitialContext();
				ctx.rebind(Utils.local_ip_jndi_name, local_ip.substring(0,local_ip.length()-1));
				LOG.debug("Bind local ip to "+local_ip);
			} catch (NamingException e) {
				LOG.error("Could not bind local ip", e);
			}
			
			//if server already exists in the  central repository, update
			if(checkRepository(httpclient, Utils.getLocalName())==true)
			{
				LOG.debug("Server already exists in repository");
				put(httpclient, Utils.getLocalName(), local_ip.substring(0, local_ip.length()-1));	
			}
			else
			{
				
				String temp = "name="+Utils.getLocalName()+"&adr="+local_ip.substring(0, local_ip.length()-1);
				try {
					uri = URIUtils.createURI(Utils.scheme, Utils.central_ip, Utils.central_port, Utils.central_path, temp, null);
					LOG.info("URI = {}", uri.toString());

				} catch (URISyntaxException e1) {
					LOG.error("URI Exception", e1);
				}
				
				HttpPost post = new HttpPost(uri);
				
				try {
					HttpResponse response = httpclient.execute(post);
					response.getEntity().getContent().close();
					if(response.getStatusLine().getStatusCode()!=200)
					{
						LOG.error("Connection to central failed!");
						throw new RuntimeException("Failed to connect to central server!");
					}
					else
					{
						LOG.debug("Connection to central ok!");
					}
				} catch (Exception e) {
					LOG.error("Post error", e);
					throw new RuntimeException("Failed to connect to central server!");
				}				
			}
			
		}else
		{
			LOG.error("Connection to central failed! Local ip is null!");
			throw new RuntimeException("Failed to connect to central server!");
		}
	}
	
	private void put(HttpClient httpclient, String localName, String ip)
	{
		URI uri = null;
		try {
			uri = URIUtils.createURI(Utils.scheme, Utils.central_ip, Utils.central_port, Utils.central_path+"/"+localName, "adr="+ip, null);
			LOG.info("URI = {}", uri.toString());
			LOG.info("URI = {}", uri.toURL());
		} catch (URISyntaxException e1) {
			LOG.error("URI Exception", e1);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		HttpPut put = new HttpPut(uri);
		try
		{
			LOG.debug("Executing put on {}", uri.toString());
			HttpResponse response = httpclient.execute(put);
			LOG.debug("Got response: {}", response.getStatusLine().getStatusCode());
			if(response.getStatusLine().getStatusCode()!=200)
			{
				throw new RuntimeException("Failed to update local ip!");
			}
			response.getEntity().getContent().close();
			
		}catch(Exception e)
		{
			throw new RuntimeException("Failed to connect to central server!");
		}
	}
	
	private boolean checkRepository(HttpClient httpclient, String localName)
	{
		URI uri = null;
		try {
			uri = URIUtils.createURI(Utils.scheme, Utils.central_ip, Utils.central_port, Utils.central_path+"/"+localName, null, null);
			LOG.info("URI = {}", uri.toString());
			LOG.info("URI = {}", uri.toURL());
		} catch (URISyntaxException e1) {
			LOG.error("URI Exception", e1);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		HttpGet get = new HttpGet(uri);
		try
		{
			
			LOG.debug("Executing get on {}", uri.toString());
			HttpResponse response = httpclient.execute(get);
			LOG.debug("Got response: {}", response.getStatusLine().getStatusCode());
			if(response.getStatusLine().getStatusCode()==200)
			{
				LOG.debug("Server exists in repository");
				response.getEntity().getContent().close();
				return true;
			}
			else if(response.getStatusLine().getStatusCode()==404)
			{
				LOG.debug("Server does not exists in repository");
				response.getEntity().getContent().close();
				return false;
			}
			
		}catch(Exception e)
		{
			throw new RuntimeException("Failed to connect to central server!");
		}
		return false;
	}
	
	private String getIP(HttpClient httpClient, String url)
	{
		HttpGet get = new HttpGet(url);
		String result = null;
		try
		{
			HttpResponse response = httpClient.execute(get);
			HttpEntity entity = response.getEntity();
			result = Utils.readInputStream(entity.getContent());
			return result;
		}catch(Exception e)
		{
			return null;
		}
	}
}
