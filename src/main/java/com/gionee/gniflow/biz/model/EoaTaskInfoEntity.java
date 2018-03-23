package com.gionee.gniflow.biz.model;

import java.util.Date;

public class EoaTaskInfoEntity {
	private String processInstanceId;
	private String processInstanceName;
	private String processDefName;
	private String proStartUserName;
	private String proStartUserId;
	private String categoryName;
	private String id;
	private String name;
	private Date createTime;
	private Date endTime;
	private Integer suspensionState;
	private Integer processStatus;
	private String taskDefinitionKey;
	private String processDefinitionId;
	private String priority;
	private Long duration;
    private String description;
    private Integer ifSign;//是否需要签收
    
	public Integer getIfSign() {
		return ifSign;
	}
	public void setIfSign(Integer ifSign) {
		this.ifSign = ifSign;
	}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public String getProcessInstanceName() {
		return processInstanceName;
	}
	public void setProcessInstanceName(String processInstanceName) {
		this.processInstanceName = processInstanceName;
	}
	public String getProcessDefName() {
		return processDefName;
	}
	public void setProcessDefName(String processDefName) {
		this.processDefName = processDefName;
	}
	public String getProStartUserName() {
		return proStartUserName;
	}
	public void setProStartUserName(String proStartUserName) {
		this.proStartUserName = proStartUserName;
	}
	public String getProStartUserId() {
		return proStartUserId;
	}
	public void setProStartUserId(String proStartUserId) {
		this.proStartUserId = proStartUserId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getProcessStatus() {
		return processStatus;
	}
	public void setProcessStatus(Integer processStatus) {
		this.processStatus = processStatus;
	}
	public Integer getSuspensionState() {
		return suspensionState;
	}
	public void setSuspensionState(Integer suspensionState) {
		this.suspensionState = suspensionState;
	}
	public String getTaskDefinitionKey() {
		return taskDefinitionKey;
	}
	public void setTaskDefinitionKey(String taskDefinitionKey) {
		this.taskDefinitionKey = taskDefinitionKey;
	}
	public String getProcessDefinitionId() {
		return processDefinitionId;
	}
	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Long getDuration() {
		return duration;
	}
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	
	
}
