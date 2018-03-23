/**
 * @(#) BpmTreeDto.java Created on 2015年1月20日
 *
 * Copyright (c) 2015 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gionee.gnif.constant.TreeConstant;

/**
 * The class <code>BpmTreeDto</code>
 * 
 * @author lipw
 * @version 1.0
 */
public class BpmTreeDto implements Serializable{
	
	private static final long serialVersionUID = -4656819803223968315L;
	
	private String id;
	private String pid;
	private String text;
	private String value;
	private String state = TreeConstant.STATE_CLOSE;
	private Boolean checked = false;
	private Map<String, Object> attributes;
	private List<BpmTreeDto> children = new ArrayList<BpmTreeDto>();

	public BpmTreeDto() {

	}

	public BpmTreeDto(String text) {
		id = "0";
		this.text = text;
		state = TreeConstant.STATE_OPEN;
	}

	public BpmTreeDto(String id, String text) {
		this.id = id;
		this.text = text;
		state = TreeConstant.STATE_OPEN;
	}

	public final Map<String, Object> getAttributes() {
		return attributes;
	}

	public final Boolean getChecked() {
		return checked;
	}

	public List<BpmTreeDto> getChildren() {
		return children;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}
	
	public String getState() {
		return state;
	}

	public String getText() {
		return text;
	}

	public final String getValue() {
		return value;
	}

	public final void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public final void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public void setChildren(List<BpmTreeDto> children) {
		this.children = children;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setText(String text) {
		this.text = text;
	}

	public final void setValue(String value) {
		this.value = value;
	}

}
