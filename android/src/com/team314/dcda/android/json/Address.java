package com.team314.dcda.android.json;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Address implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@SerializedName ("addressid")
	private int addressid;
	
	@SerializedName ("country")
	private String country;
	
	@SerializedName ("county")
	private String county;
	
	@SerializedName ("city")
	private String city;
	
	@SerializedName ("street")
	private String street;
	
	@SerializedName ("street_number")
	private String street_number;
	
	@SerializedName ("postal_code")
	private String postal_code;

	public int getAddressid() {
		return addressid;
	}

	public void setAddressid(int addressid) {
		this.addressid = addressid;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getStreet_number() {
		return street_number;
	}

	public void setStreet_number(String street_number) {
		this.street_number = street_number;
	}

	public String getPostal_code() {
		return postal_code;
	}

	public void setPostal_code(String postal_code) {
		this.postal_code = postal_code;
	}
}
