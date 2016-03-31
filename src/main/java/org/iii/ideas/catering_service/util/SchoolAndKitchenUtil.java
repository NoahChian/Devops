package org.iii.ideas.catering_service.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.iii.ideas.catering_service.dao.Code;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.Kitchen;
import org.iii.ideas.catering_service.dao.School;
import org.iii.ideas.catering_service.dao.SchoolDAO;
import org.iii.ideas.catering_service.dao.Schoolkitchen;
import org.iii.ideas.catering_service.rest.bo.KitchenBO;
import org.iii.ideas.catering_service.rest.bo.SchoolDataBO;

public class SchoolAndKitchenUtil {
	
	/*
	 * 查詢縣市學校資料 
	 * by   schoolId 
	 * 
	 * */
	public static SchoolDataBO querySchoolDataBySchoolId(Session dbsession, int schoolId) {				
		String hql="From School s,Kitchen k,Code c  where c.type='county' and c.name=s.countyId "
				+ " and concat(c.code,s.schoolCode)=k.companyId and  s.schoolId=:schoolId ";
		Query querySch=dbsession.createQuery(hql);
		querySch.setParameter("schoolId", schoolId);
		//querySch.setParameter("countyType", countyType);
		Object[] row=(Object[] ) querySch.uniqueResult();
		if (row==null){
			return null;
		}
		return setSchoolDataBO(dbsession,(School) row[0],(Kitchen) row[1]);
		
		
	}
	/*
	 * 查詢縣市學校資料 
	 * by   學校代碼
	 * 
	 * */
	public static SchoolDataBO querySchoolDataBySchoolCode(Session dbsession, String schoolCode) {		
		String hql="From School s,Kitchen k,Code c  where c.type='county' and c.name=s.countyId and concat(c.code,s.schoolCode)=k.companyId and  s.schoolCode=:schoolCode ";
		Query querySch=dbsession.createQuery(hql);
		querySch.setParameter("schoolCode", schoolCode);
		Object[] row=(Object[] ) querySch.uniqueResult();
		if (row==null){
			return null;
		}
		return setSchoolDataBO(dbsession,(School) row[0],(Kitchen) row[1]);
	}
	
	/*
	 * 查詢縣市學校資料 list
	 * by   縣市代碼 (countyId)
	 * 
	 * */	
	public static List<SchoolDataBO> querySchoolDataListByCountyId(Session dbsession,Integer countyId){
		//Session dbsession,Integer schoolId,Integer kitchenId ,Integer schoolType ,String kitchenType,Integer countyId
		List<SchoolDataBO> result=SchoolAndKitchenUtil.querySchoolDataListByConditions(dbsession,0 ,0,0,"", countyId);
		return result;
	}
	
	/*
	 * 查詢學校資料 list
	 * by 學校屬性  (schooltype), 縣市代碼 (countyId)
	 * 
	 * */
	public static List<SchoolDataBO> querySchoolDataListBySchooltypeAndCountyid(Session dbsession,Integer schoolType,Integer countyId){
		List<SchoolDataBO> result=SchoolAndKitchenUtil.querySchoolDataListByConditions(dbsession,0,0,schoolType ,"", countyId);
		return result;
	}
	
	/*
	 * 裝載school data 到 SchoolDataBO
	 * */
	private static SchoolDataBO setSchoolDataBO(Session dbsession ,School school,Kitchen kitchenOfSchool){

		SchoolDataBO schooldata=new SchoolDataBO();
		schooldata.setEnable(school.getEnable());
		schooldata.setSchoolCode(school.getSchoolCode());
		schooldata.setSchoolId(school.getSchoolId());
		schooldata.setSchoolName(school.getSchoolName());
		schooldata.setKitchenTypeOfSchool(kitchenOfSchool.getKitchenType());
		
	    String hql="From Schoolkitchen sk ,Kitchen k,Code c "
				+ " where sk.id.schoolId=:schoolId and sk.id.kitchenId=k.kitchenId "
				+ " and k.kitchenType <> :kitchenType "
				+ " and k.kitchenType=c.code and c.type='KitchenType' ";		

		Query query=dbsession.createQuery(hql);
		query.setParameter("schoolId", school.getSchoolId());
		query.setParameter("kitchenType",CateringServiceCode.USERTYPE_SCHOOL_NO_KITCHEN);
		List result = query.list();
		
		Iterator ir=result.iterator();
		while(ir.hasNext()){
			Object[] row=(Object[]) ir.next();
			KitchenBO kbo=new KitchenBO();
			Schoolkitchen sk =(Schoolkitchen) row[0];
			Kitchen  kitchen =(Kitchen) row[1];
			Code code=(Code) row[2];
			kbo.setCompanyId(kitchen.getCompanyId());
			kbo.setKitchenId(kitchen.getKitchenId());
			kbo.setKitchenName(kitchen.getKitchenName());
			kbo.setKitchenType(code.getName());
			kbo.setSupplierQuantity(sk.getQuantity());
			schooldata.getKitchens().add(kbo);
		}
		return schooldata;
	}
	
	/*
	 * 依據條件查詢school對應的資料 (schoolType,kitchenType,countyId)
	 * */
	public static List<SchoolDataBO> querySchoolDataListByConditions(Session dbsession,Integer schoolId,Integer kitchenId ,Integer schoolType ,String kitchenType,Integer countyId){
		List<SchoolDataBO> result=new ArrayList<SchoolDataBO>();
		Map<String ,Object> conditionMap=new HashMap<String ,Object>();
		
		String hql="From School s ,Kitchen k,Schoolkitchen sk where s.schoolId=sk.id.schoolId "
				+ " and sk.id.kitchenId=k.kitchenId ";
		
		if (schoolId!=0){
			hql +=" and sk.id.schoolId=:schoolId ";
			conditionMap.put("schoolId", schoolId);
		}
		
		if (kitchenId!=0){
			hql +=" and sk.id.kitchenId=:kitchenId ";
			conditionMap.put("kitchenId", kitchenId);
		}
		
		if (schoolType!=0){
			hql+= " and bitwise_andTwo(s.schoolType , :schoolType )=:schoolType ";
			conditionMap.put("schoolType", schoolType);
		}
		
		if (!"".equals(kitchenType)){
			hql += " and k.kitchenType=:kitchenType ";
			conditionMap.put("kitchenType", kitchenType);
		}
		
		if (CateringServiceCode.AUTHEN_SUPER_COUNTY_INT!=countyId){
			hql += " and s.countyId=:countyId ";
			conditionMap.put("countyId", countyId);
		}
		
		hql+=" order by s.schoolId asc ";
		
		Query query =dbsession.createQuery(hql);
		
		for(Entry<String, Object> entry : conditionMap.entrySet()){
			query.setParameter(entry.getKey(), entry.getValue());
		}
		
		List<Object[]> rs=query.list();
		Iterator<Object[]> ir =rs.iterator();
		Integer tmpSchoolid=0;
		SchoolDataBO schoolBo=null;
		while(ir.hasNext()){
			System.out.print("--");
			Object[] row=ir.next();
			School school=(School) row[0];
			Kitchen kitchen =(Kitchen) row[1];
			Schoolkitchen schoolkitchen=(Schoolkitchen) row[2];

			if (school.getSchoolId()!=tmpSchoolid){
				tmpSchoolid=school.getSchoolId();
				
				if(schoolBo !=null){
					result.add(schoolBo);					
				}
				
				schoolBo=new SchoolDataBO();
				schoolBo.setEnable(school.getEnable());
				schoolBo.setSchoolCode(school.getSchoolCode());
				schoolBo.setSchoolId(school.getSchoolId());
				schoolBo.setSchoolName(school.getSchoolName());
				schoolBo.setOffered(schoolkitchen.getOffered());
				
			}
			KitchenBO kbo=new KitchenBO();
			kbo.setCompanyId(kitchen.getCompanyId());
			kbo.setKitchenId(kitchen.getKitchenId());
			kbo.setKitchenName(kitchen.getKitchenName());
			kbo.setKitchenType(kitchen.getKitchenType());
			kbo.setSupplierQuantity(schoolkitchen.getQuantity());
			schoolBo.getKitchens().add(kbo);
		}

		result.add(schoolBo);
		
		return result;
	}

	/*
	 * 查詢學校所屬縣市的限制上傳時間
	 * 
	 * */
	
	public static String queryUploadLimitTimeBySchoolid(Session dbSession,Integer schoolId){
		String limitString="";
		SchoolDAO dao=new SchoolDAO();
		//改為傳session進去  new object查system config 避免原本static call會有的session timeout 20140818 KC 
		
		ApplicationConfig apConfig=new ApplicationConfig(dbSession);
		
		dao.setSession(dbSession);
		School school=dao.findById(schoolId);
		if (school!=null){
			limitString=apConfig.getUploadMenuLimitTime(school.getCountyId().toString());
		}
		return limitString;
		
	}
}

