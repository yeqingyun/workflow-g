package com.gionee.gniflow.web.cmd;

import org.activiti.engine.impl.HistoricActivityInstanceQueryImpl;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntity;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gniflow.biz.service.ActivitiHelpService;
import com.gionee.gniflow.web.graph.ActivitiHistoryGraphBuilder;
import com.gionee.gniflow.web.graph.Edge;
import com.gionee.gniflow.web.graph.Graph;
import com.gionee.gniflow.web.graph.Node;
import com.gionee.gniflow.web.util.SpringTools;

/**
 * 判断下一个节点是否已经完成.
 */
public class CheckNextTaskCompletedCmd implements Command<Boolean> {
	
    private static Logger logger = LoggerFactory.getLogger(CheckNextTaskCompletedCmd.class);
    
    private String historyTaskId;

    /**
     * 这个historyTaskId是已经完成的一个任务的id.
     */
    public CheckNextTaskCompletedCmd(String historyTaskId) {
        this.historyTaskId = historyTaskId;
    }

    /**
     * 撤销流程.
     * 
     * @return true-已完成  false-未完成
     */
    public Boolean execute(CommandContext commandContext) {
    	
        // 获得历史任务
        HistoricTaskInstanceEntity historicTaskInstanceEntity = Context
                .getCommandContext().getHistoricTaskInstanceEntityManager()
                .findHistoricTaskInstanceById(historyTaskId);
		
        // 获得历史节点
        HistoricActivityInstanceEntity historicActivityInstanceEntity = getHistoricActivityInstanceEntity(historyTaskId);

        Graph graph = new ActivitiHistoryGraphBuilder(
                historicTaskInstanceEntity.getProcessInstanceId()).build();

        Node node = graph.findById(historicActivityInstanceEntity.getId());

        if (!checkCouldWithdraw(node)) {
            logger.info("cannot withdraw {}", historyTaskId);
            return true;
        }
        return false;
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
