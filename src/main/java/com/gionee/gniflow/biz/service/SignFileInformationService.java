package com.gionee.gniflow.biz.service;

import java.util.List;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.model.SignFileInformation;

public interface SignFileInformationService {

	void save(SignFileInformation signFileInformation);

	SignFileInformation getSignFileInformation(Integer id);

	void logicDelete(Integer id);
	
	List<SignFileInformation> querySignFileInformation(QueryMap queryMap);
	
	SignFileInformation getMaxSignFile(QueryMap queryMap);

}
