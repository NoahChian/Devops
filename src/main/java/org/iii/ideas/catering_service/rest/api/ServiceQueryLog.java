package org.iii.ideas.catering_service.rest.api;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Query;
import org.hibernate.Transaction;
import org.iii.ideas.catering_service.dao.School;
import org.iii.ideas.catering_service.dao.Useraccount;
import org.iii.ideas.catering_service.dao.WsLog;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.LogUtil;

public class ServiceQueryLog extends AbstractApiInterface<ServiceQueryLogRequest, ServiceQueryLogResponse>{

		
		@Override
		public void process() throws NamingException, ParseException {

			String account = this.requestObj.getAccount();
			String action = this.requestObj.getAction();
			HashMap<String,String> content = this.requestObj.getContent();
			Transaction tx = this.dbSession.beginTransaction();
			
			this.responseObj.setResStatus(1);
			this.responseObj.setMsg("");
			
			//查log
			if ("query".equals(action)){
				getLogs(account);
				getUserInfo(account);
				
			}
			if ("query_ui".equals(action)){
				getLogs(account);
				getUserInfo(account);
				
			}
			//紀錄客服
			if ("logging".equals(action)){
				saveLogging(account,content);
				
			}
			tx.commit();
			
		}
		
		
		private void getLogs(String account){			

			
			String hql="select date_format(w.sendTime,'%m/%d %H:%i:%s'), c.name,w.statusCode ,w.description "
					+ " from WsLog w, Useraccount a ,Code c"
					+ " where w.companyId=a.username and c.code=w.action and w.companyId=:acc and messageId='U0001' and c.type='FILE_ACTIOM' ";
			if ("query".equals(this.requestObj.getAction())){
				hql += " and c.sort = 0";
			}
			if ("query_ui".equals(this.requestObj.getAction())){
				hql += " and c.sort = 1";
			}
			hql += " order by w.id desc  ";
			Query queryObj=dbSession.createQuery(hql);
			queryObj.setParameter("acc", account);
			queryObj.setMaxResults(500);
			List result=queryObj.list();
			
			this.responseObj.setLogss(result);
		}
		private void getUserInfo(String account){	

			try{
				//查帳號資料
				String hql="from Useraccount u where u.username=:uname";
				Query queryObj=this.dbSession.createQuery(hql);
				queryObj.setParameter("uname", account);
				queryObj.setMaxResults(1);
				Useraccount accountObj=(Useraccount) queryObj.uniqueResult();
				if (accountObj!=null){
					this.responseObj.getAccountInfo().put("uname", accountObj.getName());
					this.responseObj.getAccountInfo().put("kid", accountObj.getKitchenId().toString());
				}else{
					return;
				}
				//查供餐學校
				hql="from Schoolkitchen sk ,School s where sk.id.kitchenId=:kid and sk.id.schoolId=s.schoolId ";
				Query queryObjKitchen=this.dbSession.createQuery(hql);
				queryObjKitchen.setParameter("kid", accountObj.getKitchenId());
				String schoolList="";
				List schools=queryObjKitchen.list();
				Iterator<Object[]> ir=schools.iterator();
				while(ir.hasNext()){
					Object[] row=ir.next();
					School school=(School) row[1];
					schoolList=schoolList+school.getSchoolName()+"("+school.getSchoolId().toString()+"),";
				}
				this.responseObj.getAccountInfo().put("schools", schoolList);
				
			}catch (Exception ex){
				
			}
			
		}
		
		
		private void saveLogging(String account,HashMap<String,String> content){
			//新增表格servicelog 儲存客服問題 20140520 KC
			try{
				 Timestamp date=CateringServiceUtil.getCurrentTimestamp();
				 String qAccount=account.toUpperCase();
				 String qUsername="";
				 String qType="";
				 String serviceAccount=this.getUsername();
				 String qContent="";
				
				if (!CateringServiceUtil.isEmpty(content.get("qa_type"))){
					qType=content.get("qa_type").toUpperCase();
				}
				if (!CateringServiceUtil.isEmpty(content.get("qa_description"))){
					qContent=content.get("qa_description");
				}
				/*if (!CateringServiceUtil.isEmpty(content.get("qa_username"))){
					qUsername=content.get("qa_username");
				}*/
				System.out.println("**"+qUsername);
				LogUtil.writeServiceLog(qAccount, qUsername, qType, serviceAccount, qContent);
								
			}catch (Exception ex){
				this.responseObj.setResStatus(-1);
				this.responseObj.setMsg("客服記錄儲存失敗"+ex.getMessage());
			}
		}
}
