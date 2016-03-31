package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Query;
import org.iii.ideas.catering_service.util.AuthenUtil;
import org.iii.ideas.catering_service.util.CateringServiceCode;


public class QueryAllKitchenList extends AbstractApiInterface<QueryAllKitchenListRequest, QueryAllKitchenListResponse> {

	@Override
	public void process() throws NamingException, ParseException {
		QueryAllKitchenListRequest refRequest= this.requestObj;	
		QueryAllKitchenListResponse refResponse = this.responseObj;

		if (!this.isLogin()) {
			refResponse.setResStatus("-2");
			refResponse.setMsg("使用者未授權");
			return;
		}
		
		
		
		if (this.getUserType()==null){
			//USERTYPE_SCHOOL_NO_KITCHEN
			refResponse.setResStatus("-2");
			refResponse.setMsg("使用者未授權");
			return;
		}
		
		String userType=this.getUserType();
		Integer userKitchenid=this.getKitchenId();
		Integer userSchoolid=0;
		
		refResponse.setResStatus("0");
		
		refResponse.setMsg("查詢失敗");
		if (refRequest.getKitchenType() == null) {
			return;
		}
		Integer countyId = AuthenUtil.getCountyNumByUsername(this.getUsername());
//		System.out.print("aaaaaaaaaaaaacountyId"+countyId);
		//String queryString = "from Kitchen k where k.kitchenType in (:type)";
		/* 
		 * 只跳出所屬縣市的供應商(kitchen)
		 * if countyId=9999查全部>>CateringServiceCode.AUTHEN_SUPER_COUNTY
		 */
		String queryString = "";
		if(CateringServiceCode.AUTHEN_NO_COUNTY.equals(countyId.toString())){
			return;
		}else if(CateringServiceCode.AUTHEN_SUPER_COUNTY.equals(countyId.toString())){  //中央主管機關
			queryString = "SELECT distinct k.kitchenId, k.kitchenName FROM Kitchen as k ,School as s , Schoolkitchen as sk "
					+" where k.kitchenId = sk.id.kitchenId and s.schoolId = sk.id.schoolId order by k.kitchenName ";
			//List<String> paramList = new ArrayList<String>();
			
		}else if (CateringServiceCode.USERTYPE_SCHOOL_NO_KITCHEN.equals(userType)){  //他校供應的廚房 20140501  KC 
			// add by Joshua 20141119
			Query companyIdQuery = dbSession.createQuery("select companyId from Kitchen where kitchenId=:kid ");
			companyIdQuery.setParameter("kid", userKitchenid);
			String companyId = companyIdQuery.uniqueResult().toString();
			companyId = companyId.substring(1); // remove first char of column named companyId of table kitchen 
			
			Query schoolIdQuery = dbSession.createQuery("select schoolId from School where schoolCode=:sCode");
			schoolIdQuery.setParameter("sCode", companyId);
			userSchoolid=(Integer) schoolIdQuery.uniqueResult();
			
			// edit by Joshua 20141119 以下method不採用  無法因應原本是自設廚房而後改成業者供餐狀況
//			Query tmpQuery = dbSession.createQuery("select sk.id.schoolId from Schoolkitchen sk where sk.id.kitchenId=:kid ");
//			tmpQuery.setParameter("kid",userKitchenid );
//			userSchoolid=(Integer) tmpQuery.uniqueResult();
			
			if (userSchoolid==null){
				refResponse.setResStatus("-2");
				refResponse.setMsg("此帳號(他校供應學校)無任何供餐廚房");
				return;
			}
			queryString = "SELECT distinct k.kitchenId, k.kitchenName FROM Kitchen as k ,School as s , Schoolkitchen as sk "
					+ " where k.kitchenId = sk.id.kitchenId and s.schoolId = sk.id.schoolId "
					+ " and sk.id.schoolId=:sid and sk.id.kitchenId!=:kid order by k.kitchenName ";
					//List<String> paramList = new ArrayList<String>();
		}else if(CateringServiceCode.USERTYPE_SCHOOL.equals(userType)){ //新增團膳及自設廚房之檢驗報告查詢 add by ellis 20150210 
			Query companyIdQuery = dbSession.createQuery("select companyId from Kitchen where kitchenId=:kid ");
			companyIdQuery.setParameter("kid", userKitchenid);
			String companyId = companyIdQuery.uniqueResult().toString();
			if(this.getUsername().equals(companyId)){ 
				companyId = companyId.substring(1);
			}
			
			Query schoolIdQuery = dbSession.createQuery("select schoolId from School where schoolCode=:sCode");
			schoolIdQuery.setParameter("sCode", companyId);
			userSchoolid=(Integer) schoolIdQuery.uniqueResult();
			
			if (userSchoolid==null){
				refResponse.setResStatus("-2");
				refResponse.setMsg("此帳號(他校供應學校)無任何供餐廚房");
				return;
			}
			queryString = "SELECT distinct k.kitchenId, k.kitchenName FROM Kitchen as k ,School as s , Schoolkitchen as sk "
					+ " where k.kitchenId = sk.id.kitchenId and s.schoolId = sk.id.schoolId "
					+ " and sk.id.schoolId=:sid order by k.kitchenName ";
			
		}else{  //地方政府登入
		
			queryString = "SELECT distinct k.kitchenId, k.kitchenName FROM Kitchen as k ,School as s , Schoolkitchen as sk "
			+ " where k.kitchenId = sk.id.kitchenId and s.schoolId = sk.id.schoolId and s.countyId = :countyId order by k.kitchenName ";
			//List<String> paramList = new ArrayList<String>();
		}
		
		Query query = dbSession.createQuery(queryString);
		if(!CateringServiceCode.AUTHEN_SUPER_COUNTY.equals(countyId.toString()) && !CateringServiceCode.USERTYPE_SCHOOL_NO_KITCHEN.equals(userType) && !CateringServiceCode.USERTYPE_SCHOOL.equals(userType)){
			query.setParameter("countyId", countyId);
		}else if (CateringServiceCode.USERTYPE_SCHOOL_NO_KITCHEN.equals(userType)){
			query.setParameter("sid", userSchoolid);
			query.setParameter("kid",userKitchenid );
		}else if (CateringServiceCode.USERTYPE_SCHOOL.equals(userType)){
			query.setParameter("sid", userSchoolid);
//			query.setParameter("kid",userKitchenid );
		}
		
		/*for (String obj : refRequest.getKitchenType()) {
			paramList.add(obj);
		}*/
		
		
		//query.setParameterList("type", paramList);
		
		/*List<Kitchen> kitchenList = query.list();
		
		for (Kitchen kitchen : kitchenList) {
			refResponse.getKitchen().add(new KitchenVo(kitchen.getKitchenId().toString(), kitchen.getKitchenName()));
		}*/
		List results = query.list();
        Iterator<Object[]> iterator = results.iterator();
      
        log.debug("QueryAllKitchen Size:"+results.size());
		while (iterator.hasNext()) {
			Object[] obj = iterator.next();
			KitchenVo kiVo = new KitchenVo(obj[0].toString(),obj[1].toString());
			refResponse.getKitchen().add(kiVo);   	
		}
		refResponse.setResStatus("1");
		
		refResponse.setMsg("查詢成功");
		
	}

}
