package com.gionee.gniflow.biz.model;

import com.gionee.gnif.model.base.BusinessObject;

public class BpmEmailTemplate extends BusinessObject {

	private static final long serialVersionUID = 3628200173490221673L;
	
	private String name;
    private String subject;
    private String content;

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
