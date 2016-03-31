package org.iii.ideas.catering_service.dao;

public class AbstractCode implements java.io.Serializable {
	private Integer id;
	private String code;
	private String name;
	private String type;
	private Integer sort;
	
	public AbstractCode(){
	}
	
	public AbstractCode(Integer id,String code,String name,String type){
		this.id=id;
		this.code=code;
		this.name=name;
		this.type=type;
	}
	
	public void setId(Integer id){
		this.id=id;
	}

	public Integer getId(){
		return this.id;
	}
	
	public void setCode(String code){
		this.code=code;
	}
	
	public String getCode(){
		return this.code;
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public String getName(){
		return this.name;
	}
	public void setType(String type){
		this.type=type;
	}
	
	public String getType(){
		return this.type;
	}
	
	public void setSort(Integer sort){
		this.sort=sort;
	}
	
	public Integer getSort(){
		return this.sort;
	}
}
