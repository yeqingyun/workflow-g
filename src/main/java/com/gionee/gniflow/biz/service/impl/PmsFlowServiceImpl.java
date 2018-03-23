/**
 * @(#) FlowServiceImpl.java Created on 2014年6月10日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.biz.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gionee.auth.GroupService;
import com.gionee.auth.PrivilegeService;
import com.gionee.auth.UserService;
import com.gionee.gniflow.biz.service.PmsFlowService;
import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.integration.dao.GnifUserDao;
import com.gionee.gniflow.web.util.HttpClientUtil;
import com.gionee.gniflow.web.util.PropertyHolder;

/**
 * The class <code>FlowServiceImpl</code>
 *
 * @author lipw
 * @version 1.0
 */
@Service(value="pmsFlowService")
public class PmsFlowServiceImpl implements PmsFlowService {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PrivilegeService privilegeService;
	
	@Autowired
	private GroupService groupServcie;
	
	@Autowired
	private GnifUserDao userDao;
	
	@Autowired
	private ProcessHelpService processHelpService;
	
	/* (non-Javadoc)
	 * @see com.gionee.gniflow.biz.service.FlowService#getDeptLeader(java.lang.String)
	 */
	@Override
	public String getDeptLeader(Object userAccount) {
		return processHelpService.getOrgLeader((String)userAccount,"1");
	}

	/* (non-Javadoc)
	 * @see com.gionee.gniflow.biz.service.FlowService#getProjectCharge(java.lang.String)
	 */
	@Override
	public String getProjectCharge(String projectName) {
		String reqUrl = PropertyHolder.getContextProperty(WebReqConstant.PMS_URL_PROJECT_LEADER);
		Map<String,String> reqData = new HashMap<String,String>();
		reqData.put("id", projectName);
		String result = HttpClientUtil.sendPostRequest(reqUrl, reqData, "UTF-8");
		try {
			JSONObject json = JSON.parseObject(result);
			result = (String)json.get("account");
		} catch (Exception e){
			e.printStackTrace();
			result = null;
		}
		return result;
	}

}
