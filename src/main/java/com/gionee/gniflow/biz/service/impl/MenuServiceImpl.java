/**
 * @(#) MenuServiceImpl.java Created on 2014年8月5日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.biz.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.auth.dao.ResourceDao;
import com.gionee.auth.model.Resource;
import com.gionee.gnif.util.AppContext;
import com.gionee.gniflow.biz.service.MenuService;

/**
 * The class <code>MenuServiceImpl</code>
 *
 * @author lipw
 * @version 1.0
 */
@Service
public class MenuServiceImpl implements MenuService {
	
	@Autowired
	private ResourceDao resourceDao;
	/* (non-Javadoc)
	 * @see com.gionee.gniflow.biz.service.MenuService#getUserResource4LevelOne()
	 */
	@Override
	public List<Resource> getUserResource4LevelOne() {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId", AppContext.getCurrentAppUser().getUserId());
		param.put("pid", 0);
		param.put("types", Arrays.asList(new Integer[]{Resource.TYPE_MENU}));
		return resourceDao.getUserResources(param);
	}
	
	@Override
	public List<Resource> getUserResource4ParnetMenu(Integer menuId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId", AppContext.getCurrentAppUser().getUserId());
		param.put("pid", menuId);
		param.put("types", Arrays.asList(new Integer[]{Resource.TYPE_MENU}));
		return resourceDao.getUserResources(param);
	}

}
