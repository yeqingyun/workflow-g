package com.gionee.gniflow.web.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.form.FormData;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.rest.service.api.form.FormDataResponse;
import org.activiti.rest.service.api.form.RestEnumFormProperty;
import org.activiti.rest.service.api.form.RestFormProperty;

import com.gionee.gnif.constant.TreeConstant;
import com.gionee.gnif.dto.TreeDto;
import com.gionee.gnif.util.AppContext;

public class JsonResponseFactory {
	
	public static FormDataResponse createFormDataResponse(FormData formData) {
		FormDataResponse result = new FormDataResponse();
		result.setDeploymentId(formData.getDeploymentId());
		result.setFormKey(formData.getFormKey());
		if (formData.getFormProperties() != null) {
			for (FormProperty formProp : formData.getFormProperties()) {
		        RestFormProperty restFormProp = new RestFormProperty();
		        restFormProp.setId(formProp.getId());
		        restFormProp.setName(formProp.getName());
		        if (formProp.getType() != null) {
					restFormProp.setType(formProp.getType().getName());
		        }
		        restFormProp.setValue(formProp.getValue());
		        restFormProp.setReadable(formProp.isReadable());
		        restFormProp.setRequired(formProp.isRequired());
		        restFormProp.setWritable(formProp.isWritable());
		        if ("enum".equals(restFormProp.getType())) {
					Object values = formProp.getType().getInformation("values");
					if (values != null) {
						@SuppressWarnings("unchecked")
							Map<String, String> enumValues = (Map<String, String>) values;
						for (String enumId : enumValues.keySet()) {
							RestEnumFormProperty enumProperty = new RestEnumFormProperty();
							enumProperty.setId(enumId);
							enumProperty.setName(enumValues.get(enumId));
							restFormProp.addEnumValue(enumProperty);
						}
					}
		        } else if ("date".equals(restFormProp.getType())) {
					restFormProp.setDatePattern((String) formProp.getType().getInformation("datePattern"));
		        } else if ("curUserNm".equals(restFormProp.getType())) {
		        	restFormProp.setValue(AppContext.getCurrentAppUser().getName());
		        } else if ("curUserId".equals(restFormProp.getType())) {
		        	restFormProp.setValue(AppContext.getCurrentAppUser().getAccount());
		        } else if (restFormProp.getType().startsWith("comboTree")) {
		        	restFormProp.setType("comboTree");
		        	//url
		        	restFormProp.setDatePattern((String)formProp.getType().getInformation("url"));
		        }
		        result.addFormProperty(restFormProp);
			}
		}
		if (formData instanceof StartFormData) {
			StartFormData startFormData = (StartFormData) formData;
			if (startFormData.getProcessDefinition() != null) {
		        result.setProcessDefinitionId(startFormData.getProcessDefinition().getId());
			}
		} else if (formData instanceof TaskFormData) {
			TaskFormData taskFormData = (TaskFormData) formData;
			if (taskFormData.getTask() != null) {
		        result.setTaskId(taskFormData.getTask().getId());
			}
		}
		return result;
	}
	
	public static List<TreeDto> createProcessDefinitions(List<ProcessDefinition> processDefinitions) {
		List<TreeDto> result = new ArrayList<TreeDto>();
		for (ProcessDefinition processDefinition:processDefinitions) {
			TreeDto treeDto = new TreeDto();
			treeDto.setText(processDefinition.getName());
			treeDto.setValue(processDefinition.getId());
			treeDto.setState(TreeConstant.STATE_OPEN);
			result.add(treeDto);
		}
		return result;
	}

}
