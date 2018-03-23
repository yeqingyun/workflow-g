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

public class SendEmailToProjectHumanApplyPerson implements ExecutionListener {
	
	private Logger logger = LoggerFactory.getLogger(SendEmailToProjectHumanApplyPerson.class);
	private ProcessHelpService processHelpService;
	private static final long serialVersionUID = -8591980136228753495L;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		logger.debug("Running in SendEmailToProjectHumanApplyPerson begin！");
		processHelpService = (ProcessHelpService) SpringTools.getBean(ProcessHelpService.class);
		RuntimeService runtimeService= execution.getEngineServices().getRuntimeService();
		String applyNo = (String)runtimeService.getVariable(execution.getId(), "assigneeUpdate");//文档会签流程申请人账号
		String processId=execution.getProcessInstanceId();
		List<String> address=new ArrayList<String>();
		address.add(applyNo);
		if(address != null && address.size() > 0){
			processHelpService.sendEmail4UserAccount(address,"流程专员审核完成","您发起的流程文档会签(流程编号:"+processId+")流程专员已审批，已经到达其他环节,请登录<a href='http://flow.gionee.com/index.html'>工作流平台</a>进行查看流程信息，多谢你的支持与合作，祝工作愉快。");
		}
	}

}
