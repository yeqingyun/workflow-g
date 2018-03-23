package com.gionee.gniflow.biz.model;

import com.gionee.gnif.model.base.BusinessObject;


public class Engineer extends BusinessObject {

    private String address;
    private String userId;
    private String name;
    private String tel;

    public void setAddress(String address) {
        this.address = address;
    }
    public String getAddress() {
        return address;
    }
    public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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

}
