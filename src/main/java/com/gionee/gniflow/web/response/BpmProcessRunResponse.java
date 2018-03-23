package com.gionee.gniflow.web.response;

import com.gionee.gniflow.biz.model.BpmProcessRun;
import java.util.Date;


public class BpmProcessRunResponse {

    private Integer id;
    private String processDefKey;
    private String processInstId;
    private String reason;
    private String startUserAccount;
    private String startUserName;
    private Date startTime;
    private Date endTime;
    private Integer status;

    public BpmProcessRunResponse(BpmProcessRun bpmProcessRun) {
	    setId(bpmProcessRun.getId());
	    setProcessDefKey(bpmProcessRun.getProcessDefKey());
	    setProcessInstId(bpmProcessRun.getProcessInstId());
	    setReason(bpmProcessRun.getReason());
	    setStartUserAccount(bpmProcessRun.getStartUserAccount());
	    setStartUserName(bpmProcessRun.getStartUserName());
	    setStartTime(bpmProcessRun.getStartTime());
	    setEndTime(bpmProcessRun.getEndTime());
	    setStatus(bpmProcessRun.getStatus());
	}

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getId() {
        return id;
    }
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
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Integer getStatus() {
        return status;
    }

}
