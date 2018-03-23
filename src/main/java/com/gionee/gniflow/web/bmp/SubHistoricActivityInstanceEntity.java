/**
 * @(#) BpmTaskEntity.java Created on 2015年6月2日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.bmp;

import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntity;

/**
 * The class <code>BpmTaskEntity</code>
 *
 * @author caojj
 * @version 1.0
 */
public class SubHistoricActivityInstanceEntity extends HistoricActivityInstanceEntity {

	private static final long serialVersionUID = -6698945798964929867L;

	//流程实例名称
	private String processInstanceName;

	public String getProcessInstanceName() {
		return processInstanceName;
	}

	public void setProcessInstanceName(String processInstanceName) {
		this.processInstanceName = processInstanceName;
	}
	
}
