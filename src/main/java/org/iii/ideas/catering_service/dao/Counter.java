package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;


public class Counter extends AbstractCounter implements java.io.Serializable {
	
	
	/** default constructor */
	public Counter() {
	}
	/** full constructor */
	public Counter(Integer id,String date,String funcName,Integer count,String type,Integer kitchenid) {
		super(id,date,funcName,count,type,kitchenid);
	}

	public Counter(String date,Integer count){
		super(date,count);
	}
	
}
