package org.iii.ideas.catering_service.dao;

/**
 * School entity. @author MyEclipse Persistence Tools
 */
public class ViewKitchenUnionRestaurant implements java.io.Serializable {
	
	private String id;
	private Integer schoolid;
	private Integer kitchenid;
	private String kitchenname;
	private String kitchentype;
	private Integer restaurantid;
	private String restaurantname;
	private Integer sfstreetid;
	private String sfstreetname;
	private Integer countyid;
	
	public static final String ID = "id";	//String
	public static final String SCHOOL_ID = "schoolid";	//String
	public static final String KITCHEN_ID = "kitchenid";	//String
	public static final String KITCHEN_NAME = "kitchenname";	//String
	public static final String RESTAURANT_ID = "restaurantid";	//String
	public static final String RESTAURANT_NAME = "restaurantname";	//String
	public static final String SF_STREET_ID = "sfstreetid"; // String
	public static final String SF_STREET_NAME = "sfstreetname"; // String
	public static final String COUNTY_ID = "countyid"; // String
	
	/** default constructor */
	public ViewKitchenUnionRestaurant() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getKitchenid() {
		return kitchenid;
	}

	public void setKitchenid(Integer kitchenid) {
		this.kitchenid = kitchenid;
	}

	public String getKitchenname() {
		return kitchenname;
	}

	public void setKitchenname(String kitchenname) {
		this.kitchenname = kitchenname;
	}

	public String getKitchentype() {
		return kitchentype;
	}

	public void setKitchentype(String kitchentype) {
		this.kitchentype = kitchentype;
	}

	public Integer getRestaurantid() {
		return restaurantid;
	}

	public void setRestaurantid(Integer restaurantid) {
		this.restaurantid = restaurantid;
	}

	public String getRestaurantname() {
		return restaurantname;
	}

	public void setRestaurantname(String restaurantname) {
		this.restaurantname = restaurantname;
	}

	public Integer getSchoolid() {
		return schoolid;
	}

	public void setSchoolid(Integer schoolid) {
		this.schoolid = schoolid;
	}

	public Integer getSfstreetid() {
		return sfstreetid;
	}

	public void setSfstreetid(Integer sfstreetid) {
		this.sfstreetid = sfstreetid;
	}

	public String getSfstreetname() {
		return sfstreetname;
	}

	public void setSfstreetname(String sfstreetname) {
		this.sfstreetname = sfstreetname;
	}

	public Integer getCountyid() {
		return countyid;
	}

	public void setCountyid(Integer countyid) {
		this.countyid = countyid;
	}
}
