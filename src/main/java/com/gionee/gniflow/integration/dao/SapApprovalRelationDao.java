package com.gionee.gniflow.integration.dao;

import com.gionee.gniflow.biz.model.SapApprovalRelation ;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface SapApprovalRelationDao {

    int insert(SapApprovalRelation  sapApprovalRelation );

    int update(SapApprovalRelation  sapApprovalRelation );

    int logicDelete(@Param("approvalCode") String approvalCode);

    List<SapApprovalRelation > getAll(Map<String, Object> param);

    List<SapApprovalRelation > getPage(Map<String, Object> param);
    
    int getPageCount(Map<String, Object> param);

    SapApprovalRelation get(@Param("approvalCode") String approvalCode);
    
    String getApprovalCode(@Param("hrAccount") String hrAccount);
}
