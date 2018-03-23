package com.gionee.gniflow.biz.model;

import com.gionee.gnif.model.base.BusinessObject;

public class SapReqSheetAttachment extends BusinessObject {

	private static final long serialVersionUID = -802201946146324353L;
	
	private String preqNo;
    private Integer preqItem;
    
    private String fileNo;
    private String fileName;
    private char delFlag;

	public void setPreqNo(String preqNo) {
		this.preqNo = preqNo;
	}

	public String getPreqNo() {
		return preqNo;
	}

	public void setPreqItem(Integer preqItem) {
		this.preqItem = preqItem;
	}

	public Integer getPreqItem() {
		return preqItem;
	}

	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
	}

	public String getFileNo() {
		return fileNo;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setDelFlag(char delFlag) {
		this.delFlag = delFlag;
	}

	public char getDelFlag() {
		return delFlag;
	}
    
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(20);
		sb.append("[").append("preqNo=").append(preqNo)
		.append(",").append("preqItem=").append(preqItem)
		.append(",").append("fileNo=").append(fileNo)
		.append(",").append("fileName=").append(fileName)
		.append("]");
		return sb.toString();
	}
    
}
