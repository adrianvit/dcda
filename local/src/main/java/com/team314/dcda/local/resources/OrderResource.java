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
import com.team314.dcda.local.dao.ProductDAO;
import com.team314.dcda.local.dao.UserDAO;
import com.team314.dcda.local.db.Order;
import com.team314.dcda.local.db.Product;
import com.team314.dcda.local.db.User;
import com.team314.dcda.local.utils.ForbiddenException;
import com.team314.dcda.local.utils.UnauthorizedException;
import com.team314.dcda.local.utils.Utils;


@Path("/orders")
@Stateless
public class OrderResource {

	public static final Logger LOG = LoggerFactory.getLogger(OrderResource.class);
	
	@EJB
	private OrderDAO orderDAO;
	
	@EJB
	private UserDAO userdao;
	
	@EJB
	private ProductDAO productDAO;
	
	@EJB
	private LoggedUserDAO loggedUserDao; 
	
	@EJB
	private NotificationResource notificationResource; 
	
	@POST
	@Consumes({ "application/json" })
	public Response postOrderOnDestinationServer(Order order)
	{
		
		try {

			this.orderDAO.create(order);
			Product temp = this.productDAO.find(order.getProductid());
			temp.setQuantity(temp.getQuantity()-order.getQuantity());
			this.notificationResource.notifyBeanMethod(order.getUser().getUserId());
			LOG.debug("Got order from other peer");
			return Response.status(200).build();

		} catch (Exception e) {
			throw new WebApplicationException(new Throwable(
					"Error creating order on destination!"), 500);
		}
	}
	
}
