/**
 * @(#) SendEmailThread.java Created on 2014年8月13日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.notice;

import com.gionee.gniflow.web.util.SendMailUtil;

/**
 * The class <code>SendEmailThread</code>
 *
 * @author lipw
 * @version 1.0
 */
public class SendEmailThread extends Thread {
	private String toMailAddr;
	
	private String subject;
	
	private String message;

	public SendEmailThread(String toMailAddr, String subject, String message) {
		super();
		this.toMailAddr = toMailAddr;
		this.subject = subject;
		this.message = message;
	}

	@Override
	public void run() {
		SendMailUtil.sendCommonMail(toMailAddr, subject, message);
	}
	
}
