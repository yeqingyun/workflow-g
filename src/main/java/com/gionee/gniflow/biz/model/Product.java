/**
 * @(#) Product.java Created on 2014年7月4日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.biz.model;

import java.io.Serializable;

/**
 * The class <code>Product</code>
 *
 * @author lipw
 * @version 1.0
 */
public class Product implements Serializable {
	
	private static final long serialVersionUID = -2257856248889075851L;

	private String productId;
	
	private String productName;
	
	private String price;
	
	private String code;
	
	private String color;
	
	public Product(String productId, String productName, String price,
			String code, String color) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.price = price;
		this.code = code;
		this.color = color;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	
}
