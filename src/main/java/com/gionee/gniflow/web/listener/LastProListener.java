package com.gionee.gniflow.web.listener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.gionee.gniflow.biz.model.KeepRunTask;
import com.gionee.gniflow.integration.dao.KeepRunTaskDao;
import com.gionee.gniflow.sap.listener.SapReqSheetAgreeEndListener;
/**
 * 监听关键用户确认前个任务是否完成，完成即触发
 * @author huangfuxiong
 *
 */
public class LastProListener implements ExecutionListener {
	private Logger logger = LoggerFactory.getLogger(LastProListener.class);
	
	private static final long serialVersionUID = 4471360126718196213L;
	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		logger.debug("LastProListener======================================================");
		Date date=new Date();
		
		//Long time=date.getTime()+5*60*1000;
		//date=new Date(time);
		
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		//获取流程实例名称
		String key=execution.getProcessDefinitionId();
		key=key.substring(0,key.indexOf(":"));
		//根据流程实例名称设置流程任务执行的时间
		if(key.equals("L-SendOrder-Process")){
			cal.add(Calendar.DAY_OF_YEAR,7);
		}else{
			cal.add(Calendar.MONTH,+1);
		}
		
		cal.add(Calendar.DAY_OF_YEAR,-1);
		date=cal.getTime();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmm");
		String dateTime=sdf.format(date);
		//获取流程实例编号
		String id=execution.getProcessInstanceId();
		KeepRunTask runTask=new KeepRunTask();
		//把数据封装到实例类里
		runTask.setTaskId(id);
		runTask.setRunTime(dateTime);
		
		WebApplicationContext webContext = ContextLoader .getCurrentWebApplicationContext();
		KeepRunTaskDao keepRunTaskDao=(KeepRunTaskDao)webContext.getBean("keepRunTaskDao");
		//把数据保存到数据表里
		keepRunTaskDao.addRunTaskInf(runTask);
	}

}
