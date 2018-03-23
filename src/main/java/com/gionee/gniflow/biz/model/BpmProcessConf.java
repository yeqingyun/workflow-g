package com.gionee.gniflow.biz.model;

import com.gionee.gnif.model.base.BusinessObject;

public class BpmProcessConf extends BusinessObject {

	private static final long serialVersionUID = -849266087490521395L;

	private String processName;
	private String processDefKey;
	private Integer sort;
	private String categoryId;
	private Integer canRepeat;
	private Integer canShowInMobile;//可在手机端处理
	private String permissionCrowd;//权限人群

	public Integer getCanShowInMobile() {
		return canShowInMobile;
	}

	public void setCanShowInMobile(Integer canShowInMobile) {
		this.canShowInMobile = canShowInMobile;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessDefKey(String processDefKey) {
		this.processDefKey = processDefKey;
	}

	public String getProcessDefKey() {
		return processDefKey;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getSort() {
		return sort;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getCanRepeat() {
		return canRepeat;
	}

	public void setCanRepeat(Integer canRepeat) {
		this.canRepeat = canRepeat;
	}

	public String getPermissionCrowd() {
		return permissionCrowd;
	}

	public void setPermissionCrowd(String permissionCrowd) {
		this.permissionCrowd = permissionCrowd;
	}

}
