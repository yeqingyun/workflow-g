/**
 * @(#) GnifBpmnActivityBehavior.java Created on 2014年11月14日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package org.activiti.engine.impl.bpmn.behavior;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.Condition;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.pvm.runtime.InterpretableExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gniflow.constant.BPMConstant;

/**
 * The class <code>GnifBpmnActivityBehavior</code>
 *
 * @author lipw
 * @version 1.0
 */
public class GnifBpmnActivityBehavior extends BpmnActivityBehavior implements Serializable {
	  
	private static final long serialVersionUID = -4852733238005681408L;
	
	private static Logger log = LoggerFactory.getLogger(GnifBpmnActivityBehavior.class);
	/**
	   * Actual implementation of leaving an activity.
	   * 
	   * @param execution
	   *          The current execution context
	   * @param checkConditions
	   *          Whether or not to check conditions before determining whether or
	   *          not to take a transition.
	   * @param throwExceptionIfExecutionStuck
	   *          If true, an {@link ActivitiException} will be thrown in case no
	   *          transition could be found to leave the activity.
	   */
	  @Override
	  protected void performOutgoingBehavior(ActivityExecution execution, 
	          boolean checkConditions, boolean throwExceptionIfExecutionStuck, List<ActivityExecution> reusableExecutions) {

	    if (log.isDebugEnabled()) {
	      log.debug("Leaving activity '{}'", execution.getActivity().getId());
	    }

	    String defaultSequenceFlow = (String) execution.getActivity().getProperty("default");
	    List<PvmTransition> transitionsToTake = new ArrayList<PvmTransition>();
	    
	    //是否忽略条件
	    Boolean ignoreCondtion =  (Boolean) execution.getActivity().getProperty(BPMConstant.SKIP_TASK_IGNOR_CONDTION_KEY);
	    if(ignoreCondtion == null){
			ignoreCondtion = false;
		}
	    
	    List<PvmTransition> outgoingTransitions = execution.getActivity().getOutgoingTransitions();
	    if (!ignoreCondtion) {
	    	for (PvmTransition outgoingTransition : outgoingTransitions) {
	  	      if (defaultSequenceFlow == null || !outgoingTransition.getId().equals(defaultSequenceFlow)) {
	  	        Condition condition = (Condition) outgoingTransition.getProperty(BpmnParse.PROPERTYNAME_CONDITION);
	  	        if (condition == null || !checkConditions || condition.evaluate(execution)) {
	  	          transitionsToTake.add(outgoingTransition);
	  	        }
	  	      }
	  	    }
	    }

	    if (transitionsToTake.size() == 1) {
	      
	      execution.take(transitionsToTake.get(0));

	    } else if (transitionsToTake.size() >= 1) {

	      execution.inactivate();
	      if (reusableExecutions == null || reusableExecutions.isEmpty()) {
	        execution.takeAll(transitionsToTake, Arrays.asList(execution));
	      } else {
	        execution.takeAll(transitionsToTake, reusableExecutions);
	      }

	    } else {

	      if (defaultSequenceFlow != null) {
	        PvmTransition defaultTransition = execution.getActivity().findOutgoingTransition(defaultSequenceFlow);
	        if (defaultTransition != null) {
	          execution.take(defaultTransition);
	        } else {
	          throw new ActivitiException("Default sequence flow '" + defaultSequenceFlow + "' could not be not found");
	        }
	      } else {
	        
	        Object isForCompensation = execution.getActivity().getProperty(BpmnParse.PROPERTYNAME_IS_FOR_COMPENSATION);
	        if(isForCompensation != null && (Boolean) isForCompensation) {
	          
	          InterpretableExecution parentExecution = (InterpretableExecution) execution.getParent();
	          ((InterpretableExecution)execution).remove();
	          parentExecution.signal("compensationDone", null);            
	          
	        } else {
	          
	          if (log.isDebugEnabled()) {
	            log.debug("No outgoing sequence flow found for {}. Ending execution.", execution.getActivity().getId());
	          }
	          execution.end();
	          
	          if (throwExceptionIfExecutionStuck) {
	            throw new ActivitiException("No outgoing sequence flow of the inclusive gateway '" + execution.getActivity().getId()
	                  + "' could be selected for continuing the process");
	          }
	        }
	        
	      }
	    }
	  }
}
