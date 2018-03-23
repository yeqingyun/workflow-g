package com.gionee.gniflow.integration.dao;

import com.gionee.gniflow.biz.model.BpmConfBase;

import java.util.List;
import java.util.Map;

public interface BpmConfBaseDao {

    int insert(BpmConfBase bpmConfBase);

    int update(BpmConfBase bpmConfBase);

    int delete(Integer id);

    List<BpmConfBase> getAll(Map<String, Object> param);

    List<BpmConfBase> getPage(Map<String, Object> param);
    int getPageCount(Map<String, Object> param);

    BpmConfBase get(Integer id);

}
