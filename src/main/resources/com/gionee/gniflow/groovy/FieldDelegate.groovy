package com.gionee.gniflow.groovy

class FieldDelegate {
	Step step;
	
	FieldDelegate(Step step){
		this.step = step;
	}
	
	def field(Map args){
		Field field =  new Field();
		args.each { arg ->
			if (args instanceof Map) {
				args.each {
					field[it.key] = it.value;
				}
			}
		}
		step.field.add(field);
	}
}
