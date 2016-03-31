package org.iii.ideas.catering_service.rest.api;

import javax.naming.NamingException;

public class QueryIngredientProperty extends AbstractApiInterface<QueryIngredientPropertyRequest, QueryIngredientPropertyResponse> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -486243880820844589L;

	@Override
	public void process() throws NamingException {
		String menuDate = "";
		
		// 檢核登入
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		
		

		
		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");

	}


	
}
