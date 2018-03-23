package com.gionee.gniflow.web.scheduler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.activiti.engine.FormService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.gionee.gnif.model.AppUser;
import com.gionee.gnif.util.AppContext;
import com.gionee.gniflow.biz.model.ActRuTask;
import com.gionee.gniflow.integration.dao.ActivitiHelpDao;
import com.gionee.gniflow.integration.dao.KeepRunTaskDao;
import com.gionee.gniflow.web.cmd.SendTaskTimeoutNoticeCmd;
import com.gionee.gniflow.web.listener.CheckProListener;
/**
 * 关键用户确认系统定时自动执行类
 * @author huangfuxiong
 *
 */
public class LastProJob {
	private Logger logger = LoggerFactory.getLogger(CheckProListener.class);
	
	public void executJob(){
		logger.debug("LastProJob开始========================================================");
		
		WebApplicationContext webContext = ContextLoader .getCurrentWebApplicationContext();
		KeepRunTaskDao keepRunTaskDao=(KeepRunTaskDao)webContext.getBean("keepRunTaskDao");
		TaskService taskService=(TaskService)webContext.getBean("taskService");
		IdentityService identityService=(IdentityService)webContext.getBean("identityService");
		
		//当前系统时间，用以查看是否有到时需要执行的任务
		Date date=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmm");
		String curTime=sdf.format(date);
		
		sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(keepRunTaskDao!=null){
			List<String> taskId=keepRunTaskDao.getRunTaskId(curTime);
			if(taskId != null && taskId.size() > 0){
				for(String id:taskId){
					List<ActRuTask> taskList=keepRunTaskDao.getRunTask(id);
					if(taskList==null||taskList.size()<=0){
						continue;
					}
					Task task = taskService.createTaskQuery().taskId(taskList.get(0).getId()).singleResult();
					
					//用以根据流程实例名称存放不同的参数
					Map<String,Object> param=new HashMap<String,Object>();
					//以当前系统时间作为任务执行完毕的时间
					date=new Date();
					//获取待办任务的实例名称
					String key=taskList.get(0).getProcDefId();
					key=key.substring(0,key.indexOf(":"));
					
					if(key.equals("L-SapAccount-Application")){
						param.put("isKeyUserAgree","1");
						param.put("keyUserAdvise","");
						param.put("applyUserName",taskList.get(0).getAssignee());
						param.put("applyDate",sdf.format(date));
					}else if(key.equals("L-InformationData-Application")){
						param.put("keyUserConfirmAudiAdvice","");
						param.put("keyUserConfirmAudiName",taskList.get(0).getAssignee());
						param.put("keyUserConfirmAudiTime",sdf.format(date));
					}else if(key.equals("L-SapAccountModify-Application")){
						param.put("isAgreeConfirm","1");
						param.put("keyUserAdvise","");
						param.put("applyUserName",taskList.get(0).getAssignee());
						param.put("applyDate",sdf.format(date));
					}else if(key.equals("L-SAPAccount-Cancel-Application")){
						param.put("keyUserConfirmAudiAdvice","");
						param.put("keyUserConfirmAudiName",taskList.get(0).getAssignee());
						param.put("keyUserConfirmAudiTime",sdf.format(date));
					}else if(key.equals("L-SAP-Requirement-Application")){
						param.put("KeyUserConfirmAudi","0");
						param.put("KeyUserSatisfactionAudi","0");
						param.put("KeyUserConfirmAudiAdvice","");
						param.put("KeyUserConfirmAudiName",taskList.get(0).getAssignee());
						param.put("KeyUserConfirmAudiTime",sdf.format(date));
					}else if(key.equals("L-SendOrder-Process")){
						param.put("satisfaction","10");
						param.put("replyReason","");
						param.put("name",taskList.get(0).getAssignee());
						param.put("applyTime",sdf.format(date));
					}else{
						throw new RuntimeException("这个流程不属于需要监听定时处理的流程!");
					}
			        
					identityService.setAuthenticatedUserId(taskList.get(0).getAssignee());
					//根据任务编号及关键用户确认参数完成流程
					taskService.complete(taskList.get(0).getId(),param);
					keepRunTaskDao.delTaskInf(id);
				}
			}
		}
	}
}
