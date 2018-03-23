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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.imageio.stream.FileImageInputStream;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.lexer.PageAttribute;
import org.htmlparser.tags.FormTag;
import org.htmlparser.tags.InputTag;
import org.htmlparser.tags.OptionTag;
import org.htmlparser.tags.SelectTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.tags.TextareaTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.springframework.util.CollectionUtils;

/**
 * The class <code>TestHtmlParser</code>
 * 
 * @author lipw
 * @version 1.0
 */
public class TestHtmlForUtilParser {

	class FormElements {

		private String _elementName;

		private String _elementValue;

		private String _elementType;
		
		private String _elementLable;

		public String get_elementName() {
			return _elementName;
		}

		public void set_elementName(String _elementName) {
			this._elementName = _elementName;
		}

		public String get_elementValue() {
			return _elementValue;
		}

		public void set_elementValue(String _elementValue) {
			this._elementValue = _elementValue;
		}

		public String get_elementType() {
			return _elementType;
		}

		public void set_elementType(String _elementType) {
			this._elementType = _elementType;
		}

		public String get_elementLable() {
			return _elementLable;
		}

		public void set_elementLable(String _elementLable) {
			this._elementLable = _elementLable;
		}
		
	}

	public ArrayList<FormElements> httpParser(String content, String lable) {
		ArrayList<FormElements> ret = new ArrayList<FormElements>();

		Parser myParser;
		NodeList nodeList = null;

		myParser = Parser.createParser(content, "UTF-8");

		NodeFilter inputFilter = new NodeClassFilter(InputTag.class);
		NodeFilter selectFilter = new NodeClassFilter(SelectTag.class);
		NodeFilter textAreaFilter = new NodeClassFilter(TextareaTag.class);

		OrFilter lastFilter = new OrFilter();
		lastFilter.setPredicates(new NodeFilter[] { selectFilter, inputFilter,textAreaFilter});
		try {
			nodeList = myParser.parse(lastFilter);
		} catch (ParserException e) {
			e.printStackTrace();
		}

		Node[] nodes = nodeList.toNodeArray();

		for (int i = 0; i < nodes.length; i++) {
			Node anode = (Node) nodes[i];
			FormElements fe = new FormElements();
			if (anode instanceof SelectTag) {
				SelectTag selectnode = (SelectTag) anode;
				Vector v = selectnode.getAttributesEx();

				NodeList nl = selectnode.getChildren();
				Node[] nl_nodes = nl.toNodeArray();
				int optNum = 0;
				String select_value = "";
				for (int j = 0; j < nl_nodes.length; j++) {
					Node optnode = (Node) nl_nodes[j];
					if (optnode instanceof OptionTag) {
						optNum++;
						OptionTag opttag = (OptionTag) optnode;
						Vector vv = opttag.getAttributesEx();
						if (vv.toString().indexOf("selected") != -1)
							select_value = opttag.getOptionText();
					}
				}
				fe.set_elementName(selectnode.getAttribute("name"));
				fe.set_elementValue(select_value);
				fe.set_elementType("select");

			} else if (anode instanceof InputTag) {
				InputTag inputnode = (InputTag) anode;
				Vector v = inputnode.getAttributesEx();
				System.out.println(v.toString());
				if ((v.toString().indexOf("type=checkbox") != -1)
						&& (v.toString().indexOf("checked") == -1)) {
					fe.set_elementType("checkbox");
					continue;
				} else if ((v.toString().indexOf("type=\"radio\"") != -1)) {
					fe.set_elementType("radio");
					Iterator iter = v.iterator();
					while(iter.hasNext()){
						PageAttribute value = (PageAttribute)iter.next();
						System.out.println("xvvvv--->" + value.getName() + value.getValue());
					}
					System.out.println("here@@@");
					continue;
				} else if ((v.toString().indexOf("type=hidden") != -1)) {
					fe.set_elementType("hidden");
				} else {
					fe.set_elementType("input");
				}
				fe.set_elementName(inputnode.getAttribute("name"));
				fe.set_elementValue(inputnode.getAttribute("value"));
			} else if (anode instanceof TextareaTag) {
				TextareaTag textareaNode = (TextareaTag) anode;
				Vector v = textareaNode.getAttributesEx();
				fe.set_elementType("textarea");
				fe.set_elementName(textareaNode.getAttribute("name"));
				fe.set_elementValue(textareaNode.getAttribute("value"));
			}
			
			fe.set_elementLable(lable);
			
			ret.add(fe);
		}

		return ret;
	}
	
	/**
	 * 扫描Table
	 * @param content
	 */
	public void scanTable(String htmlContent) {
		Parser myParser;
		NodeList nodeList = null;
		myParser = Parser.createParser(htmlContent, "UTF-8");

		NodeFilter tableFilter = new NodeClassFilter(TableTag.class);
		OrFilter lastFilter = new OrFilter();
		lastFilter.setPredicates(new NodeFilter[] {tableFilter});
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
							System.out.println("<td>"+ td[k].getChildrenHTML().trim());

							TestHtmlForUtilParser test = new TestHtmlForUtilParser();
							
							List<FormElements> list = null;
							if (td.length > 1) {
								if (k == 0 ) {
									list = test.httpParser(td[k].getChildrenHTML(), null);
								} else {
									list = test.httpParser(td[k].getChildrenHTML(), td[k-1].toPlainTextString());
								}
							}
							if (!CollectionUtils.isEmpty(list)) {
								results.addAll(list);
							}
						}

					}

				}
			}

		} catch (ParserException e) {
			e.printStackTrace();
		}
	}
	
	private static List<FormElements> results = new ArrayList<FormElements>();
	
	public static void main(String[] args) {
		TestHtmlForUtilParser test = new TestHtmlForUtilParser();

		BufferedReader bis = null;
		try {
			bis = new BufferedReader(new InputStreamReader(new FileInputStream(
					new File("D:\\leave.html"))));
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

//		test.scanTable(content);
		
		test.httpParser(content, null);
		
		System.out.println(results);
		
		System.out.println("aaa");
	}
}
