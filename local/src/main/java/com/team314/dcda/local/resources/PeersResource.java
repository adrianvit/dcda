package com.team314.dcda.local.resources;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.team314.dcda.local.dao.PeerDAO;
import com.team314.dcda.local.db.Peer;


@Path("/peers")
@Stateless
public class PeersResource {

	public static final Logger LOG = LoggerFactory.getLogger(PeersResource.class);

	@EJB
	private PeerDAO peerdao;
	
	@GET
    @Produces({"application/json"})
	public Response getPeers()
	{
		List<Peer> peers = this.peerdao.getPeerList();
		
		if(peers==null || peers.size()==0)
		{
			throw new WebApplicationException(new Throwable("peers not found!"), 404);
		}
		else
		{
			return Response.status(200).entity(peers).build();
		}
	}
	
	@POST
	@Consumes({ "application/json" })
	public Response createPeer(Peer peer) {
		try {

			this.peerdao.create(peer);
			return Response.status(200).build();

		} catch (Exception e) {
			throw new WebApplicationException(new Throwable(
					"Error creating peer!"), 500);
		}
	}
}
