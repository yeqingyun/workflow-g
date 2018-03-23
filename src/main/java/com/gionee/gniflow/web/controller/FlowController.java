package com.gionee.gniflow.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.FormProperty;
import org.activiti.bpmn.model.ReceiveTask;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;
import org.activiti.rest.service.api.form.FormDataResponse;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gionee.gnif.dto.MessageDto;
import com.gionee.gnif.dto.TreeDto;
import com.gionee.gnif.model.AppUser;
import com.gionee.gnif.util.AppContext;
import com.gionee.gnif.web.util.EntryFactory;
import com.gionee.gniflow.constant.BPMConstant;
import com.gionee.gniflow.dto.FormDataAddAttachmentResponse;
import com.gionee.gniflow.dto.HisAllFromDataResponse;
import com.gionee.gniflow.dto.HisNodeFromDataResponse;
import com.gionee.gniflow.dto.MyFlowElement;
import com.gionee.gniflow.dto.ProcessDefinitionDto;
import com.gionee.gniflow.dto.TaskDto;
import com.gionee.gniflow.web.cmd.HistoryProcessInstanceDiagramCmd;
import com.gionee.gniflow.web.easyui.GridPageData;
import com.gionee.gniflow.web.util.HistoryJsonResponseFactory;
import com.gionee.gniflow.web.util.JsonResponseFactory;

@RequestMapping("/myflow")
@Controller
public class FlowController {
	
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
	ProcessEngineFactoryBean processEngineFactoryBean;
	
	@Autowired
	private ProcessEngine processEngine;
	
	//流程列表
	@RequestMapping("/flows.json")
	@ResponseBody
	public List<TreeDto> flows() {
		return JsonResponseFactory.createProcessDefinitions(repositoryService.createProcessDefinitionQuery().list());
	}
	
	//流程定义详情
	@RequestMapping("/processDefinitionDetail.json")
	@ResponseBody
	public GridPageData<ProcessDefinitionDto> processDefinitionDetail(@RequestParam("processDefinitionId") String processDefinitionId) {
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).list();
		return new GridPageData<ProcessDefinitionDto>(convert2ProcessDefinitionDto(list), (long)list.size());
	}
	
	//启动流程
	@RequestMapping("/create/startProcess.json")
	public ResponseEntity<MessageDto> createProcess(@RequestParam("processDefId") String processDefinitionId, 
			HttpServletRequest request) {
		try {
			Map<String, String> formProperties = new HashMap<String, String>();

			Map<String, String[]> parameterMap = request.getParameterMap();
			Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
			for (Entry<String, String[]> entry : entrySet) {
				String key = entry.getKey();

				if (key.startsWith("fp_")) {
					formProperties.put(key.split("fp_")[1], entry.getValue()[0]);
				}
			}

			AppUser user = AppContext.getCurrentAppUser();
			// 当流程启动时，把当前登录的用户保存到 activiti:initiator="xx"的变量名中
			identityService.setAuthenticatedUserId(user.getAccount());

			ProcessInstance processInstance = formService.submitStartFormData(
					processDefinitionId, formProperties);

			// 处理文件
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			if (multipartResolver.isMultipart(multipartRequest)) {
				MultiValueMap<String, MultipartFile> multfiles = multipartRequest
						.getMultiFileMap();
				for (String srcfname : multfiles.keySet()) {
					MultipartFile mfile = multfiles.getFirst(srcfname);
					saveAttachment(mfile, null, processInstance.getId());
				}
			}
			return EntryFactory.create(new MessageDto("启动流程成功！"));
		} catch (Exception e) {
			e.printStackTrace();
			return EntryFactory.create(new MessageDto("启动流程失败！"));
		}
        
        
	}
	
	//子系统调用接口启动流程
	@RequestMapping("/subSystem/startProcess.json")
	public ResponseEntity<MessageDto> subSystemStartProcess(@RequestParam("processDefId") String processDefinitionId, 
			HttpServletRequest request) {
		try {
			Map<String, String> formProperties = new HashMap<String, String>();

			Map<String, String[]> parameterMap = request.getParameterMap();
			Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
			for (Entry<String, String[]> entry : entrySet) {
				String key = entry.getKey();

				if (key.startsWith("fp_")) {
					formProperties.put(key.split("fp_")[1], entry.getValue()[0]);
				}
			}
			identityService.setAuthenticatedUserId(formProperties.get(BPMConstant.SUB_SYSTEM_START_PROCESS_USER_ACCOUNT));

//			ProcessInstance processInstance = formService.submitStartFormData(
//					processDefinitionId, formProperties);

			// 处理文件
//			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
//			if (multipartResolver.isMultipart(multipartRequest)) {
//				MultiValueMap<String, MultipartFile> multfiles = multipartRequest
//						.getMultiFileMap();
//				for (String srcfname : multfiles.keySet()) {
//					MultipartFile mfile = multfiles.getFirst(srcfname);
//					saveAttachment(mfile, null, processInstance.getId());
//				}
//			}
			return EntryFactory.create(new MessageDto("启动流程成功！"));
		} catch (Exception e) {
			e.printStackTrace();
			return EntryFactory.create(new MessageDto("启动流程失败！"));
		}
        
        
	}
	
	/** 跳转到process.html */
	@RequestMapping("/process/{processDefId}.html")
	public String process(@PathVariable("processDefId") String processDefId, Model model) {
		model.addAttribute("processDefId", processDefId);
//		model.addAttribute("callBackUrl", callBackUrl);
//		model.addAttribute("fileServiceUrl", fileServiceUrl);
		return "myflow/process";
	}
	
	//处理任务
	@RequestMapping("/complete/{taskId}.json")
	public ResponseEntity<MessageDto> completeTask(@PathVariable("taskId") String taskId, HttpServletRequest request, 
			RedirectAttributes ra) {
        Map<String, String> formProperties = new HashMap<String, String>();
        
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

		Map<String, String[]> parameterMap = request.getParameterMap();
		Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
		for (Entry<String, String[]> entry : entrySet) {
			String key = entry.getKey();

			if (key.startsWith("fp_")) {
				String[] paramSplit = key.split("fp_");
				formProperties.put(paramSplit[1], entry.getValue()[0]);
			}
		}
		
		//处理文件
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;   
        if (multipartResolver.isMultipart(multipartRequest)){
             MultiValueMap<String, MultipartFile> multfiles = multipartRequest.getMultiFileMap();
             for(String srcfname : multfiles.keySet()){
                 MultipartFile mfile = multfiles.getFirst(srcfname);
//               saveAttachment(mfile, taskId, null);
                 saveAttachment(mfile, null, task.getProcessInstanceId());
             }
        }
		
        AppUser user = AppContext.getCurrentAppUser();
        identityService.setAuthenticatedUserId(user.getAccount());
        
		formService.submitTaskFormData(taskId, formProperties);

		return EntryFactory.create(new MessageDto("任务处理成功"));
	}
	
	/** 任务列表 */
	@RequestMapping("tasks")
	public void tasks(Model model) {
		AppUser user = AppContext.getCurrentAppUser();
		List<Task> tasks= taskService.createTaskQuery().taskAssignee(user.getAccount()).list();
		Collections.sort(tasks,new Comparator<Task>(){  
				@Override
				public int compare(Task task1, Task task2) {
					return Integer.parseInt(task2.getId()) - Integer.parseInt(task1.getId());
				}  
		              
		       });
		model.addAttribute("tasks", tasks);
	}
	
	//任务列表
	@RequestMapping("/tasks/{procdefId}.json")
	@ResponseBody
	public GridPageData<TaskDto> taskList(@PathVariable("procdefId") String processDefinitionId, @RequestParam int page, @RequestParam int rows) {
		AppUser user = AppContext.getCurrentAppUser();
		
		List<Task> tasks= taskService.createTaskQuery().processDefinitionId(processDefinitionId)
				.taskAssignee(user.getAccount()).listPage((page-1)*rows, page*rows);
		Long total = taskService.createTaskQuery().processDefinitionId(processDefinitionId)
				.taskAssignee(user.getAccount()).count();
		
		Collections.sort(tasks,new Comparator<Task>(){  
				@Override
				public int compare(Task task1, Task task2) {
					return Integer.parseInt(task2.getId()) - Integer.parseInt(task1.getId());
				}  
	              
	        });
		List<TaskDto> taskDtos= convert2TaskDto(tasks);
		return new GridPageData<TaskDto>(taskDtos, total);
	}
	
	@RequestMapping("/runs.html")
	public void runnings(Model model) {
		List<ProcessInstance> runs = runtimeService.createProcessInstanceQuery().list();
		model.addAttribute("runs", runs);
	}
	
	@RequestMapping("/hist.html")
	public void finishs(Model model) {
		List<HistoricProcessInstance> hist = historyService.createHistoricProcessInstanceQuery().finished().list();
		model.addAttribute("hist", hist);
	}

	//获取流程开始节点表单
	@RequestMapping("/start/{procdefId}.json")
	@ResponseBody
	public Object startJson(@PathVariable("procdefId") String processDefinitionId) {
		FormDataAddAttachmentResponse attachmentFormData = new FormDataAddAttachmentResponse();
		if (formService.getStartFormKey(processDefinitionId) != null) {
			attachmentFormData.setFormHtml(formService.getRenderedStartForm(processDefinitionId).toString());
			attachmentFormData.setAttachmentList(new ArrayList<Attachment>());
		} else {
			FormDataResponse formDataResponse = JsonResponseFactory.createFormDataResponse(formService.getStartFormData(processDefinitionId));
			attachmentFormData.setFormDataResponse(formDataResponse);
			attachmentFormData.setAttachmentList(new ArrayList<Attachment>());
		}
		return attachmentFormData;
	}
	
	//获取处理任务表单
	@RequestMapping("/task/{taskId}.json")
	@ResponseBody
	public Object taskJson(@PathVariable String taskId) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processDefinitionId = task.getProcessDefinitionId();
		String taskDefinitionKey = task.getTaskDefinitionKey();
		
		FormDataAddAttachmentResponse attachmentFormData = new FormDataAddAttachmentResponse();
		
		List<Attachment> attachmentList = taskService.getProcessInstanceAttachments(task.getProcessInstanceId());
		
		if (formService.getTaskFormKey(processDefinitionId, taskDefinitionKey) != null) {
			attachmentFormData.setFormHtml(formService.getRenderedTaskForm(taskId).toString());
			attachmentFormData.setAttachmentList(attachmentList);
		} else {
			FormDataResponse formDataResponse = JsonResponseFactory.createFormDataResponse(formService.getTaskFormData(taskId));
			attachmentFormData.setFormDataResponse(formDataResponse);
			attachmentFormData.setAttachmentList(attachmentList);
		}
		return attachmentFormData;
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
	
	//已结束的流程实例
	@RequestMapping(value = "/finished.json")
	@ResponseBody
	public GridPageData<HistoricProcessInstance> finished(@RequestParam int page, @RequestParam int rows) {
		List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().
				finished().listPage((page-1)*rows, page*rows);
		
		Long total = historyService.createHistoricProcessInstanceQuery().finished().count();
		
		return new GridPageData<HistoricProcessInstance>(list, total);
	 }
	
	//跟踪流程
	@RequestMapping(value = "/resource/process-instance")
	public void loadByProcessInstance(@RequestParam("pid") String executionId, HttpServletResponse response) throws Exception {
//		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
//				.processInstanceId(executionId).singleResult();
//	    BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
//	    List<String> activeActivityIds = runtimeService.getActiveActivityIds(executionId);
//	    //解决中文乱码
//	    Context.setProcessEngineConfiguration(processEngineFactoryBean.getProcessEngineConfiguration());
//	    
//	    InputStream imageStream = ProcessDiagramGenerator.generateDiagram(bpmnModel, "png", activeActivityIds);
// 
//		// 输出资源内容到相应对象
//		byte[] b = new byte[1024];
//		int len;
//		while ((len = imageStream.read(b, 0, 1024)) != -1) {
//			response.getOutputStream().write(b, 0, len);
//		}
		
		Command<InputStream> cmd = new HistoryProcessInstanceDiagramCmd(executionId);

	    InputStream is = processEngine.getManagementService().executeCommand(cmd);
	    response.setContentType("image/png");

	    int len = 0;
	    byte[] b = new byte[1024];

	    while ((len = is.read(b, 0, 1024)) != -1) {
	       response.getOutputStream().write(b, 0, len);
	    }
	}
	
	//获取历史节点的表单数据
	@RequestMapping("/getHistoryForm.json")
	@ResponseBody
	public HisAllFromDataResponse historyForm(@RequestParam("processId") String processInstanceId ){
		Map<String,Object> hisVariable = new HashMap<String, Object>();
		HisAllFromDataResponse hisAllResponse = new HisAllFromDataResponse();
		HisNodeFromDataResponse hisNodeResponse = null;
		
		HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		//获取流程实例
//		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
//				.processInstanceId(processInstanceId).singleResult();
		
		//读取历史表单数据
//		List<HistoricDetail> formProperties = historyService.createHistoricDetailQuery()
//				.processInstanceId(processInstance.getId()).formProperties().list();
		
		List<HistoricVariableInstance> formProperties = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId).list();
		
		for(HistoricVariableInstance historicVarInstance : formProperties) {
			if (historicVarInstance.getValue() instanceof Date) {
				String dateString = org.restlet.engine.util.DateUtils.format((Date)historicVarInstance.getValue(), "yyyy-MM-dd");
				hisVariable.put(historicVarInstance.getVariableName(), dateString);
			} else {
				hisVariable.put(historicVarInstance.getVariableName(), historicVarInstance.getValue());
			}
			
		}
		//System.out.println("hisVariable---->" + hisVariable);
		
		BpmnModel model = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
		MyFlowElement myFlowEle = null;
        FlowElement flowEle = null;
		//获取历史流程的所有节点，如果endTime如果为空，说明该节点未被处理，不需处理表单数据
		List<HistoricActivityInstance> activitiList = historyService.createHistoricActivityInstanceQuery()
				.processInstanceId(processInstanceId).list();
		
		for (HistoricActivityInstance instance : activitiList){
			if (instance.getEndTime() != null) {
				flowEle = model.getFlowElement(instance.getActivityId());
				myFlowEle = new MyFlowElement();
				if (flowEle instanceof StartEvent){
					StartEvent startEvent = (StartEvent) flowEle;
					BeanUtils.copyProperties(startEvent, myFlowEle);
				} else if (flowEle instanceof UserTask){
					UserTask userTask = (UserTask) flowEle;
					BeanUtils.copyProperties(userTask, myFlowEle);
				} else if (flowEle instanceof ReceiveTask) {
					ReceiveTask receiveTask = (ReceiveTask) flowEle;
					BeanUtils.copyProperties(receiveTask, myFlowEle);
					List<FormProperty> receiveFormProperties = new ArrayList<FormProperty>();
					FormProperty property = new FormProperty();
					property.setId(receiveTask.getDocumentation());
					property.setType("text");
					property.setName("fan结果");
					property.setReadable(true);
					property.setWriteable(false);
					receiveFormProperties.add(property);
					myFlowEle.setFormProperties(receiveFormProperties);
				}
				hisNodeResponse = HistoryJsonResponseFactory.createFormDataResponse(myFlowEle, hisVariable);
				if (hisNodeResponse != null) {
					 hisAllResponse.getNodeFormDataResponse().add(hisNodeResponse);
				}
			}
		}
		//附件
		List<Attachment> attachmentList = taskService.getProcessInstanceAttachments(processInstance.getId());
		hisAllResponse.setAttachmentList(attachmentList);

		return hisAllResponse;
	}
	
	/**
	 * 处理上传文件后的处理逻辑
	 * @return
	 */
	@RequestMapping("/uploadCallback.json")
	public String uploadFileCallBack(HttpServletRequest request){
		Map<String,String[]> params = request.getParameterMap();
		for(Map.Entry<String, String[]> entry : params.entrySet()){
			//System.out.println(entry.getKey() + "---->" + entry.getValue()[0]);
		}
		//System.out.println("------------------->uploadCallback!");
		return null;
	}
	
	/**
	 * 为任务添加附件
	 * @param myfiles
	 * @param processInstance
	 */
	private void saveAttachment(MultipartFile myfile, String taskId, String processInstanceId){
        Attachment attachment = null;
		//为任务添加附件
        try {
			attachment = taskService.createAttachment(myfile.getContentType(), taskId, processInstanceId, 
					myfile.getName(), myfile.getOriginalFilename(), myfile.getInputStream());
			taskService.saveAttachment(attachment);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//将Task转换为TaskDto
	private List<TaskDto> convert2TaskDto(List<Task> tasks){
		List<TaskDto> dtoList = new ArrayList<TaskDto>();
		TaskDto dto = null;
		for (Task task : tasks) {
			dto = new TaskDto();
			BeanUtils.copyProperties(task, dto);
			dtoList.add(dto);
		}
		return dtoList;
	}
	
	//将ProcessDefinition转换为ProcessDefinitionDto
	private List<ProcessDefinitionDto> convert2ProcessDefinitionDto(List<ProcessDefinition> processDefinitions){
		List<ProcessDefinitionDto> dtoList = new ArrayList<ProcessDefinitionDto>();
		ProcessDefinitionDto dto = null;
		for (ProcessDefinition pd : processDefinitions) {
			dto = new ProcessDefinitionDto();
			BeanUtils.copyProperties(pd, dto);
			dtoList.add(dto);
		}
		return dtoList;
	}
}
