package com.gionee.gniflow.biz.model;

import com.gionee.gnif.model.base.BusinessObject;


public class LTeamActivitiesDetail extends BusinessObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1285817177933402158L;
	
	private String procInstId;//流程ID
	private String department;//部门
    private String name;//姓名
    private String empId;//员工编号
    private String company;//所在公司
    private String activityYear;//活动年份 
    private String activityQuarter;//活动季度
    private String activityMonth;//活动月份 
	public String getProcInstId() {
		return procInstId;
	}
	public void setProcInstId(String procInstId) {
		this.procInstId = procInstId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getActivityYear() {
		return activityYear;
	}
	public void setActivityYear(String activityYear) {
		this.activityYear = activityYear;
	}
	public String getActivityQuarter() {
		return activityQuarter;
	}
	public void setActivityQuarter(String activityQuarter) {
		this.activityQuarter = activityQuarter;
	}
	public String getActivityMonth() {
		return activityMonth;
	}
	public void setActivityMonth(String activityMonth) {
		this.activityMonth = activityMonth;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
   
    

}
