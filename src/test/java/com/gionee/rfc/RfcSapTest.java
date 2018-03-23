/**
 * @(#) RfcSapTest.java C
reated on 2015年1月15日
 *
 * Copyright (c) 2015 GIONEE. All Rights Reserved
 */
package com.gionee.rfc;

import org.junit.Assert;
import org.junit.Test;

import com.gionee.gniflow.sap.rfc.RfcManager;
import com.sap.conn.jco.JCoFunction;

/**
 * The class <code>RfcSapTest</code>
 *
 * @author lipw
 * @version 1.0
 */
public class RfcSapTest {
	
	@Test
	public void testRfcComTransferSyncData() {
		JCoFunction function = RfcManager.getFunction("ZHR_OA12");//接口名
		Assert.assertNotNull(function);
	}
	@Test
	public void testRfcComTransferSyncData1() {
		JCoFunction function = RfcManager.getFunction("ZHR_OA13");//接口名
		Assert.assertNotNull(function);
	}
}
