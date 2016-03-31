package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Example;
import org.iii.ideas.catering_service.rest.bo.IngredientAttributeBO;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public class SeasoningStockDataDAO {
	private static final Logger log = LoggerFactory.getLogger(SeasoningStockDataDAO.class);
	// property constants
			
	private SessionFactory sessionFactory;
	private Session dbSession;

	public SeasoningStockDataDAO() {

	}

	public SeasoningStockDataDAO(Session dbSession) {
		this.dbSession = dbSession;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setSession(Session session) {
		this.dbSession = session;
	}

	private Session getCurrentSession() {
		if (this.dbSession == null) {
			return sessionFactory.getCurrentSession();
		} else {
			return this.dbSession;
		}
	}

	protected void initDao() {
		// do nothing
	}

	public void save(SeasoningStockData transientInstance) {
		log.debug("saving Ingredientbatchdata instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(SeasoningStockData persistentInstance) {
		log.debug("deleting Ingredientbatchdata instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
//	/*
//	 * 查詢廚房調味料清單 
//	 * 1.無輸入名稱全列
//	 * 2.有輸入名稱則找出相對應資料 
//	 * add by Ellis 20150108
//	 */
//	public List<SeasoningStockData> querySeasoningListPager(String seasoningName, Integer kitchenId, Integer pageIndex, Integer pageLimitNum,String searchDate) throws HibernateException, ParseException {
//		int startIndex = (pageIndex - 1 ) * pageLimitNum;
//		Transaction tx = dbSession.beginTransaction();
//		DishDAO dishdao = new DishDAO(dbSession);
//		String hql = "FROM SeasoningStockData a where a.id.dishId = :seasoningid "
//				+ "AND a.Enable = 1 ";
//		//以名稱搜尋
//		if (!CateringServiceUtil.isEmpty(seasoningName)) {
//			hql += "AND a.ingredientName LIKE :IngredientName ";
//		}
//		//以日期搜尋
//		if (!CateringServiceUtil.isEmpty(searchDate)) {
//			hql += "AND a.useStartDate <= :searchDate ";
//			hql += "AND a.useeEndDate >= :searchDate ";
//		}
//		hql += "ORDER BY a.ingredientName ASC ";
//
//		Query query = dbSession.createQuery(hql);
//		if (!CateringServiceUtil.isEmpty(seasoningName)) {
//			query.setParameter("IngredientName", "%" + seasoningName + "%");
//		}
//		if (!CateringServiceUtil.isEmpty(searchDate) ) {
//			query.setParameter("searchDate", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", searchDate));
//		}
//		//以kitchenId去dish取得該供應商調味料資訊，若無則幫他新增一筆
//		Dish seasoning = dishdao.queryDishByName(dbSession,kitchenId,CateringServiceCode.CODETYPE_SEASONING);
//		if(seasoning == null){
//			seasoning = new Dish();
//			seasoning.setDishName(CateringServiceCode.CODETYPE_SEASONING);
//			seasoning.setKitchenId(kitchenId);
//			seasoning.setPicturePath("");
//			dbSession.save(seasoning);
//			tx.commit();
//		}
//		
//		query.setParameter("seasoningid", seasoning.getDishId());
//		query.setFirstResult(startIndex);
//		query.setMaxResults(pageLimitNum);
//		//System.out.println("查詢結果："+query.list().size());
//		return (List<SeasoningStockData>)query.list();
//	}
//	/*
//	 * 透過SeasoningStockId查詢調味料詳細資訊 add by Ellis 20150109
//	 */
//	public SeasoningStockData querySeasoningBySeasoningId(Long seasoningStockId) {
//		
//		String hql = "FROM SeasoningStockData a WHERE a.id.seasoningStockId = :seasoningId ";
//		Query query = dbSession.createQuery(hql);
//		query.setParameter("seasoningId", seasoningStockId);
//		
//		return (SeasoningStockData)query.list().get(0);
//	}
//	
	/*
	 * 透過SeasoningStockId與MenuDate查詢期間內所使用之調味料 add by Ellis 20150128
	 */
//	public List<SeasoningStockData> querySeasoningByDishIdandDate(Long seasoningStockId,String MenuDate) throws ParseException {
////		MenuDate = MenuDate.replace("/", "-");
//		
////		SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd");
////		Date date = (Date) DateFormat.parse(MenuDate);
//		Timestamp date = CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", MenuDate);
//		
////		String hql = "FROM SeasoningStockData a WHERE a.id.dishId = :seasoningId"
////				+ " AND a.usestartdate <= :menuDate "
////				+ " AND a.useeenddate >= :menuDate "
////				+ " AND a.enable = 1";
//		String hql = "FROM SeasoningStockData a WHERE a.id.dishId = :seasoningId"
//				+ " AND a.useStartDate <= :menuDate "
//				+ " AND a.useeEndDate >= :menuDate "
//				+ " AND a.Enable = 1";
//		Query query = dbSession.createQuery(hql);
//		query.setParameter("seasoningId", seasoningStockId);
//		query.setParameter("menuDate", date);
//			
//		return (List<SeasoningStockData>)query.list();
//	}
//	
//	/**
//	 * 新增調味料資訊 add by ellis 20150112
//	 * @param dishId
//	 * @param ingredientName
//	 * @param stockDate
//	 * @param manufactureDate
//	 * @param expirationDate
//	 * @param lotNumber
//	 * @param supplierId
//	 * @param sourceCertification
//	 * @param certificationId
//	 * @param productName
//	 * @param manufacturer
//	 * @param ingredientQuantity
//	 * @param ingredientUnit
//	 * @param ingredientAttr
//	 * @param usestartdate
//	 * @param useenddate
//	 */
//	public void addSeasoning(Integer kitchenId,Long dishId,String ingredientName,String stockDate,String manufactureDate,
//			String expirationDate,String lotNumber,Integer supplierId,String sourceCertification,String certificationId,
//			Integer menuType,String supplierCompanyId,String productName,String manufacturer,String ingredientQuantity,
//			String ingredientUnit,Integer ingredientAttr,String usestartdate,String useenddate,String username) {
//		//先在ingredient新增一筆食材資料 並取得此id寫入table
//		Transaction tx = dbSession.beginTransaction();
//		Ingredient ig = new Ingredient();
//		DishDAO dishdao = new DishDAO(dbSession);
//		Dish seasoning = dishdao.queryDishByName(dbSession,kitchenId,CateringServiceCode.CODETYPE_SEASONING);
//		ig.setDishId(seasoning.getDishId());
//		ig.setIngredientName(ingredientName);
//		SupplierDAO supplierDAO = new SupplierDAO(dbSession);
//		Supplier supplier = supplierDAO.querySupplierById(supplierId,kitchenId);
//		ig.setSupplierCompanyId(supplier.getCompanyId());
//		ig.setSupplierId(supplierId);
//		ig.setManufacturer(manufacturer);
//		ig.setProductName(productName);
//		ig.setBrand("");
//	//	ig.setBrand(brand);
//		dbSession.save(ig);
//		try {
//			SeasoningStockData seasoningData = new SeasoningStockData();
//			seasoningData.setDishId(seasoning.getDishId());
//			seasoningData.setIngredientId(ig.getIngredientId());
//			seasoningData.setIngredientName(ingredientName);
//			seasoningData.setStockDate(CateringServiceUtil.isEmpty(stockDate) ? null :CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", stockDate));
//			seasoningData.setManufactureDate(CateringServiceUtil.isEmpty(manufactureDate) ? null :CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", manufactureDate));
//			seasoningData.setExpirationDate(CateringServiceUtil.isEmpty(expirationDate) ? null :CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", expirationDate));
//			seasoningData.setLotNumber(lotNumber);
//			seasoningData.setSupplierId(supplierId);
//			seasoningData.setSourceCertification(sourceCertification);
//			seasoningData.setCertificationId(certificationId);
//			seasoningData.setMenuType(menuType);
//			seasoningData.setSupplierCompanyId(supplier.getCompanyId());
//			seasoningData.setSupplierName(supplier.getSupplierName());
//			seasoningData.setProductName(productName);
//			seasoningData.setManufacturer(manufacturer);
//			seasoningData.setIngredientQuantity(ingredientQuantity);
//			seasoningData.setIngredientUnit(ingredientUnit);
//			seasoningData.setIngredientAttr(ingredientAttr);
//			seasoningData.setUseStartDate(CateringServiceUtil.isEmpty(usestartdate) ? null :CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", usestartdate));
//			seasoningData.setUseeEndDate(CateringServiceUtil.isEmpty(useenddate) ? null :CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", useenddate));
//			seasoningData.setLastUpdateId(username);
//			seasoningData.setLastUpdateDate(CateringServiceUtil.getCurrentTimestamp());
//			seasoningData.setEnable(1);
//			dbSession.save(seasoningData);
//			updateSeasoningtoIngredientbatchdata(seasoningData,kitchenId);//更新資訊至ingredientbatchdata
//			tx.commit();
//		} catch (ParseException e) {
//			if(!tx.wasRolledBack()){
//				tx.rollback();
//			}
//		}
//	}
//	/**
//	 * 修改調味料資訊 add by ellis 20150112
//	 * @param kitchenId
//	 * @param seasoningstockId
//	 * @param dishId
//	 * @param ingredientId
//	 * @param ingredientName
//	 * @param stockDate
//	 * @param manufactureDate
//	 * @param expirationDate
//	 * @param lotNumber
//	 * @param supplierId
//	 * @param sourceCertification
//	 * @param certificationId
//	 * @param menuType
//	 * @param supplierCompanyId
//	 * @param productName
//	 * @param manufacturer
//	 * @param ingredientQuantity
//	 * @param ingredientUnit
//	 * @param ingredientAttr
//	 * @param usestartdate
//	 * @param useenddate
//	 * @param username
//	 */
//	public void updateSeasoning(Integer kitchenId,Long seasoningstockId,Long dishId,Long ingredientId,String ingredientName,String stockDate,String manufactureDate,
//			String expirationDate,String lotNumber,Integer supplierId,String sourceCertification,String certificationId,
//			Integer menuType,String supplierCompanyId,String productName,String manufacturer,String ingredientQuantity,
//			String ingredientUnit,Integer ingredientAttr,String usestartdate,String useenddate,String username){
//		Transaction tx = dbSession.beginTransaction();
//		try {
//			SeasoningStockData seasoningData = querySeasoningBySeasoningId(seasoningstockId);
//			seasoningData.setDishId(dishId);
//			seasoningData.setIngredientId(ingredientId);
//			seasoningData.setIngredientName(ingredientName);
//			seasoningData.setStockDate(CateringServiceUtil.isEmpty(stockDate) ? null :CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", stockDate));
//			seasoningData.setManufactureDate(CateringServiceUtil.isEmpty(manufactureDate) ? null :CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", manufactureDate));
//			seasoningData.setExpirationDate(CateringServiceUtil.isEmpty(expirationDate) ? null :CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", expirationDate));
//			seasoningData.setLotNumber(lotNumber);
//			seasoningData.setSupplierId(supplierId);
//			seasoningData.setSourceCertification(sourceCertification);
//			seasoningData.setCertificationId(certificationId);
//			seasoningData.setMenuType(menuType);
//			SupplierDAO supplierDAO = new SupplierDAO(dbSession);
//			Supplier supplier = supplierDAO.querySupplierById(supplierId,kitchenId);
//			seasoningData.setSupplierCompanyId(supplierCompanyId);
//			seasoningData.setSupplierName(supplier.getSupplierName());
//			seasoningData.setProductName(productName);
//			seasoningData.setManufacturer(manufacturer);
//			seasoningData.setIngredientQuantity(ingredientQuantity);
//			seasoningData.setIngredientUnit(ingredientUnit);
//			seasoningData.setIngredientAttr(ingredientAttr);
//			seasoningData.setUseStartDate(CateringServiceUtil.isEmpty(usestartdate) ? null :CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", usestartdate));
//			seasoningData.setUseeEndDate(CateringServiceUtil.isEmpty(useenddate) ? null :CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", useenddate));
//			seasoningData.setLastUpdateId(username);
//			seasoningData.setLastUpdateDate(CateringServiceUtil.getCurrentTimestamp());
//			updateSeasoningtoIngredientbatchdata(seasoningData,kitchenId);//更新資訊至ingredientbatchdata
//			dbSession.update(seasoningData);
//			tx.commit();
//		} catch (ParseException e) {
//			if(!tx.wasRolledBack()){
//				tx.rollback();
//			}
//		}
//	}
//	/**
//	 * 刪除調味料資訊 add by ellis 20150112
//	 * @param seasoningstockId
//	 */
//	public void deleteSeasoning(Long seasoningstockId){
//		Transaction tx = dbSession.beginTransaction();
//		SeasoningStockData seasoningData = new SeasoningStockData();
//		seasoningData = querySeasoningBySeasoningId(seasoningstockId);
//		deleteSeasoningStockDatainIngredientbatchdata(seasoningData);
//		seasoningData.setEnable(0);
//		dbSession.update(seasoningData);
//		tx.commit();
//	}
//	/**
//	 * 調味料日期驗證 add by ellis 20150121
//	 * @param _stockDate
//	 * @param _manufactureDate
//	 * @param _expirationDate
//	 * @param _usestartDate
//	 * @param _useendDate
//	 * @throws Exception 
//	 */
//	public void checkSeasoningDate(String _stockDate,String _manufactureDate,String _expirationDate,String _usestartDate,String _useendDate) throws Exception{
////		_stockDate = _stockDate.replaceAll("-", "/");
////		_manufactureDate = _manufactureDate.replaceAll("-", "/");
////		_expirationDate = _expirationDate.replaceAll("-", "/");
////		_usestartDate = _usestartDate.replaceAll("-", "/");
////		_useendDate = _useendDate.replaceAll("-", "/");
//		
//		Timestamp nowDate = CateringServiceUtil.getCurrentTimestamp(); //當前日期
//		Timestamp stockDate = null;
//		Timestamp manufactureDate = null;
//		Timestamp expirationDate = null;
//		Timestamp usestartDate = null;
//		Timestamp useendDate = null;
//		if(!CateringServiceUtil.isEmpty(_stockDate)){
//			stockDate = CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", _stockDate); //進貨日期
//		}
//		if(!CateringServiceUtil.isEmpty(_manufactureDate)){
//			manufactureDate = CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", _manufactureDate); //製造日期
//		}
//		if(!CateringServiceUtil.isEmpty(_expirationDate)){
//			expirationDate = CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", _expirationDate); //有效日期
//		}
//		if(!CateringServiceUtil.isEmpty(_usestartDate)){
//			usestartDate = CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", _usestartDate); //開始使用日
//		}
//		if(!CateringServiceUtil.isEmpty(_useendDate)){
//			useendDate = CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", _useendDate); //結束使用日
//		}
//		
//		//製造日期不可大於進貨日期
//		if(!CateringServiceUtil.isNull(manufactureDate) && !CateringServiceUtil.isNull(stockDate)){
//			if(manufactureDate.getTime() > stockDate.getTime()){
//				throw new Exception("製造日期: " + _manufactureDate + " 不可大於進貨日期: " + _stockDate);
//			}
//		}
//		
//		//進貨日期不可大於有效日期
//		if(!CateringServiceUtil.isNull(stockDate) && !CateringServiceUtil.isNull(expirationDate)){
//			if(stockDate.getTime() > expirationDate.getTime()){
//				throw new Exception("有效日期: " + _expirationDate + " 不可大於進貨日期: " + _stockDate);
//			}
//		}
//			
//		//生產日期不可大於有效日期
//		if(!CateringServiceUtil.isNull(manufactureDate) && !CateringServiceUtil.isNull(expirationDate)){
//			if(manufactureDate.getTime() > expirationDate.getTime()){
//				throw new Exception("生產日期: " + _manufactureDate + " 不可大於有效日期: " + _expirationDate);
//			}
//		}
//		
//		//結束日不可大於開始日
//		if(!CateringServiceUtil.isNull(usestartDate) && !CateringServiceUtil.isNull(useendDate)){
//			if(usestartDate.getTime() > useendDate.getTime()){
//				throw new Exception("開始使用日期: " + _usestartDate + " 不可大於結束使用日期: " + _useendDate);
//			}
//		}
//		
//		//使用期間不可超過有效日
//		if(!CateringServiceUtil.isNull(usestartDate) && !CateringServiceUtil.isNull(useendDate) && !CateringServiceUtil.isNull(expirationDate)){
//			if(usestartDate.getTime() > expirationDate.getTime() || useendDate.getTime() > expirationDate.getTime()){
//				throw new Exception("使用期間:"+ _usestartDate + "至" +_useendDate +"超出有效日期:" + _expirationDate);	
//			}
//		}
//		
//		//進貨日期不可大於使用開始日
//		if(!CateringServiceUtil.isNull(stockDate) && !CateringServiceUtil.isNull(usestartDate)){
//			if(stockDate.getTime() > usestartDate.getTime()){
//				throw new Exception("進貨日期: " + _manufactureDate + " 不可大於開始使用日期: " + _expirationDate);
//			}
//		}
//	}
//	
//	/**
//	 * 更新調味料資訊至Ingredientbatchdata
//	 * @param seasoningData
//	 * @param kitchenId
//	 * @throws ParseException
//	 */
//	public void updateSeasoningtoIngredientbatchdata(SeasoningStockData seasoningData,Integer kitchenId) throws ParseException{
//		//先找出舊有菜單中，屬於此IngredientId的調味料品項，並刪除之。
//		deleteSeasoningStockDatainIngredientbatchdata(seasoningData);
//		//找出期間內所有的batchdata
//		String startdate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", seasoningData.getUseStartDate());
//		String enddate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", seasoningData.getUseeEndDate());
//		BatchdataDAO bDAO = new BatchdataDAO(dbSession);
//		List<Long> batchdataId = bDAO.queryBatchdataIdListbyPeriod(kitchenId, startdate,enddate);
//		if(!CateringServiceUtil.isNull(batchdataId)){
//		for (int i=0;i<batchdataId.size();i++){
//			Ingredientbatchdata igdb = new Ingredientbatchdata();
//			igdb = tranSeasoningdatastockToIngredientbatchdata(seasoningData);
//			igdb.setBatchDataId(batchdataId.get(i));
//			dbSession.save(igdb);
//			}
//		}
//	}
//	/**
//	 * 刪除在Ingredientbatchdata中的調味料資訊
//	 * @param DishId
//	 * @param IngredientId
//	 */
//	public void deleteSeasoningStockDatainIngredientbatchdata(SeasoningStockData seasoning){
//
//		String hql = "delete from Ingredientbatchdata i where i.ingredientId = :id " ;
//		
//		if(!CateringServiceUtil.isNull(seasoning.getStockDate())){
//			hql += "AND i.stockDate = :stockDate ";
//		}else{
//			hql += "AND i.stockDate is null ";
//		}
//		
//		if(!CateringServiceUtil.isNull(seasoning.getManufactureDate())){
//			hql += "AND i.manufactureDate = :manufactureDate ";
//		}else{
//			hql += "AND i.manufactureDate is null ";
//		}
//		
//		if(!CateringServiceUtil.isNull(seasoning.getExpirationDate())){
//			hql += "AND i.expirationDate = :expirationDate ";
//		}else{
//			hql += "AND i.expirationDate is null ";
//		}
//		
//		if(!CateringServiceUtil.isNull(seasoning.getLotNumber())){
//			hql += "AND i.lotNumber = :lotNumber ";
//		}else{
//			hql += "AND i.lotNumber = '' ";
//		}
//		
//		if(!CateringServiceUtil.isNull(seasoning.getSupplierId())){
//			hql += "AND i.supplierId = :supplierId ";
//		}
//			
//		Query query = dbSession.createQuery(hql);
//		
//		query.setParameter("id", seasoning.getIngredientId());
//		if(!CateringServiceUtil.isNull(seasoning.getStockDate())){
//			query.setParameter("stockDate", seasoning.getStockDate());
//		}
//		if(!CateringServiceUtil.isNull(seasoning.getManufactureDate())){
//			query.setParameter("manufactureDate", seasoning.getManufactureDate());
//		}
//		if(!CateringServiceUtil.isNull(seasoning.getExpirationDate())){
//			query.setParameter("expirationDate", seasoning.getExpirationDate());
//		}
//		if(!CateringServiceUtil.isNull(seasoning.getLotNumber())){
//			query.setParameter("lotNumber", seasoning.getLotNumber());
//		}
//		if(!CateringServiceUtil.isNull(seasoning.getSupplierId())){
//			query.setParameter("supplierId", seasoning.getSupplierId());
//		}
//		query.executeUpdate();
//	}
//	
//	/**
//	 * 刪除在Ingredientbatchdata中的調味料資訊
//	 * @param DishId
//	 * @param IngredientId
//	 */
//	public void deleteSeasoningStockDatainIngredientbatchdatabyBatchdataIdAndIngredientId(Long BatchdataId,SeasoningStockData seasoning){
//
//		String hql = "delete from Ingredientbatchdata i where i.ingredientId = :id " ;
//		
//		hql += "AND i.batchDataId = :batchdata ";
//		
//		if(!CateringServiceUtil.isNull(seasoning.getStockDate())){
//			hql += "AND i.stockDate = :stockDate ";
//		}else{
//			hql += "AND i.stockDate is null ";
//		}
//		
//		if(!CateringServiceUtil.isNull(seasoning.getManufactureDate())){
//			hql += "AND i.manufactureDate = :manufactureDate ";
//		}else{
//			hql += "AND i.manufactureDate is null ";
//		}
//		
//		if(!CateringServiceUtil.isNull(seasoning.getExpirationDate())){
//			hql += "AND i.expirationDate = :expirationDate ";
//		}else{
//			hql += "AND i.expirationDate is null ";
//		}
//		
//		if(!CateringServiceUtil.isNull(seasoning.getLotNumber())){
//			hql += "AND i.lotNumber = :lotNumber ";
//		}else{
//			hql += "AND i.lotNumber = '' ";
//		}
//		
//		if(!CateringServiceUtil.isNull(seasoning.getSupplierId())){
//			hql += "AND i.supplierId = :supplierId ";
//		}
//			
//		Query query = dbSession.createQuery(hql);
//		
//		query.setParameter("id", seasoning.getIngredientId());
//		query.setParameter("batchdata", BatchdataId);
//		if(!CateringServiceUtil.isNull(seasoning.getStockDate())){
//			query.setParameter("stockDate", seasoning.getStockDate());
//		}
//		if(!CateringServiceUtil.isNull(seasoning.getManufactureDate())){
//			query.setParameter("manufactureDate", seasoning.getManufactureDate());
//		}
//		if(!CateringServiceUtil.isNull(seasoning.getExpirationDate())){
//			query.setParameter("expirationDate", seasoning.getExpirationDate());
//		}
//		if(!CateringServiceUtil.isNull(seasoning.getLotNumber())){
//			query.setParameter("lotNumber", seasoning.getLotNumber());
//		}
//		if(!CateringServiceUtil.isNull(seasoning.getSupplierId())){
//			query.setParameter("supplierId", seasoning.getSupplierId());
//		}
//		query.executeUpdate();
//	}
//	/**
//	 * 新版調味料Seasoningdatastock轉換為Ingredientbatchdata
//	 * @param seasoningData
//	 * @return
//	 */
//	public Ingredientbatchdata tranSeasoningdatastockToIngredientbatchdata(SeasoningStockData seasoningData){
//		Ingredientbatchdata igdb = new Ingredientbatchdata();
//		igdb.setDishId(seasoningData.getDishId());
//		igdb.setIngredientId(seasoningData.getIngredientId());
//		igdb.setIngredientName(seasoningData.getIngredientName());
//		igdb.setStockDate(CateringServiceUtil.isNull(seasoningData.getStockDate()) ? null : seasoningData.getStockDate());
//		igdb.setManufactureDate(CateringServiceUtil.isNull(seasoningData.getManufactureDate()) ? null : seasoningData.getManufactureDate());
//		igdb.setExpirationDate(CateringServiceUtil.isNull(seasoningData.getExpirationDate()) ? null : seasoningData.getExpirationDate());
//		igdb.setLotNumber(seasoningData.getLotNumber());
//		igdb.setBrand(seasoningData.getBrand());
//		igdb.setOrigin(seasoningData.getOrigin());
//		igdb.setSource(seasoningData.getSource());
//		igdb.setSupplierId(seasoningData.getSupplierId());
//		igdb.setSourceCertification(seasoningData.getSourceCertification());
//		igdb.setCertificationId(seasoningData.getCertificationId());
//		igdb.setMenuId(seasoningData.getMenuType());
//		igdb.setSupplierCompanyId(seasoningData.getSupplierCompanyId());
//		igdb.setSupplierName(seasoningData.getSupplierName());
//		igdb.setBrandNo(seasoningData.getBrandNo());
//		igdb.setProductName(seasoningData.getProductName());
//		igdb.setManufacturer(seasoningData.getManufacturer());
//		igdb.setIngredientQuantity(seasoningData.getIngredientQuantity());
//		igdb.setIngredientUnit(seasoningData.getIngredientUnit());
//		igdb.setIngredientAttr(seasoningData.getIngredientAttr());
//		return igdb;
//	}
	/**
	 * 用於缺漏資料查詢，
	 * @param date
	 * @return
	 */
	public boolean queryNullSeasoning(String date,Integer kid){
		
		String hql = "FROM SeasoningStockData a WHERE a.kitchenId = :kitchenId"
				+ " AND a.useStartDate <= :menuDate "
				+ " AND a.useeEndDate >= :menuDate "
				+ " AND a.Enable = 1";
		Query query = dbSession.createQuery(hql);
		query.setParameter("kitchenId", kid);
		query.setParameter("menuDate", new Date(date));
		
		if(query.list().size() !=0 ){
			return false;
		}else{
			return true;
		}
	}
}