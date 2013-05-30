package com.team314.dcda.local.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class GenericDAO<T, ID> {

	@PersistenceContext(unitName = "DCDA_Local_PU")
	protected EntityManager em;
	
	private Class<T> class_type;
	
	public GenericDAO(Class<T> class_type)
	{
		this.class_type=class_type;
	}
	
	public T find(ID id)
	{
		return em.find(this.class_type, id);
	}
	
	public void create(T entity)
	{
		em.persist(entity);
	}
	
	public T update(T entity)
	{
		return em.merge(entity);
	}
	
	public void delete(T entity)
	{
		em.remove(entity);
	}
	
	public EntityManager getEM()
	{
		return this.em;
	}
}
