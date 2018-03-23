/**
 * @(#) BpmTaskEntity.java Created on 2014年9月25日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.bmp;

import org.activiti.engine.impl.persistence.entity.TaskEntity;

/**
 * The class <code>BpmTaskEntity</code>
 * 
 * @author lipw
 * @version 1.0
 */

public class BpmTaskEntity extends TaskEntity {

	private static final long serialVersionUID = -8201099324495746190L;
	// 流程名称
	private String processDefName;
	// 分类名称
	private String categoryName;
	
	//流程发起人账户
	private String proStartUserId;
	//流程发起人姓名
	private String proStartUserName;
	
	//任务状态，即流程状态
	private String processStatus;
	//流程实例名称（描述）
	private String processInstanceName;
	
	// 是否可在手机端处理
	private Integer conShowInMobile;
		
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

	public String getProStartUserId() {
		return proStartUserId;
	}

	public void setProStartUserId(String proStartUserId) {
		this.proStartUserId = proStartUserId;
	}

	public String getProStartUserName() {
		return proStartUserName;
	}

	public void setProStartUserName(String proStartUserName) {
		this.proStartUserName = proStartUserName;
	}

	/**
	 * @return the processStatus
	 */
	public String getProcessStatus() {
		return processStatus;
	}

	/**
	 * @param processStatus the processStatus to set
	 */
	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}

	public String getProcessInstanceName() {
		return processInstanceName;
	}

	public void setProcessInstanceName(String processInstanceName) {
		this.processInstanceName = processInstanceName;
	}

	public Integer getConShowInMobile() {
		return conShowInMobile;
	}

	public void setConShowInMobile(Integer conShowInMobile) {
		this.conShowInMobile = conShowInMobile;
	}

	
}
