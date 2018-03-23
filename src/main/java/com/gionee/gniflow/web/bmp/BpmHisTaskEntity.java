/**
 * @(#) BpmHisTaskEntity.java Created on 2014年9月27日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.bmp;

import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;

/**
 * The class <code>BpmHisTaskEntity</code>
 * 
 * @author lipw
 * @version 1.0
 */
public class BpmHisTaskEntity extends HistoricTaskInstanceEntity {

	private static final long serialVersionUID = 3795186184618085913L;
	// 流程名称
	private String processDefName;
	// 分类名称
	private String categoryName;
	//流程发起人账户
	private String proStartUserId;
	//流程发起人姓名
	private String proStartUserName;
	//流程实例名称（描述）
	private String processInstanceName;
	
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

	public String getProcessInstanceName() {
		return processInstanceName;
	}

	public void setProcessInstanceName(String processInstanceName) {
		this.processInstanceName = processInstanceName;
	}

}
