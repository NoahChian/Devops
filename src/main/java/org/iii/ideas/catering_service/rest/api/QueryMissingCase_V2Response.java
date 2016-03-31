package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

public class QueryMissingCase_V2Response  extends AbstractApiResponse{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 613067573689647791L;
	private List <nullBatchdataList>nullBatchdataList = new ArrayList<nullBatchdataList>();
	
	public List<nullBatchdataList> getNullBatchdataList() {
		return nullBatchdataList;
	}
	public void setNullBatchdataList(List<nullBatchdataList> nullBatchdataList) {
		this.nullBatchdataList = nullBatchdataList;
	}

}
