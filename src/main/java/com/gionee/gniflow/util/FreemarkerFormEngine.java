package com.gionee.gniflow.util;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.form.FormData;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.form.FormEngine;
import org.activiti.engine.impl.form.TaskFormDataImpl;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gnif.model.AppUser;
import com.gionee.gnif.util.AppContext;
import com.gionee.gniflow.biz.service.BpmService;
import com.gionee.gniflow.constant.BPMConstant;
import com.gionee.gniflow.web.bmp.ProcessHisVariable;
import com.gionee.gniflow.web.util.SpringTools;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Created by doit on 2014/7/7.
 */
public class FreemarkerFormEngine implements FormEngine {
	
	private static final Logger logger = LoggerFactory.getLogger(FreemarkerFormEngine.class);
	
	//Global
	private static final String CONTEXT_RANDOM_VALUE = "rand";
	private static final String CONTEXT_BASE_VALUE = "base";
	private static final String CONTEXT_APP_USER = "appuser";
	private static final String CURRENT_STEP = "currentStep";			//Task
	private static final String CURRENT_STEP_NAME = "currentStepName";  //TaskName
	private static final String FREEMARKER_ENGINE_NAME = "freemarker";	//Engine Name
	private static final String PROCESS_INSTANCE_ID = "processInstanceId";
	private static final String PROCESS_HISTORY_VARIABLE = "proHisVar";

	
	//Config
    private Configuration cfg;
	private String formDir;
	private String base;
	//Config-GnFs
	private String file_upload_url;
	private String file_download_url;
	private String file_param_iframeUrl;
	private String file_param_syId;
	private String file_param_syNm;
	private String file_param_diyFolder;
	private String file_parma_uploadType;
	private String file_param_fileVersion;
	
	private BpmService bpmService;

    public void setFreemarkerConfig(Configuration configuration) {
    	cfg = configuration;
    }
    
    public void setFormDir(String formDir) {
    	if (StringUtils.isEmpty(formDir)) {
    		this.formDir = formDir;
    	} else if (!formDir.endsWith("/")) {
    		this.formDir = formDir + "/";
    	}
	}
    
    public void setBase(String base) {
		this.base = base;
	}
    
	public void setFile_upload_url(String file_upload_url) {
		this.file_upload_url = file_upload_url;
	}

	public void setFile_download_url(String file_download_url) {
		this.file_download_url = file_download_url;
	}

	public void setFile_param_iframeUrl(String file_param_iframeUrl) {
		this.file_param_iframeUrl = file_param_iframeUrl;
	}

	public void setFile_param_syId(String file_param_syId) {
		this.file_param_syId = file_param_syId;
	}

	public void setFile_param_syNm(String file_param_syNm) {
		this.file_param_syNm = file_param_syNm;
	}

	public void setFile_param_diyFolder(String file_param_diyFolder) {
		this.file_param_diyFolder = file_param_diyFolder;
	}

	public void setFile_parma_uploadType(String file_parma_uploadType) {
		this.file_parma_uploadType = file_parma_uploadType;
	}

	public void setFile_param_fileVersion(String file_param_fileVersion) {
		this.file_param_fileVersion = file_param_fileVersion;
	}
    

	@Override
    public String getName() {
        return FREEMARKER_ENGINE_NAME;
    }
	
	/**
	 * 渲染开始表单
	 */
    @Override
    public Object renderStartForm(StartFormData startForm) {
    	
    	//设置全局变量
    	Map<String, Object> model = new LinkedHashMap<String,Object>();
    	
    	//获取开始节点
    	RepositoryService repositoryService = Context.getProcessEngineConfiguration().getRepositoryService();
		BpmnModel bpmModel = repositoryService.getBpmnModel(startForm.getProcessDefinition().getId());
		
		List<org.activiti.bpmn.model.Process> processList = bpmModel.getProcesses();
		for (org.activiti.bpmn.model.Process process : processList) {
			List<FlowElement> eleList = (List<FlowElement>) process.getFlowElements();
			for (FlowElement ele : eleList) {
				if (ele instanceof StartEvent) {
					StartEvent startEle = (StartEvent) ele;
					logger.debug("[startEle.getId()]-->" + startEle.getId());
					//开始节点
					model.put(CURRENT_STEP, startEle.getId());
					model.put(CURRENT_STEP_NAME, startEle.getName());
					break;
				}
			}
			
		}
    	
		model = initGlobalModel(model);
    	
        return getFormTemplateString(startForm.getProcessDefinition().getKey(), startForm, model);
    }
    
    /**
     * 渲染任务表单
     */
    @Override
    public Object renderTaskForm(TaskFormData taskForm) {
    	
        TaskEntity task = (TaskEntity) taskForm.getTask();
        
        Map<String,Object> model = task.getExecution().getProcessVariables();
        
        //获取流程变量
        bpmService = (BpmService) SpringTools.getBean(BpmService.class);
        model = bpmService.getHisProcesVariable(model, task.getProcessInstanceId(), false);
        
        model = initGlobalModel(model);
        ProcessHisVariable proHisVar = bpmService.getAllHisVariables(task.getProcessInstanceId());
        
        model.put(PROCESS_HISTORY_VARIABLE, proHisVar);
        model.put(CURRENT_STEP, task.getTaskDefinitionKey());
        model.put(CURRENT_STEP_NAME, task.getName());
        model.put(PROCESS_INSTANCE_ID, task.getProcessInstanceId());
        
        String[] processKey = task.getProcessDefinitionId().split(":");
        return getFormTemplateString(processKey[0], taskForm, model);
    }
    
    
    public Object renderEoaTaskForm(TaskFormData taskForm) {
    	
        TaskEntity task = (TaskEntity) taskForm.getTask();
        
        Map<String,Object> model = task.getExecution().getProcessVariables();
        
        //获取流程变量
        bpmService = (BpmService) SpringTools.getBean(BpmService.class);
        
        model = initGlobalModel(model);
        ProcessHisVariable proHisVar = bpmService.getAllHisVariables(task.getProcessInstanceId());
        
        model.put(PROCESS_HISTORY_VARIABLE, proHisVar);
        model.put(CURRENT_STEP, task.getTaskDefinitionKey());
        model.put(CURRENT_STEP_NAME, task.getName());
        model.put(PROCESS_INSTANCE_ID, task.getProcessInstanceId());
        
        String[] processKey = task.getProcessDefinitionId().split(":");
        return getFormTemplateString(processKey[0], taskForm, model);
    }
    /**
     * 渲染批量操作时任务表单
     */
    public Object renderBatchTaskForm(TaskFormData taskForm) {
    	
        TaskEntity task = (TaskEntity) taskForm.getTask();
        
        Map<String,Object> model = task.getExecution().getProcessVariables();
        
        model = initGlobalModel(model);
        bpmService = (BpmService) SpringTools.getBean(BpmService.class);
        ProcessHisVariable proHisVar = bpmService.getAllHisVariables(task.getProcessInstanceId());
        
        model.put(PROCESS_HISTORY_VARIABLE, proHisVar);
        model.put(CURRENT_STEP, task.getTaskDefinitionKey());
        model.put(CURRENT_STEP_NAME, task.getName());
        model.put(PROCESS_INSTANCE_ID, task.getProcessInstanceId());
        
        String[] processKey = task.getProcessDefinitionId().split(":");
        return getFormTemplateString4Batch(processKey[0], taskForm, model);
    }
    
    /**
     * 渲染流程历史表单
     */
    public Object renderHisProcessForm(String  processInstanceId, ProcessEngineConfiguration engineConfig) {
    	
    	Map<String,Object> model = new LinkedHashMap<String,Object>();
    	
        //历史服务
        HistoryService historyService = engineConfig.getHistoryService();
        
        //获取流程变量
        bpmService = (BpmService) SpringTools.getBean(BpmService.class);
        model = bpmService.getHisProcesVariable(model, processInstanceId,false);
        
        ProcessHisVariable proHisVar = bpmService.getAllHisVariables(processInstanceId);
        model.put(PROCESS_HISTORY_VARIABLE, proHisVar);
        model = initGlobalModel(model);
        
		HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery()
							.processInstanceId(processInstanceId).singleResult();
        
		String deploymentId = engineConfig.getRepositoryService()
				.getProcessDefinition(processInstance.getProcessDefinitionId()).getDeploymentId();
		
		//构造一个TaskFormData
		TaskFormDataImpl formData = new TaskFormDataImpl();
		formData.setDeploymentId(deploymentId);
		formData.setFormKey(null);
		
        String[] processKey = processInstance.getProcessDefinitionId().split(":");
        
        return getFormTemplateString(processKey[0], formData, model);
    }
    /**
     * 渲染流程历史打印表单
     * @param processInstanceId
     * @param engineConfig
     * @return
     */
    public Object renderHisProcessPrintForm(String  processInstanceId, ProcessEngineConfiguration engineConfig) {
    	
    	Map<String,Object> model = new LinkedHashMap<String,Object>();
    	
        //历史服务
        HistoryService historyService = engineConfig.getHistoryService();
        
        //获取流程变量
        bpmService = (BpmService) SpringTools.getBean(BpmService.class);
        model = bpmService.getEntireProcesVariable(model, processInstanceId,false);
        
        ProcessHisVariable proHisVar = bpmService.getAllHisVariables(processInstanceId);
        model.put(PROCESS_HISTORY_VARIABLE, proHisVar);
        model = initGlobalModel(model);
        
		HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery()
							.processInstanceId(processInstanceId).singleResult();
        
		String deploymentId = engineConfig.getRepositoryService()
				.getProcessDefinition(processInstance.getProcessDefinitionId()).getDeploymentId();
		
		//构造一个TaskFormData
		TaskFormDataImpl formData = new TaskFormDataImpl();
		formData.setDeploymentId(deploymentId);
		formData.setFormKey(null);
		
        String[] processKey = processInstance.getProcessDefinitionId().split(":");
        
        return getFormTemplateString(processKey[0], formData, model);
    }
    /**
     * 根据模板填充数据
     * @param processDefinitionKey
     * @param formInstance
     * @param model
     * @return
     */
    protected String getFormTemplateString(String processDefinitionKey, FormData formInstance, 
    		Map<String,Object> model) {
        String deploymentId = formInstance.getDeploymentId();
        String formKey = formInstance.getFormKey();
        if (formKey == null) {
            formKey = getProcessDefPerfix(processDefinitionKey)+".html";
        }
        String name = null;
        if (StringUtils.isEmpty(formDir)) {
        	name = deploymentId + ";" + formKey;
        }
        else {
        	name = formDir + processDefinitionKey + "/" + formKey;
        }
        try {
            Template temp = cfg.getTemplate(name);
            Writer out = new StringWriter();
            temp.process(model, out);
            return out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    /**
     * 根据模板填充批量操作数据
     * @param processDefinitionKey
     * @param formInstance
     * @param model
     * @return
     */
    protected String getFormTemplateString4Batch(String processDefinitionKey, FormData formInstance, 
    		Map<String,Object> model) {
        String deploymentId = formInstance.getDeploymentId();
        String formKey = formInstance.getFormKey();
        if (formKey == null) {
            formKey = getProcessDefPerfix(processDefinitionKey)+".html";
        }
        String name = null;
        if (StringUtils.isEmpty(formDir)) {
        	if(processDefinitionKey.equals("M-Leave-Application"))
        		name = deploymentId + ";" + getProcessDefPerfix(processDefinitionKey)+"-batchform.html";
        	else
        	name = deploymentId + ";" + formKey;
        	
        }else if(processDefinitionKey.equals("M-Leave-Application"))
        		name = formDir + processDefinitionKey + "/" + getProcessDefPerfix(processDefinitionKey)+"-batchform.html";
        else
        		name = formDir + processDefinitionKey + "/" + formKey;
       
       
        try {
            Template temp = cfg.getTemplate(name);
            Writer out = new StringWriter();
            temp.process(model, out);
            return out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    /**
     * 初始化FreeMarker表单全局变量
     * @param model
     * @return
     */
    private Map<String, Object> initGlobalModel(Map<String, Object> model){
    	AppUser user = AppContext.getCurrentAppUser();
    	if (user != null) {
    		model.put(CONTEXT_APP_USER, user);
    	}
    	model.put(CONTEXT_RANDOM_VALUE, (""+Math.random()).substring(2, 8));
    	model.put(CONTEXT_BASE_VALUE, base);
    	//GnFS
    	model.put(BPMConstant.FILE_UPLOAD_URL, file_upload_url);
    	model.put(BPMConstant.FILE_DOWNLOAD_URL, file_download_url);
    	model.put(BPMConstant.FILE_PARAM_IFRAMEURL, file_param_iframeUrl);
    	model.put(BPMConstant.FILE_PARAM_SYID, file_param_syId);
    	model.put(BPMConstant.FILE_PARAM_SYNM, file_param_syNm);
    	model.put(BPMConstant.FILE_PARAM_DIYFOLDER, file_param_diyFolder);
    	model.put(BPMConstant.FILE_PARAM_UPLOADTYPE, file_parma_uploadType);
    	model.put(BPMConstant.FILE_PARAM_FILEVERSION, file_param_fileVersion);
    	model.put(BPMConstant.CURRENT_PARAM_DATE, new Date().toLocaleString());
    	return model;
    }
    
    /**
     * 获取流程定义的前缀
     * @param processDefinitionKey
     * @return
     */
    private String getProcessDefPerfix(String processDefinitionKey){
    	String[] keyArr = processDefinitionKey.split(":");
    	return keyArr[0];
    }

}
