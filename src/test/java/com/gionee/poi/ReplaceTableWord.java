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
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableCell.XWPFVertAlign;
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
public class ReplaceTableWord {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		XWPFTable t = null;
		
		XWPFDocument document = new XWPFDocument(POIXMLDocument.openPackage("D:\\Ya.docx"));
		
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
				                    if(str.startsWith(":") && str.length() > 1){
				                    	str = str.substring(1, str.length());
				                    }
				                    System.out.println("str-->" + str);
				                    if (map.containsKey(str)) {
				                    	 str = str.replace(str, map.get(str));
					                     ct.setStringValue(str);
				                    }
				                }
				            }
				        }
					}
					//替换Table中的值
					List<XWPFTable> pTable = cell.getTables();
					if (!CollectionUtils.isEmpty(pTable)) {
						t = pTable.get(0);
					}
				}
			}
		}
		
		XWPFTableCell cell = null;
		//画会签表格
		for (int x=1; x<3;x++) {
			XWPFTableRow xRow = t.insertNewTableRow(x);
			xRow.setHeight(700);
			cell = xRow.addNewTableCell();
	        cell.setParagraph(createXWPFParagraph(cell, "掌声那"));
	        cell.setVerticalAlignment(XWPFVertAlign.CENTER);
			
			cell = xRow.addNewTableCell();
			cell.setParagraph(createXWPFParagraph(cell, "我的世界我的世界我的世界我的世界"));
			cell.setVerticalAlignment(XWPFVertAlign.CENTER);
			
			cell = xRow.addNewTableCell();
			cell.setParagraph(createXWPFParagraph(cell, "CCCCCC"));
			cell.setVerticalAlignment(XWPFVertAlign.CENTER);
			
			cell = xRow.addNewTableCell();
			cell.setParagraph(createXWPFParagraph(cell, "AAAAAA"));
			cell.setVerticalAlignment(XWPFVertAlign.CENTER);
		}

		FileOutputStream out = new FileOutputStream("D:\\Ya2ttt.docx");
		document.write(out);
		out.close();
		
		System.out.println("Operate Word Dcoument Success!");
	}
	
	/**
	 * 创建段落
	 * @return
	 */
	private static XWPFParagraph createXWPFParagraph(XWPFTableCell cell, String content){
		//获取表格的段落，也就是Word中的回车位
		XWPFParagraph p = cell.getParagraphs().get(0);
        p.setAlignment(ParagraphAlignment.CENTER);
        p.setVerticalAlignment(TextAlignment.CENTER);
        p.setWordWrap(true);//自动换行

        XWPFRun r = p.createRun();
        r.setText(content);
        r.setBold(false);
        return p;
	}

}
