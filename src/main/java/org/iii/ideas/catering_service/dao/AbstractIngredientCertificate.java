package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;

public abstract class AbstractIngredientCertificate implements java.io.Serializable {

		// Fields

		/**
	 * 
	 */
	private static final long serialVersionUID = 5742748245386402899L;
		private Integer certId;
		private String certNo;
		private String certType;
		private Timestamp expiryDate;
		private String sourceXml;
		private Timestamp createDate;
		private String createUser;
		private Timestamp updateDate;
		private String updateUser;
		private String sourceJson;

		// Constructors

		/** default constructor */
		public AbstractIngredientCertificate() {
		}

		/** full constructor */
		public AbstractIngredientCertificate(Integer certId,String certNo,String certType,Timestamp expiryDate,Timestamp createDate,String createUser,Timestamp updateDate,	String updateUser,String sourceXml){
			this.certNo=certNo;
			this.certType=certType;
			this.expiryDate=expiryDate;			
			this.createUser=createUser;
			this.createDate=createDate;
			this.createDate=updateDate;
			this.updateUser=updateUser;
			this.sourceXml=sourceXml;
			
		}

		public Integer getCertId() {
			return certId;
		}

		public void setCertId(Integer certId) {
			this.certId = certId;
		}

		public String getCertNo() {
			return certNo;
		}

		public void setCertNo(String certNo) {
			this.certNo = certNo;
		}	

		public Timestamp getCreateDate() {
			return createDate;
		}

		public void setCreateDate(Timestamp createDate) {
			this.createDate = createDate;
		}

		public String getCreateUser() {
			return createUser;
		}

		public void setCreateUser(String createUser) {
			this.createUser = createUser;
		}

		public Timestamp getUpdateDate() {
			return updateDate;
		}

		public void setUpdateDate(Timestamp updateDate) {
			this.updateDate = updateDate;
		}

		public String getUpdateUser() {
			return updateUser;
		}

		public void setUpdateUser(String updateUser) {
			this.updateUser = updateUser;
		}

		public String getSourceXml() {
			return sourceXml;
		}

		public void setSourceXml(String sourceXml) {
			this.sourceXml = sourceXml;
		}

		public String getCertType() {
			return certType;
		}

		public void setCertType(String certType) {
			this.certType = certType;
		}

		public Timestamp getExpiryDate() {
			return expiryDate;
		}

		public void setExpiryDate(Timestamp expiryDate) {
			this.expiryDate = expiryDate;
		}

		public String getSourceJson() {
			return sourceJson;
		}

		public void setSourceJson(String sourceJson) {
			this.sourceJson = sourceJson;
		}
	}
