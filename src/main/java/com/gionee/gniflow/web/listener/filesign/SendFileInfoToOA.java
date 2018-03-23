package com.gionee.gniflow.web.listener.filesign;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.task.Attachment;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.gionee.gnif.GnifException;
import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.model.SignFileInformation;
import com.gionee.gniflow.biz.service.SignFileInformationService;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.web.util.HttpClientUtil;
import com.gionee.gniflow.web.util.PropertyHolder;
import com.gionee.gniflow.web.util.SpringTools;

public class SendFileInfoToOA	implements TaskListener {
	private Logger logger = LoggerFactory.getLogger(InsertFileSignInfoListener.class);
	
	private RuntimeService reuntimeService;
	
	private TaskService taskService;
	
	SignFileInformationService sfInfoService;
	/**发送文件信息到OA系统
	 * @author WJQ
	 */
	private static final long serialVersionUID = -4815472859681675119L;
	@Override
	public void notify(DelegateTask delegateTask) {
		//把发送邮件的部门传给OA
		logger.debug("Running in SendFileInfoToOA begin!");
		sfInfoService = (SignFileInformationService) SpringTools.getBean(SignFileInformationService.class);
		reuntimeService = delegateTask.getExecution().getEngineServices().getRuntimeService();
		String processInstanceId=delegateTask.getExecution().getProcessInstanceId();
		List<SignFileInformation> list = null;
		QueryMap queryMap = new QueryMap();
		queryMap.put("procInstId", processInstanceId);
		list = sfInfoService.querySignFileInformation(queryMap);
		String fileNo=null;
		SignFileInformation sfInfo = new SignFileInformation();
		if (!CollectionUtils.isEmpty(list)) {
			sfInfo = list.get(0);
			fileNo = sfInfo.getFileNo();
		}
		String postUrl = PropertyHolder.getContextProperty(WebReqConstant.HR_DOCUEMNT_EMAIL_NOTIFY_KEY);
		Object orgIds = reuntimeService.getVariable(delegateTask.getExecution().getId(), "EmailOrgIds");
		Map<String,String> reqData = new HashMap<String,String>();
		reqData.put("document.docNo", fileNo);
		reqData.put("document.remark", (String)orgIds);
		HttpClientUtil.sendPostRequest(postUrl, reqData, WebReqConstant.DEFAULT_CHAR_SET);
		
		
		//把文件信息传给OA
		Map<String,String> reqInfo= new HashMap<String,String>();
		reqInfo.put("document.comId",String.valueOf(sfInfo.getCompanyId().toString()));
		reqInfo.put("document.deptId",String.valueOf(sfInfo.getOrgId()));
		reqInfo.put("document.docNo", sfInfo.getFileNo());
		reqInfo.put("document.docName", sfInfo.getFileName());
		reqInfo.put("document.version", sfInfo.getFileVersion()+sfInfo.getFileVersionState());
	    taskService= delegateTask.getExecution().getEngineServices().getTaskService();
	    List<Attachment> attachmentList = taskService.getTaskAttachments(delegateTask.getId());
	    String endAttFileNo = attachmentList.get(attachmentList.size()- 1).getDescription();
		reqInfo.put("document.fileNo", endAttFileNo);
		if(sfInfo.getFileOperateType()==WebReqConstant.SIGN_FILE_OPERATE_TYPE_ADD){
			reqInfo.put("document.status", String.valueOf(1));
		}else if(sfInfo.getFileOperateType()==WebReqConstant.SIGN_FILE_OPERATE_TYPE_MODIFY){
			reqInfo.put("document.status", String.valueOf(2));
		}
		postUrl= PropertyHolder.getContextProperty(WebReqConstant.HR_DOCUEMNT_SYNCDATA_KEY);
		String result = HttpClientUtil.sendPostRequest(postUrl, reqInfo, WebReqConstant.DEFAULT_CHAR_SET);
		//调用失败就抛出异常
		JSONObject jsonObj = JSONObject.parseObject(result);
		String isSuccess = jsonObj.getString("success");
		if (StringUtils.isEmpty(isSuccess)) {
			throw new GnifException("与OA同步数据失败,处理任务失败!");
		}
		
		logger.debug("Running in SendFileInfoToOA End!");
	}
}
