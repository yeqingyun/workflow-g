package com.gionee.gniflow.biz.model;

import com.gionee.gnif.model.base.BusinessObject;

public class HardwareType extends BusinessObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String typeName;
    private String description;

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    public String getTypeName() {
        return typeName;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }

}
