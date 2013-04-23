package com.team314.dcda.local.db;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "loggedusers")
@XmlRootElement(name = "loggedUser")
public class LoggedUser implements Serializable{

	
	private static final long serialVersionUID = 1L;

	@Id
    @Basic(optional = false)
    @Column(name = "userid")
    private Integer userid;
    
    @Column(name = "token")
    private String token;
    
    @Column(name = "expiration")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiration;
    
    @Column(name = "role")
    private String role;

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getExpiration() {
		return expiration;
	}

	public void setExpiration(Date validFrom) {
		this.expiration = validFrom;
	}
	
    public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (userid != null ? userid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof LoggedUser)) {
            return false;
        }
        LoggedUser other = (LoggedUser) object;
        if ((this.userid == null && other.userid != null) || (this.userid != null && !this.userid.equals(other.userid))) {
            return false;
        }
        return true;
    }
}
