package org.iii.ideas.catering_service.dao;

/**
 * Area entity. @author MyEclipse Persistence Tools
 */
public class SfCountyProductCategory extends AbstractSfCountyProductCategory implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public SfCountyProductCategory() {
	}

	/** full constructor */
	public SfCountyProductCategory(long countyId, long productId, long categoryId) {
		super(countyId, productId, categoryId);
	}

}
