package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.Batchdata;
import org.iii.ideas.catering_service.dao.Kitchen;
import org.iii.ideas.catering_service.dao.School;
import org.iii.ideas.catering_service.dao.Schoolkitchen;
import org.iii.ideas.catering_service.dao.UserSchool;
import org.iii.ideas.catering_service.dao.ViewKitchenUnionRestaurant;
import org.iii.ideas.catering_service.util.AuthenUtil;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;

public class QueryKitchenRestaurantList extends
		AbstractApiInterface<QueryKitchenRestaurantListRequest, QueryKitchenRestaurantListResponse> {

	@Override
	public void process() throws NamingException, ParseException {
		
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		
		Integer cid = this.getCounty();

		Integer schoolId = 0;
		if(!CateringServiceUtil.isEmpty(this.requestObj.getSchoolId())){
//			schoolId = Integer.valueOf(this.requestObj.getSchoolId());
		} else {
			//先找出學校代碼，準備反查供餐廚房
			Criteria criteriaSC = this.dbSession.createCriteria(Schoolkitchen.class);
			criteriaSC.add(Restrictions.like("id.kitchenId", this.getKitchenId()));
			List<Schoolkitchen> schoolList = criteriaSC.list();
			
			criteriaSC = this.dbSession.createCriteria(UserSchool.class);
			criteriaSC.add(Restrictions.like(UserSchool.USER_NAME, this.getUsername()));
			List<UserSchool> userSchoolList = new ArrayList<UserSchool>();
			userSchoolList = criteriaSC.list();
			
			if(schoolList.size() != 0){
				Schoolkitchen schoolkitchen = schoolList.get(0);
				schoolId =  schoolkitchen.getId().getSchoolId();
			} else if(userSchoolList.size() != 0){
				UserSchool userschool = userSchoolList.get(0);
				schoolId =  userschool.getSchoolId();
			}
		}
		
		Criteria criteria = this.dbSession.createCriteria(ViewKitchenUnionRestaurant.class);
		if(!CateringServiceUtil.isEmpty(this.requestObj.getSchoolId())){
			String [] numberStrs = this.requestObj.getSchoolId().split(",");
			Object[] numbers = new Object[numberStrs.length];
			for(int i = 0;i < numberStrs.length;i++)
			{
			   numbers[i] = Integer.parseInt(numberStrs[i]);
			}
			criteria.add(Restrictions.in(ViewKitchenUnionRestaurant.SCHOOL_ID, numbers));
		} else if(schoolId != 0){
			criteria.add(Restrictions.eq(ViewKitchenUnionRestaurant.SCHOOL_ID, schoolId));
		}
		criteria.addOrder(Order.desc(ViewKitchenUnionRestaurant.KITCHEN_ID));
		criteria.addOrder(Order.desc(ViewKitchenUnionRestaurant.RESTAURANT_ID));
		
		//只撈取各縣市的資料
		if(cid != 0 && cid <= CateringServiceCode.COUNTY_NUM){
			criteria.add(Restrictions.eq(ViewKitchenUnionRestaurant.COUNTY_ID, cid));
		}
		
		List<ViewKitchenUnionRestaurant> results = criteria.list();
		List<ViewKitchenUnionRestaurant> finalResults = new ArrayList();
		
		if(results != null){
			//把重複的自設廚房做distinct
			for(int i=0; i+1 <results.size(); i++){
				ViewKitchenUnionRestaurant temp1 = results.get(i);
				ViewKitchenUnionRestaurant temp2 = results.get(i+1);
				
				Integer kitchen1 = temp1.getKitchenid();
				Integer kitchen2 = temp2.getKitchenid();
				Integer restaurant1 = temp1.getRestaurantid();
				Integer restaurant2 = temp2.getRestaurantid();
				
				//重複的都先去除吧
				if(restaurant1 != null){
					if(restaurant2 == null){
						finalResults.add(temp1);
					} else if(restaurant1.compareTo(restaurant2) != 0){
						finalResults.add(temp1);
					}
				} else if(kitchen1 != null){
					if(kitchen2 == null){
						finalResults.add(temp1);
					} else if(kitchen1.compareTo(kitchen2) != 0){
						finalResults.add(temp1);
					}
				}
			}
			
			if(!finalResults.isEmpty()){
				results = finalResults;
			}
		}
	
		// #13612 縣市主管機關(user type長度6碼)查詢廚房數量及清單,預期只出現團膳業者(KitchenType = 005)
		if (("" + this.getUserType()).length() == 6) {
			if (results != null && results.size() > 0) {
				List<ViewKitchenUnionRestaurant> tmpList = new ArrayList<ViewKitchenUnionRestaurant>(results);
				results = new ArrayList<ViewKitchenUnionRestaurant>();
				for (int i = 0; i < tmpList.size(); i++) {
					ViewKitchenUnionRestaurant bo = tmpList.get(i);
					if ("005".equals("" + bo.getKitchentype())) {
						results.add(bo);
					}
				}
			}
		}
		Iterator<ViewKitchenUnionRestaurant> iterator = results.iterator();
		while (iterator.hasNext()) {
			ViewKitchenUnionRestaurant vk = iterator.next();
			this.responseObj.getKitchenRestaurantList().add(vk);
		}
		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");
	}

}
