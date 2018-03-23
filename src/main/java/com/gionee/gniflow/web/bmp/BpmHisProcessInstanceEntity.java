/**
 * @(#) BpmHisProcessInstance.java Created on 2014年9月27日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.bmp;

import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;

/**
 * The class <code>BpmHisProcessInstance</code>
 * 
 * @author lipw
 * @version 1.0
 */
public class BpmHisProcessInstanceEntity extends HistoricProcessInstanceEntity {

	private static final long serialVersionUID = 4245588797357162643L;

	// 流程名称
	private String processDefName;
	// 分类名称
	private String categoryName;
	//状态
	private Integer processStatus;
	//流程发起人姓名
	private String startUserName;
	//流程实例名称（描述）
	private String processInstanceName;
	//流程实例ID
	private String processInstanceId;
	
	
	//审批人
	private String checkUserName;
	
	//转办Id
	private String changeId;
	
	private String assignee;
	private String actName;
		
	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getActName() {
		return actName;
	}

	public void setActName(String actName) {
		this.actName = actName;
	}

	public String getProcessDefName() {
		return processDefName;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
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
	
	public Integer getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(Integer processStatus) {
		this.processStatus = processStatus;
	}

	public String getStartUserName() {
		return startUserName;
	}

	public void setStartUserName(String startUserName) {
		this.startUserName = startUserName;
	}

	public String getProcessInstanceName() {
		return processInstanceName;
	}

	public void setProcessInstanceName(String processInstanceName) {
		this.processInstanceName = processInstanceName;
	}

	public String getCheckUserName() {
		return checkUserName;
	}

	public void setCheckUserName(String checkUserName) {
		this.checkUserName = checkUserName;
	}

	public String getChangeId() {
		return changeId;
	}

	public void setChangeId(String changeId) {
		this.changeId = changeId;
	}

}
