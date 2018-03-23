package com.gionee.gniflow.biz.model;

import com.gionee.gnif.model.base.BusinessObject;
import java.util.Date;


public class Project extends BusinessObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8957438803541967884L;
	//项目名称
	private String projectName;  
	//用户id
    private String userId;
    //用户名字
    private String userName;
    //创建时间
    private Date createtime;
    //创建人
    private String createby;
    //修改时间
    private Date updatetime;
    //修改人
    private String updateby;

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

}
