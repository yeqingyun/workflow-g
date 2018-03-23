/**
 * @(#) SapReqSheetAttachmentResponse.java Created on 2014年10月27日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.response;

import com.gionee.gniflow.biz.model.SapReqSheetAttachment;

/**
 * The class <code>SapReqSheetAttachmentResponse</code>
 * 
 * @author lipw
 * @version 1.0
 */
public class SapReqSheetAttachmentResponse {

	private String preqNo;
	private Integer preqItem;

	private String fileNo;
	private String fileName;
	private char delFlag;
	
	public SapReqSheetAttachmentResponse(SapReqSheetAttachment sapReqSheetAttachment){
		setPreqNo(sapReqSheetAttachment.getPreqNo());
		setPreqItem(sapReqSheetAttachment.getPreqItem());
		setFileNo(sapReqSheetAttachment.getFileNo());
		setFileName(sapReqSheetAttachment.getFileName());
		setDelFlag(sapReqSheetAttachment.getDelFlag());
	}

	public String getPreqNo() {
		return preqNo;
	}

	public void setPreqNo(String preqNo) {
		this.preqNo = preqNo;
	}

	public Integer getPreqItem() {
		return preqItem;
	}

	public void setPreqItem(Integer preqItem) {
		this.preqItem = preqItem;
	}

	public String getFileNo() {
		return fileNo;
	}

	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public char getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(char delFlag) {
		this.delFlag = delFlag;
	}

}
