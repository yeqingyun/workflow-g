/**
 * @(#) SyncFileDataToOAListener.java Created on 2014年9月1日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.listener.fileabolish;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.task.Attachment;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.gionee.gnif.GnifException;
import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.model.SignFileInformation;
import com.gionee.gniflow.biz.service.SignFileInformationService;
import com.gionee.gniflow.constant.BPMConstant;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.web.util.HttpClientUtil;
import com.gionee.gniflow.web.util.PropertyHolder;
import com.gionee.gniflow.web.util.SpringTools;

/**
 * The class <code>SyncFileDataToOAListener</code>
 *
 * @author lipw
 * @version 1.0
 */
public class SyncFileDataToOAListener implements ExecutionListener {

	private static final long serialVersionUID = -1017192622888076202L;
	
	private RuntimeService reuntimeService;
	
	private TaskService taskService;
	
	private SignFileInformationService signFileInformationService;
	
	private String executionId;
	
	private String processInstanceId;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		signFileInformationService = (SignFileInformationService) SpringTools.getBean(SignFileInformationService.class);
		reuntimeService = execution.getEngineServices().getRuntimeService();
		taskService = execution.getEngineServices().getTaskService();
		executionId = execution.getId();
		processInstanceId = execution.getProcessInstanceId();
		
		SignFileInformation signFileInfo = null;
		//获取文件的处理信息，调用OA的接口
		QueryMap queryMap = new QueryMap();
		queryMap.put("audiStatus", WebReqConstant.SIGN_FILE_PRO_AUDIING);//审核中
		queryMap.put("fileNo", getVariable("fileNo"));//文件编号
		queryMap.put("status", BPMConstant.STATUS_NORMAL);
		
		List<SignFileInformation> reuslts = signFileInformationService.querySignFileInformation(queryMap);
		if (!CollectionUtils.isEmpty(reuslts)) {
			signFileInfo = reuslts.get(0);
		}
		//判断体系经理是否同意
		String managementAudi = getVariable("sysManagerAudi");
		if (Integer.parseInt(managementAudi) == WebReqConstant.AGREE_YES) {
			//把数据同步给OA
			String postUrl= PropertyHolder.getContextProperty(WebReqConstant.HR_DOCUEMNT_SYNCDATA_KEY);
			Map<String,String> reqData = new HashMap<String,String>();
			reqData.put("document.comId", String.valueOf(signFileInfo.getCompanyId()));
			reqData.put("document.deptId", String.valueOf(signFileInfo.getOrgId()));
			reqData.put("document.docNo", signFileInfo.getFileNo());
			reqData.put("document.docName", signFileInfo.getFileName());
			reqData.put("document.docName", signFileInfo.getFileName());
			reqData.put("document.version", signFileInfo.getFileVersion()+signFileInfo.getFileVersionState());
			reqData.put("document.status", "3");
			
			List<Attachment> attachmentList = taskService.getProcessInstanceAttachments(processInstanceId);
			if (!CollectionUtils.isEmpty(attachmentList)) {
				reqData.put("document.fileNo", attachmentList.get(attachmentList.size() - 1).getDescription());
			}
			
			String result = HttpClientUtil.sendPostRequest(postUrl, reqData, WebReqConstant.DEFAULT_CHAR_SET);
			JSONObject jsonObj = JSONObject.parseObject(result);
			if (StringUtils.isEmpty(jsonObj.getString("success"))) {
				throw new GnifException("与OA同步数据失败,处理任务失败!");
			}
			
			//更新状态
			signFileInfo.setAudiStatus(WebReqConstant.SIGN_FILE_PRO_AUDIED);
			signFileInformationService.save(signFileInfo);
		} else {
			//不同意，将SignFileInfo对象设置为已删除
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
