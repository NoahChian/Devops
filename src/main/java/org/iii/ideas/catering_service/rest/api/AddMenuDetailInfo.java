package org.iii.ideas.catering_service.rest.api;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.Batchdata;
import org.iii.ideas.catering_service.dao.Dish;
import org.iii.ideas.catering_service.dao.School;
import org.iii.ideas.catering_service.dao.Schoolkitchen;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;
import org.iii.ideas.catering_service.util.LogUtil;

//新增菜單詳細資訊
public class AddMenuDetailInfo
		extends
		AbstractApiInterface<AddMenuDetailInfoRequest, AddMenuDetailInfoResponse> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String emptyMsg = "";

	@Override
	public void process() throws NamingException {		
		if (this.getUsername() == null) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		
		int kid = this.getKitchenId();
		int schoolId = 0;
		String schoolNameList = "";
		String schoolName = "";
		String menuDate = "";
		String menuDetail = "";
		Transaction tx = dbSession.beginTransaction();
		// tx.begin();		
				
		this.emptyMsg = "";
		schoolNameList = this.requestObj.getSchoolName();
		menuDate = this.requestObj.getMenuDate();		
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
			this.responseObj.setMsg(this.emptyMsg);
			return;
		}

		Batchdata menu = new Batchdata();		
		School sch = null; 
		//20150211 shine mod 學校改成可一次傳入多間學校, 新增同一份菜單
		String[] schoolList = schoolNameList.split(",");		
		StringBuilder errorMessage = new StringBuilder();
		
		for(int i=0;i<schoolList.length;i++){			
			schoolName = schoolList[i];
			sch = HibernateUtil.querySchoolByKitchenAndName(dbSession,kid,schoolName);
			if(sch == null){
				errorMessage.append(schoolName + " 找不到學校資料!\n");
				continue;
//				this.responseObj.setResStatus(0);
//				this.responseObj.setMsg("找不到學校資料");
//				return;
			}
			schoolId = sch.getSchoolId();
			//增加判斷是否現況供餐 add by Ellis 20160107
			Criteria criteria2 = dbSession.createCriteria(Schoolkitchen.class);
			criteria2.add(Restrictions.eq("schoolId", schoolId));
			criteria2.add(Restrictions.eq("kitchenId", kid));
			Schoolkitchen sc = (Schoolkitchen) criteria2.uniqueResult();
			if(sc.getOffered()==0){
				errorMessage.append(schoolName + " 並未勾選現況供餐!");
				break;
			}
			
			//20150327 shine add 檢查是否已有資料,若已有資料則不做
			Criteria criteria = dbSession.createCriteria(Batchdata.class);
			criteria.add(Restrictions.eq("schoolId", schoolId));
			criteria.add(Restrictions.eq("kitchenId", kid));
			criteria.add(Restrictions.eq("menuDate", menuDate));
			menu =(Batchdata) criteria.uniqueResult();
			if (menu!=null){
				this.responseObj.setResStatus(0);
				this.responseObj.setMsg("已有重覆資料");
				errorMessage.append(schoolName + " 已存在資料!");
				break;
			}			

			
			menu = new Batchdata();	
			menu.setKitchenId(kid);
			menu.setSchoolId(schoolId);
			menu.setModifyUser(this.getUsername());
			try{
				menu.setMenuDate(menuDate);
			}catch(Exception e){
				errorMessage.append(schoolName + " 日期資料有誤!\n");
				continue;
//				this.responseObj.setResStatus(0);
//				this.responseObj.setMsg("日期資料有誤");
//				return;
			}
			
			menu.setLotNumber(CateringServiceUtil.defaultLotNumber);
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
			menu.setUploadDateTime(CateringServiceUtil.getCurrentTimestamp());
			menu.setMenuType(1); //預設為1，午餐
			menu.setEnable(1); //預設為顯示
			//紀錄菜單寫入資訊
			menuDetail += "廚房:"+kid+",<br>";
			menuDetail += "供餐學校:"+schoolName+","+schoolId+",<br>";
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
			menuDetail += "熱量:"+this.requestObj.getCalorie()+",";
//			menuDetail += "mid:"+menu.getBatchDataId().toString();
			//dbSession.update(menu);
			try{
				HibernateUtil.saveBatchdata(dbSession, menu);
				this.responseObj.setResStatus(1);
				this.responseObj.setMsg("");
				
			}catch (Exception ex){
				tx.rollback();
				this.responseObj.setResStatus(-1);
				this.responseObj.setMsg(ex.getMessage());
				errorMessage.append(schoolName + " 發生錯誤: " + ex.getMessage() + "\n");
				
			}
		}
		
		if(errorMessage.length()==0){
			tx.commit();
			this.responseObj.setResStatus(1);
			this.responseObj.setMsg("");
		}else{
			try{
				tx.rollback();
			}catch(Exception e){}
			
			this.responseObj.setResStatus(-1);
			this.responseObj.setMsg(errorMessage.toString());
		}
		LogUtil.writeFileUploadLog(menu.getBatchDataId().toString(), this.getUsername(), "新增菜單:"+this.responseObj.getMsg()+menuDetail, menu.getBatchDataId().toString(), "UI_Menu");
		
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
