/**
 * @(#) ProAccountHandleInfoController.java Created on 2014年5月28日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.controller;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.model.ProAccountHandleInfo;
import com.gionee.gniflow.biz.service.ProAccountHandleInfoService;
import com.gionee.gniflow.web.easyui.AjaxJson;
/**
 * The class <code>ProAccountHandleInfoController</code>
 * 
 * @author lipw
 * @version 1.0
 */
@RequestMapping("/proAccountHandle")
@Controller
public class ProAccountHandleInfoController {
	
	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(ProAccountHandleInfoController.class);

	@Autowired
	private ProAccountHandleInfoService proAccountHandleInfoService;

	
	/** 判断用户是否申请过户口办理流程 */
    @RequestMapping("/isCanAccountHandle.json")
    @ResponseBody
    public AjaxJson isCanAccountHandle(@RequestParam("account") String userAccount) {
    	QueryMap queryMap = new QueryMap();
    	queryMap.put("startUserAccount", userAccount);
    	queryMap.put("status", ProAccountHandleInfo.STATUS_NORMAL);
    	try {
    		List<ProAccountHandleInfo> accountHandleInfos = proAccountHandleInfoService.query(queryMap);
        	if (CollectionUtils.isEmpty(accountHandleInfos)) {
        		return new AjaxJson(true, "未办理过该流程！");
        	}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
        return new AjaxJson(false, "您已成功申请过户口办理，请不要重复申请!");
    }
    
}
