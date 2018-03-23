/**
 * @(#) WorkspaceController.java Created on 2014年5月28日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.activiti.bpmn.constants.BpmnXMLConstants;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.AppUser;
import com.gionee.gnif.util.AppContext;
import com.gionee.gniflow.biz.model.BpmProcessRun;
import com.gionee.gniflow.biz.model.BpmTaskExecution;
import com.gionee.gniflow.biz.model.LTeamActivitiesDetail;
import com.gionee.gniflow.biz.model.MLeaveApplicationInfo;
import com.gionee.gniflow.biz.model.SignFileInformation;
import com.gionee.gniflow.biz.model.TeamActivitiesDetail;
import com.gionee.gniflow.biz.service.ActivitiHelpService;
import com.gionee.gniflow.biz.service.LTeamActivitiesDetailService;
import com.gionee.gniflow.biz.service.MLeaveApplicationInfoService;
import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.biz.service.RapInforService;
import com.gionee.gniflow.biz.service.SignFileInformationService;
import com.gionee.gniflow.biz.service.TeamActivitiesDetailService;
import com.gionee.gniflow.constant.BPMConstant;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.dto.FlowMessageDto;
import com.gionee.gniflow.dto.TaskDto;
import com.gionee.gniflow.integration.dao.BpmTaskExecutionDao;
import com.gionee.gniflow.integration.dao.KeepRunTaskDao;
import com.gionee.gniflow.web.bmp.BpmHisActivityEntity;
import com.gionee.gniflow.web.bmp.BpmHisProcessInstanceEntity;
import com.gionee.gniflow.web.bmp.BpmHisTaskEntity;
import com.gionee.gniflow.web.bmp.BpmTaskEntity;
import com.gionee.gniflow.web.cmd.HistoryProcessInstanceDiagramCmd;
import com.gionee.gniflow.web.cmd.ProcessDefinitionDiagramCmd;
import com.gionee.gniflow.web.cmd.TaskCmd;
import com.gionee.gniflow.web.cmd.WithdrawTaskCmd;
import com.gionee.gniflow.web.easyui.AjaxJson;
import com.gionee.gniflow.web.easyui.GridPageData;
import com.gionee.gniflow.web.util.HttpClientUtil;
import com.gionee.gniflow.web.util.ProcessConvertUtil;
import com.gionee.gniflow.web.util.PropertyHolder;
import com.gionee.gniflow.web.util.SendMailTool;

/**
 * The class <code>WorkspaceController</code>
 * 
 * @author lipw
 * @version 1.0
 */
@RequestMapping("/workspace")
@Controller
public class WorkspaceController {
	
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
	private ActivitiHelpService activitiHelpService;
	
	@Autowired
	private SignFileInformationService signFileInformationService;
	
	@Autowired
	private ProcessHelpService processHelpService;
	
	@Autowired
	private MLeaveApplicationInfoService mLeaveApplicationInfoService;

	@Autowired
	private TeamActivitiesDetailService  teamActivitiesDetailService;
	
	@Autowired
	private LTeamActivitiesDetailService  lTeamActivitiesDetailService;
	@Autowired
	private BpmTaskExecutionDao bpmTaskExecutionDao;
	
	@Autowired
	private KeepRunTaskDao keepRunTaskDao;
	
	@Autowired
	private RapInforService rapInforService;
	/**
	 * 导出用户发起的流程
	 * @param queryMap
	 * @param request
	 * @param response
	 */
	 @RequestMapping("/exportRunningProcessInfo.html")
	 @ResponseBody
	 public void exportRunningProcessInfo(QueryMap queryMap,HttpServletRequest request,HttpServletResponse response){
	    	
		 List<String> sapProDefIds = processHelpService.getSapProcessDef();
			queryMap.put("assignee", AppContext.getCurrentAccount());
			queryMap.put("sapProcessIds", sapProDefIds);
			queryMap.put("finished", "false");
			queryMap.put("lastRow", 1000);
			List<BpmHisProcessInstanceEntity> results = activitiHelpService.page4StartProcess(queryMap);
			
			//设置流程开始到现在的耗时
			for (BpmHisProcessInstanceEntity entity : results) {
				if (entity.getEndTime() == null) {
					entity.setDurationInMillis(new Date().getTime() - entity.getStartTime().getTime());

				}
			}
			
			try {
		    	if (!CollectionUtils.isEmpty(results)) {
		    		toRunningProcessInfoExcel(response, results);
		    	}else {
		            response.setContentType("text/plain; charset=UTF-8");
					try {
						response.getWriter().println("数据不存在,导出Excel失败!");
					} catch (IOException ex) {
						ex.printStackTrace();
					}
		    	}
	    	} catch (Exception e) {
	            e.printStackTrace();
	            response.setContentType("text/plain; charset=UTF-8");
				try {
					response.getWriter().println("导出Excel文件失败!");
				} catch (IOException ex) {
					ex.printStackTrace();
				}
	        } 
	    }
	    
	    void toRunningProcessInfoExcel(HttpServletResponse response, List<BpmHisProcessInstanceEntity> results) throws Exception{
	    	// 获取总列数
	        // 创建Excel文档
	        HSSFWorkbook hwb = new HSSFWorkbook();
	        // sheet 对应一个工作页
	        HSSFSheet sheet = hwb.createSheet("发起流程明细表");
	        HSSFRow firstrow = sheet.createRow(0); // 下标为0的行开始
	        //流程实例编号		发起人编号		发起人姓名			离职人员编号			离职人员姓名	离职人员部门	离职操作类型	 			离职日期
	        //procInstId	applyUserId	applyUserName	applyUserAccount	userName	orgName	 	operationReasonText		leaveDate
	        
	        //流程实例编号   			实例名称   					流程名称   				流程分类   
	        //processInstanceId		processInstanceName		processDefName		categoryName
	        //创建时间   		负责人   			截止今日处理耗时(小时)   状态
	        //startTime		startUserId		durationInMillis	processStatus
	        String[] names = new String[]{"流程实例编号","实例名称","流程名称","流程分类 ","创建时间 ","负责人编号","负责人姓名",/*"截止今日处理耗时(小时)",*/"状态"};
	        //String[] values = new String[]{"processInstanceId","processInstanceName","processDefName","categoryName","startTime","startUserId","startUserName","durationInMillis","processStatus"};
	        
	        //设置excel宽度
	        int flag = 0;
	        sheet.setColumnWidth(flag++, 20 * 255);
	        sheet.setColumnWidth(flag++, 20 * 255);
	        sheet.setColumnWidth(flag++, 20 * 255);
	        sheet.setColumnWidth(flag++, 20 * 255);
	        sheet.setColumnWidth(flag++, 20 * 255);
	        sheet.setColumnWidth(flag++, 30 * 255);
	        sheet.setColumnWidth(flag++, 25 * 255);
	       // sheet.setColumnWidth(flag++, 20 * 255);
	        
	        int CountColumnNum = names.length;
	        for (int i = 0; i < CountColumnNum; i++) {
	            firstrow.createCell(i).setCellValue(new HSSFRichTextString(names[i]));
	        }
	        HSSFCellStyle cellStyleStr = hwb.createCellStyle();  
	        cellStyleStr.setDataFormat(hwb.createDataFormat().getFormat("@"));  
	        cellStyleStr.setWrapText(true);
			
	        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	        int rown=1;
	        for(BpmHisProcessInstanceEntity bpie: results){
		        HSSFRow row = sheet.createRow(rown++);
		        
	            HSSFCell cell=row.createCell(0);
	            cell.setCellStyle(cellStyleStr);
	            cell.setCellValue(bpie.getProcessInstanceId());
	            cell=row.createCell(1);
	            cell.setCellValue(bpie.getProcessInstanceName());
	            cell=row.createCell(2);
	            cell.setCellValue(bpie.getProcessDefName());
	            cell=row.createCell(3);
	            cell.setCellValue(bpie.getCategoryName());
	            cell=row.createCell(4);
	           	cell.setCellValue(df.format((Date)bpie.getStartTime()));
	           	cell=row.createCell(5);
	            cell.setCellValue(bpie.getStartUserId());
	            cell=row.createCell(6);
	            cell.setCellValue(processHelpService.getUserName(bpie.getStartUserId()));
	            cell=row.createCell(7);
	            /*cell.setCellValue((double)((bpie.getDurationInMillis())/1000/3600));
	            cell=row.createCell(8);*/
	            int status = bpie.getProcessStatus();
	            if(status == 1){
	            	cell.setCellValue("运行中");
	            }else if(status == 2){
	            	cell.setCellValue("已完成");
	            }else if(status == 3){
	            	cell.setCellValue("已挂起");
	            }else if(status == 4){
	            	cell.setCellValue("已激活");
	            }else if(status == 5){
	            	cell.setCellValue("已作废");
	            }else {
	            	cell.setCellValue("未知状态");
	            }
	        }
	       
	        // 创建文件输出流，准备输出电子表格
	        response.setContentType("application/vnd.ms-excel");   
	        String encodedFileName = "runningProcessInfoExcel.xls" ; 
			response.setHeader("Content-disposition", "attachment;filename=" + encodedFileName);
			OutputStream out = response.getOutputStream();
	        hwb.write(out);
	        out.flush();
	        out.close();
	        //System.out.println("数据库导出成功");
	    }
	    
	    
	    @RequestMapping("/exportCompletedProcessInfo.html")
		@ResponseBody
		public void exportCompletedProcessInfo(QueryMap queryMap,HttpServletRequest request,HttpServletResponse response){
		    	
	    	List<String> sapProDefIds = processHelpService.getSapProcessDef();
			
			queryMap.put("assignee", AppContext.getCurrentAccount());
			queryMap.put("sapProcessIds", sapProDefIds);
			queryMap.put("finished", "true");
			queryMap.put("lastRow", 1000);
			List<BpmHisProcessInstanceEntity> results = activitiHelpService.page4StartProcess(queryMap);
				
			//设置流程开始到现在的耗时
			for (BpmHisProcessInstanceEntity entity : results) {
				if (entity.getEndTime() == null) {
					entity.setDurationInMillis(entity.getEndTime().getTime() - entity.getStartTime().getTime());

				}
			}
			
				try {
			    	if (!CollectionUtils.isEmpty(results)) {
			    		toCompletedProcessInfoExcel(response, results);
			    	}else {
			            response.setContentType("text/plain; charset=UTF-8");
						try {
							response.getWriter().println("数据不存在,导出Excel失败!");
						} catch (IOException ex) {
							ex.printStackTrace();
						}
			    	}
		    	} catch (Exception e) {
		            e.printStackTrace();
		            response.setContentType("text/plain; charset=UTF-8");
					try {
						response.getWriter().println("导出Excel文件失败!");
					} catch (IOException ex) {
						ex.printStackTrace();
					}
		        } 
				
		    }
		    
		    void toCompletedProcessInfoExcel(HttpServletResponse response, List<BpmHisProcessInstanceEntity> results) throws Exception{
		    	// 获取总列数
		        // 创建Excel文档
		        HSSFWorkbook hwb = new HSSFWorkbook();
		        // sheet 对应一个工作页
		        HSSFSheet sheet = hwb.createSheet("办结流程明细表");
		        HSSFRow firstrow = sheet.createRow(0); // 下标为0的行开始
		        //流程实例编号		发起人编号		发起人姓名			离职人员编号			离职人员姓名	离职人员部门	离职操作类型	 			离职日期
		        //procInstId	applyUserId	applyUserName	applyUserAccount	userName	orgName	 	operationReasonText		leaveDate
		        
		        //流程实例编号   			实例名称   					流程名称   				流程分类   
		        //processInstanceId		processInstanceName		processDefName		categoryName
		        //创建时间   		负责人   			截止今日处理耗时(小时)   状态
		        //startTime		startUserId		durationInMillis	processStatus
		        String[] names = new String[]{"流程实例编号","实例名称","流程名称","流程分类 ","创建时间 ","结束时间 ","负责人编号","负责人姓名",/*"处理耗时(小时) ",*/"状态"};
		        //String[] values = new String[]{"processInstanceId","processInstanceName","processDefName","categoryName","startTime","endTime","startUserId","startUserName","durationInMillis","processStatus"};
		        
		        //设置excel宽度
		        int flag = 0;
		        sheet.setColumnWidth(flag++, 20 * 255);
		        sheet.setColumnWidth(flag++, 20 * 255);
		        sheet.setColumnWidth(flag++, 20 * 255);
		        sheet.setColumnWidth(flag++, 20 * 255);
		        sheet.setColumnWidth(flag++, 20 * 255);
		        sheet.setColumnWidth(flag++, 30 * 255);
		        sheet.setColumnWidth(flag++, 25 * 255);
		        sheet.setColumnWidth(flag++, 20 * 255);
		        //sheet.setColumnWidth(flag++, 20 * 255);
		        
		        int CountColumnNum = names.length;
		        for (int i = 0; i < CountColumnNum; i++) {
		            firstrow.createCell(i).setCellValue(new HSSFRichTextString(names[i]));
		        }
		        HSSFCellStyle cellStyleStr = hwb.createCellStyle();  
		        cellStyleStr.setDataFormat(hwb.createDataFormat().getFormat("@"));  
		        cellStyleStr.setWrapText(true);
		        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		        int rown=1;
		        for(BpmHisProcessInstanceEntity bpie: results){
			        HSSFRow row = sheet.createRow(rown++);
			        
		            HSSFCell cell=row.createCell(0);
		            cell.setCellStyle(cellStyleStr);
		            cell.setCellValue(bpie.getProcessInstanceId());
		            cell=row.createCell(1);
		            cell.setCellValue(bpie.getProcessInstanceName());
		            cell=row.createCell(2);
		            cell.setCellValue(bpie.getProcessDefName());
		            cell=row.createCell(3);
		            cell.setCellValue(bpie.getCategoryName());
		            cell=row.createCell(4);
		           	cell.setCellValue(df.format((Date)bpie.getStartTime()));
		           	cell=row.createCell(5);
		            cell.setCellValue(df.format((Date)bpie.getEndTime()));
		            cell=row.createCell(6);
		            cell.setCellValue(bpie.getStartUserId());
		            cell=row.createCell(7);
		            cell.setCellValue(processHelpService.getUserName(bpie.getStartUserId()));
		            cell=row.createCell(8);
		            /*//NumberFormat nf = NumberFormat.getNumberInstance();
		            //nf.setMaximumFractionDigits(2);
		            DecimalFormat def = new DecimalFormat("###.00");
		           	cell.setCellValue( def.format(bpie.getDurationInMillis()/1000/3600));
		            cell=row.createCell(9);*/
		            int status = bpie.getProcessStatus();
		            if(status == 1){
		            	cell.setCellValue("运行中");
		            }else if(status == 2){
		            	cell.setCellValue("审批通过");
		            }else if(status == 3){
		            	cell.setCellValue("已挂起");
		            }else if(status == 4){
		            	cell.setCellValue("已激活");
		            }else if(status == 5){
		            	cell.setCellValue("已作废");
		            } else if(status == 6){
		            	cell.setCellValue("审批未通过");
		            }else {
		            	cell.setCellValue("未知状态");
		            }
		        }
		       
		        // 创建文件输出流，准备输出电子表格
		        response.setContentType("application/vnd.ms-excel");   
		        String encodedFileName = "completedProcessInfoExcel.xls" ; 
				response.setHeader("Content-disposition", "attachment;filename=" + encodedFileName);
				OutputStream out = response.getOutputStream();
		        hwb.write(out);
		        out.flush();
		        out.close();
		        //System.out.println("数据库导出成功");
		    }
	    
	/** 根据用户查询运行中的流程 */
	@RequestMapping("/runingProcess.json")
	@ResponseBody
	public GridPageData<BpmHisProcessInstanceEntity> getRuningProcess(QueryMap queryMap) {
		
		List<String> sapProDefIds = processHelpService.getSapProcessDef();
		
		queryMap.put("assignee", AppContext.getCurrentAccount());
		queryMap.put("sapProcessIds", sapProDefIds);
		queryMap.put("finished", "false");
		
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

	/** 根据用户查询完结的流程 */
	@RequestMapping("/completedProcess.json")
	@ResponseBody
	public GridPageData<BpmHisProcessInstanceEntity> getCompletedProcess(QueryMap queryMap) {
		
		List<String> sapProDefIds = processHelpService.getSapProcessDef();
		
		queryMap.put("assignee", AppContext.getCurrentAccount());
		queryMap.put("sapProcessIds", sapProDefIds);
		queryMap.put("finished", "true");
		
		
		List<BpmHisProcessInstanceEntity> hisProInstances = activitiHelpService.page4StartProcess(queryMap);
		for (BpmHisProcessInstanceEntity entity : hisProInstances) {
			entity.setStartUserName(entity.getStartUserName());
		}
		Integer total = activitiHelpService.count4StartProcess(queryMap);

		return new GridPageData<BpmHisProcessInstanceEntity>(hisProInstances, total.longValue());
	}

	/** 根据用户查询参与的流程 */
	@RequestMapping("/involvedProcess.json")
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
		List<BpmHisProcessInstanceEntity> hisProInstances = activitiHelpService.page4InvolvedUserProcess(queryMap);
		
		//设置流程开始到现在的耗时
		for (BpmHisProcessInstanceEntity entity : hisProInstances) {
			entity.setStartUserName(entity.getStartUserName() );//修改流程发起人编号姓名改为发起人姓名			
		}
		
		Integer total = activitiHelpService.count4InvolvedUserProcess(queryMap);

		return new GridPageData<BpmHisProcessInstanceEntity>(hisProInstances, total.longValue());
	}

	/** 根据用户终止流程 */
	@RequestMapping("/obsoleteProcess.json")
	@ResponseBody
	public AjaxJson obsoleteProcess(@RequestParam("processInstanceId") String processInstanceId) {
		try {
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
			if(processInstance!=null && processInstance.getProcessDefinitionId().startsWith("L-Leave-Application")){
				
				HistoricVariableInstance historicVariableInstance = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).variableName("hrCommissionerAccount").singleResult();
				if(historicVariableInstance != null){
					String hrCommissionerAccount = (String)historicVariableInstance.getValue();
					if(!AppContext.getCurrentAccount().equals(hrCommissionerAccount)){
						return new AjaxJson(false, "离职流程作废功能暂时关闭!");
					}
				}
			}
			//特殊处理文档会签流程
			QueryMap queryMap = new QueryMap();
			queryMap.put("procInstId", processInstanceId);
			List<SignFileInformation> searchResult = signFileInformationService.querySignFileInformation(queryMap);
			List<MLeaveApplicationInfo> mlInfolist=mLeaveApplicationInfoService.query(queryMap);
			List<TeamActivitiesDetail> tadlist = teamActivitiesDetailService.query(queryMap);
			if (!CollectionUtils.isEmpty(searchResult)) {
				SignFileInformation signFile = searchResult.get(0);
				signFileInformationService.logicDelete(signFile.getId());
			}
			if (!CollectionUtils.isEmpty(mlInfolist)) {
				MLeaveApplicationInfo mlinfo = mlInfolist.get(0);
				mLeaveApplicationInfoService.delete(mlinfo.getId().toString());
			}
			if(!CollectionUtils.isEmpty(tadlist)){
				teamActivitiesDetailService.delete(processInstanceId);
			}
			lTeamActivitiesDetailService.deleteByProInstId(processInstanceId);
			try {
				rapInforService.delete(processInstanceId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Boolean reuslt = null;
			// 对于PMS，需要调用相应的接口进行进行通知
			Execution excution =null;
//			excution =runtimeService.createExecutionQuery().processInstanceId(processInstanceId).singleResult();
			
			List<Execution>  list = runtimeService.createExecutionQuery().processInstanceId(processInstanceId).list();
			if(list!=null){
			if(list.size()>1){
				processHelpService.changeProcessState(BpmProcessRun.STATUS_OBSOLETE, processInstanceId);
				mLeaveApplicationInfoService.deleteObsoleteProcess(processInstanceId);
				teamActivitiesDetailService.delete(processInstanceId);
				lTeamActivitiesDetailService.deleteByProInstId(processInstanceId);
				keepRunTaskDao.delTaskInf(processInstanceId);
				keepRunTaskDao.delEmpAndTeaInfByProcId(processInstanceId);
				return new AjaxJson(true, "废止流程成功!");
			}else if(list.size()==1){
				excution =runtimeService.createExecutionQuery().processInstanceId(processInstanceId).singleResult();
			if(excution != null){
				// PMS流程进入到了等待完成的节点
				if(excution.getActivityId().equals("receiveProjectReusltTask"))
				{
					// 调用PMS接口，对内容进行中止
					Map<String,String> reqData = new HashMap<String,String>();
					reqData.put("processInstanceId", processInstanceId);
					String result =  HttpClientUtil.sendPostRequest(PropertyHolder.getContextProperty("pms.url.cancelPlan"), reqData, "UTF-8");
					if(!result.contains(":")){
						return new AjaxJson(false, "由于PMS服务器连接失败，作废流程失败!");
					} else{
						FlowMessageDto dto = new ObjectMapper().readValue(result, FlowMessageDto.class);
						if(dto.isSuccess()) {
//							runtimeService.deleteProcessInstance(processInstanceId, "end");
							//判断该流程的开始节点后的一个节点是否已经被办理，没有才能作废
							//reuslt = processHelpService.firstTaskIsHandledWithStartEvent(processInstanceId);
							//if (reuslt) {
								//return new AjaxJson(false, "该流程的下一步骤责任人办理已办理,废止流程失败!");
							//} else {
								processHelpService.changeProcessState(BpmProcessRun.STATUS_OBSOLETE, processInstanceId);
								mLeaveApplicationInfoService.deleteObsoleteProcess(processInstanceId);
								teamActivitiesDetailService.delete(processInstanceId);
								lTeamActivitiesDetailService.deleteByProInstId(processInstanceId);
								keepRunTaskDao.delTaskInf(processInstanceId);
								keepRunTaskDao.delEmpAndTeaInfByProcId(processInstanceId);
								return new AjaxJson(true, "废止流程成功!");
							//}
							
						} else {
							return new AjaxJson(false, "由于PMS系统操作失败，终止流程失败!");
						}
					}
				}
				// 其他非PMS的流程
				else
				{
					//判断该流程的开始节点后的一个节点是否已经被办理，没有才能作废
					//reuslt = processHelpService.firstTaskIsHandledWithStartEvent(processInstanceId);
					//if (reuslt) {
						//return new AjaxJson(false, "该流程的下一步骤责任人已办理,废止流程失败!");
					//} else {
						processHelpService.changeProcessState(BpmProcessRun.STATUS_OBSOLETE, processInstanceId);
						mLeaveApplicationInfoService.deleteObsoleteProcess(processInstanceId);
						teamActivitiesDetailService.delete(processInstanceId);
						lTeamActivitiesDetailService.deleteByProInstId(processInstanceId);
						keepRunTaskDao.delTaskInf(processInstanceId);
						keepRunTaskDao.delEmpAndTeaInfByProcId(processInstanceId);
						return new AjaxJson(true, "废止流程成功!");
					//}
				}
			}
		 }
		}
			return new AjaxJson(false, "废止流程失败!");
			
		} catch (Exception e) {
			e.printStackTrace();
			return new AjaxJson(false, "废止流程失败!");
		}
	}
	
	/** 判读该流程是否已经被处理 */
	@RequestMapping("/isHandleByNextAudi.json")
	@ResponseBody
	public AjaxJson isHandleByNextAudi(@RequestParam("processInstanceId") String processInstanceId) {
		boolean reuslt = processHelpService.firstTaskIsHandledWithStartEvent(processInstanceId);
		if (reuslt) {
			return new AjaxJson(false, "该流程的下一步骤责任人已办理,您确定废止吗？");
		} else {
			return new AjaxJson(true, "");
		}
		
	}
	/** 查看流程定义图片 */
	@RequestMapping("graphProcessDefinitionImage")
    public void graphProcessDefinitionImage(@RequestParam("processDefinitionId") String processDefinitionId,
            HttpServletResponse response) throws Exception {
        
        Command<InputStream> cmd = null;
        cmd = new ProcessDefinitionDiagramCmd(processDefinitionId);

        InputStream is = managementService.executeCommand(cmd);
        response.setContentType("image/png");

        IOUtils.copy(is, response.getOutputStream());
        
    }

	/** 查看历史流程图 */
	@RequestMapping("/graphProcessDefinition")
	public void graphProcessDefinition(@RequestParam("processInstanceId") String processInstanceId,
			HttpServletResponse response) throws Exception {
		Command<InputStream> cmd = new HistoryProcessInstanceDiagramCmd(processInstanceId);

        InputStream is = managementService.executeCommand(cmd);
        response.setContentType("image/png");
        
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        int len = 0;
        byte[] b = new byte[1024];

        while ((len = is.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
	}
	
	/**
     * 查看历史【任务列表--完成和未完成】
     */
    @RequestMapping("/viewHistoryTask.json")
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
    
    /**
     * 查看历史【任务列表--流程变量】
     */
    @RequestMapping("/viewHistoryVariable.json")
    @ResponseBody
    public GridPageData<HistoricVariableInstance> viewHistoryVariable(@RequestParam("processInstanceId") String processInstanceId,
    		@RequestParam("page") Integer page, @RequestParam("rows") Integer rows) {
        List<HistoricVariableInstance> historicVariableInstances = historyService
                .createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId).listPage(page-1, rows);
                
        Long total = historyService
                .createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId).count();

        return new GridPageData<HistoricVariableInstance>(historicVariableInstances, total);
    }
    
    /**
     * 查看历史【流程附件】
     */
    @RequestMapping("/viewHistoryAttachment.json")
    @ResponseBody
    public GridPageData<Attachment> viewHistoryAttachment(@RequestParam("processInstanceId") String processInstanceId) {
        //附件
    	List<Attachment> attachmentList = taskService.getProcessInstanceAttachments(processInstanceId);

    	return new GridPageData<Attachment>(attachmentList, (long) attachmentList.size());
    }
	
	/******************************personalTask*********************************************/
	/** 根据用户查询待办任务 */
	@RequestMapping("/personalTask.json")
	@ResponseBody
	public GridPageData<TaskDto> personalTask(QueryMap queryMap) {

		List<String> sapProIds = processHelpService.getSapProcessDef();
		queryMap.put("assignee", AppContext.getCurrentAccount());
		queryMap.put("suspensionState", 1);
		queryMap.put("sapProcessIds", sapProIds);
		queryMap.put("processStatus", BpmProcessRun.STATUS_RUN);
		
		List<BpmTaskEntity> resuktTasks = activitiHelpService.page4AssigneeTask(queryMap);
		
		Integer total = activitiHelpService.count4AssigneeTask(queryMap);
		
		return new GridPageData<TaskDto>(ProcessConvertUtil.convert2TaskDto(resuktTasks), total.longValue());
	}
	
	//zj  派单任务查看选中工程师待处理任务
	@RequestMapping("/engineerTask.json")
	@ResponseBody
	public GridPageData<TaskDto> engineerTask(QueryMap queryMap) {

		List<String> sapProIds = processHelpService.getSapProcessDef();
//		queryMap.put("assignee", AppContext.getCurrentAccount());
		queryMap.put("suspensionState", 1);
		queryMap.put("sapProcessIds", sapProIds);
		queryMap.put("processStatus", BpmProcessRun.STATUS_RUN);
		
		List<BpmTaskEntity> resuktTasks = activitiHelpService.page4EngineerTask(queryMap);
		
		Integer total = activitiHelpService.count4EngineerTask(queryMap);
		
		return new GridPageData<TaskDto>(ProcessConvertUtil.convert2TaskDto(resuktTasks), total.longValue());
	}
	
	/** 根据用户查询待办任务forEOAMobile */
	@RequestMapping("/personalTaskForEOAMobile.json")
	@ResponseBody
	/******************************personalTaskForEOAMobile*********************************************/
	public GridPageData<TaskDto> personalTaskForEOAMobile(QueryMap queryMap) {

		List<String> sapProIds = processHelpService.getSapProcessDef();
		queryMap.put("assignee", AppContext.getCurrentAccount());
		queryMap.put("suspensionState", 1);
		queryMap.put("sapProcessIds", sapProIds);
		queryMap.put("processStatus", BpmProcessRun.STATUS_RUN);
		queryMap.put("conShowInMobile",1);
		
		List<BpmTaskEntity> resuktTasks = activitiHelpService.page4AssigneeTask(queryMap);
		
		Integer total = activitiHelpService.count4AssigneeTask(queryMap);
		
		return new GridPageData<TaskDto>(ProcessConvertUtil.convert2TaskDto(resuktTasks), total.longValue());
	}
	
	/** 根据用户查询已办任务 */
	@RequestMapping("/completedTask.json")
	@ResponseBody
	public GridPageData<BpmHisTaskEntity> completedTask(QueryMap queryMap) {
		
		
		
		List<String> sapProIds = processHelpService.getSapProcessDef();

		queryMap.put("assignee", AppContext.getCurrentAccount());
		queryMap.put("sapProcessIds", sapProIds);
		List<BpmHisTaskEntity> hisTasks = activitiHelpService.page4CompletedTask(queryMap);
		for (BpmHisTaskEntity taskEntity : hisTasks) {
			//taskEntity.setProStartUserName("[" + taskEntity.getProStartUserId() + "]" + taskEntity.getProStartUserName() );
			taskEntity.setProStartUserName(taskEntity.getProStartUserName() );//将显示编号名称改为只显示姓名			
		}
		
		Integer total = activitiHelpService.count4CompletedTask(queryMap);

		return new GridPageData<BpmHisTaskEntity>(hisTasks, total.longValue());
	}
	
	/** 签收任务 */
	@RequestMapping("/signTask.html")
	@ResponseBody
	public AjaxJson signTask(@RequestParam("taskId") String id) {
		AjaxJson ajaxJson = null;
		try {
			String userId = AppContext.getCurrentAccount();
			taskService.claim(id, userId);
			ajaxJson = new AjaxJson(true, "签收任务成功!");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = new AjaxJson(true, "签收任务失败!");
		}
		return ajaxJson;
	}
	
	/** 催办 */
	@RequestMapping("/remindersTask.html")
	@ResponseBody
	public AjaxJson remindersTask(@RequestParam("taskId") String taskId) {
		AjaxJson ajaxJson = null;
		try {
			BpmHisActivityEntity hisActivityEntity = activitiHelpService.queryHisActivity(taskId);
			String user = hisActivityEntity.getAssignee();
			// 如果任务是PMS流程中的等待响应的节点
			if(user == null && hisActivityEntity.getActivityType().equals(BpmnXMLConstants.ELEMENT_TASK_RECEIVE)) {
				// 得到最后一个任务的责任人
				HistoricTaskInstance task = historyService.createHistoricTaskInstanceQuery()
						.processInstanceId(hisActivityEntity.getProcessInstanceId())
						.orderByHistoricTaskInstanceStartTime().desc().list().get(0);
				user = task.getAssignee();
			}
			
			// 对于PMS的最后一个任务的催办，需要进行特殊处理
			if( user != null) {
				String title = PropertyHolder.getContextProperty(WebReqConstant.REMINDERS_TASK_EMAIL_TITLE);
				String content = processHelpService.getRemindersTaskEmailContent(taskId, user);
				String email = processHelpService.getUserEmailAddress(user);
				//processHelpService.sendEmail4UserAccount(Arrays.asList(new String[]{email}), title, content);
		        if (!StringUtils.isEmpty(email)) {
		        	SendMailTool.sendMailByAddress(title, content, email);
		        }
				ajaxJson = new AjaxJson(true, "催办任务成功!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = new AjaxJson(false, "催办任务失败!");
		}
		return ajaxJson;
	}
	
	/** 转办 */
	@RequestMapping("/turntodoTask/{taskId}.html")
	@ResponseBody
	public AjaxJson turntodoTask(@PathVariable("taskId") String taskId, @RequestParam("accepter") String accepter,
			@RequestParam("turntodoReason") String turntodoReason,
			@RequestParam(value="hrOperate", required=false) Boolean hrOperate) {
		AjaxJson ajaxJson = null;
		try {
			AppUser curUser = AppContext.getCurrentAppUser();
			TaskEntity taskEntity = (TaskEntity) taskService.createTaskQuery().taskId(taskId).singleResult();
			if (taskEntity == null) {
				return new AjaxJson(false, "任务已经被处理，转办任务失败!");
			}
			String assignee = taskEntity.getAssignee();
			if ((!StringUtils.isEmpty(assignee)) && (assignee.equals(accepter))) {
				return new AjaxJson(false, "不能转办给任务执行人，转办任务失败!");
			}
			
			if (hrOperate == null || !hrOperate) {
				if ((StringUtils.isNotEmpty(assignee)) && (!assignee.equals(curUser.getAccount()))){
					return new AjaxJson(false, "对不起，转办失败。您（已）不是任务的执行人,转办任务失败!");
				}
			}
			
			BpmTaskExecution taskExe = new BpmTaskExecution();
			taskExe.setTaskId(taskEntity.getId());
			taskExe.setAssignee(accepter);
			String accepterName = processHelpService.getHrUser4Account(accepter).getName();
			taskExe.setAssigneeName(accepterName);
			taskExe.setOwner(taskEntity.getAssignee());
			taskExe.setOwnerName(curUser.getName());
			taskExe.setTaskName(taskEntity.getName());
			taskExe.setTaskDefKey(taskEntity.getTaskDefinitionKey());
			taskExe.setProcInstId(taskEntity.getProcessInstanceId());
			taskExe.setSuggestion((turntodoReason==null||"".equals(turntodoReason.trim()))?"无":turntodoReason);
			taskExe.setAssignType(BPMConstant.TASK_ASSIGN_TYPE_TODO);
			
			activitiHelpService.updateTaskAssignee(taskExe);
			
			ajaxJson = new AjaxJson(true, "转办任务成功!下一处理人是["+accepter+"-"+accepterName+"]");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = new AjaxJson(false, "转办任务失败!");
		}
		return ajaxJson;
	}
	
	/** 取回任务*/
	@RequestMapping("/recallTask.html")
	@ResponseBody
	public AjaxJson recallTask(@RequestParam("taskId") String taskId,String processInstanceId) {
		AjaxJson ajaxJson = new AjaxJson();
		try {
			 Command<Integer> cmd = new WithdrawTaskCmd(taskId);
			 //0-撤销成功 1-流程结束 2-下一结点已经通过,不能撤销
			 Integer resultCode = managementService.executeCommand(cmd);
			 switch (resultCode) {
			 case 0:
				keepRunTaskDao.delTaskInf(processInstanceId);
				ajaxJson.setSuccess(true);
				ajaxJson.setMsg("取回成功!");
				break;
			 case 1:
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("流程已经结束,取回失败!");
				break;
			 case 2:
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("下一结点已经通过，取回失败!");
				break;
			 default:
				break;
			 }
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = new AjaxJson(false, "取回失败!");
		}
		return ajaxJson;
	}
	/**
	 * 退回任务
	 * @param taskInstanceId
	 * @return
	 */
	@RequestMapping("/returnTask.html")
	@ResponseBody
	public AjaxJson returnTask(@RequestParam("processInstanceId") String processInstanceId,HttpSession session) {
		   
		        AjaxJson ajaxJson = new AjaxJson();
		        try{
		        	Command<Integer> cmd = new TaskCmd(processInstanceId);
		        	Integer result= managementService.executeCommand(cmd);
		        	if(result==0){
		        		keepRunTaskDao.delTaskInf(processInstanceId);
		        		ajaxJson.setSuccess(true);
		        		ajaxJson.setMsg("退回成功");
		        	}else{
		        		ajaxJson.setSuccess(false);
		        		ajaxJson.setMsg("流程刚发起不能退回");
		        	}
		        }catch(Exception e){
		        	ajaxJson.setSuccess(false);
	        		ajaxJson.setMsg("此流程不能回退");
		        }
		return ajaxJson;
	}
	
	/**
	 * 记录转办过程
	 * @param processInstanceId
	 * @param taskId
	 * @param assignee
	 * @return
	 */
	@RequestMapping("/getTruntodoInf.json")
	@ResponseBody
	public List<BpmTaskExecution> getTruntodoInf(@RequestParam("processInstanceId") String processInstanceId){
		return bpmTaskExecutionDao.getTruntodoInf(processInstanceId);
	}
	/** 根据用户查询待办任务,EOA WEB电脑端 */
	@RequestMapping("/eoaMyTask.json")
	@ResponseBody
	public GridPageData<TaskDto> eoaMyTask(HttpServletRequest request) {
		String assignee = null;
		Date startCreateTime_t = null;
		Date endCreateTime_t = null;
		String proInstId = null;
		Date startDueTime_t = null;
		Date endDueTime_t = null;
		String processDefId = null;
		String processInstanceName = null;
		String categoryId = null;
		String name = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if(request.getParameter("assignee") != null && !request.getParameter("assignee").trim().equals("")) {
			assignee = request.getParameter("assignee").trim();
		}
		if(request.getParameter("startCreateTime_t") != null && !request.getParameter("startCreateTime_t").trim().equals("")) {
			try {
				startCreateTime_t = sdf.parse(request.getParameter("startCreateTime_t").trim());
			} catch(Exception e) {
				startCreateTime_t = null;
			}
		}
		if(request.getParameter("endCreateTime_t") != null && !request.getParameter("endCreateTime_t").trim().equals("")) {
			try {
				endCreateTime_t = sdf.parse(request.getParameter("endCreateTime_t").trim());
			} catch(Exception e) {
				endCreateTime_t = null;
			}
		}
		if(request.getParameter("proInstId") != null && !request.getParameter("proInstId").trim().equals("")) {
			proInstId = request.getParameter("proInstId").trim();
		}
		if(request.getParameter("startDueTime_t") != null && !request.getParameter("startDueTime_t").trim().equals("")) {
			try {
				startDueTime_t = sdf.parse(request.getParameter("startDueTime_t").trim());
			} catch(Exception e) {
				startDueTime_t = null;
			}
		}
		if(request.getParameter("endDueTime_t") != null && !request.getParameter("endDueTime_t").trim().equals("")) {
			try {
				endDueTime_t = sdf.parse(request.getParameter("endDueTime_t").trim());
			} catch(Exception e) {
				endDueTime_t = null;
			}
		}
		if(request.getParameter("processDefId") != null && !request.getParameter("processDefId").trim().equals("")) {
			processDefId = request.getParameter("processDefId").trim();
		}
		if(request.getParameter("processInstanceName") != null && !request.getParameter("processInstanceName").trim().equals("")) {
			processInstanceName = request.getParameter("processInstanceName").trim();
		}
		if(request.getParameter("categoryId") != null && !request.getParameter("categoryId").trim().equals("")) {
			categoryId = request.getParameter("categoryId").trim();
		}
		if(request.getParameter("name") != null && !request.getParameter("name").trim().equals("")) {
			name = request.getParameter("name").trim();
		}
		
		int page = -1;
		int rows = -1;
		if(request.getParameter("page") != null && !request.getParameter("page").trim().equals("")) {
			try {
				page = Integer.parseInt(request.getParameter("page").trim());
			} catch(Exception e) {
				page = -1;
			}
		} 
		if(request.getParameter("rows") != null && !request.getParameter("rows").trim().equals("")) {
			try {
				rows = Integer.parseInt(request.getParameter("rows").trim());
			} catch(Exception e) {
				rows = -1;
			}
		}
		
		int firstRow = (page - 1) * rows + 1;
		int lastRow = (page - 1) * rows + rows + 1;
		
		List<String> sapProIds = processHelpService.getSapProcessDef();
		QueryMap queryMap = new QueryMap();
		queryMap.put("assignee", assignee);
		queryMap.put("suspensionState", 1);
		queryMap.put("sapProcessIds", sapProIds);
		queryMap.put("processStatus", BpmProcessRun.STATUS_RUN);
		queryMap.put("startCreateTime", startCreateTime_t);
		queryMap.put("endCreateTime", endCreateTime_t);
		queryMap.put("proInstId", proInstId);
		queryMap.put("startDueTime", startDueTime_t);
		queryMap.put("endDueTime", endDueTime_t);
		queryMap.put("processDefId", processDefId);
		queryMap.put("processInstanceName", processInstanceName);
		queryMap.put("categoryId", categoryId);
		queryMap.put("name", name);
		queryMap.put("firstRow", firstRow);
		queryMap.put("lastRow", lastRow);
		
		List<BpmTaskEntity> resuktTasks = activitiHelpService.page4AssigneeTask(queryMap);
		
		Integer total = activitiHelpService.count4AssigneeTask(queryMap);
		
		return new GridPageData<TaskDto>(ProcessConvertUtil.convert2TaskDto(resuktTasks), total.longValue());
	}
	
	//根据职员账号判断是否是经理级
	@RequestMapping("/checkIsManager.json")
	@ResponseBody
	public String checkIsManager(String account){
		//boolean flag=processHelpService.isMiddleManager(account);
		String isManger="1";
		String leader=processHelpService.getOrgLeader(account,"4");
		String manager=processHelpService.getOrgLeader(account,"1");
		String deptDirector=processHelpService.getOrgLeader(account,"2");
		//if(flag){
			if(account.equals(manager)){
				isManger="2";
			}
			else if(account.equals(deptDirector)){
				isManger="3";
			}
		//}else{
			//String leader=processHelpService.getOrgLeader(account,"4");
			else if(("".equals(deptDirector)||deptDirector==null)||((leader==null||"".equals(leader))&&(manager==null||"".equals(manager)))){
				isManger="4";
			}
		//}
		return isManger;
	}
	
	//调用接口判断岗位调整的流程走向
	@RequestMapping("/checkIsOffFather.json")
	@ResponseBody
	public String checkIsOffFather(boolean flag,String empId,String accepterOrgId){
		String outDir=processHelpService.getOrgLeaderByEmpId(empId,"2");
		String outChair=processHelpService.getOrgLeaderByEmpId(empId,"3");
		String inDir=processHelpService.getOtherOrgLeader(accepterOrgId,"2");
		String inChair=processHelpService.getOtherOrgLeader(accepterOrgId,"3");
		if(inDir==null){
			inDir="";
		}
		if(inChair==null){
			inChair="";
		}
		if(outDir==null){
			outDir="";
		}
		if(outChair==null){
			outChair="";
		}
		if(flag){
			if(inDir.equals(outDir)&&inChair.equals(outChair)){
				return "0";
			}else if(!inDir.equals(outDir)&&inChair.equals(outChair)){
				return "1";
			}else if(inDir.equals(outDir)&&!inChair.equals(outChair)){
				return "2";
			}else{
				return "3";
			}
		}else{
			String outManager=processHelpService.getOrgLeaderByEmpId(empId,"1");
			String inManager=processHelpService.getOtherOrgLeader(accepterOrgId,"1");
			if(outManager==null){
				outManager="";
			}
			if(inManager==null){
				inManager="";
			}
			if(inManager.equals(outManager)&&inDir.equals(outDir)&&inChair.equals(outChair)){
				return "4";
			}else if(!inManager.equals(outManager)&&inDir.equals(outDir)&&inChair.equals(outChair)){
				return "5";
			}else if(inManager.equals(outManager)&&!inDir.equals(outDir)&&inChair.equals(outChair)){
				return "6";
			}else if(inManager.equals(outManager)&&inDir.equals(outDir)&&!inChair.equals(outChair)){
				return "7";
			}else if(!inManager.equals(outManager)&&!inDir.equals(outDir)&&inChair.equals(outChair)){
				return "8";
			}else if(!inManager.equals(outManager)&&inDir.equals(outDir)&&!inChair.equals(outChair)){
				return "9";
			}else if(inManager.equals(outManager)&&!inDir.equals(outDir)&&!inChair.equals(outChair)){
				return "10";
			}else{
				return "11";
			}
		}
	}
	//获取申请人的账号
	@RequestMapping("/getApplyAccountByPro.json")
	@ResponseBody
	public String getApplyAccountByPro(String proceId){
		return keepRunTaskDao.getApplyAccountByPro(proceId);
	}
	
	//判断此编号的固定资产是否正在申请报废中
	@RequestMapping("/assetsNoisOffApply.json")
	@ResponseBody
	public boolean assetsNoisOffApply(String assetsNo){
		List<String> strs=activitiHelpService.assetsNoisOffApply();
		boolean flag=false;
		label:for(String str:strs){
			if(!"".equals(str)&&str!=null){
				List<Map<String,Object>> list=(List<Map<String,Object>>)processHelpService.parseJsonToMap(str).get("accessoryData");
				for(Map<String,Object> map:list){
					String assetNo=(String)map.get("assetsNo");
					if(!"".equals(assetNo)&&assetNo.equals(assetsNo)){
						flag=true;
						break label;
					}
				}
			}
		}
		return flag;
	}
	
	//根据实例编号获取凭证编号
	@RequestMapping("/getNoProducProofNo.json")
	@ResponseBody
	public String getNoProducProofNo(String processId){
		return keepRunTaskDao.getProofNoByProcessId(processId);
	}
	/**
	 * 
	 * @param taskId
	 * @return
	 */
	@RequestMapping("/getTaskInfoById.json")
	@ResponseBody
	public AjaxJson taskInfo(String taskId){
		AjaxJson ajaxJson = null;
		try {
			Map<String,Object> attributes = new HashMap<String,Object>();
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			attributes.put("processInstanceId", task.getProcessInstanceId());
			attributes.put("assignee", task.getAssignee());
			attributes.put("taskDefKey", task.getTaskDefinitionKey());
			attributes.put("taskId" , taskId);
			ajaxJson = new AjaxJson(true, "获取信息成功");
			ajaxJson.setAttributes(attributes);
		}catch(Exception e){
			ajaxJson = new AjaxJson(false, "获取信息失败");
		}
		return ajaxJson;
	}
}
