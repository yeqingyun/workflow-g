package com.gionee.gniflow.web.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.web.util.SpringTools;

public class SendEmailTOrelatedProbationPerson implements ExecutionListener {
	
	private Logger logger = LoggerFactory.getLogger(SendEmailTOrelatedProbationPerson.class);
	private ProcessHelpService processHelpService;
	private static final long serialVersionUID = -8591980217228753495L;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		logger.debug("Running in SendEmailTOrelatedProbationPerson begin！");
		processHelpService = (ProcessHelpService) SpringTools.getBean(ProcessHelpService.class);
		RuntimeService runtimeService= execution.getEngineServices().getRuntimeService();
		String userLoginNo = (String)runtimeService.getVariable(execution.getId(), "userLoginNo");//试用期转正人员的账号
		String empId=(String)runtimeService.getVariable(execution.getId(),"applyUserAccount");//试用期转正申请人的编号
		String empName = (String)runtimeService.getVariable(execution.getId(), "empName");//转正人员的姓名
		String managerAccount = (String)runtimeService.getVariable(execution.getId(), "managerAccount");//直属领导账号
		String wageManagerAccount = (String)runtimeService.getVariable(execution.getId(), "wageManagerAccount");//薪酬经理账号
		String processId=execution.getProcessInstanceId();
		List<String> address=new ArrayList<String>();
		address.add(userLoginNo);
		address.add(wageManagerAccount);
		if(managerAccount!=null){
			address.add(managerAccount);
		}
		//address.add("00002329");
		
		processHelpService.sendEmail4UserAccount(address,"金立试用期转正考核","工作流(http://flow.gionee.com/index.html),实例编号为："+processId+"、申请人为:"+empName+"["+empId+"]的试用期转正考核流程已完成!");
	}

}
