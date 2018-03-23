package com.gionee.gniflow.biz.model;

import com.gionee.gnif.model.base.BusinessObject;

public class BpmConfNodeEmail extends BusinessObject {

	private static final long serialVersionUID = -5093964933933968656L;

	private String processDefKey;
	private String taskDefKey;
	private Integer sendFlag;
	private String addressee;
	private Integer confEmailTemplateId;

	// display
	private Integer templateId;
	private String name;
	private String subject;
	private String content;

	public void setProcessDefKey(String processDefKey) {
		this.processDefKey = processDefKey;
	}

	public String getProcessDefKey() {
		return processDefKey;
	}

	public void setTaskDefKey(String taskDefKey) {
		this.taskDefKey = taskDefKey;
	}

	public String getTaskDefKey() {
		return taskDefKey;
	}

	public void setSendFlag(Integer sendFlag) {
		this.sendFlag = sendFlag;
	}

	public Integer getSendFlag() {
		return sendFlag;
	}

	public void setAddressee(String addressee) {
		this.addressee = addressee;
	}

	public String getAddressee() {
		return addressee;
	}

	public void setConfEmailTemplateId(Integer confEmailTemplateId) {
		this.confEmailTemplateId = confEmailTemplateId;
	}

	public Integer getConfEmailTemplateId() {
		return confEmailTemplateId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the templateId
	 */
	public Integer getTemplateId() {
		return templateId;
	}

	/**
	 * @param templateId the templateId to set
	 */
	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

}
