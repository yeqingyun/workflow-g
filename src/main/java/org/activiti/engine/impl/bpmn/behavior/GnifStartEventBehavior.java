/**
 * @(#) GnifStartEventBehavior.java Created on 2014年12月8日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package org.activiti.engine.impl.bpmn.behavior;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.el.ExpressionManager;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.util.AppContext;
import com.gionee.gniflow.biz.model.BpmConfNodeEmail;
import com.gionee.gniflow.biz.model.BpmProcessRun;
import com.gionee.gniflow.biz.service.BpmConfNodeEmailService;
import com.gionee.gniflow.biz.service.BpmProcessRunService;
import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.util.MapVariableScope;
import com.gionee.gniflow.web.util.PropertyHolder;
import com.gionee.gniflow.web.util.SendMailTool;
import com.gionee.gniflow.web.util.SpringTools;

/**
 * The class <code>GnifStartEventBehavior</code>
 * 
 * @author lipw
 * @version 1.0
 */
public class GnifStartEventBehavior extends NoneStartEventActivityBehavior {
	private static final Logger logger = LoggerFactory.getLogger(GnifStartEventBehavior.class);

	private static final long serialVersionUID = 7535669125930442261L;

	private BpmProcessRunService bpmProcessRunService;
	private RepositoryService repositoryService;
	private BpmConfNodeEmailService bpmConfNodeEmailService;
	private ProcessHelpService processHelpService;
	private ExpressionManager expressionManager;
	private RuntimeService runtimeService;
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.activiti.engine.impl.bpmn.behavior.FlowNodeActivityBehavior#execute
	 * (org.activiti.engine.impl.pvm.delegate.ActivityExecution)
	 */
	@Override
	public void execute(ActivityExecution execution) throws Exception {
		// logic
		bpmProcessRunService = (BpmProcessRunService) SpringTools
				.getBean(BpmProcessRunService.class);

		bpmProcessRunService = (BpmProcessRunService) SpringTools.getBean(BpmProcessRunService.class);
		repositoryService = (RepositoryService) SpringTools.getBean(RepositoryService.class);
		bpmConfNodeEmailService = (BpmConfNodeEmailService) SpringTools.getBean(BpmConfNodeEmailService.class);
		processHelpService = (ProcessHelpService) SpringTools.getBean(ProcessHelpService.class);
		runtimeService = execution.getEngineServices().getRuntimeService();
		//记录流程状态
		BpmProcessRun bpmProcessRun = new BpmProcessRun();
		bpmProcessRun.setProcessDefKey(execution.getProcessDefinitionId());
		bpmProcessRun.setProcessInstId(execution.getProcessInstanceId());
		bpmProcessRun.setStartTime(new Date());
		bpmProcessRun.setStatus(BpmProcessRun.STATUS_RUN);
		bpmProcessRun.setReason("流程正常启动");
		bpmProcessRun.setStartUserAccount(AppContext.getCurrentAccount());
		bpmProcessRun.setStartUserName(AppContext.getCurrentAppUser().getName());
		
		if(execution.getVariable("processName")!=null&&!"".equals(execution.getVariable("processName").toString().trim())){
			bpmProcessRun.setProcessInstName(execution.getVariable("processName").toString().trim());	
		}
		bpmProcessRunService.save(bpmProcessRun);
		
		super.execute(execution);
		//send Email
		ProcessDefinition proDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(execution.getProcessDefinitionId()).singleResult();
		QueryMap queryMap = new QueryMap();
		queryMap.put("processDefKey", proDefinition.getKey());
		queryMap.put("taskDefKey", "startevent1");
		List<BpmConfNodeEmail> nodeMails = bpmConfNodeEmailService.query(queryMap);
		
		
	    expressionManager = Context.getProcessEngineConfiguration().getExpressionManager();
	    Map<String, Object> var = runtimeService.getVariables(execution.getId());
		MapVariableScope mapVariableScope = new MapVariableScope();
		mapVariableScope.setVariables(var);
		mapVariableScope.setVariableLocal("execution", execution);
		
		
		Object startUserObject = var.get("applyUserId");
		if (!CollectionUtils.isEmpty(nodeMails) && startUserObject != null) {
			String startUserId = startUserObject.toString();
			for (BpmConfNodeEmail nodeEmail : nodeMails) {
				String mailTo = null;
				if (nodeEmail.getAddressee().equals("#{startUserId}")) {
					mailTo = startUserId;
					//发给流程发起人
					mapVariableScope.setVariableLocal("_startUser", processHelpService.getUserName(startUserId));
				} else {
					String receiver = String.valueOf(expressionManager.createExpression(nodeEmail.getAddressee()).getValue(mapVariableScope));
					mailTo  = receiver;
					mapVariableScope.setVariableLocal("_receiver", processHelpService.getUserName(receiver));
				}
				
				mapVariableScope.setVariableLocal("_startUserId", startUserId);
				mapVariableScope.setVariableLocal("_processInstanceId", execution.getProcessInstanceId());
				mapVariableScope.setVariableLocal("_processDefName", proDefinition.getName());
				
			    String to = processHelpService.getUserEmailAddress(mailTo);
			        
			    String subject = nodeEmail.getSubject();
			    String content = "";
			    try {
			    	//离职流程需要一张办理导图
			    	if(proDefinition.getKey().indexOf("L-Leave-Application")!=-1){
			    		String leaveGuideUrl = PropertyHolder.getContextProperty(WebReqConstant.LEAVE_GUIDE_EMAIL_IMAGE);
			    		mapVariableScope.setVariableLocal("_leaveGuideImage", leaveGuideUrl);
			    	}
			    	content = expressionManager.createExpression(nodeEmail.getContent()).getValue(mapVariableScope).toString();
				} catch (Exception e) {
					logger.error("[GnifStartEventBehavior]--->Error parseing UEL Expression for send email!");
					logger.error(e.getMessage());
				}
			    
			   if (!StringUtils.isEmpty(to)) {
				   SendMailTool.sendMailByAddress(subject, content, to);
			   }
		       
			}
		}
		
	}

}
