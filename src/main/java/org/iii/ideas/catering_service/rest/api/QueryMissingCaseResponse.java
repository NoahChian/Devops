package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

public class QueryMissingCaseResponse  extends AbstractApiResponse{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 613067573689647791L;
	private List <NullIngreduentList>nullIngreduentList = new ArrayList<NullIngreduentList>();//有菜單無食材
	private List <NullSeasoningList>nullSeasoningList = new ArrayList<NullSeasoningList>();//無調味料
	private List <NullDishpicList>nullDishpicList = new ArrayList<NullDishpicList>();//無菜色照片
	private List <NullIngreduentDataList>nullIngreduentDataList = new ArrayList<NullIngreduentDataList>();//食材資料未完整填寫
	public List<NullIngreduentList> getNullIngreduentList() {
		return nullIngreduentList;
	}
	public void setNullIngreduentList(List<NullIngreduentList> nullIngreduentList) {
		this.nullIngreduentList = nullIngreduentList;
	}
	
	public List<NullSeasoningList> getNullSeasoningList() {
		return nullSeasoningList;
	}
	public void setNullSeasoningList(List<NullSeasoningList> nullSeasoningList) {
		this.nullSeasoningList = nullSeasoningList;
	}

	public List<NullDishpicList> getNullDishpicList() {
		return nullDishpicList;
	}
	public void setNullDishpicList(List<NullDishpicList> nullDishpicList) {
		this.nullDishpicList = nullDishpicList;
	}
	
	public List<NullIngreduentDataList> getNullIngreduentDataList() {
		return nullIngreduentDataList;
	}
	public void setNullIngreduentDataList(List<NullIngreduentDataList> nullIngreduentDataList) {
		this.nullIngreduentDataList = nullIngreduentDataList;
	}

}
