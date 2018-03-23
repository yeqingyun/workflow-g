package com.gionee.gniflow.biz.model;

import com.gionee.gnif.model.base.BusinessObject;

import java.util.Date;


public class ProAccountHandleInfo extends BusinessObject {
	
	private static final long serialVersionUID = -5375926779711467652L;
	
	public final static Integer STATUS_HANDLEING = 1;
	public final static Integer STATUS_HANDLED = 2;
	
    private String startUserAccount;
    private String startUserName;
    private String processInstId;
    private String processName;
    private String processDefKey;
    private Date startTime;
    private Date endTime;

    public void setStartUserAccount(String startUserAccount) {
        this.startUserAccount = startUserAccount;
    }
    public String getStartUserAccount() {
        return startUserAccount;
    }
    public void setStartUserName(String startUserName) {
        this.startUserName = startUserName;
    }
    public String getStartUserName() {
        return startUserName;
    }
    public void setProcessInstId(String processInstId) {
        this.processInstId = processInstId;
    }
    public String getProcessInstId() {
        return processInstId;
    }
    public void setProcessName(String processName) {
        this.processName = processName;
    }
    public String getProcessName() {
        return processName;
    }
    public void setProcessDefKey(String processDefKey) {
        this.processDefKey = processDefKey;
    }
    public String getProcessDefKey() {
        return processDefKey;
    }
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    public Date getStartTime() {
        return startTime;
    }
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    public Date getEndTime() {
        return endTime;
    }

}
