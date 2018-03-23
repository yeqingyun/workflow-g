/**
 * @(#) BasicInfoPrinter.java Created on 2014年6月9日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.bmp;

import com.gionee.gnif.util.AppContext;

/**
 * The class <code>BasicInfoPrinter</code>
 *
 * @author lipw
 * @version 1.0
 */
public class BasicInfoPrinter {
	
	/**
	 * 获取用户的名字
	 * @return
	 */
	public String getUserName(){
		return AppContext.getCurrentAppUser().getName();
	}
	
	/**
	 * 获取用户组织的名字
	 * @return
	 */
	public String getUserOrgName(){
		return AppContext.getCurrentAppUser().getOrgName();
	}
}
