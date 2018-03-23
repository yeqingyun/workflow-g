package com.gionee.gniflow.integration.dao;

import java.util.Date;
import java.util.Map;

public interface WelfareAppDao {
	int weddingIsRepeat(Map<String, String>map);
	int birthIsRepeat(Map<String, String>map);
	Date birthIsRepeatToAgain(Map<String, String>map);
	Date weddingIsRepeatToAgain(Map<String, String>map);
	
}
