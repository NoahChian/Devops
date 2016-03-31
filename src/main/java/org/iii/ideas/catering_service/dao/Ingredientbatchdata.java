package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;

/**
 * Ingredientbatchdata entity. @author MyEclipse Persistence Tools
 */
public class Ingredientbatchdata extends AbstractIngredientbatchdata implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public Ingredientbatchdata() {
	}

	/**old full constructor */
	/*
	public Ingredientbatchdata(Integer batchDataId, Long dishId,
			Integer ingredientId, String ingredientName, Timestamp stockDate,
			Timestamp manufactureDate, Timestamp expirationDate,
			String lotNumber, String brand, String origin, String source,
			Integer supplierId, String sourceCertification,
			String certificationId, Integer menuId,String supplierCompanyId) {
		super(batchDataId, dishId, ingredientId, ingredientName, stockDate,
				manufactureDate, expirationDate, lotNumber, brand, origin,
				source, supplierId, sourceCertification, certificationId,
				menuId,supplierCompanyId);
	}
	*/	
	/**new full constructor  
	 * Add supplierName    20140306 KC  
	 * Add brandName       20140310 KC
	 * */
	/*
	public Ingredientbatchdata(Integer batchDataId, Long dishId,
			Integer ingredientId, String ingredientName, Timestamp stockDate,
			Timestamp manufactureDate, Timestamp expirationDate,
			String lotNumber, String brand, String origin, String source,
			Integer supplierId, String sourceCertification,
			String certificationId, Integer menuId,String supplierCompanyId,String supplierName,String brandNo) {
		super(batchDataId, dishId, ingredientId, ingredientName, stockDate,
				manufactureDate, expirationDate, lotNumber, brand, origin,
				source, supplierId, sourceCertification, certificationId,
				menuId,supplierCompanyId,supplierName,brandNo);
	}
	*/	

}
