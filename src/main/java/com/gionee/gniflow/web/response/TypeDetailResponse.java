package com.gionee.gniflow.web.response;

import com.gionee.gniflow.biz.model.TypeDetail;
import java.util.Date;


public class TypeDetailResponse {

    private Integer id;
    private String typeName;
    private String name;
    private String description;
    private Integer status;
    private String remark;
	private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;

    public TypeDetailResponse(TypeDetail typeDetail) {
	    setId(typeDetail.getId());
	    setTypeName(typeDetail.getTypeName());
	    setName(typeDetail.getName());
	    setDescription(typeDetail.getDescription());
	    setStatus(typeDetail.getStatus());
	    setRemark(typeDetail.getRemark());
	    setCreateBy(typeDetail.getCreateBy());
	    setCreateTime(typeDetail.getCreateTime());
	    setUpdateBy(typeDetail.getUpdateBy());
	    setUpdateTime(typeDetail.getUpdateTime());
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
    public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    public Date getUpdateTime() {
        return updateTime;
    }

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

}
