package com.gionee.gniflow.biz.model;

public class Candidate {
	
	private Integer id;
	
	private String procDefId;//流程定义id
	
	private String type;//类型
	
	private String empIds;//候选人id集，中间以,间隔

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProcDefId() {
		return procDefId;
	}

	public void setProcDefId(String procDefId) {
		this.procDefId = procDefId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEmpIds() {
		return empIds;
	}

	public void setEmpIds(String empIds) {
		this.empIds = empIds;
	}
	
	
}
