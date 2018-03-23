package com.gionee.gniflow.web.listener.LProbationExam;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.dto.HrUserDto;
import com.gionee.gniflow.sap.SapUtil;
import com.gionee.gniflow.web.support.AudiException;
import com.gionee.gniflow.web.util.SendMailTool;
import com.gionee.gniflow.web.util.SpringTools;

public class LProbationExamEmpInfoToSap implements ExecutionListener {
	
	private static final long serialVersionUID = -4554160102252582345L;
	private ProcessHelpService processHelpService;
	@Override
	public void notify(DelegateExecution execution) throws Exception {
			RuntimeService runtimeService = execution.getEngineServices().getRuntimeService();
			
			String empId  = (String)runtimeService.getVariable(execution.getId(), "applyUserAccount");//员工编号
			String examDate=(String)runtimeService.getVariable(execution.getId(), "wageManagerAgreeTime");//转正日期
			
			Map<String,String> map = new HashMap<String,String>();
			map.put("P_PERNR",empId);
			map.put("P_DATE",examDate);
			map.put("P_MASSG","11");
			map.put("P_PERSG","A");
			
			String funName="ZHR_OA09";
			 
			SapUtil sap=new SapUtil();
			
			String message="";
			
			message=sap.writeToSap(funName, map);//写入sap
			if(!"数据已成功写入SAP-HR系统!".equals(message)){
				String title = "金立试用期转正流程写入Sap出现错误";
				String processInstanceId = execution.getProcessInstanceId();
				processHelpService = (ProcessHelpService) SpringTools.getBean(ProcessHelpService.class);
				
				String sapOperator = "00000094";//梅花
				HrUserDto hrUserDto = processHelpService.getEmpInfoById(sapOperator).get(0);
			    String sapOperatorName  = hrUserDto.getName();
			    String to = hrUserDto.getEmail();
			    
				String content = "<p>"+sapOperatorName+"，您好:</p><p>您处理的金立试用期转正考核流程（流程编号："+processInstanceId+";员工编号："+empId+";转正日期："+examDate+"） 在写入Sap出现错误，错误信息为："+message+"</p><p>请及时联系SAP的同事进行处理。</p><p>此邮件由<a href='http://flow.gionee.com' target='_blank'>Gionee工作流平台</a>自动生成，请勿回复此邮件!</p> ";
				SendMailTool.sendMailByAddress(title, content.toString(), to);
				
				
				sapOperator = "00001938";//李丹
				hrUserDto = processHelpService.getEmpInfoById(sapOperator).get(0);
			    sapOperatorName  = hrUserDto.getName();
			    to = hrUserDto.getEmail();
				content = "<p>"+sapOperatorName+"，您好:</p><p>您处理的金立试用期转正考核流程（流程编号："+processInstanceId+";员工编号："+empId+";转正日期："+examDate+"） 在写入Sap出现错误，错误信息为："+message+"</p><p>请及时联系SAP的同事进行处理。</p><p>此邮件由<a href='http://flow.gionee.com' target='_blank'>Gionee工作流平台</a>自动生成，请勿回复此邮件!</p> ";
				SendMailTool.sendMailByAddress(title, content.toString(), to);
				
			}
	}

}
