package org.iii.ideas.catering_service.rest.object;

public class GenericWebRestXmlRequestBody {
	private String args;
	private String method;
	
	public String getMethod() {
		return method;
	}
	public void setMethod(String pMethod) {
		this.method = pMethod;
	}
	
	public String getArgs() {
		return this.args;
	}

	public void setArgs(String restObj) {
		this.args = restObj;
	}
}
