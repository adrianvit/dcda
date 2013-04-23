package com.team314.dcda.local.dao;

import java.sql.Timestamp;

import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.team314.dcda.local.utils.ForbiddenException;
import com.team314.dcda.local.utils.UnauthorizedException;
import com.team314.dcda.local.db.LoggedUser;

@Stateless
public class LoggedUserDAO extends GenericDAO<LoggedUser, Integer>{

	public static final Logger LOG = LoggerFactory.getLogger(LoggedUserDAO.class);
	
	public LoggedUserDAO() {
		super(LoggedUser.class);
	}

	
	public String validateToken(int userid, String token) throws UnauthorizedException, ForbiddenException
	{
		LoggedUser loggedUser = em.find(LoggedUser.class, userid);
		if(loggedUser == null)
		{
			throw new UnauthorizedException();
		}
		else
		{
			LOG.debug("User token: {}", loggedUser.getToken());
			LOG.debug("Received token: {}", token);
			
			if(loggedUser.getToken().equals(token))
			{
				LOG.debug("Tokens match");
				Timestamp timestamp = new Timestamp(loggedUser.getExpiration().getTime());
				if(tokenNotExpired(timestamp))
				{
					LOG.debug("Token valid");
					return loggedUser.getToken();
				}			
				else
				{
					LOG.debug("Tokens expired");
					throw new ForbiddenException();
				}
			}
			else
			{
				LOG.debug("Tokens do not match!");
				throw new UnauthorizedException();
			}
			
		}
		
	} 
	
	public String validateToken(int userid, String token, String role) throws UnauthorizedException, ForbiddenException
	{
		LoggedUser loggedUser = em.find(LoggedUser.class, userid);
		if(loggedUser == null)
		{
			throw new UnauthorizedException();
		}
		else
		{	
			if(!loggedUser.getRole().equals(role))
			{
				throw new UnauthorizedException();
			}
			
			return validateToken(userid, token);
		}
		
	} 
	
	private boolean tokenNotExpired(Timestamp timestamp)
	{
		Timestamp timestamp_now = new Timestamp(System.currentTimeMillis());
		if(timestamp_now.before(timestamp))
		{
			return true;			
		}
		else
		{
			return false;
		}
	}
	
}
