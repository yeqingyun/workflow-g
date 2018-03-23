package com.gionee.gniflow.web.cmd;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;

import com.gionee.gniflow.web.graph.ActivitiGraphBuilder;
import com.gionee.gniflow.web.graph.Graph;

public class FindGraphCmd implements Command<Graph> {
	
    private String processDefinitionId;

    public FindGraphCmd(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public Graph execute(CommandContext commandContext) {
        return new ActivitiGraphBuilder(processDefinitionId).build();
    }
}
