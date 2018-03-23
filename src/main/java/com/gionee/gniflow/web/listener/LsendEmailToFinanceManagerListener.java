package com.gionee.gniflow.web.listener;

import java.util.List;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.dto.HrUserDto;
import com.gionee.gniflow.web.notice.SendEmailThread;
import com.gionee.gniflow.web.util.SendMailTool;
import com.gionee.gniflow.web.util.SpringTools;

public class LsendEmailToFinanceManagerListener implements ExecutionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5632410846012973641L;
	private ProcessHelpService processHelpService;

	private Logger logger = LoggerFactory.getLogger(LsendEmailToFinanceManagerListener.class);

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		logger.debug("Running in SendEmailToApplyUser begin！");
		processHelpService = (ProcessHelpService) SpringTools.getBean(ProcessHelpService.class);
		RuntimeService runtimeService= execution.getEngineServices().getRuntimeService();
		//获取发起人信息
		String applyUserId = (String)runtimeService.getVariable(execution.getId(), "applyUserId");//发起人员工编号
		String applyUserName = processHelpService.getUserName(applyUserId);	//发起人姓名
		String infoDataContents = (String)runtimeService.getVariable(execution.getId(), "infoDataContents");//申请内容
		String infoDataUse = (String)runtimeService.getVariable(execution.getId(), "infoDataUse");//数据用途
		//HrUserDto  user = processHelpService.getHrUser4Account(applyUserId);
		//String orgName = user.getOrgName();
		String processId = execution.getProcessInstanceId();//获取流程Id
		//发邮件给财务经理
	    String toEmp="zhouxiaopei@gionee.com";
	    
	    String subjectEmp="";//邮件名称    
	    String contentEmp="";//邮件内容
	    subjectEmp = "信息数据申请流程通知";
	    contentEmp ="亲爱的同事，您好！</br>"
				+"由员工编号：【"+applyUserId+"】,姓名：【"+applyUserName+"】发起流程实例编号为:【"+processId+"】的数据信息申请流程</br>"
				+"申请数据内容及范围：【"+infoDataContents+"】,数据用途：【"+infoDataUse+"】,望知悉</br>"
				+ "此邮件由 Gionee工作流平台(网址：flow.gionee.com) 自动生成，请勿回复此邮件!感谢您的配合！</br>"
				+ "祝您工作顺利，生活愉快！";
	    
	    
	    if (!StringUtils.isEmpty(toEmp)) {
	    	//new SendEmailThread(toEmp, subjectEmp, contentEmp).start();
	    	SendMailTool.sendMailByAddress(subjectEmp, contentEmp, toEmp);
	   }
	    
	    logger.debug("Running in SendEmailToApplyUser end!");
	}
}
