/**
 * @(#) HistoryJsonResponseFactory.java Created on 2014年4月23日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.util;

import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.FormProperty;
import org.activiti.bpmn.model.FormValue;
import org.activiti.rest.service.api.form.RestEnumFormProperty;
import org.activiti.rest.service.api.form.RestFormProperty;

import com.gionee.gniflow.dto.HisNodeFromDataResponse;
import com.gionee.gniflow.dto.MyFlowElement;

/**
 * The class <code>HistoryJsonResponseFactory</code>
 *
 * @author lipw
 * @version 1.0
 */
public class HistoryJsonResponseFactory {

	public static HisNodeFromDataResponse createFormDataResponse(MyFlowElement element, Map<String, Object> variable) {
		HisNodeFromDataResponse result = new HisNodeFromDataResponse();
		result.setFormKey(element.getFormKey());
		result.setActivityId(element.getId());
		result.setActivityName(element.getName());
		if (element.getFormProperties() != null) {
			for (FormProperty formProp : element.getFormProperties()) {
		        RestFormProperty restFormProp = new RestFormProperty();
		        restFormProp.setId(formProp.getId());
		        restFormProp.setName(formProp.getName());
		        if (formProp.getType() != null) {
					restFormProp.setType(formProp.getType());
		        }
		        restFormProp.setValue((String)variable.get(formProp.getId()));
		        restFormProp.setReadable(formProp.isReadable());
		        restFormProp.setRequired(formProp.isRequired());
		        restFormProp.setWritable(formProp.isWriteable());
		        if ("enum".equals(restFormProp.getType())) {
					Object values = formProp.getFormValues();
					if (values != null) {
						@SuppressWarnings("unchecked")
							List<FormValue> formValues = (List<FormValue>) values;
						for (FormValue fv : formValues) {
							RestEnumFormProperty enumProperty = new RestEnumFormProperty();
							enumProperty.setId(fv.getId());
							enumProperty.setName(fv.getName());
							restFormProp.addEnumValue(enumProperty);
						}
					}
		        } else if ("date".equals(restFormProp.getType())) {
					restFormProp.setDatePattern(formProp.getDatePattern());
		        } else if (restFormProp.getType().startsWith("comboTree")) {
		        	restFormProp.setType("comboTree");
		        	//url
		        	restFormProp.setDatePattern(formProp.getDatePattern());
		        }
		        result.addFormProperty(restFormProp);
			}
		}
		return result;
	}
}
