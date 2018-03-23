package com.gionee.gniflow.integration.dao;

import com.gionee.gniflow.biz.model.BpmTaskExecution;

import java.util.List;
import java.util.Map;

public interface BpmTaskExecutionDao {

    int insert(BpmTaskExecution bpmTaskExecution);

    int update(BpmTaskExecution bpmTaskExecution);

    int delete(Integer id);

    List<BpmTaskExecution> getAll(Map<String, Object> param);

    List<BpmTaskExecution> getPage(Map<String, Object> param);
    
    int getPageCount(Map<String, Object> param);

    BpmTaskExecution get(Integer id);
    
    List<BpmTaskExecution> getTruntodoInf(String procInstId);

}
