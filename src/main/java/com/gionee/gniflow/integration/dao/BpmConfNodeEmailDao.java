package com.gionee.gniflow.integration.dao;

import com.gionee.gniflow.biz.model.BpmConfNodeEmail;

import java.util.List;
import java.util.Map;

public interface BpmConfNodeEmailDao {

    int insert(BpmConfNodeEmail bpmConfNodeEmail);

    int update(BpmConfNodeEmail bpmConfNodeEmail);

    int delete(Integer id);

    List<BpmConfNodeEmail> getAll(Map<String, Object> param);

    List<BpmConfNodeEmail> getPage(Map<String, Object> param);
    int getPageCount(Map<String, Object> param);

    BpmConfNodeEmail get(Integer id);

}
