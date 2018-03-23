/**
 * @(#) RfcSapReqRelease.java Created on Jul 24, 2014
 *
 * Copyright (c) 2014 GiONEE. All Rights Reserved
 */
package com.gionee.gniflow.sap.rfc;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;
/**
 * The class <code>RfcSapReqRelease</code> 调用SAP接口进行审批
 * 
 * @author lipw
 * @version 1.0
 */
public class RfcSapReqRelease {

	private static final Logger logger = LoggerFactory
			.getLogger(RfcSapReqRelease.class);

	/**
	 * 
	 * @param preqNo
	 *            采购申请号
	 * @param preqItem
	 *            采购申请行号
	 * @param relCode
	 *            审批代码
	 * @return
	 */
	public Map<String,String> sapReqSheetAudi(String preqNo, String preqItem,
			String relCode) {
		Map<String,String> errorMsgMap = new LinkedHashMap<String,String>();

		// 标名需要调用的Function
		JCoFunction function = RfcManager.getFunction("BAPI_REQUISITION_RELEASE");
		if (function == null){
			throw new RuntimeException("BAPI_COMPANYCODE_GETLIST not found in SAP.");
		}
		
		JCoParameterList input = function.getImportParameterList();
		input.setValue("NUMBER", preqNo);// 采购申请号
		input.setValue("ITEM", preqItem);// 采购申请行号
		input.setValue("REL_CODE", relCode);// 审批代码
		// 执行Function
		RfcManager.execute(function);

		// 从Function中取Table参数
		JCoParameterList tabput = function.getTableParameterList();
		JCoTable profTab = tabput.getTable("RETURN");

		if (profTab.getNumRows() == 0) {
			return null;
		}
		for (int i = 0; i < profTab.getNumRows(); i++) {
			String type = tabput.getString("TYPE");// E--错误类型
			if ("E".equalsIgnoreCase(type)) {
				errorMsgMap.put(tabput.getString("CODE"), tabput.getString("MESSAGE"));// code--message

				logger.error("CODE-->" + tabput.getString("CODE") + " || MESSAGE-->" + tabput.getString("MESSAGE"));
				logger.error("==================*******************===============================");
			}
		}
		return errorMsgMap;
	}

}
