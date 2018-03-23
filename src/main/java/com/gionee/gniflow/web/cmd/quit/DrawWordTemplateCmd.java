package com.gionee.gniflow.web.cmd.quit;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import com.gionee.gniflow.web.util.DateUtil;
import com.gionee.gniflow.web.util.SpringTools;
/**
 * 撤销任务.
 */
public class DrawWordTemplateCmd implements Command<XWPFDocument> {

	private String processInstanceId;
	
	private String fileFullPath;

	public DrawWordTemplateCmd(String processInstanceId, String fileFullPath) {
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
		Map<String, String> map = new HashMap<String, String>();
		map.put("UserName", (String) runtimeService.getVariable(executionId, "userName"));
		map.put("UserCardId", (String) runtimeService.getVariable(executionId, "userCardId"));
		map.put("DepartMent",(String) runtimeService.getVariable(executionId, "orgName"));
		map.put("Job", (String) runtimeService.getVariable(executionId, "jobDuty"));
		String startDate=(String) runtimeService.getVariable(executionId, "entryTime");
		Date date=DateUtil.parse(startDate, "yyyy-MM-dd");
		Calendar cal= GregorianCalendar.getInstance();
		
		cal.setTime(date);
		int year=cal.get(Calendar.YEAR);
		int month=cal.get(Calendar.MONTH)+1;
		int day=cal.get(Calendar.DATE);
		map.put("StartYear", Integer.toString(year));
		map.put("StartMonth", Integer.toString(month));
		map.put("StartDay", Integer.toString(day));
		
		String endDate=(String) runtimeService.getVariable(executionId, "levaeTime");
		date=DateUtil.parse(endDate, "yyyy-MM-dd");
		cal.setTime(date);
		year=cal.get(Calendar.YEAR);
		month=cal.get(Calendar.MONTH)+1;
		day=cal.get(Calendar.DATE);
		map.put("EndYear", Integer.toString(year));
		map.put("EndMonth", Integer.toString(month));
		map.put("EndDay", Integer.toString(day));
		
		Date now=new Date();
		cal.setTime(now);
		year=cal.get(Calendar.YEAR);
		month=cal.get(Calendar.MONTH)+1;
		day=cal.get(Calendar.DATE);
		map.put("SignYear", Integer.toString(year));
		map.put("SignMonth", Integer.toString(month));
		map.put("SignDay", Integer.toString(day));
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
									String str = ct.getStringValue().trim();
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
