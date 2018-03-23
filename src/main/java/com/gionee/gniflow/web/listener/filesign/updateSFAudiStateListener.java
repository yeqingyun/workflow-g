package com.gionee.gniflow.web.listener.filesign;

import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.model.SignFileInformation;
import com.gionee.gniflow.biz.service.SignFileInformationService;
import com.gionee.gniflow.web.util.SpringTools;


public class updateSFAudiStateListener implements ExecutionListener{
	private Logger logger = LoggerFactory.getLogger(InsertFileSignInfoListener.class);
	/** 通过流程ID更新审批状态
	 *@author wjq
	 */
	private static final long serialVersionUID = -7349967222331685937L;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		logger.debug("Running in updateFSAudiStateListener begin!");
		SignFileInformationService sfInfoService=(SignFileInformationService)SpringTools.getBean(SignFileInformationService.class);
		List<SignFileInformation> list = null;
		String processInstanceId = execution.getProcessInstanceId();
		QueryMap queryMap= new QueryMap();
		queryMap.put("procInstId", processInstanceId);
		
		list = sfInfoService.querySignFileInformation(queryMap);
		
		if(!CollectionUtils.isEmpty(list)){
			SignFileInformation sfInfo=new SignFileInformation();
			sfInfo=list.get(0);
			sfInfo.setAudiStatus(2);
			sfInfoService.save(sfInfo);
		}
		///调OA接口给数据
		logger.debug("Running in updateFSAudiStateListener end!");
	}

}
