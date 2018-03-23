/**
 * @(#) SapRequisitionTaskDto.java Created on 2014年7月24日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * The class <code>SapRequisitionTaskDto</code>
 * 
 * @author lipw
 * @version 1.0
 */
public class SapRequisitionProInstDto implements Serializable{

	private static final long serialVersionUID = -4715004246673700703L;
	// SapSapRequisitionSheet
	private String preqNo;
	private Integer preqItem;
	private String docType;
	private String purGroup;
	private String createdBy;
	private String preqName;
	private Date preqDate;
	private String shortText;
	private String material;
	private String plant;
	private String storeLoc;
	private String trackingno;
	private String matGrp;
	private BigDecimal quantity;
	private String unit;
	private Date delivDate;
	private Date relDate;
	private Integer grPrTime;
	private BigDecimal cAmtBapi;
	private BigDecimal priceUnit;
	private char itemCat;
	private char acctasscat;
	private Integer status;
	private String procInstId;
	private String procDefId;
	private char delflag;

	// HistoricProcessInstance
	protected String processInstanceId;
	protected Date startTime;
	protected Date endTime;
	protected Long durationInMillis;
	protected String deleteReason;

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

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Long getDurationInMillis() {
		return durationInMillis;
	}

	public void setDurationInMillis(Long durationInMillis) {
		this.durationInMillis = durationInMillis;
	}

	public String getDeleteReason() {
		return deleteReason;
	}

	public void setDeleteReason(String deleteReason) {
		this.deleteReason = deleteReason;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
