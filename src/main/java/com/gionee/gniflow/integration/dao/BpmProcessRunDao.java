package com.gionee.gniflow.integration.dao;

import com.gionee.gniflow.biz.model.BpmProcessRun;

import java.util.List;
import java.util.Map;

public interface BpmProcessRunDao {

    int insert(BpmProcessRun bpmProcessRun);

    int update(BpmProcessRun bpmProcessRun);

    int delete(Integer id);

    List<BpmProcessRun> getAll(Map<String, Object> param);

    List<BpmProcessRun> getPage(Map<String, Object> param);
    
    int getPageCount(Map<String, Object> param);

    BpmProcessRun get(String procInstId);

}
