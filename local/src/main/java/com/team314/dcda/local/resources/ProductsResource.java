package com.team314.dcda.local.resources;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.team314.dcda.local.dao.LoggedUserDAO;
import com.team314.dcda.local.dao.ProductDAO;
import com.team314.dcda.local.db.Product;
import com.team314.dcda.local.utils.Utils;


@Path("/products")
@Stateless
public class ProductsResource {

	public static final Logger LOG = LoggerFactory.getLogger(ProductsResource.class);
	
	@EJB
	private ProductDAO productdao;
	
	@EJB
	private LoggedUserDAO loggedUserDao;
	
	@GET
    @Produces({"application/json"})
	public Response getProducts()
	{
		List<Product> products = this.productdao.getProductList();
		
		if(products==null || products.size()==0)
		{
			throw new WebApplicationException(new Throwable("Products not found!"), 404);
		}
		else
		{
			return Response.status(200).entity(products).build();
		}
	}
	
	@POST
	@Consumes({"application/json"})
	public Response createProduct(Product product, @QueryParam("userId") Integer userId, @Context HttpHeaders headers)
	{
		try
		{
			Boolean valid = Utils.validateToken(userId, headers, loggedUserDao, "admin");
			
			if(valid)
			{
				this.productdao.create(product);
				return Response.status(200).build();			
			}else
			{
				LOG.debug("Could not validate token");
			}
		}catch(Exception e)
		{
			throw new WebApplicationException(new Throwable("Error creating car!"), 500);
		}
		return Response.status(500).build();
	}
}
