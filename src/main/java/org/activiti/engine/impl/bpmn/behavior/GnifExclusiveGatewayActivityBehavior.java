/**
 * @(#) GnifExclusiveGatewayActivityBehavior.java Created on 2014年11月14日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package org.activiti.engine.impl.bpmn.behavior;

import java.util.Iterator;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.Condition;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gniflow.constant.BPMConstant;

/**
 * The class <code>GnifExclusiveGatewayActivityBehavior</code>
 * 
 * @author lipw
 * @version 1.0
 */
public class GnifExclusiveGatewayActivityBehavior extends
		ExclusiveGatewayActivityBehavior {

	private static final long serialVersionUID = 1919675713787932787L;

	private static Logger log = LoggerFactory
			.getLogger(GnifExclusiveGatewayActivityBehavior.class);

	@Override
	protected void leave(ActivityExecution execution) {

		if (log.isDebugEnabled()) {
			log.debug("Leaving activity '{}'", execution.getActivity().getId());
		}

		PvmTransition outgoingSeqFlow = null;
		String defaultSequenceFlow = (String) execution.getActivity().getProperty("default");
		//
		Boolean ignoreCondtion =  (Boolean) execution.getActivity().getProperty(BPMConstant.SKIP_TASK_IGNOR_CONDTION_KEY);
		if(ignoreCondtion == null){
			ignoreCondtion = false;
		}
		
		Iterator<PvmTransition> transitionIterator = execution.getActivity()
				.getOutgoingTransitions().iterator();
		if (!ignoreCondtion) {
			while (outgoingSeqFlow == null && transitionIterator.hasNext()) {
				PvmTransition seqFlow = transitionIterator.next();

				Condition condition = (Condition) seqFlow.getProperty(BpmnParse.PROPERTYNAME_CONDITION);
				if ((condition == null && (defaultSequenceFlow == null || !defaultSequenceFlow
						.equals(seqFlow.getId())))
						|| (condition != null && condition.evaluate(execution))) {
					if (log.isDebugEnabled()) {
						log.debug("Sequence flow '{}'selected as outgoing sequence flow.", seqFlow.getId());
					}
					outgoingSeqFlow = seqFlow;
				}
			}
		}
		

		if (outgoingSeqFlow != null) {
			execution.take(outgoingSeqFlow);
		} else {

			if (defaultSequenceFlow != null) {
				PvmTransition defaultTransition = execution.getActivity()
						.findOutgoingTransition(defaultSequenceFlow);
				if (defaultTransition != null) {
					execution.take(defaultTransition);
				} else {
					throw new ActivitiException("Default sequence flow '"
							+ defaultSequenceFlow + "' not found");
				}
			} else {
				// No sequence flow could be found, not even a default one
				throw new ActivitiException(
						"No outgoing sequence flow of the exclusive gateway '"
								+ execution.getActivity().getId()
								+ "' could be selected for continuing the process");
			}
		}
	}

}
