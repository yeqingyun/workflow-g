/**
 * @(#) FormElement.java Created on 2014年12月5日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.form;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class <code>FormElement</code>
 *
 * @author lipw
 * @version 1.0
 */
public class FormElement implements Serializable{
	
	private static final long serialVersionUID = -5690718691082872090L;
	
	public static final Integer HTML_TYPE_TEXT = 1;
	public static final Integer HTML_TYPE_SELECT = 2;
	public static final Integer HTML_TYPE_CHECKBOX = 3;
	public static final Integer HTML_TYPE_RADIOBOX = 4;
	public static final Integer HTML_TYPE_TEXTAREA = 5;
	public static final Integer HTML_TYPE_HIDDEN = 6;

	private String lable;
	
	private String name;
	
	private Integer htmlType;
	
	private String value;
	
	private String checked;
	
	private Boolean readOnly = false;
	
	private Integer timeEditor;
	
	private String selectUserUrl;//选人时增加选人的URL
	private String nextAssigneeAttrName;//选择办理人后赋值给对应的字段
	
	private Boolean addField = false;//单选钮选中后是否动态增加字段
	private String relateFieldId;//动态显示的字段名称对应的单选钮的字段ID
	private String relateFieldName;//动态显示的字段名称对应的单选钮的字段名
	
	private String relateShowFieldValue;//动态显示的字段名称对应的单选钮的字段值(值是一个范围，用,逗号分隔)
	private String relateHideFieldValue;//动态隐藏的字段名称对应的单选钮的字段值(值是一个范围，用,逗号分隔)
	
	private Boolean relateFieldisRequired = false;//动态显示的字段是否为必填项
	
	private String elementId;//表单元素的ID
	
	private String unit;//单位
	
	private Map<String, String> attribute = new HashMap<String, String>();
	
	private List<FormElement> subAttributes;

	private String makethis_required_field_name;//当前字段变成必填项的字段name
	private String makethis_required_field_values;//当当前字段变成必填项的字段的值,可以是一个范围，比如某几个选项选中时必填
	/**
	 * @return the lable
	 */
	
	private Boolean isValidRange;//是否校验值的范围
	private Integer maxValue;//最大值
	private Integer minValue;//最小值
	
	private Float maxFloatValue;//最大值浮点数
	private Float minFloatValue;//最小值浮点数
	
	public String getLable() {
		return lable;
	}

	/**
	 * @param lable the lable to set
	 */
	public void setLable(String lable) {
		this.lable = lable;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the htmlType
	 */
	public Integer getHtmlType() {
		return htmlType;
	}

	/**
	 * @param htmlType the htmlType to set
	 */
	public void setHtmlType(Integer htmlType) {
		this.htmlType = htmlType;
	}

	/**
	 * @return the readOnly
	 */
	public Boolean getReadOnly() {
		return readOnly;
	}

	/**
	 * @param readOnly the readOnly to set
	 */
	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the checked
	 */
	public String getChecked() {
		return checked;
	}

	/**
	 * @param checked the checked to set
	 */
	public void setChecked(String checked) {
		this.checked = checked;
	}

	/**
	 * @return the attribute
	 */
	public Map<String, String> getAttribute() {
		return attribute;
	}

	/**
	 * @param attribute the attribute to set
	 */
	public void setAttribute(Map<String, String> attribute) {
		this.attribute = attribute;
	}

	public Integer getTimeEditor() {
		return timeEditor;
	}

	public void setTimeEditor(Integer timeEditor) {
		this.timeEditor = timeEditor;
	}

	public String getSelectUserUrl() {
		return selectUserUrl;
	}

	public void setSelectUserUrl(String selectUserUrl) {
		this.selectUserUrl = selectUserUrl;
	}

	public String getNextAssigneeAttrName() {
		return nextAssigneeAttrName;
	}

	public void setNextAssigneeAttrName(String nextAssigneeAttrName) {
		this.nextAssigneeAttrName = nextAssigneeAttrName;
	}

	public Boolean getAddField() {
		return addField;
	}

	public void setAddField(Boolean addField) {
		this.addField = addField;
	}



	public String getRelateFieldId() {
		return relateFieldId;
	}

	public void setRelateFieldId(String relateFieldId) {
		this.relateFieldId = relateFieldId;
	}




	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	public String getRelateFieldName() {
		return relateFieldName;
	}

	public void setRelateFieldName(String relateFieldName) {
		this.relateFieldName = relateFieldName;
	}

	public String getRelateShowFieldValue() {
		return relateShowFieldValue;
	}

	public void setRelateShowFieldValue(String relateShowFieldValue) {
		this.relateShowFieldValue = relateShowFieldValue;
	}

	public String getRelateHideFieldValue() {
		return relateHideFieldValue;
	}

	public void setRelateHideFieldValue(String relateHideFieldValue) {
		this.relateHideFieldValue = relateHideFieldValue;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Boolean getRelateFieldisRequired() {
		return relateFieldisRequired;
	}

	public void setRelateFieldisRequired(Boolean relateFieldisRequired) {
		this.relateFieldisRequired = relateFieldisRequired;
	}

	public List<FormElement> getSubAttributes() {
		return subAttributes;
	}

	public void setSubAttributes(List<FormElement> subAttributes) {
		this.subAttributes = subAttributes;
	}

	public String getMakethis_required_field_name() {
		return makethis_required_field_name;
	}

	public void setMakethis_required_field_name(String makethis_required_field_name) {
		this.makethis_required_field_name = makethis_required_field_name;
	}

	public String getMakethis_required_field_values() {
		return makethis_required_field_values;
	}

	public void setMakethis_required_field_values(
			String makethis_required_field_values) {
		this.makethis_required_field_values = makethis_required_field_values;
	}

	public Integer getMaxValue() {
		return maxValue;
	}

	public Float getMaxFloatValue() {
		return maxFloatValue;
	}

	public void setMaxFloatValue(Float maxFloatValue) {
		this.maxFloatValue = maxFloatValue;
	}

	public Float getMinFloatValue() {
		return minFloatValue;
	}

	public void setMinFloatValue(Float minFloatValue) {
		this.minFloatValue = minFloatValue;
	}

	public void setMaxValue(Integer maxValue) {
		this.maxValue = maxValue;
	}

	public Integer getMinValue() {
		return minValue;
	}

	public void setMinValue(Integer minValue) {
		this.minValue = minValue;
	}




	public Boolean getIsValidRange() {
		return isValidRange;
	}

	public void setIsValidRange(Boolean isValidRange) {
		this.isValidRange = isValidRange;
	}

	




	
}
