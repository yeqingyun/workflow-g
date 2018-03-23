package com.gionee.gniflow.web.response;

import com.gionee.gniflow.biz.model.BpmConfNodeSend;

public class BpmConfNodeSendResponse {

    private Integer id;
    private Integer sendType;
    private Integer sendMailFlag;
    private Integer intervalDay;
    private Integer confTemplateId;
    private Integer confNodeId;

    public BpmConfNodeSendResponse(BpmConfNodeSend bpmConfNodeSend) {
	    setId(bpmConfNodeSend.getId());
	    setSendType(bpmConfNodeSend.getSendType());
	    setSendMailFlag(bpmConfNodeSend.getSendMailFlag());
	    setIntervalDay(bpmConfNodeSend.getIntervalDay());
	    setConfTemplateId(bpmConfNodeSend.getConfTemplateId());
	    setConfNodeId(bpmConfNodeSend.getConfNodeId());
	}

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getId() {
        return id;
    }
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
