package com.gionee.gniflow.biz.model;

import java.io.Serializable;

public class InterviewEntity implements Serializable{
	
	private static final long serialVersionUID = -7034623352115781877L;
	private String processInstanceId;
	private Integer processStatus;
	private String applyTime;//申请日期
	private String appliName;//姓名
	private String inSysName;//系统
	private String inOrgName;//部门
	private String rank;//职级
	private String inJob;//职位
	private String age;//年龄
	private String education;//学历
	private String specialty;//专业
	private String workYear;//工作年限
	private String hrTotalEval;//HR总体评价
	private String depTotalEval;//部门总体评语
	private String teacherName;//新员工导师姓名
	private String salaryLevel;//薪级
    private String alaryProbation;//试用期薪资
	private String salaryPositive;//转正薪资
    private String checkUserName;
    private String actName;
	public String getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}
	public String getAppliName() {
		return appliName;
	}
	public void setAppliName(String appliName) {
		this.appliName = appliName;
	}
	public String getInSysName() {
		return inSysName;
	}
	public void setInSysName(String inSysName) {
		this.inSysName = inSysName;
	}
	public String getInOrgName() {
		return inOrgName;
	}
	public void setInOrgName(String inOrgName) {
		this.inOrgName = inOrgName;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public String getInJob() {
		return inJob;
	}
	public void setInJob(String inJob) {
		this.inJob = inJob;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getSpecialty() {
		return specialty;
	}
	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}
	public String getWorkYear() {
		return workYear;
	}
	public void setWorkYear(String workYear) {
		this.workYear = workYear;
	}
	public String getHrTotalEval() {
		return hrTotalEval;
	}
	public void setHrTotalEval(String hrTotalEval) {
		this.hrTotalEval = hrTotalEval;
	}
	public String getDepTotalEval() {
		return depTotalEval;
	}
	public void setDepTotalEval(String depTotalEval) {
		this.depTotalEval = depTotalEval;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getSalaryLevel() {
		return salaryLevel;
	}
	public void setSalaryLevel(String salaryLevel) {
		this.salaryLevel = salaryLevel;
	}
	public String getAlaryProbation() {
		return alaryProbation;
	}
	public void setAlaryProbation(String alaryProbation) {
		this.alaryProbation = alaryProbation;
	}
	public String getSalaryPositive() {
		return salaryPositive;
	}
	public void setSalaryPositive(String salaryPositive) {
		this.salaryPositive = salaryPositive;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public Integer getProcessStatus() {
		return processStatus;
	}
	public void setProcessStatus(Integer processStatus) {
		this.processStatus = processStatus;
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
