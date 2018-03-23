package com.gionee.gniflow.web.response;

import com.gionee.gniflow.biz.model.ProAccountHandleInfo;
import java.util.Date;


public class ProAccountHandleInfoResponse {

    private Integer id;
    private String startUserAccount;
    private String startUserName;
    private String processInstId;
    private String processName;
    private String processDefKey;
    private Date startTime;
    private Date endTime;

    public ProAccountHandleInfoResponse(ProAccountHandleInfo proAccountHandleInfo) {
	    setId(proAccountHandleInfo.getId());
	    setStartUserAccount(proAccountHandleInfo.getStartUserAccount());
	    setStartUserName(proAccountHandleInfo.getStartUserName());
	    setProcessInstId(proAccountHandleInfo.getProcessInstId());
	    setProcessName(proAccountHandleInfo.getProcessName());
	    setProcessDefKey(proAccountHandleInfo.getProcessDefKey());
	    setStartTime(proAccountHandleInfo.getStartTime());
	    setEndTime(proAccountHandleInfo.getEndTime());
	}

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getId() {
        return id;
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
