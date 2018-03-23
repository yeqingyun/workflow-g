package com.gionee.gniflow.biz.model;

import com.gionee.gnif.model.base.BusinessObject;

public class BpmUserRole extends BusinessObject {

	private static final long serialVersionUID = 4441033167944089291L;
	
	private Integer userId;
    private Integer roleId;
    
    public BpmUserRole() {
		super();
	}
    
    public BpmUserRole(Integer userId, Integer roleId) {
		super();
		this.userId = userId;
		this.roleId = roleId;
	}
	
	public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
    public Integer getRoleId() {
        return roleId;
    }

}
