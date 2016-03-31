package org.iii.ideas.catering_service.rest.bo;

public class CustomerQuerySFBoProduct {
	private Long productId;
	private String name;
	private String preservedMethod;
	private String soldway;
	private String packages;
	private String certification;
	private String certificationId;
	private String image;
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPreservedMethod() {
		return preservedMethod;
	}
	public void setPreservedMethod(String preservedMethod) {
		this.preservedMethod = preservedMethod;
	}
	public String getSoldway() {
		return soldway;
	}
	public void setSoldway(String soldway) {
		this.soldway = soldway;
	}
	public String getPackages() {
		return packages;
	}
	public void setPackages(String packages) {
		this.packages = packages;
	}
	public String getCertification() {
		return certification;
	}
	public void setCertification(String certification) {
		this.certification = certification;
	}
	public String getCertificationId() {
		return certificationId;
	}
	public void setCertificationId(String certificationId) {
		this.certificationId = certificationId;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
}
