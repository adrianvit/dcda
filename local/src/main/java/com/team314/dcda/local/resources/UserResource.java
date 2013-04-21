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

import com.team314.dcda.local.utils.ForbiddenException;
import com.team314.dcda.local.utils.UnauthorizedException;
import com.team314.dcda.local.utils.Utils;
import com.team314.dcda.local.dao.LoggedUserDAO;
import com.team314.dcda.local.dao.UserDAO;
import com.team314.dcda.local.db.User;


@Path("/users/{id}")
@Stateless
public class UserResource {
	
	public static final Logger LOG = LoggerFactory.getLogger(UserResource.class);
	
	@EJB
	private UserDAO userdao;
	
	@EJB
	private LoggedUserDAO loggedUserDao; 
	
	@GET
	@Produces({"application/json"})
	public Response get(@PathParam("id") Integer id, @Context HttpHeaders headers)
	{
		try {
			Boolean valid = Utils.validateToken(id, headers, loggedUserDao);
			
			if(valid)
			{
				User temp = this.userdao.find(id);
				if(temp != null)
				{
					return Response.status(200).entity(temp).build();			
				}
				else
				{
					throw new WebApplicationException(new Throwable("User not found!"), 404);
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
	
	@PUT
	@Consumes({"application/json"})
	public Response put(User user,@PathParam("id") Integer id,  @Context HttpHeaders headers)
	{
		
		
		try {
			Boolean valid = Utils.validateToken(id, headers, loggedUserDao);
			
			//sanity check
			if(id != user.getUserId())
			{
				LOG.debug("Could not match id");
				return Response.status(500).build();
			}
			
			if(valid)
			{
				User old_user = this.userdao.find(user.getUserId());
				if(old_user != null)
				{
					old_user = user;
					try
					{
						this.userdao.update(old_user);
						return Response.status(200).build();
					}catch(Exception e)
					{
						throw new WebApplicationException(new Throwable("Error updating user!"), 500);
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
	public Response delete(@PathParam("id") Integer id, @Context HttpHeaders headers)
	{

		try {
			Boolean valid = Utils.validateToken(id, headers, loggedUserDao);
			
			if(valid)
			{
				User temp = this.userdao.find(id);
				
				if(temp == null)
				{
					throw new WebApplicationException(new Throwable("Resource not found!"), 404);
				}
				else
				{			
					try
					{
						this.userdao.delete(temp);
						return Response.status(200).build();
					}
					catch(Exception e)
					{
						throw new WebApplicationException(new Throwable("Error deleting picture!"), 500);
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
