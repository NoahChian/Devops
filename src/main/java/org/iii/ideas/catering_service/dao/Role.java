package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;

public class Role extends AbstractRole  implements java.io.Serializable { 

	/** default constructor */
	public Role() {
	}
	/** full constructor */
	public Role(Integer roleId, String roletype,String rolerule,
			Timestamp createTime,Timestamp updateTime) {
		super(roleId,roletype,rolerule,createTime,updateTime);
	}

}

