/**
 * @(#) TestHtmlParser.java Created on 2014年12月5日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.htmlparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.imageio.stream.FileImageInputStream;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.FormTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 * The class <code>TestHtmlParser</code>
 * 
 * @author lipw
 * @version 1.0
 */
public class TestHtmlParser {
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestHtmlParser htmlParser = new TestHtmlParser();
		BufferedReader bis = null;
		try {
			bis = new BufferedReader(new InputStreamReader(new FileInputStream(new File("D:\\leave.html"))));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String content = "";
		String temp = null;
		try {
			while ((temp = bis.readLine()) != null) {
				content += temp + "\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.print(content);
		
		System.out.println("###############################3");
		htmlParser.testTable(content);
	}

	public void testTable(String content) {
		Parser myParser;
		NodeList nodeList = null;
		myParser = Parser.createParser(content, "GBK");
		
		NodeFilter tableFilter = new NodeClassFilter(TableTag.class);
		OrFilter lastFilter = new OrFilter();
		lastFilter.setPredicates(new NodeFilter[] { tableFilter });
		try {
			nodeList = myParser.parse(lastFilter);
			for (int i = 0; i <= nodeList.size(); i++) {
				if (nodeList.elementAt(i) instanceof TableTag) {
					TableTag tag = (TableTag) nodeList.elementAt(i);
					TableRow[] rows = tag.getRows();

					for (int j = 0; j < rows.length; j++) {
						TableRow tr = (TableRow) rows[j];
						TableColumn[] td = tr.getColumns();
						for (int k = 0; k < td.length; k++) {
//							System.out.println("<td>" + td[k].toPlainTextString());
							System.out.println("<td>" + td[k].getChildrenHTML().trim());
						}

					}

				}
			}

		} catch (ParserException e) {
			e.printStackTrace();
		}
	}
}
