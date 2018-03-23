package com.gionee.gniflow.web.response;

import com.gionee.gniflow.biz.model.ReportUserRole;

public class ReportUserRoleResponse {

    private Integer id;
    private Integer userId;
    private Integer roleId;
    private Integer status;

    public ReportUserRoleResponse(ReportUserRole reportUserRole) {
	    setId(reportUserRole.getId());
	    setUserId(reportUserRole.getUserId());
	    setRoleId(reportUserRole.getRoleId());
	    setStatus(reportUserRole.getStatus());
	}

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getId() {
        return id;
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
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Integer getStatus() {
        return status;
    }

}
