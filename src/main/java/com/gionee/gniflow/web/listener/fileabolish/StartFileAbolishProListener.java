/**
 * @(#) ProcessStartListener.java Created on 2014年9月1日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.listener.fileabolish;

import java.util.Date;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import com.gionee.gniflow.biz.model.SignFileInformation;
import com.gionee.gniflow.biz.service.SignFileInformationService;
import com.gionee.gniflow.constant.BPMConstant;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.web.util.SpringTools;

/**
 * The class <code>ProcessStartListener</code>
 *
 * @author lipw
 * @version 1.0
 */
public class StartFileAbolishProListener implements ExecutionListener {

	private static final long serialVersionUID = -1017192622888076202L;
	
	private RuntimeService reuntimeService;
	
	private SignFileInformationService signFileInformationService;
	
	private String executionId;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		signFileInformationService = (SignFileInformationService) SpringTools.getBean(SignFileInformationService.class);
		reuntimeService = execution.getEngineServices().getRuntimeService();
		executionId = execution.getId();
		//获取相关参数，插入 sign_file_information表
		SignFileInformation fileInfo = new SignFileInformation();
		
		//拟定人
		fileInfo.setCreateBy(getVariable("createBy"));
		fileInfo.setCreaterName(getVariable("createrName"));
		
		//公司，部门
		fileInfo.setOrgId(Integer.parseInt(getVariable("orgId")));
		fileInfo.setOrgName(getVariable("orgName"));
		fileInfo.setCompanyId(Integer.parseInt(getVariable("belongCompanyId")));
		fileInfo.setCompanyName(getVariable("belongCompany"));
		
		//文件信息
		fileInfo.setFileType(getVariable("fileType"));
		fileInfo.setFileOperateType(WebReqConstant.SIGN_FILE_OPERATE_TYPE_ABOLISH);
		fileInfo.setFileName(getVariable("fileName"));
		fileInfo.setFileNo(getVariable("fileNo"));
		fileInfo.setFileVersion(getVariable("version"));
		fileInfo.setFileVersionState(getVariable("versionState"));
		
		//流程
		fileInfo.setProcInstId(execution.getProcessInstanceId());
		//审核字段
		fileInfo.setAudiStatus(WebReqConstant.SIGN_FILE_PRO_AUDIING);
		
		fileInfo.setStatus(BPMConstant.STATUS_NORMAL);
		fileInfo.setCreateTime(new Date());
		fileInfo.setCreateBy(getVariable("createBy"));
		fileInfo.setCreaterName(getVariable("createrName"));
		
		signFileInformationService.save(fileInfo);
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
