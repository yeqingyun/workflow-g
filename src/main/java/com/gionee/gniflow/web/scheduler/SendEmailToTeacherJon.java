package com.gionee.gniflow.web.scheduler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.gionee.gniflow.biz.model.SaveEmpAndTeacherInfEntity;
import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.dto.HrUserDto;
import com.gionee.gniflow.integration.dao.KeepRunTaskDao;
import com.gionee.gniflow.web.listener.CheckProListener;
import com.gionee.gniflow.web.util.SendMailTool;
import com.gionee.gniflow.web.util.SpringTools;
/**
 * 导师期内每隔一个月给导师发一次邮件
 * @author huangfuxiong
 *
 */
public class SendEmailToTeacherJon {
	private Logger logger = LoggerFactory.getLogger(CheckProListener.class);
	private ProcessHelpService processHelpService;
	
	public void sendEmail() throws Exception{
		logger.debug("SendEmailToTeacherJon===========================开始");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		Date date=new Date();
		Calendar calendar=Calendar.getInstance();
		String curTime=sdf.format(date);
		
		processHelpService = (ProcessHelpService) SpringTools.getBean(ProcessHelpService.class);
		WebApplicationContext webContext = ContextLoader .getCurrentWebApplicationContext();
		KeepRunTaskDao keepRunTaskDao=(KeepRunTaskDao)webContext.getBean("keepRunTaskDao");
		//查询看否是有需要发送的邮件
		List<SaveEmpAndTeacherInfEntity> emails=keepRunTaskDao.getEmailByCurTime(curTime);
		if(emails!=null&&emails.size()>0){
			Map<String,String> map=new HashMap<String,String>();
			for(SaveEmpAndTeacherInfEntity email:emails){
				map.put(email.getEmpId(), email.getTeaEmail()+"#"+email.getTeacherName()+"#"+email.getEntryTime());
			}
			
			Set<String> empIds=map.keySet();
			for(String empId:empIds){
				String subjectEmp="";//邮件名称    
			    String contentEmp="";//邮件内容
			    subjectEmp = "请与您的Mentee做月度沟通";
			    String[] teas=(map.get(empId)).split("#");
			    String empAccount = processHelpService.getEmpAccount(empId);
			    if(empAccount==null || "".equals(empAccount)){
			    	keepRunTaskDao.delEmpAndTeaInf(empId);
			    	continue;
			    }
			    HrUserDto user = processHelpService.getHrUser4Account(empAccount);//通过员工账号获取员工信息
			    Date entryTime=user.getEntryDate();
			    String status=user.getStatus();
			    String empGroup=user.getEmpGroup();
			    int month=(int)((date.getTime()-entryTime.getTime())/1000L/60L/60L/24L/30L);
				
			    //String address="huangfuxiong@gionee.com";
				contentEmp ="亲爱的导师"+teas[1]+"你好，<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您所带的新员工已经加入金立大家庭"+month+"个月了，导师需要每月抽出时间与新员工沟通，请登录工作<br>"
						+"流平台下载《新员工月度培养反馈表》 ，与新员工共同完成，部门经理确认签字后上传附件。本月已提<br>"
						+"交的请做好下个月度的沟通，让我们为了培育并留住更好的金立人而共同努力！";
				//SendMailTool.sendMailByAddress(subjectEmp, contentEmp, address);
			    if (!StringUtils.isEmpty(teas[0])) {
			    	//new SendEmailThread(toEmp, subjectEmp, contentEmp).start();
			    	SendMailTool.sendMailByAddress(subjectEmp, contentEmp, teas[0]);
			   }
			    
			   if("在职".equals(status)&&!"A".equals(empGroup)&&month<6){
				   try {
					   calendar.setTime(sdf.parse(teas[2]));
					   calendar.add(Calendar.MONTH, +1);//一个月后
					   String nextTime=sdf.format(calendar.getTime());
					   Map<String,Object> param=new HashMap<String,Object>();
					   param.put("empId", empId);
					   param.put("entryTime", nextTime);
					   keepRunTaskDao.updateSendEmailTime(param);
					} catch (ParseException e) {
						e.printStackTrace();
					}
			   }else{
				   keepRunTaskDao.delEmpAndTeaInf(empId);
			   }
			}
		}
		logger.debug("SendEmailToTeacherJon===========================结束");
	}
}
