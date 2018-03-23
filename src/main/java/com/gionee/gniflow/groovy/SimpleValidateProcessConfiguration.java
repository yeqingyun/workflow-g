package com.gionee.gniflow.groovy;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ContextResource;
import org.springframework.core.io.Resource;

public class SimpleValidateProcessConfiguration {
	
	private Logger logger = LoggerFactory.getLogger(SimpleValidateProcessConfiguration.class);
	protected Resource[] validateResources = new Resource[0];
	private SimpleValidateProcessEngine simpleValidateProcessEngine;

    @PostConstruct
    public void init() {
        if ((validateResources == null) || (validateResources.length == 0)) {
            return;
        }
        for (Resource resource : validateResources) {
            String resourceName = null;
            if (resource instanceof ContextResource) {
                resourceName = ((ContextResource) resource).getPathWithinContext();
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
            simpleValidateProcessEngine.buildProcess(resourceName);

        }
    }

	public Resource[] getValidateResources() {
		return validateResources;
	}

	public void setValidateResources(Resource[] validateResources) {
		this.validateResources = validateResources;
	}

	public SimpleValidateProcessEngine getSimpleValidateProcessEngine() {
		return simpleValidateProcessEngine;
	}

	public void setSimpleValidateProcessEngine(SimpleValidateProcessEngine simpleValidateProcessEngine) {
		this.simpleValidateProcessEngine = simpleValidateProcessEngine;
	}
    
    
}
