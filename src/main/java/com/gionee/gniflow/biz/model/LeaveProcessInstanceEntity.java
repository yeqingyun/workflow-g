package com.gionee.gniflow.biz.model;

import com.gionee.gniflow.web.bmp.BpmHisProcessInstanceEntity;

public class LeaveProcessInstanceEntity extends BpmHisProcessInstanceEntity{
	
	private static final long serialVersionUID = 4245588711857162643L;
	
	private String leaveResion;
	
	private String detailResion;
	
	private String orgName;//部门名称
	
	private String duty;//职务
	
	private String entryDate;//入职日期
	
	private String empId;
	
	private String endAudi;//是否结束流程，用于标识流程的最终审批意见

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getLeaveResion() {
		return leaveResion;
	}

	public void setLeaveResion(String leaveResion) {
		this.leaveResion = leaveResion;
	}

	public String getDetailResion() {
		return detailResion;
	}

	public void setDetailResion(String detailResion) {
		this.detailResion = detailResion;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}

	public String getEndAudi() {
		return endAudi;
	}

	public void setEndAudi(String endAudi) {
		this.endAudi = endAudi;
	}
	

	
	
}
