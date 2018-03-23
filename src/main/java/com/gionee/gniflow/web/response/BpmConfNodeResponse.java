package com.gionee.gniflow.web.response;

import com.gionee.gniflow.biz.model.BpmConfNode;

public class BpmConfNodeResponse {

    private Integer id;
    private String code;
    private String name;
    private String type;
    private Integer priority;
    private String assignee;
    private String dueDate;
    private Integer confBaseId;

    public BpmConfNodeResponse(BpmConfNode bpmConfNode) {
	    setId(bpmConfNode.getId());
	    setCode(bpmConfNode.getCode());
	    setName(bpmConfNode.getName());
	    setType(bpmConfNode.getType());
	    setPriority(bpmConfNode.getPriority());
	    setAssignee(bpmConfNode.getAssignee());
	    setDueDate(bpmConfNode.getDueDate());
	    setConfBaseId(bpmConfNode.getConfBaseId());
	}

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getId() {
        return id;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
    public void setPriority(Integer priority) {
        this.priority = priority;
    }
    public Integer getPriority() {
        return priority;
    }
    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }
    public String getAssignee() {
        return assignee;
    }
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
    public String getDueDate() {
        return dueDate;
    }
    public void setConfBaseId(Integer confBaseId) {
        this.confBaseId = confBaseId;
    }
    public Integer getConfBaseId() {
        return confBaseId;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
