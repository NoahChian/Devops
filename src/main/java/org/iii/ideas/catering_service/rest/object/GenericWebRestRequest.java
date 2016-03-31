package org.iii.ideas.catering_service.rest.object;

public class GenericWebRestRequest {
	private Object args;
	private String method;
	
	public String getMethod() {
		return method;
	}
	public void setMethod(String pMethod) {
		this.method = pMethod;
	}
	
	public Object getArgs() {
		return this.args;
	}

	public void setArgs(Object restObj) {
		this.args = restObj;
	}
}
