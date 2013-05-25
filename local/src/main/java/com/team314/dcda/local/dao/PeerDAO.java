package com.team314.dcda.local.dao;

import java.util.List;

import javax.ejb.Stateless;

import com.team314.dcda.local.db.Peer;

@Stateless
public class PeerDAO extends GenericDAO<Peer, Integer>{

	public PeerDAO() {
		super(Peer.class);
	}
	
	public Peer getPeerByUrl(String url)
	{
		Peer temp = null;
		try
		{
			temp = (Peer)em.createQuery("Select a FROM Peer a WHERE a.peer.url = :url").setParameter("url", url).getSingleResult();
		}catch(Exception e){
			temp = null;
		}
		return temp;
	}
	
	@SuppressWarnings("unchecked")
	public List<Peer> getPeerList()
	{
		List<Peer> temp = null;
		try
		{
			temp = (List<Peer>)em.createQuery("Select p FROM Peer p").getResultList();
		}catch (Exception e)
		{
			temp = null;
		}
		return temp;
	}
}
