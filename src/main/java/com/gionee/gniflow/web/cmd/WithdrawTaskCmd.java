package com.gionee.gniflow.web.cmd;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.impl.HistoricActivityInstanceQueryImpl;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.cmd.GetDeploymentProcessDefinitionCmd;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntity;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gniflow.biz.service.ActivitiHelpService;
import com.gionee.gniflow.web.cmd.pRecall.CheckIsOrParallelTask;
import com.gionee.gniflow.web.graph.ActivitiHistoryGraphBuilder;
import com.gionee.gniflow.web.graph.Edge;
import com.gionee.gniflow.web.graph.Graph;
import com.gionee.gniflow.web.graph.Node;
import com.gionee.gniflow.web.util.SpringTools;

/**
 * 撤销任务.
 */
public class WithdrawTaskCmd implements Command<Integer> {
    private static Logger logger = LoggerFactory
            .getLogger(WithdrawTaskCmd.class);
    private String historyTaskId;

    /**
     * 这个historyTaskId是已经完成的一个任务的id.
     */
    public WithdrawTaskCmd(String historyTaskId) {
        this.historyTaskId = historyTaskId;
    }

    /**
     * 撤销流程.
     * 
     * @return 0-撤销成功 1-流程结束 2-下一结点已经通过,不能撤销
     */
    public Integer execute(CommandContext commandContext) {
    	
        // 获得历史任务
        HistoricTaskInstanceEntity historicTaskInstanceEntity = Context
                .getCommandContext().getHistoricTaskInstanceEntityManager()
                .findHistoricTaskInstanceById(historyTaskId);
        
    	// 取得流程实例
    	ProcessInstance instance = commandContext.getProcessEngineConfiguration().getRuntimeService()
    			.createProcessInstanceQuery().processInstanceId(historicTaskInstanceEntity.getProcessInstanceId())
    			.singleResult();
		if (instance == null) {
			return 1;
		}
		
        // 获得历史节点
        HistoricActivityInstanceEntity historicActivityInstanceEntity = getHistoricActivityInstanceEntity(historyTaskId);

        Graph graph = new ActivitiHistoryGraphBuilder(
                historicTaskInstanceEntity.getProcessInstanceId()).build();

        Node node = graph.findById(historicActivityInstanceEntity.getId());

        if (!checkCouldWithdraw(node)) {
            logger.info("cannot withdraw {}", historyTaskId);

            return 2;
        }
        
        //判断是否是并行任务
        String taskDefinitionKey=commandContext.getProcessEngineConfiguration().getTaskService()
        	.createTaskQuery().processInstanceId(historicTaskInstanceEntity.getProcessInstanceId())
        	.list().get(0).getTaskDefinitionKey();
        CheckIsOrParallelTask checkIsOrParallelTask=new CheckIsOrParallelTask(taskDefinitionKey);
        checkIsOrParallelTask.getRecallTaskClass(historicTaskInstanceEntity);
        boolean flag=checkIsOrParallelTask.isOrBrotherNode();
        //如果是非并行任务，删除所有活动中的task
        if(flag){
        	this.deleteActiveTasks(historicTaskInstanceEntity
                .getProcessInstanceId());
        }
        // 获得期望撤销的节点后面的所有节点历史
        List<String> historyNodeIds = new ArrayList<String>();
        this.collectNodes(node, historyNodeIds);
        this.deleteHistoryActivities(historyNodeIds);
        // 恢复期望撤销的任务和历史
        this.processHistoryTask(historicTaskInstanceEntity,
                historicActivityInstanceEntity);

        logger.info("activiti is withdraw {}",
                historicTaskInstanceEntity.getName());

        return 0;
    }

    public HistoricActivityInstanceEntity getHistoricActivityInstanceEntity(
            String historyTaskId) {
        logger.info("historyTaskId : {}", historyTaskId);

        ActivitiHelpService activitiHelpService = (ActivitiHelpService) SpringTools.getBean(ActivitiHelpService.class);
        String historicActivityInstanceId = activitiHelpService.findActHiActinstId(historyTaskId);
        
        logger.info("historicActivityInstanceId : {}",
                historicActivityInstanceId);

        HistoricActivityInstanceQueryImpl historicActivityInstanceQueryImpl = new HistoricActivityInstanceQueryImpl();
        historicActivityInstanceQueryImpl
                .activityInstanceId(historicActivityInstanceId);

        HistoricActivityInstanceEntity historicActivityInstanceEntity = (HistoricActivityInstanceEntity) Context
                .getCommandContext()
                .getHistoricActivityInstanceEntityManager()
                .findHistoricActivityInstancesByQueryCriteria(
                        historicActivityInstanceQueryImpl, new Page(0, 1))
                .get(0);

        return historicActivityInstanceEntity;
    }

    public boolean checkCouldWithdraw(Node node) {
        // TODO: 如果是catchEvent，也应该可以撤销，到时候再说
        for (Edge edge : node.getEdges()) {
            Node dest = edge.getDest();
            String type = dest.getType();

            if ("userTask".equals(type)) {
                if (!dest.isActive()) {
                    boolean isSkip = isSkipActivity(dest.getId());

                    if (isSkip) {
                        return checkCouldWithdraw(dest);
                    } else {
                        logger.info("cannot withdraw, " + type + "("
                                + dest.getName() + ") is complete.");

                        return false;
                    }
                }
            } else if (type.endsWith("Gateway")) {
                return checkCouldWithdraw(dest);
            } else {
                logger.info("cannot withdraw, " + type + "(" + dest.getName()
                        + ") is complete.");

                return false;
            }
        }

        return true;
    }

    /**
     * 删除未完成任务.
     */
    public void deleteActiveTasks(String processInstanceId) {
        Context.getCommandContext().getTaskEntityManager()
                .deleteTasksByProcessInstanceId(processInstanceId, null, true);
    }

    public void collectNodes(Node node, List<String> historyNodeIds) {
        logger.info("node : {}, {}, {}", node.getId(), node.getType(),
                node.getName());

        for (Edge edge : node.getEdges()) {
            logger.info("edge : {}", edge.getName());

            Node dest = edge.getDest();
            historyNodeIds.add(dest.getId());
            this.collectNodes(dest, historyNodeIds);
        }
    }

    /**
     * 删除历史节点.
     */
    public void deleteHistoryActivities(List<String> historyNodeIds) {
    	ActivitiHelpService activitiHelpService = (ActivitiHelpService) SpringTools.getBean(ActivitiHelpService.class);
        logger.info("historyNodeIds : {}", historyNodeIds);

        for (String id : historyNodeIds) {
            String taskId = activitiHelpService.findActHiActinstTaskId(id);

            if (taskId != null) {
                Context.getCommandContext()
                        .getHistoricTaskInstanceEntityManager()
                        .deleteHistoricTaskInstanceById(taskId);
            }
            
            activitiHelpService.deleteActHiActinst4Id(id);
        }
    }

    public void processHistoryTask(
            HistoricTaskInstanceEntity historicTaskInstanceEntity,
            HistoricActivityInstanceEntity historicActivityInstanceEntity) {
        historicTaskInstanceEntity.setEndTime(null);
        historicTaskInstanceEntity.setDurationInMillis(null);
        historicActivityInstanceEntity.setEndTime(null);
        historicActivityInstanceEntity.setDurationInMillis(null);

        TaskEntity task = TaskEntity.create();
        task.setProcessDefinitionId(historicTaskInstanceEntity
                .getProcessDefinitionId());
        task.setId(historicTaskInstanceEntity.getId());
        task.setAssigneeWithoutCascade(historicTaskInstanceEntity.getAssignee());
        task.setParentTaskIdWithoutCascade(historicTaskInstanceEntity
                .getParentTaskId());
        task.setNameWithoutCascade(historicTaskInstanceEntity.getName());
        task.setTaskDefinitionKey(historicTaskInstanceEntity
                .getTaskDefinitionKey());
        task.setExecutionId(historicTaskInstanceEntity.getExecutionId());
        task.setPriority(historicTaskInstanceEntity.getPriority());
        task.setProcessInstanceId(historicTaskInstanceEntity
                .getProcessInstanceId());
        task.setDescriptionWithoutCascade(historicTaskInstanceEntity
                .getDescription());

        Context.getCommandContext().getTaskEntityManager().insert(task);

        ExecutionEntity executionEntity = Context.getCommandContext()
                .getExecutionEntityManager()
                .findExecutionById(historicTaskInstanceEntity.getExecutionId());
        executionEntity
                .setActivity(getActivity(historicActivityInstanceEntity));
    }

    public ActivityImpl getActivity(
            HistoricActivityInstanceEntity historicActivityInstanceEntity) {
        ProcessDefinitionEntity processDefinitionEntity = new GetDeploymentProcessDefinitionCmd(
                historicActivityInstanceEntity.getProcessDefinitionId())
                .execute(Context.getCommandContext());

        return processDefinitionEntity
                .findActivity(historicActivityInstanceEntity.getActivityId());
    }

    public boolean isSkipActivity(String historyActivityId) {
    	ActivitiHelpService activitiHelpService = (ActivitiHelpService) SpringTools.getBean(ActivitiHelpService.class);
        String historyTaskId = activitiHelpService.findActHiActinstTaskId(historyActivityId);

        HistoricTaskInstanceEntity historicTaskInstanceEntity = Context
                .getCommandContext().getHistoricTaskInstanceEntityManager()
                .findHistoricTaskInstanceById(historyTaskId);
        String deleteReason = historicTaskInstanceEntity.getDeleteReason();

        return "跳过".equals(deleteReason);
    }
}
