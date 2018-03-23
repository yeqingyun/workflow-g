package com.gionee.gniflow.web.cmd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.task.NativeTaskQuery;
import org.activiti.engine.task.Task;

import com.gionee.gnif.util.AppContext;

public class FilterSapTaskPageCmd implements Command<List<Task>> {
	
    private Map<String, String> filterMap;
    
    private Integer between;
    
    private Integer end;
    
    private String userId = AppContext.getCurrentAccount();

    public FilterSapTaskPageCmd(Map<String, String> filterMap, Integer between,
			Integer end) {
		super();
		this.filterMap = filterMap;
		this.between = between;
		this.end = end;
	}

	/**
     * 获取数据
     */
    public List<Task> execute(CommandContext commandContext) {
    	
    	List<Task> resuktTasks = new ArrayList<Task>();
    	
    	//任务处理人的任务
    	StringBuffer pageSql = new StringBuffer(100);
		pageSql.append("SELECT SUB.* FROM(SELECT DISTINCT RES.*,row_number () OVER (ORDER BY RES.CREATE_TIME_ DESC) rnk ");
		pageSql.append("FROM ( ");
		pageSql.append("SELECT DISTINCT RES.* FROM ACT_RU_TASK RES ");
		pageSql.append("WHERE RES.ASSIGNEE_ = #{assignee} ");
		pageSql.append("AND RES.SUSPENSION_STATE_ = 1 ");
		
		for (Map.Entry<String, String> entry : filterMap.entrySet()) {
			pageSql.append("AND RES.PROC_DEF_ID_ NOT LIKE (#{").append(entry.getKey()).append("}) ");
		}
		pageSql.append(") RES ");
		pageSql.append(") SUB ");
		pageSql.append("WHERE SUB.rnk >= #{begin} AND SUB.rnk < #{end}");
		
		NativeTaskQuery nativeQuery = commandContext.getProcessEngineConfiguration()
				.getTaskService().createNativeTaskQuery();
		nativeQuery = nativeQuery.sql(pageSql.toString())
			.parameter("assignee", userId)
			.parameter("begin", between)
			.parameter("end", end);
		
		for (Map.Entry<String, String> entry : filterMap.entrySet()) {
			nativeQuery = nativeQuery.parameter(entry.getKey(), entry.getValue());
		}
		
		List<Task> tasks = nativeQuery.list();
		
		//根据当前人未签收的任务
		StringBuffer notSignPageSql = new StringBuffer(100);
		notSignPageSql.append("SELECT SUB.* FROM( SELECT DISTINCT RES.*, row_number () OVER (ORDER BY RES.CREATE_TIME_ DESC ) rnk ");
		notSignPageSql.append("FROM ( ");
		notSignPageSql.append("SELECT DISTINCT RES.* FROM ACT_RU_TASK RES ");
		notSignPageSql.append("INNER JOIN ACT_RU_IDENTITYLINK I ON I.TASK_ID_ = RES.ID_ ");
		notSignPageSql.append("WHERE RES.ASSIGNEE_ IS NULL ");
		
		notSignPageSql.append("AND I.TYPE_ = 'candidate' ");
		notSignPageSql.append("AND (I.USER_ID_ = #{assignee}) ");		
		notSignPageSql.append("AND RES.SUSPENSION_STATE_ = 1 ");
		
		for (Map.Entry<String, String> entry : filterMap.entrySet()) {
			notSignPageSql.append("AND RES.PROC_DEF_ID_ NOT LIKE (#{").append(entry.getKey()).append("}) ");
		}
		notSignPageSql.append(") RES ");
		notSignPageSql.append(") SUB ");
		notSignPageSql.append("WHERE SUB.rnk >= #{begin} AND SUB.rnk < #{end}");
		
		NativeTaskQuery notSignNativeQuery = commandContext.getProcessEngineConfiguration()
				.getTaskService().createNativeTaskQuery();
		notSignNativeQuery = notSignNativeQuery.sql(notSignPageSql.toString())
			.parameter("assignee", userId)
			.parameter("begin", between)
			.parameter("end", end);
		
		for (Map.Entry<String, String> entry : filterMap.entrySet()) {
			notSignNativeQuery = notSignNativeQuery.parameter(entry.getKey(), entry.getValue());
		}
		
		List<Task> noSignTasks = notSignNativeQuery.list();
		
		resuktTasks.addAll(tasks);
		resuktTasks.addAll(noSignTasks);

        return resuktTasks;
    }

}
