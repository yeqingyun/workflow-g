/**
 * @(#) HisVariable.java Created on 2015年1月29日
 *
 * Copyright (c) 2015 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.bmp;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Attachment;

import com.gionee.gniflow.dto.MapFormElement;

/**
 * The class <code>HisVariable</code>
 *
 * @author lipw
 * @version 1.0
 */
public class ProcessHisVariable implements Serializable {

	private static final long serialVersionUID = 4373062235397639796L;
	
	private Map<String,Object> vars;
	
	private Map<String, List<List<MapFormElement>>> mapSers;
	
	private List<Attachment> attachments;

	public Map<String, Object> getVars() {
		return vars;
	}

	public void setVars(Map<String, Object> vars) {
		this.vars = vars;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	public Map<String, List<List<MapFormElement>>> getMapSers() {
		return mapSers;
	}

	public void setMapSers(Map<String, List<List<MapFormElement>>> mapSers) {
		this.mapSers = mapSers;
	}
	
}
