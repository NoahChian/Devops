package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Query;

public class QueryRolesList  extends AbstractApiInterface<QueryRolesListRequest, QueryRolesListResponse> {

	@Override
	public void process() throws NamingException, ParseException {
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		String HQL = "";
		HQL = "SELECT distinct roletype FROM Role";
		
		Query criteriaRT = dbSession.createQuery(HQL);
		List<RoleList> roleList = criteriaRT.list();
		
		List resultRT = criteriaRT.list();
        Iterator<String> iteratorRT = resultRT.iterator();
        while(iteratorRT.hasNext()){
			RoleList rt = new RoleList();
			String obj = iteratorRT.next();
			rt.setRoleList(obj);
			this.responseObj.getRoleList().add(rt);
		}
		
		this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);
		
		
	}

}
