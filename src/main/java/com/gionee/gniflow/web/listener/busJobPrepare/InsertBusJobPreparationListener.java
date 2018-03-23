package com.gionee.gniflow.web.listener.busJobPrepare;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gniflow.biz.model.BusJobPreparation;
import com.gionee.gniflow.biz.service.BusJobPreparationService;
import com.gionee.gniflow.web.util.SpringTools;

public class InsertBusJobPreparationListener implements ExecutionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 449808704451563972L;
	
	private Logger logger = LoggerFactory.getLogger(InsertBusJobPreparationListener.class);

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		logger.debug("Running in InsertBusJobPreparationListener begin!");
		RuntimeService runtimeService = execution.getEngineServices().getRuntimeService();
		String orgName = (String) runtimeService.getVariable(execution.getId(), "orgName");
		int orgId = toInteger((String) runtimeService.getVariable(execution.getId(), "orgId"));
		String rootOrgName = (String) runtimeService.getVariable(execution.getId(), "companyName");
		int rootOrgId = toInteger((String) runtimeService.getVariable(execution.getId(), "companyId"));
		String positions = (String) runtimeService.getVariable(execution.getId(), "jobName");
		String grade = (String) runtimeService.getVariable(execution.getId(), "jobGrade");
		String salaryRange = (String) runtimeService.getVariable(execution.getId(), "salaryScope");
		int plait = Integer.parseInt((String) runtimeService.getVariable(execution.getId(), "stanPreparation"));
		int existNum = Integer.parseInt((String) runtimeService.getVariable(execution.getId(), "actuPreparation"));
		String selectValue = (String) runtimeService.getVariable(execution.getId(), "selectValue");
		if("5".equals(selectValue)){
			BusJobPreparationService bpService = (BusJobPreparationService) SpringTools.getBean(BusJobPreparationService.class);
			BusJobPreparation bp = new BusJobPreparation();
			bp.setGrade(grade);
			bp.setOrgId(orgId);
			bp.setOrgName(orgName);
			bp.setPlait(plait);
			bp.setPositions(positions);
			bp.setRootOrgId(rootOrgId);
			bp.setRootOrgName(rootOrgName);
			bp.setSalaryRange(salaryRange);
			bp.setExistNum(existNum);
			bpService.save(bp);
		}
		logger.debug("Running in InsertBusJobPreparationListener end!");
	}
	
	public int toInteger(String str){
		String[] strs =  str.split(",");
		String temp="";
		for(int i = 0;i<strs.length;i++){
			temp += strs[i];
		}
		int result = Integer.parseInt(temp);
		return result;
	}
	
}
