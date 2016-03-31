package org.iii.ideas.catering_service.rest.api;

import java.io.Serializable;


public class MissingCaseByCountyList  implements Serializable {
	
	/**
	 * 2014731 Ric
	 * 查詢全縣市缺漏資料
	 */
	private String kitchenName;
	private String nullIngredient;
	private String nullSeasoning;
	private String nullDishpic;
	private String nullIngredientData;
	private String isNoMenuDate;
	public String getKitchenName() {
		return kitchenName;
	}
	public void setKitchenName(String kitchenName) {
		this.kitchenName = kitchenName;
	}
	public String getNullIngredient() {
		return nullIngredient;
	}
	public void setNullIngredient(String nullIngredient) {
		this.nullIngredient = nullIngredient;
	}
	public String getNullSeasoning() {
		return nullSeasoning;
	}
	public void setNullSeasoning(String nullSeasoning) {
		this.nullSeasoning = nullSeasoning;
	}
	public String getNullDishpic() {
		return nullDishpic;
	}
	public void setNullDishpic(String nullDishpic) {
		this.nullDishpic = nullDishpic;
	}
	public String getNullIngredientData() {
		return nullIngredientData;
	}
	public void setNullIngredientData(String nullIngredientData) {
		this.nullIngredientData = nullIngredientData;
	}
	public String getIsNoMenuDate() {
		return isNoMenuDate;
	}
	public void setIsNoMenuDate(String isNoMenuDate) {
		this.isNoMenuDate = isNoMenuDate;
	}
	
	
}