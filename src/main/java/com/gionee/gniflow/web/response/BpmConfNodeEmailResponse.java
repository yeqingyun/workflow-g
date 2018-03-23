package com.gionee.gniflow.web.response;

import com.gionee.gniflow.biz.model.BpmConfNodeEmail;

public class BpmConfNodeEmailResponse {

    private Integer id;
    private String processDefKey;
    private String taskDefKey;
    private Integer sendFlag;
    private String addressee;
    private Integer confEmailTemplateId;
    
	private Integer templateId;
	private String name;
	private String subject;
	private String content;

    public BpmConfNodeEmailResponse(BpmConfNodeEmail bpmConfNodeEmail) {
	    setId(bpmConfNodeEmail.getId());
	    setProcessDefKey(bpmConfNodeEmail.getProcessDefKey());
	    setTaskDefKey(bpmConfNodeEmail.getTaskDefKey());
	    setSendFlag(bpmConfNodeEmail.getSendFlag());
	    setAddressee(bpmConfNodeEmail.getAddressee());
	    setConfEmailTemplateId(bpmConfNodeEmail.getConfEmailTemplateId());
	    setTemplateId(bpmConfNodeEmail.getTemplateId());
	    setName(bpmConfNodeEmail.getName());
	    setSubject(bpmConfNodeEmail.getSubject());
	    setContent(bpmConfNodeEmail.getContent());
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
    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }
    public String getTaskDefKey() {
        return taskDefKey;
    }
    public void setSendFlag(Integer sendFlag) {
        this.sendFlag = sendFlag;
    }
    public Integer getSendFlag() {
        return sendFlag;
    }
    public void setAddressee(String addressee) {
        this.addressee = addressee;
    }
    public String getAddressee() {
        return addressee;
    }
    public void setConfEmailTemplateId(Integer confEmailTemplateId) {
        this.confEmailTemplateId = confEmailTemplateId;
    }
    public Integer getConfEmailTemplateId() {
        return confEmailTemplateId;
    }

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
