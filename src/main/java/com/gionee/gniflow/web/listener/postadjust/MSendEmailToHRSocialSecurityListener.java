package com.gionee.gniflow.web.listener.postadjust;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gnif.GnifException;
import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.dto.HrUserDto;
import com.gionee.gniflow.web.util.SendMailTool;
//import com.gionee.gniflow.web.notice.SendEmailThread;
import com.gionee.gniflow.web.util.SpringTools;

/**
 * 岗位调整后发邮件提醒社保专员
 * @author Administrator
 *
 */
public class MSendEmailToHRSocialSecurityListener implements ExecutionListener{
	private Logger logger = LoggerFactory.getLogger(MSendEmailToHRSocialSecurityListener.class);
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
		String oprationType = (String)runtimeService.getVariable(execution.getId(), "oprationTypevalue");//获取操作类型
		if(oprationType.equals("A5")){
			
				//获取调岗人信息
				String changeUserId = (String)runtimeService.getVariable(execution.getId(), "account");//岗位调整人员
				String empAccount = processHelpService.getEmpAccount(changeUserId);
				if(empAccount==null || "".equals(empAccount)){
					return;
				}
				HrUserDto user = processHelpService.getHrUser4Account(empAccount);//通过员工账号获取员工信息
				String changeUserName = user.getName();//获取用户名
				String changeIdCard = user.getIdCard();//获取用户身份证号
				
				//获取发起人信息
				String applyUserId = (String)runtimeService.getVariable(execution.getId(), "applyUserId");//发起人员工编号
				String applyUserName = processHelpService.getUserName(applyUserId);	//发起人姓名
				
				String company = (String)runtimeService.getVariable(execution.getId(), "companyName");//岗位调整前的公司名称
				String newCompanyStart = (String)runtimeService.getVariable(execution.getId(), "showcomname");//调整后的公司名称
				String dept = (String)runtimeService.getVariable(execution.getId(), "orgName");//岗位调整前的部门名称
				String newDeptStart = (String)runtimeService.getVariable(execution.getId(), "accepterOrgname");//调整后的部门名称
				String job = (String)runtimeService.getVariable(execution.getId(), "job");//岗位调整前的岗位名称
				String newJobStart = (String)runtimeService.getVariable(execution.getId(), "job4");//调整后的岗位名称
				String processId = execution.getProcessInstanceId();//获取流程Id
				String changeDate = (String)runtimeService.getVariable(execution.getId(), "hrleaderCheckenableTime");//岗位调整批准生效月份
   
				String newCompanyStaff = (String)runtimeService.getVariable(execution.getId(), "updateCompanyNameStaff");//确认员工调整后的公司名称
				String newCompanyOfficeWorker = (String)runtimeService.getVariable(execution.getId(), "updateCompanyNameOfficeWorker");//确认职员调整后的公司名称
				String newDeptStaff = (String)runtimeService.getVariable(execution.getId(), "updateOrgNameStaff");//确认员工调整后的部门名称
				String newDeptOfficeWorker = (String)runtimeService.getVariable(execution.getId(), "updateOrgNameOfficeWorker");//确认职员调整后的部门名称
				String newJobStaff = (String)runtimeService.getVariable(execution.getId(), "updateJobNameStaff");//确认员工调整后的岗位名称
				String newJobOfficeWorker = (String)runtimeService.getVariable(execution.getId(), "updateJobNameOfficeWorker");//确认职员调整后的岗位名称
				
				String newCompany = null;
				String newDept = null;
				String newJob = null;
				
				//判断调整后公司信息
				if(null==newCompanyOfficeWorker){
					if("".equals(newCompanyStaff)||null==newCompanyStaff){
						newCompany = newCompanyStart;
						newDept = newDeptStart;
						newJob = newJobStart;
						
					}else {
						newCompany = newCompanyStaff;
						newDept = newDeptStaff;
						newJob = newJobStaff;
					}
				}
				if(null==newCompanyStaff){
					if("".equals(newCompanyOfficeWorker)||null==newCompanyOfficeWorker){
						newCompany = newCompanyStart;
						newDept = newDeptStart;
						newJob = newJobStart;
					}else{
						newCompany = newCompanyOfficeWorker;
						newDept = newDeptOfficeWorker;
						newJob = newJobOfficeWorker;
					}
				}
				
				//发邮件给社保专员
				String toEmp="guchunxiu@gionee.com";
				 //  String toEmp=" zhouxiaopei@gionee.com";
			    String subjectEmp="";//邮件名称    
			    String contentEmp="";//邮件内容
			   // String processId="";//流程ID
			    //String processName="";//流程名
			   // String applyUserId="00001474";
			  
			   // String empaccount="";//获取发起人员工编号
			   //empaccount= processHelpService.getEmpAccount(applyUserId);
			   //String mailToEmp = null;//获取发起人员工姓名
			   //mailToEmp=empaccount;
			   // toEmp = processHelpService.getUserEmailAddress(mailToEmp);//获取用户的邮件地址
				subjectEmp = "跨公司岗位调整通知";
				
				//姓名 员工编码  身份证号  调动日期  原公司 原部门 原岗位  现公司 现部门  现岗位
				contentEmp ="社保专员:</br>"
						+"您好！</br>"
						+"由员工编号：【"+applyUserId+"】,姓名：【"+applyUserName+"】发起流程实例编号为:【"+processId+"】的跨公司岗位调整申请流程</br>"
						+"将员工编号：【"+changeUserId+"】,姓名：【"+changeUserName+"】,身份证号:【"+changeIdCard+"】</br>"
						+ "由原来的公司：【"+company+"】，部门：【"+dept+"】，岗位：【"+job+"】，调整到</br>"
						+ "新的公司：【"+newCompany+"】，部门：【"+newDept+"】，岗位：【"+newJob+"】</br>"
						+"拟生效日期为：【"+changeDate+ "月】，的流程已审批完毕,请注意变更社保信息！</br>"
						+ "此邮件由 Gionee工作流平台(网址：flow.gionee.com) 自动生成，请勿回复此邮件!感谢您的配合！</br>"
						+ "祝您工作顺利，生活愉快！";
				
			    if (!StringUtils.isEmpty(toEmp)) {
			    	//new SendEmailThread(toEmp, subjectEmp, contentEmp).start();
			    	SendMailTool.sendMailByAddress(subjectEmp, contentEmp, toEmp);
			   }
				//-------------发邮件给当事人
		}/*else{
			try{
				System.out.println("不发送邮件");
			}catch(Exception e){
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new GnifException("Failure of send Email to HR!");
			}
		}*/
		logger.debug("Running in SendEmailToApplyUser end!");
		
		
	}
}
