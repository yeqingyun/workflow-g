package com.gionee.gniflow.web.controller;


import java.util.Date;
import java.util.List;

import org.restlet.engine.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.model.MLeaveApplicationInfo;
import com.gionee.gniflow.biz.service.MLeaveApplicationInfoService;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.web.easyui.AjaxJson;

@RequestMapping("/leaveInfo")
@Controller
public class MLeaveApplicationInfoController {

	@Autowired
	private MLeaveApplicationInfoService mleaveApplicationInfoService;
	
	
	/** 查找申请离职时间**/
	@RequestMapping("/leaveRunningProcess.json")
	@ResponseBody
	public AjaxJson getApplicationInfo(String account){
		
		MLeaveApplicationInfo leaveInfo = null;
		AjaxJson ajaxJson = null;
		QueryMap query = new QueryMap();
		query.put("leaveAccount", account);
		List<MLeaveApplicationInfo> list = null;
		list=mleaveApplicationInfoService.query(query);
		if(!CollectionUtils.isEmpty(list)){
			leaveInfo=list.get(0);
			if(1==leaveInfo.getProcessState()){
				ajaxJson=new AjaxJson(false, "您的离职申请流程正在审批中，请勿重复发起！");
				 return ajaxJson;
			}
			Date leaveDate =null; 
			leaveDate = leaveInfo.getLeaveDate();
			Date now = new Date();
			if(leaveDate.compareTo(now)>0){//离职日期大于当前日期就不能申请
				ajaxJson=new AjaxJson(false, "您的离职申请流程已完成，未到离职日期，请勿重复发起！");
				 return ajaxJson;
			}	 
			 
		}
		ajaxJson=new AjaxJson(true, "");
		return ajaxJson;
	}
}
