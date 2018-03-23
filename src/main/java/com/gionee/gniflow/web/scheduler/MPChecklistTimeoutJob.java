package com.gionee.gniflow.web.scheduler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gnif.util.PropertiesConfig;
import com.gionee.gniflow.biz.service.ActivitiHelpService;
import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.web.util.SendMessageUtil;
import com.gionee.gniflow.web.util.SpringTools;

public class MPChecklistTimeoutJob implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -55219166951527728L;
	private ActivitiHelpService activitiHelpService;
	private ProcessHelpService processHelpService;	
	private static final Logger logger = LoggerFactory.getLogger(MPChecklistTimeoutJob.class);

	public void mpChecklistTimeoutSendMsg() {	
        String isSendMsg = PropertiesConfig.getString("message.enable");
        if(isSendMsg != null && isSendMsg.equals("true")) {  //只在正式环境发送短信，本地、测试环境不发送短信
    		activitiHelpService = (ActivitiHelpService) SpringTools.getBean(ActivitiHelpService.class);	
    		processHelpService = (ProcessHelpService) SpringTools.getBean(ProcessHelpService.class);
    		
    		logger.info("------ MPChecklistTimeoutJob Begin------");
    		
    		Calendar cal=Calendar.getInstance();
    		cal.setTime(new Date()); 
    		int week=cal.get(Calendar.DAY_OF_WEEK)-1;
    		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
    		if(week == 1) {
    			list = activitiHelpService.getNoFinishTask("L-Mass-Production-Checklist", 6, 3);
    		} else {
    			list = activitiHelpService.getNoFinishTask("L-Mass-Production-Checklist", 4, 3);
    		}
    		
    		if(list != null && list.size() > 0) {
	    		for(Map<String,Object> map : list) {
	    			if(map.get("ASSIGNEE_") != null && !map.get("ASSIGNEE_").toString().equals("")) {
		    			String content = "尊敬的同事，您好：您有新任务需要处理，流程实例编号：" + map.get("PROC_INST_ID_").toString() + "，流程名称：量产流checklist签核单，任务名称：" 
		    					+ map.get("NAME_").toString() + "。请登录Gionee工作流平台办理，任务办理地址：http://flow.gionee.com，"
		    					+ "此短信由Gionee工作流平台自动发出，请勿回复，祝工作愉快，谢谢！";
		    			String account = map.get("ASSIGNEE_").toString();
		    			//String mobile = processHelpService.getUserMobileNumber(account);
		    			String mobile = processHelpService.getUserMobileNumberNoLogin(account);
		    			if(mobile != null && !mobile.equals("")) {
		    				SendMessageUtil.sendMessage(content, mobile);
		    			}
	    			}
	    		}
    		}
    		
    		logger.info("------ MPChecklistTimeoutJob End------");
        }
	}
}
