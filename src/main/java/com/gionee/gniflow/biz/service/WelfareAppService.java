package com.gionee.gniflow.biz.service;

import java.util.Date;
import java.util.Map;

public interface WelfareAppService {
	int weddingIsRepeat(Map<String, String>map);
	int birthIsRepeat(Map<String, String>map);
	Date birthIsRepeatToAgain(Map<String, String>map);
	Date weddingIsRepeatToAgain(Map<String, String>map);
	
} 
