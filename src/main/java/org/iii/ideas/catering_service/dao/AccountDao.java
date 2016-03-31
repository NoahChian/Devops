package org.iii.ideas.catering_service.dao;

import java.util.Date;

import org.hibernate.Query;
import org.hibernate.Session;
import org.iii.ideas.catering_service.rest.bo.SchKitchenUserBO;
import org.iii.ideas.catering_service.rest.bo.SchKitchenUserContentBO;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
/**
 * 帳號管理DAO
 * @author Raymond 2014/05/06
 * 
 */
public class AccountDao {
	private Session dbSession;

	public AccountDao() {
	}

	public AccountDao(Session dbSession) {
		this.dbSession = dbSession;
	}

	/**
	 * 2014/05/04 Raymond
	 * 查詢學校user 詳細資料
	 * @param schoolCode
	 * @param userType
	 * @return SchKitchenUserBO
	 */
	public SchKitchenUserBO querySchKitchenUserDetail(Integer schoolId) {
		
		SchKitchenUserBO skuBo = new SchKitchenUserBO();
		School school = new School();
		Useraccount user = new Useraccount();
		Userrole role = new Userrole();
		String schoolCode = "";

		SchoolDAO schoolDAO = new SchoolDAO(dbSession);
		school = schoolDAO.querySchoolById(schoolId);

		//目前school code 為空的話就是沒有學校帳號縣接端直接回空值
		if(school==null)
			return null;
		
		// 取縣市對應代碼
		CodeDAO codeDao = new CodeDAO(dbSession);
		String countyCode = codeDao.getCodeByTypeAndName(String.valueOf(school.getCountyId()), "county");
		String countyName = codeDao.getCodeMsgByStatusCode(countyCode, "city");
		
		AreaDAO areaDao = new AreaDAO(dbSession);
		String areaName = areaDao.getAreaNameById(school.getAreaId());
		String countyAreaName = countyName == null || areaName == null ? "" : countyName.concat(areaName);
		
		if(school.getSchoolCode()==null)
			return null;
		
		// 取得account(縣市代碼+學校代碼)
		schoolCode = school.getSchoolCode();
		String username = countyCode == null ? school.getSchoolCode() : countyCode.concat(school.getSchoolCode());

		UseraccountDAO userDao = new UseraccountDAO(dbSession);
		user = userDao.queryUseraccountByUsername(username);
		
		//如果查不到,就先假設他是員生消費合作社再查一次
		if(user == null){
			username += "-SHOP";
			user = userDao.queryUseraccountByUsername(username);
		}
		role = userDao.queryUserroleByUsername(username);
		
		String sortSchollName = "";
		
		//比對縣市名稱是否正確並取出原始學校名稱(不包含縣市名稱)
		if(school.getSchoolName().length() >= countyAreaName.length()){
			if(school.getSchoolName().substring(0, countyAreaName.length()).equals(countyAreaName)){
				sortSchollName = school.getSchoolName().replace(countyAreaName, "");
			} else {
				sortSchollName = school.getSchoolName();
			}
		} else {
			sortSchollName = school.getSchoolName();
		}
		
//		if(school !=null && user !=null){
		if(school !=null ){
			skuBo.setAreaId(school.getAreaId());
			skuBo.setCountyId(school.getCountyId());
			skuBo.setSchoolName(sortSchollName);
			skuBo.setSchoolCode(schoolCode);
			skuBo.setSchoolId(school.getSchoolId());
			skuBo.setEnable(school.getEnable());
			SchKitchenUserContentBO skuContnetBo = new SchKitchenUserContentBO();
			if(user != null) {
				if(user.getEmail()!=null){
					skuContnetBo.setEmail(user.getEmail());
				} else {
					skuContnetBo.setEmail("");
				}
			} else {
				skuContnetBo.setEmail("");
			}
			//role為null時 為學校停用
			if(role != null)
				skuContnetBo.setRole(role.getRoletype());
			
			if(user != null){
				skuContnetBo.setType(user.getUsertype());
			} else {
				//預設006
				skuContnetBo.setType(CateringServiceCode.USERTYPE_SCHOOL);
			}
			skuBo.setContents(skuContnetBo);
			return skuBo;
		}else{
			return null;
		}
	}

	/**
	 * 2014/05/04 Raymond
	 * 新增/修改 學校廚房帳號
	 * @param scBo
	 * @param actType
	 * @return SchKitchenUserBO
	 * @throws Exception 
	 */
	public SchKitchenUserBO updateSchoolKitchenAccount(SchKitchenUserBO scBo, String actType) throws Exception {
		// 依照學校>廚房>學校廚房>帳號>帳號權限 順序新增
		try {
			// 學校
			School school = null;
			Kitchen kitchen = null;
			Schoolkitchen schoolKitchen = null;
			Useraccount user = null;
			Userrole role = null;
			
			boolean addAccount = scBo.getAddAccount();
			
			String userType = scBo.getContents().getType();

			if (actType.equals("update")) {
				SchoolDAO schoolDAO = new SchoolDAO(dbSession);
				school = schoolDAO.querySchoolBySchoolCode(scBo.getSchoolCode());
				if(school ==null)
					new Exception("查無此學校");
				
				if(addAccount){
					KitchenDAO kitDao = new KitchenDAO(dbSession);
					
//					String userType = scBo.getContents().getType();
					
					kitchen = kitDao.queryKitchenByCompanyId(scBo.getUsername());
//					kitchen = kitDao.queryKitchenByCompanyIdAndKitchentype(scBo.getUsername(),scBo.getContents().getType());
					
					if(kitchen ==null)
						new Exception("查無此廚房");

					if (school != null && kitchen != null) {
						SchoolkitchenDAO skDao = new SchoolkitchenDAO(dbSession);
						schoolKitchen = skDao.querySchoolkitchenById(school.getSchoolId(), kitchen.getKitchenId());
					}

					UseraccountDAO userDao = new UseraccountDAO(dbSession);
					user = userDao.queryUseraccountByUsername(scBo.getUsername());
					role = userDao.queryUserroleByUsername(scBo.getUsername());

					if(user == null){
						user = userDao.queryUseraccountByUsername(scBo.getUsername()+"-SHOP");
						role = userDao.queryUserroleByUsername(scBo.getUsername()+"-SHOP");
					}
					if(kitchen ==null)
						new Exception("查無學校使用者");

				}
			}
			// 檢查學校是否存在
			SchoolDAO schoolDao = new SchoolDAO(this.dbSession);

			//增加判斷，若為合作社帳號，學校可直接取出更新。 add by Ellis 20151106
			
			school = schoolDao.querySchoolBySchoolCode(scBo.getSchoolCode());

			if (school == null) {
				school = schoolDao.querySchoolBySchoolName(scBo.getSchoolName());
			}
			
			if (school == null) {
				school = new School();
				//school.setSchoolId(0);
				school.setAreaId(scBo.getAreaId());
				school.setCountyId(scBo.getCountyId());
				school.setEnable(scBo.getEnable());
				school.setSchoolCode(scBo.getSchoolCode());
				school.setSchoolName(scBo.getSchoolName());
				school.setSchoolType(CateringServiceUtil.getSchoolTypeBySchoolName(scBo.getSchoolName()));
				dbSession.saveOrUpdate(school);
			} 
			else {
				school.setAreaId(scBo.getAreaId());
				school.setCountyId(scBo.getCountyId());
				school.setEnable(scBo.getEnable());
				school.setSchoolName(scBo.getSchoolName());
				dbSession.saveOrUpdate(school);
			}
			
			if(addAccount){
				//增加判斷，若為合作社帳號，廚房可直接取出更新。 add by Ellis 20151106
				KitchenDAO kitchenDao = new KitchenDAO(this.dbSession);
				
				String kitchenName = scBo.getSchoolName();
				if(CateringServiceCode.USERTYPE_SCHOOL_SHOP.equals(scBo.getContents().getType())){
					kitchenName = scBo.getSchoolName() +"-員生消費合作社";
				}
								
//				if(!CateringServiceCode.USERTYPE_SCHOOL_SHOP.equals(scBo.getContents().getType())){
//					kitchen = kitchenDao.queryKitchenByCompanyId(scBo.getUsername());
//					if(kitchen != null){
//						
//
//						// 006,007 不可同時存在,找到009則允許新增
//						if(!userType.equals(CateringServiceCode.USERTYPE_SCHOOL_SHOP)
//								&& CateringServiceCode.USERTYPE_SCHOOL_SHOP.equals(kitchen.getKitchenType())){
//							kitchen = null;
//						}
//					}
//				} else {
//					kitchen = kitchenDao.queryKitchenByCompanyIdAndKitchentype(scBo.getUsername(),scBo.getContents().getType());
//				}
				
				kitchen = kitchenDao.queryKitchenByCompanyId(scBo.getUsername());
				
				// 廚房
				if (kitchen == null) {
					kitchen = new Kitchen();
					//kitchen.setKitchenId(0);
					kitchen.setCompanyId(scBo.getUsername());
					kitchen.setAddress(scBo.getCountyName().concat(scBo.getAreaName()));
					kitchen.setKitchenType(scBo.getContents().getType());
					kitchen.setKitchenName(kitchenName);//scBo.getSchoolName();

					if(scBo.getContents() !=null && scBo.getContents().getEmail() !=null)
						kitchen.setEmail(scBo.getContents().getEmail());
					else
						kitchen.setEmail("");
					kitchen.setChef("");
					kitchen.setFax("");
					kitchen.setTel("");
					kitchen.setHaccp("");
					kitchen.setInsurement("");
					kitchen.setNutritionist("");
					kitchen.setOwnner("");
					kitchen.setQualifier("");
					kitchen.setPicturePath("");
					kitchen.setEnable(scBo.getEnable());
					kitchen.setCreateDate(CateringServiceUtil.getCurrentTimestamp());
					kitchen.setManager("");
					kitchen.setManageremail("");
					dbSession.saveOrUpdate(kitchen);
				}
				

				// 學校廚房
				if(!CateringServiceCode.USERTYPE_SCHOOL_SHOP.equals(scBo.getContents().getType())){
					if (schoolKitchen == null) {
						schoolKitchen = new Schoolkitchen();
						SchoolkitchenId skid = new SchoolkitchenId();
						schoolKitchen.setId(skid);
						schoolKitchen.setCreateDate(CateringServiceUtil.getCurrentTimestamp());
						schoolKitchen.setQuantity(1);
					}
					schoolKitchen.getId().setKitchenId(kitchen.getKitchenId());
					schoolKitchen.getId().setSchoolId(school.getSchoolId());
					dbSession.saveOrUpdate(schoolKitchen);
				}

				// 若帳號已存在，則
				if (user == null) {
					user = new Useraccount();
				}
				if (scBo.getPassword() != null) {
					user.setPassword(scBo.getPassword());
				}
				user.setEmail(scBo.getContents().getEmail());
				user.setKitchenId(kitchen.getKitchenId());
				
				if(CateringServiceCode.USERTYPE_SCHOOL_SHOP.equals(scBo.getContents().getType())){
					user.setUsername(scBo.getUsername()+"-SHOP");
					user.setName(school.getSchoolName()+"-員生消費合作社");
				}else{
					user.setUsername(scBo.getUsername());
					user.setName(school.getSchoolName());
				}
				user.setUsertype(scBo.getContents().getType());
				user.setEnable(1);
				dbSession.saveOrUpdate(user);
				/*
				 *  權限
				 *  如果是停用帳號(enable=0),則會去判斷有無role資料,有的話就去刪除,沒有的話就不理會
				 *  如果是啟用帳號(enable=1),會判斷有無role資料,新增或是修改role資了
				 */
				if(scBo.getEnable()==0 && role != null){
					dbSession.delete(role);
				}else if(scBo.getEnable()==1){
					if (role == null) {
						role = new Userrole();
					}
					role.setRoletype(scBo.getContents().getRole());
					role.setUsername(user.getUsername());
					dbSession.saveOrUpdate(role);
				}
				
				//Bug #13231 學校管理 新增高中職學校需增加美食街權限
//				INSERT INTO `cateringservice`.`userrelation` (`parent`, `parentType`, `child`, 
//				`authorizeType`, `authorizeUserType`, `authorizeTarget`, `authorizeTargetType`, 
//				`can_create_account`, `can_delete_account`, `can_edit_account`, `can_create_restaurant`, 
//				`can_delete_restaurant`, `can_edit_restaurant`, `can_upload_menu`, `status`, `createUser`) 
//				VALUES ('0', '0', '帳號Username', '1', '101', '學校ID', '101', '7', '7', '7', '7', '7', '7', 
//				'1', '1', '登入帳號');
				boolean isAddUserrelation = false;
				if (CateringServiceCode.USERTYPE_SCHOOL.equals(scBo.getContents().getType()) && scBo.getSchoolCode() != null) {
					if (scBo.getSchoolCode().length() == 6) {
						String code4 = scBo.getSchoolCode().substring(3, 4);
						if ("3".equals(code4) || "4".equals(code4)) {
							// 高中
							isAddUserrelation = true;
						}
					}
				}
				if (scBo.getSchoolCode().length() == 4 && scBo.getSchoolCode() != null) {
					// 大專院校
					isAddUserrelation = true;
				}
				if (isAddUserrelation) {
					Userrelation ul = new Userrelation();
					ul.setParent("0");
					ul.setParentType("0");
					ul.setChild(scBo.getUsername());
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
					ul.setCreateUser(scBo.getCreateUser());
					ul.setCreateDate(new Date());
					dbSession.saveOrUpdate(ul);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return scBo;
	}

	/**
	 * 2014/05/04 Raymond
	 * 查詢user by password
	 * @param username
	 * @param password
	 * @return Useraccount
	 */
	public Useraccount queryUserByPassword(String username, String password) {
		String hql = "FROM Useraccount ua WHERE ua.username = :username AND ua.password = :password";
		Query query = dbSession.createQuery(hql);
		query.setParameter("username", username);
		query.setParameter("password", password);
		if (query.list() != null && query.list().size() > 0) {
			return (Useraccount) query.list().get(0);
		} else {
			return null;
		}

	}

}
