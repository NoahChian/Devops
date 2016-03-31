package org.iii.ideas.catering_service.rest.object;

import org.apache.log4j.Logger;

public class GenericWebRestResponse {
	private Logger log = Logger.getLogger(this.getClass());
	private Object result_content;	
	private String resource;
	private String method;
	private String result;
	private String error_msg;
	
	
	public GenericWebRestResponse(){
		this.error_msg="";
		this.result ="1";
		this.method ="";
		this.resource ="";		
	}
	
	public Object getResult_content() {
		return result_content;
	}

	public void setResult_content(Object result_content) {
		this.result_content = result_content;
	}
	
	public void setResourceMethod(String pathInfo){
		String[] pathnames=pathInfo.split("/");										
		this.resource = pathnames[1];
	//	this.method   = pathnames[2];
	}
	
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getError_msg() {
		return error_msg;
	}
	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
	}

}
