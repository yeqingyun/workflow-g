package com.gionee.gniflow.web.listener.PersonalRequirementExchange;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gniflow.biz.model.PersonalRequirementExchange;
import com.gionee.gniflow.biz.service.PersonalRequirementExchangeService;
import com.gionee.gniflow.web.util.SpringTools;

public class InsertPersonalExchangeListener implements ExecutionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2562640038991608384L;

	private Logger logger = LoggerFactory.getLogger(InsertPersonalExchangeListener.class);

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		logger.debug("Running in InsertPersonalExchangeListener begin!");
		RuntimeService runtimeService = execution.getEngineServices().getRuntimeService();
		String reqReplacePerson = (String) runtimeService.getVariable(execution.getId(), "reqReplacePerson");
		String company = (String) runtimeService.getVariable(execution.getId(), "companyName");
		String department = (String) runtimeService.getVariable(execution.getId(), "orgName");
		if(reqReplacePerson!=null){
			PersonalRequirementExchangeService preService = (PersonalRequirementExchangeService) SpringTools.getBean(PersonalRequirementExchangeService.class);		
			PersonalRequirementExchange pre = new PersonalRequirementExchange();
			String account = reqReplacePerson.substring(reqReplacePerson.indexOf("[")+1, reqReplacePerson.indexOf("]"));
			String name = reqReplacePerson.substring(0, reqReplacePerson.indexOf("[")).trim();
			
			pre.setAccount(account);
			pre.setName(name);
			pre.setCompany(company);
			pre.setDepartment(department);
			pre.setProcInstId(execution.getProcessInstanceId());
			preService.save(pre);
		}
		logger.debug("Running in InsertPersonalExchangeListener end!");
	}

}
