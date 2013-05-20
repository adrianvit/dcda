package com.team314.dcda.local.resources;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;


@Stateless
@Path("/notification")
public class NotificationResource {

	@GET
	public void notify(@QueryParam("userIdentification") String id)
	{
		
	}
}
