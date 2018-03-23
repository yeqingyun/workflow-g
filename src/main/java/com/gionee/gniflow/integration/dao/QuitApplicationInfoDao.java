package com.gionee.gniflow.integration.dao;

import com.gionee.gniflow.biz.model.QuitApplicationInfo;

import java.util.List;
import java.util.Map;

public interface QuitApplicationInfoDao {



    int insert(QuitApplicationInfo quitApplicationInfo);

    int update(QuitApplicationInfo quitApplicationInfo);

    int delete(Integer id);

    List<QuitApplicationInfo> getAll(Map<String, Object> param);

    List<QuitApplicationInfo> getPage(Map<String, Object> param);
    int getPageCount(Map<String, Object> param);

    QuitApplicationInfo get(Integer id);

}
