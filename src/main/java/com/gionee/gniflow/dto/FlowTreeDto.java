/**
 * @(#) FlowTreeDto.java Created on 2014年8月11日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.dto;

import com.gionee.gnif.dto.TreeDto;

/**
 * The class <code>FlowTreeDto</code>
 *
 * @author lipw
 * @version 1.0
 */
public class FlowTreeDto extends TreeDto{
	
	private static final long serialVersionUID = -6757796970452412901L;

	private String iconCls;

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

}
