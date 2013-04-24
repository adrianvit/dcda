package com.team314.dcda.central.dao;

import javax.ejb.Stateless;

import com.team314.dcda.central.db.Email;


@Stateless
public class EmailDAO extends GenericDAO<Email, Integer>{

	public EmailDAO() {
		super(Email.class);
	}

}
