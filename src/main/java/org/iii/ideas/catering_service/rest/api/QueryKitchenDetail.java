package org.iii.ideas.catering_service.rest.api;

import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.Kitchen;
import org.iii.ideas.catering_service.dao.School;
import org.iii.ideas.catering_service.rest.bo.SchoolDataBO;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;
import org.iii.ideas.catering_service.util.SchoolAndKitchenUtil;

public final class QueryKitchenDetail extends AbstractApiInterface<QueryKitchenDetailRequest, QueryKitchenDetailResponse> {

	@Override
	public void process() throws NamingException {
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		Integer kitchenId = Integer.valueOf( this.requestObj.getKitchenId());
		Criteria criteria = dbSession.createCriteria(Kitchen.class);
		criteria.add( Restrictions.eq("kitchenId", kitchenId) );
		int resStatus=0;
		Kitchen kdetail =(Kitchen) criteria.uniqueResult();
		if (kdetail==null){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("找不到資料");
			return;
		}
		
		this.responseObj.setKitchenId(kdetail.getKitchenId());
		this.responseObj.setKitchenName(kdetail.getKitchenName());
		this.responseObj.setKitchenType(kdetail.getKitchenType());
		this.responseObj.setAddress(kdetail.getAddress());
		this.responseObj.setTel(kdetail.getTel());
		this.responseObj.setOwnner(kdetail.getOwnner());
		this.responseObj.setFax(kdetail.getFax());
		this.responseObj.setNutritionist(kdetail.getNutritionist());
		this.responseObj.setChef(kdetail.getChef());
		this.responseObj.setQualifier(kdetail.getQualifier());
		this.responseObj.setHaccp(kdetail.getHaccp());
		this.responseObj.setCompanyId(kdetail.getCompanyId());
		this.responseObj.setEmail(kdetail.getEmail());
		this.responseObj.setInsurement(kdetail.getInsurement());
		this.responseObj.setManager(kdetail.getManager());
		this.responseObj.setManageremail(kdetail.getManageremail());
		
		//因應iframe頁面，前端頁面回傳schoollink add by ellis 20141229
		Query companyIdQuery = dbSession.createQuery("select companyId from Kitchen where kitchenId=:kid ");
		companyIdQuery.setParameter("kid", kitchenId);
		String companyId = companyIdQuery.uniqueResult().toString();
		companyId = companyId.substring(1); // remove first char of column named companyId of table kitchen 
		
		Query schoolIdQuery = dbSession.createQuery("select schoolCode from School where schoolCode=:sCode");
		schoolIdQuery.setParameter("sCode", companyId);
		this.responseObj.setSchoolCode(String.format("%s", schoolIdQuery.uniqueResult()));
		
		
		
		//用school util取資料 增加回傳供餐份量 20140718 KC
		List<SchoolDataBO> schooldataList=SchoolAndKitchenUtil.querySchoolDataListByConditions(dbSession, 0, kitchenId, 0, "", CateringServiceCode.AUTHEN_SUPER_COUNTY_INT);
		Iterator<SchoolDataBO> ir=schooldataList.iterator();
		while(ir.hasNext()){
			SchoolDataBO bo=ir.next();
			SchoolObject so=new SchoolObject();
			if(!CateringServiceUtil.isNull(bo))
			//Ric 處理Null問題
			{
			so.setSchoolName(bo.getSchoolName());
			so.setSid(bo.getSchoolId());
			so.setQuantity(bo.getKitchens().get(0).getSupplierQuantity());
			so.setSchoolCode(bo.getSchoolCode());
			if(bo.getOffered()==1){
				so.setOffered(true);
			}
			if(bo.getOffered()==0){
				so.setOffered(false);
			}
			this.responseObj.getSchool().add(so);
			}
		}
				this.responseObj.setResStatus(1);
				this.responseObj.setMsg("");
	}
}
