package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.School;
import org.iii.ideas.catering_service.dao.SchoolDAO;
import org.iii.ideas.catering_service.dao.Schoolkitchen;
import org.iii.ideas.catering_service.dao.UserSchool;
import org.iii.ideas.catering_service.dao.UserSchoolDAO;
import org.iii.ideas.catering_service.dao.ViewKitchenUnionRestaurant;
import org.iii.ideas.catering_service.dao.ViewSchoolMenuWithBatchdata;
import org.iii.ideas.catering_service.util.AuthenUtil;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.iii.ideas.catering_service.util.CateringServiceUtil;

public class QuerySchoolList extends
		AbstractApiInterface<QuerySchoolListRequest, QuerySchoolListResponse> {

	@Override
	public void process() throws NamingException, ParseException {
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}		
		
		//改從帳號抓countyId  20140422 KC
		Integer cidd = Integer.valueOf(this.requestObj.getCid());//from requestObj
		Integer cid = AuthenUtil.getCountyNumByUsername(this.getUsername());
		String queryschoolname = this.requestObj.getQueryschoolname();
		String querySchoolId = this.requestObj.getSchoolId();
		
		String queryMode = this.requestObj.getQueryMode();
		
		Integer totalCount = 0;
		// 需要比較字串 因此先將輸入int改String
		String cidS = Integer.toString(cid);
		String userType = this.getUserType();
		SchoolDAO schoolDao = new SchoolDAO(dbSession);

		Criteria criteria = dbSession.createCriteria(School.class);
		if (!"".equals(cid)) {
			// 檢縣市
			// 檢是否啟用 CateringServiceCode.AUTHEN_SUPER_COUNTY
			if (CateringServiceCode.AUTHEN_SUPER_COUNTY.equals(cidS)) {
				// 檢是否啟用
				//criteria.add(Restrictions.eq("enable", 1)); 2014/05/09 Raymond 現在全部的學校都會query出來
				criteria.addOrder(Order.desc("enable"));//2014/05/09 Raymond 加狀態排序
				criteria.addOrder(Order.asc("schoolName"));
				if(cidd !=0){criteria.add(Restrictions.eq("countyId", cidd));}//若為中央則縣市改變學校 chu 20151124---
//				totalCount = schoolDao.queryTotelSchoolCount(null);
				totalCount = schoolDao.queryTotelSchoolCountv2(null,queryschoolname);
			} else if(CateringServiceCode.USERTYPE_SCHOOL.equals(userType)
					||CateringServiceCode.USERTYPE_SCHOOL_NO_KITCHEN.equals(userType)
					||CateringServiceCode.USERTYPE_KITCHEN.equals(userType)
					||CateringServiceCode.USERTYPE_COLLEGE_SCHOOL.contains(userType)) {
//				String schoolCode = this.getUsername().substring(1);
//				criteria.add(Restrictions.eq("schoolId", schoolId));
				int schoolId = 0;

				if(!CateringServiceUtil.isEmpty(this.requestObj.getSchoolId())){
					String [] numberStrs = this.requestObj.getSchoolId().split(",");
					Object[] numbers = new Object[numberStrs.length];
					for(int i = 0;i < numberStrs.length;i++)
					{
					   numbers[i] = Integer.parseInt(numberStrs[i]);
					}
					
					totalCount = numbers.length;
					criteria.add(Restrictions.in("schoolId", numbers));
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
					
					totalCount = 1;
					criteria.add(Restrictions.eq("schoolId", schoolId));
				}
			} else {
				//county 目前最大值是 40花蓮縣,由於怕跑到555 666 之類額外定義的 COUNTY_ID,所以加上此判斷
				if(cid != 0 && cid <= CateringServiceCode.COUNTY_NUM){
					criteria.add(Restrictions.eq("countyId", cid));
				}
				
				//criteria.add(Restrictions.eq("enable", 1)); //2014/05/09 Raymond 
				criteria.addOrder(Order.desc("enable"));//2014/05/09 Raymond 加狀態排序
				criteria.addOrder(Order.asc("schoolName"));
				
				totalCount = schoolDao.queryTotelSchoolCountv2(cid,queryschoolname);
			}
		} else {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("無查詢權限");
		}
		if(!CateringServiceUtil.isNull(queryschoolname) && !CateringServiceUtil.isEmpty(queryschoolname)){
			criteria.add(Restrictions.like("schoolName", '%'+queryschoolname+'%'));
		}
		if(this.requestObj.getPage() != 0 && this.requestObj.getPerpage() !=0){
			int startIndex = (this.requestObj.getPage()-1) * this.requestObj.getPerpage();
			criteria.setFirstResult(startIndex);
			criteria.setMaxResults(this.requestObj.getPerpage());
		}
		
		if("exceptUniversity".equals(queryMode)){
			criteria.add(Restrictions.sqlRestriction("length(schoolCode) > 4"));
		}

		List schools = criteria.list();
		Iterator<School> iterator = schools.iterator();

		while (iterator.hasNext()) {
			School sc = iterator.next();
			SchoolObject so = new SchoolObject();
			so.setSchoolName(sc.getSchoolName());
			so.setSid(sc.getSchoolId());
			so.setEnable(sc.getEnable());
			if(sc.getSchoolCode()!=null)//20140514 Raymond
				so.setSchoolCode(sc.getSchoolCode());
			else
				so.setSchoolCode("");
			this.responseObj.getSchool().add(so);
		}
		this.responseObj.setTotalCol(totalCount);
		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");

	}

}
