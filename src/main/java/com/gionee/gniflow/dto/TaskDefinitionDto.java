/**
 * @(#) TaskDefinitionDto.java Created on 2014年11月24日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.dto;

import java.io.Serializable;

/**
 * The class <code>TaskDefinitionDto</code>
 * 
 * @author lipw
 * @version 1.0
 */
public class TaskDefinitionDto implements Serializable {

	private static final long serialVersionUID = -24225271855645427L;

	// 任务定义的Key
	private String taskDefKey;
	// 任务名称
	private String taskName;
	// 任务的描述
	private String processDefKey;
	// 表达式
	private String assigneeExpression;
	//类型
	private String taskType;

	public String getTaskDefKey() {
		return taskDefKey;
	}

	public void setTaskDefKey(String taskDefKey) {
		this.taskDefKey = taskDefKey;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getAssigneeExpression() {
		return assigneeExpression;
	}

	public void setAssigneeExpression(String assigneeExpression) {
		this.assigneeExpression = assigneeExpression;
	}

	public String getProcessDefKey() {
		return processDefKey;
	}

	public void setProcessDefKey(String processDefKey) {
		this.processDefKey = processDefKey;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

}
