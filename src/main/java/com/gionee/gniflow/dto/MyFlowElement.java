/**
 * @(#) MyFlowElement.java Created on 2014年4月23日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.activiti.bpmn.model.FormProperty;

/**
 * The class <code>MyFlowElement</code>
 *
 * @author lipw
 * @version 1.0
 */
public class MyFlowElement implements Serializable{

	private static final long serialVersionUID = -2408034196556516229L;
	
	private String formKey;
	
	private String id;
	
	private String name;
	
	private List<FormProperty> formProperties = new ArrayList<FormProperty>();

	public String getFormKey() {
		return formKey;
	}

	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<FormProperty> getFormProperties() {
		return formProperties;
	}

	public void setFormProperties(List<FormProperty> formProperties) {
		this.formProperties = formProperties;
	} 
	
}
