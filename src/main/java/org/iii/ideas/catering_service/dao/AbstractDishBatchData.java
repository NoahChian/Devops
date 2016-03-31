package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;

import org.iii.ideas.catering_service.service.CateringServiceLoader;

public abstract class AbstractDishBatchData implements java.io.Serializable {
	private Long dishBatchDataId;
	private Long batchDataId;
	private String dishName;
	private String dishType;
	private Long dishId;
	private Timestamp uploadDateTime;
	private String dishShowName;
	private Integer dishOrder=999;
	
	public Integer getDishOrder() {
		return dishOrder;
	}

	public void setDishOrder(Integer dishOrder) {
		this.dishOrder = dishOrder;
	}

	public AbstractDishBatchData(){}
	
	/** default constructor */
	public AbstractDishBatchData(Long dishBatchDataId, Long batchDataId, String dishName,String dishType,Long dishId) {
		this.dishBatchDataId=dishBatchDataId;
		this.batchDataId=batchDataId;
		this.dishName=dishName;
		this.dishType=dishType;
		this.dishId=dishId;
	}

	/** full constructor */
	public AbstractDishBatchData(Long dishBatchDataId, Long batchDataId, String dishName,String dishType,
			Long dishId,Timestamp uploadDateTime, String dishShowName) {
		this.dishBatchDataId=dishBatchDataId;
		this.batchDataId=batchDataId;
		this.dishName=dishName;
		this.dishType=dishType;
		this.dishId=dishId;
		this.uploadDateTime=uploadDateTime;
		this.dishShowName="";
	}
	
	public void setDishBatchDataId(long dishBatchDataId){
		this.dishBatchDataId=dishBatchDataId;
	}
	
	public Long getDishBatchDataId(){
		return this.dishBatchDataId;
	}
	
	public void setBatchDataId(Long batchDataId){
		this.batchDataId=batchDataId;
	}
	public Long getBatchDataId(){
		return this.batchDataId;
	}
	
	public void setDishName(String dishName){
		this.dishName=dishName;
	}
	public String getDishName(){
		return this.dishName;
	}
	
	public void setDishType(String dishType){
		this.dishType=dishType;
	}
	public String getDishType(){
		return this.dishType;
	}
	
	public void setDishId(Long dishId){
		this.dishId=dishId;
	}
	public Long getDishId(){
		return this.dishId;
	}

	public Timestamp getUpdateDateTime(){
		return this.uploadDateTime;
	}
	public void setUpdateDateTime(Timestamp uploadDateTime){
		this.uploadDateTime=uploadDateTime;
	}
	
	public String getDishShowName(){
		return this.dishShowName;
	}
	public void setDishShowName(String dishShowName){
		this.dishShowName=dishShowName;
	}
}
