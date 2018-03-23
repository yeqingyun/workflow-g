package com.gionee.gniflow.biz.model;

import com.gionee.gnif.model.base.BusinessObject;
import java.util.Date;


public class BpmProcessRun extends BusinessObject {
	
	public static final Integer STATUS_RUN = 1;//运行中
	public static final Integer STATUS_COMPLETED = 2;//已完成
	public static final Integer STATUS_SUSPENDED = 3;//已挂起
	public static final Integer STATUS_ACTIVE = 4;//已激活
	public static final Integer STATUS_OBSOLETE = 5;//已作废
	public static final Integer STATUS_NO_APPRVAL_COMPLETED = 6;//审批不通过而结束

	private static final long serialVersionUID = -7034623352166301877L;
	private String processDefKey;
    private String processInstId;
    private String reason;
    private String startUserAccount;
    private String startUserName;
    private Date startTime;
    private Date endTime;
    private String processInstName;
    
    public void setProcessDefKey(String processDefKey) {
        this.processDefKey = processDefKey;
    }
    public String getProcessDefKey() {
        return processDefKey;
    }
    public void setProcessInstId(String processInstId) {
        this.processInstId = processInstId;
    }
    public String getProcessInstId() {
        return processInstId;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    public String getReason() {
        return reason;
    }
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
	public String getProcessInstName() {
		return processInstName;
	}
	public void setProcessInstName(String processInstName) {
		this.processInstName = processInstName;
	}

}
