package com.team314.dcda.local.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.team314.dcda.local.dao.ProductDAO;
import com.team314.dcda.local.db.Product;
import com.team314.dcda.local.utils.Utils;

@Path("/search")
@Stateless
public class SearchResource {

	
	@EJB
	private ProductDAO productdao;
	
	public static final Logger LOG = LoggerFactory.getLogger(SearchResource.class);
	
	@GET
	@Produces({"application/json"})
	public Response searchForProduct(@QueryParam("key") String keyword, @Context UriInfo ui)
	{
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
					uriBuilder.scheme("http").host(local_ip).port(8080).path("/"+p.getProductid());
					uris.add(uriBuilder.build());
				}
				
				return Response.status(200).entity(uris).build(); 
			}
			else
			{
				//search in neigbours
			}
			
		}catch(Exception e)
		{
			throw new WebApplicationException(new Throwable("Error serching for products"), 500);
		}
		return Response.status(500).build();
	}
	
	
}
