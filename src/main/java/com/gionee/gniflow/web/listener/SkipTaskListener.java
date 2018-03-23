package com.gionee.gniflow.web.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.bpmn.constants.BpmnXMLConstants;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.el.ExpressionManager;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.task.IdentityLink;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.gionee.gniflow.constant.BPMConstant;
import com.gionee.gniflow.util.MapVariableScope;
import com.gionee.gniflow.web.cmd.CompleteTaskWithCommentCmd;
import com.gionee.gniflow.web.support.DefaultTaskListener;
import com.gionee.gniflow.web.util.SpringTools;

public class SkipTaskListener extends DefaultTaskListener {
	private static final long serialVersionUID = -8315115344143845809L;

	private static Logger logger = LoggerFactory.getLogger(SkipTaskListener.class);
	
	private static final List<String> SKIP_NODES_FOR_FIND_STARTNODE = Arrays.asList(new String[]{"exclusiveGateway","eventBasedGateway",
			"inclusiveGateway","parallelGateway","receiveTask","scriptTask","sendTask","serviceTask","callActivity"});
	
	private ExpressionManager expressionManager;
	
	private RuntimeService runtimeService;
	
    @Override
    public void onCreate(DelegateTask delegateTask) throws Exception {
        String taskDefinitionKey = delegateTask.getTaskDefinitionKey();
        String processDefinitionId = delegateTask.getProcessDefinitionId();
        String processInstanceId = delegateTask.getProcessInstanceId();
        
        logger.debug("--------------------Running in SkipTaskListener Bengin ----------------------");
        logger.debug("delegateTask.getId : {}", delegateTask.getId());
        logger.debug("taskDefinitionKey : {}", taskDefinitionKey);
        logger.debug("processDefinitionId : {}", processDefinitionId);
        logger.debug("processInstanceId : {}", processInstanceId);

       
        expressionManager = Context
                .getProcessEngineConfiguration().getExpressionManager();
        
        runtimeService = delegateTask.getExecution().getEngineServices().getRuntimeService();
        
        boolean nextTaskHasAssginess = true;
        
        ActivityImpl activityImpl = Context.getExecutionContext().getExecution().getActivity();
        TaskDefinition taskDefinition = (TaskDefinition) activityImpl.getProperty("taskDefinition");
        String activityType = (String) activityImpl.getProperty("type");
        
        ////还原activitiImpl
        List<ActivityImpl> activitiList = findAllSkipActiviti(activityImpl, new ArrayList<ActivityImpl>(), delegateTask);
        for (ActivityImpl activity : activitiList) {
        	activity.setProperty(BPMConstant.SKIP_TASK_IGNOR_CONDTION_KEY, false);
        }
        ////
        
        //当前任务是UserTask
        if (activityType.equals(BpmnXMLConstants.ELEMENT_TASK_USER) 
        		&& !delegateTask.getTaskDefinitionKey().endsWith(BPMConstant.BPMN_MULTIINSTTASK_SUFFIX)) {
        	
        	Integer assgineeType = this.analysisTaskAssigneeExpression(taskDefinition);		
        	
            //判断任务定义的候选人配置类型，及任务的处理人是否为空
        	//如果发起人和当前处理人是同一个人，也跳过
//            if ((assgineeType == BPMConstant.TASK_ASSIGNMENT_TYPE_ASSGINEE 
//            		&& StringUtils.isEmpty(delegateTask.getAssignee()))
//            		|| taskAssigneeIsStartUser(delegateTask, activityImpl)){
          if ((assgineeType == BPMConstant.TASK_ASSIGNMENT_TYPE_ASSGINEE 
    		&& StringUtils.isEmpty(delegateTask.getAssignee()))){
            	
            	nextTaskHasAssginess = false;
            	
            } else if (((assgineeType == BPMConstant.TASK_ASSIGNMENT_TYPE_CANDIDATEUSERID
            			|| assgineeType == BPMConstant.TASK_ASSIGNMENT_TYPE_CANDIDATEGROUP
            		) 
            		&& CollectionUtils.isEmpty(delegateTask.getCandidates()))) {
            	TaskService taskService = (TaskService) SpringTools.getBean(TaskService.class);
        		taskService.claim(delegateTask.getId(), null);
        		
        		nextTaskHasAssginess = false;
            }
            
        }
        
        
        if (!nextTaskHasAssginess) {
            logger.info("skip task : {}", delegateTask.getId());
            
            //判断当前任务节点有几根流出线，如果为一条，不需要设置 ignor_condion=true
            boolean result = curActivityMoreThenTwoOutGoing(activityImpl);
            if (result) {
            	 List<ActivityImpl> actList = findAllSkipActiviti(activityImpl, new ArrayList<ActivityImpl>(), delegateTask);
                 for (ActivityImpl activity : actList) {
                 	activity.setProperty(BPMConstant.SKIP_TASK_IGNOR_CONDTION_KEY, true);
                 }
            }

            //跳过任务
            new CompleteTaskWithCommentCmd(delegateTask.getId(),
            		Collections.<String, Object> emptyMap(), "跳过")
            			.execute(Context.getCommandContext());
            
        }
        
        logger.debug("--------------------Running in SkipTaskListener End ----------------------");
    }
    
    private List<ActivityImpl> findAllSkipActiviti(ActivityImpl activityImpl, List<ActivityImpl> result, DelegateTask delegateTask){
    	
    	List<PvmTransition> activityImpList = activityImpl.getOutgoingTransitions();
    	
    	Map<String, Object> var = runtimeService.getVariables(delegateTask.getExecutionId());
    	
        if (!CollectionUtils.isEmpty(activityImpList)){
        	//获取当前任务的流向
            for (PvmTransition pvmTransition : activityImpList) {
				MapVariableScope mapVariableScope = new MapVariableScope();
				mapVariableScope.setVariables(var);

				Object objValue = null;
				try {
					objValue = expressionManager.createExpression(
							(String) pvmTransition.getProperty("conditionText")).getValue(mapVariableScope);
				} catch (Exception e) {
					objValue = null;
				}
				//流程线上配置的表达式可能为多条件表达式，而导致返回的objValue不是null,而是false
				if (objValue == null || !(boolean)objValue) {
					Object obj = null;
					if (activityImpList.size() > 1) {
						//任务拥有两个出口才需要动态设置流程线上的UEL表达式的相关变量
	        			obj = activityImpl.getProperty("default"); //找到默认出口
	        	        if (obj == null) {
	        	        	//throw new ActivitiException("当前任务节点没有默认执行流，无法跳过任务！");
	        	        	continue;
	        	        }
					} else {
						obj  = pvmTransition.getId();
					}
        	        String defaultFlow = (String) obj;
                	//找出Activiti的流出的默认节点
                	if (pvmTransition.getId().equals(defaultFlow)) {
                		result.add(activityImpl);
                		PvmActivity pvmActivity = pvmTransition.getDestination();
                		Object actType = pvmActivity.getProperty("type");
                		if (actType.equals("exclusiveGateway")) {
//                			result.add((ActivityImpl) pvmActivity);
                			findAllSkipActiviti((ActivityImpl) pvmTransition.getDestination(), result, delegateTask);
                		}
                	}
				}
            }
        }
    	
    	return result;
    }
    
    /**
     * 获取UserTask的责任人配置类型
     * @param taskDefinition
     * @return
     */
    private Integer analysisTaskAssigneeExpression(TaskDefinition taskDefinition){
    	
    	Expression assgineeExp = taskDefinition.getAssigneeExpression();
    	Set<Expression> canUserExp = taskDefinition.getCandidateUserIdExpressions();
    	Set<Expression> canGroupExp = taskDefinition.getCandidateGroupIdExpressions();
    	
    	if (assgineeExp != null && CollectionUtils.isEmpty(canUserExp)
    			&& CollectionUtils.isEmpty(canGroupExp)) {
    		return BPMConstant.TASK_ASSIGNMENT_TYPE_ASSGINEE;
    	}
    	if (assgineeExp == null && !CollectionUtils.isEmpty(canUserExp)
    			&& CollectionUtils.isEmpty(canGroupExp)) {
    		return BPMConstant.TASK_ASSIGNMENT_TYPE_CANDIDATEUSERID;
    	}
    	if (assgineeExp == null && CollectionUtils.isEmpty(canUserExp)
    			&& !CollectionUtils.isEmpty(canGroupExp)) {
    		return BPMConstant.TASK_ASSIGNMENT_TYPE_CANDIDATEGROUP;
    	}
		return BPMConstant.TASK_ASSIGNMENT_TYPE_ASSGINEE;
    }
    
    /**
     * 判断当前任务处理人是不是任务的发起人
     * @return
     */
    private boolean taskAssigneeIsStartUser(DelegateTask delegateTask, ActivityImpl activityImpl){
    	String startUser = null;
    	List<IdentityLink> inentity = runtimeService.getIdentityLinksForProcessInstance(delegateTask.getProcessInstanceId());
    	for (IdentityLink link : inentity) {
    		if (link.getType().equals("starter")) {
    			startUser = link.getUserId();
    			break;
    		}
    	}
    	
    	//判断当前节点的前一个节点，是不是开始节点
    	ActivityImpl perActivityImpl = findPerActivity(activityImpl);
    	//以update开头的修改节点不跳过
    	String taskDefKey = delegateTask.getTaskDefinitionKey();
    	
    	if (StringUtils.isNotEmpty(startUser) && startUser.equals(delegateTask.getAssignee())
    			&& perActivityImpl != null && !taskDefKey.startsWith("update")) {
    		return true;
    	}
    	return false;
    }
    
    /**
     * 判断当前节点有几根流出线
     * @param activityImpl
     * @return
     */
    private boolean curActivityMoreThenTwoOutGoing(ActivityImpl activityImpl){
    	List<PvmTransition> activityImpList = activityImpl.getOutgoingTransitions();
    	if (!CollectionUtils.isEmpty(activityImpList) && activityImpList.size() > 1){
    		return true;
    	}
    	return false;
    }
    
    /**
     * 判断当前节点的前一个节点是不是开始节点
     * @param activityImpl
     * @return
     */
    private ActivityImpl findPerActivity(ActivityImpl activityImpl){
    	ActivityImpl perActivityImpl = null; 
    	List<PvmTransition> prvmTrans = activityImpl.getIncomingTransitions();
    	for(PvmTransition pvmTran :prvmTrans){
    		PvmActivity pvmAct = pvmTran.getSource();
    		if (pvmAct.getProperty("type").equals("startEvent")) {
    			return perActivityImpl = (ActivityImpl) pvmAct;
    		} else if (SKIP_NODES_FOR_FIND_STARTNODE.contains(pvmAct.getProperty("type"))) {
    			//对于网关，receiveTask,scriptTask,sendTask,serviceTask,callActivity需要继续向前找流程图上的前一个元素
    			findPerActivity((ActivityImpl) pvmAct);
    		} else {
    			//当前节点的前一个节点为UserTask时，不能返回null
    			perActivityImpl = new ActivityImpl(null, activityImpl.getProcessDefinition());
    		}
    	}
		return perActivityImpl;
    }
}
