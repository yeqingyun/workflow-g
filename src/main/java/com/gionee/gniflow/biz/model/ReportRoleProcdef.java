package com.gionee.gniflow.biz.model;

import com.gionee.gnif.model.base.BusinessObject;

public class ReportRoleProcdef extends BusinessObject {

	private static final long serialVersionUID = -2790549780762046036L;
	
	private Integer roleId;
    private String procdefKey;

    public ReportRoleProcdef() {
		super();
	}
	public ReportRoleProcdef(Integer roleId, String procdefKey) {
		super();
		this.roleId = roleId;
		this.procdefKey = procdefKey;
	}
	
    public void setProcdefKey(String procdefKey) {
        this.procdefKey = procdefKey;
    }
    public String getProcdefKey() {
        return procdefKey;
    }
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

}
