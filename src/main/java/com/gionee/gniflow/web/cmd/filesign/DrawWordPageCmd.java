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
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.springframework.util.CollectionUtils;

import com.gionee.gnif.GnifException;
import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.model.SignFileInformation;
import com.gionee.gniflow.biz.service.SignFileInformationService;
import com.gionee.gniflow.web.util.SpringTools;
/**
 * 撤销任务.
 */
public class DrawWordPageCmd implements Command<XWPFDocument> {

	private String processInstanceId;
	
	private String fileFullPath;

	public DrawWordPageCmd(String processInstanceId, String fileFullPath) {
		this.processInstanceId = processInstanceId;
		this.fileFullPath = fileFullPath;
	}

	/**
	 * 替换Word模板中的字段-非会签
	 */
	public XWPFDocument execute(CommandContext commandContext) {

		// runTimeService
		RuntimeService runtimeService = Context.getProcessEngineConfiguration().getRuntimeService();
		
		// 填充值
		XWPFDocument document = null;
		try {
			document = new XWPFDocument(POIXMLDocument.openPackage(fileFullPath));
		} catch (IOException e) {
			throw new GnifException("Word Template Not Exist!");
		}
		
		Execution execution =  runtimeService.createExecutionQuery().processInstanceId(processInstanceId).singleResult();
		String executionId = execution.getId();
		//查找最终的文件编号
		SignFileInformationService sfService= (SignFileInformationService) SpringTools.getBean(SignFileInformationService.class);
		List<SignFileInformation> list = null;
		String processInstanceId=execution.getProcessInstanceId();
		QueryMap queryMap= new QueryMap();
		queryMap.put("procInstId", processInstanceId);
		list=sfService.querySignFileInformation(queryMap);
		String FileNo=null;
		if(!CollectionUtils.isEmpty(list)){
			SignFileInformation sfInfo=new SignFileInformation();
			sfInfo=list.get(0);
			FileNo=sfInfo.getFileNo();
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("FileName", (String) runtimeService.getVariable(executionId, "filename"));
		map.put("FileNo",FileNo);
		map.put("FileState", (String) runtimeService.getVariable(executionId, "version")+"-"+(String) runtimeService.getVariable(executionId, "versionstate"));
		map.put("Dept", (String) runtimeService.getVariable(executionId, "orgName"));
		map.put("DeptLeader", (String) runtimeService.getVariable(executionId, "departmentLeaderAudiTaskName"));
		map.put("Management", "managementLeaderAudiTaskName");
		map.put("ManagementAudi", (String) runtimeService.getVariable(executionId, "gioneechairmanAudiTaskName"));
		map.put("IssueDate", DateUtils.formatDate(new Date(), "yyyy-MM-dd"));

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
									//System.out.println("str-->" + str);
									if (map.containsKey(str)) {
										str = str.replace(str, map.get(str));
										ct.setStringValue(str);
									}
								}
							}
						}
					}

				}
			}
		}
		return document;
	}

}
