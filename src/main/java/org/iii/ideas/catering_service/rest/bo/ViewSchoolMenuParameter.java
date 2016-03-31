package org.iii.ideas.catering_service.rest.bo;

import java.util.HashMap;

import org.iii.ideas.catering_service.dao.ViewSchoolMenuWithBatchdata;

public class ViewSchoolMenuParameter {
	private HashMap<String, Object> menuParameterMap = new HashMap<String, Object>();

	// private String ingredientName;

	public void setSchoolname(String schoolname) {
		setHashmap(ViewSchoolMenuWithBatchdata.SCHOOL_NAME, schoolname);
	}

	public String getSchoolname() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata.SCHOOL_NAME);
	}

	public void setBatchdataid(String batchdataid) {
		setHashmap(ViewSchoolMenuWithBatchdata.BATCH_DATA_ID, batchdataid);
	}

	public String getBatchdataid() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata.BATCH_DATA_ID);
	}

	public void setMainFoolId(String mainFoolId) {
		setHashmap(ViewSchoolMenuWithBatchdata.MAIN_FOOD_ID, mainFoolId);
	}

	public String getMainFoolId() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata.MAIN_FOOD_ID);
	}

	public void setMainFood1id(String mainFood1id) {
		setHashmap(ViewSchoolMenuWithBatchdata.MAIN_FOOD1_ID, mainFood1id);
	}

	public String getMainFood1id() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata.MAIN_FOOD1_ID);
	}

	public void setMainDishId(String mainDishId) {
		setHashmap(ViewSchoolMenuWithBatchdata.MAIN_DISH_ID, mainDishId);
	}

	public String getMainDishId() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata.MAIN_DISH_ID);
	}

	public void setMainDish1id(String mainDish1id) {
		setHashmap(ViewSchoolMenuWithBatchdata.MAIN_DISH1_ID, mainDish1id);
	}

	public String getMainDish1id() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata.MAIN_DISH1_ID);
	}

	public void setMainDish2id(String mainDish2id) {
		setHashmap(ViewSchoolMenuWithBatchdata.MAIN_DISH2_ID, mainDish2id);
	}

	public String getMainDish2id() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata.MAIN_DISH2_ID);
	}

	public void setMainDish3id(String mainDish3id) {
		setHashmap(ViewSchoolMenuWithBatchdata.MAIN_DISH3_ID, mainDish3id);
	}

	public String getMainDish3id() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata.MAIN_DISH3_ID);
	}

	public void setSubDish1id(String subDish1id) {
		setHashmap(ViewSchoolMenuWithBatchdata.SUB_DISH1_ID, subDish1id);
	}

	public String getSubDish1id() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata.SUB_DISH1_ID);
	}

	public void setSubDish2id(String subDish2id) {
		setHashmap(ViewSchoolMenuWithBatchdata.SUB_DISH2_ID, subDish2id);
	}

	public String getSubDish2id() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata.SUB_DISH2_ID);
	}

	public void setSubDish3id(String subDish3id) {
		setHashmap(ViewSchoolMenuWithBatchdata.SUB_DISH3_ID, subDish3id);
	}

	public String getSubDish3id() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata.SUB_DISH3_ID);
	}

	public void setSubDish4id(String subDish4id) {
		setHashmap(ViewSchoolMenuWithBatchdata.SUB_DISH4_ID, subDish4id);
	}

	public String getSubDish4id() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata.SUB_DISH4_ID);
	}

	public void setSubDish5id(String subDish5id) {
		setHashmap(ViewSchoolMenuWithBatchdata.SUB_DISH5_ID, subDish5id);
	}

	public String getSubDish5id() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata.SUB_DISH5_ID);
	}

	public void setSubDish6id(String subDish6id) {
		setHashmap(ViewSchoolMenuWithBatchdata.SUB_DISH6_ID, subDish6id);
	}

	public String getSubDish6id() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata.SUB_DISH6_ID);
	}

	public void setVegetableId(String vegetableId) {
		setHashmap(ViewSchoolMenuWithBatchdata.VEGETABLE_ID, vegetableId);
	}

	public String getVegetableId() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata.VEGETABLE_ID);
	}

	public void setSoupId(String soupId) {
		setHashmap(ViewSchoolMenuWithBatchdata.SOUP_ID, soupId);
	}

	public String getSoupId() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata.SOUP_ID);
	}

	public void setDessertId(String dessertId) {
		setHashmap(ViewSchoolMenuWithBatchdata.DESSERT_ID, dessertId);
	}

	public String getDessertId() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata.DESSERT_ID);
	}

	public void setDessert1id(String dessert1id) {
		setHashmap(ViewSchoolMenuWithBatchdata.DESSERT1_ID, dessert1id);
	}

	public String getDessert1id() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata.DESSERT1_ID);
	}

	public void setTypeGrains(String typeGrains) {
		setHashmap(ViewSchoolMenuWithBatchdata.TYPE_GRAINS, typeGrains);
	}

	public String getTypeGrains() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata.TYPE_GRAINS);
	}

	public void setTypeOil(String typeOil) {
		setHashmap(ViewSchoolMenuWithBatchdata.TYPE_OIL, typeOil);
	}

	public String getTypeOil() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata.TYPE_OIL);
	}

	public void setTypeVegetable(String typeVegetable) {
		setHashmap(ViewSchoolMenuWithBatchdata.TYPE_VEGETABLE, typeVegetable);
	}

	public String getTypeVegetable() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata.TYPE_VEGETABLE);
	}

	public void setTypeMilk(String typeMilk) {
		setHashmap(ViewSchoolMenuWithBatchdata.TYPE_MILK, typeMilk);
	}

	public String getTypeMilk() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata.TYPE_MILK);
	}

	public void setTypeFruit(String typeFruit) {
		setHashmap(ViewSchoolMenuWithBatchdata.TYPE_FRUIT, typeFruit);
	}

	public String getTypeFruit() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata.TYPE_FRUIT);
	}

	public void setTypeMeatBeans(String typeMeatBeans) {
		setHashmap(ViewSchoolMenuWithBatchdata.TYPE_MEAT_BEANS, typeMeatBeans);
	}

	public String getTypeMeatBeans() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata.TYPE_MEAT_BEANS);
	}

	public void setCalorie(String calorie) {
		setHashmap(ViewSchoolMenuWithBatchdata.CALLORI, calorie);
	}

	public String getCalorie() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata.CALLORI);
	}

	public void setSrcType(String srcType) {
		setHashmap(ViewSchoolMenuWithBatchdata.SRC_TYPE, srcType);
	}

	public String getSrcType() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata.SRC_TYPE);
	}

	private void setHashmap(String key, Object value) {
		if (menuParameterMap.containsKey(key)) {
			menuParameterMap.put(key, value); // The method put will replace the value of an existing key and will create it if doesn't exist.
		} else {
			menuParameterMap.put(key, value);
		}
	}

	public HashMap<String, Object> getMap() {
		return menuParameterMap;
	}
}
