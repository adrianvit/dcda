package com.team314.dcda.local.resources;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.team314.dcda.local.dao.LoggedUserDAO;
import com.team314.dcda.local.dao.OrderDAO;
import com.team314.dcda.local.dao.UserDAO;
import com.team314.dcda.local.db.Order;
import com.team314.dcda.local.db.User;
import com.team314.dcda.local.utils.ForbiddenException;
import com.team314.dcda.local.utils.UnauthorizedException;
import com.team314.dcda.local.utils.Utils;


@Path("/")
@Stateless
public class OrderResource {

	public static final Logger LOG = LoggerFactory.getLogger(OrderResource.class);
	
	@EJB
	private OrderDAO orderDAO;
	
	@EJB
	private UserDAO userdao;
	
	@EJB
	private LoggedUserDAO loggedUserDao; 
	
	@POST
	@Path("/users/{id}/orders")
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	public Response postOrder(@PathParam("id") Integer id, @QueryParam("pid") int pid, @QueryParam("host") String host, @QueryParam("quantity") int quantity, @Context HttpHeaders headers)
	{
		
		try {
			Boolean valid = Utils.validateToken(id, headers, loggedUserDao, "user");
			
			if(valid)
			{
				User temp = this.userdao.find(id);
				if(temp != null)
				{
					Order order = new Order();
					order.setHost(host);
					order.setProductid(pid);
					order.setStatus("Started");
					order.setUser(temp);
					LOG.debug("Recorder order for user "+id);
					return Response.status(200).entity(order).build();			
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
	
	@POST
	@Path("/orders")
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	public Response postOrderOnDestinationServer(Order order)
	{
		
		return Response.status(500).build();
	}
	
}
