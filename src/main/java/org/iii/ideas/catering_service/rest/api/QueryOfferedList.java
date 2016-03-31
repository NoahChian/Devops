package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.iii.ideas.catering_service.dao.BatchdataDAO;
import org.iii.ideas.catering_service.dao.Kitchen;
import org.iii.ideas.catering_service.dao.KitchenDAO;
import org.iii.ideas.catering_service.dao.School;
import org.iii.ideas.catering_service.dao.SchoolDAO;
import org.iii.ideas.catering_service.dao.SchoolkitchenDAO;
import org.iii.ideas.catering_service.dao.UseraccountDAO;
import org.iii.ideas.catering_service.rest.bo.OfferedKitchenBO;
import org.iii.ideas.catering_service.util.AuthenUtil;
import org.iii.ideas.catering_service.util.CateringServiceUtil;

public class QueryOfferedList extends
		AbstractApiInterface<QueryOfferedListRequest, QueryOfferedListResponse> {

	@Override
	public void process() throws NamingException, ParseException {
		
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		//找出學校ID
		SchoolDAO sDao = new SchoolDAO(this.dbSession);
		//School school = sDao.querySchoolBySchoolCode(this.requestObj.getSchoolCode());
		School school = sDao.querySchoolBySchoolCode(this.getUsername().substring(1));
		if(CateringServiceUtil.isNull(school)){
			this.responseObj.setResStatus(-1);
			this.responseObj.setMsg("學校不存在。");
			return;
		}
		SchoolkitchenDAO skDao = new SchoolkitchenDAO(this.dbSession);
		List<Integer> kitchenIdList = skDao.queryKitchenListBySchool(school.getSchoolId());
		if(kitchenIdList.size() == 0){
			this.responseObj.setResStatus(-1);
			this.responseObj.setMsg("無被供餐資訊。");
			return;
		}
		KitchenDAO kDao = new KitchenDAO(this.dbSession);
		List<Kitchen> kitchenlist = kDao.queryKitchenByKitchenIdList(kitchenIdList);
		BatchdataDAO bDao = new BatchdataDAO(this.dbSession);
		for(int i =0;i<kitchenlist.size();i++){
			Kitchen obj = kitchenlist.get(i);
			OfferedKitchenBO bo =  new OfferedKitchenBO();
			bo.setSchoolId(school.getSchoolId());
			bo.setCompanyId(obj.getCompanyId());
			bo.setKitchenName(obj.getKitchenName());
			bo.setKitchenId(obj.getKitchenId());
			bo.setLastOfferedDate(bDao.queryLastMenuDateByKidAndSid(obj.getKitchenId(), school.getSchoolId()));
			this.responseObj.getKitchenBO().add(bo);
		}
		//this.responseObj.setKitchenList(kDao.queryKitchenByKitchenIdList(kitchenIdList));
		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");
	}

}
