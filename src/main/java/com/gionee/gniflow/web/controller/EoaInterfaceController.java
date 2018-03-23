package com.gionee.gniflow.web.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.interceptor.Command;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.util.AppContext;
import com.gionee.gniflow.biz.model.BpmProcessRun;
import com.gionee.gniflow.biz.model.EoaTaskInfoEntity;
import com.gionee.gniflow.biz.service.ActivitiHelpService;
import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.web.bmp.BpmHisActivityEntity;
import com.gionee.gniflow.web.bmp.BpmHisProcessInstanceEntity;
import com.gionee.gniflow.web.cmd.GetRenderedTaskHistoryFormCmd;
import com.gionee.gniflow.web.cmd.GetRenderedTaskHistoryPrintFormCmd;
import com.gionee.gniflow.web.cmd.HistoryProcessInstanceDiagramCmd;
import com.gionee.gniflow.web.easyui.GridPageData;
import com.gionee.gniflow.web.util.ProcessConvertUtil;

@RequestMapping("/eoaInterface")
@Controller
public class EoaInterfaceController {

	@Autowired
	private FormService formService;

	@Autowired
	private HistoryService historyService;


	@Autowired
	private ManagementService managementService;

	@Autowired
	CommonsMultipartResolver multipartResolver;
	
	@Autowired
	private ActivitiHelpService activitiHelpService;
	
	@Autowired
	private ProcessHelpService processHelpService;
	

	
	/** 查看历史流程图 */
	@RequestMapping("/graphProcessDefinition")
	@ResponseBody
	public Map<String,Object> graphProcessDefinition(@RequestParam("processInstanceId") String processInstanceId,
			HttpServletResponse response) throws Exception {
		Command<InputStream> cmd = new HistoryProcessInstanceDiagramCmd(processInstanceId);
        InputStream is = managementService.executeCommand(cmd);
        
        ByteArrayOutputStream  bout = new ByteArrayOutputStream();
        
        int len = 0;
        byte[] b = new byte[1024];

        while ((len = is.read(b, 0, 1024)) != -1) {
        	bout.write(b, 0, len);
        }
        byte[] data = bout.toByteArray();
        String imageStr =  new String(Base64.encodeBase64(data));
        String content = "<div style='text-align:center;'><image src='data:image/png;base64,"+imageStr+"' /></div>";
        
        Map<String,Object> imageJson = new HashMap<String,Object>();
        imageJson.put("content", content);
        return imageJson;
	}
	
	/**
     * 查看审批历史【任务列表--完成和未完成】
     */
    @RequestMapping("/processHistoryTask.json")
    @ResponseBody
    public List<BpmHisActivityEntity> viewHistory(@RequestParam("processInstanceId") String processInstanceId) {
    	
    	HistoricProcessInstance hisProcess = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
    	
    	String startActId = hisProcess.getStartActivityId();
    	String userId = hisProcess.getStartUserId();
    	//更新开始人的账号，方便查询
		activitiHelpService.updateStartAssignee(startActId,processInstanceId, userId);
    	
    	QueryMap queryMap = new QueryMap();
    	queryMap.put("procInstId", processInstanceId);
    	List<BpmHisActivityEntity> results = activitiHelpService.loadBpmHisActivity(queryMap);
    	for (int i = 0; i < results.size(); i++) {
    		BpmHisActivityEntity entity = results.get(i);
    		if (entity.getEndTime() != null) {
    			results.get(i).setProStep("<span style=\"color:blue; \">第" + (i+1) + "步</span>");
    		} else {
    			results.get(i).setProStep("<span style=\"color:red; \">当前步骤</span>");
    		}
    		
    		if (entity.getEndTime() != null && StringUtils.isEmpty(entity.getUserName())
    				&& StringUtils.isEmpty(entity.getUserAccount())) {
    			entity.setUserAccount("系统自动处理");
    			entity.setUserName("系统自动处理");
    		}
		}
        return results;
    }
    
	
	/** 根据用户任务 */
	@RequestMapping("/user/tasks.json")
	@ResponseBody
	public GridPageData<EoaTaskInfoEntity> tasks(QueryMap queryMap) {
		
		List<String> sapProIds = processHelpService.getSapProcessDef();

		queryMap.put("assignee", AppContext.getCurrentAccount());
		queryMap.put("sapProcessIds", sapProIds);
		List<EoaTaskInfoEntity> hisTasks = activitiHelpService.page4AssigneeAllTask(queryMap);
		for (EoaTaskInfoEntity taskEntity : hisTasks) {
			taskEntity.setProStartUserName(taskEntity.getProStartUserName() );//将显示编号名称改为只显示姓名			
		}
		
		Integer total = activitiHelpService.count4AssigneeAllTask(queryMap);

		return new GridPageData<EoaTaskInfoEntity>(hisTasks, total.longValue());
	}
	
	/** 根据用户待办任务数 */
	@RequestMapping("/user/countTodoTask.json")
	@ResponseBody
	public Integer countTodoTask(QueryMap queryMap) {
		
		List<String> sapProIds = processHelpService.getSapProcessDef();

		queryMap.put("assignee", AppContext.getCurrentAccount());
		queryMap.put("sapProcessIds", sapProIds);
		queryMap.put("suspensionState", "1");
		Integer total = activitiHelpService.count4AssigneeAllTask(queryMap);
		return total;
	}
	
	//获取用户参与的流程（包含自己发起的和参与办理的流程）
	@RequestMapping("/user/process.json")
	@ResponseBody
	public GridPageData<BpmHisProcessInstanceEntity> process(QueryMap queryMap) {
		
		List<String> sapProDefIds = processHelpService.getSapProcessDef();
		
		queryMap.put("assignee", AppContext.getCurrentAccount());
		queryMap.put("sapProcessIds", sapProDefIds);
		String processStatus = (String) queryMap.getMap().get("processStatus");
		if (StringUtils.isNotEmpty(processStatus) && processStatus.equals(BpmProcessRun.STATUS_RUN)) {
			queryMap.put("finished", "false");
		} else if (StringUtils.isNotEmpty(processStatus) && (
					processStatus.equals(BpmProcessRun.STATUS_COMPLETED)
					|| processStatus.equals(BpmProcessRun.STATUS_OBSOLETE))) {
			queryMap.put("finished", "true");
		}
		
		List<BpmHisProcessInstanceEntity> hisProInstances = activitiHelpService.page4InvolvedUserProcess(queryMap);
		
		//设置流程开始到现在的耗时
		for (BpmHisProcessInstanceEntity entity : hisProInstances) {
			entity.setStartUserName(entity.getStartUserName() );			
		}
		
		Integer total = activitiHelpService.count4InvolvedUserProcess(queryMap);

		return new GridPageData<BpmHisProcessInstanceEntity>(hisProInstances, total.longValue());
	}
	
	//获取用户最近的申请流程
	@RequestMapping("/user/recentlyStartedProcess.json")
	@ResponseBody
	public GridPageData<Map<String, Object>> recentlyStartedProcess( HttpServletResponse response,QueryMap queryMap) {
			queryMap.put("startUser", AppContext.getCurrentAccount());
			 List<Map<String,Object>> process = activitiHelpService.getRecentlyStartedProcess(queryMap);
			 return  new GridPageData<Map<String, Object>>(process, Integer.valueOf(process.size()).longValue());
	}
	
	/** 根据用户查询参与的流程,只包括经过他的流程*/
	@RequestMapping("/user/involvedProcess.json")
	@ResponseBody
	public GridPageData<BpmHisProcessInstanceEntity> getInvolvedProcess(QueryMap queryMap) {
		queryMap.put("assignee", AppContext.getCurrentAccount());
		
		String processStatus = (String) queryMap.getMap().get("processStatus");
		if (StringUtils.isNotEmpty(processStatus) && processStatus.equals(BpmProcessRun.STATUS_RUN)) {
			queryMap.put("finished", "false");
		} else if (StringUtils.isNotEmpty(processStatus) && (
					processStatus.equals(BpmProcessRun.STATUS_COMPLETED)
					|| processStatus.equals(BpmProcessRun.STATUS_OBSOLETE))) {
			queryMap.put("finished", "true");
		}
		queryMap.put("isForNewSystem", "1");
		List<BpmHisProcessInstanceEntity> hisProInstances = activitiHelpService.page4InvolvedUserProcess(queryMap);
		
		//设置流程开始到现在的耗时
		for (BpmHisProcessInstanceEntity entity : hisProInstances) {
			entity.setStartUserName(entity.getStartUserName() );//修改流程发起人编号姓名改为发起人姓名			
		}
		
		Integer total = activitiHelpService.count4InvolvedUserProcess(queryMap);

		return new GridPageData<BpmHisProcessInstanceEntity>(hisProInstances, total.longValue());
	}
	
	/** 根据用户查询发起的流程（包括了所有的状态） */
	@RequestMapping("/user/startedProcess.json")
	@ResponseBody
	public GridPageData<BpmHisProcessInstanceEntity> getStartedProcess(QueryMap queryMap) {
		
		List<String> sapProDefIds = processHelpService.getSapProcessDef();
		
		queryMap.put("assignee", AppContext.getCurrentAccount());
		queryMap.put("sapProcessIds", sapProDefIds);
		
		List<BpmHisProcessInstanceEntity> hisProInstances = activitiHelpService.page4StartProcess(queryMap);
		
		//设置流程开始到现在的耗时
		for (BpmHisProcessInstanceEntity entity : hisProInstances) {
			if (entity.getEndTime() == null) {
				entity.setDurationInMillis(new Date().getTime() - entity.getStartTime().getTime());
			}
			entity.setStartUserName(entity.getStartUserName());
		}
		
		Integer total = activitiHelpService.count4StartProcess(queryMap);

		return new GridPageData<BpmHisProcessInstanceEntity>(hisProInstances, total.longValue());
	}
}
