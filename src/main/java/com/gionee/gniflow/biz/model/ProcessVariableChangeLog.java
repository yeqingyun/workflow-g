package com.gionee.gniflow.biz.model;

import com.gionee.gnif.model.base.BusinessObject;

public class ProcessVariableChangeLog extends BusinessObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String procInstId;
    private String name;
    private Integer rev;
    private String text;
    private String text2;
    private String procInstKey;
    private String procInstName;

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }
    public String getProcInstId() {
        return procInstId;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setRev(Integer rev) {
        this.rev = rev;
    }
    public Integer getRev() {
        return rev;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }
    public void setText2(String text2) {
        this.text2 = text2;
    }
    public String getText2() {
        return text2;
    }
    public void setProcInstKey(String procInstKey) {
        this.procInstKey = procInstKey;
    }
    public String getProcInstKey() {
        return procInstKey;
    }
    public void setProcInstName(String procInstName) {
        this.procInstName = procInstName;
    }
    public String getProcInstName() {
        return procInstName;
    }

}
