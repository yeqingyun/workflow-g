package com.gionee.gniflow.web.listener.filesign;

import java.util.Arrays;
import java.util.List;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.model.SignFileInformation;
import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.biz.service.SignFileInformationService;
import com.gionee.gniflow.web.util.SpringTools;

public class SendEmailToOrgs implements ExecutionListener{
	private Logger logger = LoggerFactory.getLogger(UpdateSignFileInfoListener.class);
	
	private RuntimeService reuntimeService;
	SignFileInformationService sfInfoService;
	private ProcessHelpService processHelpService;
	/**给所选部门发送邮件并更新审批状态
	 * @author wjq
	 */
	private static final long serialVersionUID = 1944091016121391413L;
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		logger.debug("Running in SendEmailToOrgs begin!");
		sfInfoService = (SignFileInformationService) SpringTools.getBean(SignFileInformationService.class);
		reuntimeService = execution.getEngineServices().getRuntimeService();
		String processInstanceId=execution.getProcessInstanceId();
		List<SignFileInformation> list = null;
		QueryMap queryMap = new QueryMap();
		queryMap.put("procInstId", processInstanceId);
		list = sfInfoService.querySignFileInformation(queryMap);
		if (!CollectionUtils.isEmpty(list)) {
			SignFileInformation sfInfo = new SignFileInformation();
			sfInfo = list.get(0);
			sfInfo.setAudiStatus(2);//审批通过
			sfInfoService.save(sfInfo);
		}
		
		//发送邮件
		Object orgIds = reuntimeService.getVariable(execution.getId(), "EmailOrgIds");
		if (orgIds != null) {
			String[] orgIdArr = ((String)orgIds).split(",");
			processHelpService.sendEmail4Orgs(Arrays.asList(orgIdArr), "Email Title", "Email Content!");
		}
		logger.debug("Running in SendEmailToOrgs end!");
	}
	

}
