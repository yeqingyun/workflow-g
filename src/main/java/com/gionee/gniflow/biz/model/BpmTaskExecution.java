package com.gionee.gniflow.biz.model;

import com.gionee.gnif.model.base.BusinessObject;

public class BpmTaskExecution extends BusinessObject {

	private static final long serialVersionUID = 1417481744849499243L;
	
	private String taskId;
    private String assignee;
    private String assigneeName;
    private String owner;
    private String ownerName;
    private String taskName;
    private String taskDefKey;
    private String procInstId;
    private String subject;
    private String suggestion;
    private Integer assignType;

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    public String getTaskId() {
        return taskId;
    }
    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }
    public String getAssignee() {
        return assignee;
    }
    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }
    public String getAssigneeName() {
        return assigneeName;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }
    public String getOwner() {
        return owner;
    }
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
    public String getOwnerName() {
        return ownerName;
    }
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    public String getTaskName() {
        return taskName;
    }
    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }
    public String getTaskDefKey() {
        return taskDefKey;
    }
    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }
    public String getProcInstId() {
        return procInstId;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getSubject() {
        return subject;
    }
    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }
    public String getSuggestion() {
        return suggestion;
    }
    public void setAssignType(Integer assignType) {
        this.assignType = assignType;
    }
    public Integer getAssignType() {
        return assignType;
    }

}
