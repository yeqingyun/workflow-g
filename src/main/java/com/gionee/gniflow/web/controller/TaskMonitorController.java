package com.gionee.gniflow.web.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.util.AppContext;
import com.gionee.gniflow.biz.model.AccountHandel;
import com.gionee.gniflow.biz.model.BpmProcessConf;
import com.gionee.gniflow.biz.model.BpmProcessRun;
import com.gionee.gniflow.biz.model.EngineerCollect;
import com.gionee.gniflow.biz.model.InterviewEntity;
import com.gionee.gniflow.biz.model.LaborContractRenewalEntity;
import com.gionee.gniflow.biz.model.Probation;
import com.gionee.gniflow.biz.model.Requirements;
import com.gionee.gniflow.biz.service.ActivitiHelpService;
import com.gionee.gniflow.biz.service.BpmProcessConfService;
import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.dto.TaskDto;
import com.gionee.gniflow.web.bmp.BpmHisProcessInstanceEntity;
import com.gionee.gniflow.web.bmp.BpmTaskEntity;
import com.gionee.gniflow.web.bmp.SendOrderApplyEntity;
import com.gionee.gniflow.web.easyui.AjaxJson;
import com.gionee.gniflow.web.easyui.GridPageData;
import com.gionee.gniflow.web.util.GnifStringUtils;
import com.gionee.gniflow.web.util.ProcessConvertUtil;
import com.gionee.gniflow.web.util.PropertyHolder;

@RequestMapping("/taskmonitor")
@Controller
public class TaskMonitorController {
	@Autowired
	BpmProcessConfService bpmProcessConfService;
	
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
    private CommonsMultipartResolver multipartResolver;
	
	@Autowired
	private ProcessHelpService processHelpService;
	
	@Autowired
	private ActivitiHelpService activitiHelpService;
	
	
	@RequestMapping("/processTask.json")
	@ResponseBody
	public GridPageData<TaskDto> createProcess(QueryMap queryMap) {
		
		List<String> sapProIds = processHelpService.getSapProcessDef();
		
		queryMap.put("suspensionState", 1);
		queryMap.put("sapProcessIds", sapProIds);
		queryMap.put("processStatus", BpmProcessRun.STATUS_RUN);
		
		List<BpmTaskEntity> resuktTasks = activitiHelpService.page4AssigneeTask(queryMap);
		
		Integer total = activitiHelpService.count4AssigneeTask(queryMap);
		
		return new GridPageData<TaskDto>(ProcessConvertUtil.convert2TaskDto(resuktTasks), total.longValue());
	}
	/**
	 * 查询所有报表流程
	 * @param queryMap
	 * @return
	 */
	@RequestMapping("/listProcess.json")
	@ResponseBody
	public GridPageData<BpmHisProcessInstanceEntity> getListProcess(QueryMap queryMap) {
		List<String> sapProDefIds = processHelpService.getSapProcessDef();
		queryMap.put("sapProcessIds", sapProDefIds);
		queryMap.put("userId", AppContext.getCurrentUser().getId());
		String proDefId = (String)queryMap.getMap().get("processDefId");
		//SAP需求申请 L-SAP-Requirement-Application 模组顾问处理
		//SAP数据申请 L-InformationData-Application 业务顾问导出数据
		//SAP账号申请 L-SapAccount-Application 模组顾问填写授权角色
		//SAP帐号权限变更 L-SapAccountModify-Application 模组顾问填写授权角色
		//硬件部派单申请	L-SendOrder-Process 工程师处理
		
		String account=processHelpService.getAccountById(""+AppContext.getCurrentUser().getId());
		String empId=processHelpService.loadEmpByUsrAccount(account);
		String recruiters = PropertyHolder
				.getContextProperty(WebReqConstant.DEPART_RECRUITER_EMPID);
		Map<String,Object> map=processHelpService.parseJsonToMap(recruiters);
		Set<String> empIds=map.keySet();
		if(("L-Interview-Assessment".equals(proDefId)||"L-Personnel-Requirement".equals(proDefId))&&empIds.contains(empId)){
			try {
				List<String> departNames=processHelpService.getThreeDepartByEmpAccount(account);
				queryMap.put("departNames",departNames);
				List<BpmHisProcessInstanceEntity> hisProInstanceRec=activitiHelpService.page4RecruiterUserProcess(queryMap);
				Integer totalRec=activitiHelpService.page4RecruiterUserProcessCount(queryMap);
				return new GridPageData<BpmHisProcessInstanceEntity>(hisProInstanceRec,totalRec.longValue());
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		if("L-SendOrder-Process".equals(proDefId)){
			List<BpmHisProcessInstanceEntity> hisProInstancesEngineer = activitiHelpService.page4InvolvedUserProcessEngineer(queryMap);
			Integer totalEngineer = activitiHelpService.count4InvolvedUserProcessEngineer(queryMap);
			return new GridPageData<BpmHisProcessInstanceEntity>(hisProInstancesEngineer, totalEngineer.longValue());
		}
		if("L-SAP-Requirement-Application".equals(proDefId)){
			List<BpmHisProcessInstanceEntity> hisProInstancesReq = activitiHelpService.page4InvolvedUserProcessReq(queryMap);
			Integer totalReq = activitiHelpService.count4InvolvedUserProcessReq(queryMap);
			return new GridPageData<BpmHisProcessInstanceEntity>(hisProInstancesReq, totalReq.longValue());
		}
		if("L-InformationData-Application".equals(proDefId)){
			List<BpmHisProcessInstanceEntity> hisProInstancesInfo = activitiHelpService.page4InvolvedUserProcessInfo(queryMap);
			Integer totalInfo = activitiHelpService.count4InvolvedUserProcessInfo(queryMap);
			return new GridPageData<BpmHisProcessInstanceEntity>(hisProInstancesInfo, totalInfo.longValue());
		}
		List<BpmHisProcessInstanceEntity> hisProInstances = activitiHelpService.page4InvolvedUserProcess(queryMap);
		
		//设置流程开始到现在的耗时
		for (BpmHisProcessInstanceEntity entity : hisProInstances) {
			entity.setStartUserName(entity.getStartUserName() );//修改流程发起人编号姓名为发起人姓名	
		}
		
		Integer total = activitiHelpService.count4InvolvedUserProcess(queryMap);

		return new GridPageData<BpmHisProcessInstanceEntity>(hisProInstances, total.longValue());
	}
	
	/**
	 * 查询所有相关流程（流程监控）
	 * @param queryMap
	 * @return
	 */
	@RequestMapping("/allProcess.json")
	@ResponseBody
	public GridPageData<BpmHisProcessInstanceEntity> getAllProcess(QueryMap queryMap) {
		List<String> sapProDefIds = processHelpService.getSapProcessDef();
		
		queryMap.put("sapProcessIds", sapProDefIds);
		queryMap.put("userId", AppContext.getCurrentUser().getId());
		List<BpmHisProcessInstanceEntity> hisProInstances = activitiHelpService.page4InvolvedUserProcess(queryMap);
		Integer total = activitiHelpService.count4InvolvedUserProcess(queryMap);
		return new GridPageData<BpmHisProcessInstanceEntity>(hisProInstances, total.longValue());
	}
	
	/**
	 * 导出流程报表
	 * @param queryMap
	 * @param request
	 * @param response
	 */
    @RequestMapping("/exportProcessInfo.html")
	@ResponseBody
	public void exportCompletedProcessInfo(QueryMap queryMap,HttpServletRequest request,HttpServletResponse response){
	    List<String> sapProDefIds = processHelpService.getSapProcessDef();
		queryMap.put("sapProcessIds", sapProDefIds);
		queryMap.put("lastRow", 1000000);
	    String proDefId = (String)queryMap.getMap().get("processDefId");
		List<BpmHisProcessInstanceEntity> results=null;
	    if("L-SendOrder-Process".equals(proDefId)){
	    	results = activitiHelpService.page4InvolvedUserProcessEngineer(queryMap);
		}else if("L-SAP-Requirement-Application".equals(proDefId)){
			results = activitiHelpService.page4InvolvedUserProcessReq(queryMap);
		}else if("L-InformationData-Application".equals(proDefId)){
			results = activitiHelpService.page4InvolvedUserProcessInfo(queryMap);
		}else{
			 results = activitiHelpService.page4InvolvedUserProcess(queryMap);
		}
		try {
	    	if (!CollectionUtils.isEmpty(results)) {
	    		toProcessInfoExcel(response, results);
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
	/**
	 * 将列表数据转成excel    
	 * @param response
	 * @param results
	 * @throws Exception
	 */
    void toProcessInfoExcel(HttpServletResponse response, List<BpmHisProcessInstanceEntity> results) throws Exception{
    	// 获取总列数
        // 创建Excel文档
        HSSFWorkbook hwb = new HSSFWorkbook();
        // sheet 对应一个工作页
        HSSFSheet sheet = hwb.createSheet("流程明细表");
        HSSFRow firstrow = sheet.createRow(0); // 下标为0的行开始
        //流程实例编号		发起人编号		发起人姓名			离职人员编号			离职人员姓名	离职人员部门	离职操作类型	 			离职日期
        //procInstId	applyUserId	applyUserName	applyUserAccount	userName	orgName	 	operationReasonText		leaveDate
        
        //流程实例编号   			实例名称   					流程名称   				流程分类   
        //processInstanceId		processInstanceName		processDefName		categoryName
        //创建时间   		负责人   			截止今日处理耗时(小时)   状态
        //startTime		startUserId		durationInMillis	processStatus
        String[] names = new String[]{"流程实例编号","实例名称","流程名称","流程分类 ","创建时间 ","结束时间 ","负责人帐号","负责人姓名",/*"处理耗时(小时) ",*/"状态"};
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
        	if(bpie!=null){
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
            if(bpie.getStartTime()!=null){
            	cell.setCellValue(df.format((Date)bpie.getStartTime()));	
            }else{
            	cell.setCellValue("");
            }
           	cell=row.createCell(5);
           	if(bpie.getEndTime()!=null){
           		cell.setCellValue(df.format((Date)bpie.getEndTime()));	
            }else{
            	cell.setCellValue("");
            }
            cell=row.createCell(6);
            cell.setCellValue(bpie.getStartUserId());
            cell=row.createCell(7);
            cell.setCellValue(processHelpService.getUserName(bpie.getStartUserId()));
            cell=row.createCell(8);
            int status = 0;
            if(bpie.getProcessStatus()!=null){
            	status = bpie.getProcessStatus();
            }
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
        }
        // 创建文件输出流，准备输出电子表格
        response.setContentType("application/vnd.ms-excel");   
        String encodedFileName = "completedProcessInfoExcel.xls" ; 
		response.setHeader("Content-disposition", "attachment;filename=" + encodedFileName);
		OutputStream out = response.getOutputStream();
        hwb.write(out);
        out.flush();
        out.close();
    }
    
    //查询派单申请记录
    @SuppressWarnings("unused")
	@RequestMapping("/getAllSendOrderProcess.json")
    @ResponseBody
    public GridPageData<SendOrderApplyEntity> getAllSendOrderProcess(QueryMap queryMap,HttpServletResponse response){
    	//查看权限判断
    	String userId=AppContext.getCurrentUser().getId().toString();
    	String authority=bpmProcessConfService.getAuthority("派单流程");
    	boolean flag=false;
    	if(authority !=null && authority !=""){
    		String[] arry=authority.split(",");
    		for(int i=0;i<arry.length;i++){
    			if(userId.equals(arry[i])){
    				flag=true;
    			}
    		}
    	}else{
    		flag=true;
    	}
    	if(!flag){
    		try {
				response.getWriter().println("你没权限查看派单流程");
				return null;
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	
    	List<String> sapProDefIds = processHelpService.getSapProcessDef();
		queryMap.put("sapProcessIds", sapProDefIds);
		List<SendOrderApplyEntity> sendOrderProcess=activitiHelpService.page4SendOrderProcess(queryMap);
		Integer sendCount=activitiHelpService.count4SendOrderProcess(queryMap);
		return new GridPageData<SendOrderApplyEntity>(sendOrderProcess,sendCount.longValue());
    	
    }

    //查询面试评审记录
    @RequestMapping("/getInterviewInfo.json")
    @ResponseBody
    public GridPageData<InterviewEntity> getInterviewInfo(QueryMap queryMap){
    	String account=processHelpService.getAccountById(""+AppContext.getCurrentUser().getId());
		String empId=processHelpService.loadEmpByUsrAccount(account);
		String recruiters = PropertyHolder
				.getContextProperty(WebReqConstant.DEPART_RECRUITER_EMPID);
		Map<String,Object> map=processHelpService.parseJsonToMap(recruiters);
		Set<String> empIds=map.keySet();
		if(empIds.contains(empId)){
			List<String> departNames=processHelpService.getThreeDepartByEmpAccount(account);
			queryMap.put("departNames",departNames);
		}
		
    	List<InterviewEntity> interviewInfo=activitiHelpService.getInterviewInfo(queryMap);
    	Integer interviewCount=activitiHelpService.getInterviewCount(queryMap);
    	return new GridPageData<InterviewEntity>(interviewInfo,interviewCount.longValue());
    }
    
    //导出面试评审数据
    @RequestMapping("/exportInterviewInfo.html")
    public void exportInterviewInfo(QueryMap queryMap,HttpServletResponse response,HttpServletRequest request){
    	queryMap.put("lastRow",1000000);
    	String account = AppContext.getCurrentAppUser().getAccount();
		String empId=processHelpService.loadEmpByUsrAccount(account);
		String recruiters = PropertyHolder
				.getContextProperty(WebReqConstant.DEPART_RECRUITER_EMPID);
		Map<String,Object> map=processHelpService.parseJsonToMap(recruiters);
		Set<String> empIds=map.keySet();
		if(empIds.contains(empId)){
			List<String> departNames=processHelpService.getThreeDepartByEmpAccount(account);
			queryMap.put("departNames",departNames);
		}
    	
    	List<InterviewEntity> interviews=activitiHelpService.getInterviewInfo(queryMap);
    	try{
	    	if(!CollectionUtils.isEmpty(interviews)){
	    		activitiHelpService.toInterviewInfoExcel(interviews, response,request);
	    	}else{
	    		response.setContentType("text/plain; charset=UTF-8");
				try {
					response.getWriter().println("数据不存在,导出Excel失败!");
				} catch (IOException e) {
					e.printStackTrace();
				}
	    	}
      }catch(Exception e){
    	  e.printStackTrace();
          response.setContentType("text/plain; charset=UTF-8");
		try {
			response.getWriter().println("导出Excel文件失败!");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
      }
    }
    //派单汇总查询
    @RequestMapping("/getMaintainCollect.json")
    @ResponseBody
    public GridPageData<EngineerCollect> getMaintainCollect(QueryMap queryMap){
    	List<String> sapProDefIds = processHelpService.getSapProcessDef();
		queryMap.put("sapProcessIds", sapProDefIds);
    	List<EngineerCollect> collects=activitiHelpService.getMaintainCollect(queryMap);
    	return new GridPageData<EngineerCollect>(collects,new Integer(collects.size()).longValue());
    }
    //导出派单汇总数据
    @RequestMapping("/exportMaintainCollect.html")
    public void exportMaintainCollect(QueryMap queryMap,HttpServletResponse response,HttpServletRequest request){
    	List<String> sapProDefIds = processHelpService.getSapProcessDef();
		queryMap.put("sapProcessIds", sapProDefIds);
		queryMap.put("lastRow",1000000);
    	List<EngineerCollect> collects=activitiHelpService.getMaintainCollect(queryMap);
    	try{
	    	if(!CollectionUtils.isEmpty(collects)){
	    		activitiHelpService.toMaintainCollectExcel(collects, response,request);
	    	}else{
	    		response.setContentType("text/plain; charset=UTF-8");
				try {
					response.getWriter().println("数据不存在,导出Excel失败!");
				} catch (IOException e) {
					e.printStackTrace();
				}
	    	}
      }catch(Exception e){
    	  e.printStackTrace();
          response.setContentType("text/plain; charset=UTF-8");
		try {
			response.getWriter().println("导出Excel文件失败!");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
      }
    }
    //导出派单申请详细数据
    @RequestMapping("/exportSendOrderDetail.html")
    public void exportSendOrderDetail(QueryMap queryMap,HttpServletResponse response,HttpServletRequest request){
    	List<String> sapProDefIds = processHelpService.getSapProcessDef();
		queryMap.put("sapProcessIds", sapProDefIds);
		queryMap.put("lastRow",1000000);
		List<SendOrderApplyEntity> sendOrderProcess=activitiHelpService.page4SendOrderProcess(queryMap);
		try{
	    	if(!CollectionUtils.isEmpty(sendOrderProcess)){
	    		activitiHelpService.toSendOrderDetailExcel(sendOrderProcess, response,request);
	    	}else{
	    		response.setContentType("text/plain; charset=UTF-8");
				try {
					response.getWriter().println("数据不存在,导出Excel失败!");
				} catch (IOException e) {
					e.printStackTrace();
				}
	    	}
      }catch(Exception e){
    	  e.printStackTrace();
          response.setContentType("text/plain; charset=UTF-8");
		try {
			response.getWriter().println("导出Excel文件失败!");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
      }
    }
    
    //试用期转正查询
    @RequestMapping("/getIprobationEation.json")
    @ResponseBody
    public GridPageData<Probation> getIprobationEation(QueryMap queryMap){
    	List<Probation> probations=activitiHelpService.getIprobationEation(queryMap);
    	Integer count=activitiHelpService.getProbationCount(queryMap);
    	return new GridPageData<Probation>(probations,new Integer(count).longValue());
    }
    //试用期转正查询
    @RequestMapping("/getIprobationEation4Hr.json")
    @ResponseBody
    public GridPageData<Probation> getIprobationEation4Hr(QueryMap queryMap){
    	List<Probation> probations=activitiHelpService.getIprobationEation4Hr(queryMap);
    	Integer count=activitiHelpService.getProbationCount(queryMap);
    	return new GridPageData<Probation>(probations,new Integer(count).longValue());
    }
    //导出试用期报表
    @RequestMapping("/exportIprobationEation.html")
    public void exportIprobationEation(QueryMap queryMap,HttpServletResponse response,HttpServletRequest request){
		queryMap.put("lastRow",1000000);
		List<Probation> probations=activitiHelpService.getIprobationEation(queryMap);
		try{
	    	if(!CollectionUtils.isEmpty(probations)){
	    		activitiHelpService.toIprobationEationExcel(probations, response,request);
	    	}else{
	    		response.setContentType("text/plain; charset=UTF-8");
				try {
					response.getWriter().println("数据不存在,导出Excel失败!");
				} catch (IOException e) {
					e.printStackTrace();
				}
	    	}
      }catch(Exception e){
    	  e.printStackTrace();
          response.setContentType("text/plain; charset=UTF-8");
		try {
			response.getWriter().println("导出Excel文件失败!");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
      }
    }
    //导出试用期报表
    @RequestMapping("/exportIprobationEation4Hr.html")
    public void exportIprobationEation4Hr(QueryMap queryMap,HttpServletResponse response,HttpServletRequest request){
		queryMap.put("lastRow",1000000);
		List<Probation> probations=activitiHelpService.getIprobationEation4Hr(queryMap);
		try{
	    	if(!CollectionUtils.isEmpty(probations)){
	    		activitiHelpService.toIprobationEationExcel4Hr(probations, response,request);
	    	}else{
	    		response.setContentType("text/plain; charset=UTF-8");
				try {
					response.getWriter().println("数据不存在,导出Excel失败!");
				} catch (IOException e) {
					e.printStackTrace();
				}
	    	}
      }catch(Exception e){
    	  e.printStackTrace();
          response.setContentType("text/plain; charset=UTF-8");
		try {
			response.getWriter().println("导出Excel文件失败!");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
      }
    }
    
    //人员需求查询
    @RequestMapping("/getRequirementReport.json")
    @ResponseBody
    public GridPageData<Requirements> getRequirementReport(QueryMap queryMap){
    	String account = AppContext.getCurrentAppUser().getAccount();
		String empId=processHelpService.loadEmpByUsrAccount(account);
		String recruiters = PropertyHolder.getContextProperty(WebReqConstant.DEPART_RECRUITER_EMPID);
		Map<String,Object> map=processHelpService.parseJsonToMap(recruiters);
    	Set<String> empIds=map.keySet();
		if(empIds.contains(empId)){
			List<String> departNames=processHelpService.getThreeDepartByEmpAccount(account);
			queryMap.put("departNames",departNames);
		}
    	List<Requirements> requirements=activitiHelpService.getRequirementReport(queryMap);
    	Integer count=activitiHelpService.getRequirementReportCount(queryMap);
    	return new GridPageData<Requirements>(requirements,new Integer(count).longValue());
    }
    //导出人员需求数据
    @RequestMapping("/exportRequirementReportInfo.html")
    public void exportRequirementReportInfo(QueryMap queryMap,HttpServletResponse response,HttpServletRequest request){
    	queryMap.put("lastRow",1000000);
    	String account = AppContext.getCurrentAppUser().getAccount();
		String empId=processHelpService.loadEmpByUsrAccount(account);
		String recruiters = PropertyHolder
				.getContextProperty(WebReqConstant.DEPART_RECRUITER_EMPID);
		Map<String,Object> map=processHelpService.parseJsonToMap(recruiters);
		Set<String> empIds=map.keySet();
		if(empIds.contains(empId)){
			List<String> departNames=processHelpService.getThreeDepartByEmpAccount(account);
			queryMap.put("departNames",departNames);
		}
    	
    	List<Requirements> requirements = activitiHelpService.getRequirementReport(queryMap);
    	try{
	    	if(!CollectionUtils.isEmpty(requirements)){
	    		activitiHelpService.toRequirementReportExcel(requirements, response,request);
	    	}else{
	    		response.setContentType("text/plain; charset=UTF-8");
				try {
					response.getWriter().println("数据不存在,导出Excel失败!");
				} catch (IOException e) {
					e.printStackTrace();
				}
	    	}
      }catch(Exception e){
    	  e.printStackTrace();
          response.setContentType("text/plain; charset=UTF-8");
		try {
			response.getWriter().println("导出Excel文件失败!");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
      }
    }
  //户口申请报表查询
    @RequestMapping("/getAllAccountHandelProcess.json")
    @ResponseBody
    public GridPageData<AccountHandel> getAllAccountHandelProcess(QueryMap queryMap){
    	
    	List<AccountHandel> requirements=activitiHelpService.getAllAccountHandelProcess(queryMap);
    	Integer count=activitiHelpService.getAllAccountHandelProcessCount(queryMap);
    	return new GridPageData<AccountHandel>(requirements,new Integer(count).longValue());
    }
    //导出户口报表
    @RequestMapping("/exportAccountHandel.html")
    public void exportAccountHandel(QueryMap queryMap,HttpServletResponse response,HttpServletRequest request){
		queryMap.put("lastRow",1000000);
		List<AccountHandel> accountHandel=activitiHelpService.getAllAccountHandelProcess(queryMap);
		try{
	    	if(!CollectionUtils.isEmpty(accountHandel)){
	    		activitiHelpService.toAccountHandelExcel(accountHandel, response,request);
	    	}else{
	    		response.setContentType("text/plain; charset=UTF-8");
				try {
					response.getWriter().println("数据不存在,导出Excel失败!");
				} catch (IOException e) {
					e.printStackTrace();
				}
	    	}
      }catch(Exception e){
    	  e.printStackTrace();
          response.setContentType("text/plain; charset=UTF-8");
		try {
			response.getWriter().println("导出Excel文件失败!");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
      }
    }
   //获取三层部门名称
    @RequestMapping("/getOrgName.josn")
    @ResponseBody
    public AjaxJson getOrgName(String orgId){
    	  AjaxJson ajaxJson=new AjaxJson();
    	   String orgName=processHelpService.getThreeDepartByOrgId(orgId);
    	   if(StringUtils.isNotEmpty(orgName)){
    		   ajaxJson.setMsg(orgName); 
    		   ajaxJson.setSuccess(true);
    	   }
    	return ajaxJson;
    }
    
    
    /**
     * 劳动合同续签数据
     * @param queryMap
     * @return
     */
    @RequestMapping("/getlaborContractRenewals.json")
    @ResponseBody
    public GridPageData<LaborContractRenewalEntity> getlaborContractRenewals(QueryMap queryMap){
    	List<LaborContractRenewalEntity> interviewInfo=activitiHelpService.getLaborContractRenewals(queryMap);
    	Integer interviewCount=activitiHelpService.getLaborContractRenewalsCount(queryMap);
    	return new GridPageData<LaborContractRenewalEntity>(interviewInfo,interviewCount.longValue());
    }
    
    /**
     * 导出劳动合同续签申请
     * @param queryMap
     * @param response
     */
    @RequestMapping("/exportlaborContractRenewalReport.html")
    public void exportlaborContractRenewalReport(QueryMap queryMap,HttpServletRequest request,HttpServletResponse response){
		queryMap.put("lastRow",1000000);
    	if(queryMap.getMap().get("processInstanceIds") != null){
    		String processInstanceIds = queryMap.getMap().get("processInstanceIds").toString();
    		queryMap.put("processInstanceIds", processInstanceIds.split(","));
    	}
		List<LaborContractRenewalEntity> airlineTicketApplications=activitiHelpService. getLaborContractRenewals(queryMap) ;
		try{
	    	if(!CollectionUtils.isEmpty(airlineTicketApplications)){
	    		activitiHelpService.toLaborContractRenewalExcel(airlineTicketApplications,request, response);
	    	}else{
	    		response.setContentType("text/plain; charset=UTF-8");
				try {
					response.getWriter().println("数据不存在,导出Excel失败!");
				} catch (IOException e) {
					e.printStackTrace();
				}
	    	}
      }catch(Exception e){
    	  e.printStackTrace();
          response.setContentType("text/plain; charset=UTF-8");
		try {
			response.getWriter().println("导出Excel文件失败!");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
      }
    }
    /**
     * 发送劳动合同续签提醒邮件
     * @param queryMap
     * @param request
     * @param response
     */
    @RequestMapping("/sendLaborContractRenewalEmail.html")
    @ResponseBody
    public AjaxJson sendLaborContractRenewalEmail(QueryMap queryMap,HttpServletRequest request,HttpServletResponse response){
		AjaxJson ajaxJson = null;
		QueryMap queryMap1 = new QueryMap();
		queryMap1.put("processDefKey", "L-Labor-Contract-Renewal-Flow");
		List<BpmProcessConf> bpmProConfList = bpmProcessConfService.query(queryMap1);
		if (!CollectionUtils.isEmpty(bpmProConfList)) {
			BpmProcessConf curProConf = bpmProConfList.get(0);
			String permissionCrowd = curProConf.getPermissionCrowd();
			
			if (StringUtils.isNotEmpty(permissionCrowd) && !(GnifStringUtils.inArr(AppContext.getCurrentAccount(),permissionCrowd.split(",")))){
				ajaxJson = new AjaxJson(false, "您没有权限发送邮件!");
				return ajaxJson;
			} 
		}
    	if(queryMap.getMap().get("processInstanceIds") != null && queryMap.getMap().get("renewalDate") != null){
    		String processInstanceIds = queryMap.getMap().get("processInstanceIds").toString();
    		queryMap.put("processInstanceIds", processInstanceIds.split(","));
    		queryMap.put("approvalOpinion", "1");
    		List<LaborContractRenewalEntity> laborContractRenewals=activitiHelpService. getLaborContractRenewals(queryMap) ;
    		if(laborContractRenewals != null && laborContractRenewals.size()>0){
    			for(LaborContractRenewalEntity laborContractRenewal :laborContractRenewals){
    				String account = laborContractRenewal.getPrincipalAccount();
    				String address = processHelpService.getUserEmailAddress(account);
    				ajaxJson = activitiHelpService.sendLCRReminderEmail(laborContractRenewal, address,  queryMap.getMap().get("renewalDate").toString());
    				if(!ajaxJson.isSuccess()){
    					return ajaxJson;
    				}
    			}
    		}
    		ajaxJson = activitiHelpService.sendLCRSureEmail(laborContractRenewals,queryMap.getMap().get("renewalDate").toString());
    	}else{
    		ajaxJson = new AjaxJson(false,"参数传递错误!");
    	}
		return ajaxJson;
    }
}
