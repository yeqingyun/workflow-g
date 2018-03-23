/**
 * @(#) PropertyGridRow.java Created on 2014年11月25日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.easyui;

import java.io.Serializable;

/**
 * The class <code>PropertyGridRow</code>
 *
 * @author lipw
 * @version 1.0
 */
public class PropertyGridRow implements Serializable {

	private static final long serialVersionUID = 3015567844796842857L;
	
	private String name;
	
	private String value;
	
	private String editor;
	
	public PropertyGridRow(String name, String value, String editor) {
		super();
		this.name = name;
		this.value = value;
		this.editor = editor;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}
	
}
