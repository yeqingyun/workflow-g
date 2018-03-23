package com.gionee.gniflow.biz.model;

import com.gionee.gnif.model.base.BusinessObject;

public class BusJobPreparation extends BusinessObject {

	private static final long serialVersionUID = 3466142742157583158L;

	private Integer orgId;
	private String orgName;
	private String positions;
	private String grade;
	private Integer plait;
	private Integer existNum;
	private Integer rootOrgId;
	private String rootOrgName;
	private String salaryRange;

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setPositions(String positions) {
		this.positions = positions;
	}

	public String getPositions() {
		return positions;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getGrade() {
		return grade;
	}

	public void setPlait(Integer plait) {
		this.plait = plait;
	}

	public Integer getPlait() {
		return plait;
	}

	public void setExistNum(Integer existNum) {
		this.existNum = existNum;
	}

	public Integer getExistNum() {
		return existNum;
	}

	public Integer getRootOrgId() {
		return rootOrgId;
	}

	public void setRootOrgId(Integer rootOrgId) {
		this.rootOrgId = rootOrgId;
	}

	public String getRootOrgName() {
		return rootOrgName;
	}

	public void setRootOrgName(String rootOrgName) {
		this.rootOrgName = rootOrgName;
	}

	public String getSalaryRange() {
		return salaryRange;
	}

	public void setSalaryRange(String salaryRange) {
		this.salaryRange = salaryRange;
	}

}
