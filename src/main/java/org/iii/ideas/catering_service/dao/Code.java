package org.iii.ideas.catering_service.dao;

public class Code extends AbstractCode implements java.io.Serializable {
	
	private Integer id;
	private String code;
	private String name;
	private String type;
	
	public Code(){
	}
	
	public Code(Integer id,String code,String name,String type){
		super(id,code,name,type);
	}
	
	
}
