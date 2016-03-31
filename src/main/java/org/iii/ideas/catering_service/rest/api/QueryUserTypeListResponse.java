package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.iii.ideas.catering_service.dao.Usertype;

public class QueryUserTypeListResponse   extends AbstractApiResponse{
	List<Usertype> usertypelist = new ArrayList<Usertype>() ;

	public List<Usertype> getUsertypelist() {
		return usertypelist;
	}

	public void setUsertypelist(List<Usertype> usertypelist) {
		this.usertypelist = usertypelist;
	}
	
}

