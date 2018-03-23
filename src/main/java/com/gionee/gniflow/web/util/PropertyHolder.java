package com.gionee.gniflow.web.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
/**
 * The class <code>PropertyHolder</code>
 * 当Spring初始化加载config.properties文件时，将其文件的key-value存储到
 * 一个HashMap中，方便调用。
 * @author lipw
 * @version 1.0
 */
public class PropertyHolder extends PropertyPlaceholderConfigurer {
	private static Map<String, String> ctxPropertiesMap;

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess,
			Properties props) throws BeansException {
		super.processProperties(beanFactoryToProcess, props);
		ctxPropertiesMap = new HashMap<String, String>();
		for (Object key : props.keySet()) {
			String keyStr = key.toString();
			String value = props.getProperty(keyStr);
			ctxPropertiesMap.put(keyStr, value);
		}
	}

	public static String getContextProperty(String name) {
		return ctxPropertiesMap.get(name);
	}
}
