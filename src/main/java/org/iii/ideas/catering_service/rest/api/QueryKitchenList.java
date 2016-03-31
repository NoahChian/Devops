package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.Kitchen;
import org.iii.ideas.catering_service.util.AuthenUtil;
import org.iii.ideas.catering_service.util.CateringServiceUtil;

public class QueryKitchenList extends
		AbstractApiInterface<QueryKitchenListRequest, QueryKitchenListResponse> {

	@Override
	public void process() throws NamingException, ParseException {
		
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		//帶入使用者名稱，轉換為縣市代碼
		String countyID = "";
		countyID = AuthenUtil.getCountyTypeByUsername(this.getUsername());
		Criteria criteriaKT = this.dbSession.createCriteria(Kitchen.class);
		//如果不是系統管理員/中央機關，就要分縣市
		if(!"admin".equals(countyID)){ 
		criteriaKT.add(Restrictions.like("companyId", countyID+"%")); //單位編號(統編)第一碼為縣市代號
		}
		if(!CateringServiceUtil.isEmpty(this.requestObj.getKitchenName()) && !CateringServiceUtil.isNull(this.requestObj.getKitchenName())){
			criteriaKT.add(Restrictions.like("kitchenName", "%"+this.requestObj.getKitchenName()+"%")); 
		}
		if(!CateringServiceUtil.isEmpty(this.requestObj.getCompanyId()) && !CateringServiceUtil.isNull(this.requestObj.getCompanyId())){
			criteriaKT.add(Restrictions.like("companyId", "%"+this.requestObj.getCompanyId()+"%")); 
		}
		List<Kitchen> kitchens = criteriaKT.list();
		Iterator<Kitchen> iteratorSP = kitchens.iterator();
		log.debug("QueryKitchenList Size:" + kitchens.size());
		System.out.println("QueryKitchenList Size:" + kitchens.size());
		while (iteratorSP.hasNext()) {
			Kitchen kt = iteratorSP.next();
			KitchenList kitchenObj = new KitchenList();
			kitchenObj.setKitchenId(kt.getKitchenId());
			kitchenObj.setKitchenName(kt.getKitchenName());
			kitchenObj.setTel(kt.getTel());
			kitchenObj.setCompanyId(kt.getCompanyId());
			kitchenObj.setEmail(kt.getEmail());
			kitchenObj.setEnable(kt.getEnable());
			
			/*List<School> schools = HibernateUtil.querySchoolListByKitchenId(this.dbSession, kt.getKitchenId());
			Iterator<School> iteratorSC = schools.iterator();

			while (iteratorSC.hasNext()) {
				kitchenObj.getSchoolList().add(iteratorSC.next().getSchoolName());

			}*/
			
			
			this.responseObj.getKitchenList().add(kitchenObj);
		}
		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");
	}

}
