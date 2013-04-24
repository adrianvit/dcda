package com.team314.dcda.central.resources;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.team314.dcda.central.dao.EmailDAO;
import com.team314.dcda.central.db.Email;



@Stateless
@Path("/register")
public class Registration {

	@EJB
	private EmailDAO emailDAO;
	
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public Response addUser(@QueryParam("email") String email)
	{
		
		Email temp = new Email();
		temp.setEmail(email);
		try
		{
			emailDAO.create(temp);
			return Response.status(200).entity(Integer.toString(temp.getId())).build();
		}catch (Exception e)
		{
			return Response.status(500).build(); //conflict
		}		
	}
}
