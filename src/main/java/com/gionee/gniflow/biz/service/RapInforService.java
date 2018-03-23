package com.gionee.gniflow.biz.service;

import java.util.Map;

import com.gionee.gniflow.biz.model.RapInfo;

public interface RapInforService {
    void insert(Map<String, String> map) ;
    
    int query(Map<String, String> map);
    
    void delete (String instanceID);
}  
