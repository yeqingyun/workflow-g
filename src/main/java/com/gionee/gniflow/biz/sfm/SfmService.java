package com.gionee.gniflow.biz.sfm;

import java.util.List;
import java.util.Map;

import com.gionee.gniflow.biz.service.impl.sfm.Supplier;
import com.gionee.gniflow.web.easyui.AjaxJson;

public interface SfmService {
	
	List<Supplier> getSuppliersInfo(Integer page, Integer rows);

	int getCount();

	Supplier getSupplierByEmail(String email);

	AjaxJson addSupplier(Supplier supplier);
	
}
