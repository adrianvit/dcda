package com.team314.dcda.central.dao;

import java.util.List;

import javax.ejb.Stateless;

import com.team314.dcda.central.db.Server;


@Stateless
public class ServerDAO extends GenericDAO<Server, String>{

	public ServerDAO() {
		super(Server.class);
	}

	@SuppressWarnings("unchecked")
	public List<Server> getServers()
	{
		List<Server> temp = null;
		try
		{
			temp = (List<Server>)em.createQuery("Select u FROM Server u").getResultList();
		}catch (Exception e)
		{
			temp = null;
		}
		return temp;
	}
}
