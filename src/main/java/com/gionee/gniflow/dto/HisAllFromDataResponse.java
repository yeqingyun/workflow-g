/**
 * @(#) HisAllFromDataResponse.java Created on 2014年4月23日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.task.Attachment;

/**
 * The class <code>HisAllFromDataResponse</code>
 *
 * @author lipw
 * @version 1.0
 */
public class HisAllFromDataResponse implements Serializable{

	private static final long serialVersionUID = 262978973990951556L;
	
	private List<HisNodeFromDataResponse> nodeFormDataResponse = new ArrayList<HisNodeFromDataResponse>();
	
	private List<Attachment> attachmentList;

	public List<HisNodeFromDataResponse> getNodeFormDataResponse() {
		return nodeFormDataResponse;
	}

	public void setNodeFormDataResponse(
			List<HisNodeFromDataResponse> nodeFormDataResponse) {
		this.nodeFormDataResponse = nodeFormDataResponse;
	}

	public List<Attachment> getAttachmentList() {
		return attachmentList;
	}

	public void setAttachmentList(List<Attachment> attachmentList) {
		this.attachmentList = attachmentList;
	}
	
}
