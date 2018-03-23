package com.gionee.gniflow.biz.model;

import com.gionee.gniflow.web.bmp.BpmHisProcessInstanceEntity;

public class Probation extends BpmHisProcessInstanceEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String orgName;//部门名称
	private String agreeTime;//转正日期
	private String account;//员工编号
	private String empName;//员工名字
	private String empJob;//职位
	private String salAdjust;//调薪审批
	private String curSalaryGroup;//试用期薪级
	private String curSalary;//试用期薪资
	private String fuSalaryGroup;//转正后薪级
	private String fuSalary;//转正后薪资
	
	
	public String getSalAdjust() {
		return salAdjust;
	}
	public void setSalAdjust(String salAdjust) {
		this.salAdjust = salAdjust;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getAgreeTime() {
		return agreeTime;
	}
	public void setAgreeTime(String agreeTime) {
		this.agreeTime = agreeTime;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getEmpJob() {
		return empJob;
	}
	public void setEmpJob(String empJob) {
		this.empJob = empJob;
	}
	public String getCurSalaryGroup() {
		return curSalaryGroup;
	}
	public void setCurSalaryGroup(String curSalaryGroup) {
		this.curSalaryGroup = curSalaryGroup;
	}
	public String getCurSalary() {
		return curSalary;
	}
	public void setCurSalary(String curSalary) {
		this.curSalary = curSalary;
	}
	public String getFuSalaryGroup() {
		return fuSalaryGroup;
	}
	public void setFuSalaryGroup(String fuSalaryGroup) {
		this.fuSalaryGroup = fuSalaryGroup;
	}
	public String getFuSalary() {
		return fuSalary;
	}
	public void setFuSalary(String fuSalary) {
		this.fuSalary = fuSalary;
	}
}
