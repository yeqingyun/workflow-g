package com.gionee.gniflow.integration.dao;

import com.gionee.gniflow.biz.model.BpmUserRole;
import com.gionee.gniflow.web.response.BpmUserRoleResponse;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface BpmUserRoleDao {

    int insert(BpmUserRole bpmUserRole);

    int delete(Map<String, Object> param);
    
    int deleteByRoleId(Integer id);

    List<BpmUserRole> getAll(Map<String, Object> param);
    
    List<BpmUserRoleResponse> getPage(Map<String, Object> param);
    
    int getPageCount(Map<String, Object> param);
    
    List<String> getUserProcessDefKey(@Param("userId") Integer userId);

}
