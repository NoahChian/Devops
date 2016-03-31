package org.iii.ideas.catering_service.dao;

/**
 * ViewSchoolMenu entity. @author MyEclipse Persistence Tools
 */

public class ViewSchoolMenu implements java.io.Serializable {

	// Fields

		private Integer id;
		private String menudate;
		private Integer kitchenid;
		private Integer schoolid;
		private String schoolcode;
		private Long batchdataid;
		private String mainFoolId;
		private String mainFood1id;
		private String mainDishId;
		private String mainDish1id;
		private String mainDish2id;
		private String mainDish3id;
		private String subDish1id;
		private String subDish2id;
		private String subDish3id;
		private String subDish4id;
		private String subDish5id;
		private String subDish6id;
		private String vegetableId;
		private String soupId;
		private String dessertId;
		private String dessert1id;

		// Constructors

		/** default constructor */
		public ViewSchoolMenu() {
		}

		/** minimal constructor */
		public ViewSchoolMenu(Long batchdataid) {
			this.batchdataid = batchdataid;
		}

		/** full constructor */
		public ViewSchoolMenu(Long batchdataid, String mainFoolId,
				String mainFood1id, String mainDishId, String mainDish1id,
				String mainDish2id, String mainDish3id, String subDish1id,
				String subDish2id, String subDish3id, String subDish4id,
				String subDish5id, String subDish6id, String vegetableId,
				String soupId, String dessertId, String dessert1id) {
			this.batchdataid = batchdataid;
			this.mainFoolId = mainFoolId;
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
		}

		// Property accessors

		public Long getBatchdataid() {
			return this.batchdataid;
		}

		public void setBatchdataid(Long batchdataid) {
			this.batchdataid = batchdataid;
		}

		public String getMainFoolId() {
			return this.mainFoolId;
		}

		public void setMainFoolId(String mainFoolId) {
			this.mainFoolId = mainFoolId;
		}

		public String getMainFood1id() {
			return this.mainFood1id;
		}

		public void setMainFood1id(String mainFood1id) {
			this.mainFood1id = mainFood1id;
		}

		public String getMainDishId() {
			return this.mainDishId;
		}

		public void setMainDishId(String mainDishId) {
			this.mainDishId = mainDishId;
		}

		public String getMainDish1id() {
			return this.mainDish1id;
		}

		public void setMainDish1id(String mainDish1id) {
			this.mainDish1id = mainDish1id;
		}

		public String getMainDish2id() {
			return this.mainDish2id;
		}

		public void setMainDish2id(String mainDish2id) {
			this.mainDish2id = mainDish2id;
		}

		public String getMainDish3id() {
			return this.mainDish3id;
		}

		public void setMainDish3id(String mainDish3id) {
			this.mainDish3id = mainDish3id;
		}

		public String getSubDish1id() {
			return this.subDish1id;
		}

		public void setSubDish1id(String subDish1id) {
			this.subDish1id = subDish1id;
		}

		public String getSubDish2id() {
			return this.subDish2id;
		}

		public void setSubDish2id(String subDish2id) {
			this.subDish2id = subDish2id;
		}

		public String getSubDish3id() {
			return this.subDish3id;
		}

		public void setSubDish3id(String subDish3id) {
			this.subDish3id = subDish3id;
		}

		public String getSubDish4id() {
			return this.subDish4id;
		}

		public void setSubDish4id(String subDish4id) {
			this.subDish4id = subDish4id;
		}

		public String getSubDish5id() {
			return this.subDish5id;
		}

		public void setSubDish5id(String subDish5id) {
			this.subDish5id = subDish5id;
		}

		public String getSubDish6id() {
			return this.subDish6id;
		}

		public void setSubDish6id(String subDish6id) {
			this.subDish6id = subDish6id;
		}

		public String getVegetableId() {
			return this.vegetableId;
		}

		public void setVegetableId(String vegetableId) {
			this.vegetableId = vegetableId;
		}

		public String getSoupId() {
			return this.soupId;
		}

		public void setSoupId(String soupId) {
			this.soupId = soupId;
		}

		public String getDessertId() {
			return this.dessertId;
		}

		public void setDessertId(String dessertId) {
			this.dessertId = dessertId;
		}

		public String getDessert1id() {
			return this.dessert1id;
		}

		public void setDessert1id(String dessert1id) {
			this.dessert1id = dessert1id;
		}

		public String getMenudate() {
			return menudate;
		}

		public void setMenudate(String menudate) {
			this.menudate = menudate;
		}

		public Integer getKitchenid() {
			return kitchenid;
		}

		public void setKitchenid(Integer kitchenid) {
			this.kitchenid = kitchenid;
		}

		public Integer getSchoolid() {
			return schoolid;
		}

		public void setSchoolid(Integer schoolid) {
			this.schoolid = schoolid;
		}

		public String getSchoolcode() {
			return schoolcode;
		}

		public void setSchoolcode(String schoolcode) {
			this.schoolcode = schoolcode;
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}
}