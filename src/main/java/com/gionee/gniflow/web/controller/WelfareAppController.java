package com.gionee.gniflow.web.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gionee.gniflow.biz.service.WelfareAppService;
import com.gionee.gniflow.web.easyui.AjaxJson;

@Controller
@RequestMapping("/welfare")
public class WelfareAppController {
	 
	@Autowired
	WelfareAppService  welfareAppService;
	
	/**
	 * 判断配偶是否有发起过流程
	 */
	 @RequestMapping("/weddingIsRepeat.json")
	 @ResponseBody
     public AjaxJson weddingIsRepeat(String account,String number){
		 Map<String, String>map=new HashMap<String, String>();
		 map.put("account", account);
		 map.put("number",number);
		 
		 
		    if(welfareAppService.birthIsRepeat(map)>0){
		    	Date resultDate =welfareAppService.birthIsRepeatToAgain(map);
		    	 Calendar calendar = Calendar.getInstance();
					calendar.setTime(resultDate);
					calendar.add(Calendar.MONTH, 6);
					Date compDate = calendar.getTime();
					Date date=new Date();
				 if(compDate.after(date)){
					 return new AjaxJson(true,"你已经申请过了,请在申请的6个月之后申请");
				  }
		    }
		    if(welfareAppService.weddingIsRepeat(map)>0){
		    	 Date resultDate=welfareAppService.weddingIsRepeatToAgain(map);
		    	 
		    	 Calendar calendar = Calendar.getInstance();
					calendar.setTime(resultDate);
					calendar.add(Calendar.MONTH, 6);
					Date compDate = calendar.getTime();
					Date date=new Date();
				 if(compDate.after(date)){
					 return new AjaxJson(true,"你的配偶已经帮你申请过了,请在申请的6个月之后申请");
				 }
		    }
		 return new AjaxJson(false,"false");
		 
     }
}
