package com.gionee.gniflow.biz.model;

import com.gionee.gnif.model.base.BusinessObject;

public class BpmConfNodeSend extends BusinessObject {

	private static final long serialVersionUID = 5827510384588267466L;
	
	private Integer sendType;
    private Integer sendMailFlag;
    private Integer intervalDay;
    private Integer confTemplateId;
    private Integer confNodeId;

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }
    public Integer getSendType() {
        return sendType;
    }
    public void setSendMailFlag(Integer sendMailFlag) {
        this.sendMailFlag = sendMailFlag;
    }
    public Integer getSendMailFlag() {
        return sendMailFlag;
    }
    public void setIntervalDay(Integer intervalDay) {
        this.intervalDay = intervalDay;
    }
    public Integer getIntervalDay() {
        return intervalDay;
    }
    public void setConfTemplateId(Integer confTemplateId) {
        this.confTemplateId = confTemplateId;
    }
    public Integer getConfTemplateId() {
        return confTemplateId;
    }
    public void setConfNodeId(Integer confNodeId) {
        this.confNodeId = confNodeId;
    }
    public Integer getConfNodeId() {
        return confNodeId;
    }

}
