package com.gionee.gniflow.web.listener;

import java.util.Collections;
import java.util.List;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class ProxyTaskListener implements TaskListener {

	private static final long serialVersionUID = 5438244430434635896L;
	
	@SuppressWarnings("unchecked")
	private List<TaskListener> taskListeners = Collections.EMPTY_LIST;

    public void notify(DelegateTask delegateTask) {
        for (TaskListener taskListener : taskListeners) {
            taskListener.notify(delegateTask);
        }
    }

    public void setTaskListeners(List<TaskListener> taskListeners) {
        this.taskListeners = taskListeners;
    }
}