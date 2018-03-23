/**
 * @(#) BpmConfNodeSendDto.java Created on 2014年11月19日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * The class <code>BpmConfNodeSendDto</code>
 * 
 * @author lipw
 * @version 1.0
 */
public class BpmConfNodeSendDto implements Serializable {

	private static final long serialVersionUID = -7742190776011151888L;

	private Integer id;
	private Integer sendType;
	private Integer sendMailFlag;
	private Integer intervalDay;
	private Integer confTemplateId;
	private Integer confNodeId;

	private Integer status;
	private String remark;
	private String createBy;
	private Date createTime;
	private String updateBy;
	private Date updateTime;

	private String nodeCode;
	private String nodeAssignee;
	private String nodeDueDate;

	private String templateName;
	private String templateSubject;
	private String templateContent;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSendType() {
		return sendType;
	}

	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}

	public Integer getSendMailFlag() {
		return sendMailFlag;
	}

	public void setSendMailFlag(Integer sendMailFlag) {
		this.sendMailFlag = sendMailFlag;
	}

	public Integer getIntervalDay() {
		return intervalDay;
	}

	public void setIntervalDay(Integer intervalDay) {
		this.intervalDay = intervalDay;
	}

	public Integer getConfTemplateId() {
		return confTemplateId;
	}

	public void setConfTemplateId(Integer confTemplateId) {
		this.confTemplateId = confTemplateId;
	}

	public Integer getConfNodeId() {
		return confNodeId;
	}

	public void setConfNodeId(Integer confNodeId) {
		this.confNodeId = confNodeId;
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

	public String getNodeCode() {
		return nodeCode;
	}

	public void setNodeCode(String nodeCode) {
		this.nodeCode = nodeCode;
	}

	public String getNodeAssignee() {
		return nodeAssignee;
	}

	public void setNodeAssignee(String nodeAssignee) {
		this.nodeAssignee = nodeAssignee;
	}

	public String getNodeDueDate() {
		return nodeDueDate;
	}

	public void setNodeDueDate(String nodeDueDate) {
		this.nodeDueDate = nodeDueDate;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateSubject() {
		return templateSubject;
	}

	public void setTemplateSubject(String templateSubject) {
		this.templateSubject = templateSubject;
	}

	public String getTemplateContent() {
		return templateContent;
	}

	public void setTemplateContent(String templateContent) {
		this.templateContent = templateContent;
	}

}
