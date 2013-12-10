package com.team314.dcda.local.resources;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
import com.team314.dcda.local.utils.ForbiddenException;
import com.team314.dcda.local.utils.UnauthorizedException;
import com.team314.dcda.local.utils.UserRoles;
import com.team314.dcda.local.utils.Utils;


@Path("/products/{id}")
@Stateless
public class ProductResource {
	
	public static final Logger LOG = LoggerFactory.getLogger(ProductResource.class);

	@EJB
	private ProductDAO productdao;
	
	@EJB
	private LoggedUserDAO loggedUserDao; 

	@GET
	@Produces({"application/json"})
	public Response get(@PathParam("id") Integer id, @Context HttpHeaders headers)
	{
		try {
				Product temp = this.productdao.find(id);
				if(temp != null)
				{
					return Response.status(200).entity(temp).build();			
				}
				else
				{
					throw new WebApplicationException(new Throwable("User not found!"), 404);
				}		
			
		}catch(Exception e)
		{
			return Response.status(500).build();
		}
		
	}
	
	@PUT
	@Consumes({"application/json"})
	public Response put(Product product,@PathParam("id") Integer id,  @Context HttpHeaders headers)
	{
		
		
		try {
			Boolean valid = Utils.validateToken(headers, loggedUserDao, UserRoles.ADMIN.toString());
			
			//sanity check
			if(id != product.getProductid())
			{
				LOG.debug("Could not match id");
				return Response.status(500).build();
			}
			
			if(valid)
			{
				Product old_product = this.productdao.find(product.getProductid());
				if(old_product != null)
				{
					old_product = product;
					try
					{
						this.productdao.update(old_product);
						return Response.status(200).build();
					}catch(Exception e)
					{
						throw new WebApplicationException(new Throwable("Error updating product!"), 500);
					}
				}
				else
				{
					return Response.status(404).build();
				}	
			}
			else
			{
				LOG.debug("Could not validate token");
			}
		} catch (UnauthorizedException e1) {
			e1.printStackTrace();
		} catch (ForbiddenException e1) {
			e1.printStackTrace();
		}catch(Exception e)
		{
			return Response.status(500).build();
		}
		
		return Response.status(500).build();
		
	}
	
	@DELETE
	public Response delete(@PathParam("id") Integer id,  @Context HttpHeaders headers)
	{

		try {
			Boolean valid = Utils.validateToken(headers, loggedUserDao, UserRoles.ADMIN.toString());
			
			if(valid)
			{
				Product temp = this.productdao.find(id);
				
				if(temp == null)
				{
					throw new WebApplicationException(new Throwable("Resource not found!"), 404);
				}
				else
				{			
					try
					{
						this.productdao.delete(temp);
						return Response.status(200).build();
					}
					catch(Exception e)
					{
						throw new WebApplicationException(new Throwable("Error deleting product!"), 500);
					}
				}
			}
			else
			{
				LOG.debug("Could not validate token");
			}
		} catch (UnauthorizedException e1) {
			e1.printStackTrace();
		} catch (ForbiddenException e1) {
			e1.printStackTrace();
		}catch(Exception e)
		{
			return Response.status(500).build();
		}
		
		return Response.status(500).build();

	}
}
