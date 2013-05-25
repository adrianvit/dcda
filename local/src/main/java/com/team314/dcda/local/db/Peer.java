package com.team314.dcda.local.db;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "local.peers")
@XmlRootElement(name = "peer")
public class Peer {

	@Id
	@Basic(optional = false)
	@Column(name = "peerid")
	private Integer peerid;
	
	@Column(name = "url")
	private String url;
	
	@Column(name = "distance")
	private int distance;

	public Integer getPeerid() {
		return peerid;
	}


	public void setPeerid(Integer peerid) {
		this.peerid = peerid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + distance;
		result = prime * result + ((peerid == null) ? 0 : peerid.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Peer other = (Peer) obj;
		if (distance != other.distance)
			return false;
		if (peerid == null) {
			if (other.peerid != null)
				return false;
		} else if (!peerid.equals(other.peerid))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
}
