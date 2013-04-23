package com.team314.dcda.local.dao;

import java.util.List;

import javax.ejb.Stateless;

import com.team314.dcda.local.db.Address;


@Stateless
public class AddressDAO extends GenericDAO<Address, Integer>{

	public AddressDAO() {
		super(Address.class);
	}

	
	@SuppressWarnings("unchecked")
	public List<Address> getAddressesForUser(int userId)
	{
		List<Address> temp = null;
		temp = (List<Address>)em.createQuery("Select a FROM Address a WHERE a.user.userid = :userId").setParameter("userId", userId).getResultList();
		return temp;
	}
	
}
