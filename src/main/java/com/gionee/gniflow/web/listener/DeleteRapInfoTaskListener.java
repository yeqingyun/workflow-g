package com.gionee.gniflow.web.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.commons.lang3.StringUtils;

import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.biz.service.RapInforService;
import com.gionee.gniflow.web.support.AudiException;
import com.gionee.gniflow.web.util.SpringTools;

public class DeleteRapInfoTaskListener implements TaskListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
     
	private RapInforService rapInforService;
	@Override
	public void notify(DelegateTask delegateTask) {
		
		// TODO Auto-generated method stub
		//删除导入的奖惩信息
		rapInforService = (RapInforService) SpringTools.getBean(RapInforService.class);
		String processInstanceId=delegateTask.getExecution().getProcessInstanceId();
		try {
			rapInforService.delete(processInstanceId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   }

}
