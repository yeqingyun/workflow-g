package org.activiti.engine.impl.bpmn.behavior;

import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.task.TaskDefinition;

public class GnifUserTaskActivityBehavior extends UserTaskActivityBehavior {

	private static final long serialVersionUID = 8676507617376428781L;
	
	private GnifBpmnActivityBehavior gnifBpmnActivityBehavior = new GnifBpmnActivityBehavior();

	public GnifUserTaskActivityBehavior(TaskDefinition taskDefinition) {
		super(taskDefinition);
	}

	
	protected void leave(ActivityExecution execution) {
		if (hasCompensationHandler(execution)) {
			createCompensateEventSubscription(execution);
		}
		if (!hasLoopCharacteristics()) {
			//Modify 
			gnifBpmnActivityBehavior.performDefaultOutgoingBehavior(execution);
		} else if (hasMultiInstanceCharacteristics()) {
			multiInstanceActivityBehavior.leave(execution);
		}
	}
}
