package com.gionee.gniflow.web.cmd;

import java.util.Map;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.task.NativeTaskQuery;

import com.gionee.gnif.util.AppContext;

public class FilterSapTaskCountCmd implements Command<Long> {
	
    private Map<String, String> filterMap;
    
    private String userId = AppContext.getCurrentAccount();

    public FilterSapTaskCountCmd(Map<String, String> filterMap) {
		super();
		this.filterMap = filterMap;
	}

	/**
     * 获取数据
     */
    public Long execute(CommandContext commandContext) {
    	
    	StringBuffer countSql = new StringBuffer(100);
		countSql.append("SELECT COUNT (DISTINCT RES.ID_) ");
		countSql.append("FROM ACT_RU_TASK RES ");
		countSql.append("WHERE RES.ASSIGNEE_ = #{assignee} ");
		countSql.append("AND RES.SUSPENSION_STATE_ = 1 ");
		for (Map.Entry<String, String> entry : filterMap.entrySet()) {
			countSql.append("AND RES.PROC_DEF_ID_ NOT LIKE (#{").append(entry.getKey()).append("}) ");
		}
		
		NativeTaskQuery nativeQuery = commandContext.getProcessEngineConfiguration()
				.getTaskService().createNativeTaskQuery();
		nativeQuery = nativeQuery.sql(countSql.toString()).parameter("assignee", userId);
		for (Map.Entry<String, String> entry : filterMap.entrySet()) {
			nativeQuery = nativeQuery.parameter(entry.getKey(), entry.getValue());
		}
		Long totalOne  = nativeQuery.count();
		
		
		StringBuffer  noSignCountSql = new StringBuffer(100);
		noSignCountSql.append("SELECT COUNT (DISTINCT RES.ID_) ");
		noSignCountSql.append("FROM ACT_RU_TASK RES ");
		noSignCountSql.append("INNER JOIN ACT_RU_IDENTITYLINK I ON I.TASK_ID_ = RES.ID_ ");
		noSignCountSql.append("WHERE RES.ASSIGNEE_ IS NULL ");
		noSignCountSql.append("AND I.TYPE_ = 'candidate' ");
		noSignCountSql.append("AND RES.SUSPENSION_STATE_ = 1 " );
		noSignCountSql.append("AND (I.USER_ID_ = #{assignee}) ");
		
		for (Map.Entry<String, String> entry : filterMap.entrySet()) {
			noSignCountSql.append("AND RES.PROC_DEF_ID_ NOT LIKE (#{").append(entry.getKey()).append("}) ");
		}
		
		NativeTaskQuery noSignNativeQuery = commandContext.getProcessEngineConfiguration()
				.getTaskService().createNativeTaskQuery();
		noSignNativeQuery = noSignNativeQuery.sql(noSignCountSql.toString()).parameter("assignee", userId);
		for (Map.Entry<String, String> entry : filterMap.entrySet()) {
			noSignNativeQuery = noSignNativeQuery.parameter(entry.getKey(), entry.getValue());
		}
		Long totalTwo  = noSignNativeQuery.count();
		
        return totalOne + totalTwo;
    }

}
