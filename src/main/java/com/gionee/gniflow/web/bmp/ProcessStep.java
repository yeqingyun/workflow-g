/**
 * @(#) ProcessStep.java Created on 2014年7月9日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.bmp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Attachment;

/**
 * The class <code>ProcessStep</code>
 *
 * @author lipw
 * @version 1.0
 */
public class ProcessStep implements Serializable {

	private static final long serialVersionUID = 1960771736379060369L;

	private String key;
	
	//开始时间
	protected Date startTime;
	//结束时间
	protected Date endTime;
	//任务处理人
	protected String assignee;
	//任务节点名称
	protected String activityName;
	//过期时间
	protected Long durationInMillis;
	  
	private Map<String,Object> vars;
	
	private List<Attachment> attachments;
	
	//存会签时的多任务
	private List<ProcessStep> multiInstances = new ArrayList<ProcessStep>(); 
	
	public ProcessStep() {
		super();
	}
	
	public ProcessStep(String key, Map<String, Object> vars) {
		super();
		this.key = key;
		this.vars = vars;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Map<String, Object> getVars() {
		return vars;
	}

	public void setVars(Map<String, Object> vars) {
		this.vars = vars;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public Long getDurationInMillis() {
		return durationInMillis;
	}

	public void setDurationInMillis(Long durationInMillis) {
		this.durationInMillis = durationInMillis;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	public List<ProcessStep> getMultiInstances() {
		return multiInstances;
	}

	public void setMultiInstances(List<ProcessStep> multiInstances) {
		this.multiInstances = multiInstances;
	}

}
