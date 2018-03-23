package com.gionee.gniflow.web.cmd;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.runtime.ProcessInstance;

/**
 * 判断流程是否结束.
 */
public class CheckProcessEndCmd implements Command<Boolean> {
	
    private String historyTaskId;

    /**
     * 这个historyTaskId是已经完成的一个任务的id.
     */
    public CheckProcessEndCmd(String historyTaskId) {
        this.historyTaskId = historyTaskId;
    }

    /**
     * 
     * @return true-已结束  false-未结束
     */
    public Boolean execute(CommandContext commandContext) {
    	
        // 获得历史任务
        HistoricTaskInstanceEntity historicTaskInstanceEntity = Context
                .getCommandContext().getHistoricTaskInstanceEntityManager()
                .findHistoricTaskInstanceById(historyTaskId);
        
    	// 取得流程实例
    	ProcessInstance instance = commandContext.getProcessEngineConfiguration().getRuntimeService()
    			.createProcessInstanceQuery().processInstanceId(historicTaskInstanceEntity.getProcessInstanceId())
    			.singleResult();
		if (instance == null) {
			return true;
		}
        return false;
    }

}
