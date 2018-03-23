package com.gionee.gniflow.web.response;

import com.gionee.gniflow.biz.model.BpmRoleProcdef;

public class BpmRoleProcdefResponse {

    private Integer id;
    private Integer roleId;
    private String procdefKey;
    private Integer status;

    public BpmRoleProcdefResponse(BpmRoleProcdef bpmRoleProcdef) {
	    setId(bpmRoleProcdef.getId());
	    setRoleId(bpmRoleProcdef.getRoleId());
	    setProcdefKey(bpmRoleProcdef.getProcdefKey());
	    setStatus(bpmRoleProcdef.getStatus());
	}

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getId() {
        return id;
    }

    public void setProcdefKey(String procdefKey) {
        this.procdefKey = procdefKey;
    }
    public String getProcdefKey() {
        return procdefKey;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Integer getStatus() {
        return status;
    }

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

}
