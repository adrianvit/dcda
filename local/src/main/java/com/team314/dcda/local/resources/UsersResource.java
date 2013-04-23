package com.team314.dcda.local.resources;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.utils.URIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.team314.dcda.local.utils.EmailException;
import com.team314.dcda.local.utils.Utils;
import com.team314.dcda.local.dao.UserDAO;
import com.team314.dcda.local.db.User;

@Stateless
@Path("/users")
public class UsersResource {
	
	private static final Logger LOG = LoggerFactory.getLogger(UsersResource.class);
	
	@EJB
	private UserDAO userDAO;
	
	@GET
    @Produces({"application/json"})
	public Response getUsers()
	{
		List<User> users = this.userDAO.getUsers();
		
		if(users.size()==0 || users==null)
		{
			throw new WebApplicationException(new Throwable("Users not found!"), 404);
		}
		else
		{
			return Response.status(200).entity(users).build();
		}
	}

	@POST
	@Consumes({"application/json"})
	public Response createUser(User user)
	{
		
		String email = user.getEmail();

		URI uri = null;
		try {
			String temp = "email="+email;
			uri = URIUtils.createURI(Utils.scheme, Utils.central_ip, 8080, Utils.central_path_register, temp, null);
			LOG.debug("Getting userid for {}", uri.toString());
			//check if email is unique and return userid
			String userid = Utils.checkRegistrationEmail(uri);
			if(userid != null)
			{
				userid = userid.substring(0, userid.length()-1);
				user.setUserId(Integer.parseInt(userid));
				this.userDAO.create(user);
				return Response.status(200).build();
			}
			else
			{
				LOG.debug("Could not get userid for email {}", email);
				return Response.status(500).build();
			}
		} catch (EmailException e) {
			return Response.status(409).build();	//email could not be created
		} catch (URISyntaxException e) {
			LOG.error("Error creating uri", e);
		} catch (ClientProtocolException e) {
			LOG.error("Protocol error when performing get", e);
		} catch (IOException e) {
			LOG.error("IO error when performing get", e);
		} catch (Exception e){
			LOG.error("Exception when creating user", e);
		}
		return Response.status(500).build();
	}
	
}
