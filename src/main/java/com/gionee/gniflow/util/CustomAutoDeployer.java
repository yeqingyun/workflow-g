/**
 * @(#) CustomAutoDeployer.java Created on 2014年11月18日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.util;

import java.io.IOException;
import java.util.zip.ZipInputStream;

import javax.annotation.PostConstruct;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ContextResource;
import org.springframework.core.io.Resource;

import com.gionee.gniflow.web.cmd.HandleProcessCmd;

/**
 * The class <code>CustomAutoDeployer</code>
 *
 * @author lipw
 * @version 1.0
 */
public class CustomAutoDeployer {
	private Logger logger = LoggerFactory.getLogger(CustomAutoDeployer.class);
	
    private ProcessEngine processEngine;
    
    protected Resource[] deploymentResources = new Resource[0];

    @PostConstruct
    public void init() {
        if ((deploymentResources == null) || (deploymentResources.length == 0)) {
            return;
        }

        RepositoryService repositoryService = processEngine.getRepositoryService();

        for (Resource resource : deploymentResources) {
            String resourceName = null;

            if (resource instanceof ContextResource) {
                resourceName = ((ContextResource) resource)
                        .getPathWithinContext();
            } else if (resource instanceof ByteArrayResource) {
                resourceName = resource.getDescription();
            } else {
                try {
                    resourceName = resource.getFile().getAbsolutePath();
                } catch (IOException ex) {
                    logger.debug(ex.getMessage(), ex);
                    resourceName = resource.getFilename();
                }
            }

            try {
                DeploymentBuilder deploymentBuilder = repositoryService
                        .createDeployment().enableDuplicateFiltering()
                        .name(resourceName);

                if (resourceName.endsWith(".bar")
                        || resourceName.endsWith(".zip")
                        || resourceName.endsWith(".jar")) {
                    deploymentBuilder.addZipInputStream(new ZipInputStream(
                            resource.getInputStream()));
                } else {
                    deploymentBuilder.addInputStream(resourceName,
                            resource.getInputStream());
                }

                Deployment deployment = deploymentBuilder.deploy();
                logger.info("auto deploy : {}", resourceName);

                for (ProcessDefinition processDefinition : repositoryService
                        .createProcessDefinitionQuery()
                        .deploymentId(deployment.getId()).list()) {
                    this.handleProcessDefinition(processDefinition.getId());
                }
            } catch (IOException ex) {
                throw new ActivitiException("couldn't auto deploy resource '"
                        + resource + "': " + ex.getMessage(), ex);
            }
        }
    }

    public void handleProcessDefinition(String processDefinitionId) {
        processEngine.getManagementService().executeCommand(
                new HandleProcessCmd(processDefinitionId));
    }

    public void setProcessEngine(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    public void setDeploymentResources(Resource[] deploymentResources) {
        this.deploymentResources = deploymentResources;
    }
}
