package com.gionee.gniflow.integration.dao;

import com.gionee.gniflow.biz.model.ReportUserRole;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface ReportUserRoleDao {

    int insert(ReportUserRole reportUserRole);

    int delete(Integer id);
    
    int deleteByRoleId(Integer id);

    List<ReportUserRole> getAll(Map<String, Object> param);
    
    List<String> getUserProcessDefKey(@Param("userId") Integer userId);

}
