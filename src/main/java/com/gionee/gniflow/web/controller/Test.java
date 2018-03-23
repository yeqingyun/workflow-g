package com.gionee.gniflow.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gionee.gnif.GnifException;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.web.util.HttpClientUtil;
import com.gionee.gniflow.web.util.PropertyHolder;

@RequestMapping("/test")
@Controller
public class Test {

    @RequestMapping("/work")
    @ResponseBody
    public String test(@RequestParam(value="account")String userAccount){
    	StringBuilder sb = new StringBuilder();
		String reqUrl = PropertyHolder.getContextProperty(WebReqConstant.HR_ACCOUNT_IS_ONWORK);
		sb.append("url:").append(reqUrl).append("<br>");
		Map<String, String> reqData = new HashMap<String, String>();
		reqData.put("empId", userAccount);
		sb.append("empId:").append(userAccount).append("<br>");
		String result = HttpClientUtil.sendPostRequest(reqUrl, reqData, "UTF-8");
		sb.append("result:").append(result).append("<br>");
		sb.append("y/n:").append(result.trim().equalsIgnoreCase("Y")).append("<br>");
    	return sb.toString();
    }
}
