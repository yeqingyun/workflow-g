package com.gionee.gniflow.integration.dao;

import com.gionee.gniflow.biz.model.BpmEmailTemplate;

import java.util.List;
import java.util.Map;

public interface BpmEmailTemplateDao {

    int insert(BpmEmailTemplate bpmEmailTemplate);

    int update(BpmEmailTemplate bpmEmailTemplate);

    int delete(Integer id);

    List<BpmEmailTemplate> getAll(Map<String, Object> param);

    List<BpmEmailTemplate> getPage(Map<String, Object> param);
    int getPageCount(Map<String, Object> param);

    BpmEmailTemplate get(Integer id);

}
