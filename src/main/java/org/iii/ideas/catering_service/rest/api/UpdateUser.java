package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.common.Common;
import org.iii.ideas.catering_service.dao.Useraccount;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;

public class UpdateUser extends AbstractApiInterface<UpdateUserRequest, UpdateUserResponse>{

	@Override
	public void process() throws NamingException, ParseException {
		int password_check = 0;
		if(!this.isLogin()){
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		if(this.requestObj.getUsername()==null){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("帳戶讀取有誤，請重新選擇帳戶");
			return;
		}
		if(this.requestObj.getOri_password()==null  || "".equals(this.requestObj.getOri_password())){
			
		}else if(this.requestObj.getPassword()!=null){ 
			//DB中KitchenName為unique >> should be checked 
			Criteria criteriaUA = this.dbSession.createCriteria(Useraccount.class);
			criteriaUA.add(Restrictions.eq("username", this.requestObj.getUsername()));
			Common common =new Common();
			criteriaUA.add(Restrictions.eq("password", common.getEncoderByMd5(this.requestObj.getOri_password())));
			List<Useraccount> users = criteriaUA.list();
			if (users.size() > 0 ) {
				password_check = 1;
			}else{
				this.responseObj.setResStatus(0);
				this.responseObj.setMsg("舊密碼錯誤");
				return;
			}
		}
		if(this.requestObj.getUsertype()==null){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("請選擇帳戶身分");
			return;
		}
		if(this.requestObj.getName()==null){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("請填寫帳戶名稱");
			return;
		}
		if(this.requestObj.getEmail()==null){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("請填寫Email");
			return;
		}
		//修改帳戶基本資料
		String uName = this.requestObj.getUsername();
		Transaction tx = dbSession.beginTransaction();
		Criteria criteria = dbSession.createCriteria(Useraccount.class);
		criteria.add(Restrictions.eq("username", uName));

		List users = criteria.list();
		Iterator<Useraccount> iterator = users.iterator();
		this.responseObj.setResStatus(0);
		this.responseObj.setMsg("找不到帳戶資料");

		while (iterator.hasNext()) {
			Useraccount use = iterator.next();
			/*if(!CateringServiceUtil.isEmpty(requestObj.getUsername())  ){
				use.setUsername(this.requestObj.getUsername());
			}*/
			if(this.requestObj.getPassword()==null || "".equals(this.requestObj.getPassword())){ //密碼空白就不修改
			}else{
				Common common =new Common();//md5
				if(password_check==1){
				use.setPassword(common.getEncoderByMd5(this.requestObj.getPassword()));
				}else {
					this.responseObj.setResStatus(0);
					this.responseObj.setMsg("舊密碼驗證錯誤");
					return;
				}
			}
			if(!CateringServiceUtil.isEmpty(requestObj.getUsertype())  ){
				use.setUsertype(this.requestObj.getUsertype());
			}
			if(!CateringServiceUtil.isEmpty(requestObj.getName())  ){
				use.setName(this.requestObj.getName());
			}
			if(!CateringServiceUtil.isEmpty(requestObj.getEmail())  ){
				use.setEmail(this.requestObj.getEmail());
			}
			if(!CateringServiceUtil.isEmpty(requestObj.getRoleList())  ){
				HibernateUtil.updateUsernameRoleRelation(dbSession, this.requestObj.getUsername(), this.requestObj.getRoleList());
			}
			if(!CateringServiceUtil.isEmpty(requestObj.getKitchenId())  ){
				use.setKitchenId(Integer.valueOf(this.requestObj.getKitchenId()));
			}
			
			//21040513增加欄位enable
			use.setEnable(1);
			
			dbSession.update(use);
			
			
		}
		tx.commit();
		
		this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);
	}

}
