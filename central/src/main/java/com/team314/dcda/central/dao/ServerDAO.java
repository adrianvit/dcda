package com.team314.dcda.central.dao;

import javax.ejb.Stateless;

import com.team314.dcda.central.db.Server;


@Stateless
public class ServerDAO extends GenericDAO<Server, String>{

	public ServerDAO() {
		super(Server.class);
	}

	
}
