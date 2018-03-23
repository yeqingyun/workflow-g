package com.gionee.gniflow.biz.model;

import com.gionee.gnif.model.base.BusinessObject;
import java.util.Date;


public class MLeaveApplicationInfo extends BusinessObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3929736783100070554L;
	
	/**
	 * 流程实例编号
	 */
	private String proInstId;
	/**
	 * 离职人员编号
	 */
    private String leaveAccount;
    /**
     * 离职人员姓名
     */
    private String leaveName;
    /**
     * 离职日期
     */
    private Date leaveDate;
    /**
     * 离职操作原因
     */
    private String operationReason;
    /**
     * 申请日期
     */
    private Date applyDate;
    /**
     * 流程状态
     */
    private Integer processState;
    
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

}
