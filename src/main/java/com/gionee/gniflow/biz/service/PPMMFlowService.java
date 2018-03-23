/**
 * @(#) PPMMFlowService.java Created on 2014年6月25日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.biz.service;

import java.util.List;

/**
 * The class <code>PPMMFlowService</code>
 *
 * @author lipw
 * @version 1.0
 */
public interface PPMMFlowService {
	/** 获取样机管理员 */
	public List<String> getPrototypeAdmins();
	/** 获取财务管理员 */
	public List<String> getFinanceAdmins();
}
