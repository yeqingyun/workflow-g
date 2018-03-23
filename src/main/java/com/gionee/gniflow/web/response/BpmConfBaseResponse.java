package com.gionee.gniflow.web.response;

import com.gionee.gniflow.biz.model.BpmConfBase;

public class BpmConfBaseResponse {

    private Integer id;
    private String processDefId;
    private String processDefKey;
    private Integer processDefVersion;

    public BpmConfBaseResponse(BpmConfBase bpmConfBase) {
	    setId(bpmConfBase.getId());
	    setProcessDefId(bpmConfBase.getProcessDefId());
	    setProcessDefKey(bpmConfBase.getProcessDefKey());
	    setProcessDefVersion(bpmConfBase.getProcessDefVersion());
	}

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getId() {
        return id;
    }
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
