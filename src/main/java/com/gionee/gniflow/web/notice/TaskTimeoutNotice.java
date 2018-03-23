/**
 * @(#) TaskTimeoutNotice.java Created on 2014年11月19日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.notice;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.el.ExpressionManager;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.gionee.gnif.dto.QueryMap;
//import com.gionee.gnif.mail.biz.model.MailSender;
import com.gionee.gniflow.biz.service.BpmConfNodeSendService;
import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.constant.BPMConstant;
import com.gionee.gniflow.dto.BpmConfNodeSendDto;
import com.gionee.gniflow.util.MapVariableScope;
import com.gionee.gniflow.web.util.SendMailTool;
import com.gionee.gniflow.web.util.SpringTools;

/**
 * The class <code>TaskTimeoutNotice</code>
 * 
 * @author lipw
 * @version 1.0
 */
public class TaskTimeoutNotice {
	
	private static Logger logger = LoggerFactory.getLogger(TaskTimeoutNotice.class);
	
	private BpmConfNodeSendService bpmConfNodeSendService;
	
	private ProcessHelpService processHelpService;
	
	private ExpressionManager expressionManager;
	

	public void process(DelegateTask delegateTask) {
		initService();
		
		String taskDefinitionKey = delegateTask.getTaskDefinitionKey();
		String processDefinitionId = delegateTask.getProcessDefinitionId();

		//查询任务邮件配置项
        QueryMap param = new QueryMap();
        param.put("processDefId", processDefinitionId);
        param.put("code", taskDefinitionKey);
        param.put("sendTypes", Arrays.asList(new Integer[]{BPMConstant.MAIL_SEND_TYPE_TASK_TIMEOUT}));
        
        List<BpmConfNodeSendDto> confNodeSendDtoList = bpmConfNodeSendService.joinQueryBpmConfNodeSend(param);
        if (!CollectionUtils.isEmpty(confNodeSendDtoList)) {
			for (BpmConfNodeSendDto bpmConfNodeSendDto : confNodeSendDtoList) {
				if (bpmConfNodeSendDto.getSendMailFlag() == BPMConstant.MAIL_SEND_FLAG_YES) {
					processTimeout(delegateTask, bpmConfNodeSendDto);
				}
			}
		}
	}
	
	public void processTimeout(DelegateTask delegateTask,BpmConfNodeSendDto bpmConfNodeSendDto) {
        try {
            Date dueDate = delegateTask.getDueDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dueDate);
            
            //获取表配置的时间
            if (StringUtils.isNotEmpty(bpmConfNodeSendDto.getNodeDueDate())) {
            	DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
                Duration duration = datatypeFactory.newDuration("-"
                        + bpmConfNodeSendDto.getNodeDueDate());
                duration.addTo(calendar);
            }

            Date noticeDate = calendar.getTime();
            Date now = new Date();
            //提前一个小时
            if ((now.getTime() < noticeDate.getTime())
                    && ((noticeDate.getTime() - now.getTime()) < (60 * 1000))) {
            	
            	MapVariableScope mapVariableScope = new MapVariableScope();
            	mapVariableScope.setVariable("_taskId", delegateTask.getId());
            	mapVariableScope.setVariable("_taskName", delegateTask.getName());
            	mapVariableScope.setVariable("_assignee", processHelpService.getUserName(delegateTask.getAssignee()));

                expressionManager = Context.getProcessEngineConfiguration().getExpressionManager();

//              String to = "receiver_test@163.com";
                String to = processHelpService.getUserEmailAddress(delegateTask.getAssignee());
                
                String subject = expressionManager.createExpression(bpmConfNodeSendDto.getTemplateSubject())
                		.getValue(mapVariableScope).toString();
                String content = expressionManager.createExpression(bpmConfNodeSendDto.getTemplateContent())
                		.getValue(mapVariableScope).toString();
                
//                new SendEmailThread(to, subject, content).start();
                //正式环境使用
                //com.gionee.gnif.mail.biz.model.MailSender mailSender = (MailSender) SpringTools.getBean(MailSender.class);
                if (!StringUtils.isEmpty(to)) {
                	//mailSender.sendMail(to, subject, content);
                	SendMailTool.sendMailByAddress(subject, content, to);
                }
                
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
	
	//init
	private void initService(){
		bpmConfNodeSendService = (BpmConfNodeSendService) SpringTools.getBean(BpmConfNodeSendService.class);
		processHelpService = (ProcessHelpService) SpringTools.getBean(ProcessHelpService.class);
	}
}
