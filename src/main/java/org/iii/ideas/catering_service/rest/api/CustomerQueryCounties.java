package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.County;
import org.iii.ideas.catering_service.util.AuthenUtil;
import org.iii.ideas.catering_service.util.CateringServiceCode;

public class CustomerQueryCounties extends AbstractApiInterface<CustomerQueryCountiesRequest, CustomerQueryCountiesResponse> {

	
	@Override
	public void process() throws NamingException, ParseException {
		// select * from menu where MenuDate between [begDate] and [endDate] and SchoolId = [sid]
		
			    int condition = this.requestObj.getCondition();
			    

			    // 20140417 增加檢查目前登入身分與縣市  KC
			    String login_type="";
			    Boolean restrictFlag=false;
			    String countyCode="0";
			    String login_name="";
			    if (this.isLogin()){
			    	login_type=this.getUserType();
			    	login_name=this.getUsername();
			    }
 
			    countyCode=AuthenUtil.getCountyTypeByUsername(login_name);
			    Integer countyId=AuthenUtil.getCountryNumByCountyType(countyCode);
			    //Integer countyId=17;
				//學校身分登入時，不給全部的縣市清單
			    if (CateringServiceCode.USERTYPE_SCHOOL.equals(login_type) 
			    		|| (!CateringServiceCode.AUTHEN_NO_COUNTY.equals(countyCode) && !CateringServiceCode.AUTHEN_SUPER_COUNTY_TYPE.equals(countyCode)) ){
			    	restrictFlag=true;
			    }
			   
	    
				Criteria criteria = dbSession.createCriteria(County.class);
				criteria.add( Restrictions.ge("enable", condition) );
				
				//若有登入，則以登入身分過濾縣市清單 20140417
				if (restrictFlag){
					criteria.add(Restrictions.eq("countyId", countyId));
				} // Ric 20140421 依需求，消費者查詢暫時僅顯示台北市。  20140428 KC elseif增加判斷
				else if (!this.isLogin()){
					//criteria.add(Restrictions.eq("countyId", 19));
				}else{
					// 主管機關,團膳廚房登入 顯示全部縣市 (不限制)
				}
				
				
				
				
				List counties = criteria.list();
				
				Iterator<County> iterator = counties.iterator();
				while (iterator.hasNext()) {
					County ct = iterator.next();
					CountyObject  co = new CountyObject();
					co.setCid(String.valueOf( ct.getCountyId() ));
					co.setCountiesName(ct.getCounty());
					
					this.responseObj.getCounties().add(co);
				}
				this.responseObj.setMsg("");
				this.responseObj.setResStatus(1);
	}

}
