package com.gionee.gniflow.web.cmd.filesign;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.runtime.Execution;
import org.apache.http.client.utils.DateUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.poi.xwpf.usermodel.XWPFTableCell.XWPFVertAlign;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.gionee.gnif.GnifException;
import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.model.SignFileInformation;
import com.gionee.gniflow.biz.service.BpmService;
import com.gionee.gniflow.biz.service.SignFileInformationService;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.web.bmp.ProcessStep;
import com.gionee.gniflow.web.util.SpringTools;

/**
 * 
 * The class <code>DrawTableWordPageCmd</code>
 * 替换会签Word封面
 * @author lipw
 * @version 1.0
 */
public class DrawTableWordPageCmd implements Command<XWPFDocument> {

	private static Logger logger = LoggerFactory.getLogger(DrawTableWordPageCmd.class);

	private String processInstanceId;

	private String fileFullPath;
	
	private BpmService bpmService;

	public DrawTableWordPageCmd(String processInstanceId, String fileFullPath) {
		this.processInstanceId = processInstanceId;
		this.fileFullPath = fileFullPath;
	}

	/**
	 * 替换Word模板中的字段-非会签
	 */
	public XWPFDocument execute(CommandContext commandContext) {

		// runTimeService
		RuntimeService runtimeService = Context.getProcessEngineConfiguration()
				.getRuntimeService();
		
		bpmService = (BpmService) SpringTools.getBean(BpmService.class);

		Execution execution = runtimeService.createExecutionQuery()
				.processInstanceId(processInstanceId).singleResult();
		
		String executionId = execution.getId();

		XWPFTable t = null;

		// 填充值
		XWPFDocument document = null;
		try {
			document = new XWPFDocument(
					POIXMLDocument.openPackage(fileFullPath));
		} catch (IOException e) {
			throw new GnifException("Word Template Not Exist!");
		}
		//查找最终文件编号
		SignFileInformationService sfService= (SignFileInformationService) SpringTools.getBean(SignFileInformationService.class);
		List<SignFileInformation> list = null;
		String processInstanceId=execution.getProcessInstanceId();
		QueryMap queryMap= new QueryMap();
		queryMap.put("procInstId", processInstanceId);
		list=sfService.querySignFileInformation(queryMap);
		String FileNo = null;
		if(!CollectionUtils.isEmpty(list)){
			SignFileInformation sfInfo=new SignFileInformation();
			sfInfo=list.get(0);
			FileNo=sfInfo.getFileNo();
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("FileName", (String) runtimeService.getVariable(executionId, "filename"));
		map.put("FileNo",FileNo);
		map.put("FileState",(String) runtimeService.getVariable(executionId, "version")+"-"+(String) runtimeService.getVariable(executionId, "versionstate"));
		map.put("Dept", (String) runtimeService.getVariable(executionId, "orgName")); 
		map.put("DeptLeader", (String) runtimeService.getVariable(executionId, "departmentLeaderAudiTaskName"));
		map.put("Management","managementLeaderAudiTaskName");
		map.put("ManagementAudi", (String) runtimeService.getVariable(executionId, "gioneechairmanAudiTaskName"));
		map.put("IssueDate", DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
		
		//找到会签的人及部门
		List<ProcessStep> steps = bpmService.getSignProcessStepList(processInstanceId);
		
		// 遍历段落
		Iterator<XWPFTable> itTable = document.getTablesIterator();
		while (itTable.hasNext()) {
			XWPFTable table = (XWPFTable) itTable.next();
			int rcount = table.getNumberOfRows();
			for (int i = 0; i < rcount; i++) {
				XWPFTableRow row = table.getRow(i);
				List<XWPFTableCell> cells = row.getTableCells();
				for (XWPFTableCell cell : cells) {

					List<XWPFParagraph> paragraphList = cell.getParagraphs();
					if (!CollectionUtils.isEmpty(paragraphList)) {
						// 读取表格中的段落
						for (XWPFParagraph p : paragraphList) {
							for (XWPFRun r : p.getRuns()) {
								for (CTText ct : r.getCTR().getTList()) {
									String str = ct.getStringValue();
									if (str.startsWith(":") && str.length() > 1) {
										str = str.substring(1, str.length());
									}
									logger.debug("Word String-->" + str);
									if (map.containsKey(str)) {
										str = str.replace(str, map.get(str));
										ct.setStringValue(str);
									}
								}
							}
						}
					}
					// 替换Table中的值
					List<XWPFTable> pTable = cell.getTables();
					if (!CollectionUtils.isEmpty(pTable)) {
						t = pTable.get(0);
					}
				}
			}
		}

		XWPFTableCell cell = null;
		
		//
		List<ProcessStep> muliSteps = steps.get(steps.size() - 1).getMultiInstances();
		
		int rowIndex = 1;
		// 画会签表格
		for (int x = 0; x < muliSteps.size(); x=x+2) {
			XWPFTableRow xRow = t.insertNewTableRow(rowIndex);
			xRow.setHeight(700);
			cell = xRow.addNewTableCell();
			cell.setParagraph(createXWPFParagraph(cell, (String)muliSteps.get(x).getVars().get(muliSteps.get(x).getAssignee()+"SignAudiOrgName")));
			cell.setVerticalAlignment(XWPFVertAlign.CENTER);

			cell = xRow.addNewTableCell();
			cell.setParagraph(createXWPFParagraph(cell, (String)muliSteps.get(x).getVars().get(muliSteps.get(x).getAssignee()+"redepSignAudiMan")));
			cell.setVerticalAlignment(XWPFVertAlign.CENTER);
			
			if (muliSteps.size() - x != 1) {
				cell = xRow.addNewTableCell();
				cell.setParagraph(createXWPFParagraph(cell, (String)muliSteps.get(x+1).getVars().get(muliSteps.get(x+1).getAssignee()+"SignAudiOrgName")));
				cell.setVerticalAlignment(XWPFVertAlign.CENTER);
				
				cell = xRow.addNewTableCell();
				cell.setParagraph(createXWPFParagraph(cell, (String)muliSteps.get(x+1).getVars().get(muliSteps.get(x+1).getAssignee()+"redepSignAudiMan")));
				cell.setVerticalAlignment(XWPFVertAlign.CENTER);
			} else {
				cell = xRow.addNewTableCell();
				cell.setParagraph(createXWPFParagraph(cell, WebReqConstant.EMPTY_STRING));
				cell.setVerticalAlignment(XWPFVertAlign.CENTER);
				
				cell = xRow.addNewTableCell();
				cell.setParagraph(createXWPFParagraph(cell, WebReqConstant.EMPTY_STRING));
				cell.setVerticalAlignment(XWPFVertAlign.CENTER);
			}
			rowIndex++;
		}
		return document;
	}

	/**
	 * 创建段落
	 * 
	 * @return
	 */
	private static XWPFParagraph createXWPFParagraph(XWPFTableCell cell,
			String content) {
		// 获取表格的段落，也就是Word中的回车位
		XWPFParagraph p = cell.getParagraphs().get(0);
		p.setAlignment(ParagraphAlignment.CENTER);
		p.setVerticalAlignment(TextAlignment.CENTER);
		p.setWordWrap(true);// 自动换行

		XWPFRun r = p.createRun();
		r.setText(content);
		r.setBold(false);
		return p;
	}
}
