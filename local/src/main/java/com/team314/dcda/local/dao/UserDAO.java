package com.team314.dcda.local.dao;

import java.util.List;

import javax.ejb.Stateless;

import com.team314.dcda.local.db.User;

@Stateless
public class UserDAO extends GenericDAO<User, Integer>{

	
	public UserDAO() {
		super(User.class);
	}
	
	
	public User getUserByEmail(String email)
	{
		User temp = null;
		try
		{
			temp = (User)em.createQuery("Select u FROM User u WHERE u.email = :email").setParameter("email", email).getSingleResult();
		}catch (Exception e)
		{
			temp = null;
		}
		return temp;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<User> getUsers()
	{
		List<User> temp = null;
		try
		{
			temp = (List<User>)em.createQuery("Select u FROM User u").getResultList();
		}catch (Exception e)
		{
			temp = null;
		}
		return temp;
	}
	
}
