/**
 * @(#) GnifEenEventBehavior.java Created on 2014年12月8日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package org.activiti.engine.impl.bpmn.behavior;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.bpmn.behavior.NoneEndEventActivityBehavior;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.el.ExpressionManager;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.gionee.gnif.dto.QueryMap;
//import com.gionee.gnif.mail.biz.model.MailSender;
import com.gionee.gniflow.biz.model.BpmConfNodeEmail;
import com.gionee.gniflow.biz.model.BpmProcessRun;
import com.gionee.gniflow.biz.service.BpmConfNodeEmailService;
import com.gionee.gniflow.biz.service.BpmProcessRunService;
import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.constant.BPMConstant;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.util.MapVariableScope;
import com.gionee.gniflow.web.util.PropertyHolder;
import com.gionee.gniflow.web.util.SendMailTool;
import com.gionee.gniflow.web.util.SpringTools;

/**
 * The class <code>GnifEenEventBehavior</code>
 * 
 * @author lipw
 * @version 1.0
 */
public class GnifEndEventBehavior extends NoneEndEventActivityBehavior {

	private static final long serialVersionUID = -4410482551305690298L;
	
	private static final Logger logger = LoggerFactory.getLogger(GnifEndEventBehavior.class);
	
	private static final String EMAIL_TEMPLATE_PREFIX_PASS = "审批通过";
	private static final String EMAIL_TEMPLATE_PREFIX_NOPASS = "审批未通过";
	
	private BpmProcessRunService bpmProcessRunService;
	
	private ProcessHelpService processHelpService;
	
	private RepositoryService repositoryService;
	
	private RuntimeService runtimeService;
	
	private HistoryService historyService;
	
	private BpmConfNodeEmailService bpmConfNodeEmailService;
	
	private ExpressionManager expressionManager;
	
	
	/* (non-Javadoc)
	 * @see org.activiti.engine.impl.bpmn.behavior.NoneEndEventActivityBehavior#execute(org.activiti.engine.impl.pvm.delegate.ActivityExecution)
	 */
	@Override
	public void execute(ActivityExecution execution) throws Exception {
		logger.debug("--------------------Running in GnifEndEventBehavior Begin ----------------------");
		
		//do super
		super.execute(execution);
		
        logger.debug("execution.getId : {}", execution.getId());
        logger.debug("execution.getProcessDefinitionId : {}", execution.getProcessDefinitionId());
        logger.debug("execution.getProcessInstanceId : {}", execution.getProcessInstanceId());
		//record BpmProcessRun Data
        initService(execution);
        
        //判断流入EndEvent的Flow的条件是不是返回true
        expressionManager = Context.getProcessEngineConfiguration().getExpressionManager();
        Map<String, Object> var = runtimeService.getVariables(execution.getId());
    	MapVariableScope mapVariableScope = new MapVariableScope();
		mapVariableScope.setVariables(var);
		mapVariableScope.setVariableLocal("execution", execution);
		
        List<PvmTransition> pvmList = execution.getActivity().getIncomingTransitions();
        
        boolean reuslt = isAutiSuccessForEnd(pvmList, execution.getProcessInstanceId());
        BpmProcessRun bpmProcessRun = bpmProcessRunService.getBpmProcessRun(execution.getProcessInstanceId());
		if (bpmProcessRun != null) {
			bpmProcessRun.setEndTime(new Date());
			if (reuslt) {
				bpmProcessRun.setStatus(BpmProcessRun.STATUS_COMPLETED);
			} else {
				bpmProcessRun.setStatus(BpmProcessRun.STATUS_NO_APPRVAL_COMPLETED);
			}
		}
		bpmProcessRunService.save(bpmProcessRun);
		//record BpmProcessRun end
		
		//send Email
		ProcessDefinition proDefinition = repositoryService.createProcessDefinitionQuery()
				 .processDefinitionId(execution.getProcessDefinitionId()).singleResult();
		QueryMap queryMap = new QueryMap();
		queryMap.put("processDefKey", proDefinition.getKey());
		queryMap.put("taskDefKey", ((ExecutionEntity)execution).getActivityId());
		if (reuslt) {
			//审批通过
			queryMap.put("name", EMAIL_TEMPLATE_PREFIX_PASS);
		} else {
			//审批不通过
			queryMap.put("name", EMAIL_TEMPLATE_PREFIX_NOPASS);
		}
		List<BpmConfNodeEmail> nodeMails = bpmConfNodeEmailService.query(queryMap);
		
		String startUserId = processHelpService.getProcessStartUser(execution.getProcessInstanceId());
		
		if (!CollectionUtils.isEmpty(nodeMails)) {
			for (BpmConfNodeEmail nodeEmail : nodeMails) {
				String mailTo = null;
				if (nodeEmail.getAddressee().equals("#{startUserId}")) {
					mailTo = startUserId;
					//发给流程发起人
					mapVariableScope.setVariableLocal("_startUser", processHelpService.getUserName(startUserId));
				} else {
					String receiver = String.valueOf(expressionManager.createExpression(nodeEmail.getAddressee())
			        		.getValue(mapVariableScope));
					mailTo  = receiver;
					mapVariableScope.setVariableLocal("_receiver", processHelpService.getUserName(receiver));
				}
				//
				mapVariableScope.setVariableLocal("_startUserId", startUserId);
				mapVariableScope.setVariableLocal("_processInstanceId", execution.getProcessInstanceId());
				mapVariableScope.setVariableLocal("_processDefName", proDefinition.getName());
				
			    String to = processHelpService.getUserEmailAddress(mailTo);
			        
			    String subject = nodeEmail.getSubject();
			    String content = "";
			    try {
			    	content = expressionManager.createExpression(nodeEmail.getContent())
			        		.getValue(mapVariableScope).toString();
			    	//户口办理流程需要一张办理导图
			    	if(proDefinition.getKey().indexOf("L-Account-Handle")!=-1&&nodeEmail.getAddressee().equals("#{startUserId}")){
			    		String accountGuideUrl = PropertyHolder.getContextProperty(WebReqConstant.ACCOUNT_GUIDE_EMAIL_BGIMAGE);
			    		content+="<image src='"+accountGuideUrl+"'/>";
			    	}
				} catch (Exception e) {
					logger.error("[GnifEndEventBehavior]--->Error parseing UEL Expression for send email!");
					logger.error(e.getMessage());
				}
			    
			   if (!StringUtils.isEmpty(to)) {
				   SendMailTool.sendMailByAddress(subject, content, to);
			   }
		       
			}
		}
		
		logger.debug("--------------------Running in GnifEndEventBehavior end ----------------------");
	}

	/**
	 * 初始化服务
	 */
	private void initService(ActivityExecution execution){
		bpmProcessRunService = (BpmProcessRunService) SpringTools.getBean(BpmProcessRunService.class);
		processHelpService = (ProcessHelpService) SpringTools.getBean(ProcessHelpService.class);
		bpmConfNodeEmailService = (BpmConfNodeEmailService) SpringTools.getBean(BpmConfNodeEmailService.class);
		repositoryService = execution.getEngineServices().getRepositoryService();
		runtimeService = execution.getEngineServices().getRuntimeService();
		historyService = execution.getEngineServices().getHistoryService();
	}
	
	/**
	 * 判断是不是流程审批通过而结束
	 * @return
	 */
	private boolean isAutiSuccessForEnd(List<PvmTransition> pvmList, String processInstId){
		
		HistoricActivityInstance tempHisAct = null;
		
		List<HistoricActivityInstance> hisActivityList = historyService.createHistoricActivityInstanceQuery()
				 .processInstanceId(processInstId).unfinished()
				 .orderByHistoricActivityInstanceStartTime().desc().list();
		if (!CollectionUtils.isEmpty(hisActivityList)) {
			tempHisAct = hisActivityList.get(0);
		}
		for (PvmTransition pvmTransition : pvmList) {
			//1.根据结束节点找到 进入的连接线
			//2.根据连接线在获取连接线连接Activity元素
			//3.在根据Act元素的key是不是ACT_HI_ACTINST表中最后一个元素
			//4.判断连接该元素的连接线是y_还是n_
			String pvmSourceEleId = pvmTransition.getSource().getId();
			if(pvmSourceEleId.contains("exclusivegateway")){
				PvmActivity act=pvmTransition.getSource();
				PvmTransition tran=act.getIncomingTransitions().get(0);
				pvmSourceEleId = tran.getSource().getId();
			}
			if (tempHisAct.getActivityId().equals(pvmSourceEleId) &&  pvmTransition.getId().startsWith("n_")) {
				return false;
			}
        }
		return true;
	}
	
}
