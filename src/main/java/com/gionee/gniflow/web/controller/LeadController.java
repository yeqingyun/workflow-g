package com.gionee.gniflow.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.web.easyui.AjaxJson;

@Controller
@RequestMapping("/lead")
public class LeadController {
	 @Autowired
     ProcessHelpService processHelpService;
	 //查询是否是领导 
	 @RequestMapping("/queryLead.json") 
	 @ResponseBody
	 public  AjaxJson queryLead(String account){
		 AjaxJson ajaxJson=null;
		 boolean flag=processHelpService.isMiddleManager(account);
		 String leader=processHelpService.getOrgLeader(account,"4");
		 String manager=processHelpService.getOrgLeader(account,"1");
		 String deptDirector=processHelpService.getOrgLeader(account,"2");
		if(flag){
			ajaxJson=new AjaxJson(true,"");
		}else if(deptDirector==null||"".equals(deptDirector)){
			ajaxJson=new AjaxJson(true,"");
		}else if((leader==null||"".equals(leader)&&(manager==null||"".equals(manager)))){
			ajaxJson=new AjaxJson(true,"");
		}else{
			ajaxJson=new AjaxJson(false,"");
		}
		 
		/*String result= processHelpService.getOrgLeaderByEmpId(account,"1");
		 if(StringUtils.isEmpty(result)){
			 result= processHelpService.getOrgLeader(account,"2");
			 ajaxJson=new AjaxJson(false,result);
		 }else if(result.equals(account)){
			 result= processHelpService.getOrgLeader(account,"2");
			 ajaxJson=new AjaxJson(false,result);
		 }else{
			 ajaxJson=new AjaxJson(true,result);
		 }*/
		 return ajaxJson;
	 }
	 
	 @RequestMapping("/queryDirectLead")
	 public AjaxJson queryDirectLead(String account){
		 AjaxJson ajaxJson = new AjaxJson(false,"查询失败");
		 String result = processHelpService.findOrgLeaderForSpec(account,"4");
		 if(StringUtils.isEmpty(result)){
			 result = processHelpService.findOrgLeaderForSpec(account,"1");
			 if(StringUtils.isEmpty(result)){
				 return ajaxJson;
			 }
			 ajaxJson.setSuccess(true);
			 ajaxJson.setMsg(result);
		 }else{
			 ajaxJson.setSuccess(true);
			 ajaxJson.setMsg(result);
		 }
		 return ajaxJson;
	 }
	 
	    //根据职员账号判断是否是经理级还是总监   3主管副总裁   2代表总监  1代表经理  0代表职员
		@RequestMapping("/checkIsManager.json")
		@ResponseBody
		public AjaxJson checkIsManager(String account){
			 AjaxJson ajaxJson=null;
			 
			 //如果是账号登陆 就去查找他的员工编号
			 String userAccount= processHelpService.loadEmpByUsrAccount(account);
			 //if(StringUtils.isNotEmpty(userAccount)){
				// account=userAccount;
			 //}
			String assigned=processHelpService.getOrgLeaderByEmpId(userAccount,"2");
			 if(account.equals(assigned)){
				 return ajaxJson=new AjaxJson (true,"2");
			 }
			String deputygeneral=processHelpService.getOrgLeaderByEmpId(userAccount,"3");
			 if(account.equals(deputygeneral)){
				 return ajaxJson=new AjaxJson (true,"3");
			 }
			 boolean bool= processHelpService.isMiddleManager(account);
			 if(bool){
				 return ajaxJson=new AjaxJson (true,"1");
			 }else{
				 return ajaxJson=new AjaxJson (false,"0");
			 }
		}
		//根据职员账号判断是否是经理级还是总监    2代表总监  1代表经理  0代表职员
		@RequestMapping("/isMajordomo.json")
		@ResponseBody
		public AjaxJson isMajordomo(String account){
			 AjaxJson ajaxJson=null;
			String   assigned=processHelpService.getOrgLeader(account,"3");
			if(StringUtils.isEmpty(assigned)){
				ajaxJson=new AjaxJson(true);
				return ajaxJson;
			}
			return new AjaxJson(false);
		}
      //根据职员账号判断是否是人力资源部
		@RequestMapping("/isHrDepa.json")
		@ResponseBody
		public AjaxJson isHrDepa(String account){
			 AjaxJson ajaxJson=null;
			String   assigned=processHelpService.getOrgLeaderByEmpId(account,"2");
			if(StringUtils.isNotEmpty(assigned)&&account.equals(assigned)){
				ajaxJson=new AjaxJson(true,"总监");
			}else if(StringUtils.isNotEmpty(assigned)&&assigned.equals("01000036")){
				ajaxJson=new AjaxJson(true,"是");
			}else{
				ajaxJson=new AjaxJson(false,"否");
			}
			return ajaxJson;
		}
       //根据员工编号查找最高领导人
		@RequestMapping("/maxManage.json")
		@ResponseBody
		public AjaxJson maxManage(String account){
			 AjaxJson ajaxJson=null;
			 String assigned="";
			 if(StringUtils.isEmpty(account)){
				 return ajaxJson= new AjaxJson(false,"account为空");
			 }
			 //如果是账号登陆 就去查找他的员工编号
			 String userAccount= processHelpService.loadEmpByUsrAccount(account);
			 if(StringUtils.isNotEmpty(userAccount)){
				 account=userAccount;
			 }
			  assigned=processHelpService.findOrgLeaderByEmpId(account, "2");
			 if(StringUtils.isNotEmpty(assigned)){
				 ajaxJson=new AjaxJson(true,assigned);
			 }else if(StringUtils.isNotEmpty(processHelpService.findOrgLeaderByEmpId(account, "1"))){
				 ajaxJson=new AjaxJson(true,processHelpService.findOrgLeaderByEmpId(account, "1"));
			 }else{
				 ajaxJson=new AjaxJson(true,processHelpService.findOrgLeaderByEmpId(account, "7"));
			 }
			return ajaxJson;
		}
		
	     //判断是否有主管
			@RequestMapping("/isManage.json")
			@ResponseBody
			public AjaxJson isManage(String account){
				 AjaxJson ajaxJson=null;
				 if(StringUtils.isEmpty(account)){
					 return ajaxJson= new AjaxJson(false);
				 }
				 String assigned=processHelpService.findOrgLeaderForSpecByEmpId(account,"4");
				 if(StringUtils.isNotEmpty(assigned)){
					 ajaxJson=new AjaxJson(true);
				 }else{
					 ajaxJson=new AjaxJson(false);
				 }
				return ajaxJson;
			}
			//判断是否是部门主管
			@RequestMapping("/depaHead.json")
			@ResponseBody
			public AjaxJson depaHead(String account){
				 AjaxJson ajaxJson=null;
				 String assigned=processHelpService.getOrgLeaderByEmpId(account,"4");
				 if(account.equals(assigned)){
					 ajaxJson=new AjaxJson(true);
				 }else{
					 ajaxJson=new AjaxJson(false);
				 }
				return ajaxJson;
			}
			//判断是否有总监
			@RequestMapping("/majordomo.json")
			@ResponseBody
			public AjaxJson majordomo(String account){
				 AjaxJson ajaxJson=null;
				 String assigned=processHelpService.getOrgLeader(account,"2");
				 if(StringUtils.isNotEmpty(assigned)){
					 ajaxJson=new AjaxJson(true);
				 }else{
					 ajaxJson=new AjaxJson(false);
				 }
				return ajaxJson;
			}
			
			//判断是否是经理或者副经理  如果是经理找总监  是副经理找总监没有的情况下找经理 1表示有总监 2没有总监   3 是副经理
			@RequestMapping("/isManager.json")
			@ResponseBody
			public AjaxJson isManager(String account) throws Exception{
				 AjaxJson ajaxJson=null;
				 //获取部门经理
				 String assigned=processHelpService.findOrgLeaderByEmpId(account,"1");  
				 //获取员工账号5
				 String userAccount =processHelpService.getEmpAccount(account);
				 //获取部门副经理
				 String manager=processHelpService.findOrgLeaderByEmpId(account,"7");
				 //如果经理是自己去总监
				 if(StringUtils.isNotEmpty(assigned)&&userAccount.equals(assigned)){
					 //获取部门总监
					 assigned=processHelpService.findOrgLeaderByEmpId(account,"2");
					 //是否有总监 1有 ，  2无
					 if(StringUtils.isNotEmpty(assigned)){
						 ajaxJson=new AjaxJson(true,"1");
					 }else{
						 ajaxJson=new AjaxJson(true,"2");
					 }
				 }else if(StringUtils.isNotEmpty(manager)&&manager.equals(userAccount)){
					 ajaxJson=new AjaxJson(true,"3");
				 }else{
					 ajaxJson=new AjaxJson(false);
				 }
				return ajaxJson;
			}
}
