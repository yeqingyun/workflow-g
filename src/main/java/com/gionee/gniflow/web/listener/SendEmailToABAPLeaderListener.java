package com.gionee.gniflow.web.listener;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.web.util.SpringTools;

public class SendEmailToABAPLeaderListener implements ExecutionListener {
	
	private static final long serialVersionUID = -5632410846012965641L;
	
	private Logger logger=LoggerFactory.getLogger(SendEmailToABAPLeaderListener.class);
	
	private ProcessHelpService processHelpService;
	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		logger.debug("Running in SendEmailToTeacher begin！");
		processHelpService = (ProcessHelpService) SpringTools.getBean(ProcessHelpService.class);
		RuntimeService runtimeService= execution.getEngineServices().getRuntimeService();
		String empNo = (String)runtimeService.getVariable(execution.getId(), "ABAPManagerAccount");//ABAP负责人账号
		String empName = (String)runtimeService.getVariable(execution.getId(), "ABAPDevelopAudiName");//ABAP开发人名字
		String procInstanceId=execution.getProcessInstanceId();
		
		List<String> accounts=new ArrayList<String>();
		accounts.add(empNo);
		
		String subjectEmp="";//邮件名称    
	    String contentEmp="";//邮件内容
	    subjectEmp = "ABAP开发处理情况";
		
	    //String address="huangfuxiong@gionee.com";
		contentEmp ="编号为"+procInstanceId+"的SAP需求申请流程的ABAP开发人员："+empName+",已完成ABAP开发环节工作";
		//SendMailTool.sendMailByAddress(subjectEmp, contentEmp, address);
	    if (accounts.size()>0) {
	    	//new SendEmailThread(toEmp, subjectEmp, contentEmp).start();
	    	processHelpService.sendEmail4UserAccount(accounts, subjectEmp, contentEmp);
	   }
	    
	  logger.debug("Running in SendEmailToTeacher end！");  
	}

}
