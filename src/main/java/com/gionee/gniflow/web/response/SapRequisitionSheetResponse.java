/**
 * @(#) SapRequisitionSheetResponse.java Created on 2014年10月27日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.response;

import java.math.BigDecimal;
import java.util.Date;

import com.gionee.gniflow.biz.model.SapRequisitionSheet;

/**
 * The class <code>SapRequisitionSheetResponse</code>
 *
 * @author lipw
 * @version 1.0
 */
public class SapRequisitionSheetResponse {
	
	private String preqNo;//采购申请编号
	private Integer preqItem;//采购申请的项目编号
	private String docType;//采购申请凭证类型
	private String purGroup;
	private String createdBy;//创建对象的人员名称 
	private String preqName;
	private Date preqDate;//需求日期
	private String shortText;//短文本
	private String material;//物料号
	private String plant;//工厂
	private String storeLoc;//库存地点
	private String trackingno;//需求跟踪号
	private String matGrp;//物料组
	private BigDecimal quantity;//采购申请数量
	private String unit;//采购申请计量单位
	private Date delivDate;//项目交货日期
	private Date relDate;//采购申请批准日期
	private Integer grPrTime;
	private BigDecimal cAmtBapi;//采购申请中的价格
	private BigDecimal priceUnit;//价格单位
	private char itemCat;//采购凭证中的项目类别
	private char acctasscat;//科目分配类别
	private String procInstId;
	private String procDefId;
	private char delflag;
	
	//补充字段
	private char approvalFlag;//批准标识
	private String approvalStrategy;//采购请求中的批准策略
	private String frgc1;//一级审批代码
	private String frgc2;//二级审批代码
	private String frgc3;//三级审批代码
	
	private Date changeDate;
	
	public SapRequisitionSheetResponse(SapRequisitionSheet sapRequisitionSheet){
		setPreqNo(sapRequisitionSheet.getPreqNo());
		setPreqItem(sapRequisitionSheet.getPreqItem());
		setDocType(sapRequisitionSheet.getDocType());
		setPurGroup(sapRequisitionSheet.getPurGroup());
		setCreatedBy(sapRequisitionSheet.getCreateBy());
		setPreqName(sapRequisitionSheet.getPreqName());
		setPreqDate(sapRequisitionSheet.getPreqDate());
		setShortText(sapRequisitionSheet.getShortText());
		setMaterial(sapRequisitionSheet.getMaterial());
		setPlant(sapRequisitionSheet.getPlant());
		setStoreLoc(sapRequisitionSheet.getStoreLoc());
		setTrackingno(sapRequisitionSheet.getTrackingno());
		setMatGrp(sapRequisitionSheet.getMatGrp());
		setQuantity(sapRequisitionSheet.getQuantity());
		setUnit(sapRequisitionSheet.getUnit());
		setDelivDate(sapRequisitionSheet.getDelivDate());
		setRelDate(sapRequisitionSheet.getRelDate());
		setGrPrTime(sapRequisitionSheet.getGrPrTime());
		setcAmtBapi(sapRequisitionSheet.getcAmtBapi());
		setPriceUnit(sapRequisitionSheet.getPriceUnit());
		setItemCat(sapRequisitionSheet.getItemCat());
		setAcctasscat(sapRequisitionSheet.getAcctasscat());
		setProcInstId(sapRequisitionSheet.getProcInstId());
		setProcDefId(sapRequisitionSheet.getProcDefId());
		setDelflag(sapRequisitionSheet.getDelflag());
		setApprovalFlag(sapRequisitionSheet.getApprovalFlag());
		setApprovalStrategy(sapRequisitionSheet.getApprovalStrategy());
		setFrgc1(sapRequisitionSheet.getFrgc1());
		setFrgc2(sapRequisitionSheet.getFrgc2());
		setFrgc3(sapRequisitionSheet.getFrgc3());
		setChangeDate(sapRequisitionSheet.getChangeDate());
	}

	public String getPreqNo() {
		return preqNo;
	}

	public void setPreqNo(String preqNo) {
		this.preqNo = preqNo;
	}

	public Integer getPreqItem() {
		return preqItem;
	}

	public void setPreqItem(Integer preqItem) {
		this.preqItem = preqItem;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getPurGroup() {
		return purGroup;
	}

	public void setPurGroup(String purGroup) {
		this.purGroup = purGroup;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getPreqName() {
		return preqName;
	}

	public void setPreqName(String preqName) {
		this.preqName = preqName;
	}

	public Date getPreqDate() {
		return preqDate;
	}

	public void setPreqDate(Date preqDate) {
		this.preqDate = preqDate;
	}

	public String getShortText() {
		return shortText;
	}

	public void setShortText(String shortText) {
		this.shortText = shortText;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getPlant() {
		return plant;
	}

	public void setPlant(String plant) {
		this.plant = plant;
	}

	public String getStoreLoc() {
		return storeLoc;
	}

	public void setStoreLoc(String storeLoc) {
		this.storeLoc = storeLoc;
	}

	public String getTrackingno() {
		return trackingno;
	}

	public void setTrackingno(String trackingno) {
		this.trackingno = trackingno;
	}

	public String getMatGrp() {
		return matGrp;
	}

	public void setMatGrp(String matGrp) {
		this.matGrp = matGrp;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Date getDelivDate() {
		return delivDate;
	}

	public void setDelivDate(Date delivDate) {
		this.delivDate = delivDate;
	}

	public Date getRelDate() {
		return relDate;
	}

	public void setRelDate(Date relDate) {
		this.relDate = relDate;
	}

	public Integer getGrPrTime() {
		return grPrTime;
	}

	public void setGrPrTime(Integer grPrTime) {
		this.grPrTime = grPrTime;
	}

	public BigDecimal getcAmtBapi() {
		return cAmtBapi;
	}

	public void setcAmtBapi(BigDecimal cAmtBapi) {
		this.cAmtBapi = cAmtBapi;
	}

	public BigDecimal getPriceUnit() {
		return priceUnit;
	}

	public void setPriceUnit(BigDecimal priceUnit) {
		this.priceUnit = priceUnit;
	}

	public char getItemCat() {
		return itemCat;
	}

	public void setItemCat(char itemCat) {
		this.itemCat = itemCat;
	}

	public char getAcctasscat() {
		return acctasscat;
	}

	public void setAcctasscat(char acctasscat) {
		this.acctasscat = acctasscat;
	}

	public String getProcInstId() {
		return procInstId;
	}

	public void setProcInstId(String procInstId) {
		this.procInstId = procInstId;
	}

	public String getProcDefId() {
		return procDefId;
	}

	public void setProcDefId(String procDefId) {
		this.procDefId = procDefId;
	}

	public char getDelflag() {
		return delflag;
	}

	public void setDelflag(char delflag) {
		this.delflag = delflag;
	}

	public char getApprovalFlag() {
		return approvalFlag;
	}

	public void setApprovalFlag(char approvalFlag) {
		this.approvalFlag = approvalFlag;
	}

	public String getApprovalStrategy() {
		return approvalStrategy;
	}

	public void setApprovalStrategy(String approvalStrategy) {
		this.approvalStrategy = approvalStrategy;
	}

	public String getFrgc1() {
		return frgc1;
	}

	public void setFrgc1(String frgc1) {
		this.frgc1 = frgc1;
	}

	public String getFrgc2() {
		return frgc2;
	}

	public void setFrgc2(String frgc2) {
		this.frgc2 = frgc2;
	}

	public String getFrgc3() {
		return frgc3;
	}

	public void setFrgc3(String frgc3) {
		this.frgc3 = frgc3;
	}

	public Date getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}
	
}
