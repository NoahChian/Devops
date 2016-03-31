package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;

public abstract class AbstractRole implements java.io.Serializable {
	private Integer roleId;
	private String roletype;
	private String rolerule;
	private Timestamp createTime;
	private Timestamp updateTime;

	
	/** default constructor */
	public AbstractRole() {
	}

	/** full constructor */
	public AbstractRole(Integer roleId, String roletype,String rolerule,
			Timestamp createTime,Timestamp updateTime) {
		this.roleId=roleId;
		this.roletype=roletype;
		this.rolerule=rolerule;
		this.createTime=createTime;
		this.updateTime=updateTime;
	}
	
	public void setRoleId(Integer roleId){
		this.roleId=roleId;
	}
	
	public Integer getRoleId(){
		return this.roleId;
	}
	
	
	public void setRoletype(String roletype){
		this.roletype=roletype;
	}
	public String getRoletype(){
		return this.roletype;
	}
	
	public void setRolerule(String rolerule){
		this.rolerule=rolerule;
	}
	public String getRolerule(){
		return this.rolerule;
	}
	

	public Timestamp getCreateTime(){
		return this.createTime;
	}
	public void setCreateTime(Timestamp createTime){
		this.createTime=createTime;
	}
	public Timestamp getUpdateTime(){
		return this.updateTime;
	}
	public void setUpdateTime(Timestamp updateTime){
		this.updateTime=updateTime;
	}
	
}
