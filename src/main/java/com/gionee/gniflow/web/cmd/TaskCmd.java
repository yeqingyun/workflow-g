package com.gionee.gniflow.web.cmd;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.HistoricActivityInstanceQueryImpl;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.cmd.GetDeploymentProcessDefinitionCmd;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.PersistentObject;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntity;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gniflow.biz.service.ActivitiHelpService;
import com.gionee.gniflow.web.graph.ActivitiHistoryGraphBuilder;
import com.gionee.gniflow.web.graph.Edge;
import com.gionee.gniflow.web.graph.Graph;
import com.gionee.gniflow.web.graph.Node;
import com.gionee.gniflow.web.util.SpringTools;

public class TaskCmd implements  Command<Integer>{
  
	
	   private static Logger logger = LoggerFactory
	            .getLogger(TaskCmd.class);
	    private String taskInstanceId;

	    /**
	     * 这个historyTaskId是已经完成的一个任务的id.
	     */
	    public TaskCmd(String taskInstanceId) {
	        this.taskInstanceId = taskInstanceId;
	    }

	    /**
	     * 撤销流程.
	     * 
	     * @return 0-撤销成功 1-流程结束 2-下一结点已经通过,不能撤销
	     */
	@Override
	public Integer execute(CommandContext commandContext) {
		// TODO Auto-generated method stub
		
/*		commandContext.getProcessEngineConfiguration().getHistoryService().deleteHistoricTaskInstance(historyTaskId);
*/
		
		
		
		
		//获取历史任务的集合
		List<HistoricActivityInstance> list=commandContext.getProcessEngineConfiguration().getHistoryService().
		createHistoricActivityInstanceQuery().processInstanceId(taskInstanceId).orderByHistoricActivityInstanceEndTime().asc().list();
		
		if(list.size()<=2){
		  return 1;
		}
		 //获取上个节点的任务数据
		  HistoricTaskInstance currTask = commandContext.getProcessEngineConfiguration().getHistoryService()
	                .createHistoricTaskInstanceQuery().taskId(list.get(list.size()-2).getTaskId())
	                .singleResult();  


		  // 获得历史任务 正在执行的任务
       HistoricTaskInstanceEntity historicTaskInstanceEntity = Context
                .getCommandContext().getHistoricTaskInstanceEntityManager()
                .findHistoricTaskInstanceById(currTask.getId());
        // 取得当前任务
      
  
       
     // 获得历史节点   完成任务
        HistoricActivityInstanceEntity historicActivityInstanceEntity = getHistoricActivityInstanceEntity(currTask.getId());

		 // 删除所有活动中的task
        this.deleteActiveTasks(currTask
                .getProcessInstanceId());


        
      Graph graph = new ActivitiHistoryGraphBuilder(
                historicTaskInstanceEntity.getProcessInstanceId()).build();

        Node node = graph.findById(historicActivityInstanceEntity.getId());

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
    	//将代办任务的数据放入完成的任务里面
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
