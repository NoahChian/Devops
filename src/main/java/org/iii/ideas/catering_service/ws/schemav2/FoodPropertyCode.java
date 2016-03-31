package org.iii.ideas.catering_service.ws.schemav2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.iii.ideas.catering_service.util.CateringServiceCode;

public final class FoodPropertyCode {
	public final static HashMap<String,Integer>  WS_FOODCODE_GENE_List;
	static {
		WS_FOODCODE_GENE_List=new HashMap<String,Integer>();
		WS_FOODCODE_GENE_List.put("1", CateringServiceCode.INGREDIENT_ATTR_GMBEAM);
		WS_FOODCODE_GENE_List.put("2", CateringServiceCode.INGREDIENT_ATTR_GMCORN);
	}
	
	
	public final static HashMap<String ,String> WS_FOODCODE_DISHTYPE_LIST;
	static {
		WS_FOODCODE_DISHTYPE_LIST=new HashMap<String , String >();
		WS_FOODCODE_DISHTYPE_LIST.put("1","MainFood");
		WS_FOODCODE_DISHTYPE_LIST.put("2","MainDish");
		WS_FOODCODE_DISHTYPE_LIST.put("3","SubDish");
		WS_FOODCODE_DISHTYPE_LIST.put("4","Vegetable");
		WS_FOODCODE_DISHTYPE_LIST.put("5","Dessert");
		WS_FOODCODE_DISHTYPE_LIST.put("6","Soup");
	}
	
}
