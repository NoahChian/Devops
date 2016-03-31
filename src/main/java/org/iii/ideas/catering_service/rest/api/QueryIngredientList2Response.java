package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.iii.ideas.catering_service.dao.ViewDishAndIngredient;
import org.iii.ideas.catering_service.dao.ViewSchoolMenuWithBatchdata;
import org.iii.ideas.catering_service.rest.bo.ViewMenuBO;

public class QueryIngredientList2Response  extends AbstractApiResponse{
	
	private List<ViewDishAndIngredient> ingredient = new ArrayList<ViewDishAndIngredient>();
	private Integer totalCol;
	private HashMap<String,String> header=new HashMap<String,String>();
	private String fileKey;
	
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
	public HashMap<String,String> getHeader() {
		return header;
	}

	public void setHeader(HashMap<String,String> header) {
		this.header = header;
	}
	public String getFileKey(){
		return this.fileKey;
	}
	public void setFileKey(String fileKey){
		this.fileKey=fileKey;
	}
}
 