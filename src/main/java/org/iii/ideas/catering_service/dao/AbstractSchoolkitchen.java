package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;

/**
 * AbstractSchoolkitchen entity provides the base persistence definition of the
 * Schoolkitchen entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractSchoolkitchen implements java.io.Serializable {

	// Fields

	private SchoolkitchenId id;
	private Timestamp createDate;
	private Timestamp endDate;
	private Integer quantity=1;
	private Integer offered;
	

	// Constructors

	/** default constructor */
	public AbstractSchoolkitchen() {
	}

	/** minimal constructor */
	public AbstractSchoolkitchen(SchoolkitchenId id) {
		this.id = id;
	}

	/** full constructor */
	public AbstractSchoolkitchen(SchoolkitchenId id, Timestamp createDate,
			Timestamp endDate) {
		this.id = id;
		this.createDate = createDate;
		this.endDate = endDate;
	}

	// Property accessors

	public SchoolkitchenId getId() {
		return this.id;
	}

	public void setId(SchoolkitchenId id) {
		this.id = id;
	}

	public Timestamp getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public void setQuantity(Integer quantity){
		this.quantity=quantity;
	}
	public Integer getQuantity(){
		return this.quantity;
	}

	public Integer getOffered() {
		return offered;
	}

	public void setOffered(Integer offered) {
		this.offered = offered;
	}
	
}