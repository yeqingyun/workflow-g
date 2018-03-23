package com.gionee.gniflow.biz.sfm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InnerDictionaryFileTree {

	private String id;
	private String text;
	private String state;
	private List<InnerDictionaryFileTree> children;
	private Map<String,Object> attributes = new HashMap<String, Object>();
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public List<InnerDictionaryFileTree> getChildren() {
		return children;
	}
	public void setChildren(List<InnerDictionaryFileTree> children) {
		this.children = children;
	}
	public void addChildren(InnerDictionaryFileTree children) {
		this.children.add(children);
	}
	public Map<String, Object> getAttributes() {
		return attributes;
	}
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	public void addAttribute(String key,Object value){
		this.attributes.put(key, value);
	}
	
	
}
