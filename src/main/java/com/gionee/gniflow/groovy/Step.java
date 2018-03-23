package com.gionee.gniflow.groovy;

import java.util.ArrayList;
import java.util.List;

public class Step {
	String id ="";
	String label = "";
	String type="";
	String key = "";
	List<Field> field = new ArrayList<Field>();
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public List<Field> getField() {
		return field;
	}
	public void setField(List<Field> field) {
		this.field = field;
	}
	
	
}
