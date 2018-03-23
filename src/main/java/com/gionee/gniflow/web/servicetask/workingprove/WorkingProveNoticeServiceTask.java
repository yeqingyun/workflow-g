package com.gionee.gniflow.web.servicetask.workingprove;

import java.util.Arrays;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.web.util.SpringTools;

/**
 * 
 * The class <code>SendEmailServiceTask</code>
 * 远程调用的服务类
 * @author lipw
 * @version 1.0
 */
public class WorkingProveNoticeServiceTask implements JavaDelegate {
	
	private static final Logger logger = LoggerFactory.getLogger(WorkingProveNoticeServiceTask.class);
	
	private ProcessHelpService processHelpService;
	
	private RuntimeService runtimeService;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		logger.debug("[SendEmailServiceTask]-->Send Email Begin！");
		processHelpService = (ProcessHelpService) SpringTools.getBean(ProcessHelpService.class);
		runtimeService = execution.getEngineServices().getRuntimeService();
		
		String startUser = (String) runtimeService.getVariable(execution.getId(), "applyUserId");
		
		processHelpService.sendEmail4UserAccount(Arrays.asList(startUser), "在职证明邮件提醒", "在职证明邮件提醒内容!");
		
		logger.debug("[SendEmailServiceTask]-->Send Email End！");
		
	}

}
