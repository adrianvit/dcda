package com.team314.dcda.central.resources;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.team314.dcda.central.dao.ServerDAO;
import com.team314.dcda.central.db.Server;



@Stateless
@Path("/locate")
public class Repository {
	
	@EJB
	private ServerDAO serverDAO;
	
	@GET
    @Produces({"application/json"})
	public Response getServers()
	{
		List<Server> servers = this.serverDAO.getServers();
		
		if(servers.size()==0 || servers==null)
		{
			throw new WebApplicationException(new Throwable("Servers not found!"), 404);
		}
		else
		{
			return Response.status(200).entity(servers).build();
		}
	}
	
	
	@POST
	public Response addServer(@QueryParam("name") String name,@QueryParam("adr") String address, @QueryParam("lat") Double latitude, @QueryParam("lon") Double longitude)
	{
		try
		{	
			Server temp = new Server();
			temp.setName(name);
			temp.setAddress(address);
			temp.setLatitude(latitude);
			temp.setLongitude(longitude);
			serverDAO.create(temp);
			return Response.status(200).build();
		}catch(Exception e)
		{
			throw new WebApplicationException(new Throwable("Could not add server!"), 500);
		}
	}
	
	
	@Path("/server/{name}")
	@PUT
	public Response updateServer(@PathParam("name") String name, @QueryParam("adr") String adr)
	{
		Server temp = serverDAO.find(name);
		if(temp != null)
		{
			
			try
			{			
				temp.setAddress(adr);
				serverDAO.update(temp);
			}catch(Exception e)
			{
				throw new WebApplicationException(new Throwable("Could not delete server!"), 500);
			}
			return Response.status(200).build();
		}
		else
		{
			throw new WebApplicationException(new Throwable("Server not found!"), 404);
		}
	}
	
	@Path("/server/{name}")
	@DELETE
	public Response removeServer(@PathParam ("name") String name)
	{
		Server temp = serverDAO.find(name);
		if(temp != null)
		{
			
			try
			{			
				serverDAO.delete(temp);
			}catch(Exception e)
			{
				throw new WebApplicationException(new Throwable("Could not delete server!"), 500);
			}
			return Response.status(200).build();
		}
		else
		{
			throw new WebApplicationException(new Throwable("Server not found!"), 404);
		}
	}
	
	@Path("/server/{name}")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Response getServer(@PathParam ("name") String name)
	{
		Server temp = serverDAO.find(name);
		if(temp!=null)
		{
			return Response.status(200).entity(temp.getAddress()).build();
		}
		else
		{
			throw new WebApplicationException(new Throwable("Could not find server!"), 404);
		}
	}
	
	@Path("/server/{host}/location")
	@GET
	@Produces({"application/json"})
	public Response getServerLocation(@PathParam ("host") String host)
	{
		Server temp = null;
		List<Server> serverList = serverDAO.getServers();
		for(Server s: serverList){
			if(s.getAddress().equals(host)){
				temp = s;
				break;
			}
		}
		if(temp!=null)
		{
			return Response.status(200).entity(temp).build();
		}
		else
		{
			throw new WebApplicationException(new Throwable("Could not find server!"), 404);
		}
	}
}
