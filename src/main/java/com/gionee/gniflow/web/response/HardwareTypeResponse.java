package com.gionee.gniflow.web.response;

import com.gionee.gniflow.biz.model.HardwareType;
import java.util.Date;


public class HardwareTypeResponse {

    private Integer id;
    private String typeName;
    private String description;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
    private Integer status;
    private String remark;

    public HardwareTypeResponse(HardwareType hardwareType) {
	    setId(hardwareType.getId());
	    setTypeName(hardwareType.getTypeName());
	    setDescription(hardwareType.getDescription());
	    setCreateBy(hardwareType.getCreateBy());
	    setCreateTime(hardwareType.getCreateTime());
	    setUpdateBy(hardwareType.getUpdateBy());
	    setUpdateTime(hardwareType.getUpdateTime());
	    setStatus(hardwareType.getStatus());
	    setRemark(hardwareType.getRemark());
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

	public void setId(Integer id) {
        this.id = id;
    }
    public Integer getId() {
        return id;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    public String getTypeName() {
        return typeName;
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

}
