/**
 * @(#) BpmIEntity.java Created on 2014年10月20日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.bmp;

import org.activiti.engine.impl.persistence.entity.IdentityLinkEntity;

/**
 * The class <code>BpmIEntity</code>
 *
 * @author lipw
 * @version 1.0
 */
public class BpmIdentityLinkEntity extends IdentityLinkEntity{

	private static final long serialVersionUID = 2839579610356699207L;
	
	private String userName;
	//流程实例名称（描述）
	private String processInstanceName;
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getProcessInstanceName() {
		return processInstanceName;
	}

	public void setProcessInstanceName(String processInstanceName) {
		this.processInstanceName = processInstanceName;
	}
	
}
