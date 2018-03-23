/**
 * @(#) TestHtmlScanFormEle.java Created on 2014年12月5日
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
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.InputTag;
import org.htmlparser.tags.OptionTag;
import org.htmlparser.tags.SelectTag;
import org.htmlparser.tags.TextareaTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.alibaba.fastjson.JSONObject;
import com.gionee.gniflow.form.FormElement;
import com.gionee.gniflow.form.FormJson;

/**
 * The class <code>TestHtmlScanFormEle</code>
 *
 * @author lipw
 * @version 1.0
 */
public class TestHtmlScanFormEle {

	public ArrayList<FormElement> httpParser(String content) {

		ArrayList<FormElement> scanResult = new ArrayList<FormElement>();

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
			FormElement formEle = new FormElement();
			if (anode instanceof SelectTag) {
				//select tag
				SelectTag selectnode = (SelectTag) anode;
				Vector<?> v = selectnode.getAttributesEx();

				NodeList nl = selectnode.getChildren();
				Node[] nl_nodes = nl.toNodeArray();
				int optNum = 0;
				String select_value = "";
				for (int j = 0; j < nl_nodes.length; j++) {
					Node optnode = (Node) nl_nodes[j];
					if (optnode instanceof OptionTag) {
						optNum++;
						OptionTag opttag = (OptionTag) optnode;
						Vector<?> vv = opttag.getAttributesEx();
						if (vv.toString().indexOf("selected") != -1)
							select_value = opttag.getOptionText();
					}
				}
				formEle.setName(selectnode.getAttribute("name"));
				formEle.setValue(select_value);
				formEle.setHtmlType(FormElement.HTML_TYPE_SELECT);
				
			} else if (anode instanceof InputTag) {
				//input tag
				InputTag inputnode = (InputTag) anode;
				Vector<?> v = inputnode.getAttributesEx();
				String type = inputnode.getAttribute("type");
				if (StringUtils.isNotEmpty(type) && type.equals("checkbox")) {
					formEle.setHtmlType(FormElement.HTML_TYPE_CHECKBOX);
				} else if (StringUtils.isNotEmpty(type) && type.equals("radio")) {
					formEle.setHtmlType(FormElement.HTML_TYPE_RADIOBOX);
				} else if (StringUtils.isNotEmpty(type) && type.equals("hidden")) {
					formEle.setHtmlType(FormElement.HTML_TYPE_HIDDEN);
				} else {
					formEle.setHtmlType(FormElement.HTML_TYPE_TEXT);
				}
				formEle.setName(inputnode.getAttribute("name"));
				formEle.setValue(inputnode.getAttribute("value"));
				formEle.setChecked(inputnode.getAttribute("checked"));
				formEle.setLable(inputnode.getAttribute("mobile"));
				if (StringUtils.isNotEmpty(inputnode.getAttribute("readonly"))){
					formEle.setReadOnly(true);
				}
				if (StringUtils.isNotEmpty(inputnode.getAttribute("data-options"))) {
					String[] property = inputnode.getAttribute("data-options").split(",");
					for (String item : property) {
						String[] arrt = item.split(":");
						formEle.getAttribute().put(arrt[0], arrt[1]);
					}
				}
			} else if (anode instanceof TextareaTag) {
				//textarea tag
				TextareaTag textareaNode = (TextareaTag) anode;
				formEle.setHtmlType(FormElement.HTML_TYPE_TEXTAREA);
				formEle.setName(textareaNode.getAttribute("name"));
				formEle.setValue(textareaNode.getAttribute("value"));
				formEle.setLable(textareaNode.getAttribute("mobile"));
				if (StringUtils.isNotEmpty(textareaNode.getAttribute("readonly"))){
					formEle.setReadOnly(true);
				}
				if (StringUtils.isNotEmpty(textareaNode.getAttribute("data-options"))) {
					String[] property = textareaNode.getAttribute("data-options").split(",");
					for (String item : property) {
						String[] arrt = item.split(":");
						formEle.getAttribute().put(arrt[0], arrt[1]);
					}
				}
			}
			
			scanResult.add(formEle);
		}

		return scanResult;
	
	}
	
	public static void main(String[] args) {
		TestHtmlScanFormEle test = new TestHtmlScanFormEle();

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

		List<FormElement> results = test.httpParser(content);
		
		FormJson json = new FormJson();
		json.setElements(results);
		json.setFormTitle("绩效专员审批");
		
		System.out.println(results);
		
		System.out.println(JSONObject.toJSONString(json));
	}
}
