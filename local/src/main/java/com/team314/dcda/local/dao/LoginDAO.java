package com.team314.dcda.local.dao;

import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.team314.dcda.local.db.LoggedUser;
import com.team314.dcda.local.db.User;
import com.team314.dcda.local.dao.LoggedUserDAO;

@Stateless
public class LoginDAO {
	
    @EJB
    private LoggedUserDAO loggedUserDAO;
    
    @EJB
    private UserDAO userDAO;
    
    private static final Logger LOG = LoggerFactory.getLogger(LoginDAO.class);
    
    public LoggedUser login(String email, String password)
    {
    	try
    	{
    		LoggedUser temp = null;
    		User user = userDAO.getUserByEmail(email);
    		
    		if(user == null) {
    			return null;
    		}
    		if(user.getPass().equals(password) == false) {
    			return null;
    		}
    		
    		try
    		{
    			temp = loggedUserDAO.find(user.getUserId());        			
    		}catch(Exception e)
    		{
    			temp = null;
    		}
    		
    		java.util.Date date = new java.util.Date(System.currentTimeMillis()); 
    		java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime() + 1800000);
    		
    		if(temp == null)
    		{
    			temp = new LoggedUser();
    			temp.setUserid(user.getUserId());
    			temp.setExpiration(timestamp);
    			temp.setToken(UUID.randomUUID().toString());
    			loggedUserDAO.create(temp);
    			LOG.debug("Created new logged user {}", user.getUserId());
    		}
    		else
    		{
    			LOG.debug("Old timestamp is: "+temp.getExpiration().toString());
    			temp.setExpiration(timestamp);
    			temp.setToken(UUID.randomUUID().toString());
    			LOG.debug("Updated new logged user {}",  user.getUserId());
    			loggedUserDAO.update(temp);    			
    		}
    		
    		return temp;
    		
    	}catch(Exception e)
    	{    		
    		LOG.error("Error loging in!",e);
    		return null;
    	}
    	
    }
}

