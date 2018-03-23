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
import com.gionee.gniflow.web.easyui.AjaxJson;
import com.gionee.gniflow.web.util.HttpClientUtil;
import com.gionee.gniflow.web.util.PropertyHolder;

@RequestMapping("/trialPositive")
@Controller
public class TrialPositiveController {
	
	@RequestMapping("/judageIsPositive.json")
	@ResponseBody
	public AjaxJson juadgeIsPositive(@RequestParam("userAccount") String userAccount){
		AjaxJson ajaxJosn = null;
		try{
		String reqUrl = PropertyHolder.getContextProperty(WebReqConstant.HR_ACOUNT_IS_REGULAREMPLOYEE);

		Map<String, String> reqData = new HashMap<String, String>();
		reqData.put("usr.login", userAccount);
		
		String result = HttpClientUtil.sendPostRequest(reqUrl, reqData, "UTF-8");
		ajaxJosn=new AjaxJson();
		if (StringUtils.isNotEmpty(result)) {
			if(result.trim().equalsIgnoreCase("Y")){
				ajaxJosn.setSuccess(true);
				ajaxJosn.setMsg("该员工已属于正式员工，不能发起转正流程！");
			}else{
				ajaxJosn.setSuccess(false);
			}
		} else {
			ajaxJosn.setSuccess(false);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return ajaxJosn;
	}
}
