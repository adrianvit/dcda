package com.team314.dcda.local.resources;

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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.team314.dcda.local.dao.LoggedUserDAO;
import com.team314.dcda.local.dao.UserDAO;
import com.team314.dcda.local.db.User;
import com.team314.dcda.local.utils.ForbiddenException;
import com.team314.dcda.local.utils.UnauthorizedException;
import com.team314.dcda.local.utils.Utils;

@Stateless
@Path("/notification")
public class NotificationResource {

	public static final Logger LOG = LoggerFactory.getLogger(AddressResource.class);

	@EJB
	private UserDAO userdao;
	
	@EJB
	private LoggedUserDAO loggedUserDao;

	@GET
	public Response notify(@QueryParam("userIdentification") String id)
	{
		return Response.status(200).build();	
	}
	
	@POST
	public Response setRegId(@QueryParam("id") Integer id, @QueryParam("regId") String regId, @Context HttpHeaders headers)
	{
		try {
			Boolean valid = Utils.validateToken(id, headers, loggedUserDao, "user");
			
			if(valid)
			{
				User user = this.userdao.find(id);
				LOG.debug("reg id:"+regId);
				user.setGCMRegId(regId);
				
				userdao.update(user);
				
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
