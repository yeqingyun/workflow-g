package com.gionee.gniflow.web.response;

import com.gionee.gniflow.biz.model.Engineer;
import java.util.Date;


public class EngineerResponse {

    private Integer id;
    private String address;
    private String userId;
    private String name;
    private String tel;
    private Date createtime;
    private String createby;
    private Date updatetime;
    private String updateby;
    private Integer status;

    public EngineerResponse(Engineer engineer) {
	    setId(engineer.getId());
	    setAddress(engineer.getAddress());
	    setUserId(engineer.getUserId());
	    setName(engineer.getName());
	    setTel(engineer.getTel());
	    setCreatetime(engineer.getCreateTime());
	    setCreateby(engineer.getCreateBy());
	    setUpdatetime(engineer.getUpdateTime());
	    setUpdateby(engineer.getUpdateBy());
	    setStatus(engineer.getStatus());
	}

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getId() {
        return id;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getAddress() {
        return address;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserId() {
        return userId;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
    public Date getCreatetime() {
        return createtime;
    }
    public void setCreateby(String createby) {
        this.createby = createby;
    }
    public String getCreateby() {
        return createby;
    }
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
    public Date getUpdatetime() {
        return updatetime;
    }
    public void setUpdateby(String updateby) {
        this.updateby = updateby;
    }
    public String getUpdateby() {
        return updateby;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Integer getStatus() {
        return status;
    }

}
