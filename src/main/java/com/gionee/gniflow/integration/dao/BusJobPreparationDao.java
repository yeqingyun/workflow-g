package com.gionee.gniflow.integration.dao;

import com.gionee.gniflow.biz.model.BusJobPreparation;

import java.util.List;
import java.util.Map;

public interface BusJobPreparationDao {

    int insert(BusJobPreparation busJobPreparation);

    int update(BusJobPreparation busJobPreparation);

    int delete(Integer id);

    List<BusJobPreparation> getAll(Map<String, Object> param);

    List<BusJobPreparation> getPage(Map<String, Object> param);
    
    int getPageCount(Map<String, Object> param);

    BusJobPreparation get(Integer id);
    
    /**
     * 根据部门ID统计岗位编制信息
     * @param orgId
     * @return
     */
    List<BusJobPreparation> statBusJobPreparation(Integer orgId);
    
	List<BusJobPreparation> getJob(String deptId);

}
