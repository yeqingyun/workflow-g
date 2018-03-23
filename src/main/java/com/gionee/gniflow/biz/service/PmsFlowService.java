/**
 * @(#) PmsFlowService.java Created on 2014年6月10日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.biz.service;

/**
 * The class <code>PmsFlowService</code>
 *
 * @author lipw
 * @version 1.0
 */
public interface PmsFlowService {
	/** 获取部门领导 */
	public String getDeptLeader(Object userAccount);
	
	/** 获取项目负责人*/
	public String getProjectCharge(String projectName);
	
}
