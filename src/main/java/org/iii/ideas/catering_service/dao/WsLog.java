package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;

public class WsLog extends AbstractWsLog implements java.io.Serializable{
	public WsLog(){
		super();
	}
	
	//full construction
	public WsLog(Integer id, String messageId,String companyId,String statusCode,Timestamp sendTime,Timestamp updateTime,String description,String action){
		super( id,  messageId, companyId, statusCode, sendTime, updateTime, description,action);
	}
}
