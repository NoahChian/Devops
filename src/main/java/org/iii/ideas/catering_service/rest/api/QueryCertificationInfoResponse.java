package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

public class QueryCertificationInfoResponse  extends AbstractApiResponse {
	
	
	private List <CertificationList>certificationList = new ArrayList<CertificationList>();
	private Integer totalCol;
	
	public List <CertificationList> getCertificationList() {
		return certificationList;
	}
	public void setCertificationList(List <CertificationList> certificationList) {
		this.certificationList = certificationList;
	}
	public Integer getTotalCol() {
		return totalCol;
	}
	public void setTotalCol(Integer totalCol) {
		this.totalCol = totalCol;
	}
}
