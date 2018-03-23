package com.gionee.gniflow.web.response;

import com.gionee.gniflow.biz.model.BpmProcessConf;

public class BpmProcessConfResponse {

    private Integer id;
    private String processName;
    private String processDefKey;
    private Integer sort;
    private String categoryId;
    private Integer canRepeat;
    private String permissionCrowd;
    private Integer canShowInMobile;

    public BpmProcessConfResponse(BpmProcessConf bpmProcessConf) {
	    setId(bpmProcessConf.getId());
	    setProcessName(bpmProcessConf.getProcessName());
	    setProcessDefKey(bpmProcessConf.getProcessDefKey());
	    setSort(bpmProcessConf.getSort());
	    setCategoryId(bpmProcessConf.getCategoryId());
	    setCanRepeat(bpmProcessConf.getCanRepeat());
	    setCanShowInMobile(bpmProcessConf.getCanShowInMobile());
	    setPermissionCrowd(bpmProcessConf.getPermissionCrowd());
	}

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getId() {
        return id;
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
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
    public String getCategoryId() {
        return categoryId;
    }
    public void setCanRepeat(Integer canRepeat) {
        this.canRepeat = canRepeat;
    }
    public Integer getCanRepeat() {
        return canRepeat;
    }

	public Integer getCanShowInMobile() {
		return canShowInMobile;
	}

	public void setCanShowInMobile(Integer canShowInMobile) {
		this.canShowInMobile = canShowInMobile;
	}

	public String getPermissionCrowd() {
		return permissionCrowd;
	}

	public void setPermissionCrowd(String permissionCrowd) {
		this.permissionCrowd = permissionCrowd;
	}

}
