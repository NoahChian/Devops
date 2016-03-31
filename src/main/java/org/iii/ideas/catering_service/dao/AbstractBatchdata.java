package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;
import java.util.regex.Pattern;

/**
 * AbstractBatchdata entity provides the base persistence definition of the
 * Batchdata entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractBatchdata implements java.io.Serializable {

	// Fields

	private Long batchDataId;
	private Integer kitchenId;
	private Integer schoolId;
	private String menuDate;
	private Long mainFoodId;
	private Long mainFood1id=(long) 0;
	private Long mainDishId;
	private Long mainDish1id=(long) 0;
	private Long mainDish2id=(long) 0;
	private Long mainDish3id=(long) 0;
	private Long subDish1id=(long) 0;
	private Long subDish2id=(long) 0;
	private Long subDish3id=(long) 0;
	private Long subDish4id=(long) 0;
	private Long subDish5id=(long) 0;
	private Long subDish6id=(long) 0;
	private Long vegetableId=(long) 0;
	private Long soupId=(long) 0;
	private Long dessertId=(long) 0;
	private Long dessert1id=(long) 0;
	private Timestamp uploadDateTime;
	private String lotNumber;
	private String typeGrains;
	private String typeOil;
	private String typeVegetable;
	private String typeMilk;
	private String typeFruit;
	private String typeMeatBeans;
	private String calorie;
	private String srcType;
	private Integer menuType;
	private Integer enable;
	private String modifyUser;
	// Constructors

	/** default constructor */
	public AbstractBatchdata() {
	}

	/** minimal constructor */
	public AbstractBatchdata(Integer kitchenId, Integer schoolId,
			String menuDate, Long mainFoodId, Long mainFood1id,
			Long mainDishId, Long mainDish1id, Long mainDish2id,
			Long mainDish3id, Long subDish1id, Long subDish2id,
			Long subDish3id, Long subDish4id, Long subDish5id,
			Long subDish6id, Long vegetableId, Long soupId,
			Long dessertId, Long dessert1id, Timestamp uploadDateTime) {
		this.kitchenId = kitchenId;
		this.schoolId = schoolId;
		this.menuDate = menuDate;
		this.mainFoodId = mainFoodId;
		this.mainFood1id = mainFood1id;
		this.mainDishId = mainDishId;
		this.mainDish1id = mainDish1id;
		this.mainDish2id = mainDish2id;
		this.mainDish3id = mainDish3id;
		this.subDish1id = subDish1id;
		this.subDish2id = subDish2id;
		this.subDish3id = subDish3id;
		this.subDish4id = subDish4id;
		this.subDish5id = subDish5id;
		this.subDish6id = subDish6id;
		this.vegetableId = vegetableId;
		this.soupId = soupId;
		this.dessertId = dessertId;
		this.dessert1id = dessert1id;
		this.uploadDateTime = uploadDateTime;
	}

	/**  constructor for add source type  20140305 KC*/
	public AbstractBatchdata(Integer kitchenId, Integer schoolId,
			String menuDate, Long mainFoodId, Long mainFood1id,
			Long mainDishId, Long mainDish1id, Long mainDish2id,
			Long mainDish3id, Long subDish1id, Long subDish2id,
			Long subDish3id, Long subDish4id, Long subDish5id,
			Long subDish6id, Long vegetableId, Long soupId,
			Long dessertId, Long dessert1id, Timestamp uploadDateTime,
			String lotNumber, String typeGrains, String typeOil,
			String typeVegetable, String typeMilk, String typeFruit,
			String typeMeatBeans, String calorie,String srcType) {
		this.kitchenId = kitchenId;
		this.schoolId = schoolId;
		this.menuDate = menuDate;
		this.mainFoodId = mainFoodId;
		this.mainFood1id = mainFood1id;
		this.mainDishId = mainDishId;
		this.mainDish1id = mainDish1id;
		this.mainDish2id = mainDish2id;
		this.mainDish3id = mainDish3id;
		this.subDish1id = subDish1id;
		this.subDish2id = subDish2id;
		this.subDish3id = subDish3id;
		this.subDish4id = subDish4id;
		this.subDish5id = subDish5id;
		this.subDish6id = subDish6id;
		this.vegetableId = vegetableId;
		this.soupId = soupId;
		this.dessertId = dessertId;
		this.dessert1id = dessert1id;
		this.uploadDateTime = uploadDateTime;
		this.lotNumber = lotNumber;
		this.typeGrains = typeGrains;
		this.typeOil = typeOil;
		this.typeVegetable = typeVegetable;
		this.typeMilk = typeMilk;
		this.typeFruit = typeFruit;
		this.typeMeatBeans = typeMeatBeans;
		this.calorie = calorie;
		this.srcType=srcType;
	}

	
	/** full constructor */
	public AbstractBatchdata(Integer kitchenId, Integer schoolId,
			String menuDate, Long mainFoodId, Long mainFood1id,
			Long mainDishId, Long mainDish1id, Long mainDish2id,
			Long mainDish3id, Long subDish1id, Long subDish2id,
			Long subDish3id, Long subDish4id, Long subDish5id,
			Long subDish6id, Long vegetableId, Long soupId,
			Long dessertId, Long dessert1id, Timestamp uploadDateTime,
			String lotNumber, String typeGrains, String typeOil,
			String typeVegetable, String typeMilk, String typeFruit,
			String typeMeatBeans, String calorie) {
		this.kitchenId = kitchenId;
		this.schoolId = schoolId;
		this.menuDate = menuDate;
		this.mainFoodId = mainFoodId;
		this.mainFood1id = mainFood1id;
		this.mainDishId = mainDishId;
		this.mainDish1id = mainDish1id;
		this.mainDish2id = mainDish2id;
		this.mainDish3id = mainDish3id;
		this.subDish1id = subDish1id;
		this.subDish2id = subDish2id;
		this.subDish3id = subDish3id;
		this.subDish4id = subDish4id;
		this.subDish5id = subDish5id;
		this.subDish6id = subDish6id;
		this.vegetableId = vegetableId;
		this.soupId = soupId;
		this.dessertId = dessertId;
		this.dessert1id = dessert1id;
		this.uploadDateTime = uploadDateTime;
		this.lotNumber = lotNumber;
		this.typeGrains = typeGrains;
		this.typeOil = typeOil;
		this.typeVegetable = typeVegetable;
		this.typeMilk = typeMilk;
		this.typeFruit = typeFruit;
		this.typeMeatBeans = typeMeatBeans;
		this.calorie = calorie;
	}

	// Property accessors

	public Long getBatchDataId() {
		return this.batchDataId;
	}

	public void setBatchDataId(Long batchDataId) {
		this.batchDataId = batchDataId;
	}

	public Integer getKitchenId() {
		return this.kitchenId;
	}

	public void setKitchenId(Integer kitchenId) {
		this.kitchenId = kitchenId;
	}

	public Integer getSchoolId() {
		return this.schoolId;
	}

	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}

	public String getMenuDate() {
		return this.menuDate;
	}

	public void setMenuDate(String menuDate) throws Exception{
		Pattern fmt_date=Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");
		if( fmt_date.matcher(menuDate).matches()){
			menuDate=menuDate.replace("-", "/");
		}
		
		this.menuDate = menuDate;
	}

	public Long getMainFoodId() {
		return this.mainFoodId;
	}

	public void setMainFoodId(Long mainFoodId) {
		this.mainFoodId = mainFoodId;
	}

	public Long getMainFood1id() {
		return this.mainFood1id;
	}

	public void setMainFood1id(Long mainFood1id) {
		this.mainFood1id = mainFood1id;
	}

	public Long getMainDishId() {
		return this.mainDishId;
	}

	public void setMainDishId(Long mainDishId) {
		this.mainDishId = mainDishId;
	}

	public Long getMainDish1id() {
		return this.mainDish1id;
	}

	public void setMainDish1id(Long mainDish1id) {
		this.mainDish1id = mainDish1id;
	}

	public Long getMainDish2id() {
		return this.mainDish2id;
	}

	public void setMainDish2id(Long mainDish2id) {
		this.mainDish2id = mainDish2id;
	}

	public Long getMainDish3id() {
		return this.mainDish3id;
	}

	public void setMainDish3id(Long mainDish3id) {
		this.mainDish3id = mainDish3id;
	}

	public Long getSubDish1id() {
		return this.subDish1id;
	}

	public void setSubDish1id(Long subDish1id) {
		this.subDish1id = subDish1id;
	}

	public Long getSubDish2id() {
		return this.subDish2id;
	}

	public void setSubDish2id(Long subDish2id) {
		this.subDish2id = subDish2id;
	}

	public Long getSubDish3id() {
		return this.subDish3id;
	}

	public void setSubDish3id(Long subDish3id) {
		this.subDish3id = subDish3id;
	}

	public Long getSubDish4id() {
		return this.subDish4id;
	}

	public void setSubDish4id(Long subDish4id) {
		this.subDish4id = subDish4id;
	}

	public Long getSubDish5id() {
		return this.subDish5id;
	}

	public void setSubDish5id(Long subDish5id) {
		this.subDish5id = subDish5id;
	}

	public Long getSubDish6id() {
		return this.subDish6id;
	}

	public void setSubDish6id(Long subDish6id) {
		this.subDish6id = subDish6id;
	}

	public Long getVegetableId() {
		return this.vegetableId;
	}

	public void setVegetableId(Long vegetableId) {
		this.vegetableId = vegetableId;
	}

	public Long getSoupId() {
		return this.soupId;
	}

	public void setSoupId(Long soupId) {
		this.soupId = soupId;
	}

	public Long getDessertId() {
		return this.dessertId;
	}

	public void setDessertId(Long dessertId) {
		this.dessertId = dessertId;
	}

	public Long getDessert1id() {
		return this.dessert1id;
	}

	public void setDessert1id(Long dessert1id) {
		this.dessert1id = dessert1id;
	}

	public Timestamp getUploadDateTime() {
		return this.uploadDateTime;
	}

	public void setUploadDateTime(Timestamp uploadDateTime) {
		this.uploadDateTime = uploadDateTime;
	}

	public String getLotNumber() {
		return this.lotNumber;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}

	public String getTypeGrains() {
		return this.typeGrains;
	}

	public void setTypeGrains(String typeGrains) {
		this.typeGrains = typeGrains;
	}

	public String getTypeOil() {
		return this.typeOil;
	}

	public void setTypeOil(String typeOil) {
		this.typeOil = typeOil;
	}

	public String getTypeVegetable() {
		return this.typeVegetable;
	}

	public void setTypeVegetable(String typeVegetable) {
		this.typeVegetable = typeVegetable;
	}

	public String getTypeMilk() {
		return this.typeMilk;
	}

	public void setTypeMilk(String typeMilk) {
		this.typeMilk = typeMilk;
	}

	public String getTypeFruit() {
		return this.typeFruit;
	}

	public void setTypeFruit(String typeFruit) {
		this.typeFruit = typeFruit;
	}

	public String getTypeMeatBeans() {
		return this.typeMeatBeans;
	}

	public void setTypeMeatBeans(String typeMeatBeans) {
		this.typeMeatBeans = typeMeatBeans;
	}

	public String getCalorie() {
		return this.calorie;
	}

	public void setCalorie(String calorie) {
		this.calorie = calorie;
	}
	public String getSrcType(){
		return this.srcType;
	}
	
	public void setSrcType(String srcType){
		this.srcType=srcType;
	}

	public Integer getMenuType() {
		return menuType;
	}

	public void setMenuType(Integer menuType) {
		this.menuType = menuType;
	}

	public Integer getEnable() {
		return enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

}
