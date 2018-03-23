package com.gionee.gniflow.web.listener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.gionee.gniflow.biz.model.SaveEmpAndTeacherInfEntity;
import com.gionee.gniflow.integration.dao.KeepRunTaskDao;

public class SaveEmpAndTeacherInfoListener implements ExecutionListener {
	
	private Logger logger = LoggerFactory.getLogger(SaveEmpAndTeacherInfoListener.class);
	//private ProcessHelpService processHelpService;
	private static final long serialVersionUID = -8591980217111953495L;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		logger.debug("Running in monthSendEmailToTeacher begin！");
		//processHelpService = (ProcessHelpService) SpringTools.getBean(ProcessHelpService.class);
		RuntimeService runtimeService= execution.getEngineServices().getRuntimeService();
		String empId = (String)runtimeService.getVariable(execution.getId(), "empId");//职员编号
		String empName = (String)runtimeService.getVariable(execution.getId(), "userName");//职员姓名
		String entryTime = (String)runtimeService.getVariable(execution.getId(), "entryTime");//职员入职时间
		String teacherEmail = (String)runtimeService.getVariable(execution.getId(), "teacherEmail");//导师邮箱
		String teacherName = (String)runtimeService.getVariable(execution.getId(), "teacherName");
		String procId = execution.getProcessInstanceId();
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date date=sdf.parse(entryTime);
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, +1);//一个月后
		sdf=new SimpleDateFormat("yyyyMMdd");
		entryTime=sdf.format(calendar.getTime());
		
		//把信息封装到实体对象
		SaveEmpAndTeacherInfEntity empAndTeacherInfo=new SaveEmpAndTeacherInfEntity();
		empAndTeacherInfo.setProcId(procId);
		empAndTeacherInfo.setEmpId(empId);
		empAndTeacherInfo.setEmpName(empName);
		empAndTeacherInfo.setEntryTime(entryTime);
		empAndTeacherInfo.setTeaEmail(teacherEmail);
		empAndTeacherInfo.setTeacherName(teacherName);
		
		WebApplicationContext webContext = ContextLoader .getCurrentWebApplicationContext();
		KeepRunTaskDao keepRunTaskDao=(KeepRunTaskDao)webContext.getBean("keepRunTaskDao");
		//如果表里有此员工的信息，先删除
		keepRunTaskDao.delEmpAndTeaInf(empId);
		//把信息数据保存到数据表里
		keepRunTaskDao.addEmpAndTeaInf(empAndTeacherInfo);
	}

}
