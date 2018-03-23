package com.gionee.gniflow.web.listener.LSAPRequirementApplication;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import com.gionee.gnif.model.AppUser;
import com.gionee.gnif.util.AppContext;
import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.dto.HrUserDto;
import com.gionee.gniflow.web.util.SendMailTool;
import com.gionee.gniflow.web.util.SpringTools;

public class StartLSAPRequirementApplicationListener implements ExecutionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		RuntimeService runtimeService = execution.getEngineServices().getRuntimeService();
		
		String programOpt  = (String)runtimeService.getVariable(execution.getId(), "programOpt");//项目
		String descriptionOfRequirement  = (String)runtimeService.getVariable(execution.getId(), "descriptionOfRequirement");
		String processInstanceId = execution.getProcessInstanceId();
		String applyUserName  = (String)runtimeService.getVariable(execution.getId(), "applyUserName");
		
		ProcessHelpService processHelpService = (ProcessHelpService)SpringTools.getBean("processHelpService");
		AppUser user = AppContext.getCurrentAppUser();
		HrUserDto hrUser = processHelpService.getHrUser4Account(user.getAccount());
		if(programOpt.equals("3")){
			//如果为印度Sap需求则发送邮件给固定的人员
			//彭昶昕(00002637，pengcx@gionee.com )
			if(hrUser.getEmpId()!=Integer.parseInt("2637")){
				String title = "印度SAP项目需求";
				String content = "<p>彭昶昕，您好:</p><p style='padding-left:2em'>"+applyUserName+"发起了SAP需求流程（流程编号："+processInstanceId+"；项目需求描述："+descriptionOfRequirement+"),请及时确认！</p><p>此邮件由<a href='http://flow.gionee.com' target='_blank'>Gionee工作流平台</a>自动生成，请勿回复此邮件!</p> ";
				SendMailTool.sendMailByAddress(title, content.toString(), "pengcx@gionee.com");
			}
			
		}
	}

}
