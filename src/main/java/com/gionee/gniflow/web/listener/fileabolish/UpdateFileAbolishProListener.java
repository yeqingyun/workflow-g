/**
 * @(#) UpdateFileAbolishProListener.java Created on 2014年9月1日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.listener.fileabolish;

import java.util.Date;
import java.util.List;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.util.CollectionUtils;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.model.SignFileInformation;
import com.gionee.gniflow.biz.service.SignFileInformationService;
import com.gionee.gniflow.constant.BPMConstant;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.web.util.SpringTools;

/**
 * The class <code>UpdateFileAbolishProListener</code>
 *
 * @author lipw
 * @version 1.0
 */
public class UpdateFileAbolishProListener implements ExecutionListener {

	private static final long serialVersionUID = -4759051359522932496L;

	private RuntimeService reuntimeService;
	
	private SignFileInformationService signFileInformationService;
	
	private String executionId;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		signFileInformationService = (SignFileInformationService) SpringTools.getBean(SignFileInformationService.class);
		reuntimeService = execution.getEngineServices().getRuntimeService();
		executionId = execution.getId();
		
		QueryMap queryMap = new QueryMap();
		queryMap.put("audiStatus", WebReqConstant.SIGN_FILE_PRO_AUDIING);//审核中
		queryMap.put("fileNo", getVariable("fileNo"));//文件编号
		queryMap.put("status", BPMConstant.STATUS_NORMAL);
		
		List<SignFileInformation> reuslts = signFileInformationService.querySignFileInformation(queryMap);
		if (!CollectionUtils.isEmpty(reuslts)) {
			SignFileInformation signFileInfo = reuslts.get(0);
			signFileInfo.setUpdateTime(new Date());
			signFileInfo.setStatus(BPMConstant.STATUS_DELETED);
			signFileInfo.setAudiStatus(WebReqConstant.SIGN_FILE_PRO_AUDIED);
			signFileInformationService.save(signFileInfo);
		}
	}
	
	/**
	 * 获取变量值
	 * @param key
	 * @return
	 */
	private String getVariable(String key){
		return (String) reuntimeService.getVariable(executionId, key);
	}
}
