package org.iii.ideas.catering_service.dao;

/**
 * AbstractSupplier entity provides the base persistence definition of the
 * Supplier entity. @author MyEclipse Persistence Tools
 */
import java.sql.Timestamp;

public abstract class AbstractSupplier implements java.io.Serializable {

	// Fields

	private SupplierId id;
	private String supplierName;
	private String ownner;
	private String companyId;
	private Integer countyId;
	private Integer areaId;
	private String supplierAdress;
	private String supplierTel;
	private String supplierCertification;
	private String suppliedType = "0";
	private String fdaCompanyId;
	private Timestamp fdaUpdateTime;
	private String fdaInfoUpdater;


		
	// Constructors

	/** default constructor */
	public AbstractSupplier() {
	}

	/** full constructor */
	public AbstractSupplier(SupplierId id, String supplierName, String ownner, 
			String companyId, Integer countyId, Integer areaId,
			String supplierAdress, String supplierTel,
			String supplierCertification, 
			String fdaCompanyId,Timestamp fdaUpdateTime,String fdaInfoUpdater) {  //add fdaInfo
		this.id = id;
		this.supplierName = supplierName;
		this.ownner = ownner;
		this.companyId = companyId;
		this.countyId = countyId;
		this.areaId = areaId;
		this.supplierAdress = supplierAdress;
		this.supplierTel = supplierTel;
		this.supplierCertification = supplierCertification;
		this.fdaCompanyId = fdaCompanyId;
		this.fdaInfoUpdater = fdaInfoUpdater;
		this.fdaUpdateTime = fdaUpdateTime;
	}


	
	// Property accessors

	public Integer getSupplierId() {
		return this.getId().getSupplierId();
	}

	public void setSupplierId(Integer supplierId) {
		this.getId().setSupplierId(supplierId);
	}

	public SupplierId getId() {
		return this.id;
	}

	public void setId(SupplierId id) {
		this.id = id;
	}

	

	
	
	public String getSupplierName() {
		return this.supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getOwnner() {
		return this.ownner;
	}

	public void setOwnner(String ownner) {
		this.ownner = ownner;
	}
	public String getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public Integer getCountyId() {
		return this.countyId;
	}

	public void setCountyId(Integer countyId) {
		this.countyId = countyId;
	}

	public Integer getAreaId() {
		return this.areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public String getSupplierAdress() {
		return this.supplierAdress;
	}

	public void setSupplierAdress(String supplierAdress) {
		this.supplierAdress = supplierAdress;
	}

	public String getSupplierTel() {
		return this.supplierTel;
	}

	public void setSupplierTel(String supplierTel) {
		this.supplierTel = supplierTel;
	}

	public String getSupplierCertification() {
		return this.supplierCertification;
	}

	public void setSupplierCertification(String supplierCertification) {
		this.supplierCertification = supplierCertification;
	}

	public String getSuppliedType() {
		return suppliedType;
	}

	public void setSuppliedType(String suppliedType) {
		this.suppliedType = suppliedType;
	}
	
	public String getFdaCompanyId() {
		return this.fdaCompanyId;
	}

	public void setFdaCompanyId(String fdaCompanyId) {
		this.fdaCompanyId = fdaCompanyId;
	}

	public Timestamp getFdaUpdateTime() {
		return this.fdaUpdateTime;
	}

	public void setFdaUpdateTime(Timestamp fdaUpdateTime) {
		this.fdaUpdateTime = fdaUpdateTime;
	}

	public String getFdaInfoUpdater() {
		return this.fdaInfoUpdater;
	}

	public void setFdaInfoUpdater(String fdaInfoUpdater) {
		this.fdaInfoUpdater = fdaInfoUpdater;
	}

}
