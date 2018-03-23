package com.gionee.gniflow.biz.model;

import com.gionee.gnif.model.base.BusinessObject;

public class BpmConfNode extends BusinessObject {

	private static final long serialVersionUID = 5450647446409314624L;
	
	private String code;
    private String name;
    private String type;
    private Integer priority;
    private String assignee;
    private String dueDate;
    private Integer confBaseId;

   
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
    public void setPriority(Integer priority) {
        this.priority = priority;
    }
    public Integer getPriority() {
        return priority;
    }
    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }
    public String getAssignee() {
        return assignee;
    }
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
    public String getDueDate() {
        return dueDate;
    }
    public void setConfBaseId(Integer confBaseId) {
        this.confBaseId = confBaseId;
    }
    public Integer getConfBaseId() {
        return confBaseId;
    }
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

}
