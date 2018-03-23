package com.gionee.gniflow.integration.dao;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.model.BpmCommCode;

import java.util.List;
import java.util.Map;

public interface BpmCommCodeDao {

    int insert(BpmCommCode bpmCommCode);

    int update(BpmCommCode bpmCommCode);

    int delete(Integer id);

    List<BpmCommCode> getAll(Map<String, Object> param);

    List<BpmCommCode> getPage(Map<String, Object> param);
    int getPageCount(Map<String, Object> param);

    BpmCommCode get(Integer id);
    
    List<BpmCommCode> loadSysLike(Map<String, Object> param);

}
