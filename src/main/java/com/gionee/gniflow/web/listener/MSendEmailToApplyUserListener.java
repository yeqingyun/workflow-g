package com.gionee.gniflow.web.listener;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.web.util.SendMailTool;
//import com.gionee.gniflow.web.notice.SendEmailThread;
import com.gionee.gniflow.web.util.SpringTools;

public class MSendEmailToApplyUserListener implements ExecutionListener{
	private Logger logger = LoggerFactory.getLogger(MSendEmailToApplyUserListener.class);
	private ProcessHelpService processHelpService;
	/**
	 * 
	 */
	private static final long serialVersionUID = -8591980217384953495L;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		
		logger.debug("Running in SendEmailToApplyUser begin！");
		processHelpService = (ProcessHelpService) SpringTools.getBean(ProcessHelpService.class);
		RuntimeService runtimeService= execution.getEngineServices().getRuntimeService();
		//获取申请人信息
		String applyUserId = (String)runtimeService.getVariable(execution.getId(), "applyUserId");
		
		  //发邮件给当事人
	    String toEmp="";
	    String subjectEmp="";//邮件名称
	    String contentEmp="";//邮件内容
	    String applyUserName="";//获取发起人姓名
	   // String processId="";//流程ID
	    //String processName="";//流程名
	   // String applyUserId="00001474";
	  
	    String empaccount="";//获取发起人员工编号
	    //empaccount= processHelpService.getEmpAccount(applyUserId);
	    //String mailToEmp = null;//获取发起人员工姓名
	    //mailToEmp=empaccount;
	    applyUserName = processHelpService.getUserName(applyUserId);//通过用户编号获得用户名
	    toEmp = processHelpService.getUserEmailAddress(applyUserId);//获取用户的邮件地址
		//processId = execution.getProcessInstanceId();
		subjectEmp = "流程审批未通过通知";
		contentEmp ="Dear&nbsp;&nbsp;&nbsp;&nbsp;"+applyUserName+":</br>"
				+"您好！有一条您发起的申请流程未通过审批。</br>"
				+ "特此通知您登陆  工作流平台系统  进行申请修改，此邮件为系统自动发送，请勿回复，感谢您的配合！</br></br>"
				+ "祝您工作顺利，生活愉快！";
		
	    if (!StringUtils.isEmpty(toEmp)) {
	    	//new SendEmailThread(toEmp, subjectEmp, contentEmp).start();
	    	SendMailTool.sendMailByAddress(subjectEmp, contentEmp, toEmp);
	   }
		//-------------发邮件给当事人
		logger.debug("Running in SendEmailToApplyUser end!");
		
		
	}
}
