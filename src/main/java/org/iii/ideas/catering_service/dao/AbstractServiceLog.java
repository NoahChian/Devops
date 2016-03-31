package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;
import java.util.regex.Pattern;

/**
 * AbstractBatchdata entity provides the base persistence definition of the
 * Batchdata entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractServiceLog implements java.io.Serializable {

	// Fields

	private Integer id;
	private Timestamp date;
	private String qAccount;
	private String qUsername;
	private String qType;
	private String serviceAccount;
	private String qContent;
	
	
	// Constructors

	/** default constructor */
	public AbstractServiceLog() {
	}

	public AbstractServiceLog(Integer id, Timestamp date, String qAccount,String  qUsername,
			String qType,String serviceAccount,String qContent) {
			this.id=id;
			this.date=date;
			this.qAccount=qAccount;
			this.qUsername=qUsername;
			this.qType=qType;
			this.serviceAccount=serviceAccount;
			this.qContent=qContent;
	}

	
	// Property accessors
	public void setId(Integer id){
		this.id=id;
	}
	public Integer getId(){
		return this.id;
	}
	public void setDate(Timestamp date){
		this.date=date;
	}
	public Timestamp getDate(){
		return this.date;
	}
	public void setqAccount(String qAccount){
		this.qAccount=qAccount;
	}
	public String getqAccount(){
		return this.qAccount;
	}
	public void setqUsername(String qUsername){
		this.qUsername=qUsername;
	}
	public String getqUsername(){
		return this.qUsername;
	}
	
	public void setqType(String qType){
		this.qType=qType;
	}
	public String getqType(){
		return this.qType;
	}
	
	public void setServiceAccount(String serviceAccount){
		this.serviceAccount=serviceAccount;
	}
	
	
	public String getServiceAccount(){
		return this.serviceAccount;
	}
	
	public void setqContent(String qContent){
		this.qContent=qContent;
	}
	
	public String getqContent(){
		return this.qContent;
	}

}