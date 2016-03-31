package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.Batchdata;
import org.iii.ideas.catering_service.dao.Kitchen;
import org.iii.ideas.catering_service.dao.School;
import org.iii.ideas.catering_service.dao.Schoolkitchen;
import org.iii.ideas.catering_service.util.AuthenUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;

public class QueryKitchenList2 extends
		AbstractApiInterface<QueryKitchenList2Request, QueryKitchenList2Response> {

	@Override
	public void process() throws NamingException, ParseException {
		
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		//先找出學校代碼，準備反查供餐廚房
		Criteria criteriaSC = this.dbSession.createCriteria(Schoolkitchen.class);
		criteriaSC.add(Restrictions.like("id.kitchenId", this.getKitchenId()));
		List<Schoolkitchen> schoolList = criteriaSC.list();
		if (schoolList.size() == 0) {
			this.responseObj.setResStatus(-1);
			this.responseObj.setMsg("查無資料");
			return;
		}
		Schoolkitchen schoolkitchen = schoolList.get(0);
		Integer schoolId =  schoolkitchen.getId().getSchoolId();
		
		/*
		 * 20140807 Ric
		 * 用受供餐學校找出供餐的廚房清單
		 */
		String HQL = "from Kitchen k, Schoolkitchen sk  where sk.id.schoolId =:schoolId and k.kitchenId = sk.id.kitchenId  and k.kitchenType <> :kType";
		Query query = this.dbSession.createQuery(HQL);
		query.setParameter("schoolId", schoolId);
		query.setParameter("kType", "007");
			
		List results = query.list();
		Iterator<Object[]> iterator = results.iterator();
		while (iterator.hasNext()) {
			Object[] obj = iterator.next();
			Kitchen kt = (Kitchen) obj[0];
			KitchenList kitchenObj = new KitchenList();
			kitchenObj.setKitchenId(kt.getKitchenId());
			kitchenObj.setKitchenName(kt.getKitchenName());
			kitchenObj.setTel(kt.getTel());
			kitchenObj.setCompanyId(kt.getCompanyId());
			kitchenObj.setEmail(kt.getEmail());
			this.responseObj.getKitchenList().add(kitchenObj);
		}
		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");
	}

}
