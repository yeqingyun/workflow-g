package com.gionee.gniflow.biz.model;

import com.gionee.gnif.model.base.BusinessObject;

import java.util.Date;


public class QuitApplicationInfo extends BusinessObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2272052359184534711L;
	private String name;
    private String account;
    private Date planLeaveDate;
    private String signPerson;
    private Date applicationDate;
    private Integer state;
    private String processid;
    private String timeLimit;
    private String isPreAgreement;
    private String isCompanyShares;

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getAccount() {
        return account;
    }
    public void setPlanLeaveDate(Date planLeaveDate) {
        this.planLeaveDate = planLeaveDate;
    }
    public Date getPlanLeaveDate() {
        return planLeaveDate;
    }
    public void setSignPerson(String signPerson) {
        this.signPerson = signPerson;
    }
    public String getSignPerson() {
        return signPerson;
    }
    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }
    public Date getApplicationDate() {
        return applicationDate;
    }
    public void setState(Integer state) {
        this.state = state;
    }
    public Integer getState() {
        return state;
    }
    public void setProcessid(String processid) {
        this.processid = processid;
    }
    public String getProcessid() {
        return processid;
    }
    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }
    public String getTimeLimit() {
        return timeLimit;
    }
    public void setIsPreAgreement(String isPreAgreement) {
        this.isPreAgreement = isPreAgreement;
    }
    public String getIsPreAgreement() {
        return isPreAgreement;
    }
    public void setIsCompanyShares(String isCompanyShares) {
        this.isCompanyShares = isCompanyShares;
    }
    public String getIsCompanyShares() {
        return isCompanyShares;
    }

}
