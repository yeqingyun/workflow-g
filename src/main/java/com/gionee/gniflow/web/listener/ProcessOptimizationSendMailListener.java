package com.gionee.gniflow.web.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gniflow.web.util.SendMailTool;

//流程优化需求流程问题分析步骤发邮件给廖海如
public class ProcessOptimizationSendMailListener implements ExecutionListener {

	private static final long serialVersionUID = 7156658471027518899L;
	private Logger logger = LoggerFactory.getLogger(ProcessOptimizationSendMailListener.class);

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		logger.debug("Running in ProcessOptimizationSendMailListener begin！");
		String title = "流程优化需求流程问题分析阅知";
		String processId = execution.getProcessInstanceId();
		String content = "尊敬的同事，您好：</br>" + "流程实例编号为" + processId + "的流程优化需求流程已到达问题分析步骤，请登录工作流平台查看。</br>"
				+ "此邮件由 Gionee工作流平台(网址：<a href='http://flow.gionee.com'>http://flow.gionee.com</a>) 自动生成，请勿回复此邮件!感谢您的配合！</br>"
				+ "祝您工作顺利，生活愉快！";
		String email = "LHR@GIONEE.COM";
		SendMailTool.sendMailByAddress(title, content, email);
		logger.debug("Running in ProcessOptimizationSendMailListener end！");	
	}
	
}
