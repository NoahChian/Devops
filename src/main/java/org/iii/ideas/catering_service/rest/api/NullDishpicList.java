package org.iii.ideas.catering_service.rest.api;

import java.io.Serializable;


public class NullDishpicList  implements Serializable {
	
	/**
	 * 20140612 Ric
	 * 無菜色照片
	 */
	//private static final long serialVersionUID = -4253202757365005723L;
	private String dishName;
	private Long dishId;

	public String getDishName() {
		return dishName;
	}
	public void setDishName(String dishName) {
		this.dishName = dishName;
	}
	public Long getDishId() {
		return dishId;
	}
	public void setDishId(Long dishId) {
		this.dishId = dishId;
	}
}
