package org.iii.ideas.catering_service.dao;

/**
 * SfCountyProductSet entity. @author MyEclipse Persistence Tools
 */
public class SfCountyProductSet extends AbstractSfCountyProductSet implements java.io.Serializable {

	// Constructors
	/** default constructor */
	public SfCountyProductSet() {
	}

	/** full constructor */
	public SfCountyProductSet(int countyId, long productId, String onShelfDate) {
		super(countyId, productId, onShelfDate);
	}
	public SfCountyProductSet(int countyId, long productId, String onShelfDate, String offShelfDate) {
		super(countyId, productId, onShelfDate, offShelfDate);
	}
}
