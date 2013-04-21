package com.team314.dcda.local.resources;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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


@Stateless
@Path("/users/{id}/addresses")
public class AddressesResource {

	public static final Logger LOG = LoggerFactory.getLogger(AddressesResource.class);
	
	@EJB
	private AddressDAO addressdao;
	
	@EJB
	private UserDAO userdao;
	
	@EJB
	private LoggedUserDAO loggedUserDao;
	
	@GET
	@Produces({"application/json"})
	public Response getAddresses(@PathParam("id") Integer id, @Context HttpHeaders headers)
	{		
		try {
			Boolean valid = Utils.validateToken(id, headers, loggedUserDao);
			
			if(valid)
			{
				List<Address> addresses = this.addressdao.getAddressesForUser(id);
				
				if(addresses.size()==0 || addresses==null)
				{
					throw new WebApplicationException(new Throwable("Comments not found!"), 404);
				}
				else
				{
					return Response.status(200).entity(addresses).build();			
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
	
	@POST
	@Consumes({"application/json"})
	public Response post(@PathParam("id") Integer id, Address address, @Context HttpHeaders headers)
	{
		
		
		try {
			Boolean valid = Utils.validateToken(id, headers, loggedUserDao);
			
			if(valid)
			{
				User user = this.userdao.find(id);
				if(user != null)
				{
					address.setUser(user);
				}
				address.setAddressid(null);
				this.addressdao.create(address);				
				return Response.status(200).build();				
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
