package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Transaction;
import org.iii.ideas.catering_service.dao.Navi;
import org.iii.ideas.catering_service.service.QueryNaviAuthorityService;
import org.iii.ideas.catering_service.util.CateringServiceUtil;

public class QueryNaviAuthority extends AbstractApiInterface<QueryNaviAuthorityRequest, QueryNaviAuthorityResponse>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3330615501383341649L;

	@Override
	public void process() throws NamingException, ParseException {
		
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		String username = getUsername();
		String userType = getUserType();
		QueryNaviAuthorityService queryNaviAuthorityService = new QueryNaviAuthorityService(dbSession);
		try{
			List<Navi> menuTree = queryNaviAuthorityService.queryNaviAuth(username);
			
			if(CateringServiceUtil.isNull(menuTree)){
				this.responseObj.setMsg("使用者未授權功能選單項目");
			}else{
				this.responseObj.setMenuTree(menuTree);
				this.responseObj.setMsg("");
			}
			this.responseObj.setResStatus(1);
			this.responseObj.setUsername(username);
			this.responseObj.setUserType(userType);
			
		}catch(Exception e){
			e.printStackTrace();
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("刪除失敗,請洽系統管理員");
		}
		
	}
}
