package org.iii.ideas.catering_service.rest.api;

import java.awt.Menu;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
/*import org.iii.ideas.catering_service.dao.Fda;*/
import org.iii.ideas.catering_service.dao.Batchdata;
import org.iii.ideas.catering_service.dao.Code;
import org.iii.ideas.catering_service.dao.CodeDAO;
import org.iii.ideas.catering_service.dao.Dish;
import org.iii.ideas.catering_service.dao.DishBatchData;
import org.iii.ideas.catering_service.dao.DishBatchDataDAO;
import org.iii.ideas.catering_service.dao.IngredientCertificate;
import org.iii.ideas.catering_service.dao.IngredientCertificateDAO;
import org.iii.ideas.catering_service.dao.Ingredientbatchdata;
import org.iii.ideas.catering_service.dao.Kitchen;
import org.iii.ideas.catering_service.dao.School;
import org.iii.ideas.catering_service.dao.Schoolkitchen;
import org.iii.ideas.catering_service.dao.SchoolkitchenDAO;
import org.iii.ideas.catering_service.dao.SeasoningStockData;
import org.iii.ideas.catering_service.dao.Supplier;
import org.iii.ideas.catering_service.dao.Kitchenfda;
import org.iii.ideas.catering_service.dao.KitchenfdaDAO;
import org.iii.ideas.catering_service.dao.FdaCompanyReglist;
import org.iii.ideas.catering_service.dao.FdaCompanyReglistDAO;
import org.iii.ideas.catering_service.rest.bo.IngredientAttributeBO;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;
import org.iii.ideas.catering_service.util.XmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tempuri.CasQueryServiceSoapProxy;
import org.w3c.dom.Document;

public class CustomerQueryMenuDetailInfo_v2
		extends AbstractApiInterface<CustomerQueryMenuDetailInfo_v2Request, CustomerQueryMenuDetailInfo_v2Response> {
	private static final Logger log = LoggerFactory.getLogger(CustomerQueryMenuDetailInfo_v2.class);

	@Override
	public void process() throws NamingException, ParseException {
		Long mid = this.requestObj.getMid();
		
		this.responseObj.setMidBefore("0");
		this.responseObj.setMidAfter("0");
		Timestamp currentTS = CateringServiceUtil.getCurrentTimestamp();
		String currentTSStr = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", currentTS);
		this.responseObj.setNowdate(currentTSStr);//設定當前日期
		this.responseObj.setUserName(this.getUsername());
		
		List<Code> dishCodeList = new ArrayList<Code>();
		HashMap<String, String> dishCodeMap = new HashMap<String, String>();
		CodeDAO codedao = new CodeDAO();
		codedao.setSession(this.dbSession);
		// 取得餐名對應代碼表
		dishCodeList = codedao.findCodeListByType(CateringServiceCode.CODETYPE_DISHTYPE);
		Iterator<Code> irDish = dishCodeList.iterator();
		while (irDish.hasNext()) {
			Code row = irDish.next();
			dishCodeMap.put(row.getCode(), row.getName());
		}

		// ------------計算是否有前後一天------------
		String beforeMidHQL = "";
		//20150507 shine mod 加上menuType條件, 讓找前後一天的餐點會跟目前傳入的是同一種類
		if (this.getUsername() == null) {
			beforeMidHQL = "from Batchdata a,Batchdata b where a.schoolId =b.schoolId and a.kitchenId = b.kitchenId and a.menuDate < b.menuDate and b.batchDataId=:batchdataId and a.menuType=b.menuType  order by a.menuDate desc";
		} else {
			beforeMidHQL = "from Batchdata a,Batchdata b where a.schoolId =b.schoolId and a.kitchenId = b.kitchenId and a.menuDate < b.menuDate and b.batchDataId=:batchdataId and b.kitchenId = :kitchenId and a.menuType=b.menuType order by a.menuDate desc";
		}
		String afterMidHQL = "";
//		if (this.getUsername() == null) {
//			afterMidHQL = "from Batchdata a,Batchdata b where a.schoolId =b.schoolId and a.kitchenId = b.kitchenId and a.menuDate > b.menuDate and b.batchDataId=:batchdataId and a.menuDate <= :currentTSStr order by a.menuDate asc";
//		
//		} else {
			//afterMidHQL = "from Batchdata a,Batchdata b where a.schoolId =b.schoolId and a.kitchenId = b.kitchenId and a.menuDate > b.menuDate and b.batchDataId=:batchdataId and b.kitchenId = :kitchenId order by a.menuDate asc";
//		}
		//未來日仍會顯示 modify by ellis 20141209
		afterMidHQL = "from Batchdata a,Batchdata b where a.schoolId =b.schoolId and a.kitchenId = b.kitchenId and a.menuDate > b.menuDate and b.batchDataId=:batchdataId  and a.menuType=b.menuType order by a.menuDate asc";

		// 前一天
		Query beforeMidQuery = this.dbSession.createQuery(beforeMidHQL);
		beforeMidQuery.setParameter("batchdataId", mid);
		if (this.getUsername() != null) {
			beforeMidQuery.setParameter("kitchenId", this.getKitchenId());
		}
		beforeMidQuery.setMaxResults(1);
		List beforeMidList = beforeMidQuery.list();
		Iterator<Object[]> beforeMids = beforeMidList.iterator();
		while (beforeMids.hasNext()) {
			Object[] obj = beforeMids.next();
			Batchdata beforeBatchdata = (Batchdata) obj[0];
			this.responseObj.setMidBefore(String.valueOf(beforeBatchdata.getBatchDataId()));
		}
		// 後一天
		Query afterMidQuery = this.dbSession.createQuery(afterMidHQL);
		afterMidQuery.setParameter("batchdataId", mid);
//		if (this.getUsername() == null) {
//			afterMidQuery.setParameter("currentTSStr", currentTSStr);
//		} else {
//			afterMidQuery.setParameter("kitchenId", this.getKitchenId());
//		}
		afterMidQuery.setMaxResults(1);
		List afterMidList = afterMidQuery.list();
		Iterator<Object[]> afterMids = afterMidList.iterator();
		while (afterMids.hasNext()) {
			Object[] obj = afterMids.next();
			Batchdata beforeBatchdata = (Batchdata) obj[0];
			this.responseObj.setMidAfter(String.valueOf(beforeBatchdata.getBatchDataId()));
		}
		// ------------計算是否有前後一個月------------
		Criteria criteria = dbSession.createCriteria(Batchdata.class);
		criteria.add(Restrictions.eq("batchDataId", mid));

		List Menus = criteria.list();
		Iterator<Batchdata> iterator = Menus.iterator();
		while (iterator.hasNext()) {
			Batchdata menu = iterator.next();
			int kitchenId = menu.getKitchenId();
			this.responseObj.setDate(menu.getMenuDate().substring(0, 4) + menu.getMenuDate().substring(5, 7) + menu.getMenuDate().substring(8, 10));
			//20150501 shine add 記錄菜單類別
			this.responseObj.setMenuType(menu.getMenuType());
			
			// school
			Criteria criteriaSchool = dbSession.createCriteria(School.class);
			criteriaSchool.add(Restrictions.eq("schoolId", menu.getSchoolId())).add(Restrictions.eq("enable", 1));
			List Schools = criteriaSchool.list();
			Iterator<School> iteratorSchool = Schools.iterator();
			while (iteratorSchool.hasNext()) {
				School sc = iteratorSchool.next();
				this.responseObj.setSchoolName(sc.getSchoolName());
			}
			//--查詢供餐分數
			SchoolkitchenDAO schoolKitchenDao = new SchoolkitchenDAO();
			schoolKitchenDao.setSession(dbSession);
			Schoolkitchen schoolKiatchen = schoolKitchenDao.querySchoolkitchenById(menu.getSchoolId(), menu.getKitchenId());
			//System.out.println(schoolKiatchen+" sid:"+menu.getSchoolId()+" kid:"+this.getKitchenId());
			//System.out.println(schoolKiatchen.getQuantity());
			if (CateringServiceUtil.isNull(schoolKiatchen)){
				this.responseObj.setQuantity(0);
			}else{
				this.responseObj.setQuantity(schoolKiatchen.getQuantity());
			}
			// kitchen
			Criteria criteriaKitchen = dbSession.createCriteria(Kitchen.class);
			criteriaKitchen.add(Restrictions.eq("kitchenId", menu.getKitchenId()));
			List Kitchens = criteriaKitchen.list();
			Iterator<Kitchen> iteratorKitchen = Kitchens.iterator();
			//Kitchen kt = new Kitchen(); //trying
			while (iteratorKitchen.hasNext()) {
				Kitchen kt = iteratorKitchen.next();
				// supplierInfo
				this.responseObj.getSupplierInfo().setSupplierID(kt.getKitchenId());//test ID
				this.responseObj.getSupplierInfo().setSupplierName(kt.getKitchenName());// 供應商
				this.responseObj.getSupplierInfo().setSupplierAddress(kt.getAddress());// 供應商地址
				this.responseObj.getSupplierInfo().setSupplierPhone(kt.getTel());// 供應商電話
				this.responseObj.getSupplierInfo().setSupplierLeader(kt.getChef());// 負責人
				this.responseObj.getSupplierInfo().setDietitians(kt.getNutritionist());// 營養師
				this.responseObj.getSupplierInfo().setAuthenticate(kt.getHaccp());// 品質認證體系
				this.responseObj.getSupplierInfo().setKitchenType(kt.getKitchenType());// 團膳別
			}
			
			//get fdaInfo--test
			KitchenfdaDAO kitchenfdaDao = new KitchenfdaDAO();
			kitchenfdaDao.setSession(dbSession);
			Kitchenfda kitchenfda = kitchenfdaDao.queryKitchenfdaByKitchenId(menu.getKitchenId());
			if(!CateringServiceUtil.isNull(kitchenfda)){
				FdaCompanyReglistDAO fdaCompanyReglistDao = new FdaCompanyReglistDAO();
				fdaCompanyReglistDao.setSession(dbSession);
				FdaCompanyReglist fdaCompanyReglist = fdaCompanyReglistDao.queryFdaCompanyReglistByFdaCompanyId(kitchenfda.getFdaCompanyId());
				this.responseObj.getSupplierInfo().setFdaCompanyName(fdaCompanyReglist.getCompany_Name());
				this.responseObj.getSupplierInfo().setFdaCompanyBusinessId(fdaCompanyReglist.getBusinessId());
				this.responseObj.getSupplierInfo().setFdaCompanyAddress(fdaCompanyReglist.getAddress());
				this.responseObj.getSupplierInfo().setFdaCompanyId(fdaCompanyReglist.getFdaCompanyId());
				this.responseObj.getSupplierInfo().setFdaCompanyRegType(fdaCompanyReglist.getRegType());
			}
			
			// nutrition
			String mainFood = HibernateUtil.queryDishNameById(dbSession, menu.getMainFoodId());
			this.responseObj.getNutrition().setMainFood(menu.getTypeGrains());
			this.responseObj.getNutrition().setVegetable(menu.getTypeVegetable());
			this.responseObj.getNutrition().setOil(menu.getTypeOil());
			this.responseObj.getNutrition().setMeatBeans(menu.getTypeMeatBeans());
			this.responseObj.getNutrition().setFruit(menu.getTypeFruit());
			this.responseObj.getNutrition().setMilk(menu.getTypeMilk());
			this.responseObj.getNutrition().setCalories(menu.getCalorie());

			// ------------------------
			// lunchContent array

//			if (menu.getMainFoodId() != null && menu.getMainFoodId() != 0) {
			if (!"A".equals(menu.getSrcType())) {
				if (menu.getMainFoodId() != null && menu.getMainFoodId() != 0) {
					addDishInfo(dbSession, menu.getKitchenId(), getMenunameByCode(CateringServiceCode.DISHTYPE_MAINFOOD, dishCodeMap), menu.getMainFoodId());
				}
				if (menu.getMainFood1id() != null && menu.getMainFood1id() != 0) {
					addDishInfo(dbSession, menu.getKitchenId(), getMenunameByCode(CateringServiceCode.DISHTYPE_MAINFOOD1, dishCodeMap), menu.getMainFood1id());
				}
				if (menu.getMainDishId() != null && menu.getMainDishId() != 0) {
					addDishInfo(dbSession, menu.getKitchenId(), getMenunameByCode(CateringServiceCode.DISHTYPE_MAINDISH, dishCodeMap), menu.getMainDishId());
				}
				if (menu.getMainDish1id() != null && menu.getMainDish1id() != 0) {
					addDishInfo(dbSession, menu.getKitchenId(), getMenunameByCode(CateringServiceCode.DISHTYPE_MAINDISH1, dishCodeMap), menu.getMainDish1id());
				}
				if (menu.getMainDish2id() != null && menu.getMainDish2id() != 0) {
					addDishInfo(dbSession, menu.getKitchenId(), getMenunameByCode(CateringServiceCode.DISHTYPE_MAINDISH2, dishCodeMap), menu.getMainDish2id());
				}
				if (menu.getMainDish3id() != null && menu.getMainDish3id() != 0) {
					addDishInfo(dbSession, menu.getKitchenId(), getMenunameByCode(CateringServiceCode.DISHTYPE_MAINDISH3, dishCodeMap), menu.getMainDish3id());
				}
				if (menu.getSubDish1id() != null && menu.getSubDish1id() != 0) {
					addDishInfo(dbSession, menu.getKitchenId(), getMenunameByCode(CateringServiceCode.DISHTYPE_SUBDISH1, dishCodeMap), menu.getSubDish1id());
				}
				if (menu.getSubDish2id() != null && menu.getSubDish2id() != 0) {
					addDishInfo(dbSession, menu.getKitchenId(), getMenunameByCode(CateringServiceCode.DISHTYPE_SUBDISH2, dishCodeMap), menu.getSubDish2id());
				}
				if (menu.getSubDish3id() != null && menu.getSubDish3id() != 0) {
					addDishInfo(dbSession, menu.getKitchenId(), getMenunameByCode(CateringServiceCode.DISHTYPE_SUBDISH3, dishCodeMap), menu.getSubDish3id());
				}
				if (menu.getSubDish4id() != null && menu.getSubDish4id() != 0) {
					addDishInfo(dbSession, menu.getKitchenId(), getMenunameByCode(CateringServiceCode.DISHTYPE_SUBDISH4, dishCodeMap), menu.getSubDish4id());
				}
				if (menu.getSubDish5id() != null && menu.getSubDish5id() != 0) {
					addDishInfo(dbSession, menu.getKitchenId(), getMenunameByCode(CateringServiceCode.DISHTYPE_SUBDISH5, dishCodeMap), menu.getSubDish5id());
				}
				if (menu.getSubDish6id() != null && menu.getSubDish6id() != 0) {
					addDishInfo(dbSession, menu.getKitchenId(), getMenunameByCode(CateringServiceCode.DISHTYPE_SUBDISH6, dishCodeMap), menu.getSubDish6id());
				}
				if (menu.getVegetableId() != null && menu.getVegetableId() != 0) {
					addDishInfo(dbSession, menu.getKitchenId(), getMenunameByCode(CateringServiceCode.DISHTYPE_VEGETABLE, dishCodeMap), menu.getVegetableId());
				}
				if (menu.getSoupId() != null && menu.getSoupId() != 0) {
					addDishInfo(dbSession, menu.getKitchenId(), getMenunameByCode(CateringServiceCode.DISHTYPE_SOUP, dishCodeMap), menu.getSoupId());
				}
				if (menu.getDessertId() != null && menu.getDessertId() != 0) {
					addDishInfo(dbSession, menu.getKitchenId(), getMenunameByCode(CateringServiceCode.DISHTYPE_DESSERT, dishCodeMap), menu.getDessertId());
				}
				if (menu.getDessert1id() != null && menu.getDessert1id() != 0) {
					addDishInfo(dbSession, menu.getKitchenId(), getMenunameByCode(CateringServiceCode.DISHTYPE_DESSERT1, dishCodeMap), menu.getDessert1id());
				}
				//因應幼兒園菜單，由dishbatchdata取得dish modify by Ellis 20150604
				if (menu.getMainFoodId() == 0) {
					DishBatchDataDAO dishbatchdatadao = new DishBatchDataDAO();
					dishbatchdatadao.setSession(dbSession);
					List dishListResult = dishbatchdatadao.getDishBatchDataWithTypenameByBatchId(mid);
					Iterator<DishBatchData> ir = dishListResult.iterator();
					while (ir.hasNext()) {
						DishBatchData row = ir.next();
						addDishInfo(dbSession, menu.getKitchenId(), row.getDishType(), row.getDishId());
					}
				}
			} else if ("A".equals(menu.getSrcType())) {
				DishBatchDataDAO dishbatchdatadao = new DishBatchDataDAO();
				dishbatchdatadao.setSession(dbSession);
				List dishListResult = dishbatchdatadao.getDishBatchDataWithTypenameByBatchId(mid);
				Iterator<DishBatchData> ir = dishListResult.iterator();
				while (ir.hasNext()) {
					DishBatchData row = ir.next();
					// 要另外處理台北市的圖檔路徑問題(因為沒有dishId)
					addDishInfo2(row.getDishType(), row.getDishName(), row.getDishId(), row.getDishShowName());
				}
			}
			//未來日期不搜尋食材、調味料 modify by ellis 20141205
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
			//long dis = formatter.parse(menu.getMenuDate()).getTime()-formatter.parse(currentTSStr).getTime();
			if (this.getUsername() != null || formatter.parse(menu.getMenuDate()).getTime()-formatter.parse(currentTSStr).getTime()<=0){
				this.setFoodInfo(dbSession, menu);
			}
			//若菜單為未來日或菜色全無照片時，則以清單顯示
			this.responseObj.setShowMode(authShowMode(menu.getMenuDate(),currentTSStr,menu.getKitchenId()));
		}
		
		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");
		//dbSession.close();
	}

	String imagePath = "/cateringservice/file/SHOW/";

	/*
	 * 判斷日期是否大於今日，若是則不顯示照片僅顯示清單 判斷是否全部都無照片，若全無則僅顯示清單 add by ellis 20150106
	 */
	public boolean authShowMode(String MenuDate, String CurrentDate, Integer KitchenId) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		String uploadPatch = "";
		try {
			uploadPatch = CateringServiceUtil.getConfig("uploadPath");
			// 判斷日期，若菜單日期小於當前日期則顯示照片
			if (formatter.parse(MenuDate).getTime() - formatter.parse(CurrentDate).getTime() > 0) {
				return false;
			}
			// String filepathOld = "";
			// String filepathNew = "";
			// //判斷有無照片，只要有一張照片便顯示
			// for(int i=0;i<this.responseObj.getLunchContent().size();i++){
			// filepathOld = uploadPatch + "dish/" + KitchenId + "/" + KitchenId
			// + "_" + this.responseObj.getLunchContent().get(i).getDishid();
			// if
			// (CateringServiceUtil.fileExists(CateringServiceUtil.getDishImageFileName(KitchenId,
			// this.responseObj.getLunchContent().get(i).getDishid(), "jpg"))) {
			// return true;
			// } else if
			// (CateringServiceUtil.fileExists(CateringServiceUtil.getDishImageFileName(KitchenId,
			// this.responseObj.getLunchContent().get(i).getDishid(), "png"))) {
			// return true;
			// } else if
			// (CateringServiceUtil.fileExists(CateringServiceUtil.getDishImageFileName(KitchenId,
			// this.responseObj.getLunchContent().get(i).getDishid(), "gif"))) {
			// return true;
			// } else if (CateringServiceUtil.fileExists(filepathOld + ".jpg"))
			// {
			// return true;
			// } else if (CateringServiceUtil.fileExists(filepathOld + ".png"))
			// {
			// return true;
			// } else if (CateringServiceUtil.fileExists(filepathOld + ".gif"))
			// {
			// return true;
			// }
			// }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public void addDishInfo(Session session, int kitchenId, String foodName, Long dishId) {
		CustomerQueryMenuDetailInfoLunchContent lunchContent = new CustomerQueryMenuDetailInfoLunchContent();
		lunchContent.setCategory(foodName);
		lunchContent.setFoodName(HibernateUtil.queryDishNameById(session, dishId));
		lunchContent.setImage(imagePath + "dishId" + "|" + kitchenId + "|" + dishId + "|"
				+ CateringServiceUtil.DishImageSizeHigh + "|" + CateringServiceUtil.DishImageSizeWidth);
		lunchContent.setDishid(dishId);
		this.responseObj.getLunchContent().add(lunchContent);
		// //this.setFoodInfo(session,DishId);
	}

	// 從dishbatchdata抓資料的處理
	public void addDishInfo2(String foodName, String dishName, Long dishId, String showName) {
		CustomerQueryMenuDetailInfoLunchContent lunchContent = new CustomerQueryMenuDetailInfoLunchContent();
		lunchContent.setCategory(foodName);
		lunchContent.setFoodName(dishName);
		// lunchContent.setImage(imagePath + "dishId" + "|" + kitchenId + "|" +
		// dishId + "|" + CateringServiceUtil.DishImageSizeHigh + "|" +
		// CateringServiceUtil.DishImageSizeWidth);
		lunchContent.setImage(showName);
		lunchContent.setDishid(dishId);
		this.responseObj.getLunchContent().add(lunchContent);
		// //this.setFoodInfo(session,DishId);
	}

	// 抓食材與調味料
	public void setFoodInfo(Session session, Batchdata batchdata) {
		// kitchen
		List<Long> dishIdList = HibernateUtil.getDishIdByBatchdata(batchdata);

		Integer kitchenId = batchdata.getKitchenId();
		Long batchdataId = batchdata.getBatchDataId();

		Dish dishSeasoning = HibernateUtil.queryDishByName(session, kitchenId,
				CateringServiceUtil.ColumnNameOfSeasoning);
		Long dishSeasonId = (long) 0; // 台北市的調味料為0 先預設

		if (dishSeasoning != null) {
			dishSeasonId = dishSeasoning.getDishId();
		}

		if (batchdata.getSrcType() != null && batchdata.getSrcType().equals("A")) {
			dishSeasonId = (long) 0;
		}

		Criteria criteriaIngredient = session.createCriteria(Ingredientbatchdata.class);
		criteriaIngredient.add(Restrictions.eq("batchDataId", batchdataId));
		List ingredients = criteriaIngredient.list();
		String queryString = "";
		if (dishSeasoning == null) {
			// queryString = "from Ingredientbatchdata where batchDataId=:id and
			// dishId in :dishIdList ";
			// 不顯示進貨日期為空的食材 20140911 KC
			queryString = "from Ingredientbatchdata where batchDataId=:id  and dishId in :dishIdList ";
		} else {
			// 過濾調味料資料
			if("A".equals(batchdata.getSrcType())){
				queryString = "from Ingredientbatchdata where batchDataId=:id  and dishId!=:dishId ";
			}else{
				queryString = "from Ingredientbatchdata where batchDataId=:id  and dishId!=:dishId  and dishId in :dishIdList ";
			}
		}
		Query queryObject = session.createQuery(queryString);
		queryObject.setParameter("id", batchdataId);
		if(!"A".equals(batchdata.getSrcType())){
			if(dishIdList.size()==0) dishIdList.add((long)-1);  //20150507 shine add dishIdList不能為空,至少要有個值,否則SQL會出錯
			queryObject.setParameterList("dishIdList", dishIdList);
		}
//		if (dishIdList.size() == 0)
//			dishIdList.add((long) -1); // 20150507 shine add
//										// dishIdList不能為空,至少要有個值,否則SQL會出錯
//		queryObject.setParameterList("dishIdList", dishIdList);
		if (dishSeasoning != null) {
			// queryObject.setParameter("dishId", dishSeasoning.getDishId());
			queryObject.setParameter("dishId", dishSeasonId);
		}
		List ingredientBatchdataList = queryObject.list();
		Iterator<Ingredientbatchdata> iteratorIngredients = ingredientBatchdataList.iterator();
		//FdaCompanyReglist fdaCompanyReglist = fdaCompanyReglistDao.queryFdaCompanyReglistByFdaCompanyId(kitchenfda.getFdaCompanyId());
		while (iteratorIngredients.hasNext()) {
			Ingredientbatchdata ig = iteratorIngredients.next();
			// foodInfo array 食材資訊
			CustomerQueryMenuDetailInfoFoodInfo foodInfo = new CustomerQueryMenuDetailInfoFoodInfo();
			foodInfo.setFoodName(HibernateUtil.queryDishNameById(session, ig.getDishId()));
			foodInfo.setMaterial(ig.getIngredientName()); // 原料
			foodInfo.setBrand(ig.getBrand() == null ? "" : ig.getBrand());// 品牌
			Supplier supplier = HibernateUtil.querySupplierById(this.dbSession, ig.getSupplierId());
			if (supplier == null) {
				if (!CateringServiceUtil.isEmpty(ig.getSupplierName())) {
					foodInfo.setSource(ig.getSupplierName()); // 接設定 201404007
																// KC
					foodInfo.setSupplierCompanyId(ig.getSupplierCompanyId()); // 廠商統編
																				// 20140416
																				// Raymond
				} else {
					foodInfo.setSource("");// 來源
					foodInfo.setSupplierCompanyId("");// 廠商統編
				}
			} else {
				//fda--test
				FdaCompanyReglistDAO fdaCompanyReglistDao = new FdaCompanyReglistDAO();
				fdaCompanyReglistDao.setSession(this.dbSession);
				if(!CateringServiceUtil.isNull(supplier.getFdaCompanyId())){
					FdaCompanyReglist fdaCompanyReglist = fdaCompanyReglistDao.queryFdaCompanyReglistByFdaCompanyId(supplier.getFdaCompanyId());
					foodInfo.setFdaCompanyName(fdaCompanyReglist.getCompany_Name());
					foodInfo.setFdaCompanyBusinessId(fdaCompanyReglist.getBusinessId());
					foodInfo.setFdaCompanyAddress(fdaCompanyReglist.getAddress());
					foodInfo.setFdaCompanyId(fdaCompanyReglist.getFdaCompanyId());
					foodInfo.setFdaCompanyRegType(fdaCompanyReglist.getRegType());
				}
				//fda--test
				
				foodInfo.setSource(supplier.getSupplierName());// 來源
				foodInfo.setSupplierCompanyId(supplier.getCompanyId());// 廠商統編
			}
			foodInfo.setAuthenticate(
					ig.getSourceCertification() == null ? "" : ig.getSourceCertification().trim().toUpperCase());// 認證標章
			foodInfo.setAuthenticateId(ig.getCertificationId() == null ? "" : ig.getCertificationId().trim());
			// --------為中衛新增的欄位---------
			// 商品名稱
			if (!CateringServiceUtil.isEmpty(ig.getProductName()))
				foodInfo.setProductName(ig.getProductName());
			else
				foodInfo.setProductName("");
			//
			if (!CateringServiceUtil.isEmpty(ig.getManufacturer()))
				foodInfo.setManufacture(ig.getManufacturer());
			else
				foodInfo.setManufacture("");

			if (null != ig.getStockDate()) {
				try {
					foodInfo.setStockDate(
							HibernateUtil.converTimestampToStr("yyyy/MM/dd", ig.getStockDate(), Locale.ENGLISH));
				} catch (ParseException e) {
					log.debug("請確認進貨日期時間格式有誤  每日食材檔ID:" + ig.getIngredientBatchId());
				}
			}

			foodInfo.setManufactureDate(ig.getManufacturer());
			if (null != ig.getExpirationDate()) {
				try {
					foodInfo.setExpirationDate(
							HibernateUtil.converTimestampToStr("yyyy/MM/dd", ig.getExpirationDate(), Locale.ENGLISH));
				} catch (ParseException e) {
					log.debug("請確認有效日期時間格式有誤  每日食材檔ID:" + ig.getIngredientBatchId());
				}
			}

			IngredientAttributeBO ingredientAttr = CateringServiceUtil.getIngredientAttrBo(ig.getIngredientAttr());
			// 是否為加工食品
			if (ingredientAttr.getPsfood() == 0) {
				foodInfo.setProcessedFood("N");
			} else {
				foodInfo.setProcessedFood("Y");
			}
			// 是否為基改食品
			String gmAttr = "";
			if (ingredientAttr.getGmbean() == 1) {
				gmAttr += "黃豆;";
			}
			if (ingredientAttr.getGmcorn() == 1) {
				gmAttr += "玉米;";
			}
			foodInfo.setGeneticallyModifiedFood(gmAttr);

			if (CateringServiceUtil.isEmpty(ig.getIngredientQuantity())) {
				foodInfo.setIngredientQuantity(0);
			} else {
				foodInfo.setIngredientQuantity(Double.valueOf(ig.getIngredientQuantity()));
			}
			foodInfo.setIngredientUnit(ig.getIngredientUnit());
			// -----------------
			// 驗證各種認證資訊是否正確
			// 如果有則回傳認證資訊
			if (foodInfo.getAuthenticate().trim() != "" && foodInfo.getAuthenticateId().trim() != "") {

				switch (foodInfo.getAuthenticate()){
				case "CAS":
					try {
						IngredientCertificate ic = getCert(foodInfo.getAuthenticateId().trim(),foodInfo.getAuthenticate().trim());
						if(ic != null && ic.getSourceXml() != null)
							foodInfo.setIngredientCertificateObject(transXmlStringToIngredientCertificateObject(ic.getSourceXml()));
					}catch (RemoteException e) {
						log.error("Connect to TCBK SOAP error" , e);
						e.printStackTrace();
					}
					catch (Exception e) {
						log.error("Get Certificate error" , e);
						e.printStackTrace();
					}
					break;
				case "SN":
//					IngredientCertificate ic = new IngredientCertificate();
//					ic.setCertNo(foodInfo.getAuthenticateId().trim());
//					ic.setCertType(foodInfo.getAuthenticate().trim());
					IngredientCertificateObject ico = new IngredientCertificateObject();
					ico.setCertNo(foodInfo.getAuthenticateId().trim());
					ico.setCertType(foodInfo.getAuthenticate().trim());
					foodInfo.setIngredientCertificateObject(ico);
					break;
				}
				
			}

			/*
			 * if(foodInfo.getAuthenticate().toUpperCase().equals("CAS")){
			 * String href = "<a href=\""+"http://cas.coa.gov.tw/"+"\">CAS</a>";
			 * foodInfo.setAuthenticate(href); }
			 */
			this.responseObj.getFoodInfo().add(foodInfo);
		}

		// 查詢調味料內容
		// if (dishSeasoning != null) {
		if (dishSeasoning != null || dishIdList.contains(0)) {
			// queryString = "from Ingredientbatchdata where batchDataId=:id and
			// dishId = :dishId ";
			// 不顯示進貨日期為nul的資料 20140911 KC
			queryString = "from Ingredientbatchdata where batchDataId=:id  and dishId  = :dishId ";
			queryObject = session.createQuery(queryString);
			queryObject.setParameter("id", batchdataId);
			queryObject.setParameter("dishId", dishSeasonId);
			ingredientBatchdataList = queryObject.list();
			iteratorIngredients = ingredientBatchdataList.iterator();
			while (iteratorIngredients.hasNext()) {
				Ingredientbatchdata ig = iteratorIngredients.next();
				// seasoning array 調味料
				CustomerQueryMenuDetailInfoSeasoning seasoning = new CustomerQueryMenuDetailInfoSeasoning();
				seasoning.setMaterial(ig.getIngredientName());// 原料
				seasoning.setBrand(ig.getBrand() == null ? "" : ig.getBrand());// 品牌
				Supplier supplier = HibernateUtil.querySupplierById(this.dbSession, ig.getSupplierId());
				if (supplier == null) {
					seasoning.setSource("");// 來源
					seasoning.setSupplierCompanyId("");// 廠商統編
				} else {
					seasoning.setSource(supplier.getSupplierName());// 來源
					seasoning.setSupplierCompanyId(supplier.getCompanyId());// 廠商統編
				}
				seasoning.setAuthenticate(
						ig.getSourceCertification() == null ? "" : ig.getSourceCertification().trim().toUpperCase());// 認證標章
				seasoning.setAuthenticateId(ig.getCertificationId() == null ? "" : ig.getCertificationId().trim());// 認證標章

				// --------為中衛新增的欄位---------
				// 商品名稱
				if (!CateringServiceUtil.isEmpty(ig.getProductName()))
					seasoning.setProductName(ig.getProductName());
				else
					seasoning.setProductName("");
				//
				if (!CateringServiceUtil.isEmpty(ig.getManufacturer()))
					seasoning.setManufacture(ig.getManufacturer());
				else
					seasoning.setManufacture("");

				// 驗證各種認證資訊是否正確
				// 如果有則回傳認證資訊
				if ((ig.getSourceCertification() != null && ig.getSourceCertification().trim() != "")
						&& (ig.getCertificationId() != null && ig.getCertificationId().trim() != "")) {
					switch(seasoning.getAuthenticate()){
					case "CAS" :
						try {
							IngredientCertificate ic = getCert(ig.getCertificationId().trim(),
									ig.getSourceCertification().trim());
							if (ic != null && ic.getSourceXml() != null)
								seasoning.setIngredientCertificateObject(
										transXmlStringToIngredientCertificateObject(ic.getSourceXml()));
						} catch (RemoteException e) {
							log.error("Connect to TCBK SOAP error", e);
							e.printStackTrace();
						} catch (Exception e) {
							log.error("Get Certificate error", e);
							e.printStackTrace();
						}
						break;
					case "SN" :	
						IngredientCertificateObject ico = new IngredientCertificateObject();
						ico.setCertNo(seasoning.getAuthenticateId().trim());
						ico.setCertType(seasoning.getAuthenticate().trim());
						seasoning.setIngredientCertificateObject(ico);
						break;
					}
				}

				this.responseObj.getSeasoning().add(seasoning);
			}
		}

		// 顯示新版調味料
		String hql = "FROM SeasoningStockData a WHERE a.kitchenId = :kitchenId" + " AND a.useStartDate <= :menuDate "
				+ " AND a.useeEndDate >= :menuDate " + " AND a.Enable = 1";
		Query query = dbSession.createQuery(hql);
		query.setParameter("kitchenId", batchdata.getKitchenId());
		query.setParameter("menuDate", new Date(batchdata.getMenuDate()));
		List newseasoning = query.list();
		Iterator<SeasoningStockData> iteratorSessonings = newseasoning.iterator();
		while (iteratorSessonings.hasNext()) {
			SeasoningStockData ig = iteratorSessonings.next();
			// seasoning array 調味料
			CustomerQueryMenuDetailInfoSeasoning seasoning = new CustomerQueryMenuDetailInfoSeasoning();
			seasoning.setMaterial(ig.getSeasoningName());// 原料
			seasoning.setBrand(ig.getBrand() == null ? "" : ig.getBrand());// 品牌
			Supplier supplier = HibernateUtil.querySupplierById(this.dbSession, ig.getSupplierId());
			if (supplier == null) {
				seasoning.setSource("");// 來源
				seasoning.setSupplierCompanyId("");// 廠商統編
			} else {
				seasoning.setSource(supplier.getSupplierName());// 來源
				seasoning.setSupplierCompanyId(supplier.getCompanyId());// 廠商統編
			}
			seasoning.setAuthenticate(
					ig.getSourceCertification() == null ? "" : ig.getSourceCertification().trim().toUpperCase());// 認證標章
			seasoning.setAuthenticateId(ig.getCertificationId() == null ? "" : ig.getCertificationId().trim());// 認證標章

			// --------為中衛新增的欄位---------
			// 商品名稱
			if (!CateringServiceUtil.isEmpty(ig.getProductName()))
				seasoning.setProductName(ig.getProductName());
			else
				seasoning.setProductName("");
			//
			if (!CateringServiceUtil.isEmpty(ig.getManufacturer()))
				seasoning.setManufacture(ig.getManufacturer());
			else
				seasoning.setManufacture("");

			// 驗證各種認證資訊是否正確
			// 如果有則回傳認證資訊
			if ((ig.getSourceCertification() != null && ig.getSourceCertification().trim() != "")
					&& (ig.getCertificationId() != null && ig.getCertificationId().trim() != "")) {
				switch(seasoning.getAuthenticate()){
				case "CAS" :
					try {
						IngredientCertificate ic = getCert(ig.getCertificationId().trim(),
								ig.getSourceCertification().trim());
						if (ic != null && ic.getSourceXml() != null)
							seasoning.setIngredientCertificateObject(
									transXmlStringToIngredientCertificateObject(ic.getSourceXml()));
					} catch (RemoteException e) {
						log.error("Connect to TCBK SOAP error", e);
						e.printStackTrace();
					} catch (Exception e) {
						log.error("Get Certificate error", e);
						e.printStackTrace();
					}
					break;
				case "SN" :	
					IngredientCertificateObject ico = new IngredientCertificateObject();
					ico.setCertNo(seasoning.getAuthenticateId().trim());
					ico.setCertType(seasoning.getAuthenticate().trim());
					seasoning.setIngredientCertificateObject(ico);
					break;	
				}
			}

			this.responseObj.getSeasoning().add(seasoning);
		}
	}

	// 取得食品雲CAS認證 透過Third party 對方有檔8080 port
	private String getCompanyInfoFromTCBK(String certNo) throws RemoteException {
		String result = "";
		if (certNo != null && !certNo.isEmpty()) {
			CasQueryServiceSoapProxy casQueryServiceSoapProxy = new CasQueryServiceSoapProxy();
			result = casQueryServiceSoapProxy.queryCompany(certNo);
		}
		return result;
	}

	// 取得IngredientCertificate
	private IngredientCertificate getCert(String certNo, String certType) throws Exception {
		IngredientCertificateDAO icDao = new IngredientCertificateDAO();
		IngredientCertificate ic = icDao.queryIngredientCertificateByCertNo(certNo, certType);
		if (ic == null) {
			switch (certType) {
			case "CAS":
				if (!certNo.isEmpty()) {
					// 20140827 SOAP問題先行MARK
					String xml = getCompanyInfoFromTCBK(certNo);
					// String xml = null;
					if (xml != null) {
						IngredientCertificateObject obj = transXmlStringToIngredientCertificateObject(xml);
						if (obj != null) {
							ic = new IngredientCertificate();
							ic.setCertNo(certNo);
							ic.setCertType(certType);
							ic.setSourceXml(xml);
							icDao.updateIngredientCertificate(ic);
							ic = icDao.queryIngredientCertificateByCertNo(certNo, certType);
						}
					}
				}
				break;
			case "吉園圃":
				break;
			default:
				break;
			}
		}
		return ic;
	}

	// 轉換IngredientCertificate source XML to object
	private IngredientCertificateObject transXmlStringToIngredientCertificateObject(String xml) throws Exception {
		IngredientCertificateObject ico = new IngredientCertificateObject();
		Document doc = XmlUtil.loadXML(xml);
		String result = XmlUtil.getNodeValue(doc, "Result");
		if (result != null && result.equals("Success")) {
			ico.setCertNo(XmlUtil.getNodeValue(doc, "Emblem_ID"));
			ico.setCertType("CAS");//++修正CAS無資料 --chu--
			ico.setCompanyId(XmlUtil.getNodeValue(doc, "CompanyId"));
			ico.setCompanyName(XmlUtil.getNodeValue(doc, "Name"));
			ico.setAddress(XmlUtil.getNodeValue(doc, "Address"));
			ico.setDirector(XmlUtil.getNodeValue(doc, "Director"));
			ico.setFax(XmlUtil.getNodeValue(doc, "Fax"));
			ico.setTel(XmlUtil.getNodeValue(doc, "Tel"));
			ico.setWebsite(XmlUtil.getNodeValue(doc, "Website"));
		} else {
			return null;
		}
		return ico;
	}

	private String getMenunameByCode(String code, HashMap<String, String> map) {
		if (map.containsKey(code)) {
			return map.get(code);
		} else {
			return "";
		}
	}

}
