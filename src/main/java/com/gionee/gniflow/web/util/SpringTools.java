/**
 * @(#) SpringTool.java Created on 2014年4月14日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * The class <code>SpringTool</code>
 *
 * @author lipw
 * @version 1.0
 */
public class SpringTools implements ApplicationContextAware{
	private static ApplicationContext application = null;
	
	public void setApplicationContext(ApplicationContext applicationcontext)
			throws BeansException {
		if (SpringTools.application == null) {
			SpringTools.application = applicationcontext;
		}
	}

	public static ApplicationContext getApplication() {
		return application;
	}
	
	public static Object getBean(String beanName){
		return getApplication().getBean(beanName);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object getBean(Class clazz){
		return getApplication().getBean(clazz);
	}

	public static void setApplication(ApplicationContext application) {
		SpringTools.application = application;
	}
}
