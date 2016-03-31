package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.Kitchen;
import org.iii.ideas.catering_service.util.CateringServiceUtil;

public class AddKitchen extends AbstractApiInterface<AddKitchenRequest, AddKitchenResponse>{
	
	@Override
	public void process() throws NamingException, ParseException {

		if(!this.isLogin()){
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		if(this.requestObj.getKitchenName()==null){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("請填寫名稱 ");
			return;
		}else{ 
			//DB中KitchenName為unique >> should be checked 
			Criteria criteriaKN = this.dbSession.createCriteria(Kitchen.class);
			criteriaKN.add(Restrictions.eq("kitchenName", this.requestObj.getKitchenName()));
			List<Kitchen> kitchens = criteriaKN.list();
			if (kitchens.size() > 0 ) {
				this.responseObj.setResStatus(0);
				this.responseObj.setMsg("此廚房名稱已存在:"+ this.requestObj.getKitchenName());
				return;
			}
		}
		
		if(this.requestObj.getKitchenType()==null){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("請選擇廚房型態");
			return;
		}
		//儲存廚房基本資料
		Transaction tx = dbSession.beginTransaction();
		
		Kitchen kit = new Kitchen();
		if(!CateringServiceUtil.isEmpty(requestObj.getKitchenName())  ){
			kit.setKitchenName(this.requestObj.getKitchenName());
		}
			kit.setTel(this.requestObj.getTel());
		if(!CateringServiceUtil.isEmpty(requestObj.getCompanyId())  ){
			kit.setCompanyId(this.requestObj.getCompanyId());
		}
			kit.setEmail(this.requestObj.getEmail());
		if(!CateringServiceUtil.isEmpty(requestObj.getKitchenType())  ){
			kit.setKitchenType(this.requestObj.getKitchenType());
		}
			kit.setAddress(this.requestObj.getAddress());
			kit.setOwnner(this.requestObj.getOwnner());
			kit.setFax(this.requestObj.getFax());
			kit.setNutritionist(this.requestObj.getNutritionist());
			kit.setChef(this.requestObj.getChef());
			kit.setQualifier(this.requestObj.getQualifier());
			kit.setHaccp(this.requestObj.getHaccp());
			kit.setManager(this.requestObj.getManager());
			kit.setManageremail(this.requestObj.getManageremail());
		/*if(!CateringServiceUtil.isEmpty(requestObj.getInsurement())  ){
			kit.setInsurement(this.requestObj.getInsurement());
		}*/
		kit.setInsurement("");
		kit.setPicturePath("");
		kit.setCreateDate(CateringServiceUtil.getCurrentTimestamp());
		//21040513增加欄位enable
		kit.setEnable(1);
		dbSession.save(kit);
		tx.commit();
		
		//儲存廚房維護的學校 SchoolKitchen
		//取出剛才新增的KitchenId
		//array進來
		//問 是不是要新增一個新的session
		/*
		if(this.requestObj.getSchool().size()>0){
			
			String HQL = "";
			
			Kitchen kitchen_Id = HibernateUtil.queryKitchenByName(this.dbSession, this.requestObj.getKitchenName());
			//kitchen_Id.getKitchenId().toString());
			Schoolkitchen scq = new Schoolkitchen();
			List schools = this.requestObj.getSchool();
			Iterator<SchoolObject> iteratorSC = schools.iterator();
			while (iteratorSC.hasNext()) {
				SchoolObject sc = iteratorSC.next();
				//kitchen_Id.getKitchenId();
				//sc.getSid();
				//this.responseObj.getSchool().add(so);
				HQL = "SET SchoolId =:schoolId roletype FROM Role";
				
			}
			Query criteriaRT = dbSession.createQuery(HQL);
			sct.commit();
			
		}
		*/
		this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);
	}

	

}
