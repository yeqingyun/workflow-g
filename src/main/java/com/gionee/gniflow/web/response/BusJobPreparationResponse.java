package com.gionee.gniflow.web.response;

import com.gionee.gniflow.biz.model.BusJobPreparation;

public class BusJobPreparationResponse {

    private Integer id;
    private Integer orgId;
    private String orgName;
    private String positions;
    private String grade;
    private Integer plait;
    private Integer existNum;
    private Integer rootOrgId;
    private String rootOrgName;
    private String salaryRange;
    private Integer status;
    private String remark;

    public BusJobPreparationResponse(BusJobPreparation busJobPreparation) {
	    setId(busJobPreparation.getId());
	    setOrgId(busJobPreparation.getOrgId());
	    setOrgName(busJobPreparation.getOrgName());
	    setPositions(busJobPreparation.getPositions());
	    setGrade(busJobPreparation.getGrade());
	    setPlait(busJobPreparation.getPlait());
	    setExistNum(busJobPreparation.getExistNum());
	    setRootOrgId(busJobPreparation.getRootOrgId());
	    setRootOrgName(busJobPreparation.getRootOrgName());
	    setSalaryRange(busJobPreparation.getSalaryRange());
	    setStatus(busJobPreparation.getStatus());
	    setRemark(busJobPreparation.getRemark());
	}

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getId() {
        return id;
    }
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
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Integer getStatus() {
        return status;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getRemark() {
        return remark;
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
