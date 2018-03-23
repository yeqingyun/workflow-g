package com.gionee.gniflow.integration.dao;

import com.gionee.gniflow.biz.model.SapAccountRelation ;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface SapAccountRelationDao {

    int insert(SapAccountRelation  sapAccountRelation );

    int update(SapAccountRelation  sapAccountRelation );

    int logicDelete(@Param("sapAccount") String sapAccount);

    List<SapAccountRelation> getAll(Map<String, Object> param);

    List<SapAccountRelation> getPage(Map<String, Object> param);
    
    int getPageCount(Map<String, Object> param);

    SapAccountRelation get(@Param("sapAccount") String sapAccount);
    
    SapAccountRelation find(@Param("hrAccount") String hrAccount);

}
