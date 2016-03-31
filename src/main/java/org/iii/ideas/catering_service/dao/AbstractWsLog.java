package org.iii.ideas.catering_service.dao;

import java.sql.Date;
import java.sql.Timestamp;

public abstract class AbstractWsLog implements java.io.Serializable {

		// Fields

		private Integer id;
		private String messageId;
		private String companyId;
		private String statusCode;
		private Timestamp sendTime;
		private Timestamp updateTime;
		private String description;
		private String action;

		// Constructors

		/** default constructor */
		public AbstractWsLog() {
		}

		/** full constructor */
		public AbstractWsLog(Integer id, String messageId,String companyId,String statusCode,Timestamp sendTime,Timestamp updateTime,String description,String action) {
			this.id=id;
			this.messageId=messageId;
			this.companyId=companyId;
			this.statusCode=statusCode;
			this.sendTime=sendTime;
			this.updateTime=updateTime;
			this.description=description;
			this.action=action;
		}

		// Property accessors

		public void setId(Integer id){
			this.id=id;
		}
		
		public Integer getId(){
			return this.id;
		}

		public void setMessageId(String messageId){
			this.messageId=messageId;
		}
		
		public String getMessageId(){
			return this.messageId;
		}
		
		public void setCompanyId(String companyId){
			this.companyId=companyId;
		}
		
		public String getCompanyId(){
			return this.companyId;
		}
		public void setStatusCode(String statusCode){
			this.statusCode=statusCode;
		}	
		public String getStatusCode(){
			return this.statusCode;
		}
		
		public void setSendTime(Timestamp sendTime){
			this.sendTime=sendTime;
		}
		public Timestamp getSendTime(){
			return  this.sendTime;
		}
		
		public void setUpdateTime(Timestamp updateTime){
			this.updateTime=updateTime;
		}
		public Timestamp getUpdateTime(){
			return this.updateTime;
		}
		public void setDescription(String description){
			this.description=description;
		}
		public String getDescription(){
			return this.description;
		}
		
		public void setAction(String action){
			this.action=action.toUpperCase();
		}
		public String getAction(){
			return this.action;
		}
		
	}
