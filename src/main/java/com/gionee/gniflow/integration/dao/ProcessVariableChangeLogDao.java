package com.gionee.gniflow.integration.dao;

import com.gionee.gniflow.biz.model.ProcessVariableChangeLog;

import java.util.List;
import java.util.Map;

public interface ProcessVariableChangeLogDao {

    int insert(ProcessVariableChangeLog processVariableChangeLog);

    int update(ProcessVariableChangeLog processVariableChangeLog);

    int delete(Integer id);

    List<ProcessVariableChangeLog> getPage(Map<String, Object> param);
    int getPageCount(Map<String, Object> param);

    ProcessVariableChangeLog get(Integer id);
    
    int updateHisProcessVar(ProcessVariableChangeLog processVariableChangeLog);
    
    int updateDetailProcessVar(ProcessVariableChangeLog processVariableChangeLog);
    
    int updateRunProcessVar(ProcessVariableChangeLog processVariableChangeLog);

}
