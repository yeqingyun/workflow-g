package com.gionee.gniflow.web.listener.filesign;

import java.util.Date;
import java.util.List;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.model.SignFileInformation;
import com.gionee.gniflow.biz.service.SignFileInformationService;
import com.gionee.gniflow.constant.BPMConstant;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.web.util.SpringTools;

public class UpdateTaskInfoListener  implements ExecutionListener {
	/** 跟新修改后的数据到会签表
	 *@author wjq
	 */
	private static final long serialVersionUID = -7185259501884166839L;
	private Logger logger = LoggerFactory.getLogger(InsertFileSignInfoListener.class);
	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		logger.debug("Running in UpdateTaskInfoListener begin!");
		SignFileInformationService sfService= (SignFileInformationService) SpringTools.getBean(SignFileInformationService.class);
		List<SignFileInformation> list = null;
		String processInstanceId=execution.getProcessInstanceId();
		QueryMap queryMap= new QueryMap();
		queryMap.put("procInstId", processInstanceId);
		list=sfService.querySignFileInformation(queryMap);
		if(!CollectionUtils.isEmpty(list)){
			SignFileInformation sfInfo=new SignFileInformation();
			sfInfo=list.get(0);
		    RuntimeService runtimeService= execution.getEngineServices().getRuntimeService();
		    sfInfo.setOrgCode((String) runtimeService.getVariable(execution.getId(), "orgCodeAbbreviation")); 
			sfInfo.setOrgId(Integer.parseInt((String)runtimeService.getVariable(execution.getId(), "orgId")));
			sfInfo.setOrgName((String) runtimeService.getVariable(execution.getId(), "orgName"));
			sfInfo.setFileNo((String) runtimeService.getVariable(execution.getId(), "filenumber"));
			sfInfo.setFileName((String) runtimeService.getVariable(execution.getId(), "filename"));
			//sfInfo.setFileOperateType(Integer.parseInt((String)runtimeService.getVariable(execution.getId(), "filestate")));
			sfInfo.setFileType((String) runtimeService.getVariable(execution.getId(), "filetype"));
			sfInfo.setFileVersion((String) runtimeService.getVariable(execution.getId(), "version"));
			sfInfo.setFileVersionState((String) runtimeService.getVariable(execution.getId(), "versionstate"));
			sfInfo.setProcInstId(execution.getProcessInstanceId());
			sfInfo.setPreFileVersionState((String) runtimeService.getVariable(execution.getId(), "beforeversionstate"));;
			sfInfo.setProtoFileVersion((String) runtimeService.getVariable(execution.getId(), "beforeversion"));
			sfInfo.setProtoFileNo((String) runtimeService.getVariable(execution.getId(), "filenumber"));
			String cid=(String) runtimeService.getVariable(execution.getId(), "belongcompany");
			if(StringUtils.isNotEmpty(cid)){
				sfInfo.setCompanyId(Integer.parseInt((String) runtimeService.getVariable(execution.getId(), "belongcompany")));
			}
			
			sfInfo.setCompanyName((String) runtimeService.getVariable(execution.getId(), "companyName"));
			//sfInfo.setCreaterName((String) runtimeService.getVariable(execution.getId(), "createName"));;
			//sfInfo.setCreaterNo((String) runtimeService.getVariable(execution.getId(), "createNo"));
	        sfInfo.setAudiStatus(WebReqConstant.SIGN_FILE_PRO_AUDIING);
	        sfInfo.setStatus(BPMConstant.STATUS_NORMAL);
	        sfInfo.setUpdateTime(new Date());
	        //sfInfo.setCreateDate(new Date());
			sfService.save(sfInfo);
		
		}
		
	}

}
