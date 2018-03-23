package com.gionee.gniflow.biz.model;

import java.io.Serializable;

public class SapApprovalRelation implements Serializable {

	private static final long serialVersionUID = -6312707039982540090L;

	private String approvalCode;
	private String hrAccount;
	private char delflag;

	public void setApprovalCode(String approvalCode) {
		this.approvalCode = approvalCode;
	}

	public String getApprovalCode() {
		return approvalCode;
	}

	public void setHrAccount(String hrAccount) {
		this.hrAccount = hrAccount;
	}

	public String getHrAccount() {
		return hrAccount;
	}

	public void setDelflag(char delflag) {
		this.delflag = delflag;
	}

	public char getDelflag() {
		return delflag;
	}

}
