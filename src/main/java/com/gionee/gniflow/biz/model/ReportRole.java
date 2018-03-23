package com.gionee.gniflow.biz.model;

import com.gionee.gnif.model.base.BusinessObject;

public class ReportRole extends BusinessObject {

	private static final long serialVersionUID = -5476061254030563734L;
	
	private String name;
    private String description;
    //显示BPM_ROLE_PROCDEF关系表
    private String relationDetail;

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
	public String getRelationDetail() {
		return relationDetail;
	}
	public void setRelationDetail(String relationDetail) {
		this.relationDetail = relationDetail;
	}

}
