/**
 * @(#) FormDataAddAttachmentResponse.java Created on 2014年4月14日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.dto;

import java.util.List;

import org.activiti.engine.task.Attachment;
import org.activiti.rest.service.api.form.FormDataResponse;

/**
 * The class <code>FormDataAddAttachmentResponse</code>
 *
 * @author lipw
 * @version 1.0
 */
public class FormDataAddAttachmentResponse {
	
	private FormDataResponse formDataResponse;
	
	private List<Attachment> attachmentList;
	
	private String formHtml;

	public FormDataResponse getFormDataResponse() {
		return formDataResponse;
	}

	public void setFormDataResponse(FormDataResponse formDataResponse) {
		this.formDataResponse = formDataResponse;
	}

	public List<Attachment> getAttachmentList() {
		return attachmentList;
	}

	public void setAttachmentList(List<Attachment> attachmentList) {
		this.attachmentList = attachmentList;
	}

	public String getFormHtml() {
		return formHtml;
	}

	public void setFormHtml(String formHtml) {
		this.formHtml = formHtml;
	}
	
}
