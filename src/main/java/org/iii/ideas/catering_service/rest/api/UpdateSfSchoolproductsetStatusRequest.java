package org.iii.ideas.catering_service.rest.api;

public class UpdateSfSchoolproductsetStatusRequest {
	private Long sfschoolproductsetId;
	private String sfschoolproductsetDecision;

	public Long getSfschoolproductsetId() {
		return sfschoolproductsetId;
	}

	public void setSfschoolproductsetId(Long sfschoolproductsetId) {
		this.sfschoolproductsetId = sfschoolproductsetId;
	}

	public String getSfschoolproductsetDecision() {
		return sfschoolproductsetDecision;
	}

	public void setSfschoolproductsetDecision(String sfschoolproductsetDecision) {
		this.sfschoolproductsetDecision = sfschoolproductsetDecision;
	}

}
