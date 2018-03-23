package com.gionee.gniflow.web.listener;


import org.activiti.engine.delegate.DelegateTask;

import com.gionee.gniflow.web.notice.ArrivalNotice;
import com.gionee.gniflow.web.notice.CompleteNotice;
import com.gionee.gniflow.web.support.DefaultTaskListener;

public class NoticeTaskListener extends DefaultTaskListener {
	
	private static final long serialVersionUID = -3316306690856310447L;

    private ArrivalNotice arrivalNotice = new ArrivalNotice();
    
    private CompleteNotice completeNotice = new CompleteNotice();

    @Override
    public void onCreate(DelegateTask delegateTask) throws Exception {
        arrivalNotice.process(delegateTask);
    }

    @Override
    public void onComplete(DelegateTask delegateTask) throws Exception {
        completeNotice.process(delegateTask);
    }

}
