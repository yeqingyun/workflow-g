package com.gionee.gniflow.web.response;

import java.util.List;

import com.gionee.auth.model.Organization;
import com.gionee.auth.model.User;

public class OrganizationResponse {

    private String id;
    private String pid;
    private String name;
    private String fullName;
    private String address;
    private String telephone;
    private String state;
    
    private List<OrganizationResponse> children;

	public OrganizationResponse(Organization organization) {
	    setId(organization.getId().toString());
	    setPid(organization.getPid().toString());
	    setName(organization.getName());
	    setFullName(organization.getFullName());
	    setAddress(organization.getAddress());
	    setTelephone(organization.getTelephone());
	}
	
	public OrganizationResponse(User user) {
	    setId(user.getAccount());
	    setName(user.getName());
	}

    public OrganizationResponse() {
    	setId("0");
    	setName("系统组织架构");
    	setState("open");
	}

	public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }
    public void setPid(String pid) {
        this.pid = pid;
    }
    public String getPid() {
        return pid;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getFullName() {
        return fullName;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getAddress() {
        return address;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public String getTelephone() {
        return telephone;
    }

	public void setChildren(List<OrganizationResponse> children) {
		this.children = children;
	}
	
    public List<OrganizationResponse> getChildren() {
		return children;
	}
    
    public String getText() {
    	return getName();
    }
    
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
