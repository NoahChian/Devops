package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;

import org.iii.ideas.catering_service.dao.NegIngredientId;

/**
 * AbstractNegIngredient entity provides the base persistence definition of the
 * NegIngredient entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractNegIngredient implements java.io.Serializable {

	// Fields

	private NegIngredientId id;
	private Integer supplierId;
	private Timestamp begDate;
	private Timestamp endDate;
	private Timestamp createtime;
	private Timestamp manufactureDate;
	private Timestamp expirationDate;
	private String lotNumber;
	private String description;

	// Constructors

	/** default constructor */
	public AbstractNegIngredient() {
	}

	/** minimal constructor */
	public AbstractNegIngredient(NegIngredientId id, Integer supplierId) {
		this.id = id;
		this.supplierId = supplierId;
	}

	/** full constructor */
	public AbstractNegIngredient(NegIngredientId id, Integer supplierId,
			Timestamp begDate, Timestamp endDate, Timestamp createtime,
			Timestamp manufactureDate, Timestamp expirationDate,
			String lotNumber, String description) {
		this.id = id;
		this.supplierId = supplierId;
		this.begDate = begDate;
		this.endDate = endDate;
		this.createtime = createtime;
		this.manufactureDate = manufactureDate;
		this.expirationDate = expirationDate;
		this.lotNumber = lotNumber;
		this.description = description;
	}

	// Property accessors

	public NegIngredientId getId() {
		return this.id;
	}

	public void setId(NegIngredientId id) {
		this.id = id;
	}

	public Integer getSupplierId() {
		return this.supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public Timestamp getBegDate() {
		return this.begDate;
	}

	public void setBegDate(Timestamp begDate) {
		this.begDate = begDate;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public Timestamp getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	public Timestamp getManufactureDate() {
		return this.manufactureDate;
	}

	public void setManufactureDate(Timestamp manufactureDate) {
		this.manufactureDate = manufactureDate;
	}

	public Timestamp getExpirationDate() {
		return this.expirationDate;
	}

	public void setExpirationDate(Timestamp expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getLotNumber() {
		return this.lotNumber;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}