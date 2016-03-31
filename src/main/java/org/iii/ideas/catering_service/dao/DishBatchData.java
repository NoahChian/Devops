package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;

public class DishBatchData extends AbstractDishBatchData  implements java.io.Serializable { 
	/** default constructor */
	public DishBatchData(Long dishBatchDataId, Long batchDataId, String dishName,String dishType,Long dishId) {
		super(dishBatchDataId,batchDataId,dishName,dishType,dishId);
	}
	/** full constructor */
	public DishBatchData(Long dishBatchDataId, Long batchDataId, String dishName,String dishType,Long dishId,Timestamp uploadDateTime,String dishShowName) {
		super(dishBatchDataId,batchDataId,dishName,dishType,dishId,uploadDateTime,dishShowName);
	}
	public DishBatchData(){
		
	}

}
