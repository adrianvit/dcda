package com.team314.dcda.android.json;

import com.google.gson.annotations.SerializedName;

public class Product {

	@SerializedName ("productid")
	private Integer productid;
	
	@SerializedName ("description")
	private String description;

	@SerializedName ("category")
	private int category;

	@SerializedName ("quantity")
	private int quantity;

	@SerializedName ("price")
	private int price;

	public Integer getProductid() {
		return productid;
	}

	public void setProductid(Integer productid) {
		this.productid = productid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}	
}
