package com.gionee.gniflow.biz.model;

import java.io.Serializable;

public class SapAccountRelation implements Serializable {

	private static final long serialVersionUID = -1969682873555487523L;

	private String sapAccount;
    private String hrAccount;
    private char delFlag;

    public void setSapAccount(String sapAccount) {
        this.sapAccount = sapAccount;
    }
    public String getSapAccount() {
        return sapAccount;
    }
    public void setHrAccount(String hrAccount) {
        this.hrAccount = hrAccount;
    }
    public String getHrAccount() {
        return hrAccount;
    }
    public void setDelFlag(char delFlag) {
        this.delFlag = delFlag;
    }
    public char getDelFlag() {
        return delFlag;
    }

}
