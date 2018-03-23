package com.gionee.gniflow.biz.model;

import com.gionee.gnif.model.base.BusinessObject;

public class BpmConfBase extends BusinessObject {

	private static final long serialVersionUID = -8390597399423136072L;
	
	private String processDefId;
    private String processDefKey;
    private Integer processDefVersion;

    public void setProcessDefId(String processDefId) {
        this.processDefId = processDefId;
    }
    public String getProcessDefId() {
        return processDefId;
    }
    public void setProcessDefKey(String processDefKey) {
        this.processDefKey = processDefKey;
    }
    public String getProcessDefKey() {
        return processDefKey;
    }
    public void setProcessDefVersion(Integer processDefVersion) {
        this.processDefVersion = processDefVersion;
    }
    public Integer getProcessDefVersion() {
        return processDefVersion;
    }

}
