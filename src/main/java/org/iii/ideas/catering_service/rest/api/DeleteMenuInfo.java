package org.iii.ideas.catering_service.rest.api;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.Batchdata;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;
import org.iii.ideas.catering_service.util.LogUtil;
import org.iii.ideas.catering_service.util.SchoolAndKitchenUtil;

public class DeleteMenuInfo extends AbstractApiInterface<DeleteMenuInfoRequest, DeleteMenuInfoResponse> {

	
	@Override
	public void process() throws NamingException {
		// select * from menu where MenuDate between menuId = [mid]
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		int kid = this.getKitchenId();
		Long mid = Long.valueOf(this.requestObj.getMid());
		String menuDetail = "";
		// Integer kid = 1;

		//判斷是否超過上傳限制時間  20140724 KC
		Criteria criteria = dbSession.createCriteria(Batchdata.class);
		criteria.add(Restrictions.eq("batchDataId", mid));
		//criteria.add(Restrictions.eq("kitchenId", kid));
		Batchdata menu =(Batchdata) criteria.uniqueResult();
		if (menu==null){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("找不到資料");
			return;
		}
		try {
			if(!CateringServiceUtil.isUploadTime(CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", menu.getMenuDate())
					,new Timestamp( (new Date()).getTime()), SchoolAndKitchenUtil.queryUploadLimitTimeBySchoolid(dbSession, menu.getSchoolId()))){
				this.responseObj.setResStatus(0);
				this.responseObj.setMsg("超過上傳限制時間，無法更新! ");
				return;
			}
		} catch (ParseException e) {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("超過上傳限制時間，無法更新! ");
			return;
		}
		//紀錄菜單寫入資訊
		menuDetail += "廚房:"+menu.getKitchenId()+",<br>";
		menuDetail += "供餐學校:"+menu.getSchoolId()+",<br>";
		menuDetail += "日期:"+menu.getMenuDate()+",<br>";
		menuDetail += "主食:"+menu.getMainFoodId()+",";
		menuDetail += "主食2:"+menu.getMainFood1id()+",";
		menuDetail += "主菜:"+menu.getMainDishId()+",";
		menuDetail += "主菜1:"+menu.getMainDish1id()+",";
		menuDetail += "主菜2:"+menu.getMainDish2id()+",";
		menuDetail += "主菜3:"+menu.getMainDish3id()+",";
		menuDetail += "副菜1:"+menu.getSubDish1id()+",";
		menuDetail += "副菜2:"+menu.getSubDish2id()+",";
		menuDetail += "副菜3:"+menu.getSubDish3id()+",";
		menuDetail += "副菜4:"+menu.getSubDish4id()+",";
		menuDetail += "副菜5:"+menu.getSubDish5id()+",";
		menuDetail += "副菜6:"+menu.getSubDish6id()+",";
		menuDetail += "蔬菜:"+menu.getVegetableId()+",";
		menuDetail += "湯品:"+menu.getSoupId()+",";
		menuDetail += "附餐一:"+menu.getDessertId()+",";
		menuDetail += "附餐二:"+menu.getDessert1id()+",";
		menuDetail += "全榖根莖:"+menu.getTypeGrains()+",";
		menuDetail += "豆魚肉蛋:"+menu.getTypeMeatBeans()+",";
		menuDetail += "蔬菜:"+menu.getTypeVegetable()+",";
		menuDetail += "油脂與堅果種子:"+menu.getTypeOil()+",";
		menuDetail += "水果:"+menu.getTypeFruit()+",";
		menuDetail += "乳品:"+menu.getTypeMilk()+",";
		menuDetail += "熱量:"+menu.getCalorie()+",";
		menuDetail += "mid:"+menu.getBatchDataId();
		
		LogUtil.writeFileUploadLog(menu.getBatchDataId().toString(), this.getUsername(), "刪除菜單："+this.responseObj.getMsg()+menuDetail, menu.getBatchDataId().toString(), "UI_Menu");
		
		Transaction tx = dbSession.beginTransaction();
		HibernateUtil.deleteBatchdataById(dbSession, mid);
		tx.commit();
		
		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");

	}

}
