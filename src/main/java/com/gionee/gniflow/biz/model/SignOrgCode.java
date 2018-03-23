package com.gionee.gniflow.biz.model;

import com.gionee.gnif.model.base.BusinessObject;

public class SignOrgCode extends BusinessObject {
	
	private static final long serialVersionUID = -9737589167122257L;
	
	private Integer orgId;
    private String orgCode;

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }
    public Integer getOrgId() {
        return orgId;
    }
    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }
    public String getOrgCode() {
        return orgCode;
    }

}
