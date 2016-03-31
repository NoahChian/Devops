package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.School;

public class AddSchool extends AbstractApiInterface<AddSchoolRequest, AddSchoolResponse>{
	@Override
	public void process() throws NamingException, ParseException {

		if(!this.isLogin()){
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		if(this.requestObj.getSchoolId()==null){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("請填寫學校編號");
			return;
		}else{ 
			//DB中KitchenName為unique >> should be checked 
			Criteria criteriaSC = this.dbSession.createCriteria(School.class);
			criteriaSC.add(Restrictions.eq("schoolId", this.requestObj.getSchoolId()));
			List<School> schId = criteriaSC.list();
			if (schId.size() > 0 ) {
				this.responseObj.setResStatus(0);
				this.responseObj.setMsg("此學校編號已存在:"+ this.requestObj.getSchoolId());
				return;
			}
		}
		if(this.requestObj.getSchoolName()==null){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("請填寫學校名稱");
			return;
		}else{ 
			//DB中KitchenName為unique >> should be checked 
			Criteria criteriaSCN = this.dbSession.createCriteria(School.class);
			criteriaSCN.add(Restrictions.eq("schoolName", this.requestObj.getSchoolName()));
			List<School> schName = criteriaSCN.list();
			if (schName.size() > 0 ) {
				this.responseObj.setResStatus(0);
				this.responseObj.setMsg("此學校編號已存在:"+ this.requestObj.getSchoolName());
				return;
			}
		}
		if(this.requestObj.getCountyId()==null){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("請選擇縣市 ");
			return;
		}
		if(this.requestObj.getAreaId()==null){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("請選擇區域");
			return;
		}
		
		//儲存帳戶基本資料
		Transaction tx = dbSession.beginTransaction();
		
		School sch = new School();
		
//		sch.setSchoolId(Integer.valueOf(this.requestObj.getSchoolId()));
	
		sch.setSchoolName(this.requestObj.getSchoolName());
	
		sch.setCountyId(Integer.valueOf(this.requestObj.getCountyId()));
	
		sch.setAreaId(Integer.valueOf(this.requestObj.getAreaId()));
	
		sch.setEnable(Integer.valueOf(this.requestObj.getEnable()));
	
		
		dbSession.save(sch);
		tx.commit();
		
		this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);
	}

}
