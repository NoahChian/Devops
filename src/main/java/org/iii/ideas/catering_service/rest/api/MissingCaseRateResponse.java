package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

public class MissingCaseRateResponse  extends AbstractApiResponse{
	
	private static final long serialVersionUID = 613067573689647791L;
	
	private List <MICRate>MICRate = new ArrayList<MICRate>();
	private List <RateDetail>RateDetail = new ArrayList<RateDetail>();
	
	public List<MICRate> getMICRate() {
		return MICRate;
	}
	public void setMICRate(List<MICRate> mICRate) {
		this.MICRate = mICRate;
	}
	public List<RateDetail> getRateDetail() {
		return RateDetail;
	}
	public void setRateDetail(List<RateDetail> rateDetail) {
		RateDetail = rateDetail;
	}

}
