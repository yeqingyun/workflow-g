/**
 * @(#) ActivitiHelpServiceImpl.java Created on 2014年7月28日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.biz.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.file.util.CalcUtil;
import com.gionee.gnif.file.web.message.Message;
import com.gionee.gnif.model.AppUser;
import com.gionee.gnif.util.AppContext;
import com.gionee.gniflow.biz.model.AccountHandel;
import com.gionee.gniflow.biz.model.BpmEmailTemplate;
import com.gionee.gniflow.biz.model.BpmProcessRun;
import com.gionee.gniflow.biz.model.BpmTaskExecution;
import com.gionee.gniflow.biz.model.EngineerCollect;
import com.gionee.gniflow.biz.model.EoaTaskInfoEntity;
import com.gionee.gniflow.biz.model.InterviewEntity;
import com.gionee.gniflow.biz.model.LaborContractRenewalEntity;
import com.gionee.gniflow.biz.model.LeaveProcessInstanceEntity;
import com.gionee.gniflow.biz.model.Probation;
import com.gionee.gniflow.biz.model.Requirements;
import com.gionee.gniflow.biz.service.ActivitiHelpService;
import com.gionee.gniflow.biz.service.BpmEmailTemplateService;
import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.dto.HrUserDto;
import com.gionee.gniflow.dto.ProcessDefinitionDto;
import com.gionee.gniflow.integration.dao.ActivitiHelpDao;
import com.gionee.gniflow.integration.dao.BpmTaskExecutionDao;
import com.gionee.gniflow.web.bmp.BpmHisActivityEntity;
import com.gionee.gniflow.web.bmp.BpmHisProcessInstanceEntity;
import com.gionee.gniflow.web.bmp.BpmHisTaskEntity;
import com.gionee.gniflow.web.bmp.BpmIdentityLinkEntity;
import com.gionee.gniflow.web.bmp.BpmTaskEntity;
import com.gionee.gniflow.web.bmp.SendOrderApplyEntity;
import com.gionee.gniflow.web.easyui.AjaxJson;
import com.gionee.gniflow.web.util.MimeUtil;
import com.gionee.gniflow.web.util.PoiUtil;
import com.gionee.gniflow.web.util.PropertyHolder;
import com.gionee.gniflow.web.util.SendMailTool;
import com.gionee.gniflow.web.util.SpringTools;
import com.gionee.oss.api.client.impl.OssClient;
import com.gionee.oss.api.constant.DownloadParameter;
import com.gionee.oss.api.util.EncryptUtil;

/**
 * The class <code>ActivitiHelpServiceImpl</code>
 *
 * @author lipw
 * @version 1.0
 */
@Service
public class ActivitiHelpServiceImpl implements ActivitiHelpService {
	
	@Autowired
	private ActivitiHelpDao activitiHelpDao;
	
	@Autowired
	private BpmTaskExecutionDao bpmTaskExecutionDao;
	
	@Autowired
	BpmEmailTemplateService bpmEmailTemplateService;
	
	@Autowired
	BpmServiceImpl bpmServiceImpl;
	
	@Override
	public String findActHiActinstId(String taskId) {
		return activitiHelpDao.findActHiActinstId(taskId);
	}

	@Override
	public String findActHiActinstTaskId(String id) {
		return activitiHelpDao.findActHiActinstTaskId(id);
	}

	@Override
	public void deleteActHiActinst4Id(String id) {
		activitiHelpDao.deleteActHiActinst4Id(id);
	}

	@Override
	public List<ProcessDefinitionDto> page4ProcessDefinition4Page(
			QueryMap queryMap) {
		return activitiHelpDao.page4ProcessDefinition4Page(queryMap.getMap());
	}

	@Override
	public Integer count4ProcessDefinition(QueryMap queryMap) {
		return activitiHelpDao.count4ProcessDefinition(queryMap.getMap());
	}

	@Override
	public void updateProCategory(String processDefId, Integer categoryId) {
		activitiHelpDao.updateProCategory(processDefId, categoryId);
	}

	@Override
	public List<BpmHisActivityEntity> loadBpmHisActivity(QueryMap queryMap) {
		return activitiHelpDao.loadBpmHisActivity(queryMap.getMap());
	}

	@Override
	public void updateStartAssignee(String actId, String procInstId,
			String assignee) {
		activitiHelpDao.updateStartAssignee(actId, procInstId, assignee);
	}

	@Override
	public List<BpmTaskEntity> page4EngineerTask(QueryMap queryMap) {
		return activitiHelpDao.page4EngineerTask(queryMap.getMap());
	}

	@Override
	public Integer count4EngineerTask(QueryMap queryMap) {
		return activitiHelpDao.count4EngineerTask(queryMap.getMap());
	}

	@Override
	public List<BpmTaskEntity> page4AssigneeTask(QueryMap queryMap) {
		return activitiHelpDao.page4AssigneeTask(queryMap.getMap());
	}

	@Override
	public Integer count4AssigneeTask(QueryMap queryMap) {
		return activitiHelpDao.count4AssigneeTask(queryMap.getMap());
	}

	@Override
	public List<BpmHisTaskEntity> page4CompletedTask(QueryMap queryMap) {
		return activitiHelpDao.page4CompletedTask(queryMap.getMap());
	}

	@Override
	public Integer count4CompletedTask(QueryMap queryMap) {
		return activitiHelpDao.count4CompletedTask(queryMap.getMap());
	}
	@Override
	public List<EoaTaskInfoEntity> page4AssigneeAllTask(QueryMap queryMap) {
		// TODO Auto-generated method stub
		return activitiHelpDao.page4AssigneeAllTask(queryMap.getMap());
	}

	@Override
	public Integer count4AssigneeAllTask(QueryMap queryMap) {
		// TODO Auto-generated method stub
		return activitiHelpDao.count4AssigneeAllTask(queryMap.getMap());
	}
	@Override
	public List<BpmHisProcessInstanceEntity> page4StartProcess(QueryMap queryMap) {
		return activitiHelpDao.page4StartProcess(queryMap.getMap());
	}

	@Override
	public Integer count4StartProcess(QueryMap queryMap) {
		return activitiHelpDao.count4StartProcess(queryMap.getMap());
	}

	@Override
	public List<BpmHisProcessInstanceEntity> page4InvolvedUserProcess(
			QueryMap queryMap) {
		return activitiHelpDao.page4InvolvedUserProcess(queryMap.getMap());
	}

	@Override
	public Integer count4InvolvedUserProcess(QueryMap queryMap) {
		return activitiHelpDao.count4InvolvedUserProcess(queryMap.getMap());
	}

	@Override
	public List<BpmHisProcessInstanceEntity> page4InvolvedUserProcessReq(
			QueryMap queryMap) {
		return activitiHelpDao.page4InvolvedUserProcessReq(queryMap.getMap());
	}

	@Override
	public Integer count4InvolvedUserProcessReq(QueryMap queryMap) {
		return activitiHelpDao.count4InvolvedUserProcessReq(queryMap.getMap());
	}
	@Override
	public List<BpmHisProcessInstanceEntity> page4InvolvedUserProcessEngineer(
			QueryMap queryMap) {
		return activitiHelpDao.page4InvolvedUserProcessEngineer(queryMap.getMap());
	}

	@Override
	public Integer count4InvolvedUserProcessEngineer(QueryMap queryMap) {
		return activitiHelpDao.count4InvolvedUserProcessEngineer(queryMap.getMap());
	}
	
	
	@Override
	public List<BpmHisProcessInstanceEntity> page4InvolvedUserProcessInfo(
			QueryMap queryMap) {
		return activitiHelpDao.page4InvolvedUserProcessInfo(queryMap.getMap());
	}

	@Override
	public Integer count4InvolvedUserProcessInfo(QueryMap queryMap) {
		return activitiHelpDao.count4InvolvedUserProcessInfo(queryMap.getMap());
	}
	
	
	@Override
	public List<BpmIdentityLinkEntity> queryTaskCandidates(QueryMap queryMap) {
		return activitiHelpDao.queryTaskCandidates(queryMap.getMap());
	}

	@Override
	public BpmHisActivityEntity queryHisActivity(String id) {
		return activitiHelpDao.queryHisActivity(id);
	}

	@Override
	public List<HistoricActivityInstance> queryTrackHistoricActivityInstance(
			String processInstanceId) {
		return activitiHelpDao.queryTrackHistoricActivityInstance(processInstanceId);
	}

	@Override
	public HistoricActivityInstance queryPreviousNotInclusivegatewayHisActInst4CurNode(String procInstId, String executionId, String actType){
		return activitiHelpDao.queryPreviousNotInclusivegatewayHisActInst4CurNode(procInstId, executionId, actType);
	}
	
	@Override
	public String queryInclusivegatewayHisActInstMaxId(String instanceId,String lastId){
		return activitiHelpDao.queryInclusivegatewayHisActInstMaxId(instanceId, lastId);
	}
	
	@Override
	public String queryInclusivegatewayHisActInstMinId(String instanceId,String maxId){
		return activitiHelpDao.queryInclusivegatewayHisActInstMinId(instanceId, maxId);
	}
	
	@Override
	public List<HistoricActivityInstance> queryInclusivegatewayBetNode(String instanceId,String minId,String maxId){
		return activitiHelpDao.queryInclusivegatewayBetNode(instanceId, minId, maxId);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void updateHisInclusivegatewayEndTime(String procInstId,
			String executionId, Date endTime) {
		activitiHelpDao.updateHisInclusivegatewayEndTime(procInstId,executionId,endTime);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public List<HistoricActivityInstance> queryHisActInst4NewTransaction(
			String procInstId) {
		return activitiHelpDao.queryHisActInst4NewTransaction(procInstId);
	}

	@Override
	public void updateTaskAssignee(BpmTaskExecution taskExecution) {
		//执行跟新操作
		activitiHelpDao.updateTaskAssignee(taskExecution.getTaskId(), taskExecution.getAssignee());
		//执行更新历史操作
		activitiHelpDao.updateHisTaskAssignee(taskExecution.getTaskId(), taskExecution.getAssignee());
		//执行更新ACT_HI_TASKINST表
		activitiHelpDao.updateHisTaskInstAssignee(taskExecution.getTaskId(), taskExecution.getAssignee());
		//记录日志
		bpmTaskExecutionDao.insert(taskExecution);
	}

	@Override
	public List<ProcessDefinition> queryLastVersionAndCategoryBySort(
			String category) {
		return activitiHelpDao.queryLastVersionAndCategoryBySort(category);
	}

	@Override
	public void deleteActRuVariable4ProInstId(String procInstId) {
		activitiHelpDao.deleteActRuVariable4ProInstId(procInstId);
	}
	
	@Override
	public List<Map<String,Object>> getNoFinishTask(String code,Integer beginDays,Integer endDays) {
		return activitiHelpDao.getNoFinishTask(code,beginDays,endDays);
	}

	@Override
	public List<SendOrderApplyEntity> page4SendOrderProcess(QueryMap map) {
		List<SendOrderApplyEntity> sendOrderProcess=activitiHelpDao.page4SendOrderProcess(map.getMap());
		for(SendOrderApplyEntity sendOrderEntity:sendOrderProcess){
			double hour=(sendOrderEntity.getEndTime().getTime()-sendOrderEntity.getStartTime().getTime())/1000/60/60.00;
			//BigDecimal bg = new BigDecimal(hour);
			DecimalFormat fnum = new DecimalFormat("##0.00");
			//sendOrderEntity.setElapsedTime(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			sendOrderEntity.setElapsedTime(fnum.format(hour));
			String rate=Integer.parseInt(sendOrderEntity.getSatisfaction())*100/10+"%";
			sendOrderEntity.setSatisfactionRate(rate);
		}
		return sendOrderProcess;
	}

	@Override
	public Integer count4SendOrderProcess(QueryMap map) {
		return activitiHelpDao.count4SendOrderProcess(map.getMap());
	}

	@Override
	public List<InterviewEntity> getInterviewInfo(QueryMap map) {
		List<InterviewEntity> interviews=activitiHelpDao.getInterviewInfo(map.getMap());
		/*for(InterviewEntity interview:interviews){
			String time=interview.getDeptRetestAudiTime();
			interview.setDeptRetestAudiTime(time.substring(0,10));
			if(interview.getFirstInOrgName().indexOf("有限公司")>0){
				interview.setFirstInOrgName(interview.getSecondInOrgName());
				interview.setSecondInOrgName(interview.getInOrgName());
				interview.setInOrgName("");
			}
		}*/
		return interviews;
	}

	@Override
	public Integer getInterviewCount(QueryMap map) {
		return activitiHelpDao.getInterviewCount(map.getMap());
	}

	@Override
	public void toInterviewInfoExcel(List<InterviewEntity> interviews, HttpServletResponse response,HttpServletRequest request) throws IOException{
		// 获取总列数
        // 创建Excel文档
        HSSFWorkbook hwb = new HSSFWorkbook();
        // sheet 对应一个工作页
        HSSFSheet sheet = hwb.createSheet("录用报表");
        HSSFRow firstrow = sheet.createRow(0); // 下标为0的行开始
        String[] names = new String[]{"流程编号","申请日期","姓名","系统","部门","职级","职位","年龄","学历","专业","工作年限","HR总体评价","部门总体评语","新员工导师姓名","薪级","试用期薪资","转正后薪资","流程状态"};
        
        //设置excel宽度
        int flag = 0;
        sheet.setColumnWidth(flag++, 20 * 255);
        sheet.setColumnWidth(flag++, 10 * 255);
        sheet.setColumnWidth(flag++, 15 * 255);
        sheet.setColumnWidth(flag++, 15 * 255);
        sheet.setColumnWidth(flag++, 10 * 255);
        sheet.setColumnWidth(flag++, 15 * 255);
        sheet.setColumnWidth(flag++, 10 * 255);
        sheet.setColumnWidth(flag++, 10 * 255);
        sheet.setColumnWidth(flag++, 15 * 255);
        sheet.setColumnWidth(flag++, 10 * 255);
        sheet.setColumnWidth(flag++, 10 * 255);
        sheet.setColumnWidth(flag++, 10 * 255);
        sheet.setColumnWidth(flag++, 10 * 255);
        sheet.setColumnWidth(flag++, 10 * 255);
        sheet.setColumnWidth(flag++, 10 * 255);
        sheet.setColumnWidth(flag++, 10 * 255);
        sheet.setColumnWidth(flag++, 10 * 255);
        sheet.setColumnWidth(flag++, 10 * 255);
        
        int CountColumnNum = names.length;
        for (int i = 0; i < CountColumnNum; i++) {
            firstrow.createCell(i).setCellValue(new HSSFRichTextString(names[i]));
        }
        HSSFCellStyle cellStyleStr = hwb.createCellStyle();  
        cellStyleStr.setDataFormat(hwb.createDataFormat().getFormat("@"));  
        cellStyleStr.setWrapText(true);
        //DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        int rown=1;
        for(InterviewEntity interview: interviews){
        	if(interview!=null){
	        HSSFRow row = sheet.createRow(rown++);
	        
            HSSFCell cell=row.createCell(0);
            cell.setCellStyle(cellStyleStr);
            cell.setCellValue(interview.getProcessInstanceId());
            cell=row.createCell(1);
            cell.setCellValue(interview.getApplyTime());
            cell=row.createCell(2);
            cell.setCellValue(interview.getAppliName());
            cell=row.createCell(3);
            cell.setCellValue(interview.getInSysName());
            cell=row.createCell(4);
            cell.setCellValue(interview.getInOrgName());
            cell=row.createCell(5);
            cell.setCellValue(interview.getRank());	
           	cell=row.createCell(6);
           	cell.setCellValue(interview.getInJob());	
            cell=row.createCell(7);
            cell.setCellValue(interview.getAge());
            cell=row.createCell(8);
            cell.setCellValue(interview.getEducation());
            cell=row.createCell(9);
            cell.setCellValue(interview.getSpecialty());
            cell=row.createCell(10);
            cell.setCellValue(interview.getWorkYear());
            cell=row.createCell(11);
            cell.setCellValue(interview.getHrTotalEval());
            cell=row.createCell(12);
            cell.setCellValue(interview.getDepTotalEval());
            cell=row.createCell(13);
            cell.setCellValue(interview.getTeacherName());
            cell=row.createCell(14);
            cell.setCellValue(interview.getSalaryLevel());
            cell=row.createCell(15);
            cell.setCellValue(interview.getAlaryProbation());
            cell=row.createCell(16);
            cell.setCellValue(interview.getSalaryPositive());
            cell=row.createCell(17);
            Integer status=interview.getProcessStatus();
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
            }else if(status == 6){
            	cell.setCellValue("审批未通过");
            }else{
            	cell.setCellValue("未知状态");
            }
          }
        }
       
        // 创建文件输出流，准备输出电子表格
        response.setContentType("application/vnd.ms-excel");   
        String encodedFileName = MimeUtil.encodeFilename(request, "录用审批报表.xls");
		response.setHeader("Content-disposition", "attachment;filename=" + encodedFileName);
		OutputStream out = response.getOutputStream();
        hwb.write(out);
        out.flush();
        out.close();
        //System.out.println("数据库导出成功");
	}

	@Override
	public List<String> getApprover(String id) {
		// TODO Auto-generated method stub
		return activitiHelpDao.getApprover(id);
	}

	@Override
	public String getFatherDepartmentName(QueryMap queryMap) {
		return activitiHelpDao.getFatherDepartmentName(queryMap.getMap());
	}

	@Override
	public List<EngineerCollect> getMaintainCollect(QueryMap queryMap) {
		List<EngineerCollect> engineerCollect=activitiHelpDao.getMaintainCollect(queryMap.getMap());
		Integer sendCount=activitiHelpDao.count4SendOrderProcess(queryMap.getMap());
		double sumTime=0;
		double sumSatisfaCount=0;
		int count=0;
		DecimalFormat fnum = new DecimalFormat("##0.00");
		for(EngineerCollect collect:engineerCollect){
			double time=collect.getPerTime();
			double satisfac=Double.parseDouble(collect.getPerSatisfaction());
			sumTime+=time*Integer.parseInt(collect.getPerCount());
			sumSatisfaCount+=satisfac*Integer.parseInt(collect.getPerCount());
			double hour=time/60/60;
			collect.setPerAvgTime(fnum.format(hour));
			String rate=fnum.format(satisfac*100/10)+"%";
			collect.setPerSatisfactionRate(rate);
			collect.setPerSatisfaction(String.valueOf((int)satisfac));
			count++;
		}
		if(count>0){
			EngineerCollect engineer=new EngineerCollect();
			engineer.setEngineerName("汇总：维护总次数："+sendCount);
			engineer.setPerCount("整体维护平均数量："+sendCount/1.0/count);
			engineer.setPerAvgTime("整体维护平均耗时："+fnum.format(sumTime/sendCount/60/60));
			engineer.setPerSatisfaction("整体平均满意度:"+(int)(sumSatisfaCount/sendCount));
			engineer.setPerSatisfactionRate("整体平均满意率："+fnum.format(sumSatisfaCount/sendCount*100/10)+"%");
			engineerCollect.add(engineer);
		}
		return engineerCollect;
	}

	@Override
	public void toMaintainCollectExcel(List<EngineerCollect> collects, HttpServletResponse response,HttpServletRequest request) throws IOException {
		int size=collects.size();
		EngineerCollect engineer=null;
		int value=0;
		for(int i=0;i<size-1;i++){
			engineer=collects.get(i);
			value=Integer.parseInt(engineer.getPerSatisfaction());
			if(value == 9 || value == 10){
				engineer.setPerSatisfaction("非常满意");
			}else if(value == 7 || value == 8){
				engineer.setPerSatisfaction("基本满意");
			}else if(value == 6){
				engineer.setPerSatisfaction("满意");
			}else if(value == 4 || value == 5){
				engineer.setPerSatisfaction("不满意");
			}else{
				engineer.setPerSatisfaction("非常不满意");
			}
		}
		engineer=collects.get(size-1);
		String value1=engineer.getPerSatisfaction();
		value=Integer.parseInt(value1.substring(8));
		if(value == 9 || value == 10){
			engineer.setPerSatisfaction("整体平均满意度：非常满意");
		}else if(value == 7 || value == 8){
			engineer.setPerSatisfaction("整体平均满意度：基本满意");
		}else if(value == 6){
			engineer.setPerSatisfaction("整体平均满意度：满意");
		}else if(value == 4 || value == 5){
			engineer.setPerSatisfaction("整体平均满意度：不满意");
		}else{
			engineer.setPerSatisfaction("整体平均满意度：非常不满意");
		}
		// 获取总列数
        // 创建Excel文档
        HSSFWorkbook hwb = new HSSFWorkbook();
        // sheet 对应一个工作页
        HSSFSheet sheet = hwb.createSheet("派单汇总表");
        HSSFRow firstrow = sheet.createRow(0); // 下标为0的行开始
        String[] names = new String[]{"处理工程师","个人维护数量","个人处理平均耗时(小时)","个人平均满意度","个人平均满意率"};
        
        //设置excel宽度
        int flag = 0;
        sheet.setColumnWidth(flag++, 20 * 300);
        sheet.setColumnWidth(flag++, 20 * 300);
        sheet.setColumnWidth(flag++, 20 * 300);
        sheet.setColumnWidth(flag++, 20 * 300);
        sheet.setColumnWidth(flag++, 20 * 300);
        
        int CountColumnNum = names.length;
        for (int i = 0; i < CountColumnNum; i++) {
            firstrow.createCell(i).setCellValue(new HSSFRichTextString(names[i]));
        }
        HSSFCellStyle cellStyleStr = hwb.createCellStyle();  
        cellStyleStr.setDataFormat(hwb.createDataFormat().getFormat("@"));  
        cellStyleStr.setWrapText(true);
        //DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        int rown=1;
        for(EngineerCollect collect: collects){
        	if(collect!=null){
	        HSSFRow row = sheet.createRow(rown++);
	        
            HSSFCell cell=row.createCell(0);
            cell.setCellStyle(cellStyleStr);
            cell.setCellValue(collect.getEngineerName());
            cell=row.createCell(1);
            cell.setCellValue(collect.getPerCount());
            cell=row.createCell(2);
            cell.setCellValue(collect.getPerAvgTime());
            cell=row.createCell(3);
            cell.setCellValue(collect.getPerSatisfaction());
            cell=row.createCell(4);
            cell.setCellValue(collect.getPerSatisfactionRate());	
          }
        }
       
        // 创建文件输出流，准备输出电子表格
        response.setContentType("application/vnd.ms-excel");   
        String encodedFileName = MimeUtil.encodeFilename(request, "维护汇总报表.xls");
		response.setHeader("Content-disposition", "attachment;filename=" + encodedFileName);
		OutputStream out = response.getOutputStream();
        hwb.write(out);
        out.flush();
        out.close();
        //System.out.println("数据库导出成功");
	}

	@Override
	public List<String> assetsNoisOffApply() {
		return activitiHelpDao.assetsNoisOffApply();
	}

	@Override
	public void toSendOrderDetailExcel(List<SendOrderApplyEntity> sendOrderProcess, HttpServletResponse response,HttpServletRequest request)
			throws IOException {
		int size=sendOrderProcess.size();
		SendOrderApplyEntity sendOrder=null;
		int value=0;
		int status=0;
		for(int i=0;i<size;i++){
			sendOrder=sendOrderProcess.get(i);
			value=Integer.parseInt(sendOrder.getSatisfaction());
			status=sendOrder.getProcessStatus();
			if(value == 9 || value == 10){
				sendOrder.setSatisfaction("非常满意");
			}else if(value == 7 || value == 8){
				sendOrder.setSatisfaction("基本满意");
			}else if(value == 6){
				sendOrder.setSatisfaction("满意");
			}else if(value == 4 || value == 5){
				sendOrder.setSatisfaction("不满意");
			}else{
				sendOrder.setSatisfaction("非常不满意");
			}
			if(status==1){
				sendOrder.setStatusConent("运行中");
			}else if(status==2){
				sendOrder.setStatusConent("已完成");
			}
		}
		
		// 获取总列数
        // 创建Excel文档
        HSSFWorkbook hwb = new HSSFWorkbook();
        // sheet 对应一个工作页
        HSSFSheet sheet = hwb.createSheet("派单申请表");
        HSSFRow firstrow = sheet.createRow(0); // 下标为0的行开始
        String[] names = new String[]{"流程实例编号","实例名称","流程名称","任务名称","任务处理工程师","任务创建时间","工程师处理完成时间","处理耗时(小时)","流程状态","用户满意度","满意率"};
        
        //设置excel宽度
        int flag = 0;
        sheet.setColumnWidth(flag++, 20 * 300);
        sheet.setColumnWidth(flag++, 20 * 300);
        sheet.setColumnWidth(flag++, 20 * 200);
        sheet.setColumnWidth(flag++, 20 * 200);
        sheet.setColumnWidth(flag++, 20 * 200);
        sheet.setColumnWidth(flag++, 20 * 250);
        sheet.setColumnWidth(flag++, 20 * 250);
        sheet.setColumnWidth(flag++, 20 * 200);
        sheet.setColumnWidth(flag++, 20 * 200);
        sheet.setColumnWidth(flag++, 20 * 200);
        sheet.setColumnWidth(flag++, 20 * 200);
        
        int CountColumnNum = names.length;
        for (int i = 0; i < CountColumnNum; i++) {
            firstrow.createCell(i).setCellValue(new HSSFRichTextString(names[i]));
        }
        HSSFCellStyle cellStyleStr = hwb.createCellStyle();  
        cellStyleStr.setDataFormat(hwb.createDataFormat().getFormat("@"));  
        cellStyleStr.setWrapText(true);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int rown=1;
        for(SendOrderApplyEntity sendOrders: sendOrderProcess){
        	if(sendOrders!=null){
	        HSSFRow row = sheet.createRow(rown++);
	        
            HSSFCell cell=row.createCell(0);
            cell.setCellStyle(cellStyleStr);
            cell.setCellValue(sendOrders.getProcessInstanceId());
            cell=row.createCell(1);
            cell.setCellValue(sendOrders.getProcessInstanceName());
            cell=row.createCell(2);
            cell.setCellValue(sendOrders.getProcessDefName());
            cell=row.createCell(3);
            cell.setCellValue(sendOrders.getTaskName());
            cell=row.createCell(4);
            cell.setCellValue(sendOrders.getEngineerName());
            cell=row.createCell(5);
            cell.setCellValue(df.format(sendOrders.getStartTime()));
            cell=row.createCell(6);
            cell.setCellValue(df.format(sendOrders.getEndTime()));
            cell=row.createCell(7);
            cell.setCellValue(sendOrders.getElapsedTime());
            cell=row.createCell(8);
            cell.setCellValue(sendOrders.getStatusConent());
            cell=row.createCell(9);
            cell.setCellValue(sendOrders.getSatisfaction());
            cell=row.createCell(10);
            cell.setCellValue(sendOrders.getSatisfactionRate());
          }
        }
       
        // 创建文件输出流，准备输出电子表格
        response.setContentType("application/vnd.ms-excel");   
        String encodedFileName = MimeUtil.encodeFilename(request, "派单申请报表.xls");
		response.setHeader("Content-disposition", "attachment;filename=" + encodedFileName);
		OutputStream out = response.getOutputStream();
        hwb.write(out);
        out.flush();
        out.close();
        //System.out.println("数据库导出成功");
	}

	@Override
	public List<Probation> getIprobationEation(QueryMap queryMap) {
		List<Probation> probations=activitiHelpDao.getIprobationEation(queryMap.getMap());
		for(Probation probation:probations){
			List<Probation> salarys=activitiHelpDao.getSalaryGroup(probation.getProcessInstanceId());
			if(salarys!=null&&salarys.size()>0){
				Probation salary=salarys.get(0);
				if(salary!=null){
					probation.setCurSalaryGroup(salary.getCurSalaryGroup());
					probation.setCurSalary(salary.getCurSalary());
					probation.setFuSalaryGroup(salary.getFuSalaryGroup());
					probation.setFuSalary(salary.getFuSalary());
				}
			}
		}
		return probations;
	}
	@Override
	public List<Probation> getIprobationEation4Hr(QueryMap queryMap) {
		List<Probation> probations=activitiHelpDao.getIprobationEation(queryMap.getMap());
		return probations;
	}
	@Override
	public Integer getProbationCount(QueryMap queryMap) {
		return activitiHelpDao.getProbationCount(queryMap.getMap());
	}

	@Override
	public List<BpmHisProcessInstanceEntity> page4RecruiterUserProcess(QueryMap queryMap) throws Exception {
		return activitiHelpDao.page4RecruiterUserProcess(queryMap.getMap());
	}

	@Override
	public Integer page4RecruiterUserProcessCount(QueryMap queryMap) throws Exception {
		return activitiHelpDao.page4RecruiterUserProcessCount(queryMap.getMap());
	}

	@Override
	public List<Requirements> getRequirementReport(QueryMap queryMap) {
		return 	activitiHelpDao.getRequirementReport(queryMap.getMap());
	}

	@Override
	public Integer getRequirementReportCount(QueryMap queryMap) {
		 return activitiHelpDao.getRequirementReportCount(queryMap.getMap());
	}

	@Override
	public void toIprobationEationExcel(List<Probation> probations, HttpServletResponse response,HttpServletRequest request) throws IOException {
		 HSSFWorkbook hwb = new HSSFWorkbook();
	        // sheet 对应一个工作页
	        HSSFSheet sheet = hwb.createSheet("金立试用期考核报表");
	        HSSFRow firstrow = sheet.createRow(0); // 下标为0的行开始
	        String[] names = new String[]{"流程编号","直属部门","转正日期","员工编号","姓名","职位","调薪审批","试用期薪级","试用期薪资","转正后薪级","转正后薪资","流程状态"};
	        
	        //设置excel宽度
	        int flag = 0;
	        sheet.setColumnWidth(flag++, 20 * 255);
	        sheet.setColumnWidth(flag++, 15 * 255);
	        sheet.setColumnWidth(flag++, 15 * 255);
	        sheet.setColumnWidth(flag++, 10 * 255);
	        sheet.setColumnWidth(flag++, 22 * 255);
	        sheet.setColumnWidth(flag++, 15 * 255);
	        sheet.setColumnWidth(flag++, 10 * 255);
	        sheet.setColumnWidth(flag++, 10 * 255);
	        sheet.setColumnWidth(flag++, 10 * 255);
	        sheet.setColumnWidth(flag++, 10 * 255);
	        sheet.setColumnWidth(flag++, 10 * 255);
	        sheet.setColumnWidth(flag++, 10 * 255);
	        
	        int CountColumnNum = names.length;
	        for (int i = 0; i < CountColumnNum; i++) {
	            firstrow.createCell(i).setCellValue(new HSSFRichTextString(names[i]));
	        }
	        HSSFCellStyle cellStyleStr = hwb.createCellStyle();  
	        cellStyleStr.setDataFormat(hwb.createDataFormat().getFormat("@"));  
	        cellStyleStr.setWrapText(true);
	        //DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        int rown=1;
	        for(Probation probation: probations){
	        	if(probation!=null){
		        HSSFRow row = sheet.createRow(rown++);
		        HSSFCell cell=row.createCell(0);
		        cell.setCellStyle(cellStyleStr);
	            cell.setCellValue(probation.getProcessInstanceId());
	            cell=row.createCell(1);
	            cell.setCellValue(probation.getOrgName());
	            cell=row.createCell(2);
	            cell.setCellValue(probation.getAgreeTime());
	            cell=row.createCell(3);
	            cell.setCellValue(probation.getAccount());
	            cell=row.createCell(4);
	            cell.setCellValue(probation.getEmpName());
	            cell=row.createCell(5);
	            cell.setCellValue(probation.getEmpJob());
	            cell=row.createCell(6);
	            String salAdjust=probation.getSalAdjust();
	            if("2".equals(salAdjust)){
	            	cell.setCellValue("按约定转正调薪");
	            }else if("3".equals(salAdjust)){
	            	cell.setCellValue("转正不调薪");
	            }else{
	            	cell.setCellValue("特殊转正调薪");
	            }
	           	cell=row.createCell(7);
	           	cell.setCellValue(probation.getCurSalaryGroup());	
	            cell=row.createCell(8);
	            cell.setCellValue(probation.getCurSalary());
	            cell=row.createCell(9);
	            cell.setCellValue(probation.getFuSalaryGroup());
	            cell=row.createCell(10);
	            cell.setCellValue(probation.getFuSalary());
	            cell=row.createCell(11);
	            Integer status=probation.getProcessStatus();
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
	            }else if(status == 6){
	            	cell.setCellValue("审批未通过");
	            }else{
	            	cell.setCellValue("未知状态");
	            }
	          }
	        }
	       
	        // 创建文件输出流，准备输出电子表格
	        response.setContentType("application/vnd.ms-excel");   
	        String encodedFileName = MimeUtil.encodeFilename(request, "试用期转正审批报表.xls");
			response.setHeader("Content-disposition", "attachment;filename=" + encodedFileName);
			OutputStream out = response.getOutputStream();
	        hwb.write(out);
	        out.flush();
	        out.close();
	}
	@Override
	public void toIprobationEationExcel4Hr(List<Probation> probations, HttpServletResponse response,HttpServletRequest request) throws IOException {
		 HSSFWorkbook hwb = new HSSFWorkbook();
	        // sheet 对应一个工作页
	        HSSFSheet sheet = hwb.createSheet("金立试用期考核报表");
	        HSSFRow firstrow = sheet.createRow(0); // 下标为0的行开始
	        String[] names = new String[]{"流程编号","直属部门","转正日期","员工编号","姓名","职位","调薪审批","流程状态"};
	        
	        //设置excel宽度
	        int flag = 0;
	        sheet.setColumnWidth(flag++, 20 * 255);
	        sheet.setColumnWidth(flag++, 15 * 255);
	        sheet.setColumnWidth(flag++, 15 * 255);
	        sheet.setColumnWidth(flag++, 10 * 255);
	        sheet.setColumnWidth(flag++, 22 * 255);
	        sheet.setColumnWidth(flag++, 15 * 255);
	        sheet.setColumnWidth(flag++, 10 * 255);
	        sheet.setColumnWidth(flag++, 10 * 255);
	        
	        int CountColumnNum = names.length;
	        for (int i = 0; i < CountColumnNum; i++) {
	            firstrow.createCell(i).setCellValue(new HSSFRichTextString(names[i]));
	        }
	        HSSFCellStyle cellStyleStr = hwb.createCellStyle();  
	        cellStyleStr.setDataFormat(hwb.createDataFormat().getFormat("@"));  
	        cellStyleStr.setWrapText(true);
	        int rown=1;
	        for(Probation probation: probations){
	        	if(probation!=null){
		        HSSFRow row = sheet.createRow(rown++);
		        
	            HSSFCell cell=row.createCell(0);
	            cell.setCellStyle(cellStyleStr);
	            cell.setCellValue(probation.getProcessInstanceId());
	            cell=row.createCell(1);
	            cell.setCellValue(probation.getOrgName());
	            cell=row.createCell(2);
	            cell.setCellValue(probation.getAgreeTime());
	            cell=row.createCell(3);
	            cell.setCellValue(probation.getAccount());
	            cell=row.createCell(4);
	            cell.setCellValue(probation.getEmpName());
	            cell=row.createCell(5);
	            cell.setCellValue(probation.getEmpJob());
	            cell=row.createCell(6);
	            String salAdjust=probation.getSalAdjust();
	            if("2".equals(salAdjust)){
	            	cell.setCellValue("按约定转正调薪");
	            }else if("3".equals(salAdjust)){
	            	cell.setCellValue("转正不调薪");
	            }else{
	            	cell.setCellValue("特殊转正调薪");
	            }
	           	cell=row.createCell(7);
	            Integer status=probation.getProcessStatus();
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
	            }else if(status == 6){
	            	cell.setCellValue("审批未通过");
	            }else{
	            	cell.setCellValue("未知状态");
	            }
	          }
	        }
	       
	        // 创建文件输出流，准备输出电子表格
	        response.setContentType("application/vnd.ms-excel");   
	        String encodedFileName = MimeUtil.encodeFilename(request, "试用期转正审批报表.xls");
			response.setHeader("Content-disposition", "attachment;filename=" + encodedFileName);
			OutputStream out = response.getOutputStream();
	        hwb.write(out);
	        out.flush();
	        out.close();
	}
	@Override
	public List<AccountHandel> getAllAccountHandelProcess(QueryMap queryMap) {
		// TODO Auto-generated method stub
		return activitiHelpDao.getAllAccountHandelProcess(queryMap.getMap());
	}

	@Override
	public Integer getAllAccountHandelProcessCount(QueryMap queryMap) {
		// TODO Auto-generated method stub
		return activitiHelpDao.getAllAccountHandelProcessCount(queryMap.getMap());
	}

	@Override
	public void toAccountHandelExcel(List<AccountHandel> accountHandel, HttpServletResponse response,HttpServletRequest request) throws IOException {
		// TODO Auto-generated method stub
		 HSSFWorkbook hwb = new HSSFWorkbook();
	        // sheet 对应一个工作页
	        HSSFSheet sheet = hwb.createSheet("户口申请报表");
	        HSSFRow firstrow = sheet.createRow(0); // 下标为0的行开始
	        String[] names = new String[]{"流程实例编号","部门","员工编号","姓名","职位","入职日期","学历","申请日期","状态"};
	        
	        //设置excel宽度
	        int flag = 0;
	        sheet.setColumnWidth(flag++, 20 * 255);
	        sheet.setColumnWidth(flag++, 20 * 255);
	        sheet.setColumnWidth(flag++, 15 * 255);
	        sheet.setColumnWidth(flag++, 10 * 255);
	        sheet.setColumnWidth(flag++, 25 * 255);
	        sheet.setColumnWidth(flag++, 10 * 255);
	        sheet.setColumnWidth(flag++, 10 * 255);
	        sheet.setColumnWidth(flag++, 10 * 255);
	        sheet.setColumnWidth(flag++, 10 * 255);
	        sheet.setColumnWidth(flag++, 10 * 255);
	        
	        int CountColumnNum = names.length;
	        for (int i = 0; i < CountColumnNum; i++) {
	            firstrow.createCell(i).setCellValue(new HSSFRichTextString(names[i]));
	        }
	        HSSFCellStyle cellStyleStr = hwb.createCellStyle();  
	        cellStyleStr.setDataFormat(hwb.createDataFormat().getFormat("@"));  
	        cellStyleStr.setWrapText(true);
	        //DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        int rown=1;
	        for(AccountHandel ah: accountHandel){
	        	if(ah!=null){
		        HSSFRow row = sheet.createRow(rown++);
		        
	            HSSFCell cell=row.createCell(0);
	            cell.setCellStyle(cellStyleStr);
	            cell.setCellValue(ah.getProcessInstanceId());
	            cell=row.createCell(1);
	            cell.setCellValue(ah.getDepa());
	            cell=row.createCell(2);
	            cell.setCellValue(ah.getEmpId());
	            cell=row.createCell(3);
	            cell.setCellValue(ah.getName());
	            cell=row.createCell(4);
	            cell.setCellValue(ah.getJob());	
	           	cell=row.createCell(5);
	           	cell.setCellValue(ah.getEntryDate());	
	            cell=row.createCell(6);
	            cell.setCellValue(ah.getEducation());
	            cell=row.createCell(7);
	            cell.setCellValue(ah.getApplyDate());
	            cell=row.createCell(8);
	            Integer status=ah.getProcessStatus();
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
	            }else if(status == 6){
	            	cell.setCellValue("审批未通过");
	            }else{
	            	cell.setCellValue("未知状态");
	            }
	          }
	        }
	       
	        // 创建文件输出流，准备输出电子表格
	        response.setContentType("application/vnd.ms-excel");   
	        String encodedFileName = MimeUtil.encodeFilename(request, "户口办理申请审批报表.xls");
			response.setHeader("Content-disposition", "attachment;filename=" + encodedFileName);
			OutputStream out = response.getOutputStream();
	        hwb.write(out);
	        out.flush();
	        out.close();
	}

	@Override
	public void toLeaveProcessInfoExcel(List<LeaveProcessInstanceEntity> leaves, HttpServletResponse response,HttpServletRequest request)
			throws IOException {
			HSSFWorkbook hwb = new HSSFWorkbook();
		 	HSSFSheet sheet = hwb.createSheet("离职流程报表");
	        HSSFRow firstrow = sheet.createRow(0); // 下标为0的行开始
	        firstrow.setHeightInPoints((float)36.6);
	        
	        Font fontTitle = PoiUtil.createFount(hwb, (short)12);
	        CellStyle cellStyleTitle = PoiUtil.defaultCellStyle(hwb,fontTitle);
	        cellStyleTitle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	        HSSFPalette palette = hwb.getCustomPalette();
	        palette.setColorAtIndex((short)9, (byte) (0xff & 183), (byte) (0xff & 222), (byte) (0xff & 232));
	        cellStyleTitle.setFillForegroundColor((short)9);
	        
	        String[] names = new String[]{"流程实例编号","部门","职务","员工编号","姓名","入职日期","离职原因","详细原因","申请时间","状态","审批人"};
	        
	        //设置excel宽度
	        int flag = 0;
	        sheet.setColumnWidth(flag++, 20 * 255);
	        sheet.setColumnWidth(flag++, 20 * 255);
	        sheet.setColumnWidth(flag++, 20 * 255);
	        sheet.setColumnWidth(flag++, 20 * 255);
	        sheet.setColumnWidth(flag++, 20 * 255);
	        sheet.setColumnWidth(flag++, 20 * 255);
	        sheet.setColumnWidth(flag++, 20 * 255);
	        sheet.setColumnWidth(flag++, 20 * 255);
	        sheet.setColumnWidth(flag++, 20 * 255);
	        sheet.setColumnWidth(flag++, 25 * 255);
	        sheet.setColumnWidth(flag++, 25 * 255);
	        
	        int CountColumnNum = names.length;
	        for (int i = 0; i < CountColumnNum; i++) {
	        	HSSFCell cell = firstrow.createCell(i);
	        	cell.setCellValue(new HSSFRichTextString(names[i]));
	        	cell.setCellStyle(cellStyleTitle);
	        }
	        HSSFCellStyle cellStyleStr = hwb.createCellStyle();  
	        cellStyleStr.setDataFormat(hwb.createDataFormat().getFormat("@"));  
	        cellStyleStr.setWrapText(true);
	        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        int rown=1;
	        DecimalFormat fnum = new DecimalFormat("##0.00");
	        for(LeaveProcessInstanceEntity leave: leaves){
	        	if(leave!=null){
		        HSSFRow row = sheet.createRow(rown++);
		        
	            HSSFCell cell=row.createCell(0);
	            cell.setCellStyle(cellStyleStr);
	            cell.setCellValue(leave.getProcessInstanceId());
	            cell=row.createCell(1);
	            cell.setCellValue(leave.getOrgName());
	            cell=row.createCell(2);
	            cell.setCellValue(leave.getDuty());
	            cell=row.createCell(3);
	            cell.setCellValue(leave.getEmpId());
	           	cell=row.createCell(4);
	           	cell.setCellValue(leave.getStartUserName());	
	           	cell=row.createCell(5);
	           	cell.setCellValue(leave.getEntryDate());
	            cell=row.createCell(6);
	            cell.setCellValue(leave.getLeaveResion());
	            cell=row.createCell(7);
	            cell.setCellValue(leave.getDetailResion());
	            cell=row.createCell(8);
	            if(leave.getStartTime() != null){
	            	cell.setCellValue(df.format(leave.getStartTime()));
	            }else{
	            	cell.setCellValue("");
	            }
	            cell=row.createCell(9);
	            Integer status=leave.getProcessStatus();
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
	            }else if(status == 6){
	            	cell.setCellValue("审批未通过");
	            }else{
	            	cell.setCellValue("未知状态");
	            }
	            cell=row.createCell(10);
	            cell.setCellValue(leave.getCheckUserName());
	          }
	        }
	       
	        // 创建文件输出流，准备输出电子表格
	        response.setContentType("application/vnd.ms-excel");   
	        String encodedFileName = MimeUtil.encodeFilename(request, "离职流程审批报表.xls");
			response.setHeader("Content-disposition", "attachment;filename=" + encodedFileName);
			OutputStream out = response.getOutputStream();
	        hwb.write(out);
	        out.flush();
	        out.close();
	}
	@Override
	public List<LaborContractRenewalEntity> getLaborContractRenewals(QueryMap map) {
		// TODO Auto-generated method stub
		List<LaborContractRenewalEntity> laborContractRenewals=activitiHelpDao.getLaborContractRenewals(map.getMap());
		return laborContractRenewals;
	}

	@Override
	public Integer getLaborContractRenewalsCount(QueryMap map) {
		// TODO Auto-generated method stub
		return activitiHelpDao.getLaborContractRenewalsCount(map.getMap());
	}

	@Override
	public void toLaborContractRenewalExcel(List<LaborContractRenewalEntity> laborContractRenewals,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
			 HSSFWorkbook hwb = returnLaborContractHSSFWorkbook(laborContractRenewals);
	        // 创建文件输出流，准备输出电子表格
	        response.setContentType("application/vnd.ms-excel");   
	        String encodedFileName = MimeUtil.encodeFilename(request, "劳动合同续签审批报表.xls");
			response.setHeader("Content-disposition", "attachment;filename=" + encodedFileName);
			OutputStream out = response.getOutputStream();
	        hwb.write(out);
	        out.flush();
	        out.close();
	}
	
	@Override
	public HSSFWorkbook returnLaborContractHSSFWorkbook(List<LaborContractRenewalEntity> laborContractRenewals){
		HSSFWorkbook hwb = new HSSFWorkbook();
        // sheet 对应一个工作页
        HSSFSheet sheet = hwb.createSheet("劳动合同续签审批报表");
        //在sheet里增加合并单元格  
        Font fontTitle = PoiUtil.createFount(hwb, (short)11);
        CellStyle cellStyleTitle = PoiUtil.defaultCellStyle(hwb,fontTitle);
        cellStyleTitle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        HSSFPalette palette = hwb.getCustomPalette();
        palette.setColorAtIndex((short)9, (byte) (0xff & 183), (byte) (0xff & 222), (byte) (0xff & 232));
        cellStyleTitle.setFillForegroundColor((short)9);
        Font fontCell = PoiUtil.createFount(hwb, (short)10);
        CellStyle cellStyleCell = PoiUtil.defaultCellStyle(hwb,fontCell);
        //设置表头的一些字段属性
        HSSFRow firstrow = sheet.createRow(0); // 下标为0的行开始
        firstrow.setHeightInPoints((float)36.6);
        String[] names = new String[]{"流程实例编号","部门","员工编码","姓名","职位","入职时间","续签合同类型","本次合同开始时间","本次合同结束时间","审批意见","状态","审批人","创建时间","完成时间"};
        //设置excel宽度
        int flag = 0;
        sheet.setColumnWidth(flag++, 20 * 255);
        sheet.setColumnWidth(flag++, 20 * 255);
        sheet.setColumnWidth(flag++, 15 * 255);
        sheet.setColumnWidth(flag++, 10 * 255);
        sheet.setColumnWidth(flag++, 20 * 255);
        sheet.setColumnWidth(flag++, 15 * 255);
        sheet.setColumnWidth(flag++, 15 * 255);
        sheet.setColumnWidth(flag++, 20 * 255);
        sheet.setColumnWidth(flag++, 20 * 255);
        sheet.setColumnWidth(flag++, 10 * 255);
        sheet.setColumnWidth(flag++, 10 * 255);
        sheet.setColumnWidth(flag++, 10 * 255);
        sheet.setColumnWidth(flag++, 20 * 255);
        sheet.setColumnWidth(flag++, 20 * 255);
        int CountColumnNum = names.length;
        for (int i = 0; i < CountColumnNum; i++) {
        	HSSFCell cell = firstrow.createCell(i);
        	cell.setCellValue(new HSSFRichTextString(names[i]));
        	cell.setCellStyle(cellStyleTitle);
        }
        //开始内容区域的绘制
        cellStyleCell.setDataFormat(hwb.createDataFormat().getFormat("@"));
        cellStyleCell.setWrapText(true);
        int rown=1;
        for(LaborContractRenewalEntity laborContractRenewal: laborContractRenewals){
          	if(laborContractRenewal!=null){
          		
          		HSSFRow row = sheet.createRow(rown); 
	            HSSFCell cell0=row.createCell(0);
	            cell0.setCellStyle(cellStyleCell);
	            cell0.setCellValue(laborContractRenewal.getProcessInstanceId());
	            
	            HSSFCell cell1=row.createCell(1);
	            cell1.setCellStyle(cellStyleCell);
	            cell1.setCellValue(laborContractRenewal.getPrincipalOrgName());
	            
	            HSSFCell cell2=row.createCell(2);
	            cell2.setCellStyle(cellStyleCell);
	            cell2.setCellValue(laborContractRenewal.getPrincipalEmpId());
	            
	            HSSFCell cell3=row.createCell(3);
	            cell3.setCellStyle(cellStyleCell);
	            cell3.setCellValue(laborContractRenewal.getPrincipalName());
	            
	            HSSFCell cell4=row.createCell(4);
	            cell4.setCellStyle(cellStyleCell);
	            cell4.setCellValue(laborContractRenewal.getPrincipalJob());
	            
	            HSSFCell cell5=row.createCell(5);
	            cell5.setCellStyle(cellStyleCell);
	            cell5.setCellValue(laborContractRenewal.getPrincipalEntryTime());
	            
	            HSSFCell cell6=row.createCell(6);
	            cell6.setCellStyle(cellStyleCell);
	            if(laborContractRenewal.getPrincipalContractType().equals("1")){
	            	cell6.setCellValue("固定期限合同");
	            }else if(laborContractRenewal.getPrincipalContractType().equals("0")){
	            	cell6.setCellValue("无固定期限合同");
	            }else{
	            	cell6.setCellValue("未知合同");
	            }
	            
	            HSSFCell cell7=row.createCell(7);
	            cell7.setCellStyle(cellStyleCell);
	            cell7.setCellValue(laborContractRenewal.getPrincipalContractStartTime());
	            
	            HSSFCell cell8=row.createCell(8);
	            cell8.setCellStyle(cellStyleCell);
	            cell8.setCellValue(laborContractRenewal.getPrincipalContractEndTime());
	            
	            HSSFCell cell9=row.createCell(9);
	            cell9.setCellStyle(cellStyleCell);
	            if(laborContractRenewal.getApprovalOpinion().equals("2")){
	            	cell9.setCellValue("同意");
	            }else if(laborContractRenewal.getApprovalOpinion().equals("6")){
	            	cell9.setCellValue("不同意");
	            }else{
	            	cell9.setCellValue("");
	            }
	            
	            HSSFCell cell10=row.createCell(10);
	            cell10.setCellStyle(cellStyleCell);
	            cell10.setCellValue(laborContractRenewal.getStatus());
	            if(laborContractRenewal.getStatus().equals(BpmProcessRun.STATUS_ACTIVE.toString())){
	            	cell10.setCellValue("已激活");
	            }else if(laborContractRenewal.getStatus().equals(BpmProcessRun.STATUS_COMPLETED.toString())){
	            	cell10.setCellValue("已完成");
	            }else if(laborContractRenewal.getStatus().equals(BpmProcessRun.STATUS_NO_APPRVAL_COMPLETED.toString())){
	            	cell10.setCellValue("审批未通过");
	            }else if(laborContractRenewal.getStatus().equals(BpmProcessRun.STATUS_OBSOLETE.toString())){
	            	cell10.setCellValue("已作废");
	            }else if(laborContractRenewal.getStatus().equals(BpmProcessRun.STATUS_RUN.toString())){
	            	cell10.setCellValue("运行中");
	            }else if(laborContractRenewal.getStatus().equals(BpmProcessRun.STATUS_SUSPENDED.toString())){
	            	cell10.setCellValue("已挂起");
	            }else{
	            	cell10.setCellValue("未知状态");
	            }
	            
	            HSSFCell cell11=row.createCell(11);
	            cell11.setCellStyle(cellStyleCell);
	            cell11.setCellValue(laborContractRenewal.getApprovalUserName());
	            
	            HSSFCell cell12=row.createCell(12);
	            cell12.setCellStyle(cellStyleCell);
	            cell12.setCellValue(laborContractRenewal.getStartTime());
	            
	            HSSFCell cell13=row.createCell(13);
	            cell13.setCellStyle(cellStyleCell);
	            cell13.setCellValue(laborContractRenewal.getEndTime());
	            
	            
	            rown+=1;
	          }
        }
        
        return hwb;
	}

	@Override
	public AjaxJson sendLCRSureEmail(List<LaborContractRenewalEntity> laborContractRenewals,String renewalDate) {
		// TODO Auto-generated method stub
		HSSFWorkbook hwb = this.returnLaborContractHSSFWorkbook(laborContractRenewals);
		AjaxJson ajaxJson = null;
		
		OssClient ossClient = (OssClient) SpringTools.getBean("ossClient");
		ProcessHelpService processHelpService = (ProcessHelpService) SpringTools.getBean("processHelpService");
		try {
			String fileName = "劳动合同续签报表_"+new Date().getTime()+".xls";
			String title = "";
			String content = "";
			
			//查询 邮件模板
			QueryMap queryMap = new QueryMap();
			queryMap.put("name", PropertyHolder.getContextProperty(WebReqConstant.SURE_LABOR_CONTRACT_EMAIL_QUERY));
			List<BpmEmailTemplate> bpmEmailTemplates = bpmEmailTemplateService.query(queryMap);
			if(bpmEmailTemplates != null && bpmEmailTemplates.size()>0){
				content = bpmEmailTemplates.get(0).getContent();
				title = bpmEmailTemplates.get(0).getSubject();
			}else{
				return new AjaxJson(false, "邮件模板不存在，请上传");
			}
			
			//上传工作簿文件至文件服务器
			ByteArrayOutputStream  outputStream = new ByteArrayOutputStream(); 
			hwb.write(outputStream);
			InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
			outputStream.close();
			Message message = ossClient.upload(inputStream, fileName);
			inputStream.close();
			
			//处理邮件模板中的附件下载部门
			if(message!= null && message.getIsSuccess()){
				String fileNo = message.getAttributes().get("fileNo").toString();
				Map<String, String> map = new HashMap<String, String>();
				String policy = EncryptUtil.getPolicy("0", "0", fileNo);
				map.put(DownloadParameter.code.name(), ossClient.getCode());
				map.put(DownloadParameter.signature.name(), EncryptUtil.signature(ossClient.getKey(), policy));
				map.put(DownloadParameter.policy.name(), URLEncoder.encode(CalcUtil.getBase64(policy), "UTF-8"));
				String downloadUrl = ossClient.getDownloadURL().toString();
				content = content.replace("#downloadUrl", downloadUrl);
				content = content.replace("#policy", map.get(DownloadParameter.policy.name()));
				content = content.replace("#code", map.get(DownloadParameter.code.name()));
				content = content.replace("#signature", map.get(DownloadParameter.signature.name()));
				
			}
			
			AppUser user = AppContext.getCurrentAppUser();
			HrUserDto userDto = processHelpService.getHrUser4Account(user.getAccount());
			content = content.replace("#assignee", user.getName());
			title = title.replace("#renewalDate", renewalDate);
			SendMailTool.sendMailByAddress(title, content, userDto.getEmail());
//			SendMailTool.sendMailByAddress(title, content, "zhangp@gionee.com");
			ajaxJson = new AjaxJson(true, "邮件发送成功!");
		} catch (Exception e1) {
			e1.printStackTrace();
			ajaxJson = new AjaxJson(true, "提醒邮件发送失败");
		}
		return ajaxJson;
	}

	@Override
	public AjaxJson sendLCRReminderEmail(LaborContractRenewalEntity laborContractRenewal,String address,String renewalDate) {
		// TODO Auto-generated method stub
		AjaxJson ajaxJson = null;
		ProcessHelpService processHelpService = (ProcessHelpService) SpringTools.getBean("processHelpService");
		try {
			String title = "";
			String content = "";
			
			//查询邮件模板
			QueryMap queryMap = new QueryMap();
			queryMap.put("name", PropertyHolder.getContextProperty(WebReqConstant.REMINDER_LABOR_CONTRACT_EMAIL_QUERY));
			List<BpmEmailTemplate> bpmEmailTemplates = bpmEmailTemplateService.query(queryMap);
			if(bpmEmailTemplates != null && bpmEmailTemplates.size()>0){
				content = bpmEmailTemplates.get(0).getContent();
				title = bpmEmailTemplates.get(0).getSubject();
			}else{
				return new AjaxJson(false, "邮件模板不存在，请上传");
			}
			
			if (!StringUtils.isEmpty(address)) {
				AppUser user = AppContext.getCurrentAppUser();
				String account = user.getAccount();
				HrUserDto userDto = processHelpService.getHrUser4Account(account);
				
				String userName = laborContractRenewal.getPrincipalName();
				String expireDate = laborContractRenewal.getPrincipalContractExpire();
				String name = userDto.getName();
				String email = userDto.getEmail();
				String imageUrl = PropertyHolder.getContextProperty(WebReqConstant.REMINDER_LABOR_CONTRACT_EMAIL_IMAGE);
				String bgImageUrl = PropertyHolder.getContextProperty(WebReqConstant.REMINDER_LABOR_CONTRACT_EMAIL_BGIMAGE);
				
				//获取文件的完整路径
//				String appPath = SendMailUtil.getAppPath(this.getClass());
//				FileInputStream inputStream = new FileInputStream(new File(appPath + "/../../image/emailBg.png"));
//				bgImageUrl = returnDownloadUrLAfterUpload(inputStream, "emailBg.jpg");
				
				if (StringUtils.isNotEmpty(userName) && expireDate != null && name != null && email != null) {
					content = content.replace("#assignee", userName);
					content = content.replace("#expireDate", expireDate);
					content = content.replace("#renewalDate", renewalDate);
					content = content.replace("#name", name);
					content = content.replaceAll("#email", email);
					content = content.replace("#imageUrl", imageUrl);
					content = content.replace("#bgImageUrl", bgImageUrl);
					SendMailTool.sendMailByAddress(title, content,address);
//					SendMailTool.sendMailByAddress(title, content, "zhangp@gionee.com");
					ajaxJson = new AjaxJson(true, "邮件发送成功!");
				}else{
					ajaxJson = new AjaxJson(true, "劳动合同续签邮件发送失败!");
				}
			}else{
				ajaxJson = new AjaxJson(false, "邮件接收者不存在邮箱!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = new AjaxJson(false, "劳动合同续签邮件发送失败!");
		}
		return ajaxJson;
	}

	@Override
	public String returnDownloadUrLAfterUpload(InputStream inputStream,String fileName) {
		// TODO Auto-generated method stub
		OssClient ossClient = (OssClient) SpringTools.getBean("ossClient");
		String downloadUrl = "";
		try {
			Message message = ossClient.upload(inputStream, fileName);
			inputStream.close();
			if(message!= null && message.getIsSuccess()){
				String fileNo = message.getAttributes().get("fileNo").toString();
				Map<String, String> map = new HashMap<String, String>();
				String policy = EncryptUtil.getPolicy("0", "0", fileNo);
				map.put(DownloadParameter.code.name(), ossClient.getCode());
				map.put(DownloadParameter.signature.name(), EncryptUtil.signature(ossClient.getKey(), policy));
				map.put(DownloadParameter.policy.name(), URLEncoder.encode(CalcUtil.getBase64(policy), "UTF-8"));
				
				StringBuilder sb = new StringBuilder();
				for (Map.Entry<String, String> entry : map.entrySet()) {
					sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
				}
				downloadUrl = ossClient.getDownloadURL() + "?" + sb.substring(0, sb.length());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return downloadUrl;
	}

	@Override
	public List<Map<String, Object>> getFrequentlyStartedProcess(QueryMap queryMap) {
		// TODO Auto-generated method stub
		return activitiHelpDao.getFrequentlyStartedProcess(queryMap.getMap());
	}
	
	@Override
	public List<Map<String, Object>> getRecentlyStartedProcess(QueryMap queryMap) {
		// TODO Auto-generated method stub
		return activitiHelpDao.getRecentlyStartedProcess(queryMap.getMap());
	}

	@Override
	public void toRequirementReportExcel(List<Requirements> requirements, HttpServletResponse response,HttpServletRequest request)
			throws IOException {
		// TODO Auto-generated method stub
		// 获取总列数
        // 创建Excel文档
        HSSFWorkbook hwb = new HSSFWorkbook();
        // sheet 对应一个工作页
        HSSFSheet sheet = hwb.createSheet("人员需求报表");
        HSSFRow firstrow = sheet.createRow(0); // 下标为0的行开始
        String[] names = new String[]{"流程编号","申请日期","需求系统","需求部门","职位职级","职位名称","需求人数","期望到岗日期","需求类型","性别","学历","专业","招聘途径","流程状态"};
        
        //设置excel宽度
        int flag = 0;
        sheet.setColumnWidth(flag++, 20 * 255);
        sheet.setColumnWidth(flag++, 10 * 255);
        sheet.setColumnWidth(flag++, 15 * 255);
        sheet.setColumnWidth(flag++, 15 * 255);
        sheet.setColumnWidth(flag++, 10 * 255);
        sheet.setColumnWidth(flag++, 15 * 255);
        sheet.setColumnWidth(flag++, 10 * 255);
        sheet.setColumnWidth(flag++, 10 * 255);
        sheet.setColumnWidth(flag++, 15 * 255);
        sheet.setColumnWidth(flag++, 10 * 255);
        sheet.setColumnWidth(flag++, 10 * 255);
        sheet.setColumnWidth(flag++, 10 * 255);
        sheet.setColumnWidth(flag++, 10 * 255);
        sheet.setColumnWidth(flag++, 10 * 255);
        
        int CountColumnNum = names.length;
        for (int i = 0; i < CountColumnNum; i++) {
            firstrow.createCell(i).setCellValue(new HSSFRichTextString(names[i]));
        }
        HSSFCellStyle cellStyleStr = hwb.createCellStyle();  
        cellStyleStr.setDataFormat(hwb.createDataFormat().getFormat("@"));  
        cellStyleStr.setWrapText(true);
        //DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        int rown=1;
        for(Requirements interview: requirements){
        	if(interview!=null){
	        HSSFRow row = sheet.createRow(rown++);
	        
            HSSFCell cell=row.createCell(0);
            cell.setCellStyle(cellStyleStr);
            cell.setCellValue(interview.getProcessInstanceId());
            
            cell=row.createCell(1);
            cell.setCellValue(interview.getReqDate());
            cell=row.createCell(2);
            cell.setCellValue(interview.getReqSystem());
            cell=row.createCell(3);
            cell.setCellValue(interview.getOrgName());
            cell=row.createCell(4);
            cell.setCellValue(interview.getReqRank());	
           	cell=row.createCell(5);
           	cell.setCellValue(interview.getReqJob());	
            cell=row.createCell(6);
            cell.setCellValue(interview.getReqNumber());
            cell=row.createCell(7);
            cell.setCellValue(interview.getReqHopeDate());
            cell=row.createCell(8);
            cell.setCellValue(interview.getReqType());
            cell=row.createCell(9);
            cell.setCellValue(interview.getSex());
            cell=row.createCell(10);
            cell.setCellValue(interview.getEducation());
            cell=row.createCell(11);
            cell.setCellValue(interview.getMajor());
            cell=row.createCell(12);
            cell.setCellValue(interview.getReqChannels());
            cell=row.createCell(13);
            Integer status=interview.getProcessStatus();
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
            }else if(status == 6){
            	cell.setCellValue("审批未通过");
            }else{
            	cell.setCellValue("未知状态");
            }
            
          }
        }
       
        // 创建文件输出流，准备输出电子表格
        response.setContentType("application/vnd.ms-excel");  
        String encodedFileName = MimeUtil.encodeFilename(request, "人员需求报表.xls");
		response.setHeader("Content-disposition", "attachment;filename=" + encodedFileName);
		OutputStream out = response.getOutputStream();
        hwb.write(out);
        out.flush();
        out.close();
	}
}
