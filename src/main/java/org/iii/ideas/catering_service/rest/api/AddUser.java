package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.common.Common;
import org.iii.ideas.catering_service.dao.School;
import org.iii.ideas.catering_service.dao.SchoolDAO;
import org.iii.ideas.catering_service.dao.Useraccount;
import org.iii.ideas.catering_service.dao.Userrelation;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;

public class AddUser extends AbstractApiInterface<AddUserRequest, AddUserResponse>{
	@Override
	public void process() throws NamingException, ParseException {
		if(!this.isLogin()){
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
//		String usertype = requestObj.getUsertype();
//		String a = usertype.substring(0, 3);
		if(this.requestObj.getUsername()==null){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("請填寫帳戶");
			return;
		}else{ 
			//DB中KitchenName為unique >> should be checked 
			Criteria criteriaUA = this.dbSession.createCriteria(Useraccount.class);
			criteriaUA.add(Restrictions.eq("username", this.requestObj.getUsername()));
			List<Useraccount> users = criteriaUA.list();
			if (users.size() > 0 ) {
				this.responseObj.setResStatus(0);
				this.responseObj.setMsg("此帳戶已存在:"+ this.requestObj.getUsername());
				return;
			}
		}
		if(this.requestObj.getPassword()==null){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("請填寫密碼 ");
			return;
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
		//儲存帳戶基本資料
		Transaction tx = dbSession.beginTransaction();
		
		Useraccount use = new Useraccount();
		if(!CateringServiceUtil.isEmpty(requestObj.getUsername())  ){
			use.setUsername(this.requestObj.getUsername());
		}
		if(!CateringServiceUtil.isEmpty(requestObj.getPassword())  ){
			Common common =new Common();//md5
			use.setPassword(common.getEncoderByMd5(this.requestObj.getPassword()));
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
		
		//因應新版帳戶新增，更動role modify by Ellis 20160113
		//主管機關 地方政府分開處理
		switch(requestObj.getUsertype().substring(0, 2)){
			case "11" :
				requestObj.setRoleList("admin");
				break;
			case "12" :
				switch(requestObj.getUsertype().substring(3, 6)){
					case "001":
						requestObj.setRoleList("kGov2");
						break;
					case "002":
						requestObj.setRoleList("kGov1");
						break;
				}
				break;
		}

		switch(requestObj.getUsertype()){
			case "000" :
				requestObj.setRoleList("kSys");
				break;
			case "005" :
				requestObj.setRoleList("kCom");
				break;
			case "006" :
				requestObj.setRoleList("kSch");
				//判斷若為高中則寫入美食階權限。 add by Ellis 20160115
				String code4 = requestObj.getUsername().substring(4, 5);
				if("3".equals(code4)||"4".equals(code4)){
					String SchoolCode = requestObj.getUsername().substring(1, 7);
					SchoolDAO dao = new SchoolDAO(this.dbSession);
					School school = dao.querySchoolBySchoolCode(SchoolCode);
					if(CateringServiceUtil.isNull(school)){
						this.responseObj.setResStatus(0);
						this.responseObj.setMsg("學校代碼："+SchoolCode+"不存在，請至學校管理頁面新增學校及帳號。");
						return;
					}
					Userrelation ul = new Userrelation();
					ul.setParent("0");
					ul.setParentType("0");
					ul.setChild(requestObj.getUsername());
					ul.setAuthorizeType("1");
					ul.setAuthorizeUserType("101");
					ul.setAuthorizeTarget(String.valueOf(school.getSchoolId()));
					ul.setAuthorizeTargetType("101");
					ul.setCanCreateAccount("7");
					ul.setCanDeleteAccount("7");
					ul.setCanEditAccount("7");
					ul.setCanCreateRestaurant("7");
					ul.setCanDeleteRestaurant("7");
					ul.setCanEditRestaurant("7");
					ul.setCanUploadMenu("1");
					ul.setStatus("1");
					ul.setCreateUser(this.getUsername());
					ul.setCreateDate(new Date());
					dbSession.saveOrUpdate(ul);
				}
				break;
			case "007" :
				requestObj.setRoleList("kSch");
				break;
			case "009" :
				requestObj.setRoleList("kSHOP");
				break;
			case "101" :
				requestObj.setRoleList("101");
				break;
			case "102" :
				requestObj.setRoleList("102");
				break;
			case "103" :
				requestObj.setRoleList("103");
				break;
				
		}
		if(!CateringServiceUtil.isEmpty(requestObj.getRoleList())  ){
			HibernateUtil.saveUsernameRoleRelation(dbSession, this.requestObj.getUsername(), this.requestObj.getRoleList());
		}
		if(!CateringServiceUtil.isEmpty(requestObj.getKitchenId())  ){
			use.setKitchenId(Integer.valueOf(this.requestObj.getKitchenId()));
		}
		
		//21040513增加欄位enable
		use.setEnable(1);
		
		dbSession.save(use);
		tx.commit();
		
		this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);
	}

}
