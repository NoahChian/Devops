package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.County;
import org.iii.ideas.catering_service.dao.School;
import org.iii.ideas.catering_service.dao.Schoolkitchen;
import org.iii.ideas.catering_service.dao.ViewKitchenUnionRestaurant;
import org.iii.ideas.catering_service.util.AuthenUtil;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.iii.ideas.catering_service.util.CateringServiceUtil;

public class QueryCounties extends
		AbstractApiInterface<QueryCountiesRequest, QueryCountiesResponse> {

	@Override
	public void process() throws NamingException, ParseException {
		// 需要比較字串 因此先將輸入int改String
		int condition = this.requestObj.getCondition();
		String conditionS = Integer.toString(this.requestObj.getCondition());
		
		//20140502 county 於UI不能帶，因此如果為0，於API中從登入的user去取得type Ric
		if("0".equals(conditionS)){
			condition = AuthenUtil.getCountyNumByUsername(this.getUsername());
		}
		
		Criteria criteria = dbSession.createCriteria(County.class);
		if (!"".equals(condition)) {
			// 檢縣市
			// 檢是否啟用
			if (CateringServiceCode.AUTHEN_SUPER_COUNTY.equals(Integer.toString(condition))) {
				// 檢是否啟用
				criteria.add(Restrictions.eq("enable", 1));
			} else {
				//county 目前最大值是 40花蓮縣,由於怕跑到555 666 之類額外定義的 COUNTY_ID,所以加上此判斷
				if(condition <= CateringServiceCode.COUNTY_NUM && condition != 0){
					criteria.add(Restrictions.eq("countyId", condition));
					
				} else if (!CateringServiceUtil.isEmpty(this.requestObj.getSchoolId())){
					//用schoolId查到該校的縣市作為下拉
					Criteria criteriaSC = this.dbSession.createCriteria(School.class);
					
					String [] numberStrs = this.requestObj.getSchoolId().split(",");
					Object[] numbers = new Object[numberStrs.length];
					for(int i = 0;i < numberStrs.length;i++)
					{
					   numbers[i] = Integer.parseInt(numberStrs[i]);
					}
					
					criteriaSC.add(Restrictions.in("schoolId", numbers));
					List<School> schoolList = criteriaSC.list();
					
					if(schoolList.size()!=0){
						criteria.add(Restrictions.eq("countyId", schoolList.get(0).getCountyId()));
					}
				}
				criteria.add(Restrictions.eq("enable", 1));
			}
		} else {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("無查詢權限");
		}
		List counties = criteria.list();

		Iterator<County> iterator = counties.iterator();
		while (iterator.hasNext()) {
			County ct = iterator.next();
			CountyObject co = new CountyObject();
			co.setCid(String.valueOf(ct.getCountyId()));
			co.setCountiesName(ct.getCounty());

			this.responseObj.getCounties().add(co);
		}
		this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);

	}

}
