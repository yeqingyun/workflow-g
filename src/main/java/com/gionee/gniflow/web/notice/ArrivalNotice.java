package com.gionee.gniflow.web.notice;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.el.ExpressionManager;
import org.activiti.engine.impl.form.FormEngine;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.IdentityLink;
import org.activiti.spring.SpringConfigurationHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.gionee.gnif.dto.QueryMap;
//import com.gionee.gnif.mail.biz.model.MailSender;
import com.gionee.gnif.util.PropertiesConfig;
import com.gionee.gniflow.biz.service.BpmConfNodeSendService;
import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.constant.BPMConstant;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.dto.BpmConfNodeSendDto;
import com.gionee.gniflow.util.FreemarkerFormEngine;
import com.gionee.gniflow.util.MapVariableScope;
import com.gionee.gniflow.web.util.PropertyHolder;
import com.gionee.gniflow.web.util.SendMailTool;
import com.gionee.gniflow.web.util.SendMessageUtil;
import com.gionee.gniflow.web.util.SpringTools;

public class ArrivalNotice {
	
	private ProcessHelpService processHelpService;
	
	private BpmConfNodeSendService bpmConfNodeSendService;
	
	private RepositoryService repositoryService; 
	
	private ExpressionManager expressionManager;
	
    public void process(DelegateTask delegateTask) {
    	
    	initService();//init
    	
    	expressionManager = Context.getProcessEngineConfiguration().getExpressionManager();
    	
        //给流程发起人发送邮件
        HistoricProcessInstanceEntity historicProcessInstanceEntity = Context
                .getCommandContext()
                .getHistoricProcessInstanceEntityManager()
                .findHistoricProcessInstance(
                        delegateTask.getProcessInstanceId());
        
//        ProcessDefinitionEntity processDefinitionEntity = Context
//        		.getCommandContext()
//        		.getProcessDefinitionEntityManager()
//        		.findProcessDefinitionById(historicProcessInstanceEntity.getProcessDefinitionId());
        //if (processDefinitionEntity.getKey().equals(BPMConstant.SAP_REQ_PRO_DEF_KEY)) {
		//    	urlParam = "?title=我的待办任务&url=sapspace/approvalProcess.html&icon=icon-sapwaittask";
		//    } else {
		//    	urlParam = "?title=待办任务&url=workspace/personalTask.html&icon=icon-waittask";
		//    }
        
        String startUserId = historicProcessInstanceEntity.getStartUserId();
        
        //获取启动流程的id及下节点的任务名
        String processDefinitionId=delegateTask.getProcessDefinitionId();
        String taskDefinitionKey=delegateTask.getTaskDefinitionKey();
        
        //查询任务邮件配置项
        QueryMap param = new QueryMap();
        param.put("processDefId", processDefinitionId);
        param.put("code", taskDefinitionKey);
        param.put("sendTypes", Arrays.asList(new Integer[]{BPMConstant.MAIL_SEND_TYPE_TASK_ARRIVAL_STARTER,
        		BPMConstant.MAIL_SEND_TYPE_TASK_ARRIVAL_TRANSACTOR}));
        List<BpmConfNodeSendDto> confNodeSendDtoList = bpmConfNodeSendService.joinQueryBpmConfNodeSend(param);
        if (!CollectionUtils.isEmpty(confNodeSendDtoList)) {
        	for (BpmConfNodeSendDto bpmConfNodeSendDto : confNodeSendDtoList) {
        		if (BPMConstant.MAIL_SEND_FLAG_YES == bpmConfNodeSendDto.getSendMailFlag()) {
        			if (bpmConfNodeSendDto.getSendType() == BPMConstant.MAIL_SEND_TYPE_TASK_ARRIVAL_STARTER) {
        				if(isOffConcurrentPoint(processDefinitionId,taskDefinitionKey)){
        					
        				}else{
        					this.handleStarterEmail(bpmConfNodeSendDto, delegateTask, startUserId);
        				}
        			}
        			if (bpmConfNodeSendDto.getSendType() == BPMConstant.MAIL_SEND_TYPE_TASK_ARRIVAL_TRANSACTOR) {
        				this.handleAssignEmail(bpmConfNodeSendDto, delegateTask);
        			}
        		}
        	}
        }
    }
    
    /**
     * 初始化服务
     */
    private void initService(){
    	processHelpService = (ProcessHelpService) SpringTools.getBean(ProcessHelpService.class);
    	bpmConfNodeSendService = (BpmConfNodeSendService) SpringTools.getBean(BpmConfNodeSendService.class);
    	repositoryService = Context.getExecutionContext().getExecution().getEngineServices().getRepositoryService();
    }
    
    /**
     * 处理发给流程发起人的邮件
     */
    private void handleStarterEmail(BpmConfNodeSendDto bpmConfNodeSendDto, DelegateTask delegateTask, String startUserId){
    	this.sendMail(bpmConfNodeSendDto, delegateTask, true, startUserId);
    }
    
    /**
     * 处理发给任务处理人的邮件
     */
    private void handleAssignEmail(BpmConfNodeSendDto bpmConfNodeSendDto, DelegateTask delegateTask){
    	if (!delegateTask.getCandidates().isEmpty()) {
    		Set<IdentityLink> links = delegateTask.getCandidates();
        	for(IdentityLink link : links) {
        		this.sendMail(bpmConfNodeSendDto, delegateTask, false, link.getUserId());
        	}
    	} else {
    		this.sendMail(bpmConfNodeSendDto, delegateTask, false, delegateTask.getAssignee());
    	}

    }
    
    /**
     * 获取邮件的必要项
     * @param bpmConfNodeSendDto
     * @param delegateTask
     * @return
     */
    private void sendMail(BpmConfNodeSendDto bpmConfNodeSendDto, DelegateTask delegateTask,
    		boolean isSendStarter, String userAccount){
    	
    	MapVariableScope mapVariableScope = new MapVariableScope();
    	mapVariableScope.setVariable("_taskId", delegateTask.getId());
		mapVariableScope.setVariable("_assignee", processHelpService.getUserName(userAccount));
		mapVariableScope.setVariable("_taskName", delegateTask.getName());
		mapVariableScope.setVariable("_processInstanceId", delegateTask.getProcessInstanceId());
		mapVariableScope.setVariable("_taskAddreess", PropertyHolder.getContextProperty(WebReqConstant.FLOW_TASK_HAND_URL));
        ProcessDefinition proDef = repositoryService.createProcessDefinitionQuery()
        		.processDefinitionId(delegateTask.getProcessDefinitionId()).singleResult();
        if (proDef != null) {
        	mapVariableScope.setVariable("_processDefName", proDef.getName());
        }
        
        if (isSendStarter) {
        	mapVariableScope.setVariable("_initiator", processHelpService.getUserName(userAccount));
        }
        
//        String to = "receiver_test@163.com";
        String to = null;
		try {
			to = processHelpService.getUserEmailAddress(userAccount);
		} catch (Exception e) {
			to = null;
			e.printStackTrace();
		}
        
        String subject = expressionManager.createExpression(bpmConfNodeSendDto.getTemplateSubject())
        		.getValue(mapVariableScope).toString();
        String content = expressionManager.createExpression(bpmConfNodeSendDto.getTemplateContent())
        		.getValue(mapVariableScope).toString();
        
//        new SendEmailThread(to, subject, content).start();
        //正式环境使用
        /*com.gionee.gnif.mail.biz.model.MailSender mailSender = (MailSender) SpringTools.getBean(MailSender.class);
        if (!StringUtils.isEmpty(to)) {
        	mailSender.sendMail(to, subject, content);
        }*/
        
        if (!StringUtils.isEmpty(to)) {
        	SendMailTool.sendMailByAddress(subject, content, to);
        }
        
        String isSendMsg = PropertiesConfig.getString("message.enable");
        if(isSendMsg != null && isSendMsg.equals("true")) {  //只在正式环境发送短信，本地、测试环境不发送短信
	        //量产流checklist签核单,量产软件评审表给任务接收者发送短信
	        if((proDef.getName().equals("量产流checklist签核单") || proDef.getName().equals("量产软件评审表")|| proDef.getName().equals("流程文档会签")) && subject.equals("您有新任务需要处理")) {
    			String msgcontent = "尊敬的同事，您好：您有新任务需要处理，流程实例编号：" + delegateTask.getProcessInstanceId() + "，流程名称：" + proDef.getName() + "，任务名称：" 
    					+ delegateTask.getName() + "。请登录Gionee工作流平台办理，任务办理地址：http://flow.gionee.com，"
    					+ "此短信由Gionee工作流平台自动发出，请勿回复，祝工作愉快，谢谢！";
    			String mobile = processHelpService.getUserMobileNumber(userAccount);
    			if(mobile != null && !mobile.equals("")) {
    				SendMessageUtil.sendMessage(msgcontent, mobile);
    			}
	        }
        }
        
    }
    /**
     * 判断是否是文档会签的四级并行节点
     * @param processName
     * @param pointName
     * @return
     */
    private boolean isOffConcurrentPoint(String processName,String pointName){
    	boolean flag=false;
    	if(processName.indexOf("L-Project-Human-Input-Account") !=-1){
    		if("SystemFirstTask".equals(pointName) || "SystemSecondTask".equals(pointName) || "SystemThreeTask".equals(pointName) ||
    				"SystemDrivePeripheralsTask".equals(pointName) || "SystemModemTask".equals(pointName) || "SystemToolTask".equals(pointName) ||
    				"SystemSoftTask".equals(pointName) || "SystemStewardTask".equals(pointName) || "ScienceFirstTask".equals(pointName) || 
    				"ScienceSecondMultiInstTask".equals(pointName) || "ImageEffectTask".equals(pointName) || "ImageSoftTask".equals(pointName) || 
    				"TestProjectTask".equals(pointName) || "TestPlatformTask".equals(pointName) || "ProjMageSoftProjTask".equals(pointName) || 
    				"ProjMageSoftQualityTask".equals(pointName) || "ClientDeveTask".equals(pointName) || "ProductDeveThreeMultiInstTask".equals(pointName) || 
    				"ProductDeveTwoTask".equals(pointName) || "ProductDeveOneTask".equals(pointName) || "BaseRomSoftFirstTask".equals(pointName) || 
    				"BaseRomSoftSecondTask".equals(pointName) || "BaseRomProductTask".equals(pointName) || "ProductLifeTask".equals(pointName) || 
    				"ProductAmigoTask".equals(pointName) || "VisualProductTask".equals(pointName) || "ProductGameTask".equals(pointName) || 
    				"ProductPlanLockTask".equals(pointName) || "SecondProductVadeoTask".equals(pointName) || "UserInteractTask".equals(pointName) || 
    				"UserExperienceTask".equals(pointName) || "ComCenterTask".equals(pointName) || "BigDataStateTask".equals(pointName) || 
    				"DataPayAndSetTask".equals(pointName) || "IPRTask".equals(pointName) || "otherDeptMultiInstTask".equals(pointName)){
    			flag=true;
    		}
    	}
    	return flag;
    }
}
