package com.gionee.gniflow.biz.model;

public class Requirements {
	 private String processInstanceId;
	 private Integer processStatus;
	 private String reqDate;//申请日期
     private String reqSystem;//需求系统
     private String orgName;//需求部门
     private String reqJob;//职位名称
     private String reqRank;//职位职级
     private String reqNumber;//需求人数
     private String reqHopeDate;//期望到岗日期
     private String reqType;//需求类型
     private String sex;//性别
     private String education;//学历
     private String major;//专业
     private String reqChannels;//招聘途径
     private String checkUserName;
     private String actName;
	public String getReqDate() {
		return reqDate;
	}
	public void setReqDate(String reqDate) {
		this.reqDate = reqDate;
	}
	public String getReqSystem() {
		return reqSystem;
	}
	public void setReqSystem(String reqSystem) {
		this.reqSystem = reqSystem;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getReqJob() {
		return reqJob;
	}
	public void setReqJob(String reqJob) {
		this.reqJob = reqJob;
	}
	public String getReqRank() {
		return reqRank;
	}
	public void setReqRank(String reqRank) {
		this.reqRank = reqRank;
	}
	public String getReqNumber() {
		return reqNumber;
	}
	public void setReqNumber(String reqNumber) {
		this.reqNumber = reqNumber;
	}
	public String getReqHopeDate() {
		return reqHopeDate;
	}
	public void setReqHopeDate(String reqHopeDate) {
		this.reqHopeDate = reqHopeDate;
	}
	public String getReqType() {
		return reqType;
	}
	public void setReqType(String reqType) {
		this.reqType = reqType;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public String getReqChannels() {
		return reqChannels;
	}
	public void setReqChannels(String reqChannels) {
		this.reqChannels = reqChannels;
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
