package com.gionee.gniflow.biz.model;

import com.gionee.gniflow.web.bmp.BpmHisProcessInstanceEntity;

public class AccountHandel  extends BpmHisProcessInstanceEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String depa;//部门
	
	private String empId; //员工编号
	
	private String job;//职位
	
	private String entryDate; //入职日期
	
	private String education;//学历
	
	private String applyDate;//申请时间 
	
	private String name;//姓名

	public String getDepa() {
		return depa;
	}

	public void setDepa(String depa) {
		this.depa = depa;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	
}
