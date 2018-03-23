package com.gionee.gniflow.integration.dao;

import com.gionee.gniflow.biz.model.SapReqSheetAttachment;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface SapReqSheetAttachmentDao {

    int logicDelete(@Param("preqNo") String preqNo, @Param("preqItem") Integer preqItem,
    		@Param("fileNo") String fileNo,
    		@Param("delflag") char delflag);

    List<SapReqSheetAttachment> getAll(Map<String, Object> param);

    List<SapReqSheetAttachment> getPage(Map<String, Object> param);
    
    Integer getPageCount(Map<String, Object> param);

    SapReqSheetAttachment get(@Param("preqNo") String preqNo, @Param("preqItem") Integer preqItem);

    int insert(SapReqSheetAttachment sapReqSheetAttachment);

    int update(SapReqSheetAttachment sapReqSheetAttachment);

}
