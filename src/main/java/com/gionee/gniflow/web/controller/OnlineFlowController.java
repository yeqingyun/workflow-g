package com.gionee.gniflow.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.model.Candidate;
import com.gionee.gniflow.biz.model.EngineerCollect;
import com.gionee.gniflow.biz.model.JTeamMonth;
import com.gionee.gniflow.biz.model.LaborContractRenewalEntity;
import com.gionee.gniflow.biz.model.LeaveProcessInstanceEntity;
import com.gionee.gniflow.biz.service.ActivitiHelpService;
import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.integration.dao.KeepRunTaskDao;
import com.gionee.gniflow.web.easyui.AjaxJson;
import com.gionee.gniflow.web.easyui.GridPageData;

@RequestMapping("/onlineFlow")
@Controller
public class OnlineFlowController {
	@Autowired
	private ProcessHelpService processHelpService;
	
	@Autowired
	private KeepRunTaskDao keepRunTaskDao;
	
	@Autowired
	private ActivitiHelpService activitiHelpService;
	
	//试用期判断是否要跳过主管副总审核
	@RequestMapping("/getProbationFlow.json")
	@ResponseBody
	public String getProbationFlow(String account){
		return processHelpService.getProbationFlow(account);
	}
	
	//获取季度活动所有季度对应的申请月份
	@RequestMapping("/getAllJTeamMonth.json")
	@ResponseBody
	public GridPageData<JTeamMonth> getAllJTeamMonth(){
		List<JTeamMonth> months=keepRunTaskDao.getAllJTeamMonth();
		Integer count=months.size();
		return new GridPageData<JTeamMonth>(months,count.longValue());
	}
	
	//更改季度活动申请月份
	@RequestMapping(value="/updateJTeamMonth.json", method=RequestMethod.POST)
	@ResponseBody
	public AjaxJson updateJTeamMonth(QueryMap queryMap){
		AjaxJson ajaxjson=new AjaxJson();
		try{
			keepRunTaskDao.updateJTeamMonth(queryMap.getMap());
			ajaxjson.setMsg("月份修改成功！");
		}catch(Exception e){
			ajaxjson.setSuccess(false);
			ajaxjson.setMsg("月份修改失败！");
			e.printStackTrace();
		}
		return ajaxjson;
	}
	
	//获取金立离职流程监控报表
	@RequestMapping("/leaveProcess.json")
	@ResponseBody
	public GridPageData<LeaveProcessInstanceEntity> getLeaveProcess(QueryMap queryMap){
		List<LeaveProcessInstanceEntity> leaves=keepRunTaskDao.getAllLeaveProcess(queryMap.getMap());
		//给各个实体的部门、职务、入职日期字段赋值
		setLeaveEntitry(leaves);
		Integer count=keepRunTaskDao.getLeaveCount(queryMap.getMap());
		return new GridPageData<LeaveProcessInstanceEntity>(leaves,count.longValue());
	}
	
	private void setLeaveEntitry(List<LeaveProcessInstanceEntity> leaves){
		for(LeaveProcessInstanceEntity leave:leaves){
			if(leave.getProcessInstanceName() != null && !"".equals(leave.getProcessInstanceName())){
				String[] proName=leave.getProcessInstanceName().split("：");
				String orgName=proName[2].substring(0, proName[2].indexOf(","));
				String duty=proName[3].substring(0, proName[3].indexOf(","));
				String entryDate=proName[4].substring(0, proName[4].indexOf("]"));
				leave.setOrgName(orgName);
				leave.setDuty(duty);
				leave.setEntryDate(entryDate);
			}
		}
	}
	
	//导出离职流程报表信息
	@RequestMapping("/exportLeaveProcessInfo.html")
	public void exportLeaveProcessInfo(QueryMap queryMap,HttpServletResponse response,HttpServletRequest request){
		queryMap.put("lastRow",1000000);
		List<LeaveProcessInstanceEntity> leaves=keepRunTaskDao.getAllLeaveProcess(queryMap.getMap());
		setLeaveEntitry(leaves);
    	try{
	    	if(!CollectionUtils.isEmpty(leaves)){
	    		activitiHelpService.toLeaveProcessInfoExcel(leaves, response,request);
	    	}else{
	    		response.setContentType("text/plain; charset=UTF-8");
				try {
					response.getWriter().println("数据不存在,导出Excel失败!");
				} catch (IOException e) {
					e.printStackTrace();
				}
	    	}
      }catch(Exception e){
    	  e.printStackTrace();
          response.setContentType("text/plain; charset=UTF-8");
		try {
			response.getWriter().println("导出Excel文件失败!");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
      }
	}
	
	//判断是否是北京金立职员
	@RequestMapping("/isBeijingEmpByEmpId.json")
	@ResponseBody
	public boolean isBeijingEmpByEmpId(String account){
		String empId=processHelpService.loadEmpByUsrAccount(account);
		return processHelpService.isBeijingEmpByEmpId(empId);
	}
	
	//获取历史的流程定义
	@RequestMapping("/isOffSign.josn")
	@ResponseBody
	public String getProcessDefineId(String pocessId){
		String processDefineId=keepRunTaskDao.getProcessDefineId(pocessId);
		return processDefineId.substring(0,processDefineId.indexOf(":"));
	}
	
	//根据账号获取职员编号
	@RequestMapping("/getEmpIdByAccount.json")
	@ResponseBody
	public String getEmpIdByAccount(String account){
		return processHelpService.loadEmpByUsrAccount(account);
	}
	//获取所有候选人
	@RequestMapping("/getAllCandidateUser.json")
	@ResponseBody
	public GridPageData<Candidate> getAllCandidateUser(){
		List<Candidate> users=keepRunTaskDao.getAllCandidateUser();
		Integer count=keepRunTaskDao.getAllCandidateCount();
		return new GridPageData<Candidate>(users,count.longValue());
	}
	
	//更新候选人信息
	@RequestMapping("/editCandidateInfoById.json")
	@ResponseBody
	public String editCandidateInfoById(QueryMap queryMap){
		try{
			keepRunTaskDao.editCandidateInfoById(queryMap.getMap());
		}catch(Exception e){
			return e.getMessage();
		}
		return "更新成功!";
	}
	
	//添加候选人信息
	@RequestMapping("/addCandidateInfo.json")
	@ResponseBody
	public String addCandidateInfo(QueryMap queryMap){
		try{
			keepRunTaskDao.addCandidateInfo(queryMap.getMap());
		}catch(Exception e){
			return e.getMessage();
		}
		return "添加成功!";
	}
	
	//根据id删除候选人信息
	@RequestMapping("/delCandidateInfoById.json")
	@ResponseBody
	public String delCandidateInfoById(String ids){
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("ids",ids.split(","));
			keepRunTaskDao.delCandidateInfoById(map);
		}catch(Exception e){
			return e.getMessage();
		}
		return "删除成功!";
	}
}
