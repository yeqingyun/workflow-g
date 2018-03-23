package com.gionee.gniflow.web.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gionee.auth.UserService;
import com.gionee.gnif.dto.MessageDto;
import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.file.util.CalcUtil;
import com.gionee.gnif.model.AppUser;
import com.gionee.gnif.util.AppContext;
import com.gionee.gnif.web.util.EntryFactory;
import com.gionee.gniflow.biz.model.BpmProcessConf;
import com.gionee.gniflow.biz.model.BpmProcessRun;
import com.gionee.gniflow.biz.model.JTeamMonth;
import com.gionee.gniflow.biz.service.BpmProcessConfService;
import com.gionee.gniflow.biz.service.BpmProcessRunService;
import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.constant.BPMConstant;
import com.gionee.gniflow.dto.HrUserDto;
import com.gionee.gniflow.dto.MapFormElement;
import com.gionee.gniflow.form.FormElement;
import com.gionee.gniflow.form.FormJson;
import com.gionee.gniflow.integration.dao.KeepRunTaskDao;
import com.gionee.gniflow.web.cmd.DeleteStartVariableCmd;
import com.gionee.gniflow.web.cmd.GetRenderedBatchTaskFormCmd;
import com.gionee.gniflow.web.cmd.GetRenderedEoaTaskFormCmd;
import com.gionee.gniflow.web.cmd.GetRenderedTaskHistoryFormCmd;
import com.gionee.gniflow.web.cmd.GetRenderedTaskHistoryPrintFormCmd;
import com.gionee.gniflow.web.cmd.GnifStartProcessInstanceCmd;
import com.gionee.gniflow.web.easyui.AjaxJson;
import com.gionee.gniflow.web.support.AudiException;
import com.gionee.gniflow.web.util.GnifStringUtils;
import com.gionee.gniflow.web.util.HtmlParserUtil;
import com.gionee.gniflow.web.util.MapFormEleUtils;
import com.gionee.gniflow.web.util.ProcessConvertUtil;
import com.gionee.gniflow.web.util.ProcessFormValidateUtil;
import com.gionee.gniflow.web.util.PropertyHolder;
import com.gionee.oss.api.util.EncryptUtil;

@RequestMapping("/sysprocess")
@Controller
public class SysProcessController {

	@Value("${file.download.url}")
	private String fileDownLoadUrl;
	@Value("${oss.key}")
	private String osskey;
	@Value("${oss.code}")
	private String ossCode;

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
	private UserService userService;
	
	@Autowired
	private ProcessHelpService processHelpService;
	
	@Autowired
	private BpmProcessRunService bpmProcessRunService;
	
	@Autowired
	private BpmProcessConfService bpmProcessConfService;
	
	@Autowired
	private KeepRunTaskDao keepRunTaskDao;
	
	//启动流程
	@SuppressWarnings("rawtypes")
	@RequestMapping("/create/startProcess.html")
	@ResponseBody
	public AjaxJson createProcess(@RequestParam("processDefId") String processDefinitionId, 
			HttpServletRequest request) {
		try {
			Map<String, Object> formProperties = new HashMap<String, Object>();
			Map<String, String> attchmentMap = new LinkedHashMap<String,String>();

			Map<String, String[]> parameterMap = request.getParameterMap();
			//判断页面传参是否为空，为空则抛出异常
			if(parameterMap == null || parameterMap.size() < 2){
				throw new AudiException("页面数据提交出现异常，请稍后再试");
			}
			Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
			//遍历处理map_开头的元素，将其做类似group操作，保存到Map<String,Object>中
			List<MapFormElement> mapFormEleList = new ArrayList<MapFormElement>();
			MapFormElement mapFormEle = null;
			String reqChannels="";
			Boolean bool=false;
			for (Entry<String, String[]> entry : entrySet) {
				String key = entry.getKey();
				if(key.indexOf("fp_reqChannels")!=-1){
					reqChannels=entry.getValue()[0];
				}
				if (key.startsWith("fp_")) {
					if (entry.getValue().length > 1) {
						formProperties.put(key.split("fp_")[1], GnifStringUtils.join(",", entry.getValue()));
					} else {
						formProperties.put(key.split("fp_")[1], entry.getValue()[0]);
					}
				} else if (key.startsWith("attch_")) {
					//String[] arrValue =  entry.getValue()[0].split("_");
					//attchmentMap.put(arrValue[0], arrValue[1]);//文件名存在下划线的时候，上传附件历史列表中显示不全
					String[] arrValue =  entry.getValue()[0].split("_");
					String value = entry.getValue()[0].substring(arrValue[0].length()+1);//+1将中间的连接符"_"截取掉
					attchmentMap.put(arrValue[0],value);
					bool=true;
				} else if (key.startsWith("gridJson_")){
					JSONObject jsonObject = JSON.parseObject(entry.getValue()[0]);
					List<Map> lists = JSONArray.parseArray(jsonObject.get("rows").toString(),  Map.class);
					formProperties.put(entry.getKey(), lists);
				} else if (key.startsWith("map_")) {
					mapFormEle = new MapFormElement();
					String[] mapFormKeyArr = key.split("_");
					mapFormEle.setKey(mapFormKeyArr[1]);
					mapFormEle.setName(key);
					mapFormEle.setColIndex(mapFormKeyArr[3]);
					mapFormEle.setRowIndex(mapFormKeyArr[4]);
					if (entry.getValue().length > 1) {
						mapFormEle.setValue(GnifStringUtils.join(",", entry.getValue()));
					} else {
						mapFormEle.setValue(entry.getValue()[0]);
					}
					
					if (mapFormEle != null) {
						mapFormEleList.add(mapFormEle);
					}
				}
				
			}
			if(processDefinitionId.indexOf("M-Welfare-Application")!=-1&&bool==false&&StringUtils.isNotEmpty(reqChannels)){
				if(reqChannels.equals("1")){
					return new AjaxJson(false, "新婚");
				}else if(reqChannels.equals("2")){
					return new AjaxJson(false,"生育");
				}
				
			}
			//处理这个List
			if (!CollectionUtils.isEmpty(mapFormEleList)) {
				//排序
				Collections.sort(mapFormEleList);
				
				Map<String, List<List<MapFormElement>>> mapResult = MapFormEleUtils.handleMapFormEle2MapData(mapFormEleList);
				for (Map.Entry<String, List<List<MapFormElement>>> entry : mapResult.entrySet()) {
					formProperties.put(entry.getKey(), entry.getValue());
				}
			}
			
			AppUser user = AppContext.getCurrentAppUser();
			
			//验证重复
			Boolean reuslt = processHelpService.validateRepeatStartProcess(processDefinitionId, 
					BpmProcessRun.STATUS_RUN, user.getAccount());
			if (reuslt) {
				//后台表单验证
				String shortedProcessDefinetionId = processDefinitionId.split(":")[0];;
				String taskDefinetionId = "startevent1";
		        AjaxJson validateAjaxJson = ProcessFormValidateUtil.validate(shortedProcessDefinetionId, taskDefinetionId, formProperties);
		        if(!validateAjaxJson.isSuccess()){
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("close", false);
					validateAjaxJson.setAttributes(map);
		        	return validateAjaxJson;
		        }
		        
				// 当流程启动时，把当前登录的用户保存到 activiti:initiator="xx"的变量名中
				identityService.setAuthenticatedUserId(user.getAccount());
				
				ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinitionId, formProperties);
				// 处理文件
				for (Map.Entry<String, String> entry : attchmentMap.entrySet()) {
					saveAttachment(null, processInstance.getId(), entry.getKey(), entry.getValue());
				}
				//获取流程启动后下一步处理人
				return processHelpService.getNextTaskAssignee(processInstance.getId(), true);
			} else {
				return new AjaxJson(false, "您发起的同类流程尚未结束,请不要重复发起流程,启动流程失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(e instanceof AudiException)
			{
				AjaxJson ajaxJson = new AjaxJson(false, "启动流程失败，原因："+e.getMessage()+"！");
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("close", false);
				ajaxJson.setAttributes(map);
				return ajaxJson;
			}
			return new AjaxJson(false, "启动流程失败！");
		}
        
	}
	
	//EOA处理任务
	@RequestMapping("/completeForEOA/{taskId}.html")
	@ResponseBody
	public AjaxJson completeTaskForEOA(@PathVariable("taskId") String taskId, HttpServletRequest request, 
			RedirectAttributes ra) {
		try {
			Map<String, Object> formProperties = new HashMap<String, Object>();
	        Map<String, String> attchmentMap = new LinkedHashMap<String,String>();
	        
	        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
	
			Map<String, String[]> parameterMap = request.getParameterMap();
			//判断页面传过来的参数是否为空，为空则抛出异常
			if(parameterMap.size() <= 0){
				throw new AudiException("页面数据提交出现异常，请稍后再试");
			}
			Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
			//遍历处理map_开头的元素，将其做类似group操作，保存到Map<String,Object>中
			List<MapFormElement> mapFormEleList = new ArrayList<MapFormElement>();
			MapFormElement mapFormEle = null;
			
			for (Entry<String, String[]> entry : entrySet) {
				String key = entry.getKey();
				
				if (key.startsWith("fp_")) {
					if (entry.getValue().length > 1) {
						formProperties.put(key.split("fp_")[1], GnifStringUtils.join(",", entry.getValue()));
					} else {
						formProperties.put(key.split("fp_")[1], entry.getValue()[0]);
					}
				} else if (key.startsWith("attch_")) {
					//String[] arrValue =  entry.getValue()[0].split("_");
					//attchmentMap.put(arrValue[0], arrValue[1]);//文件名存在下划线的时候，上传附件历史列表中显示不全
					String[] arrValue =  entry.getValue()[0].split("_");
					String value = entry.getValue()[0].substring(arrValue[0].length()+1);//+1将中间的连接符"_"截取掉
					attchmentMap.put(arrValue[0],value);
				} else if (key.startsWith("map_")) {
					mapFormEle = new MapFormElement();
					String[] mapFormKeyArr = key.split("_");
					mapFormEle.setKey(mapFormKeyArr[1]);
					mapFormEle.setName(key);
					mapFormEle.setColIndex(mapFormKeyArr[3]);
					mapFormEle.setRowIndex(mapFormKeyArr[4]);
					if (entry.getValue().length > 1) {
						mapFormEle.setValue(GnifStringUtils.join(",", entry.getValue()));
					} else {
						mapFormEle.setValue(entry.getValue()[0]);
					}
					
					if (mapFormEle != null) {
						mapFormEleList.add(mapFormEle);
					}
				}
			}
			//处理这个List
			if (!CollectionUtils.isEmpty(mapFormEleList)) {
				Map<String, List<List<MapFormElement>>> mapResult = MapFormEleUtils.handleMapFormEle2MapData(mapFormEleList);
				for (Map.Entry<String, List<List<MapFormElement>>> entry : mapResult.entrySet()) {
					formProperties.put(entry.getKey(), entry.getValue());
				}
			}
			//后台表单验证
	        String processDefinitionId = task.getProcessDefinitionId();
	        String taskDefinetionId = task.getTaskDefinitionKey();
	        String shortedProcessDefinetionId = processDefinitionId.split(":")[0];
	        AjaxJson validateAjaxJson = ProcessFormValidateUtil.validate(shortedProcessDefinetionId, taskDefinetionId, formProperties);
	        if(!validateAjaxJson.isSuccess()){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("close", false);
				validateAjaxJson.setAttributes(map);
	        	return validateAjaxJson;
	        }
			//处理文件
			for (Map.Entry<String, String> entry : attchmentMap.entrySet()) {
				saveAttachment(task.getId(), null, entry.getKey(), entry.getValue());
			}
			
	        AppUser user = AppContext.getCurrentAppUser();
	        identityService.setAuthenticatedUserId(user.getAccount());
	        
			taskService.complete(taskId, formProperties);
			
			//获取下一步处理人
			AjaxJson ajaxJson = processHelpService.getNextTaskAssignee(task.getProcessInstanceId(), false);
			
			return ajaxJson;
		} catch (Exception e) {
			e.printStackTrace();
			if(e instanceof AudiException)
			{
				return new AjaxJson(false, "审批任务失败，原因："+e.getMessage()+",请过段时间再试！");
			}
			else
				return new AjaxJson(false, "审批任务失败");
		}
	}
	/**
	 * EOA发起流程
	 * @param processDefinitionId
	 * @param request
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/createForEOA/startProcess.html")
	@ResponseBody
	public AjaxJson createProcessForEOA(@RequestParam("processDefId") String processDefinitionId,
			HttpServletRequest request) {
		try {
			Map<String, Object> formProperties = new HashMap<String, Object>();
			Map<String, String> attchmentMap = new LinkedHashMap<String,String>();

			Map<String, String[]> parameterMap = request.getParameterMap();
			//判断页面传参是否为空，为空则抛出异常
			if(parameterMap == null || parameterMap.size() < 2){
				throw new AudiException("页面数据提交出现异常，请稍后再试");
			}
			Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
			//遍历处理map_开头的元素，将其做类似group操作，保存到Map<String,Object>中
			List<MapFormElement> mapFormEleList = new ArrayList<MapFormElement>();
			MapFormElement mapFormEle = null;
			String reqChannels="";
			Boolean bool=false;
			for (Entry<String, String[]> entry : entrySet) {
				String key = entry.getKey();
				if(key.indexOf("fp_reqChannels")!=-1){
					reqChannels=entry.getValue()[0];
				}
				if (key.startsWith("fp_")) {
					if (entry.getValue().length > 1) {
						formProperties.put(key.split("fp_")[1], GnifStringUtils.join(",", entry.getValue()));
					} else {
						formProperties.put(key.split("fp_")[1], entry.getValue()[0]);
					}
				} else if (key.startsWith("attch_")) {
					//String[] arrValue =  entry.getValue()[0].split("_");
					//attchmentMap.put(arrValue[0], arrValue[1]);//文件名存在下划线的时候，上传附件历史列表中显示不全
					String[] arrValue =  entry.getValue()[0].split("_");
					String value = entry.getValue()[0].substring("arrValue[0]".length()+1);//+1将中间的连接符"_"截取掉
					attchmentMap.put(arrValue[0],value);
					bool=true;
				} else if (key.startsWith("gridJson_")){
					JSONObject jsonObject = JSON.parseObject(entry.getValue()[0]);
					List<Map> lists = JSONArray.parseArray(jsonObject.get("rows").toString(),  Map.class);
					formProperties.put(entry.getKey(), lists);
				} else if (key.startsWith("map_")) {
					mapFormEle = new MapFormElement();
					String[] mapFormKeyArr = key.split("_");
					mapFormEle.setKey(mapFormKeyArr[1]);
					mapFormEle.setName(key);
					mapFormEle.setColIndex(mapFormKeyArr[3]);
					mapFormEle.setRowIndex(mapFormKeyArr[4]);
					if (entry.getValue().length > 1) {
						mapFormEle.setValue(GnifStringUtils.join(",", entry.getValue()));
					} else {
						mapFormEle.setValue(entry.getValue()[0]);
					}

					if (mapFormEle != null) {
						mapFormEleList.add(mapFormEle);
					}
				}

			}
			if(processDefinitionId.indexOf("M-Welfare-Application")!=-1&&bool==false&&StringUtils.isNotEmpty(reqChannels)){
				if(reqChannels.equals("1")){
					return new AjaxJson(false, "新婚");
				}else if(reqChannels.equals("2")){
					return new AjaxJson(false,"生育");
				}

			}
			//处理这个List
			if (!CollectionUtils.isEmpty(mapFormEleList)) {
				//排序
				Collections.sort(mapFormEleList);

				Map<String, List<List<MapFormElement>>> mapResult = MapFormEleUtils.handleMapFormEle2MapData(mapFormEleList);
				for (Map.Entry<String, List<List<MapFormElement>>> entry : mapResult.entrySet()) {
					formProperties.put(entry.getKey(), entry.getValue());
				}
			}
			String account = AppContext.getCurrentAppUser().getAccount();
			/*//传入流程发起人的账号
			String account = formProperties.get("applyUserId").toString();
			List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
			list.add(new SimpleGrantedAuthority("AUTH_BASE"));
			User user = new User();
			user.setAccount(account);
			CasUser casUser = new CasUser(account,list);
			casUser.setUser(user);
			Authentication authentication = new AnonymousAuthenticationToken(account, casUser, list);
			SecurityContextHolder.getContext().setAuthentication(authentication);*/
			//验证重复
			Boolean reuslt = processHelpService.validateRepeatStartProcess(processDefinitionId,
					BpmProcessRun.STATUS_RUN, account);
			if (reuslt) {
				//后台表单验证
				String shortedProcessDefinetionId = processDefinitionId.split(":")[0];;
				String taskDefinetionId = "startevent1";
		        AjaxJson validateAjaxJson = ProcessFormValidateUtil.validate(shortedProcessDefinetionId, taskDefinetionId, formProperties);
		        if(!validateAjaxJson.isSuccess()){
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("close", false);
					validateAjaxJson.setAttributes(map);
		        	return validateAjaxJson;
		        }
				// 当流程启动时，把当前登录的用户保存到 activiti:initiator="xx"的变量名中
				identityService.setAuthenticatedUserId(account);

				ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinitionId, formProperties);
				// 处理文件
				for (Map.Entry<String, String> entry : attchmentMap.entrySet()) {
					saveAttachment(null, processInstance.getId(), entry.getKey(), entry.getValue());
				}
				//获取流程启动后下一步处理人
				return processHelpService.getNextTaskAssignee(processInstance.getId(), true);
			} else {
				return new AjaxJson(false, "您发起的同类流程尚未结束,请不要重复发起流程,启动流程失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(e instanceof AudiException)
			{
				return new AjaxJson(false, "审批任务失败，原因："+e.getMessage()+"！");
			}
			return new AjaxJson(false, "启动流程失败！");
		}

	}
	//修改流程启动信息
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/create/modifyProStartEvent.html")
	@ResponseBody
	public AjaxJson modifyProStartEvent(@RequestParam("processInstId") String processInstId, 
			HttpServletRequest request) {
		//判断流程的下一个节点是否已经审批
		Boolean reuslt = processHelpService.firstTaskIsHandledWithStartEvent(processInstId);
		if (reuslt) {
			return new AjaxJson(false, "该流程的下一步骤责任人办理已办理,修改流程发起表单失败!");
		} else {
			try {
				Map<String, Object> formProperties = new HashMap<String, Object>();
				Map<String, String> attchmentMap = new LinkedHashMap<String,String>();

				Map<String, String[]> parameterMap = request.getParameterMap();
				Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
				
				//遍历处理map_开头的元素，将其做类似group操作，保存到Map<String,Object>中
				List<MapFormElement> mapFormEleList = new ArrayList<MapFormElement>();
				MapFormElement mapFormEle = null;
				
				for (Entry<String, String[]> entry : entrySet) {
					String key = entry.getKey();
					if (key.startsWith("fp_")) {
						if (entry.getValue().length > 1) {
							formProperties.put(key.split("fp_")[1], GnifStringUtils.join(",", entry.getValue()));
						} else {
							formProperties.put(key.split("fp_")[1], entry.getValue()[0]);
						}
					} else if (key.startsWith("attch_")) {
						//String[] arrValue =  entry.getValue()[0].split("_");
						//attchmentMap.put(arrValue[0], arrValue[1]);//文件名存在下划线的时候，上传附件历史列表中显示不全
						String[] arrValue =  entry.getValue()[0].split("_");
						String value = entry.getValue()[0].substring(arrValue[0].length()+1);//+1将中间的连接符"_"截取掉
						attchmentMap.put(arrValue[0],value);
					} else if (key.startsWith("gridJson_")){
						JSONObject jsonObject = JSON.parseObject(entry.getValue()[0]);
						List<Map> lists = JSONArray.parseArray(jsonObject.get("rows").toString(),  Map.class);
						formProperties.put(entry.getKey(), lists);
					} else if (key.startsWith("map_")) {
						mapFormEle = new MapFormElement();
						String[] mapFormKeyArr = key.split("_");
						mapFormEle.setKey(mapFormKeyArr[1]);
						mapFormEle.setName(key);
						mapFormEle.setColIndex(mapFormKeyArr[3]);
						mapFormEle.setRowIndex(mapFormKeyArr[4]);
						if (entry.getValue().length > 1) {
							mapFormEle.setValue(GnifStringUtils.join(",", entry.getValue()));
						} else {
							mapFormEle.setValue(entry.getValue()[0]);
						}
						
						if (mapFormEle != null) {
							mapFormEleList.add(mapFormEle);
						}
					}
				}
				//处理这个List
				if (!CollectionUtils.isEmpty(mapFormEleList)) {
					Map<String, List<List<MapFormElement>>> mapResult = MapFormEleUtils.handleMapFormEle2MapData(mapFormEleList);
					for (Map.Entry<String, List<List<MapFormElement>>> entry : mapResult.entrySet()) {
						formProperties.put(entry.getKey(), entry.getValue());
					}
				}
				
				//删除start节点的产生的变量
				DeleteStartVariableCmd deleteStartVarCmd = new DeleteStartVariableCmd(processInstId);
				managementService.executeCommand(deleteStartVarCmd);
				
				//删除start节点的附件
				GnifStartProcessInstanceCmd startCmd = new GnifStartProcessInstanceCmd(processInstId,formProperties);
				managementService.executeCommand(startCmd);
				// 处理文件
				for (Map.Entry<String, String> entry : attchmentMap.entrySet()) {
					saveAttachment(null, processInstId, entry.getKey(), entry.getValue());
				}
//				//获取流程启动后下一步处理人
				return new AjaxJson(true, "修改流程开始表单信息成功！");
			} catch (Exception e) {
				e.printStackTrace();
				return new AjaxJson(false, "修改流程开始表单信息失败！");
			}
		}
	}
	
	//处理任务
	@RequestMapping("/complete/{taskId}.html")
	@ResponseBody
	public AjaxJson completeTask(@PathVariable("taskId") String taskId, HttpServletRequest request, 
			RedirectAttributes ra) {
		try {
			Map<String, Object> formProperties = new HashMap<String, Object>();
	        Map<String, String> attchmentMap = new LinkedHashMap<String,String>();
	        String reqChannels="";
	        Boolean bool=false;
	        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
	        
			Map<String, String[]> parameterMap = request.getParameterMap();
			
			//判断页面传过来的参数是否为空，为空则抛出异常
			if(parameterMap.size() <= 0){
				throw new AudiException("页面数据提交出现异常，请稍后再试");
			}
			
			Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
			//遍历处理map_开头的元素，将其做类似group操作，保存到Map<String,Object>中
			List<MapFormElement> mapFormEleList = new ArrayList<MapFormElement>();
			MapFormElement mapFormEle = null;
			
			for (Entry<String, String[]> entry : entrySet) {
				String key = entry.getKey();
				if(key.indexOf("fp_reqChannels")!=-1){
					reqChannels=entry.getValue()[0];
				}
				if (key.startsWith("fp_")) {
					if (entry.getValue().length > 1) {
						formProperties.put(key.split("fp_")[1], GnifStringUtils.join(",", entry.getValue()));
					} else {
						formProperties.put(key.split("fp_")[1], entry.getValue()[0]);
					}
				} else if (key.startsWith("attch_")) {
					//String[] arrValue =  entry.getValue()[0].split("_");
					//attchmentMap.put(arrValue[0], arrValue[1]);//文件名存在下划线的时候，上传附件历史列表中显示不全
					String[] arrValue =  entry.getValue()[0].split("_");
					String value = entry.getValue()[0].substring(arrValue[0].length()+1);//+1将中间的连接符"_"截取掉
					attchmentMap.put(arrValue[0],value);
					bool=true;
				} else if (key.startsWith("map_")) {
					mapFormEle = new MapFormElement();
					String[] mapFormKeyArr = key.split("_");
					mapFormEle.setKey(mapFormKeyArr[1]);
					mapFormEle.setName(key);
					mapFormEle.setColIndex(mapFormKeyArr[3]);
					mapFormEle.setRowIndex(mapFormKeyArr[4]);
					if (entry.getValue().length > 1) {
						mapFormEle.setValue(GnifStringUtils.join(",", entry.getValue()));
					} else {
						mapFormEle.setValue(entry.getValue()[0]);
					}
					
					if (mapFormEle != null) {
						mapFormEleList.add(mapFormEle);
					}
				}
			}
			if(task.getProcessDefinitionId().indexOf("M-Welfare-Application")!=-1&&bool==false&&StringUtils.isNotEmpty(reqChannels)){
				if(reqChannels.equals("1")){
					return new AjaxJson(false, "新婚");
				}else if(reqChannels.equals("2")){
					return new AjaxJson(false,"生育");
				}
			}
			//处理这个List
			if (!CollectionUtils.isEmpty(mapFormEleList)) {
				Map<String, List<List<MapFormElement>>> mapResult = MapFormEleUtils.handleMapFormEle2MapData(mapFormEleList);
				for (Map.Entry<String, List<List<MapFormElement>>> entry : mapResult.entrySet()) {
					formProperties.put(entry.getKey(), entry.getValue());
				}
			}
			//后台表单验证
	        String processDefinitionId = task.getProcessDefinitionId();
	        String taskDefinetionId = task.getTaskDefinitionKey();
	        String shortedProcessDefinetionId = processDefinitionId.split(":")[0];
	        AjaxJson validateAjaxJson = ProcessFormValidateUtil.validate(shortedProcessDefinetionId, taskDefinetionId, formProperties);
	        if(!validateAjaxJson.isSuccess()){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("close", false);
				validateAjaxJson.setAttributes(map);
	        	return validateAjaxJson;
	        }
			//处理文件
			for (Map.Entry<String, String> entry : attchmentMap.entrySet()) {
				saveAttachment(task.getId(), null, entry.getKey(), entry.getValue());
			}
			
	        AppUser user = AppContext.getCurrentAppUser();
	        identityService.setAuthenticatedUserId(user.getAccount());
	        
			taskService.complete(taskId, formProperties);
			
			//获取下一步处理人
			AjaxJson ajaxJson = processHelpService.getNextTaskAssignee(task.getProcessInstanceId(), false);
			
			return ajaxJson;
		} catch (Exception e) {
			e.printStackTrace();
			if(e instanceof AudiException)
			{
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("close", false);
				AjaxJson ajaxJson = new AjaxJson(false, "审批任务失败，原因："+e.getMessage()+",请过段时间再试！");
				ajaxJson.setAttributes(map);
				return ajaxJson;
			}
			else
				return new AjaxJson(false, "审批任务失败");
		}
	}
	
	//处理任务
	@RequestMapping("/batchCompleteTask.html")
	@ResponseBody
	public AjaxJson batchCompleteTask(@RequestParam("taskIds") String taskIds, HttpServletRequest request) {
		try {
			Map<String, Object> formProperties = new HashMap<String, Object>();
			Map<String, String> attchmentMap = new LinkedHashMap<String, String>();

			Map<String, String[]> parameterMap = request.getParameterMap();
			Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
			//遍历处理map_开头的元素，将其做类似group操作，保存到Map<String,Object>中
			List<MapFormElement> mapFormEleList = new ArrayList<MapFormElement>();
			MapFormElement mapFormEle = null;
			
			for (Entry<String, String[]> entry : entrySet) {
				String key = entry.getKey();

				if (key.startsWith("fp_")) {
					if (entry.getValue().length > 1) {
						formProperties.put(key.split("fp_")[1], GnifStringUtils.join(",", entry.getValue()));
					} else {
						formProperties.put(key.split("fp_")[1], entry.getValue()[0]);
					}
				} else if (key.startsWith("attch_")) {
					//String[] arrValue = entry.getValue()[0].split("_");
					//attchmentMap.put(arrValue[0], arrValue[1]);//文件名存在下划线的时候，上传附件历史列表中显示不全
					String[] arrValue =  entry.getValue()[0].split("_");
					String value = entry.getValue()[0].substring(arrValue[0].length()+1);//+1将中间的连接符"_"截取掉
					attchmentMap.put(arrValue[0],value);
				} else if (key.startsWith("map_")) {
					mapFormEle = new MapFormElement();
					String[] mapFormKeyArr = key.split("_");
					mapFormEle.setKey(mapFormKeyArr[1]);
					mapFormEle.setName(key);
					mapFormEle.setColIndex(mapFormKeyArr[3]);
					mapFormEle.setRowIndex(mapFormKeyArr[4]);
					if (entry.getValue().length > 1) {
						mapFormEle.setValue(GnifStringUtils.join(",", entry.getValue()));
					} else {
						mapFormEle.setValue(entry.getValue()[0]);
					}
					
					if (mapFormEle != null) {
						mapFormEleList.add(mapFormEle);
					}
				}
			}
			//处理这个List
			if (!CollectionUtils.isEmpty(mapFormEleList)) {
				Map<String, List<List<MapFormElement>>> mapResult = MapFormEleUtils.handleMapFormEle2MapData(mapFormEleList);
				for (Map.Entry<String, List<List<MapFormElement>>> entry : mapResult.entrySet()) {
					formProperties.put(entry.getKey(), entry.getValue());
				}
			}
			
			AppUser user = AppContext.getCurrentAppUser();
			identityService.setAuthenticatedUserId(user.getAccount());
			
			String[] taskIdArr = taskIds.split(",");
			Task task = taskService.createTaskQuery().taskId(taskIdArr[0]).singleResult();
			//后台表单验证
	        String processDefinitionId = task.getProcessDefinitionId();
	        String taskDefinetionId = task.getTaskDefinitionKey();
	        String shortedProcessDefinetionId = processDefinitionId.split(":")[0];
	        AjaxJson validateAjaxJson = ProcessFormValidateUtil.validate(shortedProcessDefinetionId, taskDefinetionId, formProperties);
	        if(!validateAjaxJson.isSuccess()){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("close", false);
				validateAjaxJson.setAttributes(map);
	        	return validateAjaxJson;
	        }
			for (String taskId : taskIdArr) {
				taskService.complete(taskId, formProperties);
			}

			// 获取下一步处理人
			AjaxJson ajaxJson = processHelpService.getNextTaskAssignee(task.getProcessInstanceId(), false);

			return ajaxJson;
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof AudiException) {
				return new AjaxJson(false, "审批任务失败，原因：" + e.getMessage() + ",请过段时间再试！");
			} else
				return new AjaxJson(false, "审批任务失败");
		}
	}
	
	//获取流程开始节点表单
	@RequestMapping("/start/form.html")
	public void viewStartForm(@RequestParam("procdefId") String processDefinitionId, HttpServletResponse response) {
		try {
			Object reuslt = formService.getRenderedStartForm(processDefinitionId, "freemarker");
			ProcessConvertUtil.processObj2ClientString(response, reuslt);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//获取处理任务表单
	@RequestMapping("/task/form.html")
	public void viewTaskForm(@RequestParam("taskId") String taskId, HttpServletResponse response) {
		try {
			//System.out.println("taskId:"+taskId);
			int historyTaskSize = historyService.createHistoricTaskInstanceQuery().taskId(taskId).list().size();
			int taskSize = taskService.createTaskQuery().taskId(taskId).list().size();
			if(historyTaskSize > 0 && taskSize == 0){
				String result = "您已经处理过此任务了。。。";
				ProcessConvertUtil.processObj2ClientString(response, result);
			}else{
				Object reuslt = formService.getRenderedTaskForm(taskId, "freemarker");
				ProcessConvertUtil.processObj2ClientString(response, reuslt);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//获取处理任务表单
	@RequestMapping("/task/batchform.html")
	public void viewBatchTaskForm(@RequestParam("taskId") String taskId, HttpServletResponse response) {
		try {
			Object reuslt = managementService.executeCommand(
					new GetRenderedBatchTaskFormCmd(taskId, "freemarker"));
			
			ProcessConvertUtil.processObj2ClientString(response, reuslt);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//获取处理任务表单
	@RequestMapping("/task/viewHisform.html")
	public void viewHisTaskForm(@RequestParam("processInstanceId") String processInstanceId, HttpServletResponse response) {
		try {
			Object reuslt = managementService.executeCommand(
					new GetRenderedTaskHistoryFormCmd(processInstanceId, "freemarker"));
			ProcessConvertUtil.processObj2ClientString(response, reuslt);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//获取处理任务打印表单
	@RequestMapping("/task/viewHisPrintform.html")
	public void viewHisPrintform(@RequestParam("processInstanceId") String processInstanceId, HttpServletResponse response) {
		try {
			Object reuslt = managementService.executeCommand(
					new GetRenderedTaskHistoryPrintFormCmd(processInstanceId, "freemarker"));
			ProcessConvertUtil.processObj2ClientString(response, reuslt);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//获取处理任务表单
	@RequestMapping("/task/mobileform.html")
	@ResponseBody
	public FormJson viewTaskFormForMobile(@RequestParam("taskId") String taskId, HttpServletResponse response) {
		FormJson formJson = new FormJson();
		try {
			Object reuslt =  managementService.executeCommand(new GetRenderedEoaTaskFormCmd(taskId, "freemarker"));
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			
			if (!task.getTaskDefinitionKey().startsWith("nmb_")) {
				List<FormElement> results = HtmlParserUtil.httpParser(reuslt.toString());
				
				formJson.setElements(results);
				formJson.setFormTitle(task.getName());
				formJson.setTaskId(task.getId());
				formJson.setHandleFlag(true);
				
				//System.out.println(JSONObject.toJSONString(formJson));
			} else {
				formJson.setHandleFlag(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return formJson;
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
	 * 下载岗位编制模板
	 * 
	 * 根据config.properties文件的key下载文件
	 * @param key
	 * @param request
	 * @param response
	 */
	@RequestMapping("/task/download.html")
	public void download(@RequestParam String key, 
			HttpServletRequest request, HttpServletResponse response){
		String fileName = PropertyHolder.getContextProperty(key);
		String fullpath =  request.getSession().getServletContext().getRealPath("./WEB-INF/common/"+fileName);
//		String filePath = WebReqConstant.COMMON_PATH;
//		String fileName = PropertyHolder.getContextProperty(key);
//		String fullpath = appPath + filePath;
//		
//		if (StringUtils.isEmpty(fileName)) {
//			throw new GnifException("找不到对应下载的文件信息！");
//		}
		//System.out.println(fileName+"#%%%%#"+fullpath);
		BufferedInputStream ins = null;
		BufferedOutputStream out = null;
		try {
//			fullpath = appPath + filePath + fileName;
//			fullpath = "D:\\" + fileName;
			if (!new File(fullpath).exists()) {
				response.setContentType("text/plain; charset=UTF-8");
				response.getWriter().println("对不起，您需要下载的文件不存在!");
				return;
			}

			response.setContentType("application/x-download; charset=utf-8");
			response.setHeader("Content-disposition", "attachment; filename="
					+ URLEncoder.encode(fileName, "UTF-8"));
			ins = new BufferedInputStream(new FileInputStream(fullpath));
			out = new BufferedOutputStream(response.getOutputStream());
			int i = 0;
			byte[] b = new byte[1024];
			while ((i = ins.read(b)) != -1)
				out.write(b, 0, i);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (ins != null) {
				try {
					ins.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 金铭相关流程excel模版下载
	 * @param key
	 * @param request
	 * @param response
	 */
	@RequestMapping("/task/mDownload.html")
	public void mDownload(@RequestParam String key, 
			HttpServletRequest request, HttpServletResponse response){
		String fileName = PropertyHolder.getContextProperty(key);
		String fullpath =  request.getSession().getServletContext().getRealPath("./WEB-INF/m-excel/"+fileName);
//		String filePath = WebReqConstant.COMMON_PATH;
//		String fileName = PropertyHolder.getContextProperty(key);
//		String fullpath = appPath + filePath;
//		
//		if (StringUtils.isEmpty(fileName)) {
//			throw new GnifException("找不到对应下载的文件信息！");
//		}
		//System.out.println(fileName+"#%%%%#"+fullpath);
		BufferedInputStream ins = null;
		BufferedOutputStream out = null;
		try {
//			fullpath = appPath + filePath + fileName;
//			fullpath = "D:\\" + fileName;
			if (!new File(fullpath).exists()) {
				response.setContentType("text/plain; charset=UTF-8");
				response.getWriter().println("对不起，您需要下载的文件不存在!");
				return;
			}

			response.setContentType("application/x-download; charset=utf-8");
			response.setHeader("Content-disposition", "attachment; filename="
					+ URLEncoder.encode(fileName, "UTF-8"));
			ins = new BufferedInputStream(new FileInputStream(fullpath));
			out = new BufferedOutputStream(response.getOutputStream());
			int i = 0;
			byte[] b = new byte[1024];
			while ((i = ins.read(b)) != -1)
				out.write(b, 0, i);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (ins != null) {
				try {
					ins.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 打印控件下载
	 * @param key
	 * @param request
	 * @param response
	 */
	@RequestMapping("/task/printDownload.html")
	public void printDownload(@RequestParam String key, 
			HttpServletRequest request, HttpServletResponse response){
		String fileName = PropertyHolder.getContextProperty(key);
		String fullpath =  request.getSession().getServletContext().getRealPath("./WEB-INF/print-install/"+fileName);
//		String filePath = WebReqConstant.COMMON_PATH;
//		String fileName = PropertyHolder.getContextProperty(key);
//		String fullpath = appPath + filePath;
//		
//		if (StringUtils.isEmpty(fileName)) {
//			throw new GnifException("找不到对应下载的文件信息！");
//		}
		//System.out.println(fileName+"#%%%%#"+fullpath);
		BufferedInputStream ins = null;
		BufferedOutputStream out = null;
		try {
//			fullpath = appPath + filePath + fileName;
//			fullpath = "D:\\" + fileName;
			if (!new File(fullpath).exists()) {
				response.setContentType("text/plain; charset=UTF-8");
				response.getWriter().println("对不起，您需要下载的文件不存在!");
				return;
			}

			response.setContentType("application/x-download; charset=utf-8");
			response.setHeader("Content-disposition", "attachment; filename="
					+ URLEncoder.encode(fileName, "UTF-8"));
			ins = new BufferedInputStream(new FileInputStream(fullpath));
			out = new BufferedOutputStream(response.getOutputStream());
			int i = 0;
			byte[] b = new byte[1024];
			while ((i = ins.read(b)) != -1)
				out.write(b, 0, i);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (ins != null) {
				try {
					ins.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * word文档模版下载
	 * @param key
	 * @param request
	 * @param response
	 */
	@RequestMapping("/task/wordDownload.html")
	public void wordDownload(@RequestParam String key, 
			HttpServletRequest request, HttpServletResponse response){
		String fileName = PropertyHolder.getContextProperty(key);
		String fullpath =  request.getSession().getServletContext().getRealPath("./WEB-INF/word-template/"+fileName);
//		String filePath = WebReqConstant.COMMON_PATH;
//		String fileName = PropertyHolder.getContextProperty(key);
//		String fullpath = appPath + filePath;
//		
//		if (StringUtils.isEmpty(fileName)) {
//			throw new GnifException("找不到对应下载的文件信息！");
//		}
		//System.out.println(fileName+"#%%%%#"+fullpath);
		BufferedInputStream ins = null;
		BufferedOutputStream out = null;
		try {
//			fullpath = appPath + filePath + fileName;
//			fullpath = "D:\\" + fileName;
			if (!new File(fullpath).exists()) {
				response.setContentType("text/plain; charset=UTF-8");
				response.getWriter().println("对不起，您需要下载的文件不存在!");
				return;
			}

			response.setContentType("application/x-download; charset=utf-8");
			response.setHeader("Content-disposition", "attachment; filename="
					+ URLEncoder.encode(fileName, "UTF-8"));
			ins = new BufferedInputStream(new FileInputStream(fullpath));
			out = new BufferedOutputStream(response.getOutputStream());
			int i = 0;
			byte[] b = new byte[1024];
			while ((i = ins.read(b)) != -1)
				out.write(b, 0, i);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (ins != null) {
				try {
					ins.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/*/**
	 * 下载部门活动模板teamListDownload
	 * @param key
	 * @param request
	 * @param response
	 */
/*	@RequestMapping("/task/downloadTeamlist.html")
	public void downloadTeamlist(@RequestParam String key, 
			HttpServletRequest request, HttpServletResponse response){
		String fileName = PropertyHolder.getContextProperty(key);
		String fullpath =  request.getSession().getServletContext().getRealPath("./WEB-INF/common/"+fileName);
//		String filePath = WebReqConstant.COMMON_PATH;
//		String fileName = PropertyHolder.getContextProperty(key);
//		String fullpath = appPath + filePath;
//		
//		if (StringUtils.isEmpty(fileName)) {
//			throw new GnifException("找不到对应下载的文件信息！");
//		}
		
		BufferedInputStream ins = null;
		BufferedOutputStream out = null;
		try {
//			fullpath = appPath + filePath + fileName;
//			fullpath = "D:\\" + fileName;
			if (!new File(fullpath).exists()) {
				response.setContentType("text/plain; charset=UTF-8");
				response.getWriter().println("对不起，您需要下载的文件不存在!");
				return;
			}

			response.setContentType("application/x-download; charset=utf-8");
			response.setHeader("Content-disposition", "attachment; filename="
					+ URLEncoder.encode(fileName, "UTF-8"));
			ins = new BufferedInputStream(new FileInputStream(fullpath));
			out = new BufferedOutputStream(response.getOutputStream());
			int i = 0;
			byte[] b = new byte[1024];
			while ((i = ins.read(b)) != -1)
				out.write(b, 0, i);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (ins != null) {
				try {
					ins.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	*//**
	 * 下载旅游活动模板travel
	 * @param taskId
	 * @param processInstanceId
	 * @return
	 *//*
	@RequestMapping("/task/downloadExcel.html")
	public void downloadPlugin(@RequestParam String key, 
			HttpServletRequest request, HttpServletResponse response){
		String fullpath =  request.getSession().getServletContext().getRealPath("./WEB-INF/common/travel.xlsx");
		//String filePath = "\\";
//		String filePath = WebReqConstant.TEMPLATE_PATH;
		String fileName = PropertyHolder.getContextProperty(key);
//		String fullpath = appPath + filePath;
//		
//		if (StringUtils.isEmpty(fileName)) {
//			throw new GnifException("找不到对应下载的文件信息！");
//		}
		
		BufferedInputStream ins = null;
		BufferedOutputStream out = null;
		try {
//			fullpath = appPath + filePath + fileName;
//			fullpath = "D:\\" + fileName;
			if (!new File(fullpath).exists()) {
				response.setContentType("text/plain; charset=UTF-8");
				response.getWriter().println("对不起，您需要下载的文件不存在!");
				return;
			}

			response.setContentType("application/x-download; charset=utf-8");
			response.setHeader("Content-disposition", "attachment; filename="
					+ URLEncoder.encode(fileName, "UTF-8"));
			ins = new BufferedInputStream(new FileInputStream(fullpath));
			out = new BufferedOutputStream(response.getOutputStream());
			int i = 0;
			byte[] b = new byte[1024];
			while ((i = ins.read(b)) != -1)
				out.write(b, 0, i);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (ins != null) {
				try {
					ins.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	*/
	/**
	 * 为任务添加附件
	 * @param myfiles
	 * @param processInstance
	 */
	private void saveAttachment(String taskId, String processInstanceId, String fileNo, String fileName) throws UnsupportedEncodingException {
		//把上传文件的文件编号存到DESCRIPTION，URL字段
		StringBuilder sb = new StringBuilder();
		String policy = EncryptUtil.getPolicy("0", "0", fileNo);

		sb.append("?").append("policy=").append(URLEncoder.encode(CalcUtil.getBase64(policy), "UTF-8")).append("&");
		sb.append("signature=").append(EncryptUtil.signature(osskey, policy)).append("&").append("code=").append(ossCode);

		Attachment attachment = taskService.createAttachment(null, taskId, processInstanceId, fileName, fileNo, fileDownLoadUrl + sb);

		taskService.saveAttachment(attachment);
	}
	
	
	@RequestMapping("/validatePermissionCrowd.json")
	@ResponseBody
	public AjaxJson validatePermissionCrowd(@RequestParam("account") String account, @RequestParam("processDefKey") String processDefKey){
		
		AjaxJson json = null;
		BpmProcessConf curProConf = null;
		try {
			QueryMap queryMap = new QueryMap();
			queryMap.put("processDefKey", processDefKey.split(":")[0]);
			queryMap.put("status", BPMConstant.STATUS_NORMAL);
			List<BpmProcessConf> bpmProConfList = bpmProcessConfService.query(queryMap);
			if (!CollectionUtils.isEmpty(bpmProConfList)) {
				curProConf = bpmProConfList.get(0);
				String permissionCrowd = curProConf.getPermissionCrowd();
				
				if (StringUtils.isNotEmpty(permissionCrowd)
						&& !(GnifStringUtils.inArr(account,permissionCrowd.split(",")))){
					json = new AjaxJson(false, "您没有权限发起该流程!");
				} else {
					if(StringUtils.isEmpty(permissionCrowd)){
						String processName=processDefKey.split(":")[0];
						if("L-Probation-Exam".equals(processName)){
							boolean flag=false;
							String manager=processHelpService.getOrgLeader(account,"1");
							String departer=processHelpService.getOrgLeader(account,"2");
							String director=processHelpService.getOrgLeader(account, "3");
							if(account.equals(manager) || account.equals(departer) || account.equals(director)){
								flag=true;
							}
							if(processHelpService.isOffSpecRole(account, "1")){
								flag=true;
							}
							String charge=processHelpService.getOrgLeader(account,"4");
							if(StringUtils.isEmpty(manager)&&account.equals(charge)&&!StringUtils.isEmpty(departer)){
								flag=true;
							}
							if(processHelpService.isMiddleManager(account)){
								flag = true;
							}
							if(!flag){
								return new AjaxJson(false, "您没有权限发起该流程!");
							}
						}else if("M-foreign-training-Application".equals(processName)){
							String result=processHelpService.isManager(account);
							if(result.equals("n")){
								return new AjaxJson(false, "您没有权限发起该流程!");
							}
						}else if("M-Welfare-Application".equals(processName)){
							Boolean result=processHelpService.isOfficeWorker(account);
							if(!result){
								return new AjaxJson(false, "您没有权限发起该流程!");
							}
						}else if("L-Personnel-Requirement".equals(processName)){
							if(!processHelpService.isMiddleManager(account)){
								return new AjaxJson(false, "您没有权限发起该流程!");
							}
						}else if("L-Account-Handle".equals(processName)){
							//判断是否满一年
							 if(isOffOneYear(processHelpService.getHrUser4Account(account).getEntryDate())){
								 return new AjaxJson(false, "员工入职满一年才可以发起流程");
							 }
							 Calendar calendar = Calendar.getInstance();
							 Date date = new Date();
							 calendar.setTime(date);
							 int month = calendar.get(Calendar.MONTH);
							 if(month!=3&&month!=9){
								 return new AjaxJson(false, "户口办理申请只有在4月份和10月份才可以申请");
							 }
							
						}else if("L-Airline-Ticke-Application".equals(processName)){
							String charge=processHelpService.getOrgLeader(account,"3");
							if(charge == null){
								HrUserDto hrUser = processHelpService.getHrUser4Account(account);
								if(hrUser.getJob()!= null && hrUser.getJob().contains("副总裁")){
									 return new AjaxJson(false, "您没有权限发起该流程!");
								}
							}
							if(account.equals(charge)){
								 return new AjaxJson(false, "您没有权限发起该流程!");
							}
						}else if("L-JinLiTeam-Activities".equals(processName)){
							  List<JTeamMonth> jTeamMonths =keepRunTaskDao.getAllJTeamMonth();
							  Date date=new Date();
							  Calendar calendar = Calendar.getInstance();
							  calendar.setTime(date);
							  Integer currentMonth = calendar.get(Calendar.MONTH)+1;
							  json = new AjaxJson(false);
							  for(JTeamMonth jTeamMonth:jTeamMonths){
								  Integer applyMonth = jTeamMonth.getApply_month();
								  if(applyMonth == currentMonth || applyMonth+1 ==currentMonth){
									  json = new AjaxJson(true);
									  break;
								  }
							  }
							  if(!json.isSuccess()){
								  return new AjaxJson(false, "您好，上个季度的部门活动经费申请已经超过时间限制！");
							  }
						}
					} 
					json = new AjaxJson(true);
				}
			}else{
				json = new AjaxJson(false, "该流程暂时不可用!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			json = new AjaxJson(false, "您没有权限发起该流程!");
		}
		return json;
	}
	public boolean isOffOneYear(Date inDate){
		  Date date=new Date();
		  Long time1=date.getTime();
		  Calendar cal=Calendar.getInstance();  
		cal.setTime(inDate);
		cal.add(Calendar.MONTH,+12);
		inDate=cal.getTime();
		Long time2=inDate.getTime();
		if(time1<time2){
			return true;
		}else{
			return false;
		}
	  }
}
