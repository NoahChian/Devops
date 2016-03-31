package org.iii.ideas.catering_service.dao;

public class AbstractSystemConfig implements java.io.Serializable {
	private Integer id;
	private String pname;
	private String pvalue;
	private String ptype;
	private String penable;
	

	public AbstractSystemConfig(){}
	
	public AbstractSystemConfig(String pname,String pvalue,String ptype,String penable){
		this.pname=pname;
		this.pvalue=pvalue;
		this.ptype=ptype;
		this.penable=penable;
	}
	
	
	public void setId(Integer id){
		this.id=id;
	}
	public Integer getId(){
		return this.id;
	}
	
	
	public void setPname(String pname){
		this.pname=pname;
	}
	public String getPname(){
		return this.pname;
	}
	
	public void setPvalue(String pvalue){
		this.pvalue=pvalue;
	}
	
	public String getPvalue(){
		return this.pvalue;
	}
	
	public void setPtype(String ptype){
		this.ptype=ptype;
	}
	
	public String getPtype(){
		return this.ptype;
	}
	
	public void setPenable(String penable){
		this.penable=penable;
	}
	
	public String getPenable(){
		return this.penable;
	}
}
