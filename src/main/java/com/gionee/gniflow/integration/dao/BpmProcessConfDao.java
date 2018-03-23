package com.gionee.gniflow.integration.dao;

import com.gionee.gniflow.biz.model.BpmProcessConf;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface BpmProcessConfDao {

    int insert(BpmProcessConf bpmProcessSort);

    int update(BpmProcessConf bpmProcessSort);

    int delete(Integer id);

    List<BpmProcessConf> getAll(Map<String, Object> param);

    List<BpmProcessConf> getPage(Map<String, Object> param);
    
    int getPageCount(Map<String, Object> param);

    BpmProcessConf get(Integer id);
    
    BpmProcessConf getByDefKey(@Param("proDefKey") String proDefKey);
    
    String getAuthority(String processName);
}
