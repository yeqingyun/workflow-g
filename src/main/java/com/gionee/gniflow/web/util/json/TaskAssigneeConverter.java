/**
 * @(#) TaskAssigneeConverter.java Created on 2014年12月2日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.util.json;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.gionee.auth.UserService;
import com.gionee.auth.model.User;
import com.gionee.gniflow.web.util.SpringTools;
/**
 * The class <code>TaskAssigneeConverter</code>
 *
 * @author lipw
 * @version 1.0
 */
public class TaskAssigneeConverter extends StdConverter<String, String>{
	
	@Override
	public String convert(String value) {
		UserService userService = (UserService) SpringTools.getBean(UserService.class);
		String name = "";
		try {
			if(StringUtils.isNotEmpty(value)) {
				User user = userService.getUserByAccount(value);
				if(user != null)
					name = "[" + value + "]" + user.getName();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return name;
	}

}
