package com.gionee.gniflow.web.response;

import com.gionee.gniflow.biz.model.MLeaveApplicationInfo;
import java.util.Date;


public class MLeaveApplicationInfoResponse {

    private Integer id;
    private String proInstId;
    private String leaveAccount;
    private String leaveName;
    private Date leaveDate;
    private String operationReason;
    private Date applyDate;
    private Integer processState;
    private Integer status;
    private String remark;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;

    public MLeaveApplicationInfoResponse(MLeaveApplicationInfo mLeaveApplicationInfo) {
	    setId(mLeaveApplicationInfo.getId());
	    setProInstId(mLeaveApplicationInfo.getProInstId());
	    setLeaveAccount(mLeaveApplicationInfo.getLeaveAccount());
	    setLeaveName(mLeaveApplicationInfo.getLeaveName());
	    setLeaveDate(mLeaveApplicationInfo.getLeaveDate());
	    setOperationReason(mLeaveApplicationInfo.getOperationReason());
	    setApplyDate(mLeaveApplicationInfo.getApplyDate());
	    setProcessState(mLeaveApplicationInfo.getProcessState());
	    setStatus(mLeaveApplicationInfo.getStatus());
	    setRemark(mLeaveApplicationInfo.getRemark());
	    setCreateBy(mLeaveApplicationInfo.getCreateBy());
	    setCreateTime(mLeaveApplicationInfo.getCreateTime());
	    setUpdateBy(mLeaveApplicationInfo.getUpdateBy());
	    setUpdateTime(mLeaveApplicationInfo.getUpdateTime());
	}

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getId() {
        return id;
    }
    public void setProInstId(String proInstId) {
        this.proInstId = proInstId;
    }
    public String getProInstId() {
        return proInstId;
    }
    public void setLeaveAccount(String leaveAccount) {
        this.leaveAccount = leaveAccount;
    }
    public String getLeaveAccount() {
        return leaveAccount;
    }
    public void setLeaveName(String leaveName) {
        this.leaveName = leaveName;
    }
    public String getLeaveName() {
        return leaveName;
    }
    public void setLeaveDate(Date leaveDate) {
        this.leaveDate = leaveDate;
    }
    public Date getLeaveDate() {
        return leaveDate;
    }
    public void setOperationReason(String operationReason) {
        this.operationReason = operationReason;
    }
    public String getOperationReason() {
        return operationReason;
    }
    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }
    public Date getApplyDate() {
        return applyDate;
    }
    public void setProcessState(Integer processState) {
        this.processState = processState;
    }
    public Integer getProcessState() {
        return processState;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Integer getStatus() {
        return status;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getRemark() {
        return remark;
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
