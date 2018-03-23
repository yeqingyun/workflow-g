package com.gionee.gniflow.integration.dao;

import com.gionee.gniflow.biz.model.ReportRoleProcdef;

import java.util.List;
import java.util.Map;

public interface ReportRoleProcdefDao {

    int insert(ReportRoleProcdef reportRoleProcdef);

    int delete(Integer id);

    List<ReportRoleProcdef> getAll(Map<String, Object> param);

    ReportRoleProcdef get(Integer id);

}
