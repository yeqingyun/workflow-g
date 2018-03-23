package com.gionee.gniflow.web.response;

import com.gionee.gniflow.biz.model.BpmConfProcessRole;

public class BpmConfProcessRoleResponse {

    private Integer id;
    private String roleName;
    private String processDefKey;
    private String assignee;
    private String area;
    private String details;
    private String method;

    public BpmConfProcessRoleResponse(BpmConfProcessRole bpmConfProcessRole) {
	    setId(bpmConfProcessRole.getId());
	    setRoleName(bpmConfProcessRole.getRoleName());
	    setProcessDefKey(bpmConfProcessRole.getProcessDefKey());
	    setAssignee(bpmConfProcessRole.getAssignee());
	    setArea(bpmConfProcessRole.getArea());
	    setDetails(bpmConfProcessRole.getDetails());
	    setMethod(bpmConfProcessRole.getMethod());
	}

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getId() {
        return id;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    public String getRoleName() {
        return roleName;
    }
    public void setProcessDefKey(String processDefKey) {
        this.processDefKey = processDefKey;
    }
    public String getProcessDefKey() {
        return processDefKey;
    }
    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }
    public String getAssignee() {
        return assignee;
    }
    public void setArea(String area) {
        this.area = area;
    }
    public String getArea() {
        return area;
    }

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
    

}
