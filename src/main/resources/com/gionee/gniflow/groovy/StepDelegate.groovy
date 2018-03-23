package com.gionee.gniflow.groovy

import java.util.Map

import groovy.lang.Closure

class StepDelegate {
	Process process;
	StepDelegate(Process process){
		this.process = process;
	}
	
	def step(Map args, Closure closure){
		Step step = new Step();
		args.each { arg ->
			if (args instanceof Map) {
				args.each {
					step[it.key] = it.value;
				}
			}
		}
		FieldDelegate fd = new FieldDelegate(step);
		closure.delegate = fd;
		closure.call();
		process.step.add(step);
	}
}
