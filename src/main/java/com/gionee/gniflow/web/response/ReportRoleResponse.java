package com.gionee.gniflow.web.response;

import com.gionee.gniflow.biz.model.ReportRole;

public class ReportRoleResponse {

    private Integer id;
    private String name;
    private String description;
    private Integer status;
    private String relationDetail;

    public ReportRoleResponse(ReportRole reportRole) {
	    setId(reportRole.getId());
	    setName(reportRole.getName());
	    setDescription(reportRole.getDescription());
	    setStatus(reportRole.getStatus());
	    setRelationDetail(reportRole.getRelationDetail());
	}

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getId() {
        return id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Integer getStatus() {
        return status;
    }

	public String getRelationDetail() {
		return relationDetail;
	}

	public void setRelationDetail(String relationDetail) {
		this.relationDetail = relationDetail;
	}

}
