package com.gionee.gniflow.groovy;

import java.util.ArrayList;
import java.util.List;

public class Process {
	String id = "";
	String label = "";
	String desc = "";
	List<Step> step = new ArrayList<Step>();
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
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public List<Step> getStep() {
		return step;
	}
	public void setStep(List<Step> step) {
		this.step = step;
	}
	
	
}
