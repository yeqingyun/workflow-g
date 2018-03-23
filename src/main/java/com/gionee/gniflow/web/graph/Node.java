package com.gionee.gniflow.web.graph;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.impl.task.TaskDefinition;

public class Node extends GraphElement {
    private String type;
    private boolean active;
    private List<Edge> edges = new ArrayList<Edge>();
    
    //update 
    private String proInstId;
    
    private String processDefKey;
    private TaskDefinition taskDefinition;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

	public String getProInstId() {
		return proInstId;
	}

	public void setProInstId(String proInstId) {
		this.proInstId = proInstId;
	}

	public TaskDefinition getTaskDefinition() {
		return taskDefinition;
	}

	public void setTaskDefinition(TaskDefinition taskDefinition) {
		this.taskDefinition = taskDefinition;
	}

	public String getProcessDefKey() {
		return processDefKey;
	}

	public void setProcessDefKey(String processDefKey) {
		this.processDefKey = processDefKey;
	}
    
}
