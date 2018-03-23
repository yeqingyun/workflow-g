/**
 * @(#) HtmlParserUtil.java Created on 2014年12月10日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.util;

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

import com.gionee.gniflow.form.FormElement;

/**
 * The class <code>HtmlParserUtil</code>
 *
 * @author lipw
 * @version 1.0
 */
public class HtmlParserUtil {
	/**
	 * 解析Html中的表单
	 * @param content
	 * @return
	 */
	public static ArrayList<FormElement> httpParser(String content) {
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
				//下拉框
				//select tag
				SelectTag selectnode = (SelectTag) anode;
				Vector<?> v = selectnode.getAttributesEx();

				NodeList nl = selectnode.getChildren();
				Node[] nl_nodes = nl.toNodeArray();
				int optNum = 0;
				String select_value = "";
				
				//下拉列表的所有选择项
				List<FormElement> subAttrs=new ArrayList<FormElement>();
				
				for (int j = 0; j < nl_nodes.length; j++) {
					Node optnode = (Node) nl_nodes[j];
					if (optnode instanceof OptionTag) {
						optNum++;
						OptionTag opttag = (OptionTag) optnode;
						Vector<?> vv = opttag.getAttributesEx();
						
						FormElement ele=new FormElement();
						ele.setValue(opttag.getAttribute("value"));
						ele.setLable(opttag.getAttribute("mobile"));
						subAttrs.add(ele);
						
						
						if (vv.toString().indexOf("selected") != -1)
							select_value = opttag.getOptionText();
					}
				}
				
				if (StringUtils.isNotEmpty(selectnode.getAttribute("data-options"))) {
					String[] property = selectnode.getAttribute("data-options").split(",");
					for (String item : property) {
						String[] arrt = item.split(":");
						formEle.getAttribute().put(arrt[0], arrt[1]);
					}
				}
				
				formEle.setSubAttributes(subAttrs);
				formEle.setName(selectnode.getAttribute("name"));
				formEle.setValue(select_value);
				formEle.setHtmlType(FormElement.HTML_TYPE_SELECT);
				formEle.setLable(selectnode.getAttribute("mobile"));
				//表单元素的ID
				formEle.setElementId(selectnode.getAttribute("id"));
				//单选钮点击时如果动态增加字段，则给动态字段赋值
				formEle.setAddField(Boolean.parseBoolean(selectnode.getAttribute("addField")));
				formEle.setRelateFieldId(selectnode.getAttribute("relateFieldId"));
				formEle.setRelateFieldName(selectnode.getAttribute("relateFieldName"));
				formEle.setRelateShowFieldValue(selectnode.getAttribute("relateShowFieldValue"));
				formEle.setRelateHideFieldValue(selectnode.getAttribute("relateHideFieldValue"));
				formEle.setRelateFieldisRequired(Boolean.parseBoolean(selectnode.getAttribute("relateFieldisRequired")));
				formEle.setUnit(selectnode.getAttribute("unit"));
				formEle.setMakethis_required_field_name(selectnode.getAttribute("makethis_required_field_name"));
				formEle.setMakethis_required_field_values(selectnode.getAttribute("makethis_required_field_values"));
				
			} else if (anode instanceof InputTag) {
				//文本框
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
				//表单元素的ID
				formEle.setElementId(inputnode.getAttribute("id"));
				
				//判断是否是取当前系统时间的字段
				if(inputnode.getAttribute("timeEditor")!=null&&!"".equals(inputnode.getAttribute("timeEditor").trim())){
					//1：自动获取当前时间，格式：yyyy-MM-dd HH:mm:ss
					//2：自动获取当前时间，格式：yyyy-MM-dd
					//3：手动输入当前时间，格式：yyyy-MM-dd HH:mm:ss
					//4：手动输入当前时间，格式：yyyy-MM-dd
					//5：手动输入当前时间，格式：HH:mm:ss
					
					formEle.setTimeEditor(Integer.parseInt(inputnode.getAttribute("timeEditor").trim()));
				}
				
				if (StringUtils.isNotEmpty(inputnode.getAttribute("readonly"))){
					formEle.setReadOnly(true);
				}
				if (StringUtils.isNotEmpty(inputnode.getAttribute("data-options"))) {
					String[] property = inputnode.getAttribute("data-options").split(",");
					for (String item : property) {
//						System.out.println("item--"+item);
						String[] arrt = item.split(":");
						formEle.getAttribute().put(arrt[0], arrt[1]);
					}
				}
				
				//手机端下一步骤选择特定的人(在mobileUrl.property文件配置)
				if(inputnode.getAttribute("selectUserUrl")!=null&&!"".equals(inputnode.getAttribute("selectUserUrl").trim())){
					
					formEle.setSelectUserUrl(inputnode.getAttribute("selectUserUrl").trim());
				}
				if(inputnode.getAttribute("nextAssigneeAttrName")!=null&&!"".equals(inputnode.getAttribute("nextAssigneeAttrName").trim())){
					
					formEle.setNextAssigneeAttrName(inputnode.getAttribute("nextAssigneeAttrName").trim());
				}
				
				//单选钮点击时如果动态增加字段，则给动态字段赋值
				formEle.setAddField(Boolean.parseBoolean(inputnode.getAttribute("addField")));
				formEle.setRelateFieldId(inputnode.getAttribute("relateFieldId"));
				formEle.setRelateFieldName(inputnode.getAttribute("relateFieldName"));
				formEle.setRelateShowFieldValue(inputnode.getAttribute("relateShowFieldValue"));
				formEle.setRelateHideFieldValue(inputnode.getAttribute("relateHideFieldValue"));
				formEle.setRelateFieldisRequired(Boolean.parseBoolean(inputnode.getAttribute("relateFieldisRequired")));
				formEle.setUnit(inputnode.getAttribute("unit"));
	
				formEle.setMakethis_required_field_name(inputnode.getAttribute("makethis_required_field_name"));
				formEle.setMakethis_required_field_values(inputnode.getAttribute("makethis_required_field_values"));
				
				 if(inputnode.getAttribute("isValidRange")!=null&&!"".equals(inputnode.getAttribute("isValidRange").trim())){
		            	formEle.setIsValidRange(Boolean.parseBoolean(inputnode.getAttribute("isValidRange")));
				}
				 
	            if(inputnode.getAttribute("maxValue")!=null&&!"".equals(inputnode.getAttribute("maxValue").trim())){
	            	formEle.setMaxValue(Integer.parseInt(inputnode.getAttribute("maxValue")));
				}
	            if(inputnode.getAttribute("minValue")!=null&&!"".equals(inputnode.getAttribute("minValue").trim())){
	            	formEle.setMinValue(Integer.parseInt(inputnode.getAttribute("minValue")));
				}
				
	            if(inputnode.getAttribute("maxFloatValue")!=null&&!"".equals(inputnode.getAttribute("maxFloatValue").trim())){
	            	formEle.setMaxFloatValue(Float.parseFloat(inputnode.getAttribute("maxFloatValue")));
				}
	            if(inputnode.getAttribute("minFloatValue")!=null&&!"".equals(inputnode.getAttribute("minFloatValue").trim())){
	            	formEle.setMinFloatValue(Float.parseFloat(inputnode.getAttribute("minFloatValue")));
				}
				
			} else if (anode instanceof TextareaTag) {
				//多行文本框
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
				
				//手机端下一步骤选择特定的人(在mobileUrl.property文件配置)
				if(textareaNode.getAttribute("selectUserUrl")!=null&&!"".equals(textareaNode.getAttribute("selectUserUrl").trim())){
					
					formEle.setSelectUserUrl(textareaNode.getAttribute("selectUserUrl").trim());
				}
				if(textareaNode.getAttribute("nextAssigneeAttrName")!=null&&!"".equals(textareaNode.getAttribute("nextAssigneeAttrName").trim())){
					
					formEle.setNextAssigneeAttrName(textareaNode.getAttribute("nextAssigneeAttrName").trim());
				}

				
				//单选钮点击时如果动态增加字段，则给动态字段赋值
				formEle.setAddField(Boolean.parseBoolean(textareaNode.getAttribute("addField")));
				formEle.setRelateFieldId(textareaNode.getAttribute("relateFieldId"));
				formEle.setRelateFieldName(textareaNode.getAttribute("relateFieldName"));
				formEle.setRelateShowFieldValue(textareaNode.getAttribute("relateShowFieldValue"));
				formEle.setRelateHideFieldValue(textareaNode.getAttribute("relateHideFieldValue"));
				formEle.setRelateFieldisRequired(Boolean.parseBoolean(textareaNode.getAttribute("relateFieldisRequired")));
				formEle.setUnit(textareaNode.getAttribute("unit"));
				
				formEle.setMakethis_required_field_name(textareaNode.getAttribute("makethis_required_field_name"));
				formEle.setMakethis_required_field_values(textareaNode.getAttribute("makethis_required_field_values"));

				
			}
			
			scanResult.add(formEle);
		}

		return scanResult;
	}
}
