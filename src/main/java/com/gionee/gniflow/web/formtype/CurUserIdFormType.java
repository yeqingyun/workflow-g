/**
 * @(#) CurUserIdFormType.java Created on 2014年6月5日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.formtype;

import org.activiti.engine.form.AbstractFormType;

/**
 * The class <code>CurUserIdFormType</code>
 *
 * @author lipw
 * @version 1.0
 */
public class CurUserIdFormType extends AbstractFormType {
	
	private static final String USERS_FROM_TYPE_CURUSERID = "curUserId";

	@Override
	public String getName() {
		return USERS_FROM_TYPE_CURUSERID;
	}

	@Override
	public Object convertFormValueToModelValue(String propertyValue) {
		 return propertyValue;
	}

	@Override
	public String convertModelValueToFormValue(Object modelValue) {
		return (String) modelValue;
	}

}
