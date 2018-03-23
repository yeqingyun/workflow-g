package com.gionee.gniflow.biz.model;

import com.gionee.gnif.model.base.BusinessObject;

public class BpmConfProcessRole extends BusinessObject {

	private static final long serialVersionUID = -4340659129593114043L;

	private String roleName;
	private String processDefKey;
	private String assignee;
	private String area;
	private String details;
	private String method;

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
