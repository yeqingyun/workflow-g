/**
 * @(#) MenuService.java Created on 2014年8月5日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.biz.service;

import java.util.List;

import com.gionee.auth.model.Resource;

/**
 * The class <code>MenuService</code>
 *
 * @author lipw
 * @version 1.0
 */
public interface MenuService {
	
	List<Resource> getUserResource4LevelOne();
	
	List<Resource> getUserResource4ParnetMenu(Integer menuId);
}
