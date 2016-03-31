package org.iii.ideas.catering_service.rest.api;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.Batchdata;
import org.iii.ideas.catering_service.dao.Dish;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;
import org.iii.ideas.catering_service.util.LogUtil;
import org.iii.ideas.catering_service.util.SchoolAndKitchenUtil;

//修改菜單詳細資訊
public class UpdateMenuDetailInfo
		extends
		AbstractApiInterface<UpdateMenuDetailInfoRequest, UpdateMenuDetailInfoResponse> {
	private String emptyMsg = "";

	@Override
	public void process() throws NamingException {
		// select * from menu where MenuDate between menuId = [mid]
		Long mid = Long.valueOf(this.requestObj.getMid());
		if (this.getUsername() == null) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		
		int kid = this.getKitchenId();
		String menuDetail = "";
		
		Transaction tx = dbSession.beginTransaction();
		// tx.begin();

		Criteria criteria = dbSession.createCriteria(Batchdata.class);
		criteria.add(Restrictions.eq("batchDataId", mid));
		criteria.add(Restrictions.eq("kitchenId", kid));

		//改為uniqueResult取菜單  且加入try catch 抓return exception  20140722 KC	
		Batchdata menu =(Batchdata) criteria.uniqueResult();
		if (menu==null){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("找不到資料");
			return;
		}
					
		//判斷是否超過上傳限制時間  20140724 KC
		try {
			if(!CateringServiceUtil.isUploadTime(CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", menu.getMenuDate())
					,new Timestamp( (new Date()).getTime()), SchoolAndKitchenUtil.queryUploadLimitTimeBySchoolid(dbSession, menu.getSchoolId()))){
				this.responseObj.setResStatus(0);
				this.responseObj.setMsg("超過上傳限制時間，無法更新! ");
//				LogUtil.writeFileUploadLog(mid.toString(), this.getUsername(), this.responseObj.getMsg(), mid.toString(), "UI_Menu");
				return;
			}
		} catch (ParseException e) {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("超過上傳限制時間，無法更新! ");
//			LogUtil.writeFileUploadLog(mid.toString(), this.getUsername(), this.responseObj.getMsg(), mid.toString(), "UI_Menu");
			return;
		}
		
		//20150915 Ellis紀錄Log
		
//		LogUtil.writeFileUploadLog(filename, uName, retInsDish.getRetMsg(), filename, funcname);
		
		//20150216 shine add取出是否同步所有學校參數及菜單日期
		String applyToShool = this.requestObj.getApplyToSchool();
		String menuDate = menu.getMenuDate();
		String errorMessage = "";
		ArrayList<Batchdata> batchList = new ArrayList<Batchdata>();
		ArrayList<Integer> schoolList = new ArrayList<Integer>();
		//20150216 shine mod start 改成可對一廚房
		//若需要同步所有學校,則取出根據KitchenId及MenuDate取出其他的BatchDataId
		if(applyToShool != null && !applyToShool.equals("")){
			if(applyToShool.equalsIgnoreCase("all")){
				criteria = dbSession.createCriteria(Batchdata.class);
				criteria.add(Restrictions.eq("kitchenId", kid));
				criteria.add(Restrictions.eq("menuDate", menuDate));				
			}else{
				criteria = dbSession.createCriteria(Batchdata.class);
				criteria.add(Restrictions.eq("kitchenId", kid));
				String[] tmpSchoolList = applyToShool.split(",");
				
				for(int i=0;i<tmpSchoolList.length;i++){
					schoolList.add(Integer.parseInt(tmpSchoolList[i]));
				}
				criteria.add(Restrictions.in("schoolId", schoolList));
				criteria.add(Restrictions.eq("menuDate", menuDate));
			}
			batchList = (ArrayList<Batchdata>) criteria.list();
		}else{
			batchList.add(menu);
		}
		
		this.emptyMsg = "";
		Long mainFoodId = checkDishId(dbSession, kid,this.requestObj.getMain());
		Long mainFood1id = checkDishId(dbSession, kid,this.requestObj.getMain2());
		Long mainDishId = checkDishId(dbSession, kid,this.requestObj.getMajor());
		Long mainDish1id = checkDishId(dbSession, kid,this.requestObj.getMajor1());
		Long mainDish2id = checkDishId(dbSession, kid,this.requestObj.getMajor2());
		Long mainDish3id = checkDishId(dbSession, kid,this.requestObj.getMajor3());
		Long subDish1id = checkDishId(dbSession, kid,this.requestObj.getSide1());
		Long subDish2id = checkDishId(dbSession, kid,this.requestObj.getSide2());
		Long subDish3id = checkDishId(dbSession, kid,this.requestObj.getSide3());
		Long subDish4id = checkDishId(dbSession, kid,this.requestObj.getSide4());
		Long subDish5id = checkDishId(dbSession, kid,this.requestObj.getSide5());
		Long subDish6id = checkDishId(dbSession, kid,this.requestObj.getSide6());
		Long soupId = checkDishId(dbSession, kid,this.requestObj.getSoup());
		Long vegetableId = checkDishId(dbSession, kid,this.requestObj.getVegetableName());
		Long dessertId = checkDishId(dbSession, kid,this.requestObj.getMeals());
		Long dessert1id = checkDishId(dbSession, kid,this.requestObj.getMeals2());
		
		if (!this.emptyMsg.equals("")) {
			errorMessage += this.emptyMsg + "\n";
			this.responseObj.setMsg(this.emptyMsg);
			return;
		}
		
		for(int i=0;i<batchList.size();i++){
			menu = batchList.get(i);
							
			menu.setMainFoodId(mainFoodId);
			menu.setMainFood1id(mainFood1id);
			menu.setMainDishId(mainDishId);
			menu.setMainDish1id(mainDish1id);
			menu.setMainDish2id(mainDish2id);
			menu.setMainDish3id(mainDish3id);
			menu.setSubDish1id(subDish1id);
			menu.setSubDish2id(subDish2id);
			menu.setSubDish3id(subDish3id);
			menu.setSubDish4id(subDish4id);
			menu.setSubDish5id(subDish5id);
			menu.setSubDish6id(subDish6id);
			menu.setDessertId(dessertId);
			menu.setDessert1id(dessert1id);
			menu.setSoupId(soupId);
			menu.setVegetableId(vegetableId);
			menu.setCalorie(this.requestObj.getCalorie());
			menu.setTypeFruit(this.requestObj.getFruit());
			menu.setTypeGrains(this.requestObj.getGrains());
			menu.setTypeMeatBeans(this.requestObj.getMeatBeans());
			menu.setTypeMilk(this.requestObj.getMilk());
			menu.setTypeOil(this.requestObj.getOil());
			menu.setTypeVegetable(this.requestObj.getVegetable());
			menu.setTypeGrains(this.requestObj.getGrains());
			//dbSession.update(menu);
			try{
				HibernateUtil.updateBatchdata(dbSession, menu);
//				tx.commit();
//				this.responseObj.setResStatus(1);
//				this.responseObj.setMsg("");
				
			}catch (Exception ex){
//				tx.rollback();
//				this.responseObj.setResStatus(-1);
//				this.responseObj.setMsg(ex.getMessage());
				errorMessage += ex.getMessage() + "\n";
			}
		}
		//紀錄菜單寫入資訊
		menuDetail += "廚房:"+kid+",<br>";
		menuDetail += "供餐學校:"+schoolList+",<br>";
		menuDetail += "日期:"+this.requestObj.getMenuDate()+",<br>";
		menuDetail += "主食:"+this.requestObj.getMain()+",";
		menuDetail += "主食2:"+this.requestObj.getMain2()+",";
		menuDetail += "主菜:"+this.requestObj.getMajor()+",";
		menuDetail += "主菜1:"+this.requestObj.getMajor1()+",";
		menuDetail += "主菜2:"+this.requestObj.getMajor2()+",";
		menuDetail += "主菜3:"+this.requestObj.getMajor3()+",";
		menuDetail += "副菜1:"+this.requestObj.getSide1()+",";
		menuDetail += "副菜2:"+this.requestObj.getSide2()+",";
		menuDetail += "副菜3:"+this.requestObj.getSide3()+",";
		menuDetail += "副菜4:"+this.requestObj.getSide4()+",";
		menuDetail += "副菜5:"+this.requestObj.getSide5()+",";
		menuDetail += "副菜6:"+this.requestObj.getSide6()+",";
		menuDetail += "蔬菜:"+this.requestObj.getSoup()+",";
		menuDetail += "湯品:"+this.requestObj.getVegetableName()+",";
		menuDetail += "附餐一:"+this.requestObj.getMeals()+",";
		menuDetail += "附餐二:"+this.requestObj.getMeals2()+",";
		menuDetail += "全榖根莖:"+this.requestObj.getGrains()+",";
		menuDetail += "豆魚肉蛋:"+this.requestObj.getMeatBeans()+",";
		menuDetail += "蔬菜:"+this.requestObj.getVegetable()+",";
		menuDetail += "油脂與堅果種子:"+this.requestObj.getOil()+",";
		menuDetail += "水果:"+this.requestObj.getFruit()+",";
		menuDetail += "乳品:"+this.requestObj.getMilk()+",";
		menuDetail += "熱量:"+this.requestObj.getCalorie();
		
		if(errorMessage.equals("")){
			tx.commit();
			this.responseObj.setResStatus(1);
			this.responseObj.setMsg("");
		}else{
			tx.rollback();
			this.responseObj.setResStatus(-1);
			this.responseObj.setMsg(errorMessage);
		}
		LogUtil.writeFileUploadLog(menu.getBatchDataId().toString(), this.getUsername(), "更新菜單:"+this.responseObj.getMsg()+menuDetail, menu.getBatchDataId().toString(), "UI_Menu");
		
	}
	
	public void saveIngredient(Session session, Long batchdataId, Long dishId) {
		if(HibernateUtil.queryIngredientbatchdataByBatchdataIdAndDish(session, batchdataId, dishId)==null){
			HibernateUtil.saveIngredientbatchdataFromIngredient(session,  batchdataId,  dishId);
		}
	//	return void;
	}

	public Long checkDishId(Session session, Integer kid, String dishName) {
		Long defaultDishiId=(long) 0;
		if (dishName == null) {
			return defaultDishiId;
		} else if (dishName.trim().equals("")) {
			return defaultDishiId;
		}
		Long dishId = HibernateUtil
				.queryDishIdByName(session, kid, dishName);
		if (dishId == null) {
			//if(this.getUserType().equals(CateringServiceUtil.KitchenType006)){
				Dish newDish = new Dish();
				newDish.setDishName(dishName);
				newDish.setKitchenId(this.getKitchenId());
				newDish.setPicturePath("");
				session.save(newDish);
				dishId = newDish.getDishId();
				return dishId;
			//}else{
			//	this.emptyMsg += "菜色[" + dishName + "]找不到資料\n";
			//}
		}
	//	HibernateUtil.saveIngredientbatchdataByDishId(session, dishId);
		return dishId;
	}

}
