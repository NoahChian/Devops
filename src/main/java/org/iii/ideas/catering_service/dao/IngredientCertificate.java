package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;

/**
 * Ingredientbatchdata entity. @author MyEclipse Persistence Tools
 */
public class IngredientCertificate extends AbstractIngredientCertificate implements java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = -3041581695749955460L;

	/** default constructor */
	public IngredientCertificate() {
	}

	/** old full constructor */
	public IngredientCertificate(Integer certId, String certNo, String certType,Timestamp expiryDate, Timestamp createDate, String createUser,
			Timestamp updateDate, String updateUser,String sourceXml) {
		super(certId, certNo, certType, expiryDate, createDate, createUser, updateDate, updateUser,sourceXml);
	}

}
