package com.team314.dcda.local.resources;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;


@Stateless
@Path("/notification")
public class NotificationResource {

	@GET
	public Response notify(@QueryParam("userIdentification") String id)
	{
		return Response.status(200).build();
	}
}
