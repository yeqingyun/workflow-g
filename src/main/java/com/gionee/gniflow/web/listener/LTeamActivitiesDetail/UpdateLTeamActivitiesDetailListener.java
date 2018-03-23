package com.gionee.gniflow.web.listener.LTeamActivitiesDetail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.model.LTeamActivitiesDetail;
import com.gionee.gniflow.biz.model.TeamActivitiesDetail;
import com.gionee.gniflow.biz.service.LTeamActivitiesDetailService;
import com.gionee.gniflow.biz.service.TeamActivitiesDetailService;
import com.gionee.gniflow.constant.BPMConstant;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.web.support.AudiException;
import com.gionee.gniflow.web.util.DateUtil;
import com.gionee.gniflow.web.util.SpringTools;

public class UpdateLTeamActivitiesDetailListener implements ExecutionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6040840716957154734L;
	private Logger logger = LoggerFactory.getLogger(UpdateLTeamActivitiesDetailListener.class);

	@SuppressWarnings("unchecked")
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		logger.debug("Running in UpdateLTeamActivitiesDetailLister begin!");
		LTeamActivitiesDetailService lTeamActivitiesDetailService = (LTeamActivitiesDetailService)SpringTools.getBean(LTeamActivitiesDetailService.class);
		RuntimeService runtimeService = execution.getEngineServices().getRuntimeService();
		//获取申请的信息
		String applyUserId = (String)runtimeService.getVariable(execution.getId(), "applyUserId");
		String companyName = (String)runtimeService.getVariable(execution.getId(), "companyName");
		String activityYear = (String)runtimeService.getVariable(execution.getId(), "activityYear");
		String activityQuarter = (String)runtimeService.getVariable(execution.getId(), "activityQuarter");
		
		//获取人员信息
		String humanStr1 = (String) runtimeService.getVariable(execution.getId(), "humanStr1");
		String humanStr2 = (String) runtimeService.getVariable(execution.getId(), "humanStr2");
		String humanStr3 = (String) runtimeService.getVariable(execution.getId(), "humanStr3");
		
		String month1 = (String)runtimeService.getVariable(execution.getId(), "month1");
		String month2 = (String)runtimeService.getVariable(execution.getId(), "month2");
		String month3 = (String)runtimeService.getVariable(execution.getId(), "month3");
		
		JSONArray human1Array = JSONArray.parseArray(humanStr1);
		JSONArray human2Array = JSONArray.parseArray(humanStr2);
		JSONArray human3Array = JSONArray.parseArray(humanStr3);
		
		List<LTeamActivitiesDetail> lTeamActivitiesDetails = new ArrayList<LTeamActivitiesDetail>();
		for(int i = 0;i<human1Array.size();i++){
			if(human1Array.get(i) != null){
				Map<String,String> human = (Map<String,String>)human1Array.get(i);
				
				QueryMap queryMap = new QueryMap();
				queryMap.put("empId", human.get("empId"));
				queryMap.put("activityYear", activityYear);
				queryMap.put("activityQuarter", activityQuarter);
				queryMap.put("activityMonth", month1);
				List<LTeamActivitiesDetail> list  = lTeamActivitiesDetailService.query(queryMap);
				if(list != null && list.size()>0){
					if(list.get(0).getStatus() == WebReqConstant.NOT_PASSED){
						throw new AudiException("员工["+human.get("empId")+","+human.get("empNm")+"]"+month1+"的经费申请正在处于审批当中！");
					}else{
						throw new AudiException("员工["+human.get("empId")+","+human.get("empNm")+"]"+month1+"的经费申请已经审批通过了！");
					}
				}
				LTeamActivitiesDetail ltad = new LTeamActivitiesDetail();
				ltad.setEmpId(human.get("empId"));
				ltad.setActivityMonth(month1);
				ltad.setActivityQuarter(activityQuarter);
				ltad.setActivityYear(activityYear);
				ltad.setName(human.get("empNm"));
				ltad.setDepartment(companyName);
				ltad.setProcInstId(execution.getProcessInstanceId());
				ltad.setCreateBy(applyUserId);
				ltad.setCreateTime(new Date());
				ltad.setStatus(WebReqConstant.NOT_PASSED);
				lTeamActivitiesDetails.add(ltad);
			}
		}
		for(int i = 0;i<human2Array.size();i++){
			if(human2Array.get(i) != null){
				Map<String,String> human = (Map<String,String>)human2Array.get(i);
				
				QueryMap queryMap = new QueryMap();
				queryMap.put("empId", human.get("empId"));
				queryMap.put("activityYear", activityYear);
				queryMap.put("activityQuarter", activityQuarter);
				queryMap.put("activityMonth", month2);
				List<LTeamActivitiesDetail> list  = lTeamActivitiesDetailService.query(queryMap);
				if(list != null && list.size()>0){
					if(list.get(0).getStatus() == WebReqConstant.NOT_PASSED){
						throw new AudiException("员工["+human.get("empId")+","+human.get("empNm")+"]"+month2+"的经费申请正在处于审批当中！");
					}else{
						throw new AudiException("员工["+human.get("empId")+","+human.get("empNm")+"]"+month2+"的经费申请已经审批通过了！");
					}
				}
				LTeamActivitiesDetail ltad = new LTeamActivitiesDetail();
				ltad.setEmpId(human.get("empId"));
				ltad.setActivityMonth(month2);
				ltad.setActivityQuarter(activityQuarter);
				ltad.setActivityYear(activityYear);
				ltad.setName(human.get("empNm"));
				ltad.setDepartment(companyName);
				ltad.setProcInstId(execution.getProcessInstanceId());
				ltad.setCreateBy(applyUserId);
				ltad.setCreateTime(new Date());
				ltad.setStatus(WebReqConstant.NOT_PASSED);
				lTeamActivitiesDetails.add(ltad);
			}
		}
		for(int i = 0;i<human3Array.size();i++){
			if(human3Array.get(i) != null){
				Map<String,String> human = (Map<String,String>)human3Array.get(i);
				
				QueryMap queryMap = new QueryMap();
				queryMap.put("empId", human.get("empId"));
				queryMap.put("activityYear", activityYear);
				queryMap.put("activityQuarter", activityQuarter);
				queryMap.put("activityMonth", month3);
				List<LTeamActivitiesDetail> list  = lTeamActivitiesDetailService.query(queryMap);
				if(list != null && list.size()>0){
					if(list.get(0).getStatus() == WebReqConstant.NOT_PASSED){
						throw new AudiException("员工["+human.get("empId")+","+human.get("empNm")+"]"+month3+"的经费申请正在处于审批当中！");
					}else{
						throw new AudiException("员工["+human.get("empId")+","+human.get("empNm")+"]"+month3+"的经费申请已经审批通过了！");
					}
				}
				LTeamActivitiesDetail ltad = new LTeamActivitiesDetail();
				ltad.setEmpId(human.get("empId"));
				ltad.setActivityMonth(month3);
				ltad.setActivityQuarter(activityQuarter);
				ltad.setActivityYear(activityYear);
				ltad.setName(human.get("empNm"));
				ltad.setDepartment(companyName);
				ltad.setProcInstId(execution.getProcessInstanceId());
				ltad.setCreateBy(applyUserId);
				ltad.setCreateTime(new Date());
				ltad.setStatus(WebReqConstant.NOT_PASSED);
				lTeamActivitiesDetails.add(ltad);
			}
		}
		lTeamActivitiesDetailService.save(lTeamActivitiesDetails);
		logger.debug("Running in UpdateLTeamActivitiesDetailLister end!");
	}

}
