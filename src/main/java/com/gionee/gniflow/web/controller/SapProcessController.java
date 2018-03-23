package com.gionee.gniflow.web.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.persistence.entity.HistoricDetailVariableInstanceUpdateEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gionee.gnif.dto.MessageDto;
import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.AppUser;
import com.gionee.gnif.util.AppContext;
import com.gionee.gnif.web.util.EntryFactory;
import com.gionee.gnif.web.util.ResponseFactory;
import com.gionee.gniflow.biz.model.SapReqSheetAttachment;
import com.gionee.gniflow.biz.model.SapRequisitionSheet;
import com.gionee.gniflow.biz.service.SapFlowService;
import com.gionee.gniflow.biz.service.SapReqSheetAttachmentService;
import com.gionee.gniflow.biz.service.SapRequisitionSheetService;
import com.gionee.gniflow.constant.BPMConstant;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.dto.SapRequisitionTaskDto;
import com.gionee.gniflow.sap.rfc.RfcSapReqRelease;
import com.gionee.gniflow.sap.rfc.RfcSapReqRestRelease;
import com.gionee.gniflow.sap.rfc.RfcSyncSapReqSheet;
import com.gionee.gniflow.web.bmp.ProcessStep;
import com.gionee.gniflow.web.cmd.CheckNextTaskCompletedCmd;
import com.gionee.gniflow.web.cmd.CheckProcessEndCmd;
import com.gionee.gniflow.web.cmd.WithdrawTaskCmd;
import com.gionee.gniflow.web.easyui.AjaxJson;
import com.gionee.gniflow.web.easyui.GridPageData;
import com.gionee.gniflow.web.response.SapReqSheetAttachmentResponse;
import com.gionee.gniflow.web.response.SapRequisitionSheetResponse;
import com.gionee.gniflow.web.util.SapMessageUtil;

@RequestMapping("/sapspace")
@Controller
public class SapProcessController {
	
	private static Logger logger = LoggerFactory.getLogger(SapProcessController.class);
	
	//Global
	private static final String CONTEXT_RANDOM_VALUE = "rand";
	private static final String CONTEXT_APP_USER = "appuser";
	private static final String CURRENT_STEP = "currentStep";			//Task
	private static final String PROCESS_STEPS = "processSteps";			//Process Task
	
	private static final String PROCESS_STEP_ONE = "projectAudiTask";
	private static final String PROCESS_STEP_TWO = "generalManagerTask";
	private static final String PROCESS_STEP_THREE = "syncSapTask";
	
	private static final String SAP_REQ_SHEET_SER_KEY = "sapRequisitionSheetSer";
	
	private static final String IS_SAVE_SAP_SHEET_SER_KEY = "ser_saveSapSheetSer";//是否保存序列化对象
	
	private static final String APPROVAL_CODE_PERFIX = "000";
	
	@Value("${file.download.url}")
	private String fileDownLoadUrl;
	
	@Autowired
	private FormService formService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private IdentityService identityService;
	
	@Autowired
	private ManagementService managementService;
	
	@Autowired
    CommonsMultipartResolver multipartResolver;
	
	@Autowired
	private ProcessEngine processEngine;
	
	@Autowired
	private SapRequisitionSheetService sapRequisitionSheetService;
	
	@Autowired
	private SapReqSheetAttachmentService sapReqSheetAttachmentService;
	
	@Autowired
	private SapFlowService sapFlowService;
	

	@RequestMapping("/startProcess.html")
	public ModelAndView startSapProcess(){
		ProcessDefinition processDef = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionCategory(BPMConstant.SAP_PROCESS_REQ_CATAGORY).latestVersion().singleResult();
		ModelAndView model = new ModelAndView("/sapspace/startProcess");
		model.addObject("processDefId", processDef.getId());
		return model;
	}
	
	//启动流程
	@SuppressWarnings("rawtypes")
	@RequestMapping("/create/startProcess.html")
	@ResponseBody
	public ResponseEntity<MessageDto> createProcess(@RequestParam("processDefId") String processDefinitionId, 
			HttpServletRequest request) {
		try {
			Map<String, Object> formProperties = new HashMap<String, Object>();
			Map<String, String> attchmentMap = new LinkedHashMap<String,String>();

			Map<String, String[]> parameterMap = request.getParameterMap();
			Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
			for (Entry<String, String[]> entry : entrySet) {
				String key = entry.getKey();
				if (key.startsWith("fp_")) {
					formProperties.put(key.split("fp_")[1], entry.getValue()[0]);
				} else if (key.startsWith("attch_")) {
					String[] arrValue =  entry.getValue()[0].split("_");
					attchmentMap.put(arrValue[0], arrValue[1]);
				} else if (key.startsWith("gridJson_")){
					JSONObject jsonObject = JSON.parseObject(entry.getValue()[0]);
					List<Map> lists = JSONArray.parseArray(jsonObject.get("rows").toString(),  Map.class);
					formProperties.put(entry.getKey(), lists);
				}
			}
			
			//把开始节点的SAP单据对象存到流程变量中，便于历史显示
			SapRequisitionSheet sapSheet = sapRequisitionSheetService.getSapRequisitionSheet((String)formProperties.get("preqNo"), 
					Integer.parseInt((String)formProperties.get("preqItem")));
			if (sapSheet != null) {
				formProperties.put(SAP_REQ_SHEET_SER_KEY, sapSheet);
			}

			AppUser user = AppContext.getCurrentAppUser();
			// 当流程启动时，把当前登录的用户保存到 activiti:initiator="xx"的变量名中
			identityService.setAuthenticatedUserId(user.getAccount());

			ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinitionId, formProperties);
			
			// 处理文件
			for (Map.Entry<String, String> entry : attchmentMap.entrySet()) {
				saveAttachment(null, processInstance.getId(), entry.getKey(), entry.getValue());
			}
			
			sapSheet = new SapRequisitionSheet();
			//更新单据的状态为审核中
			for (Map.Entry<String, Object> entry : formProperties.entrySet()) {
				if (entry.getKey().equals("preqNo")) {
					sapSheet.setPreqNo((String)entry.getValue());
				}
				if (entry.getKey().equals("preqItem")) {
					sapSheet.setPreqItem(Integer.parseInt((String)entry.getValue()));
				}
			}
			//设置装填为审核中
			sapSheet.setStatus(WebReqConstant.PROCURE_SHEET_AUDIING);
			//设置流程实例ID
			sapSheet.setProcInstId(processInstance.getId());
			
			sapRequisitionSheetService.update(sapSheet);
			
			return EntryFactory.create(new MessageDto("启动流程成功！"));
		} catch (Exception e) {
			e.printStackTrace();
			return EntryFactory.create(new MessageDto("启动流程失败！"));
		}
	}
	
	//批量处理流程
	@RequestMapping("/batchStartProcess.html")
	@ResponseBody
	public ResponseEntity<MessageDto> batchStartProcess(@RequestParam("processDefId") String processDefinitionId, 
			HttpServletRequest request) {
		try {
			List<Map<String,Object>> fpParams = new ArrayList<Map<String,Object>>();

			Map<String, String[]> parameterMap = request.getParameterMap();
			
			Map<String, Object> tempMap = null;
			for (Entry<String, String[]> entry : parameterMap.entrySet()) {
				String key = entry.getKey();
				if (key.startsWith("reqSheet")) {
					tempMap = new LinkedHashMap<String, Object>();
					String[] valueArr = entry.getValue()[0].split("_");
					tempMap.put("preqNo", valueArr[0]);
					tempMap.put("preqItem", valueArr[1]);
					
					fpParams.add(tempMap);
				}
				
			}
			
			for (Map<String, Object> map : fpParams) {
				map.put("curUserId", request.getParameter("curUserId"));
				//把开始节点的SAP单据对象存到流程变量中，便于历史显示
				SapRequisitionSheet sapSheet = sapRequisitionSheetService.getSapRequisitionSheet((String)map.get("preqNo"), 
						Integer.parseInt((String)map.get("preqItem")));
				if (sapSheet != null) {
					map.put(SAP_REQ_SHEET_SER_KEY, sapSheet);
				}
			}
			
			AppUser user = AppContext.getCurrentAppUser();
			
			for (Map<String,Object> map : fpParams) {
				// 当流程启动时，把当前登录的用户保存到 activiti:initiator="xx"的变量名中
				identityService.setAuthenticatedUserId(user.getAccount());
				
				ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinitionId, map);
				
				//数据库中查询附件，然后绑定到Task
				QueryMap queryMap = new QueryMap();
				queryMap.put("preqNo", map.get("preqNo"));
				queryMap.put("preqItem", map.get("preqItem"));
				queryMap.put("delFlag", WebReqConstant.DEL_NO);
				List<SapReqSheetAttachment> attachList = sapReqSheetAttachmentService.getAllSapReqSheetAttachment(queryMap);
				
				for (SapReqSheetAttachment attach : attachList) {
					saveAttachment(null, processInstance.getId(), attach.getFileNo(), attach.getFileName());
				}
				
				//更新单据的状态为审核中
				SapRequisitionSheet sapSheet = new SapRequisitionSheet();
				sapSheet.setPreqNo((String)map.get("preqNo"));
				sapSheet.setPreqItem(Integer.parseInt((String)map.get("preqItem")));
				//设置装填为审核中
				sapSheet.setStatus(WebReqConstant.PROCURE_SHEET_AUDIING);
				//设置流程实例ID
				sapSheet.setProcInstId(processInstance.getId());
				
				sapRequisitionSheetService.update(sapSheet);
			}
			return EntryFactory.create(new MessageDto("启动流程成功！"));
		} catch (Exception e) {
			e.printStackTrace();
			return EntryFactory.create(new MessageDto("启动流程失败！"));
		}
	}
	
	//处理任务
	@RequestMapping("/complete/{taskId}.html")
	public ResponseEntity<MessageDto> completeTask(@PathVariable("taskId") String taskId, HttpServletRequest request) {
        Map<String, Object> formProperties = new HashMap<String, Object>();
        Map<String, String> attchmentMap = new LinkedHashMap<String,String>();
        
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

		Map<String, String[]> parameterMap = request.getParameterMap();
		Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
		for (Entry<String, String[]> entry : entrySet) {
			String key = entry.getKey();
			
			if (key.startsWith("fp_")) {
				String[] paramSplit = key.split("fp_");
				formProperties.put(paramSplit[1], entry.getValue()[0]);
			} else if (key.startsWith("attch_")) {
				String[] arrValue =  entry.getValue()[0].split("_");
				attchmentMap.put(arrValue[0], arrValue[1]);
			}
		}
		
		String isSer = request.getParameter(IS_SAVE_SAP_SHEET_SER_KEY);
		if (!StringUtils.isEmpty(isSer)) {
			//把开始节点的SAP单据对象存到流程变量中，便于历史显示
			SapRequisitionSheet sapSheet = sapRequisitionSheetService.getSapRequisitionSheet((String)formProperties.get("preqNo"), 
					Integer.parseInt((String)formProperties.get("preqItem")));
			if (sapSheet != null) {
				formProperties.put(SAP_REQ_SHEET_SER_KEY, sapSheet);
			}
		}
		
		//处理文件
		for (Map.Entry<String, String> entry : attchmentMap.entrySet()) {
			saveAttachment(task.getId(), null, entry.getKey(), entry.getValue());
		}
		
        identityService.setAuthenticatedUserId(AppContext.getCurrentAccount());
        
        String projectAudi = StringUtils.isEmpty((String)formProperties.get("projectAudi")) 
        		? "" : (String)formProperties.get("projectAudi");
        String generalManagerAudi = StringUtils.isEmpty((String)formProperties.get("generalManagerAudi")) 
        		? "" : (String)formProperties.get("generalManagerAudi");
        
        if (projectAudi.equals(BPMConstant.SAP_AUDI_NOAGREE) 
				|| generalManagerAudi.equals(BPMConstant.SAP_AUDI_NOAGREE)) {
			 taskService.complete(taskId, formProperties);
			 return EntryFactory.create(new MessageDto("任务处理成功"));
		}
        
        //调用SAP的接口，获取审批结果.根据结果传递
		RfcSapReqRelease reqRelease = new RfcSapReqRelease();
		//获取审批代码
		SapRequisitionSheet sapReqSheet = sapRequisitionSheetService.getSapRequisitionSheet(task.getProcessInstanceId());
		Map<String,String> sapMessage = null;
		if (sapReqSheet != null ) {
			String preqNo = sapReqSheet.getPreqNo();
			Integer preqItem = sapReqSheet.getPreqItem();
			String relCode = null;
			//调整请求不需要审批，直接完成任务
			boolean isNotSyncTaskSubmit =  formProperties.get("curStep") == null ? false : true;
			if (isNotSyncTaskSubmit) {
				if (formProperties.get("curStep").equals("second")) {
					relCode = sapReqSheet.getFrgc1();
					try {
						sapMessage = reqRelease.sapReqSheetAudi(preqNo, APPROVAL_CODE_PERFIX + preqItem, relCode);
					} catch (Exception e) {
						e.printStackTrace();
						return EntryFactory.create(new MessageDto("任务处理失败!"));
					}
					//说明审核不通过
					if (sapMessage != null) {
						formProperties.put("projectAudi", 1);//设置为不同意的值
						taskService.complete(taskId, formProperties);
						return EntryFactory.create(new MessageDto(SapMessageUtil.formatSapMessage(sapMessage)));
					}
				} else if (formProperties.get("curStep").equals("third")) {
					relCode = sapReqSheet.getFrgc2();
					try {
						sapMessage = reqRelease.sapReqSheetAudi(preqNo, APPROVAL_CODE_PERFIX + preqItem, relCode);
					} catch (Exception e) {
						e.printStackTrace();
						return EntryFactory.create(new MessageDto("任务处理失败!"));
					}
					//说明审核不通过
					if (sapMessage != null) {
						formProperties.put("generalManagerAudi", 1);//设置为不同意的值
						taskService.complete(taskId, formProperties);
						
						return EntryFactory.create(new MessageDto(SapMessageUtil.formatSapMessage(sapMessage)));
					}
				}
			}
		}
		//完成任务
		taskService.complete(taskId, formProperties);
		return EntryFactory.create(new MessageDto("任务处理成功"));
	}
	
	@RequestMapping("/batchCompleteTask.html")
	public ResponseEntity<MessageDto> batchCompleteTask(@RequestParam("taskIds") String taskIds, HttpServletRequest request) {
        Map<String, Object> formProperties = new HashMap<String, Object>();
        Map<String, String> attchmentMap = new LinkedHashMap<String,String>();
        
        Map<String, String[]> parameterMap = request.getParameterMap();
		Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
		for (Entry<String, String[]> entry : entrySet) {
			String key = entry.getKey();
			
			if (key.startsWith("fp_")) {
				String[] paramSplit = key.split("fp_");
				formProperties.put(paramSplit[1], entry.getValue()[0]);
			} else if (key.startsWith("attch_")) {
				String[] arrValue =  entry.getValue()[0].split("_");
				attchmentMap.put(arrValue[0], arrValue[1]);
			}
		}
        
        String[] taskIdArr = taskIds.split(",");
        
        for (String taskId : taskIdArr) {
        	Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        	
        	
        	//处理文件
    		for (Map.Entry<String, String> entry : attchmentMap.entrySet()) {
    			saveAttachment(task.getId(), null, entry.getKey(), entry.getValue());
    		}
    		
			identityService.setAuthenticatedUserId(AppContext.getCurrentAccount());
			
			//判断是不是不同意
			boolean isProNoAgree = formProperties.get("projectAudi") == null ? true : false;
			boolean isGenNoAgree = formProperties.get("generalManagerAudi") == null ? true : false;
			if (!isProNoAgree) {
				if (formProperties.get("projectAudi").equals(BPMConstant.SAP_AUDI_NOAGREE)) {
					 taskService.complete(taskId, formProperties);
					 return EntryFactory.create(new MessageDto("任务处理成功"));
				}
			}
			if (!isGenNoAgree) {
				if (formProperties.get("generalManagerAudi").equals(BPMConstant.SAP_AUDI_NOAGREE)) {
					 taskService.complete(taskId, formProperties);
					 return EntryFactory.create(new MessageDto("任务处理成功"));
				}
			}
			// 调用SAP的接口，获取审批结果.根据结果传递
			RfcSapReqRelease reqRelease = new RfcSapReqRelease();
			//获取审批代码
			SapRequisitionSheet sapReqSheet = sapRequisitionSheetService.getSapRequisitionSheet(task.getProcessInstanceId());
			Map<String,String> sapMessage = null;
			if (sapReqSheet != null ) {
				String preqNo = sapReqSheet.getPreqNo();
				Integer preqItem = sapReqSheet.getPreqItem();
				String relCode = null;
				if (formProperties.get("curStep").equals("second")) {
					relCode = sapReqSheet.getFrgc1();
					try {
						sapMessage = reqRelease.sapReqSheetAudi(preqNo, APPROVAL_CODE_PERFIX + preqItem, relCode);
					} catch (Exception e) {
						e.printStackTrace();
						return EntryFactory.create(new MessageDto("任务处理失败!"));
					}
					//说明审核不通过
					if (sapMessage != null) {
						formProperties.put("projectAudi", 1);//设置为不同意的值
						taskService.complete(taskId, formProperties);
						return EntryFactory.create(new MessageDto(SapMessageUtil.formatSapMessage(sapMessage)));
					}
				} else if (formProperties.get("curStep").equals("third")) {
					relCode = sapReqSheet.getFrgc2();
					try {
						sapMessage = reqRelease.sapReqSheetAudi(preqNo, APPROVAL_CODE_PERFIX + preqItem, relCode);
					} catch (Exception e) {
						e.printStackTrace();
						return EntryFactory.create(new MessageDto("任务处理失败!"));
					}
					//说明审核不通过
					if (sapMessage != null) {
						formProperties.put("generalManagerAudi", 1);//设置为不同意的值
						taskService.complete(taskId, formProperties);
						
						return EntryFactory.create(new MessageDto(SapMessageUtil.formatSapMessage(sapMessage)));
					}
				}
			}
			//完成任务
	        taskService.complete(taskId, formProperties);
        }
		return EntryFactory.create(new MessageDto("任务处理成功"));
	}
	
	@RequestMapping("/batchCompleteSyncTask.html")
	@ResponseBody
	public AjaxJson batchCompleteSyncTask(@RequestParam("taskIds") String taskIds, HttpServletRequest request) {
        Map<String, Object> formProperties = new HashMap<String, Object>();
        Map<String, String> attchmentMap = new LinkedHashMap<String,String>();
        
        Map<String, String[]> parameterMap = request.getParameterMap();
		Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
		for (Entry<String, String[]> entry : entrySet) {
			String key = entry.getKey();
			
			if (key.startsWith("fp_")) {
				String[] paramSplit = key.split("fp_");
				formProperties.put(paramSplit[1], entry.getValue()[0]);
			} else if (key.startsWith("attch_")) {
				String[] arrValue =  entry.getValue()[0].split("_");
				attchmentMap.put(arrValue[0], arrValue[1]);
			}
		}
        
        String[] taskIdArr = taskIds.split(",");
        
        String isSer = request.getParameter("ser_saveSapSheetSer");
        
        for (String taskId : taskIdArr) {
        	Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        	
        	//处理文件
    		for (Map.Entry<String, String> entry : attchmentMap.entrySet()) {
    			saveAttachment(task.getId(), null, entry.getKey(), entry.getValue());
    		}
    		
			identityService.setAuthenticatedUserId(AppContext.getCurrentAccount());
			
			//判断是不是不同意
			boolean isProNoAgree = formProperties.get("projectAudi") == null ? true : false;
			boolean isGenNoAgree = formProperties.get("generalManagerAudi") == null ? true : false;
			
			if (!isProNoAgree) {
				if (formProperties.get("projectAudi").equals(BPMConstant.SAP_AUDI_NOAGREE)) {
					 taskService.complete(taskId, formProperties);
				}
			} else if (!isGenNoAgree) {
				if (formProperties.get("generalManagerAudi").equals(BPMConstant.SAP_AUDI_NOAGREE)) {
					 taskService.complete(taskId, formProperties);
				}
			} else {
				SapRequisitionSheet sapReqSheet = sapRequisitionSheetService.getSapRequisitionSheet(task.getProcessInstanceId());
				
				if (!StringUtils.isEmpty(isSer)) {
					//把开始节点的SAP单据对象存到流程变量中，便于历史显示
					SapRequisitionSheet sapSheet = sapRequisitionSheetService.getSapRequisitionSheet(sapReqSheet.getPreqNo(), 
							sapReqSheet.getPreqItem());
					if (sapSheet != null) {
						formProperties.put(SAP_REQ_SHEET_SER_KEY, sapSheet);
					}
				}
				//完成任务
		        taskService.complete(taskId, formProperties);
			}
        }
		return new AjaxJson(true,"任务处理成功");
	}
	
	//获取流程开始节点表单
	@RequestMapping("/start/form.html")
	public ModelAndView starthtml(@RequestParam("procdefId") String processDefinitionId,
			@RequestParam("preqNo") String preqNo,
			@RequestParam("preqItem") Integer preqItem) {
		
		ModelAndView model = new ModelAndView("/sapspace/procureProcessTemplate");
		
		BpmnModel bpmModel = repositoryService.getBpmnModel(processDefinitionId);
		
		List<org.activiti.bpmn.model.Process> processList = bpmModel.getProcesses();
		for (org.activiti.bpmn.model.Process process : processList) {
			List<FlowElement> eleList = (List<FlowElement>) process.getFlowElements();
			for (FlowElement ele : eleList) {
				if (ele instanceof StartEvent) {
					StartEvent startEle = (StartEvent) ele;
					logger.debug("[startEle.getId()]-->" + startEle.getId());
					//开始节点
					model.addObject(CURRENT_STEP, startEle.getId());
					break;
				}
			}
			
		}
		
		model = initGlobalModel(model);
		//根据采购单号，行号查找对象
		SapRequisitionSheet sapRequisitionSheet = sapRequisitionSheetService.getSapRequisitionSheet(preqNo, preqItem);
		if (sapRequisitionSheet != null) {
			model.addObject("sapRequisitionSheet", sapRequisitionSheet);
		}
		
		return model;
	}
	
	//获取处理任务表单
	@RequestMapping("/task/form.html")
	@ResponseBody
	public ModelAndView taskJson(@RequestParam("taskId") String taskId) {
		
		ModelAndView model = new ModelAndView("/sapspace/procureProcessTemplate");
		
		TaskEntity task = (TaskEntity) taskService.createTaskQuery().taskId(taskId).singleResult();
		
//		Map<String,Object> map = task.getExecution().getProcessVariables();
        
//		model.addAllObjects(task.getExecution().getProcessVariables());
        
        model = this.getHisProcesVariable(model, task.getProcessInstanceId());
        
        model = initGlobalModel(model);
        
        model.addObject(CURRENT_STEP, task.getTaskDefinitionKey());
        
        //根据流程实例ID，查找业务对象
        SapRequisitionSheet sapReqSheet = sapRequisitionSheetService.getSapRequisitionSheet(task.getProcessInstanceId());
        if (sapReqSheet != null) {
			model.addObject("sapRequisitionSheet", sapReqSheet);
		}
        
		return model;
	}
	
	//获取处理任务表单
	@RequestMapping("/task/viewHisform.html")
	@ResponseBody
	public Object viewHisTaskForm(@RequestParam("processInstanceId") String processInstanceId) {
		
		ModelAndView model = new ModelAndView("/sapspace/procureProcessTemplate");
		 
        model = this.getHisProcesVariable(model, processInstanceId);
        
        model = initGlobalModel(model);
        
		//根据流程实例ID，查找业务对象
		QueryMap queryMap = new QueryMap();
		queryMap.put("procInstId", processInstanceId);
		queryMap.put("delflag", WebReqConstant.DEL_NO);
		List<SapRequisitionSheet> list = sapRequisitionSheetService.getAllSapRequisitionSheet(queryMap);
		if (list != null) {
			model.addObject("sapRequisitionSheet", list.get(0));
		}
		return model;
	}
	
	/** 根据用户查询待办任务 */
	@RequestMapping("/personalTask.json")
	@ResponseBody
	public GridPageData<SapRequisitionTaskDto> personalTask(QueryMap queryMap) {

		queryMap.put("assignee", AppContext.getCurrentAccount());
		queryMap.put("delflag", WebReqConstant.DEL_NO);
		queryMap.put("procDefId", BPMConstant.SAP_REQ_PRO_DEF_KEY);
		
        List<SapRequisitionTaskDto> taskList = sapFlowService.queryPageSapRequisitionSheet4UserTask(queryMap);
        
        Integer total = sapFlowService.countSapRequisitionSheet4UserTask(queryMap);
		
		return new GridPageData<SapRequisitionTaskDto>(taskList, (long)total);
	}
	
	/**
	 * 根据附件的attachmentId以及流程实例processInstanceId，删除流程附件
	 * @param taskId
	 * @param processInstanceId
	 * @return
	 */
	@RequestMapping("/task/delAttachment.json")
	public ResponseEntity<MessageDto> deleteAttachment(@RequestParam String attachmentId){
		ResponseEntity<MessageDto> re = null;
		try {
			taskService.deleteAttachment(attachmentId);
			re = EntryFactory.create(new MessageDto("任务处理成功"));
		} catch (Exception e) {
			re = EntryFactory.create(new MessageDto("任务处理失败", false));
		}
		return re;
	}
	
	/**
	 * 为任务添加附件
	 * @param myfiles
	 * @param processInstance
	 */
	private void saveAttachment(String taskId, String processInstanceId, String fileNo, String fileName){
		//把上传文件的文件编号存到DESCRIPTION，URL字段
        Attachment attachment = taskService.createAttachment(null, taskId, processInstanceId, fileName, fileNo, fileDownLoadUrl + fileNo);
		
		taskService.saveAttachment(attachment);
	}
	
	
	@RequestMapping("/getSapRequisitionSheet.json")
	@ResponseBody
	public GridPageData<SapRequisitionSheetResponse> getProductJosn(QueryMap queryMap){
		//只查询自己创建的单
		//先取消点，SAP的账号只有一个，无法区分人员
//		String curUserAccount = AppContext.getCurrentAccount();
//		String sapAccount = sapFlowService.getSapAccount4CurUserAccount(curUserAccount);
//		if (sapAccount != null) {
//			queryMap.put("createdBy", sapAccount);
//		} else {
//			queryMap.put("createdBy", curUserAccount);
//		}
		queryMap.put("status", WebReqConstant.PROCURE_SHEET_TOBEAUDI);
		queryMap.put("delflag", WebReqConstant.DEL_NO);
		
		List<SapRequisitionSheet> list = sapRequisitionSheetService.getPageSapRequisitionSheet(queryMap);
		long count = sapRequisitionSheetService.getPageCount(queryMap);
		
		return new GridPageData<SapRequisitionSheetResponse>(ResponseFactory.convertList(list, SapRequisitionSheetResponse.class), count);
	}
	
	/**
	 * Sap采购单处理上传文件后的处理逻辑
	 * @return
	 */
	@RequestMapping("/uploadCallback.json")
	public void uploadFileCallBack(HttpServletRequest request){
		SapReqSheetAttachment attachment = new SapReqSheetAttachment();
		String fileNo = request.getParameter("fileNo");
		String fileName = null;
		try {
			fileName = URLDecoder.decode(request.getParameter("fileName"),"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String tempParams = request.getParameter("tempParams");

		try {
			tempParams = URLDecoder.decode(tempParams, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String[] params = tempParams.split(",");
		for (int i = 0; i < params.length; i++) {
			String ss[] = params[i].split(":");
			if (ss[0].equals("preqNo")) {
				attachment.setPreqNo(ss[1]);
			}
			if (ss[0].equals("preqItem")) {
				attachment.setPreqItem(Integer.parseInt(ss[1]));
			}
		}
		
		attachment.setFileNo(fileNo);
		attachment.setFileName(fileName);
		
		attachment.setDelFlag(WebReqConstant.DEL_NO);
		
		logger.debug(attachment.toString());
		
		sapReqSheetAttachmentService.save(attachment);
	}
	
	/**  删除附件 */
	@RequestMapping("/delAttachment.json")
	@ResponseBody
	public AjaxJson delAttachment(@RequestParam("preqNo") String preqNo,@RequestParam("preqItem") String preqItem,
			@RequestParam("fileNo") String fileNo) {
		AjaxJson ajaxJson = null;
		try {
			sapReqSheetAttachmentService.logicDelete(preqNo, Integer.parseInt(preqItem), fileNo);
			ajaxJson = new AjaxJson(true, "删除附件成功!");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = new AjaxJson(false, "删除附件失败!");
		}
		return ajaxJson;
	}
	
	/** 加载附件 */
	@RequestMapping("/loadAttachments.json")
	@ResponseBody
	public GridPageData<SapReqSheetAttachmentResponse> loadAttachments(@RequestParam(value="preqNo") String preqNo,
			@RequestParam(value="preqItem") String preqItem) {
		if (StringUtils.isEmpty(preqNo) && StringUtils.isEmpty(preqItem)) {
			return new GridPageData<SapReqSheetAttachmentResponse>(
					new ArrayList<SapReqSheetAttachmentResponse>(),0L);
		}
		QueryMap queryMap = new QueryMap();
		if (!StringUtils.isEmpty(preqNo)) {
			queryMap.put("preqNo", preqNo);
		}
		if (!StringUtils.isEmpty(preqItem)) {
			queryMap.put("preqItem", preqItem);
		}
		queryMap.put("delFlag", WebReqConstant.DEL_NO);
		List<SapReqSheetAttachment> list = sapReqSheetAttachmentService.getAllSapReqSheetAttachment(queryMap);
		
		return new GridPageData<SapReqSheetAttachmentResponse>(
				ResponseFactory.convertList(list, SapReqSheetAttachmentResponse.class), (long)list.size());
	}
	
	/** 查询采购申请流程 */
	@RequestMapping("/searchProcess.json")
	@ResponseBody
	public GridPageData<SapRequisitionSheetResponse> getCompletedProcess(QueryMap queryMap) {
		// 只查询自己创建的单
		String curUserAccount = AppContext.getCurrentAccount();
//		String sapAccount = sapFlowService.getSapAccount4CurUserAccount(curUserAccount);
//		if (sapAccount != null) {
//			queryMap.put("createdBy", sapAccount);
//		}
		queryMap.put("curUserAccount", curUserAccount);
		queryMap.put("delflag", WebReqConstant.DEL_NO);
		
		queryMap.put("procDefId", BPMConstant.SAP_REQ_PRO_DEF_KEY);
		
		List<SapRequisitionSheet> sapReqSheetList = sapFlowService.queryPageSapRequisitionSheet4HisProcess(queryMap);
		
		Integer total = sapFlowService.countSapRequisitionSheet4HisProcess(queryMap);
		
		return new GridPageData<SapRequisitionSheetResponse>(
				ResponseFactory.convertList(sapReqSheetList, SapRequisitionSheetResponse.class), (long)total);
	}
	
	/** 采购人员查询采购单 */
	@RequestMapping("/searchProcureProcess.json")
	@ResponseBody
	public GridPageData<SapRequisitionSheetResponse> searchProcureProcess(QueryMap queryMap) {
		// 只查询自己创建的单
		queryMap.put("delflag", WebReqConstant.DEL_NO);
		
		queryMap.put("procDefId", BPMConstant.SAP_REQ_PRO_DEF_KEY);
		
		//之前
		//queryMap.put("status", WebReqConstant.PROCURE_SHEET_AUDIED);
		//List<SapRequisitionSheet> sapReqSheetList = sapFlowService.queryPageSapRequisitionSheet4HisProcess(queryMap);
		//Integer total = sapFlowService.countSapRequisitionSheet4HisProcess(queryMap);
		
		//update by lipw 2014-10-10
		List<SapRequisitionSheet> sapReqSheetList = sapFlowService.querySapReqSheet4ProcurementInquiry(queryMap);
		Integer total = sapFlowService.countSapReqSheet4ProcurementInquiry(queryMap);
		
		return new GridPageData<SapRequisitionSheetResponse>(
				ResponseFactory.convertList(sapReqSheetList, SapRequisitionSheetResponse.class), (long)total);
	}
	
	/** 查询需要同步的采购单据 */
	@RequestMapping("/searchSyncSheet.json")
	@ResponseBody
	public GridPageData<SapRequisitionSheetResponse> searchSyncSheet(QueryMap queryMap) {

		queryMap.put("status", WebReqConstant.PROCURE_SHEET_TOBEMODEFY);
		queryMap.put("delflag", WebReqConstant.DEL_NO);
		
//		queryMap.put("procDefId", SAP_REQ_PRO_DEF_ID);
		
		List<SapRequisitionSheet> sapReqSheetList = sapRequisitionSheetService.getPageSapRequisitionSheet(queryMap);
		
		Integer total = sapRequisitionSheetService.getPageCount(queryMap);
		
		return new GridPageData<SapRequisitionSheetResponse>(
				ResponseFactory.convertList(sapReqSheetList, SapRequisitionSheetResponse.class), (long)total);
	}
	
	/** 查询需要同步的采购单据 */
	@RequestMapping("/syncSapReqSheet.html")
	@ResponseBody
	public AjaxJson syncSapReqSheet(@RequestParam(value="preqNo") String preqNo,
			@RequestParam(value="preqItem") Integer preqItem) {
		AjaxJson ajaxJosn = new AjaxJson();
		
		RfcSyncSapReqSheet syncSheet = new RfcSyncSapReqSheet();
		try {
			List<SapRequisitionSheet> syncSheetList = syncSheet.getSyncSapReqSheets(preqNo, preqItem);
			sapRequisitionSheetService.updateSyncSapReqSheets(syncSheetList);
			
			ajaxJosn.setSuccess(true);
			ajaxJosn.setMsg("同步SAP采购单据数据成功!");
		} catch (Exception e) {
			logger.error(e.getMessage());
			ajaxJosn.setSuccess(false);
			ajaxJosn.setMsg("同步SAP采购单据数据失败!");
		}
		
		return ajaxJosn;
	}
	
	/** 根据用户查询已办任务 */
	@RequestMapping("/completedTask.json")
	@ResponseBody
	public GridPageData<HistoricTaskInstance> completedTask(@RequestParam("page") Integer page, 
			@RequestParam("rows") Integer rows) {
		String userId = AppContext.getCurrentAccount();
		
		HistoricTaskInstanceQuery  taskQuery = historyService.createHistoricTaskInstanceQuery();
		taskQuery.taskAssignee(userId);

		List<HistoricTaskInstance> historicTasks = taskQuery.finished().processDefinitionKeyLike(BPMConstant.SAP_REQ_PRO_DEF_KEY)
				.orderByHistoricTaskInstanceStartTime().desc().listPage(page-1, rows);
        
        Long total  = taskQuery.finished().processDefinitionKeyLike(BPMConstant.SAP_REQ_PRO_DEF_KEY).count();
		
		return new GridPageData<HistoricTaskInstance>(historicTasks, total);
	}
	
	/** 取回任务 */
	@RequestMapping("/taskRollback.html")
	@ResponseBody
	public AjaxJson completedTask(@RequestParam("taskId") String taskId) {
		AjaxJson ajaxJson = new AjaxJson();

		HistoricTaskInstance  hisTaskInstance = historyService.createHistoricTaskInstanceQuery()
					.taskId(taskId).singleResult();
		
		if (hisTaskInstance != null) {
			SapRequisitionSheet sapRequisitionSheet = sapRequisitionSheetService.getSapRequisitionSheet(hisTaskInstance.getProcessInstanceId());
			
			String taskDefKey = hisTaskInstance.getTaskDefinitionKey();
			
			HistoricVariableInstance hisVar = null;
			if (taskDefKey.equals(PROCESS_STEP_ONE)) {
				hisVar = historyService.createHistoricVariableInstanceQuery()
						.variableName("projectAudi").processInstanceId(hisTaskInstance.getProcessInstanceId()).singleResult();
			} else if (taskDefKey.equals(PROCESS_STEP_TWO)) {
				hisVar = historyService.createHistoricVariableInstanceQuery()
						.variableName("generalManagerAudi").processInstanceId(hisTaskInstance.getProcessInstanceId()).singleResult();
			}
			//取回任务，需要判断流是不是已经结束，已结束不能取回流程
			 Command<Boolean> processEndCmd = new CheckProcessEndCmd(taskId);
			 Boolean isEnd = managementService.executeCommand(processEndCmd);
			 if (isEnd) {
				//流程已经结束，无法取回
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("流程已经结束,取回任务失败!");
				return ajaxJson;
			}
			
			//下一个节点已经结束，不能取回
			//取回任务，需要判断流是不是已经结束，已结束不能取回流程
			 Command<Boolean> nextTaskCompleteCmd = new CheckNextTaskCompletedCmd(taskId);
			 Boolean isNextEnd = managementService.executeCommand(nextTaskCompleteCmd);
			 if (isNextEnd) {
				//下一个节点已经结束，不能取回
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("下一结点已经通过，取回任务失败!");
				return ajaxJson;
			}
			
			//如果是选择不通过，不需要调用SAP的接口取消审批
			//如果是调整采购单节点取回任务，不需要调用SAP的接口
			Map<String, String> sqpMessage = null;
			if (hisVar.getValue().equals(BPMConstant.SAP_AUDI_NOAGREE)
					|| !taskDefKey.equals(PROCESS_STEP_THREE)) {
				sqpMessage = null;
				if (taskDefKey.equals(PROCESS_STEP_TWO)) {
					//如果是总经理审批取回任务，需要调用SAP接口把第一步进行审批
					SapRequisitionSheet sapReqSheet = sapRequisitionSheetService.getSapRequisitionSheet(hisTaskInstance.getProcessInstanceId());
					String approvalCode = sapReqSheet.getFrgc1();
					
					RfcSapReqRelease release = new RfcSapReqRelease();
					try {
						release.sapReqSheetAudi(sapReqSheet.getPreqNo(),
										"000" + sapReqSheet.getPreqItem(), approvalCode);
					} catch (Exception e) {
						e.printStackTrace();
						sqpMessage = new HashMap<String,String>();
					}
				}
			} else {
				//如果是不同意，是不是不需要调用SAP接口取消审批？
				String approvalCode = sapFlowService.getSapApprovalCode4HrAccount(hisTaskInstance.getAssignee());
					
				RfcSapReqRestRelease restRelease = new RfcSapReqRestRelease();
				try {
					sqpMessage = restRelease.sapReqSheetResetAudi(sapRequisitionSheet.getPreqNo(),
								APPROVAL_CODE_PERFIX + sapRequisitionSheet.getPreqItem(),
								approvalCode);
				} catch (Exception e) {
					logger.error(e.getMessage());
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("取回任务失败!");
					return ajaxJson;
				}
			}
			if (sqpMessage == null) {
				 Command<Integer> cmd = new WithdrawTaskCmd(taskId);
				 //0-撤销成功 1-流程结束 2-下一结点已经通过,不能撤销
				 Integer resultCode = managementService.executeCommand(cmd);
				 switch (resultCode) {
				 case 0:
					ajaxJson.setSuccess(true);
					ajaxJson.setMsg("取回任务成功!");
					break;
				 case 1:
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("流程已经结束,取回任务失败!");
					break;
				 case 2:
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("下一结点已经通过，取回任务失败!");
					break;
				 default:
					break;
				 }
			} else {
			    ajaxJson.setSuccess(false);
			    ajaxJson.setMsg(SapMessageUtil.formatSapMessage(sqpMessage));
			}
		}
	    return ajaxJson;
	}
	
    /**
     * 初始化FreeMarker表单全局变量
     * @param model
     * @return
     */
    private ModelAndView initGlobalModel(ModelAndView model){
    	AppUser user = AppContext.getCurrentAppUser();
    	if (user != null) {
    		model.addObject(CONTEXT_APP_USER, user);
    	}
    	model.addObject(CONTEXT_RANDOM_VALUE, (""+Math.random()).substring(2, 8));

    	return model;
    }
    
    /**
     * 获取历史流程相关节点及变量
     * @param model
     * @param processInstanceId
     * @param historyService
     * @return
     */
    private ModelAndView getHisProcesVariable(ModelAndView model, String processInstanceId){
		
		List<ProcessStep> stepList = null;
    	
		//查询一个活动的执行信息
        List<HistoricActivityInstance> hisActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
        					.processInstanceId(processInstanceId).finished().list();
		
		if (hisActivityInstanceList != null && hisActivityInstanceList.size() > 0) {
			stepList = new ArrayList<ProcessStep>();
			ProcessStep step = null;
			
			for (HistoricActivityInstance activitiInstance : hisActivityInstanceList) {
				 // 查询历史流程实例以及人物实例相关信息
		     	List<HistoricDetail> processVars = historyService
		     				.createHistoricDetailQuery().activityInstanceId(activitiInstance.getId()).list();
		     	
		     	if(processVars != null && processVars.size() > 0){
		     		step = new ProcessStep(activitiInstance.getActivityId(), new LinkedHashMap<String,Object>());
					step.setStartTime(activitiInstance.getStartTime());
					step.setEndTime(activitiInstance.getEndTime());
					step.setAssignee(activitiInstance.getAssignee());
					step.setDurationInMillis(activitiInstance.getDurationInMillis());
					step.setActivityName(activitiInstance.getActivityName());
					
					for (HistoricDetail hisDetail : processVars) {
			     		HistoricDetailVariableInstanceUpdateEntity entity = (HistoricDetailVariableInstanceUpdateEntity)hisDetail;
			     		if (entity.getVariableType().getTypeName().equals("serializable")) {
			     			step.getVars().put(entity.getName(), readSerializableObj(entity));
			     		} else {
			     			step.getVars().put(entity.getName(), entity.getTextValue());
			     		}
			     	}
					
					//处理附件
					List<Attachment> attachmentList = null;
					if (activitiInstance.getActivityType().equals("startEvent")) {
						attachmentList = taskService.getProcessInstanceAttachments(processInstanceId);
					} else {
						attachmentList = taskService.getTaskAttachments(activitiInstance.getTaskId());
					}
					
					if (attachmentList != null && attachmentList.size() > 0) {
						step.setAttachments(attachmentList);
					} else {
						step.setAttachments(null);
					}
					
			     	stepList.add(step);
		     	}
			}
		}
        // 查询历史流程变量
     	List<HistoricVariableInstance> formProperties = historyService
     				.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).list();
     	
     	for (HistoricVariableInstance historicVarInstance : formProperties) {
     		model.addObject(historicVarInstance.getVariableName(), historicVarInstance.getValue());
		}
        
     	model.addObject(PROCESS_STEPS, stepList);
     	
        return model;
	}
    
    /**
     * 从历史流程变量中读取序列化对象
     * @param entity
     * @return
     */
    private Object readSerializableObj(HistoricDetailVariableInstanceUpdateEntity entity){
    	Object obj = null;
    	byte[] bytes = entity.getBytes();
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bis);
			obj = ois.readObject();
			ois.close();
			bis.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		return obj;
    }
    
}
