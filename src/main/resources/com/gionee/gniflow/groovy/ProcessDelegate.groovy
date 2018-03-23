package com.gionee.gniflow.groovy
class ProcessDelegate{
	Process process;
	
	ProcessDelegate(Process process){
		this.process = process;
	}
	
	def process(Map args, Closure closure){
			args.each { arg ->
		        if (args instanceof Map) {  
		            args.each {  
						process[it.key] = it.value;
		            }  
		        }
			} 
			StepDelegate sd = new StepDelegate(process);
			closure.delegate = sd  ;
			closure.call();
			return process;
	}
	
}
