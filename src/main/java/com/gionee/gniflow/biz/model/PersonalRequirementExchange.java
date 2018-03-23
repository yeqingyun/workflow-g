package com.gionee.gniflow.biz.model;

import com.gionee.gnif.model.base.BusinessObject;

public class PersonalRequirementExchange extends BusinessObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7604260974420415052L;
	
	private String account;
    private String name;
    private String department;
    private String company;
    private String procInstId;

    public void setAccount(String account) {
        this.account = account;
    }
    public String getAccount() {
        return account;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    public String getDepartment() {
        return department;
    }
    public void setCompany(String company) {
        this.company = company;
    }
    public String getCompany() {
        return company;
    }
    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }
    public String getProcInstId() {
        return procInstId;
    }

}
