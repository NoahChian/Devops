package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.Kitchen;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;

public class UpdateKitchen extends AbstractApiInterface<UpdateKitchenRequest, UpdateKitchenResponse>{

	@Override
	public void process() throws NamingException, ParseException {

		if(!this.isLogin()){
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		/*if(this.requestObj.getKitchenName()==null){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("查不到此廚房");
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
		}*/
		
		if(this.requestObj.getKitchenType()==null){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("請選擇廚房型態");
			return;
		}
		//修改廚房基本資料

		Integer kid = Integer.valueOf(this.requestObj.getKitchenId());
		Transaction tx = dbSession.beginTransaction();
		Criteria criteria = dbSession.createCriteria(Kitchen.class);
		criteria.add(Restrictions.eq("kitchenId", kid));
		
		List kitchens = criteria.list();
		Iterator<Kitchen> iterator = kitchens.iterator();
		this.responseObj.setResStatus(0);
		this.responseObj.setMsg("找不到資料");

		while (iterator.hasNext()) {
			Kitchen kit = iterator.next();
			/*if(!CateringServiceUtil.isNull(requestObj.getKitchenName())  ){
				kit.setKitchenName(this.requestObj.getKitchenName());
			}*/
			if(!CateringServiceUtil.isNull(requestObj.getTel())  ){
				kit.setTel(this.requestObj.getTel());
			}
			if(!CateringServiceUtil.isNull(requestObj.getCompanyId())  ){
				kit.setCompanyId(this.requestObj.getCompanyId());
			}
			if(!CateringServiceUtil.isNull(requestObj.getKitchenType())  ){
				kit.setKitchenType(this.requestObj.getKitchenType());
			}
			if(!CateringServiceUtil.isNull(requestObj.getAddress())  ){
				kit.setAddress(this.requestObj.getAddress());
			}
			if(!CateringServiceUtil.isNull(requestObj.getOwnner())  ){
				kit.setOwnner(this.requestObj.getOwnner());
			}
			if(!CateringServiceUtil.isNull(requestObj.getFax())  ){
				kit.setFax(this.requestObj.getFax());
			}
			if(!CateringServiceUtil.isNull(requestObj.getNutritionist())  ){
				kit.setNutritionist(this.requestObj.getNutritionist());
			}
			if(!CateringServiceUtil.isNull(requestObj.getChef())  ){
				kit.setChef(this.requestObj.getChef());
			}
			if(!CateringServiceUtil.isNull(requestObj.getQualifier())  ){
				kit.setQualifier(this.requestObj.getQualifier());
			}
			if(!CateringServiceUtil.isNull(requestObj.getHaccp())  ){
				kit.setHaccp(this.requestObj.getHaccp());
			}
			if(!CateringServiceUtil.isNull(requestObj.getInsurement())  ){
				kit.setInsurement(this.requestObj.getInsurement());
			}
			if(!CateringServiceUtil.isNull(requestObj.getManager())  ){
				kit.setManager(this.requestObj.getManager());
			}
			if(!CateringServiceUtil.isNull(requestObj.getInsurement())  ){
				kit.setManageremail(this.requestObj.getManageremail());
			}
			if(!CateringServiceUtil.isNull(requestObj.getEmail())  ){
				kit.setEmail(this.requestObj.getEmail());
				//20140729 Ric Email連同Userccount同步更新
//				HibernateUtil.updateUseraccountEmailByKitchenId(this.dbSession,this.requestObj.getEmail(),kid);
				HibernateUtil.updateUseraccountEmailByKitchenIdV2(this.dbSession,this.requestObj.getEmail(),kid,this.getUsername());
			}
			kit.setPicturePath("-");
			//21040513增加欄位enable
			kit.setEnable(1);
			//kit.setCreateDate(CateringServiceUtil.getCurrentTimestamp());

			dbSession.update(kit);
			
			
		}
		tx.commit();
		
		this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);
	}

}
