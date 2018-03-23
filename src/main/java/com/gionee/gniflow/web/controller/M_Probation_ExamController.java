package com.gionee.gniflow.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gionee.gniflow.biz.service.M_Probation_ExamService;
import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.web.easyui.AjaxJson;

@RequestMapping("/probationExam")
@Controller
public class M_Probation_ExamController {
	
    @Autowired
	private M_Probation_ExamService m_Probation_ExamService;
	
    @Autowired
    private ProcessHelpService processHelpService;
	@RequestMapping("/deleteSalary.json")
	@ResponseBody
	public AjaxJson deleteSalary(@RequestParam("processInstanceId") String processInstanceId) throws Exception {
		try {
			int count=0;
			count=m_Probation_ExamService.deleteSalary(processInstanceId);
			if(count==0){
				return new AjaxJson(false, "实例编号为："+processInstanceId+"的流程不能删除薪资数据!");	
			}else{
			return new AjaxJson(true, "实例编号为："+processInstanceId+"的流程删除薪资数据成功!");
			}
    	} catch (Exception e) {
            e.printStackTrace();
            return new AjaxJson(false, "实例编号为："+processInstanceId+"的流程删除薪资数据失败!");
        } 
	}

	
}
