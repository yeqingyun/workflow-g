package com.gionee.gniflow.web.response;

import com.gionee.gniflow.biz.model.BpmEmailTemplate;

public class BpmEmailTemplateResponse {

    private Integer id;
    private String name;
    private String subject;
    private String content;

    public BpmEmailTemplateResponse(BpmEmailTemplate bpmEmailTemplate) {
	    setId(bpmEmailTemplate.getId());
	    setName(bpmEmailTemplate.getName());
	    setSubject(bpmEmailTemplate.getSubject());
	    setContent(bpmEmailTemplate.getContent());
	}

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getId() {
        return id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getSubject() {
        return subject;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }

}
