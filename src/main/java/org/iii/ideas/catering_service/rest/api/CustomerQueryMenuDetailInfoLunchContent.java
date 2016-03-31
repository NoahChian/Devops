package org.iii.ideas.catering_service.rest.api;

import java.io.Serializable;

/*
 * "category":"主食",
            "foodName":"五穀飯",
            "image":"http://XXXX.jpg"

 */
public class CustomerQueryMenuDetailInfoLunchContent  implements Serializable {
	private String category;
	private String foodName;
	private String image;
	private Long dishid;
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getFoodName() {
		return foodName;
	}
	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Long getDishid() {
		return dishid;
	}
	public void setDishid(Long dishid) {
		this.dishid = dishid;
	}
	
}
