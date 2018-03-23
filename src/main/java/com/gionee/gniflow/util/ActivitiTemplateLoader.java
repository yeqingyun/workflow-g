package com.gionee.gniflow.util;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ResourceEntity;

import freemarker.cache.ClassTemplateLoader;

/**
 * Created by doit on 2014/7/7.
 */
public class ActivitiTemplateLoader extends ClassTemplateLoader {
	
	public ActivitiTemplateLoader() {
		super(ActivitiTemplateLoader.class, "/");
	}
	
	@Override
    public Object findTemplateSource(String name) throws IOException {
        String[] names = name.split(";");
        if (names.length == 2) {
        	return this.getActivitiTemplateSource(name, names[0], names[1]);
        }
        else {
        	return super.findTemplateSource(name);
        }
    }

	private Object getActivitiTemplateSource(String name, String deploymentId, String formKey) {
        ResourceEntity resourceStream = Context
                .getCommandContext()
                .getResourceEntityManager()
                .findResourceByDeploymentIdAndResourceName(deploymentId, formKey);

        if (resourceStream == null) {
            throw new ActivitiObjectNotFoundException("Form with formKey '" + formKey + "' does not exist", String.class);
        }

        byte[] resourceBytes = resourceStream.getBytes();

        String encoding = "UTF-8";
        String formTemplateString = "";
        try {
            formTemplateString = new String(resourceBytes, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new ActivitiException("Unsupported encoding of :" + encoding, e);
        }
        return new ActivitiTemplateSource(name, formTemplateString, System.currentTimeMillis());
	}

	@Override
    public long getLastModified(Object templateSource) {
		if (templateSource instanceof ActivitiTemplateSource) {
			return ((ActivitiTemplateSource)templateSource).lastModified;
		}
		else {
			return super.getLastModified(templateSource);
		}
    }

    @Override
    public Reader getReader(Object templateSource, String encoding) throws IOException {
    	if (templateSource instanceof ActivitiTemplateSource) {
    		return new StringReader(((ActivitiTemplateSource)templateSource).source);
    	}
    	else {
    		return super.getReader(templateSource, encoding);
    	}
    }
    
    @Override
	public void closeTemplateSource(Object templateSource) throws IOException {
    	if (!(templateSource instanceof ActivitiTemplateSource)) {
    		super.closeTemplateSource(templateSource);
    	}
	}

	private static class ActivitiTemplateSource {
        private final String name;
        private final String source;
        private final long lastModified;

        ActivitiTemplateSource(String name, String source, long lastModified) {
            if (name == null) {
                throw new IllegalArgumentException("name == null");
            }
            if (source == null) {
                throw new IllegalArgumentException("source == null");
            }
            if (lastModified < -1L) {
                throw new IllegalArgumentException("lastModified < -1L");
            }
            this.name = name;
            this.source = source;
            this.lastModified = lastModified;
        }

        public boolean equals(Object obj) {
            if (obj instanceof ActivitiTemplateSource) {
                return name.equals(((ActivitiTemplateSource) obj).name);
            }
            return false;
        }

        public int hashCode() {
            return name.hashCode();
        }
    }
}
