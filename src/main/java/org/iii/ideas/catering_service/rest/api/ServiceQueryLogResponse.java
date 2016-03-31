package org.iii.ideas.catering_service.rest.api;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.iii.ideas.catering_service.dao.WsLog;

public class ServiceQueryLogResponse  extends AbstractApiResponse{
	private List<logObj> logs=new ArrayList<logObj>();
	private HashMap<String ,String> accountInfo=new HashMap<String ,String>();
	
	
	public List<logObj> getlogs() {
		return this.logs;
	}

	public void setLogss(List<logObj> logs) {
		this.logs = logs;
	}
	
	
	public HashMap<String ,String> getAccountInfo() {
		return this.accountInfo;
	}

	public void setAccountInfo(HashMap<String ,String> accountInfo) {
		this.accountInfo = accountInfo;
	}
	
}

class logObj{
	public Timestamp sendTime;
	public String action;
	public String statusCode;
	public String description;

}
