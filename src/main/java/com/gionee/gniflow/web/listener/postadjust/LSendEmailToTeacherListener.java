package com.gionee.gniflow.web.listener.postadjust;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.dto.HrUserDto;
import com.gionee.gniflow.web.util.SendMailTool;
import com.gionee.gniflow.web.util.SpringTools;
/**
 * 金立导师制导师上传反馈表时，邮件提醒导师下次反馈的时间
 * @author Administrator
 *
 */
public class LSendEmailToTeacherListener implements ExecutionListener {
	
	private Logger logger = LoggerFactory.getLogger(LSendEmailToTeacherListener.class);
	private ProcessHelpService processHelpService;
	/**
	 * 
	 */
	private static final long serialVersionUID = -8591980217257953495L;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		logger.debug("Running in SendEmailToTeacher begin！");
		processHelpService = (ProcessHelpService) SpringTools.getBean(ProcessHelpService.class);
		RuntimeService runtimeService= execution.getEngineServices().getRuntimeService();
		String teacherNo = (String)runtimeService.getVariable(execution.getId(), "teacherNo");//导师编号
		String empName = (String)runtimeService.getVariable(execution.getId(), "userName");
		//获取下次反馈的时间
		/*SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar=Calendar.getInstance();
		calendar.add(Calendar.MONTH, +1);//一个月后
		Date date=calendar.getTime();
		String nextTime=sdf.format(date);*/
		HrUserDto user = processHelpService.getHrUser4Account(teacherNo);//通过员工编号获取员工信息
		String email=user.getEmail();
		String name=user.getName();
		String subjectEmp="";//邮件名称    
	    String contentEmp="";//邮件内容
	    subjectEmp = "您的Mentee试用内已离职";
		
	    //String address="huangfuxiong@gionee.com";
		contentEmp ="亲爱的导师"+name+"你好，<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;很遗憾您所带的新员工"+empName+"在试用期内已经离职，您与"+empName+"的导师辅导事宜终止，再次感谢您对新员工培养<br>的辛苦付出！";
		//SendMailTool.sendMailByAddress(subjectEmp, contentEmp, address);
	    if (!StringUtils.isEmpty(email)) {
	    	//new SendEmailThread(toEmp, subjectEmp, contentEmp).start();
	    	SendMailTool.sendMailByAddress(subjectEmp, contentEmp, email);
	   }
	    
	  logger.debug("Running in SendEmailToTeacher end！");  
	}

}
