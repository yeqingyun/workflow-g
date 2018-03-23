package com.gionee.gniflow.integration.dao;

import com.gionee.gniflow.biz.model.HardwareType;

import java.util.List;
import java.util.Map;

public interface HardwareTypeDao {

    int insert(HardwareType hardwareType);

    int update(HardwareType hardwareType);

    int delete(Integer id);

    List<HardwareType> getAllById(Integer id);

    List<HardwareType> getPageById(Integer id);
    int getPageCountById(Integer id);

    List<HardwareType> getAll(Map<String, Object> param);
    
    List<HardwareType>  getPage(Map<String, Object> param);
    int getPageCount(Map<String, Object> param);
    
    
    HardwareType getById(Integer id);

}
