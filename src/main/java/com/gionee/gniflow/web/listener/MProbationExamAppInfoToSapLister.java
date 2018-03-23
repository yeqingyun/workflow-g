package com.gionee.gniflow.web.listener;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.gionee.gnif.mail.biz.model.MailSender;
import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.dto.HrUserDto;
import com.gionee.gniflow.sap.SapUtil;
import com.gionee.gniflow.web.support.AudiException;
//import com.gionee.gniflow.web.notice.SendEmailThread;
import com.gionee.gniflow.web.util.SendMailTool;
import com.gionee.gniflow.web.util.SpringTools;


public class MProbationExamAppInfoToSapLister implements ExecutionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4554160919252582345L;
	private Logger logger = LoggerFactory.getLogger(MProbationExamAppInfoToSapLister.class);
	private ProcessHelpService processHelpService;
	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		try {
		logger.debug("Running in SapParameterPassLister begin!");
		processHelpService = (ProcessHelpService) SpringTools.getBean(ProcessHelpService.class);

		RuntimeService runtimeService = execution.getEngineServices().getRuntimeService();

		//获取员工转正的信息
		String oprateReason  = (String)runtimeService.getVariable(execution.getId(), "zhuanzhengOption");
		String empGroup  = (String)runtimeService.getVariable(execution.getId(), "empGroup");
		//获取审批后的工资项
		//人力总监
		String empno  = (String)runtimeService.getVariable(execution.getId(), "empNo");
		
		String zhuanzhengOrJinsheng  = (String)runtimeService.getVariable(execution.getId(), "zhuanzhengOrJinsheng");
		
		String hrManagerisAujustSalary  = (String)runtimeService.getVariable(execution.getId(), "hrManagerisAujustOpt");
		String hrManagerMonth  = (String)runtimeService.getVariable(execution.getId(), "hrManagerMonth");
		String hrManagerfuSalaryGroup  = (String)runtimeService.getVariable(execution.getId(), "hrManagerfuSalaryGroup");
		
		//总经理
		String ceoisAujustSalary  = (String)runtimeService.getVariable(execution.getId(), "ceoisAujustOpt");
		String ceoMonth  = (String)runtimeService.getVariable(execution.getId(), "ceoMonth");
		String ceofuSalaryGroup  = (String)runtimeService.getVariable(execution.getId(), "ceofuSalaryGroup");
		
//		String finalisAujustSalary="";
		String finalMonth="";
		String finalfuSalaryGroup="";
		boolean isWriteToSap=false;
		if(ceoisAujustSalary!=null&&!"".equals(ceoisAujustSalary.trim())){
			if("1".equals(ceoisAujustSalary.trim())||"2".equals(ceoisAujustSalary.trim())||"3".equals(ceoisAujustSalary.trim())){
			isWriteToSap=true;
			finalMonth=ceoMonth;
			finalfuSalaryGroup=ceofuSalaryGroup;
				}else{
					isWriteToSap=false;	
				}
		}else if(ceoisAujustSalary==null||"".equals(ceoisAujustSalary.trim())){
			if(hrManagerisAujustSalary!=null&&("1".equals(hrManagerisAujustSalary.trim())||"2".equals(hrManagerisAujustSalary.trim())||"3".equals(hrManagerisAujustSalary.trim()))){
				isWriteToSap=true;
				finalMonth=hrManagerMonth;
				finalfuSalaryGroup=hrManagerfuSalaryGroup;
			}
		}
		

		
		//将finalMonth组成SAP日期格式yyyyddmm
		//如果试用期转正审批通过，需将转正结果写入SAP
		if(isWriteToSap){
		String tempstr=finalMonth;
		if(finalMonth!=null&&!"".equals(finalMonth.trim())){
		finalMonth=tempstr.substring(0, 4)+tempstr.substring(5, 7)+"01";
		}
		if(zhuanzhengOrJinsheng!=null&&!"".equals(zhuanzhengOrJinsheng.trim())){
			
			String text="";
			if("1".equals(zhuanzhengOrJinsheng.trim())){
				text="转正申请";	
			}
			else  if("2".equals(zhuanzhengOrJinsheng.trim())){
				text="晋升申请";	
			}else  if("3".equals(zhuanzhengOrJinsheng.trim())){
				text="岗位异动";	
			}
				
			Map<String,String> map = new HashMap<String,String>();
			map.put("P_PERNR",empno);
			map.put("P_DATE",finalMonth);
			map.put("P_MASSG",oprateReason);
			map.put("P_PERSG","A");
//			map.put("P_PERSG",empGroup);
			
			String funName="ZHR_OA09";
			 
			SapUtil sap=new SapUtil();
			String zhuanzhengmessage="";
			if(zhuanzhengOrJinsheng!=null&&!"".equals(zhuanzhengOrJinsheng.trim())){
				if("1".equals(zhuanzhengOrJinsheng.trim())){
					//转正考核需要将转正信息写入SAP
			
			zhuanzhengmessage=sap.writeToSap(funName,map);//ZHR_OA09为写入转正结果
			if(!"数据已成功写入SAP-HR系统!".equals(zhuanzhengmessage)){
				throw new AudiException("SAP保存数据反馈信息："+zhuanzhengmessage);
			}
			logger.debug("Running in SapParameterPassLister_ZHR_OA09 end!");
			
			
			//转正信息写入SAP后SAP返回信息邮件反馈给流程发起人
			//com.gionee.gnif.mail.biz.model.MailSender mailSender = (MailSender) SpringTools.getBean(MailSender.class);
		    String to="";
		    String subject="";
		    String content="";
		    String startUserId =(String) runtimeService.getVariable(execution.getId(), "applyUserId");
		    String mailTo = null;
		    mailTo=startUserId;
		    to = processHelpService.getUserEmailAddress(mailTo);
		    subject = "试用期转正考核申请流程结束时写入SAP成功!";
		    content = "员工"+empno+text+"转正结果写入SAP，SAP返回结果信息："+zhuanzhengmessage;

		    if (!StringUtils.isEmpty(to)) {
		    	//new SendEmailThread(to, subject, content).start();
		    	SendMailTool.sendMailByAddress(subject, content, to);
		   }
		  //-------------SAP返回信息邮件反馈给流程发起人
			  //发邮件给当事人
		    String toEmp="";
		    String subjectEmp="";
		    String contentEmp="";
		    String empId =(String) runtimeService.getVariable(execution.getId(), "empNo");
		    String empaccount="";
		    empaccount= processHelpService.getEmpAccount(empId);
		    String mailToEmp = null;
		    mailToEmp=empaccount;
		    toEmp = processHelpService.getUserEmailAddress(mailToEmp);
			if("1".equals(zhuanzhengOrJinsheng.trim())){
				subjectEmp = "转正通知";
				contentEmp ="Dear&nbsp;&nbsp;&nbsp;&nbsp;"+mailToEmp+":</br>"
					+"您好！恭喜您通过公司的试用期转正考核。</br>特此通知您从"
					+finalMonth
					+"日起，成为公司正式员工，并按公司相关规定享有正式员工的福利待遇，望您在工作岗位上再接再厉，迈向新的成功。";
			}else if("2".equals(zhuanzhengOrJinsheng.trim())){
				subjectEmp = "晋升通知";
				contentEmp ="Dear&nbsp;&nbsp;&nbsp;&nbsp;"+mailToEmp+":</br>"
					+"您好！恭喜您通过公司晋升考核期的考核评估。</br>"
					+"特此通知您从"
					+finalMonth
					+ "起，正式晋升为"
					+ finalfuSalaryGroup
					+ "，望继续发挥您的才干，为公司发展作出更大贡献。";
			}
			
		    
		    if (!StringUtils.isEmpty(toEmp)) {
		    	SendMailTool.sendMailByAddress(subjectEmp, contentEmp, toEmp);
	    	}
			}
			}
		  }
		} 
		} catch (Exception e) {
			logger.error(e.getMessage());
			//SAP返回信息邮件反馈给流程发起人
			RuntimeService runtimeService = execution.getEngineServices().getRuntimeService();
		    String to="";
		    String subject="";
		    String content="";
		    String startUserId =(String) runtimeService.getVariable(execution.getId(), "applyUserId");
		    String empno  = (String)runtimeService.getVariable(execution.getId(), "empNo");
		    String empName  = (String)runtimeService.getVariable(execution.getId(), "empName");
			String zhuanzhengOrJinsheng  = (String)runtimeService.getVariable(execution.getId(), "zhuanzhengOrJinsheng");
			
			if(zhuanzhengOrJinsheng!=null&&!"".equals(zhuanzhengOrJinsheng.trim())){
				subject = "试用期转正考核申请流程结束时写入SAP失败";
				String processInstanceId = execution.getProcessInstanceId();
				//给流程发起人发送Sap写入失败邮件
			    HrUserDto hrUserDto = processHelpService.getHrUser4Account(startUserId);
			    String startUserName  = hrUserDto.getName();
			    to = hrUserDto.getEmail();
				content = "<p>"+startUserName+"，您好:</p><p>您发起的试用期转正考核申请流程（流程编号："+processInstanceId+";员工编号："+empno+";员工姓名："+empName+"）写入Sap失败。"+e.getMessage()+"</p><p>请及时联系人力资源的同事进行手工处理。</p><p>此邮件由<a href='http://flow.gionee.com' target='_blank'>Gionee工作流平台</a>自动生成，请勿回复此邮件!</p> ";
				if (!StringUtils.isEmpty(to)) {
					SendMailTool.sendMailByAddress(subject, content, to);
				}
//				//给sap操作人员发送失败邮件
				String sapOperator = "01036314";//杨永秀
				hrUserDto = processHelpService.getEmpInfoById(sapOperator).get(0);
			    startUserName  = hrUserDto.getName();
			    to = hrUserDto.getEmail();
				content = "<p>"+startUserName+"，您好:</p><p>您参与的试用期转正考核申请流程（流程编号："+processInstanceId+";员工编号："+empno+";员工姓名："+empName+"）写入Sap失败。"+e.getMessage()+"</p><p>请及时联系SAP的同事进行处理。</p><p>此邮件由<a href='http://flow.gionee.com' target='_blank'>Gionee工作流平台</a>自动生成，请勿回复此邮件!</p> ";
			    SendMailTool.sendMailByAddress(subject, content, to);
			}
		}
		
	}

}
