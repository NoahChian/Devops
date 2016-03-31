package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;

/**
 * Kitchen entity. @author MyEclipse Persistence Tools
 */
public class Kitchen extends AbstractKitchen implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public Kitchen() {
	}

	/** minimal constructor */
	public Kitchen(String kitchenName, String kitchenType, String address,
			String ownner, String tel, String fax, String nutritionist,
			String chef, String haccp, String qualifier, String insurement,
			String picturePath, Timestamp createDate,String companyId) {
		super(kitchenName, kitchenType, address, ownner, tel, fax,
				nutritionist, chef, haccp, qualifier, insurement, picturePath,
				createDate,companyId);
	}

	/** full constructor */
	public Kitchen(String kitchenName, String kitchenType, String address,
			String ownner, String tel, String fax, String nutritionist,
			String chef, String haccp, String qualifier, String insurement,
			String picturePath, Timestamp createDate, Timestamp endDate,
			String companyId,String email) {
		super(kitchenName, kitchenType, address, ownner, tel, fax,
				nutritionist, chef, haccp, qualifier, insurement, picturePath,
				createDate, endDate,companyId,email);
	}

}
