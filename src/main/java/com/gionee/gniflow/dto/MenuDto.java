/**
 * @(#) MenuDto.java Created on 2014年8月5日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.dto;

import java.io.Serializable;

/**
 * The class <code>MenuDto</code>
 * 
 * @author lipw
 * @version 1.0
 */
public class MenuDto implements Serializable{

	private static final long serialVersionUID = 9150084882969988106L;
	
	private Integer id;
	private Integer pid = 0;
	private Integer type;
	private String name;
	private String code;
	private String url;
	private Integer index;
	private String action;
	private String icon;

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
