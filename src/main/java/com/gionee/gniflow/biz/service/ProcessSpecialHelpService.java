/**
 * @(#) ProcessSpecialHelpService.java Created on 2014年12月11日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.biz.service;

/**
 * The class <code>ProcessSpecialHelpService</code>
 *
 * @author lipw
 * @version 1.0
 */
public interface ProcessSpecialHelpService {
	
	/**
	 * 流程：金立内部调动流程
	 * 作用：判断一级调用，即调出人所在的部门是不是与调入部门是用一个总监
	 * @param account
	 * @return
	 */
	Boolean isLevelOneTransfer(String outOrgId, String inOrgId);
}
