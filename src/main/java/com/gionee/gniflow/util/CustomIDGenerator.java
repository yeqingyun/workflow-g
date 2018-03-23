/**
 * @(#) CustomIDGenerator.java Created on 2014年11月17日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.util;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.gionee.gniflow.web.util.DateUtil;

/**
 * The class <code>CustomIDGenerator</code>
 * 
 * @author lipw
 * @version 1.0
 */
public class CustomIDGenerator implements org.activiti.engine.impl.cfg.IdGenerator{
	
	private static final String GC_PROPERTY_KEY_PREFIX = "bpm.";
	
	@Autowired
	private com.gionee.gnif.util.IDGenerator idGenerator;
	
	@Override
	public String getNextId() {
		String dateFormat = DateUtil.format(new Date(), DateUtil.DATE_TIME_FORMAT_DAY_YYYYMMDD);
		StringBuffer sb = new StringBuffer(dateFormat);
		Integer sequence = idGenerator.getId(GC_PROPERTY_KEY_PREFIX + dateFormat);
		sb.append(String.format("%07d", sequence));
		return sb.toString();
	}
	
}
