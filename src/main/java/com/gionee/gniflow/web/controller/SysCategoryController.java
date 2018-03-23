package com.gionee.gniflow.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gionee.gniflow.biz.model.SysCategory;
import com.gionee.gniflow.biz.service.SysCategoryService;

@Controller
@RequestMapping("/sysCategory")
public class SysCategoryController {
  
   @Autowired
   SysCategoryService sysCategoryService;
	
   @RequestMapping("/add.json")
   @ResponseBody
   public String add(SysCategory sysCategory){
	   
	   Integer result=sysCategoryService.query(sysCategory);
	   if(result==1){
		   return "该部门系统已经存在";
	   }
	   sysCategoryService.add(sysCategory);
	   return "成功";
   }
}
