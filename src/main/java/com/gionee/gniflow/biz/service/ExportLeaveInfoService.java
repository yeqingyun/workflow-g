package com.gionee.gniflow.biz.service;

import java.util.List;
import java.util.Map;


public interface ExportLeaveInfoService {
	
	List<Map> queryLeaveInfo(Map<String, Object> map);

	List<Map> query(Map<String, Object> hashMap);

}
