/**
 * @(#) BpmConfNodeDto.java Created on 2014年11月19日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.dto;

import java.util.Date;

/**
 * The class <code>BpmConfNodeDto</code>
 *
 * @author lipw
 * @version 1.0
 */
public class BpmConfNodeDto {
	
	private Integer id;
	private String code;
    private String name;
    private String type;
    private Integer priority;
    private String assignee;
    private String dueDate;
    private Integer confBaseId;
	
	private Integer status;
	private String remark;
	private String createBy;
	private Date createTime;
	private String updateBy;
	private Date updateTime;
	
	private String processDefId;
	private String processDefKey;
	private Integer processDefVersion;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public Integer getConfBaseId() {
		return confBaseId;
	}
	public void setConfBaseId(Integer confBaseId) {
		this.confBaseId = confBaseId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getProcessDefId() {
		return processDefId;
	}
	public void setProcessDefId(String processDefId) {
		this.processDefId = processDefId;
	}
	public String getProcessDefKey() {
		return processDefKey;
	}
	public void setProcessDefKey(String processDefKey) {
		this.processDefKey = processDefKey;
	}
	public Integer getProcessDefVersion() {
		return processDefVersion;
	}
	public void setProcessDefVersion(Integer processDefVersion) {
		this.processDefVersion = processDefVersion;
	}
	
	
}
