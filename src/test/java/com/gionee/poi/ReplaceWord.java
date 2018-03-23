/**
 * @(#) ReplaceWord.java Created on 2014年9月1日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.poi;

import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.springframework.util.CollectionUtils;

import com.gionee.gniflow.web.util.DateUtil;

/**
 * The class <code>ReplaceWord</code>
 * 
 * @author lipw
 * @version 1.0
 */
public class ReplaceWord {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		XWPFDocument document = new XWPFDocument(POIXMLDocument.openPackage("D:\\Xaa.docx"));
		
		Map<String,String> map = new HashMap<String, String>();
		map.put("FileName", "开发说明文档");
		map.put("FileNo", "开发测试文档");
		map.put("FileState", "A-001");
		map.put("Dept", "信息中心-开发部");
		map.put("DeptLeader", "张三");
		map.put("Management", "企管部");
		map.put("ManagementAudi", "李四");
		map.put("IssueDate", DateUtil.format(new Date(), "yyyy-MM-dd"));

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
						//读取表格中的段落
						for (XWPFParagraph p : paragraphList) {
				            for (XWPFRun r : p.getRuns()) {
				                for (CTText ct : r.getCTR().getTList()) {
				                    String str = ct.getStringValue();
				                    System.out.println("str-->" + str);
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

		FileOutputStream out = new FileOutputStream("D:\\simple.docx");
		document.write(out);
		out.close();
	}

}
