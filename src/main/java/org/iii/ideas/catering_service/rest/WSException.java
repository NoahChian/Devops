package org.iii.ideas.catering_service.rest;

public class WSException extends Exception {
	private WSError errorValue;

	public WSException(WSError err){
		super("error");
		this.errorValue=err;		
	}
	
	public WSException(WSError err, String desc){
		super(desc);
		this.errorValue=err;		
	}
	
	public WSException(WSError err,Throwable cause){
		super(cause);
		this.errorValue=err;		
	}
	
	public WSError getErrorValue() {
		return errorValue;
	}

	public void setErrorValue(WSError errorValue) {
		this.errorValue = errorValue;
	}
}
