package com.gionee.gniflow.integration.dao;

import com.gionee.gniflow.biz.model.ReportRole;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface ReportRoleDao {

    int insert(ReportRole reportRole);

    int update(ReportRole reportRole);

    int delete(Integer id);

    List<ReportRole> getAll(Map<String, Object> param);

    List<ReportRole> getPage(Map<String, Object> param);
    int getPageCount(Map<String, Object> param);

    ReportRole get(Integer id);
    
    List<ReportRole> getUserRoles(Integer id);
    
    ReportRole getByRoleName(@Param(value="roleName") String roleName);
}
