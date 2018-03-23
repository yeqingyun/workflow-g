/**
 * @(#) UserDto.java Created on 2014年12月2日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.dto;

import java.io.Serializable;

/**
 * The class <code>UserDto</code>
 *
 * @author lipw
 * @version 1.0
 */
public class UserDto implements Serializable{
	
	private static final long serialVersionUID = 5606710845949390632L;

	private Integer id;
    
	private String account;
    
	private String name;
    
	private Integer orgId;
    
	private String orgName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
