package com.gionee.gniflow.biz.service;

import java.util.List;
import java.util.Map;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gniflow.biz.model.BusJobPreparation;
import com.gionee.gniflow.web.easyui.ComboBoxData;

public interface BusJobPreparationService {

	void save(BusJobPreparation busJobPreparation);

	BusJobPreparation getBusJobPreparation(Integer id);

	void delete(Integer id);

	List<BusJobPreparation> query(QueryMap critera);

	PageResult<BusJobPreparation> queryPage(QueryMap critera);
	
	void handleBusJobPrepara(List<Map<String,String>> lists) throws Exception;
	
    List<BusJobPreparation> statBusJobPreparation(Integer orgId);
    
    List<ComboBoxData> getJobInfoFromDept(String deptId);
    
}
