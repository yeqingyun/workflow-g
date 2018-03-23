package com.gionee.gniflow.integration.dao;

import com.gionee.gniflow.biz.model.QuartzJobLog;

import java.util.List;
import java.util.Map;

public interface QuartzJobLogDao {

    int insert(QuartzJobLog quartzJobLog);

    int update(QuartzJobLog quartzJobLog);

    int delete(Integer id);

    List<QuartzJobLog> getAll(Map<String, Object> param);

    List<QuartzJobLog> getPage(Map<String, Object> param);
    
    int getPageCount(Map<String, Object> param);

    QuartzJobLog get(Integer id);

}
