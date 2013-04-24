package com.team314.dcda.android.json;

import com.google.gson.annotations.SerializedName;

public class LoggedUser {

	@SerializedName ("userid")
	private Integer userid;
	
	@SerializedName ("token")
	private String token;
	
	@SerializedName ("expiration")
	private long expiration;

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

	public long getExpiration() {
		return expiration;
	}

	public void setExpiration(long expiration) {
		this.expiration = expiration;
	}
	
}
