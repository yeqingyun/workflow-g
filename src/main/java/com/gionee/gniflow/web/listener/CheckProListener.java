package com.gionee.gniflow.web.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.gionee.gniflow.integration.dao.KeepRunTaskDao;
/**
 * 当关键用户确认点击完成操作时，触发此监听类
 * @author huangfuxiong
 *
 */
public class CheckProListener implements ExecutionListener {
	private Logger logger = LoggerFactory.getLogger(CheckProListener.class);
	
	private static final long serialVersionUID = 4471360126718196111L;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		logger.debug("ChckeProListener=================================================");
		//获取流程实例编号
		String id=execution.getProcessInstanceId();
		WebApplicationContext webContext = ContextLoader .getCurrentWebApplicationContext();
		KeepRunTaskDao keepRunTaskDao=(KeepRunTaskDao)webContext.getBean("keepRunTaskDao");
		//根据流程实例编号删除表里的记录
		keepRunTaskDao.delTaskInf(id);
	}

}
