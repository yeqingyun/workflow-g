/**
 * @(#) SignFileInformationResponse.java Created on 2014年10月27日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.response;

import com.gionee.gniflow.biz.model.SignFileInformation;

/**
 * The class <code>SignFileInformationResponse</code>
 * 
 * @author lipw
 * @version 1.0
 */
public class SignFileInformationResponse {

	private Integer id;
	private Integer companyId;// 所属公司ID
	private String companyName;// 所属公司名称
	private Integer orgId;// 部门ID
	private String orgCode;// 部门代码
	private String orgName;// 部门名称
	private String createrName;// 拟定人姓名
	private String fileName;// 文件名称
	private String fileType;// 文件类型
	private String fileNo;// 文件编号
	private String fileEndSerNo;// 文件最终编号
	private String fileVersion;// 文件版本
	private String fileVersionState;// 文件版本状态
	private Integer audiStatus;// 审核状态
	private String protoFileNo;// 原始文件编号
	private String protoFileVersion;// 原始文件版本
	private String preFileVersionState;// 原始文件版本状态
	private int fileOperateType;// 文件操作类型
	private String procInstId;// 流程实例ID
	
	
	public SignFileInformationResponse(SignFileInformation signFileInformation){
		setId(signFileInformation.getId());
		setCompanyId(signFileInformation.getCompanyId());
		setCompanyName(signFileInformation.getCompanyName());
		setOrgId(signFileInformation.getOrgId());
		setOrgCode(signFileInformation.getOrgCode());
		setOrgName(signFileInformation.getOrgName());
		setCreaterName(signFileInformation.getCreaterName());
		setFileName(signFileInformation.getFileName());
		setFileType(signFileInformation.getFileType());
		setFileNo(signFileInformation.getFileNo());
		setFileEndSerNo(signFileInformation.getFileEndSerNo());
		setFileVersion(signFileInformation.getFileVersion());
		setFileVersionState(signFileInformation.getFileVersionState());
		setAudiStatus(signFileInformation.getAudiStatus());
		setProtoFileNo(signFileInformation.getProtoFileNo());
		setProtoFileVersion(signFileInformation.getProtoFileVersion());
		setPreFileVersionState(signFileInformation.getPreFileVersionState());
		setFileOperateType(signFileInformation.getFileOperateType());
		setProcInstId(signFileInformation.getProcInstId());
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getCreaterName() {
		return createrName;
	}

	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileNo() {
		return fileNo;
	}

	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
	}

	public String getFileEndSerNo() {
		return fileEndSerNo;
	}

	public void setFileEndSerNo(String fileEndSerNo) {
		this.fileEndSerNo = fileEndSerNo;
	}

	public String getFileVersion() {
		return fileVersion;
	}

	public void setFileVersion(String fileVersion) {
		this.fileVersion = fileVersion;
	}

	public String getFileVersionState() {
		return fileVersionState;
	}

	public void setFileVersionState(String fileVersionState) {
		this.fileVersionState = fileVersionState;
	}

	public Integer getAudiStatus() {
		return audiStatus;
	}

	public void setAudiStatus(Integer audiStatus) {
		this.audiStatus = audiStatus;
	}

	public String getProtoFileNo() {
		return protoFileNo;
	}

	public void setProtoFileNo(String protoFileNo) {
		this.protoFileNo = protoFileNo;
	}

	public String getProtoFileVersion() {
		return protoFileVersion;
	}

	public void setProtoFileVersion(String protoFileVersion) {
		this.protoFileVersion = protoFileVersion;
	}

	public String getPreFileVersionState() {
		return preFileVersionState;
	}

	public void setPreFileVersionState(String preFileVersionState) {
		this.preFileVersionState = preFileVersionState;
	}

	public int getFileOperateType() {
		return fileOperateType;
	}

	public void setFileOperateType(int fileOperateType) {
		this.fileOperateType = fileOperateType;
	}

	public String getProcInstId() {
		return procInstId;
	}

	public void setProcInstId(String procInstId) {
		this.procInstId = procInstId;
	}

}
