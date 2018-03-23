package com.gionee.gniflow.integration.dao;

import com.gionee.gniflow.biz.model.BpmConfProcessRole;

import java.util.List;
import java.util.Map;

public interface BpmConfProcessRoleDao {

    int insert(BpmConfProcessRole bpmConfProcessRole);

    int update(BpmConfProcessRole bpmConfProcessRole);

    int delete(Integer id);

    List<BpmConfProcessRole> getAll(Map<String, Object> param);

    List<BpmConfProcessRole> getPage(Map<String, Object> param);
    
    int getPageCount(Map<String, Object> param);

    BpmConfProcessRole get(Integer id);
    
    BpmConfProcessRole getSingleBpmConfProcessRole(Map<String, Object> param);
}
