/**
 * @(#) HisFromDataResponse.java Created on 2014年4月23日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.task.Attachment;
import org.activiti.rest.service.api.form.RestFormProperty;

/**
 * The class <code>HisFromDataResponse</code>
 * 历史表单节点输出对象
 * @author lipw
 * @version 1.0
 */
public class HisNodeFromDataResponse implements Serializable {

	private static final long serialVersionUID = 7852632923998888542L;

	protected String formKey;
	protected String deploymentId;
	protected String processDefinitionId;
	protected String taskId;
	protected String activityId;
	protected String activityName;
	protected List<RestFormProperty> formProperties = new ArrayList<RestFormProperty>();
	protected List<Attachment> taskAttachmentList = new ArrayList<Attachment>();

	public String getFormKey() {
		return formKey;
	}

	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}

	public String getDeploymentId() {
		return deploymentId;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public List<RestFormProperty> getFormProperties() {
		return formProperties;
	}

	public void setFormProperties(List<RestFormProperty> formProperties) {
		this.formProperties = formProperties;
	}

	public void addFormProperty(RestFormProperty formProperty) {
		formProperties.add(formProperty);
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public List<Attachment> getTaskAttachmentList() {
		return taskAttachmentList;
	}

	public void setTaskAttachmentList(List<Attachment> taskAttachmentList) {
		this.taskAttachmentList = taskAttachmentList;
	}
	
}
