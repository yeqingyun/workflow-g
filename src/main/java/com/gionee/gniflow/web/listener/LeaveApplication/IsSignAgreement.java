package com.gionee.gniflow.web.listener.LeaveApplication;


import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.web.listener.filesign.InsertFileSignInfoListener;
import com.gionee.gniflow.web.util.SendMailTool;
import com.gionee.gniflow.web.util.SpringTools;

public class IsSignAgreement implements ExecutionListener{
	private Logger logger = LoggerFactory.getLogger(InsertFileSignInfoListener.class);
	private ProcessHelpService processHelpService;
	/**
	 * 
	 */
	private static final long serialVersionUID = 8373919912017106958L;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		logger.debug("Running in IsSignAgreement begin!");
		//QuitApplicationInfoService qaService= (QuitApplicationInfoService) SpringTools.getBean(QuitApplicationInfoService.class);
		//com.gionee.gnif.mail.biz.model.MailSender mailSender = new MailSender();
		RuntimeService runtimeService= execution.getEngineServices().getRuntimeService();
		processHelpService = (ProcessHelpService) SpringTools.getBean(ProcessHelpService.class);
		String processId = execution.getProcessInstanceId();//获取流程Id
		//List<QuitApplicationInfo> list = null;
		//QueryMap queryMap = new QueryMap();
		//queryMap.put("processid", execution.getProcessInstanceId());
		//list=qaService.query(queryMap);
		//if (!CollectionUtils.isEmpty(list)) {
		//QuitApplicationInfo qaInfo=new QuitApplicationInfo();
		//qaInfo=list.get(0);
		String isAgreement=(String)runtimeService.getVariable(execution.getId(), "isAgreement");
		//是否执行竞业协议 0 执行竞业协议，1不执行竞业协议
		//qaInfo.setIsPreAgreement(isSignAgreement);
		//qaInfo.setTimeLimit((String)runtimeService.getVariable(execution.getId(), "dutaryTime"));//执行竞业协议期限
		//qaService.save(qaInfo);
		String companyShares = (String)runtimeService.getVariable(execution.getId(), "companyShares");
		//是否有公司股份  0 有公司股份，1没有公司股份
		String dutaryTime= (String)runtimeService.getVariable(execution.getId(), "dutaryTime");//执行竞业协议期限
		String userAccount=(String)runtimeService.getVariable(execution.getId(), "applyUserId");//申请人
		String userName=(String)runtimeService.getVariable(execution.getId(), "userName");//申请人姓名
		String orgName=(String)runtimeService.getVariable(execution.getId(), "orgName");//申请人部门
		String duty=(String)runtimeService.getVariable(execution.getId(), "duty");//申请人职位
		String title="离职申请执行竞业协议";
		String titleCompanyShares = "离职申请公司股份提醒";
		String titleCompanySharesContent ="姓名："+userName+",员工编号："
				+userAccount+",部门："+orgName+",职位："
				+duty+"。该员工发起的发起的流程编号为【"
				+processId+"】离职申请中，该员工拥有公司股份，请知悉!!本邮件为工作流邮件，请勿回复！";
		String emailContentToApply = userName+"您好，您发起的流程编号为【"+processId+"】离职申请中需要执行竞业协议，期限为"+dutaryTime+"个月,望知悉本邮件为工作流邮件，请勿回复！";
		String emailContentToHrContiansCompnyShares =  "姓名："+userName+",员工编号："
												+userAccount+",部门："+orgName+",职位："
												+duty+"。该员工发起的发起的流程编号为【"
												+processId+"】离职申请中，该员工需要执行竞业协议"
												+dutaryTime+"个月。并且该员工拥有公司股份，请知悉!!本邮件为工作流邮件，请勿回复！";
		String  eamilContentToHr = "姓名："+userName+",员工编号："
								+userAccount+",部门："+orgName+",职位："
								+duty+"。该员工发起的发起的流程编号为【"
								+processId+"】离职申请中，该员工需要执行竞业协议"
								+dutaryTime+"个月，请知悉!!本邮件为工作流邮件，请勿回复！";
		String eamilSendToApply = processHelpService.getUserEmailAddress(userAccount);//发给发起人
		String emailSendToHr = processHelpService.getUserEmailAddress("00002091");//人事专员张锦菊00002091
		if("0".equals(isAgreement)){//如果执行竞业协议，则发邮件提醒人事专员及离职人员
			//如果执行竞业协议，给人力资源发邮件
			/*mailSender.sendMail(processHelpService.getUserEmailAddress("00000201"), "离职申请执行竞业协议", 
					"姓名："+userName+",员工编号："+userAccount+",部门："+orgName+",职位："+duty+"。该员工在离职申请中执行竞业协议，请知悉!!本邮件为工作流邮件，请勿回复！");*/
			/*
			 * 若执行竞业协议，发邮件给本人，
			 */
			SendMailTool.sendMailByAddress(title,emailContentToApply ,eamilSendToApply);//发邮件提醒申请人
			if("1".equals(companyShares)){
				SendMailTool.sendMailByAddress(title,eamilContentToHr,emailSendToHr );
			}else{
				SendMailTool.sendMailByAddress(title,emailContentToHrContiansCompnyShares,emailSendToHr);
			}
			
		}else{
			if("1".equals(companyShares)){
				SendMailTool.sendMailByAddress(titleCompanyShares,titleCompanySharesContent,emailSendToHr );
			}
		}
		//}
		logger.debug("Running in IsSignAgreement end!");
	}

}
