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

import com.team314.dcda.local.dao.PeerDAO;
import com.team314.dcda.local.db.Peer;


@Path("/peers/{id}")
@Stateless
public class PeerResource {

	public static final Logger LOG = LoggerFactory.getLogger(PeerResource.class);

	@EJB
	private PeerDAO peerdao;
	
	@GET
	@Produces({"application/json"})
	public Response get(@PathParam("id") Integer id, @Context HttpHeaders headers)
	{
		try {
				Peer temp = this.peerdao.find(id);
				if(temp != null)
				{
					return Response.status(200).entity(temp).build();			
				}
				else
				{
					throw new WebApplicationException(new Throwable("Peer not found!"), 404);
				}		
			
		}catch(Exception e)
		{
			return Response.status(500).build();
		}	
	}
	
	
	@PUT
	@Consumes({ "application/json" })
	public Response put(Peer peer, @PathParam("id") Integer id) {

		//sanity check
		if(id != peer.getPeerid())
		{
			LOG.debug("Could not match id");
			return Response.status(500).build();
		}
		
		Peer old_peer = this.peerdao.find(peer.getPeerid());
		if (old_peer != null) {
			old_peer = peer;
			try {
				this.peerdao.update(old_peer);
				return Response.status(200).build();
			} catch (Exception e) {
				throw new WebApplicationException(new Throwable(
						"Error updating peer!"), 500);
			}
		} else {
			return Response.status(404).build();
		}
	}
	
	
	@DELETE
	public Response delete(@PathParam("id") Integer id)
	{
		Peer peer = this.peerdao.find(id);
		if(peer!=null)
		{
			this.peerdao.delete(peer);
			return Response.status(200).build();
		}else{
			return Response.status(404).build();
		}
	}
}
