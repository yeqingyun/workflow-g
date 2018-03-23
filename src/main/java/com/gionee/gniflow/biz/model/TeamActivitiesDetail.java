package com.gionee.gniflow.biz.model;

import com.gionee.gnif.model.base.BusinessObject;
import java.util.Date;


public class TeamActivitiesDetail extends BusinessObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1285817177933402158L;
	
	private String department;//部门
    private String name;//姓名
    private Date entryDate;//入职日期
    private Date travelDate;//旅游假期
    private String procInstId;//流程ID
    private String account;//员工编号
    private String company;//所在公司
    private Integer number;//序号
    private Date leaveDate;//离职日期
    private String job;//岗位
    private Integer cost;//费用 
   
    
	public Date getLeaveDate() {
		return leaveDate;
	}
	public void setLeaveDate(Date leaveDate) {
		this.leaveDate = leaveDate;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public Integer getCost() {
		return cost;
	}
	public void setCost(Integer cost) {
		this.cost = cost;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public void setDepartment(String department) {
        this.department = department;
    }
    public String getDepartment() {
        return department;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }
    public Date getEntryDate() {
        return entryDate;
    }
    public void setTravelDate(Date travelDate) {
        this.travelDate = travelDate;
    }
    public Date getTravelDate() {
        return travelDate;
    }
    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }
    public String getProcInstId() {
        return procInstId;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getAccount() {
        return account;
    }
    public void setCompany(String company) {
        this.company = company;
    }
    public String getCompany() {
        return company;
    }
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}

}
