package org.iii.ideas.catering_service.dao;
/**
 * AbstractDish entity provides the base persistence definition of the Dish
 * entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractCounter implements java.io.Serializable {

	// Fields

	private Integer id=0;
	private String date;
	private String funcName;
	private Integer count;
	private String type;
	private Integer kitchenid;
	// Constructors

	/** default constructor */
	public AbstractCounter() {
	}

	/** full constructor */
	public AbstractCounter(Integer id,String date,String funcName, Integer count,String type,Integer kitchenid) {
		this.id=id;
		this.date=date;
		this.funcName=funcName;
		this.count=count;
		this.type=type;
		this.kitchenid=kitchenid;
	}

	public AbstractCounter(String date,Integer count) {
		this.date=date;
		this.count=count;
	}
	
	
	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getFuncName(){
		return this.funcName;
	}
	
	public void setFuncName(String funcName){
		this.funcName=funcName;
	}
	
	public String getDate(){
		return this.date;
	}
	
	public void setDate(String date){
		this.date=date;
	}

	public Integer getCount(){
		return this.count;
	}
	
	public void setCount(int count){
		this.count=count;
	}
	
	public String getType(){
		return this.type;
	}
	public void setType(String type){
		this.type=type;
	}
	
	public Integer getKitchenid(){
		return this.kitchenid;
	}
	public void setKitchenid(Integer kitchenid){
		this.kitchenid=kitchenid;
	}

}