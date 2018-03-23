/**
 * @(#) RfcSapReqSheet.java Created on Jul 24, 2014
 *
 * Copyright (c) 2014 GiONEE. All Rights Reserved
 */
package com.gionee.gniflow.sap.rfc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gniflow.biz.model.SapRequisitionSheet;
import com.gionee.gniflow.constant.WebReqConstant;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;
/**
 * The class <code>RfcSapReqSheet</code>
 * 从SAP获取采购申请单
 * @author lipw
 * @version 1.0
 */
public class RfcSapReqSheet {
	
	private static final Logger logger = LoggerFactory.getLogger(RfcSapReqSheet.class);
	
	private List<SapRequisitionSheet> sapReqSheets4ReqDate = new ArrayList<SapRequisitionSheet>();
	
	private List<SapRequisitionSheet> sapReqSheets4ChangeDate = new ArrayList<SapRequisitionSheet>();
	
	/**
	 * 根据请求时间区间取数据
	 * @return
	 */
	public List<SapRequisitionSheet> getSapReqSheets4ReqDate() {
		// 连接SAP系统的数据信息（Client、连接用户名、密码、登录语言、IP地址、实例编号）
		JCoFunction function = RfcManager.getFunction("ZGNMM05F01");
		if (function == null){
			throw new RuntimeException("BAPI_COMPANYCODE_GETLIST not found in SAP.");
		}
		JCoParameterList input = function.getImportParameterList();
		// Function的输入参数 USERNAME 赋值为 AC_ZHANGJIE
		input.setValue("I_BSART", "ZR02");//采购单类型
		input.setValue("I_FRGKZ", "X");//审批标识
//		input.setValue("I_DATEF", this.getYesStartDate());//需求开始时间
//		input.setValue("I_DATET", this.getYesEndDate());//需求结束时间
		// 执行Function
		RfcManager.execute(function);

		// 从Function中取Table参数
		JCoParameterList tabput = function.getTableParameterList();
		JCoTable profTab = tabput.getTable("T_MM05F01");
		
		SapRequisitionSheet sapReqSheet = null;
		for (int i = 0; i < profTab.getNumRows(); i++) {
			
			sapReqSheet= new SapRequisitionSheet();
			
			sapReqSheet.setPreqNo(profTab.getString("BANFN"));//采购申请编号
			sapReqSheet.setPreqItem(profTab.getInt("BNFPO"));//采购申请的项目编号 
			sapReqSheet.setDocType(profTab.getString("BSART"));//采购申请凭证类型
			//少了一个批准标识
			sapReqSheet.setCreatedBy(profTab.getString("ERNAM"));//创建对象的人员名称 
			sapReqSheet.setPreqDate(profTab.getDate("BADAT"));//需求日期
			sapReqSheet.setShortText(profTab.getString("TXZ01"));//短文本 
			if (!StringUtils.isEmpty(profTab.getString("MATNR"))) {
				sapReqSheet.setMaterial(profTab.getString("MATNR").substring(9));//物料号
			}
			sapReqSheet.setPlant(profTab.getString("WERKS"));//工厂
			sapReqSheet.setStoreLoc(profTab.getString("LGORT"));//库存地点
			sapReqSheet.setTrackingno(profTab.getString("BEDNR"));//需求跟踪号
			sapReqSheet.setMatGrp(profTab.getString("MATKL"));//物料组
			sapReqSheet.setQuantity(new BigDecimal(profTab.getDouble("MENGE")));//采购申请数量
			sapReqSheet.setUnit(profTab.getString("MEINS"));//采购申请计量单位
			sapReqSheet.setDelivDate(profTab.getDate("LFDAT"));//项目交货日期
			sapReqSheet.setRelDate(profTab.getDate("FRGDT"));//采购申请批准日期
			sapReqSheet.setcAmtBapi(new BigDecimal(decimalFormat(profTab.getDouble("PREIS"))));//采购申请中的价格
			sapReqSheet.setPriceUnit(new BigDecimal(profTab.getString("PEINH")));//价格单位
			sapReqSheet.setItemCat(profTab.getChar("PSTYP"));//采购凭证中的项目类别
			sapReqSheet.setAcctasscat(profTab.getChar("KNTTP"));//科目分配类别
			sapReqSheet.setApprovalStrategy(profTab.getString("FRGST"));//采购批准策略
			sapReqSheet.setFrgc1(profTab.getString("FRGC1"));//一级审批代码
			sapReqSheet.setFrgc2(profTab.getString("FRGC2"));//二级审批代码
			sapReqSheet.setFrgc3(profTab.getString("FRGC3"));//三级审批代码
			
			sapReqSheet.setChangeDate(profTab.getDate("ERDAT"));
			
			//设置状态
			sapReqSheet.setStatus(WebReqConstant.PROCURE_SHEET_TOBEAUDI);//设置为待审核
			sapReqSheet.setDelflag(WebReqConstant.DEL_NO);//删除标识
			
			logger.error("BANFN-->" + profTab.getString("BANFN") +" || BNFPO-->"+ profTab.getInt("BNFPO") +" || "
					+ "BSART-->" + profTab.getString("BSART") + " || FRGKZ-->" +profTab.getString("FRGKZ") +" || "
					+ "ERNAM-->" + profTab.getString("ERNAM") + "|| BADAT-->" + profTab.getDate("BADAT") +" || "
					+ "TXZ01-->" + profTab.getString("TXZ01") +" || MATNR-->"+ profTab.getString("MATNR") + " || " 
					+ "WERKS-->" + profTab.getString("WERKS") +" || LGORT-->"+ profTab.getString("LGORT") + "||"
					+ "BEDNR-->" + profTab.getString("BEDNR") + "|| MATKL-->" + profTab.getString("MATKL") +" || "
					+ "MENGE-->" + profTab.getDouble("MENGE") +" || MEINS-->"+ profTab.getString("MEINS") + " || " 
					+ "LFDAT-->" + profTab.getDate("LFDAT") + "|| FRGDT-->" + profTab.getDate("FRGDT") +" || "
					+ "PREIS-->" + profTab.getDouble("PREIS") +" || PEINH-->"+ profTab.getString("PEINH") + " || " 
					+ "PSTYP-->" + profTab.getChar("PSTYP") + " || KNTTP-->" +profTab.getChar("KNTTP") +" || "
					+ "FRGST-->" + profTab.getString("FRGST") +" || FRGC1-->"+ profTab.getString("FRGC1") + " || " 
					+ "ERDAT-->" + profTab.getDate("ERDAT")
					+ "FRGC2-->" + profTab.getShort("FRGC2") + " || FRGC3-->"+ profTab.getString("FRGC3"));
			logger.error("==================*******************===============================");
			
			sapReqSheets4ReqDate.add(sapReqSheet);
			
			profTab.nextRow();
		}
		return sapReqSheets4ReqDate;
	}
	
	/**
	 * 根据更改时间区间取数据
	 * @return
	 */
	public List<SapRequisitionSheet> getSapReqSheets4ChangeDate() {
		// 连接SAP系统的数据信息（Client、连接用户名、密码、登录语言、IP地址、实例编号）
		JCoFunction function = RfcManager.getFunction("ZGNMM05F01");
		if (function == null){
			throw new RuntimeException("BAPI_COMPANYCODE_GETLIST not found in SAP.");
		}
		// 标名需要调用的Function
		JCoParameterList input = function.getImportParameterList();
		// Function的输入参数 USERNAME 赋值为 AC_ZHANGJIE
		input.setValue("I_BSART", "ZR02");//采购单类型
		input.setValue("I_FRGKZ", "X");//审批标识
//		input.setValue("I_ERDAF", this.getYesStartDate());//需求开始时间
//		input.setValue("I_ERDAT", this.getYesEndDate());//需求结束时间
		// 执行Function
		RfcManager.execute(function);

		// 从Function中取Table参数
		JCoParameterList tabput = function.getTableParameterList();
		JCoTable profTab = tabput.getTable("T_MM05F01");
		
		SapRequisitionSheet sapReqSheet = null;
		for (int i = 0; i < profTab.getNumRows(); i++) {
			
			sapReqSheet= new SapRequisitionSheet();
			
			sapReqSheet.setPreqNo(profTab.getString("BANFN"));//采购申请编号
			sapReqSheet.setPreqItem(profTab.getInt("BNFPO"));//采购申请的项目编号 
			sapReqSheet.setDocType(profTab.getString("BSART"));//采购申请凭证类型
			//少了一个批准标识
			sapReqSheet.setCreatedBy(profTab.getString("ERNAM"));//创建对象的人员名称 
			sapReqSheet.setPreqDate(profTab.getDate("BADAT"));//需求日期
			sapReqSheet.setShortText(profTab.getString("TXZ01"));//短文本 
			if (!StringUtils.isEmpty(profTab.getString("MATNR"))) {
				sapReqSheet.setMaterial(profTab.getString("MATNR").substring(9));//物料号
			}
			sapReqSheet.setPlant(profTab.getString("WERKS"));//工厂
			sapReqSheet.setStoreLoc(profTab.getString("LGORT"));//库存地点
			sapReqSheet.setTrackingno(profTab.getString("BEDNR"));//需求跟踪号
			sapReqSheet.setMatGrp(profTab.getString("MATKL"));//物料组
			sapReqSheet.setQuantity(new BigDecimal(profTab.getDouble("MENGE")));//采购申请数量
			sapReqSheet.setUnit(profTab.getString("MEINS"));//采购申请计量单位
			sapReqSheet.setDelivDate(profTab.getDate("LFDAT"));//项目交货日期
			sapReqSheet.setRelDate(profTab.getDate("FRGDT"));//采购申请批准日期
			sapReqSheet.setcAmtBapi(new BigDecimal(decimalFormat(profTab.getDouble("PREIS"))));//采购申请中的价格
			sapReqSheet.setPriceUnit(new BigDecimal(profTab.getString("PEINH")));//价格单位
			sapReqSheet.setItemCat(profTab.getChar("PSTYP"));//采购凭证中的项目类别
			sapReqSheet.setAcctasscat(profTab.getChar("KNTTP"));//科目分配类别
			sapReqSheet.setApprovalStrategy(profTab.getString("FRGST"));//采购批准策略
			sapReqSheet.setFrgc1(profTab.getString("FRGC1"));//一级审批代码
			sapReqSheet.setFrgc2(profTab.getString("FRGC2"));//二级审批代码
			sapReqSheet.setFrgc3(profTab.getString("FRGC3"));//三级审批代码
			
			sapReqSheet.setChangeDate(profTab.getDate("ERDAT"));
			
			//设置状态
			sapReqSheet.setStatus(WebReqConstant.PROCURE_SHEET_TOBEAUDI);//设置为待审核
			sapReqSheet.setDelflag(WebReqConstant.DEL_NO);//删除标识
			
			logger.error("BANFN-->" + profTab.getString("BANFN") +" || BNFPO-->"+ profTab.getInt("BNFPO") +" || "
					+ "BSART-->" + profTab.getString("BSART") + " || FRGKZ-->" +profTab.getString("FRGKZ") +" || "
					+ "ERNAM-->" + profTab.getString("ERNAM") + "|| BADAT-->" + profTab.getDate("BADAT") +" || "
					+ "TXZ01-->" + profTab.getString("TXZ01") +" || MATNR-->"+ profTab.getString("MATNR") + " || " 
					+ "WERKS-->" + profTab.getString("WERKS") +" || LGORT-->"+ profTab.getString("LGORT") + "||"
					+ "BEDNR-->" + profTab.getString("BEDNR") + "|| MATKL-->" + profTab.getString("MATKL") +" || "
					+ "MENGE-->" + profTab.getDouble("MENGE") +" || MEINS-->"+ profTab.getString("MEINS") + " || " 
					+ "LFDAT-->" + profTab.getDate("LFDAT") + "|| FRGDT-->" + profTab.getDate("FRGDT") +" || "
					+ "PREIS-->" + profTab.getDouble("PREIS") +" || PEINH-->"+ profTab.getString("PEINH") + " || " 
					+ "PSTYP-->" + profTab.getChar("PSTYP") + " || KNTTP-->" +profTab.getChar("KNTTP") +" || "
					+ "FRGST-->" + profTab.getString("FRGST") +" || FRGC1-->"+ profTab.getString("FRGC1") + " || " 
					+ "ERDAT-->" + profTab.getDate("ERDAT")
					+ "FRGC2-->" + profTab.getShort("FRGC2") + " || FRGC3-->"+ profTab.getString("FRGC3"));
			logger.error("==================*******************===============================");
			
			sapReqSheets4ChangeDate.add(sapReqSheet);
			
			profTab.nextRow();
		}
		return sapReqSheets4ChangeDate;
	}
	
	/**
	 * 去掉重复对象
	 * @param listOne
	 * @param two
	 * @return
	 */
	public List<SapRequisitionSheet> removeDuplicateItem(List<SapRequisitionSheet> listOne, List<SapRequisitionSheet> listTwo){
		//以车号作为key  
		Map<String, SapRequisitionSheet> sapReqSheetMap = new HashMap<String, SapRequisitionSheet>();
		for (SapRequisitionSheet tempSapReq : listOne) {
			sapReqSheetMap.put(tempSapReq.getPreqNo() + "_" + tempSapReq.getPreqItem(), tempSapReq);
		}
		for (SapRequisitionSheet tempSapReq : listTwo) {
			if (sapReqSheetMap.containsKey(tempSapReq.getPreqNo() + "_" + tempSapReq.getPreqItem())) {
				//sapReqSheetMap.remove(tempSapReq.getPreqNo() + "_" + tempSapReq.getPreqItem());// 移除键值为车号相等的对象
				continue;// 停止并返回继续循环
			}
			sapReqSheetMap.put(tempSapReq.getPreqNo() + "_" + tempSapReq.getPreqItem(), tempSapReq);
		}
		List<SapRequisitionSheet> result = new ArrayList<SapRequisitionSheet>(sapReqSheetMap.values());//转为List
		return result;
	}
	
	/**
	 * 获取昨天的开始时间
	 * @return
	 */
	private Date getYesStartDate(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);  
		calendar.set(Calendar.MINUTE, 0);  
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}
	/**
	 * 获取昨天的结束时间
	 * @return
	 */
	private Date getYesEndDate(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 23);  
		calendar.set(Calendar.MINUTE, 59);  
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}
	
	private String decimalFormat(Double value){
		 return new java.text.DecimalFormat("#.0000").format(value);
	}
}
