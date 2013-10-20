package com.team314.dcda.local.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.AsyncWebResource;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.client.non.blocking.NonBlockingClient;
import com.sun.jersey.client.non.blocking.config.DefaultNonBlockingClientConfig;
import com.sun.jersey.client.non.blocking.config.NonBlockingClientConfig;
import com.team314.dcda.local.dao.PeerDAO;
import com.team314.dcda.local.dao.ProductDAO;
import com.team314.dcda.local.db.Peer;
import com.team314.dcda.local.db.Product;
import com.team314.dcda.local.utils.Utils;

@Path("/search")
@Stateless
public class SearchResource {

	
	@EJB
	private ProductDAO productdao;
	
	@EJB
	private PeerDAO peerdao;
	
	public static final Logger LOG = LoggerFactory.getLogger(SearchResource.class);
	
	@GET
	@Produces({"application/json"})
	public Response searchForProduct(@QueryParam("key") String keyword, @QueryParam("ttl") int ttl, @Context UriInfo ui, @Context HttpServletRequest req)
	{
		LOG.debug("Got request with: key="+keyword+" ttl="+ttl);
		if(ttl-1<0)
		{
			return Response.status(500).build(); 
		}else{
			ttl = ttl - 1;
		}
		
		String local_ip = null;
		try {
			InitialContext ic = new InitialContext();
			local_ip = (String) ic.lookup(Utils.local_ip_jndi_name);
			LOG.debug("local ip jndi entry "+ local_ip);
		} catch (NamingException e) {
			LOG.error("Could not find local ip jndi entry", e);
		}
		
		if(local_ip == null)
		{
			return Response.status(500).build();
		}
		
		String remote_ip = getRequestIp(req);
		
		try
		{
			List<Product> products = this.productdao.getProductsByKeyword(keyword);
			List<URI> uris = new ArrayList<URI>();
			
			if(products.size()>1)
			{
				//create a list of urls with products
				for(Product p: products)
				{
					UriBuilder uriBuilder = UriBuilder.fromUri("/local/products");
					uriBuilder.scheme("http").host(local_ip).port(18080).path("/"+p.getProductid());
					uris.add(uriBuilder.build());
				}

				return Response.status(200).entity(uris).build(); 
			}
			else
			{
				List<URI> temp = searchForProductInPeers(this.peerdao.getPeerList(), keyword, ttl, remote_ip);
				if(temp != null)
				{
					uris.addAll(temp);					
				}
				if(uris.size()>1)
				{
					return Response.status(200).entity(uris).build(); 					
				}else
				{
					return Response.status(404).build();
				}
			}
			
		}catch(Exception e)
		{
			throw new WebApplicationException(new Throwable("Error serching for products"), 500);
		}
	}
	
	private String getRequestIp(HttpServletRequest req)
	{
		String result = null;
		result = req.getRemoteHost();
		return result;
	}
	
	private List<URI> searchForProductInPeers(List<Peer> peers, String key, int ttl, String remote_ip)
	{
		List<URI> result = null;
		ExecutorService executorService = Executors.newCachedThreadPool();
		CompletionService<List<URI>> completionService = new ExecutorCompletionService<List<URI>>(executorService);
		List<Future<List<URI>>> futureList = new ArrayList<Future<List<URI>>>();
		
		for(Peer p: peers)
		{
			if(p.getUrl()!=remote_ip)
			{
				futureList.add(completionService.submit(new SearchForProductsInPeerTask(p, key, ttl)));
				LOG.debug("Submitted task to peer" + p.getPeerid());				
			}
		}
		try {
			
			Future<List<URI>> temp = null;
			for(Future<List<URI>> f: futureList)
			{
				temp = completionService.take();
				if(temp.get() != null)
				{
					break;
				}
			}
			
			//Future<List<URI>> temp = completionService.take();
			result = temp.get();
			for(Future<List<URI>> f: futureList){
				f.cancel(true);
			}
		} catch (InterruptedException e) {
			LOG.error("Error while executing search", e);
		} catch (ExecutionException e) {
			LOG.error("Error while executing search", e);
		}
		return result;
	}
}

class SearchForProductsInPeerTask implements Callable<List<URI>>{

	private Peer peer;
	private String key;
	private int ttl;
	
	public SearchForProductsInPeerTask(Peer peer, String key, int ttl)
	{
		this.peer = peer;
		this.key = key;
		this.ttl = ttl;
	}
	
	@Override
	public List<URI> call() throws Exception {
		UriBuilder uriBuilder = UriBuilder.fromUri("/local");
		uriBuilder.scheme("http").host(peer.getUrl()).path("/search").queryParam("key", key).queryParam("ttl", Integer.toString(ttl)).port(18080);
		URI uri = uriBuilder.build();
		ClientConfig cc = new DefaultClientConfig();
		cc.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		Client c = Client.create(cc);
		WebResource wr = c.resource(uri);
		ClientResponse response = wr.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		List<URI> result = null;
		if(response.getStatus() == 200)
		{
			result = response.getEntity(new GenericType<List<URI>>(){});
			if(result.size()<1)
			{
				return null;
			}
		}
		//List<URI> result = wr.accept(MediaType.APPLICATION_JSON).get(new GenericType<List<URI>>(){});
		return result;
	}
	
}