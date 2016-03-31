package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import org.iii.ideas.catering_service.dao.ViewDishAndIngredient;


public class QueryIngredientList2Request {
	private String begDate;
	private String endDate;
	private Integer queryLimit;
	private int page;
	private int perpage;
	private String func;
	private ArrayList<ViewDishAndIngredient> cond=new ArrayList<ViewDishAndIngredient>();
	//private ArrayList<HashMap<String,String>> condObj=new ArrayList<HashMap<String,String>>();
	
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getBegDate() {
		return begDate;
	}
	public void setBegDate(String begDate) {
		this.begDate = begDate;
	}
	
	public Integer getQueryLimit() {
		return queryLimit;
	}
	public void setQueryLimit(Integer queryLimit) {
		this.queryLimit = queryLimit;
	}
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getPerpage() {
		return perpage;
	}
	public void setPerpage(int perpage) {
		this.perpage = perpage;
	}
	
	public void setCond(ArrayList<ViewDishAndIngredient> cond){
		this.cond=cond;
	}
	public ArrayList<ViewDishAndIngredient> getCond(){
		return this.cond;
	}
	public String getFunc(){
		return this.func;
	}
	
	public void setFunc(String func){
		this.func=func;
	}
	
}


