package com.gionee.gniflow.web.listener.LSapAccountModifyApplication;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import com.gionee.gnif.model.AppUser;
import com.gionee.gnif.util.AppContext;
import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.dto.HrUserDto;
import com.gionee.gniflow.web.util.SendMailTool;
import com.gionee.gniflow.web.util.SpringTools;

public class UpdateLSapAccountModifyApplication implements ExecutionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		RuntimeService runtimeService = execution.getEngineServices().getRuntimeService();
		
		String programOpt  = (String)runtimeService.getVariable(execution.getId(), "programOpt");//项目
		String accountDescribe  = (String)runtimeService.getVariable(execution.getId(), "updateaccountDescribe");
		String processInstanceId = execution.getProcessInstanceId();
		
		ProcessHelpService processHelpService = (ProcessHelpService)SpringTools.getBean("processHelpService");
		AppUser user = AppContext.getCurrentAppUser();
		HrUserDto hrUser = processHelpService.getHrUser4Account(user.getAccount());
		if(programOpt.equals("3")){
			//如果为印度Sap需求则发送邮件给固定的人员
			//彭昶昕(00002637，pengcx@gionee.com )
			if(hrUser.getEmpId()!=Integer.parseInt("2637")){
				String name = hrUser.getName();
				String title = "印度SAP项目需求";
				String content = "<p>彭昶昕，您好:</p><p style='padding-left:2em'>"+name+"发起了SAP帐号权限变更流程（流程编号："+processInstanceId+"；权限申请描述："+accountDescribe+"),请及时确认！</p><p>此邮件由<a href='http://flow.gionee.com' target='_blank'>Gionee工作流平台</a>自动生成，请勿回复此邮件!</p> ";
				SendMailTool.sendMailByAddress(title, content.toString(), "pengcx@gionee.com");
			}
			
		}
	}

}
