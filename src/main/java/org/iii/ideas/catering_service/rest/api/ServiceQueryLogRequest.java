package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.iii.ideas.catering_service.dao.WsLog;

public class ServiceQueryLogRequest extends AbstractApiResponse{
	private String action;
	private String account;

	private HashMap<String ,String> content=new HashMap<String ,String>();
	
	
	public void setAction(String action){
		this.action=action;
	}
	
	public String getAction(){
		return this.action;
	}
	
	
	
	public void setAccount(String account){
		this.account=account;
	}
	
	public String getAccount(){
		return this.account;
	}
	
	

	
	public void setContent(HashMap content){
		this.content=content;
	}
	
	public HashMap getContent(){
		return this.content;
	}
}
