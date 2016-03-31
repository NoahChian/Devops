package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;

import javax.naming.NamingException;

import org.iii.ideas.catering_service.dao.SeasoningStockDataDAO;

public class DeleteSeasoning extends AbstractApiInterface<DeleteSeasoningRequest, DeleteSeasoningResponse> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -252459111270754375L;

	@Override
	public void process() throws NamingException, ParseException {
		// TODO Auto-generated method stub
//		try{
//			SeasoningStockDataDAO sDAO = new SeasoningStockDataDAO(this.dbSession);
//			sDAO.deleteSeasoning(this.requestObj.getSeasoningstockId());
//			this.responseObj.setResStatus(1);
//			this.responseObj.setMsg("");
//		}catch(Exception ex){
//			this.responseObj.setResStatus(0);
//			this.responseObj.setMsg(ex.getMessage());
//		}
		
	}

}
