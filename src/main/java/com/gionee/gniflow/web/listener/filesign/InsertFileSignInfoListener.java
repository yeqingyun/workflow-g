package com.gionee.gniflow.web.listener.filesign;

import java.util.Date;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gniflow.biz.model.SignFileInformation;
import com.gionee.gniflow.biz.service.SignFileInformationService;
import com.gionee.gniflow.constant.BPMConstant;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.web.util.SpringTools;


/**
 * 插入文档会签相关信息
 *
 * @author wjq
 * @version 1.0
 */
public class InsertFileSignInfoListener implements ExecutionListener{
	private Logger logger = LoggerFactory.getLogger(InsertFileSignInfoListener.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -8145828351194753850L;


	@Override
	public void notify(DelegateExecution execution) throws Exception {
		logger.debug("Running in InsertFileSignInfoListener begin!");
		SignFileInformationService sfService= (SignFileInformationService) SpringTools.getBean(SignFileInformationService.class);
		SignFileInformation sfInfo = new SignFileInformation();
		RuntimeService runtimeService= execution.getEngineServices().getRuntimeService();
		sfInfo.setOrgCode((String) runtimeService.getVariable(execution.getId(), "orgCodeAbbreviation")); 
		sfInfo.setOrgId(Integer.parseInt((String)runtimeService.getVariable(execution.getId(), "orgId")));
		sfInfo.setOrgName((String) runtimeService.getVariable(execution.getId(), "orgName"));
		sfInfo.setFileNo((String) runtimeService.getVariable(execution.getId(), "filenumber"));
		sfInfo.setFileName((String) runtimeService.getVariable(execution.getId(), "filename"));
		sfInfo.setFileOperateType(Integer.parseInt((String)runtimeService.getVariable(execution.getId(), "filestate")));
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
		sfInfo.setCreaterName((String) runtimeService.getVariable(execution.getId(), "createName"));
		sfInfo.setCreateBy((String) runtimeService.getVariable(execution.getId(), "createNo"));
        sfInfo.setAudiStatus(WebReqConstant.SIGN_FILE_PRO_AUDIING);
        sfInfo.setStatus(BPMConstant.STATUS_NORMAL);
        sfInfo.setUpdateTime(new Date());
        sfInfo.setCreateTime(new Date());
		sfService.save(sfInfo);
		
		logger.debug("Running in InsertFileSignInfoListener end!");
	}

}
