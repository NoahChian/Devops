package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

import org.iii.ideas.catering_service.dao.Ingredientbatchdata;

public class CustomerQueryMenuDetailInfov3Response  extends AbstractApiResponse  {

	private List<Ingredientbatchdata> result = new ArrayList<Ingredientbatchdata>();

	public void setResult(List<Ingredientbatchdata> result){
		this.result=result;
	}
	public List<Ingredientbatchdata>  getResult(){
		return this.result;
	}
}
