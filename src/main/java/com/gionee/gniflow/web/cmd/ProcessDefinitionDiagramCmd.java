package com.gionee.gniflow.web.cmd;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.activiti.engine.impl.cmd.GetDeploymentProcessDefinitionCmd;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;

public class ProcessDefinitionDiagramCmd implements Command<InputStream> {
    protected String processDefinitionId;

    public ProcessDefinitionDiagramCmd(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

	public InputStream execute(CommandContext commandContext) {
//        GetBpmnModelCmd getBpmnModelCmd = new GetBpmnModelCmd(
//                processDefinitionId);
//        BpmnModel bpmnModel = getBpmnModelCmd.execute(commandContext);

//        InputStream is = ProcessDiagramGenerator.generateDiagram(bpmnModel,
//                "png", Collections.EMPTY_LIST);
        
        ProcessDefinitionEntity definition = new GetDeploymentProcessDefinitionCmd(
                processDefinitionId).execute(commandContext);
        String diagramResourceName = definition.getDiagramResourceName();
        String deploymentId = definition.getDeploymentId();
        byte[] bytes = commandContext.getResourceEntityManager()
                .findResourceByDeploymentIdAndResourceName(deploymentId,
                        diagramResourceName).getBytes();
        InputStream is = new ByteArrayInputStream(bytes);
        return is;
    }
}
