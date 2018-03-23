package com.gionee.gniflow.integration.dao;

import com.gionee.gniflow.biz.model.TypeDetail;

import java.util.List;
import java.util.Map;

public interface TypeDetailDao {

    int insert(TypeDetail typeDetail);

    int update(TypeDetail typeDetail);

    int delete(Integer id);

    List<TypeDetail> getAllById(Map<String, Object> map);

    List<TypeDetail> getPageById(Map<String, Object> map);
    int getPageCountById(Map<String, Object> map);


    List<TypeDetail>  getPage(Map<String, Object> param);
    int getPageCount(Map<String, Object> param);
    
    
    
    TypeDetail getById(Integer id);

	TypeDetail get(Integer id);

}
