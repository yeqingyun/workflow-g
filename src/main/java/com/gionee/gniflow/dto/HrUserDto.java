/**
 * @(#) HrUserDto.java Created on 2014年8月20日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gionee.gniflow.web.util.CustomDateSerializer;

/**
 * The class <code>HrUserDto</code>
 *
 * @author lipw
 * @version 1.0
 */
public class HrUserDto implements Serializable {
	
	private static final long serialVersionUID = -7799461613036642324L;
	
	private Integer empId;//员工编号

	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	private String name;//姓名
	
	private String orgName;//部门
	
	private String job;//岗位
	
	private String position;//岗位性质
	
	@JsonSerialize(using = CustomDateSerializer.class)
	private Date entryDate;//入职日期
	
	@JsonSerialize(using = CustomDateSerializer.class)
	private Date departureDate;//离职日期
	
	private String education;//学历
	
	private String idCard;//身份证
	
	private String sex;//性别
	
	@JsonSerialize(using = CustomDateSerializer.class)
	private Date birthdayDate;//出生日期
	
	private String national;//民族
	
	private String nativePlace;//籍贯
	
	private String maritalStatus;//婚姻状况
	
	private Integer age;//年龄
	
	private String major;//专业
	
	//update by lipw 2014-11-16
	private String isAttend;//是否考勤人员1-是，2-否
	
	private String email;//邮箱
	
	private Integer companyId;//公司ID
	
	private String companyName;//公司名
	
	private String status;//0-离职，1-不活动，2-退休，3-在职
	
	private String tel;//联系电话
	
	private String householdRegAddress;//户籍地址
	
	private String residenceAddress;//户口
	
	private String enLevel;//英语等级
	
	private String computerLevel;//计算机水平
	
	private String systemName;//系统名称
	
	private String companyDomain;//公司域
	
	private String workPlace;//人员工作地点
	
	private String school;//毕业院校
	
	private String empType;//员工类型
	
	private String empGroup;//员工组
	
	private String empChildGroup;//员工子组
	private String empChildGroupName;//员工子组名称
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Date getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Date getBirthdayDate() {
		return birthdayDate;
	}

	public void setBirthdayDate(Date birthdayDate) {
		this.birthdayDate = birthdayDate;
	}

	public String getNational() {
		return national;
	}

	public void setNational(String national) {
		this.national = national;
	}

	public String getNativePlace() {
		return nativePlace;
	}

	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getIsAttend() {
		return isAttend;
	}

	public void setIsAttend(String isAttend) {
		this.isAttend = isAttend;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getHouseholdRegAddress() {
		return householdRegAddress;
	}

	public void setHouseholdRegAddress(String householdRegAddress) {
		this.householdRegAddress = householdRegAddress;
	}

	public String getResidenceAddress() {
		return residenceAddress;
	}

	public void setResidenceAddress(String residenceAddress) {
		this.residenceAddress = residenceAddress;
	}

	public String getEnLevel() {
		return enLevel;
	}

	public void setEnLevel(String enLevel) {
		this.enLevel = enLevel;
	}

	public String getComputerLevel() {
		return computerLevel;
	}

	public void setComputerLevel(String computerLevel) {
		this.computerLevel = computerLevel;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getCompanyDomain() {
		return companyDomain;
	}

	public void setCompanyDomain(String companyDomain) {
		this.companyDomain = companyDomain;
	}

	public String getWorkPlace() {
		return workPlace;
	}

	public void setWorkPlace(String workPlace) {
		this.workPlace = workPlace;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	/**
	 * @return the empType
	 */
	public String getEmpType() {
		return empType;
	}

	/**
	 * @param empType the empType to set
	 */
	public void setEmpType(String empType) {
		this.empType = empType;
	}

	public String getEmpGroup() {
		return empGroup;
	}

	public void setEmpGroup(String empGroup) {
		this.empGroup = empGroup;
	}

	public String getEmpChildGroup() {
		return empChildGroup;
	}

	public void setEmpChildGroup(String empChildGroup) {
		this.empChildGroup = empChildGroup;
	}

	public String getEmpChildGroupName() {
		return empChildGroupName;
	}

	public void setEmpChildGroupName(String empChildGroupName) {
		this.empChildGroupName = empChildGroupName;
	}
	
}
