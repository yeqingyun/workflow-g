/**
 * @(#) RfcComTransferSyncData.java Created on 2015年1月8日
 *
 * Copyright (c) 2015 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.sap.rfc.comtransfer;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gniflow.sap.rfc.RfcManager;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;

/**
 * The class <code>RfcComTransferSyncData</code>
 *
 * @author lipw
 * @version 1.0
 */
public class RfcComTransferSyncData {
	
	private static final Logger logger = LoggerFactory.getLogger(RfcComTransferSyncData.class);
	
	public Map<String,String> sapReqSheetAudi(String preqNo, String preqItem,
			String relCode) {
		Map<String,String> errorMsgMap = new LinkedHashMap<String,String>();

		// 标名需要调用的Function
		JCoFunction function = RfcManager.getFunction("ZHR_OA12");//接口名
		if (function == null){
			logger.error("ZHR_OA12 not found in SAP.");
			throw new RuntimeException("ZHR_OA12 not found in SAP.");
		}
		
		JCoParameterList input = function.getImportParameterList();
		input.setValue("P_PERNR", preqNo);// 人员编号
		input.setValue("P_DATE", preqItem);// 日期和时间
		input.setValue("P_MASSG", relCode);// 操作原因
		input.setValue("P_PERSG", relCode);// 员工组
		input.setValue("P_PERSK", relCode);// 员工子组
		input.setValue("P_ABKRS", relCode);// 工资范围
		input.setValue("P_ANSVH", relCode);// 工作合同
		input.setValue("P_WERKS", relCode);// 人事范围
		input.setValue("P_BTRTL", relCode);// 人事子范围
		input.setValue("P_PLANS", relCode);// 职位
		
		// 执行Function
		RfcManager.execute(function);

		// 从Function中取Table参数
		JCoParameterList tabput = function.getTableParameterList();
		JCoTable profTab = tabput.getTable("RETURN");

		if (profTab.getNumRows() == 0) {
			return null;
		}
		for (int i = 0; i < profTab.getNumRows(); i++) {
			
		}
		return errorMsgMap;
	}
}
