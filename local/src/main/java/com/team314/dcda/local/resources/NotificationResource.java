package com.team314.dcda.local.resources;

import java.io.IOException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
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
	public Response notify(@QueryParam("id") int id)
	{
		if(notifyBeanMethod(id))
		{
			return Response.status(200).build();				
		}
		else
		{
			return Response.status(500).build();
		}
	}
	
	public boolean notifyBeanMethod(int userID)
	{
		User user = null;
		if((user = this.userdao.find(userID))!= null)
		{
			if(user.getGcmregid() == null)
			{
				return false;
			}
		}else{
			return false;
		}
			
		Sender sender = new Sender("AIzaSyDqNfVwp-E-DonV9KvSyj3frWkNfMbpKpw");
		Message msg =  new Message.Builder().addData("Message:", "Your package is at "+Utils.getLocalName()).build();
		Result result = null;
		try {
			result = sender.send(msg, user.getGcmregid(), 3);
		} catch (IOException e) {
			LOG.error("Error sending message to GMC",e);
		}
		
		if(result == null)
		{
			LOG.debug("Could not send message after multiple attemps.");
			return false;
		}
		return true;
	}
	
	@POST
	public Response setRegId(@QueryParam("id") Integer id, @QueryParam("regId") String regId, @Context HttpHeaders headers)
	{
		try {
			Boolean valid = Utils.validateToken(headers, loggedUserDao, "user");
			
			if(valid)
			{
				User user = this.userdao.find(id);
				LOG.debug("reg id:"+regId);
				user.setGcmregid(regId);
				
				userdao.update(user);
				
				return Response.status(200).build();			
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
}
