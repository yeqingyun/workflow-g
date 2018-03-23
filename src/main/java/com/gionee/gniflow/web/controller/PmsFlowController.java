/**
 * @(#) WorkspaceController.java Created on 2014年5月28日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.web.util.HttpClientUtil;
import com.gionee.gniflow.web.util.PropertyHolder;

/**
 * The class <code>WorkspaceController</code>
 * 
 * @author lipw
 * @version 1.0
 */
@RequestMapping("/pms")
@Controller
public class PmsFlowController {

	/** 获取项目信息 */
	@RequestMapping("/projectTree.json")
	@ResponseBody
	public List<Map<String, Object>> getProjectTree(Integer id) {
//		String result = "[{'id':57681,'pid':null,'text':'信息中心','children':[{'id':3,'pid':57681,'text':'开发管理系统','children':null},{'id':7,'pid':57681,'text':'私有项目','children':null},{'id':10,'pid':57681,'text':'pms','children':null},{'id':20,'pid':57681,'text':'测试','children':null},{'id':21,'pid':57681,'text':'我的项目','children':null}]},{'id':57707,'pid':null,'text':'开发部（信息中心）','children':[{'id':9,'pid':57707,'text':'开发管理系统','children':null}]}]";
		String result = null;
		List<Map<String, Object>> listMap = null;
		try {
			result = HttpClientUtil.sendGetRequest(PropertyHolder.getContextProperty(WebReqConstant.PMS_URL_PROJECT_TREE));
			//System.out.println("result000-->" + result);
			listMap = JSON.parseObject(result, new TypeReference<List<Map<String,Object>>>(){});
		} catch (Exception e) {
			e.printStackTrace();
			listMap = new ArrayList<Map<String, Object>>();
		}
		return listMap;
	}
	
	
}
