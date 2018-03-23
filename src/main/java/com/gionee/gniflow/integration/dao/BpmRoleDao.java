package com.gionee.gniflow.integration.dao;

import com.gionee.gniflow.biz.model.BpmRole;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface BpmRoleDao {

    int insert(BpmRole bpmRole);

    int update(BpmRole bpmRole);

    int delete(Integer id);

    List<BpmRole> getAll(Map<String, Object> param);

    List<BpmRole> getPage(Map<String, Object> param);
    int getPageCount(Map<String, Object> param);

    BpmRole get(Integer id);
    
    List<BpmRole> getUserRoles(Integer id);
    
    BpmRole getByRoleName(@Param(value="roleName") String roleName);

	List<BpmRole> getReportUserRoles(Integer id);
}
