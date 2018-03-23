/**
 * @(#) TaskDto.java Created on 2014年4月17日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gionee.gniflow.web.util.json.TaskAssigneeConverter;

/**
 * The class <code>TaskDto</code>
 * 
 * @author lipw
 * @version 1.0
 */
public class TaskDto implements Serializable{

	private static final long serialVersionUID = 1199120500066656690L;

	private String id;
	
	private String taskDefinitionKey;
	
	private String name;
	
	private String processInstanceId;

	private String processDefinitionId;
	
	private int priority;
	
	private Date createTime;
	
	private Date startTime;
	
	private Date dueDate;
	
	private String description;
	
	private String owner;
	//状态
	private int suspensionState;
	//负责人
	@JsonSerialize(converter=TaskAssigneeConverter.class)
	private String assignee;
	//流程名称
	private String processDefName;
	//分类名称
	private String categoryName;
	//流程发起人账户
	private String proStartUserId;
	//流程发起人姓名
	private String proStartUserName;
	//流程实例名称（描述）
	private String processInstanceName;
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTaskDefinitionKey() {
		return taskDefinitionKey;
	}

	public void setTaskDefinitionKey(String taskDefinitionKey) {
		this.taskDefinitionKey = taskDefinitionKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public int getSuspensionState() {
		return suspensionState;
	}

	public void setSuspensionState(int suspensionState) {
		this.suspensionState = suspensionState;
	}
	
	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
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

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

}
