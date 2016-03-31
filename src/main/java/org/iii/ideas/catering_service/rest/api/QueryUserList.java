package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.iii.ideas.catering_service.dao.UseraccountDAO;
import org.iii.ideas.catering_service.util.AuthenUtil;
import org.iii.ideas.catering_service.util.CateringServiceUtil;

public class QueryUserList extends
		AbstractApiInterface<QueryUserListRequest, QueryUserListResponse> {

	@Override
	public void process() throws NamingException, ParseException {
		
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		
		//帶入使用者名稱，轉換為縣市代碼
		String countyID = "";
		countyID = AuthenUtil.getCountyTypeByUsername(this.getUsername());
		
		UseraccountDAO user =  new UseraccountDAO();
		user.openSessionFactory();
		
		List<Object[]> Useraccount = null;
		String userName = this.requestObj.getName();
		String type = this.requestObj.getType();
		if(!CateringServiceUtil.isNull(type) && !CateringServiceUtil.isEmpty(type)){
			type = type.replaceAll("#", "%");
		}
		//直接傳入由function判斷有無值 20160112 modify by Ellis
//		if (userName != null && userName.length() > 0 ) {
		Useraccount = user.getUseraccountAndKitchenNameByKitchenId2(countyID, userName,type);
//		} else {
//			Useraccount = user.getUseraccountAndKitchenNameByKitchenId2(countyID);
//		}
		Iterator<Object[]> iteratorUA = Useraccount.iterator();
		Object[] obj=null; 
		UserList userObj = null;
		while (iteratorUA.hasNext()) {
			obj = iteratorUA.next();	
			userObj = new UserList();
			userObj.setUsername((String)obj[0]);
			userObj.setUsertype((String)obj[1]);
			userObj.setName((String)obj[2]);
			userObj.setEmail((String)obj[3]);
			userObj.setKitchenName((String)obj[4]);
			userObj.getRoleList().add((String)obj[5]);
			userObj.setUsertypename((String)obj[7]);
			userObj.setEnable((Integer)obj[8]);
			
			/* 下列寫法會放進List 但比較慢
			 * UseraccountDAO userRole =  new UseraccountDAO();
			//userRole.openSessionFactory();//啟用session Q:可否共用(如上方)
			List<Object[]> Userroles = user.getRoletypeByUsername((String)obj[0]);
			//回傳的東西要用List去接，在這裡是不知道回傳的格式為何(目前已知為string,string)
			Iterator<Object[]> iteratorUR = Userroles.iterator();
			//放入第二個迭代器
			while (iteratorUR.hasNext()) {
				Object[] obj2=iteratorUR.next(); 
				String role_type = (String)obj2[1];
				String role_type2 = (String)obj2[0];
				userObj.getRoleList().add(role_type);	
			}*/
			this.responseObj.getUserList().add(userObj);
			
		}	
		user.closeSession();
		/*//原本寫法 先取得useraccount 再用kid 去找kName 
		//拿出Useraccount的Class，取出變數讀取資料庫
		Criteria criteriaUA = this.dbSession.createCriteria(Useraccount.class);
		//輸出結果放在list
		List<Useraccount> users = criteriaUA.list();
		//list放進迭代
		Iterator<Useraccount> iteratorUA = users.iterator();
		log.debug("QueryUserList Size:" + users.size());
		System.out.println("QueryUserList Size:" + users.size());
		Useraccount ua = null; //再while外面先宣告變數，避免在while中重複宣告
		UserList userObj = null;
		//迭代器逐一處理輸出的list
		while (iteratorUA.hasNext()) {
			ua = iteratorUA.next();
			userObj = new UserList();
			userObj.setUsername(ua.getUsername());
			userObj.setUsertype(ua.getUsertype());
			userObj.setName(ua.getName());
			userObj.setEmail(ua.getEmail());
			//使用HibernateUtil中已經寫好的queryKitchenById 功能，丟入KitchenId去查詢所有的Kitchen資料
			Kitchen kitchen_Name = HibernateUtil.queryKitchenById(this.dbSession, ua.getKitchenId());
			//只取出KitchenName塞入
			userObj.setKitchenName(kitchen_Name.getKitchenName().toString());
			
			//要開始處理從Userrole關聯資料表中由Username去找出Role中對應的rolerule，功能寫在UseraccountDAO中
			UseraccountDAO user =  new UseraccountDAO();
			user.openSessionFactory();//啟用session Q:可否共用(如上方)
			List<Object[]> Userroles = user.getRoleruleByUsername(ua.getUsername());
			//回傳的東西要用List去接，在這裡是不知道回傳的格式為何(目前已知為string,string)
			Iterator<Object[]> iteratorUR = Userroles.iterator();
			//放入第二個迭代器
			while (iteratorUR.hasNext()) {
				Object[] obj=iteratorUR.next(); 
				String role_type = (String)obj[1];
				String role_type2 = (String)obj[0];
				userObj.getRoleList().add(role_type);	
			}
			user.closeSession();
			this.responseObj.getUserList().add(userObj);
		}
		*/
		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");
	}

}
