/**
 * @(#) ComboBoxData.java Created on 2014年11月20日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.easyui;

import java.io.Serializable;

/**
 * The class <code>ComboBoxData</code>
 *
 * @author lipw
 * @version 1.0
 */
public class ComboBoxData implements Serializable{
	
	private static final long serialVersionUID = 3726344807754837974L;

	private String id;
	
	private String text;
	
	public ComboBoxData() {
		super();
	}
	
	public ComboBoxData(String id, String text) {
		super();
		this.id = id;
		this.text = text;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
