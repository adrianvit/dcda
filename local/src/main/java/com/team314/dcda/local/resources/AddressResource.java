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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.team314.dcda.local.dao.AddressDAO;
import com.team314.dcda.local.dao.LoggedUserDAO;
import com.team314.dcda.local.dao.UserDAO;
import com.team314.dcda.local.db.Address;
import com.team314.dcda.local.db.User;
import com.team314.dcda.local.utils.ForbiddenException;
import com.team314.dcda.local.utils.UnauthorizedException;
import com.team314.dcda.local.utils.Utils;

@Path("/users/{id}/addresses/{address_id}")
@Stateless
public class AddressResource {
	
	public static final Logger LOG = LoggerFactory.getLogger(AddressResource.class);
	
	@EJB
	private AddressDAO addressdao;
	
	@EJB
	private LoggedUserDAO loggedUserDao;
	
	@EJB
	private UserDAO userDao;
	
	@GET
	@Produces({"application/json"})
	public Response get(@PathParam("id") Integer id, @PathParam("address_id") Integer address_id, @Context HttpHeaders headers)
	{
		try {
			Boolean valid = Utils.validateToken(id, headers, loggedUserDao, "user");
			
			if(valid)
			{
				Address addr = this.addressdao.find(address_id);
				if(addr != null)
				{
					return Response.status(200).entity(addr).build();			
				}
				else
				{
					throw new WebApplicationException(new Throwable("Address not found!"), 404);
				}		
			}
			else
			{
				LOG.debug("Could not validate token");
			}
		} catch (UnauthorizedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ForbiddenException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch(Exception e)
		{
			return Response.status(500).build();
		}
		
		return Response.status(500).build();	
		
	}
	
	@PUT
	@Consumes({"application/json"})
	public Response put(Address addr,@PathParam("id") Integer id, @Context HttpHeaders headers)
	{
		
		try {
			Boolean valid = Utils.validateToken(id, headers, loggedUserDao, "user");
			
			if(valid)
			{
				Address old_addr = this.addressdao.find(addr.getAddressid());
				User user = this.userDao.find(id);
				
				if(old_addr != null)
				{
					addr.setUser(user);
					old_addr = addr;
					try
					{
						this.addressdao.update(old_addr);
						return Response.status(200).build();
					}catch(Exception e)
					{
						throw new WebApplicationException(new Throwable("Error updating addr!"), 500);
					}
				}
				else
				{
					throw new WebApplicationException(new Throwable("Resource not found!"), 404);
				}	
			}
			else
			{
				LOG.debug("Could not validate token");
				return Response.status(401).build();	
			}
		} catch (UnauthorizedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ForbiddenException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch(Exception e)
		{
			return Response.status(500).build();
		}
		
		return Response.status(500).build();	
		
	}
	
	@DELETE
	public Response delete(@PathParam("id") int id, @PathParam("address_id") Integer address_id, @Context HttpHeaders headers)
	{
		try {
			Boolean valid = Utils.validateToken(id, headers, loggedUserDao, "user");
			
			if(valid)
			{
				Address temp = this.addressdao.find(address_id);
				
				if(temp == null)
				{
					throw new WebApplicationException(new Throwable("Resource not found!"), 404);
				}
				else
				{			
					try
					{
						this.addressdao.delete(temp);
						return Response.status(200).build();
					}
					catch(Exception e)
					{
						throw new WebApplicationException(new Throwable("Error deleting car!"), 500);
					}
				}
			}
			else
			{
				LOG.debug("Could not validate token");
			}
		} catch (UnauthorizedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ForbiddenException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch(Exception e)
		{
			return Response.status(500).build();
		}
		
		return Response.status(500).build();			
	}
}
