/**
 * @(#) PPMMFlowServiceImpl.java Created on 2014年6月25日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.biz.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gionee.gniflow.biz.service.PPMMFlowService;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.web.util.HttpClientUtil;
import com.gionee.gniflow.web.util.PropertyHolder;

/**
 * The class <code>PPMMFlowServiceImpl</code>
 *
 * @author lipw
 * @version 1.0
 */
@Service(value="ppmmFlowService")
public class PPMMFlowServiceImpl implements PPMMFlowService {
	//样机管理员
	private static final String PROTOTYPE_ADMIN_KEY = "PROTOTYPE_ADMINISTRATOR";
	//财务管理员
	@SuppressWarnings("unused")
	private static final String FINANCE_ADMIN_KEY = "FINANCE_MANAGER";
	
	@Override
	public List<String> getPrototypeAdmins() {
//		List<String> users = new ArrayList<String>();
//		String reqUrl = PropertyHolder.getContextProperty(WebReqConstant.PPMM_URL_GET_ADMINS);
//		Map<String,String> reqData = new HashMap<String,String>();
//		reqData.put("type", PROTOTYPE_ADMIN_KEY);
//		String result = HttpClientUtil.sendPostRequest(reqUrl, reqData, "UTF-8");
//		try {
//			JSONObject json = JSON.parseObject(result);
//			users = JSONArray.parseArray(json.get("stringList").toString(),  String.class);
//		} catch (Exception e){
//			e.printStackTrace();
//			users = null;
//		}
		
		
		List<String> users = new ArrayList<String>();
		users.add("jiangzl");
		users.add("ali");
		//System.out.println("PPMM-users---->" + users);
		return users;
	}

	@Override
	public List<String> getFinanceAdmins() {
		return null;
	}

}
