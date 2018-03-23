package com.gionee.gniflow.web.response;

import java.util.List;

import com.gionee.auth.model.User;
import com.gionee.gniflow.biz.model.BpmRole;

public class UserResponse {

    private Integer id;
    private String account;
    private String name;
    private Integer orgId;
    private String telephone;
    private String mobile;
    private String email;
    private Integer type;
    private Integer status;
    private String roles = "";
    private String orgName;

    public UserResponse(User user) {
	    setId(user.getId());
	    setAccount(user.getAccount());
	    setName(user.getName());
	    setOrgId(user.getOrgId());
	    setTelephone(user.getTelephone());
	    setMobile(user.getMobile());
	    setEmail(user.getEmail());
	    setType(user.getType());
	    setStatus(user.getStatus());
	    setOrgName(user.getOrgName());
	}

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getId() {
        return id;
    }
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
    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }
    public Integer getOrgId() {
        return orgId;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public String getTelephone() {
        return telephone;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getMobile() {
        return mobile;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    public Integer getType() {
        return type;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Integer getStatus() {
        return status;
    }

	public void setRoleEntities(List<BpmRole> roleEntyties) {
		StringBuilder sb = new StringBuilder();
		for (BpmRole role:roleEntyties) {
			sb.append(",").append(role.getName());
		}
		if (sb.length() > 1) {
			roles = sb.substring(1);
		}
	}

	public String getRoles() {
		return roles;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
}
