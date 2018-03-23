package com.gionee.gniflow.util;

import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.impl.bpmn.behavior.ExclusiveGatewayActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.GnifEndEventBehavior;
import org.activiti.engine.impl.bpmn.behavior.GnifExclusiveGatewayActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.GnifStartEventBehavior;
import org.activiti.engine.impl.bpmn.behavior.GnifUserTaskActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.NoneEndEventActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.NoneStartEventActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.bpmn.parser.factory.DefaultActivityBehaviorFactory;
import org.activiti.engine.impl.task.TaskDefinition;

public class GnifActivityBehaviorFactory extends DefaultActivityBehaviorFactory {

	@Override
	public UserTaskActivityBehavior createUserTaskActivityBehavior(
			UserTask userTask, TaskDefinition taskDefinition) {
		return new GnifUserTaskActivityBehavior(taskDefinition);
	}

	@Override
	public ExclusiveGatewayActivityBehavior createExclusiveGatewayActivityBehavior(
			ExclusiveGateway exclusiveGateway) {
		return new GnifExclusiveGatewayActivityBehavior();
	}

	/* (non-Javadoc)
	 * @see org.activiti.engine.impl.bpmn.parser.factory.DefaultActivityBehaviorFactory#createNoneEndEventActivityBehavior(org.activiti.bpmn.model.EndEvent)
	 */
	@Override
	public NoneEndEventActivityBehavior createNoneEndEventActivityBehavior(
			EndEvent endEvent) {
		return new GnifEndEventBehavior();
	}

	/* (non-Javadoc)
	 * @see org.activiti.engine.impl.bpmn.parser.factory.DefaultActivityBehaviorFactory#createNoneStartEventActivityBehavior(org.activiti.bpmn.model.StartEvent)
	 */
	@Override
	public NoneStartEventActivityBehavior createNoneStartEventActivityBehavior(
			StartEvent startEvent) {
		return new GnifStartEventBehavior();
	}
	
	
}
