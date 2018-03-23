/**
 * @(#) FormElement.java Created on 2014年12月5日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The class <code>FormElement</code>
 *
 * @author lipw
 * @version 1.0
 */
public class FormJson implements Serializable {
	
	private static final long serialVersionUID = 7083905145376212359L;

	private String formTitle;
	
	private String taskId;
	
	private Boolean handleFlag; 
	
	private List<FormElement> elements = new ArrayList<FormElement>();

	/**
	 * @return the formTitle
	 */
	public String getFormTitle() {
		return formTitle;
	}

	/**
	 * @param formTitle the formTitle to set
	 */
	public void setFormTitle(String formTitle) {
		this.formTitle = formTitle;
	}

	/**
	 * @return the elements
	 */
	public List<FormElement> getElements() {
		return elements;
	}

	/**
	 * @param elements the elements to set
	 */
	public void setElements(List<FormElement> elements) {
		this.elements = elements;
	}

	/**
	 * @return the taskId
	 */
	public String getTaskId() {
		return taskId;
	}

	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	/**
	 * @return the handleFlag
	 */
	public Boolean getHandleFlag() {
		return handleFlag;
	}

	/**
	 * @param handleFlag the handleFlag to set
	 */
	public void setHandleFlag(Boolean handleFlag) {
		this.handleFlag = handleFlag;
	}
	
}
