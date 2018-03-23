package com.gionee.gniflow.integration.dao;


import java.util.Map;

public interface RapInfoDao {
	int insert(Map<String, String> map) ;
	
	int query(Map<String, String> map);
	
	int delete (String instanceID);
}
