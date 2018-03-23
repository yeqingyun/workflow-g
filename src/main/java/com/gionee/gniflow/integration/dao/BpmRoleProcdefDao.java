package com.gionee.gniflow.integration.dao;

import com.gionee.gniflow.biz.model.BpmRoleProcdef;

import java.util.List;
import java.util.Map;

public interface BpmRoleProcdefDao {

    int insert(BpmRoleProcdef bpmRoleProcdef);

    int delete(Integer id);

    List<BpmRoleProcdef> getAll(Map<String, Object> param);

    BpmRoleProcdef get(Integer id);

}
