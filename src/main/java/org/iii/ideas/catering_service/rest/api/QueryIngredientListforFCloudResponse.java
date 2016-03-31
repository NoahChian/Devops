package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

import org.iii.ideas.catering_service.dao.ViewDishAndIngredient;
import org.iii.ideas.catering_service.dao.ViewDishAndIngredient2;
import org.iii.ideas.catering_service.dao.ViewSchoolMenuWithBatchdata;
import org.iii.ideas.catering_service.rest.bo.ViewMenuBO;

public class QueryIngredientListforFCloudResponse  extends AbstractApiResponse{
	
	private List<ViewDishAndIngredient> ingredient = new ArrayList<ViewDishAndIngredient>();
	// ingredient2 這個List用於『食材資料查詢』頁畫之『供應商/製造商』之欄位複合式查詢
	private List<ViewDishAndIngredient2> ingredient2 = new ArrayList<ViewDishAndIngredient2>();
	private Integer totalCol;

	public List <ViewDishAndIngredient> getIngredient() {
		return ingredient;
	}

	public void setIngredient(List<ViewDishAndIngredient> vdai) {
		this.ingredient = vdai;
	}

	public Integer getTotalCol() {
		return totalCol;
	}

	public void setTotalCol(Integer totalCol) {
		this.totalCol = totalCol;
	}

	public List<ViewDishAndIngredient2> getIngredient2() {
		return ingredient2;
	}

	public void setIngredient2(List<ViewDishAndIngredient2> ingredient2) {
		this.ingredient2 = ingredient2;
	}

	
}
 