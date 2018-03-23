/**
 * @(#) UsersFormType.java Created on 2014年6月5日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.formtype;

import org.activiti.engine.form.AbstractFormType;

/**
 * The class <code>UsersFormType</code>
 *
 * @author lipw
 * @version 1.0
 */
public class ComboTreeFormType extends AbstractFormType {
	
	private static final String USERS_FROM_TYPE_COMBOTREE = "comboTree_";
	
	private String name;
	
	private String url;
	
	public ComboTreeFormType(String name, String url) {
		this.name = name;
		this.url = url;
	}

	@Override
	public String getName() {
		return USERS_FROM_TYPE_COMBOTREE + name;
	}

	@Override
	public Object convertFormValueToModelValue(String propertyValue) {
		 return propertyValue;
	}

	@Override
	public String convertModelValueToFormValue(Object modelValue) {
		return (String) modelValue;
	}
	
	@Override
	public Object getInformation(String key) {
		if ("url".equals(key)) {
		      return url;
		    }
		return null;
	}
}
