package com.team314.dcda.android.json;

import com.google.gson.annotations.SerializedName;

public class User {
	
	@SerializedName ("userId")
	private int userid;

	@SerializedName ("firstName")
	private String firstName;

	@SerializedName ("lastName")
	private String lastName;
	
	@SerializedName ("phone")
	private String phone;
	
	@SerializedName ("email")
	private String email;
	
	@SerializedName ("pass")
	private String pass;
	
	@SerializedName ("role")
	private String role;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
