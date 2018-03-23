package com.gionee.gniflow.web.response;

import com.gionee.gnif.model.base.BusinessObject;

public class BpmUserRoleResponse  extends BusinessObject{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer userId;
    private Integer roleId;
    private String account;
    private String userName;
    private Integer orgId;
    private String orgName;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}


}
