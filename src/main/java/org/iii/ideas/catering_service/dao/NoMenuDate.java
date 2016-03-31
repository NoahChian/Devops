package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;

public class NoMenuDate extends AbstractNoMenuDate implements java.io.Serializable {
	
	private Long nmDateId;
	private String schoolId;
	private Timestamp startdate;
	private Timestamp enddate;
	private Integer menuType;
	private Integer nmdescId;
	private String note;
	private String createUser;
	private Timestamp createDateTime;
	private String updateUser;
	private Timestamp updateDateTime;
	
	public NoMenuDate(){
	}
	
	public NoMenuDate(Long nmDateId, String schoolId,
			Timestamp startdate, Timestamp enddate,Integer menuType,
			Integer nmdescId,String note,String createUser,Timestamp createDateTime,
			String updateUser,Timestamp updateDateTime){
		super(nmDateId, schoolId,startdate, enddate,menuType, nmdescId, note, createUser, createDateTime,
				 updateUser, updateDateTime);
	}
	
	
}
