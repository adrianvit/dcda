package com.team314.dcda.local.dao;

import javax.ejb.Stateless;

import com.team314.dcda.local.db.Order;

@Stateless
public class OrderDAO extends GenericDAO<Order, Integer>{

	public OrderDAO() {
		super(Order.class);
	}
}
