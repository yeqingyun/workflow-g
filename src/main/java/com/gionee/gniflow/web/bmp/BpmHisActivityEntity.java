/**
 * @(#) BpmTaskEntity.java Created on 2014年8月7日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.bmp;

import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntity;

/**
 * The class <code>BpmTaskEntity</code>
 *
 * @author lipw
 * @version 1.0
 */
public class BpmHisActivityEntity extends HistoricActivityInstanceEntity {

	private static final long serialVersionUID = -6698945798964929867L;
	
	private String userAccount;
	
	private String userName;
	
	private String proStep;

	// 流程名称
	private String processDefName;
	// 分类名称
	private String categoryName;
	//流程实例名称
	private String processInstanceName;
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getProStep() {
		return proStep;
	}

	public void setProStep(String proStep) {
		this.proStep = proStep;
	}

	public String getProcessDefName() {
		return processDefName;
	}

	public void setProcessDefName(String processDefName) {
		this.processDefName = processDefName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getProcessInstanceName() {
		return processInstanceName;
	}

	public void setProcessInstanceName(String processInstanceName) {
		this.processInstanceName = processInstanceName;
	}
	
}
