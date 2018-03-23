package com.gionee.gniflow.groovy;

import groovy.lang.Closure;

class ProcessGroovy{
	def  Process make(Closure closure) {
		Process process = new Process();
		ProcessDelegate pd = new ProcessDelegate(process);
		closure.delegate = pd;
		closure.call();
		return process;
	 }
}