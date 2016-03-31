package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;

public class ServiceLog extends AbstractServiceLog implements java.io.Serializable {
	
	private Integer id;
	private Timestamp date;
	private String qAccount;
	private String qUsername;
	private String qType;
	private String serviceAccount;
	private String qContent;
	
	public ServiceLog(){
	}
	
	public ServiceLog(Integer id, Timestamp date, String qAccount,String  qUsername,
			String qType,String serviceAccount,String qContent) {
			this.id=id;
			this.date=date;
			this.qAccount=qAccount;
			this.qUsername=qUsername;
			this.qType=qType;
			this.serviceAccount=serviceAccount;
			this.qContent=qContent;
	}

	
	
}
