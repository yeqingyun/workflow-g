package com.gionee.gniflow.web.listener.AccessoriesPricing;

import java.util.Arrays;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.web.util.SendMailTool;
import com.gionee.gniflow.web.util.SpringTools;

//配件定价流程通过审批时给所有参与审批的人员发送邮件通知
public class AccessoriesPricingSendMailListener implements ExecutionListener {

	private static final long serialVersionUID = -5004958930882885109L;
	private Logger logger = LoggerFactory.getLogger(AccessoriesPricingSendMailListener.class);
	private ProcessHelpService processHelpService;
	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		logger.debug("Running in AccessoriesPricingSendMailListener begin！");
				
		RuntimeService runtimeService= execution.getEngineServices().getRuntimeService();	
		processHelpService = (ProcessHelpService) SpringTools.getBean(ProcessHelpService.class);
		
		String AccessoriesManagerAccount = "";
		if(runtimeService.getVariable(execution.getId(),"AccessoriesManagerAccount") != null) {
			AccessoriesManagerAccount = (String) runtimeService.getVariable(execution.getId(),"AccessoriesManagerAccount");
		}
		String FinanceManagerAccount = "00000074";
		String ECManagerAccount = "";
		if(runtimeService.getVariable(execution.getId(),"ECManagerAccount") != null) {
			ECManagerAccount = (String) runtimeService.getVariable(execution.getId(),"ECManagerAccount");
		}
		String BrandsManagerAccount = "";
		if(runtimeService.getVariable(execution.getId(),"BrandsManagerAccount") != null) {
			BrandsManagerAccount = (String) runtimeService.getVariable(execution.getId(),"BrandsManagerAccount");
		}
		String CSManagerAccount = "";
		if(runtimeService.getVariable(execution.getId(),"CSManagerAccount") != null) {
			CSManagerAccount = (String) runtimeService.getVariable(execution.getId(),"CSManagerAccount");
		}
		String CommercialManagementAccount = "02500075";
		String processId = execution.getProcessInstanceId();
		
		String title = "配件定价流程已通过审批通知";
		String content = "尊敬的同事，您好：</br>" + "流程实例编号为" + processId + "的配件定价流程已通过审批，请登录工作流平台查看。</br>"
				+ "此邮件由 Gionee工作流平台(网址：<a href='http://flow.gionee.com'>http://flow.gionee.com</a>) 自动生成，请勿回复此邮件!感谢您的配合！</br>"
				+ "祝您工作顺利，生活愉快！";
		
		if(AccessoriesManagerAccount != null && !AccessoriesManagerAccount.equals("")) {
			String email = processHelpService.getUserEmailAddress(AccessoriesManagerAccount);
			if(email != null && !email.trim().equals("")) {
				//processHelpService.sendEmail4UserAccount(Arrays.asList(new String[]{email.trim()}), title, content);
				SendMailTool.sendMailByAddress(title, content, email);
			}
		}		
		if(FinanceManagerAccount != null && !FinanceManagerAccount.equals("")) {
			String email = processHelpService.getUserEmailAddress(FinanceManagerAccount);
			if(email != null && !email.trim().equals("")) {
				//processHelpService.sendEmail4UserAccount(Arrays.asList(new String[]{email.trim()}), title, content);
				SendMailTool.sendMailByAddress(title, content, email);
			}
		}
		if(ECManagerAccount != null && !ECManagerAccount.equals("")) {
			String email = processHelpService.getUserEmailAddress(ECManagerAccount);
			if(email != null && !email.trim().equals("")) {
				//processHelpService.sendEmail4UserAccount(Arrays.asList(new String[]{email.trim()}), title, content); 
				SendMailTool.sendMailByAddress(title, content, email);
			}
		}
		if(BrandsManagerAccount != null && !BrandsManagerAccount.equals("")) {
			String email = processHelpService.getUserEmailAddress(BrandsManagerAccount);
			if(email != null && !email.trim().equals("")) {
				//processHelpService.sendEmail4UserAccount(Arrays.asList(new String[]{email.trim()}), title, content);
				SendMailTool.sendMailByAddress(title, content, email);
			}
		}
		if(CSManagerAccount != null && !CSManagerAccount.equals("")) {
			String email = processHelpService.getUserEmailAddress(CSManagerAccount);
			if(email != null && !email.trim().equals("")) {
				//processHelpService.sendEmail4UserAccount(Arrays.asList(new String[]{email.trim()}), title, content);
				SendMailTool.sendMailByAddress(title, content, email);
			}
		}
		if(CommercialManagementAccount != null && !CommercialManagementAccount.equals("")) {
			String email = processHelpService.getUserEmailAddress(CommercialManagementAccount);
			if(email != null && !email.trim().equals("")) {
				//processHelpService.sendEmail4UserAccount(Arrays.asList(new String[]{email.trim()}), title, content);
				SendMailTool.sendMailByAddress(title, content, email);
			}
		}

		logger.debug("Running in AccessoriesPricingSendMailListener end！");
	}

}
