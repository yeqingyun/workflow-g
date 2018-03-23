package com.gionee.gniflow.integration.dao;

import com.gionee.gniflow.biz.model.SapRequisitionSheet;
import com.gionee.gniflow.dto.SapRequisitionTaskDto;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface SapRequisitionSheetDao {

    int insert(SapRequisitionSheet sapRequisitionSheet);

    int update(SapRequisitionSheet sapRequisitionSheet);

    int logicDelete(@Param("preqNo") String preqNo, @Param("preqItem") Integer preqItem);

    List<SapRequisitionSheet> getAll(Map<String, Object> param);

    List<SapRequisitionSheet> getPage(Map<String, Object> param);
    
    Integer getPageCount(Map<String, Object> param);

    SapRequisitionSheet get(@Param("preqNo") String preqNo, @Param("preqItem") Integer preqItem);
    
	SapRequisitionSheet queryByProcInstId(@Param("procInstId") String procInstId);
	
	List<SapRequisitionSheet> queryPageSapReqSheet4HisProcess(Map<String, Object> param);

	Integer countSapReqSheet4HisProcess(Map<String, Object> param);
	
	List<SapRequisitionTaskDto> queryPageSapRequisitionSheet4UserTask(Map<String, Object> param);

	Integer countSapRequisitionSheet4UserTask(Map<String, Object> param);
	
	List<SapRequisitionSheet> querySapReqSheet4ProcurementInquiry(Map<String, Object> param);

	Integer countSapReqSheet4ProcurementInquiry(Map<String, Object> param);
}
