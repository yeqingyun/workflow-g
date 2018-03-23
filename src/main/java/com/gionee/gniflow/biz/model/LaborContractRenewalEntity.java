package com.gionee.gniflow.biz.model;

import java.io.Serializable;

public class LaborContractRenewalEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4872648047735460734L;
	private String processInstanceId;//流程编号
	private String principalOrgName;//员工部门
	private String principalOrgId;//员工部门编号
	private String principalEmpId;//员工帐号
	private String principalAccount;//员工帐号
	private String principalName;//员工姓名
	private String principalJob;//员工职位
	private String principalEntryTime;//员工入职时间
	private String principalContractExpire;//合同到期时间
	private String principalContractType;//续签合同类型
	private String principalContractStartTime;//本次合同开始时间
	private String principalContractEndTime;//本次合同结束时间
	private String approvalOpinion;//审批意见（同意/不同意）
	private String status;//状态
	private String approvalUserName;//审批人
	private String startTime;//创建时间
	private String endTime;//结束时间
    private String checkUserName;
    private String actName;
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public String getPrincipalOrgName() {
		return principalOrgName;
	}
	public void setPrincipalOrgName(String principalOrgName) {
		this.principalOrgName = principalOrgName;
	}
	public String getPrincipalOrgId() {
		return principalOrgId;
	}
	public void setPrincipalOrgId(String principalOrgId) {
		this.principalOrgId = principalOrgId;
	}
	public String getPrincipalAccount() {
		return principalAccount;
	}
	public void setPrincipalAccount(String principalAccount) {
		this.principalAccount = principalAccount;
	}
	public String getPrincipalName() {
		return principalName;
	}
	public void setPrincipalName(String principalName) {
		this.principalName = principalName;
	}
	public String getPrincipalJob() {
		return principalJob;
	}
	public void setPrincipalJob(String principalJob) {
		this.principalJob = principalJob;
	}
	public String getPrincipalEntryTime() {
		return principalEntryTime;
	}
	public void setPrincipalEntryTime(String principalEntryTime) {
		this.principalEntryTime = principalEntryTime;
	}
	public String getPrincipalContractType() {
		return principalContractType;
	}
	public void setPrincipalContractType(String principalContractType) {
		this.principalContractType = principalContractType;
	}
	public String getPrincipalContractStartTime() {
		return principalContractStartTime;
	}
	public void setPrincipalContractStartTime(String principalContractStartTime) {
		this.principalContractStartTime = principalContractStartTime;
	}
	public String getPrincipalContractEndTime() {
		return principalContractEndTime;
	}
	public void setPrincipalContractEndTime(String principalContractEndTime) {
		this.principalContractEndTime = principalContractEndTime;
	}
	public String getApprovalOpinion() {
		return approvalOpinion;
	}
	public void setApprovalOpinion(String approvalOpinion) {
		this.approvalOpinion = approvalOpinion;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getApprovalUserName() {
		return approvalUserName;
	}
	public void setApprovalUserName(String approvalUserName) {
		this.approvalUserName = approvalUserName;
	}
	public String getPrincipalEmpId() {
		return principalEmpId;
	}
	public void setPrincipalEmpId(String principalEmpId) {
		this.principalEmpId = principalEmpId;
	}
	public String getPrincipalContractExpire() {
		return principalContractExpire;
	}
	public void setPrincipalContractExpire(String principalContractExpire) {
		this.principalContractExpire = principalContractExpire;
	}
	public String getCheckUserName() {
		return checkUserName;
	}
	public void setCheckUserName(String checkUserName) {
		this.checkUserName = checkUserName;
	}
	public String getActName() {
		return actName;
	}
	public void setActName(String actName) {
		this.actName = actName;
	}
	
	
	
}
