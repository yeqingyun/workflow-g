package com.gionee.gniflow.web.response;

import com.gionee.gniflow.biz.model.Project;
import java.util.Date;


public class ProjectResponse {

    private Integer id;
    private String projectName;
    private String userId;
    private String userName;
    private Date createtime;
    private String createby;
    private Date updatetime;
    private String updateby;
    private Integer status;

    public ProjectResponse(Project project) {
	    setId(project.getId());
	    setProjectName(project.getProjectName());
	    setUserId(project.getUserId());
	    setUserName(project.getUserName());
	    setCreatetime(project.getCreatetime());
	    setCreateby(project.getCreateby());
	    setUpdatetime(project.getUpdatetime());
	    setUpdateby(project.getUpdateby());
	    setStatus(project.getStatus());
	}

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getId() {
        return id;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    public String getProjectName() {
        return projectName;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserName() {
        return userName;
    }
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
    public Date getCreatetime() {
        return createtime;
    }
    public void setCreateby(String createby) {
        this.createby = createby;
    }
    public String getCreateby() {
        return createby;
    }
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
    public Date getUpdatetime() {
        return updatetime;
    }
    public void setUpdateby(String updateby) {
        this.updateby = updateby;
    }
    public String getUpdateby() {
        return updateby;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Integer getStatus() {
        return status;
    }

}
