package com.team314.dcda.local.dao;

import java.util.List;

import javax.ejb.Stateless;

import com.team314.dcda.local.db.Product;

@Stateless
public class ProductDAO extends GenericDAO<Product, Integer>{

	public ProductDAO() {
		super(Product.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<Product> getProductList()
	{
		List<Product> temp = null;
		try
		{
			temp = (List<Product>)em.createQuery("Select u FROM Product u").getResultList();
		}catch (Exception e)
		{
			temp = null;
		}
		return temp;
	}
	
	@SuppressWarnings("unchecked")
	public List<Product> getProductListPaged(int start, int stop)
	{
		List<Product> temp = null;
		try
		{
			temp = (List<Product>)em.createQuery("Select u FROM Product u").setFirstResult(start).setMaxResults(stop).getResultList();
		}catch (Exception e)
		{
			temp = null;
		}
		return temp;
	}
	
	@SuppressWarnings("unchecked")
	public List<Product> getProductsByKeyword(String keyword)
	{
		List<Product> temp = null;
		try
		{
			temp = (List<Product>)em.createQuery("SELECT u FROM Product u WHERE UPPER(u.description) LIKE :keyword ORDER BY u.description")
					.setParameter("keyword",  "%" + keyword.toUpperCase() + "%").getResultList();
		}catch(Exception e){
			temp = null;
		}
		return temp;
	}
}
