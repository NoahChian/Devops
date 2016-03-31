package org.iii.ideas.catering_service.rest.api;

import javax.naming.NamingException;

import org.hibernate.Transaction;
//import org.iii.ideas.catering_service.dao.BatchdataDAO;
//import org.iii.ideas.catering_service.dao.Kitchen;
//import org.iii.ideas.catering_service.dao.KitchenDAO;
import org.iii.ideas.catering_service.dao.School;
import org.iii.ideas.catering_service.dao.SchoolDAO;
//import org.iii.ideas.catering_service.dao.UseraccountDAO;
//import org.iii.ideas.catering_service.dao.Userrole;
//import org.iii.ideas.catering_service.util.AuthenUtil;
//import org.iii.ideas.catering_service.util.CateringServiceCode;

/**
 * 
 * @author Raymond 2014/05/09
 * @version 0.2
 * 此API會依照學校目前的狀態,決定要將學校/廚房/權限等資料做 啟用/停用
 * 如學校狀態為啟用,則會將學校/廚房 狀態改為停用
 * 如學校狀態為停用,則會將學校/廚房 狀態改為啟用
 * 
 */
public class DeleteSchKitchenUser extends AbstractApiInterface<DeleteSchKitchenUserRequest, DeleteSchKitchenUserResponse> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4038816461109320369L;

	@Override
	public void process() throws NamingException {

		Integer schoolId = null;
//		String username = null;
//		String schoolCode = null;

		// 檢核登入
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}

		// 檢核空值
		if (requestObj.getSchoolId() == null) {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("資料傳遞有誤");
			return;
		}

		schoolId = requestObj.getSchoolId();

		// 檢查學校是否存在
		SchoolDAO schoolDao = new SchoolDAO(this.dbSession);
		School school = schoolDao.querySchoolById(schoolId);
		if (school == null) {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("學校不存在");
			return;
		}
// #13522 學校管理介面變更,只要變更學校狀態,kitchen與role改由讓帳號管理功能處理
//		schoolCode = school.getSchoolCode();
//		String countyCode = AuthenUtil.getCountyTypeByCountryNum(school.getCountyId());
//		
//		username = countyCode.concat(schoolCode);
//		UseraccountDAO userDao = new UseraccountDAO(dbSession);
//		Userrole role = userDao.queryUserroleByUsername(username);
//
//		
//		KitchenDAO kitchenDao = new KitchenDAO(dbSession);
//
//		Kitchen kitchen = kitchenDao.queryKitchenByCompanyId(username);//.queryKitchenByCompanyIdAndKitchentype(, "006");
		
		//用合作社再查一次
//		if(kitchen == null){
//			kitchen = kitchenDao.queryKitchenByCompanyIdAndKitchentype(username+"-SHOP", "009");
//		}
//		if (kitchen == null) {
//			if(CateringServiceCode.USERTYPE_SCHOOL_SHOP.equals(kitchen.getKitchenType())){
//				this.responseObj.setResStatus(0);
//				this.responseObj.setMsg("學校廚房不存在");
//				return;
//			}
//		}
//		
//		BatchdataDAO batchdataDao = new BatchdataDAO(dbSession);
//		//判斷學校廚房有無菜單
//		if (batchdataDao.queryBatchdataCount(kitchen.getKitchenId()) > 0) {
//			this.responseObj.setResStatus(0);
//			this.responseObj.setMsg("尚有菜單紀錄");
//			return;
//		}

		
		Transaction tx = dbSession.beginTransaction();
		
		//會依照目前學校狀況 去做狀態改變
		if(school.getEnable() == 1){
			//目前式啟用狀態 則會將學校/廚房停用
			//將學校enable = 0
			schoolDao.updateSchoolStatus(school, 0);
			//將廚房enable = 0
//			kitchenDao.updateSchoolStatus(kitchen, 0);
			//處理role
//			if(role!=null){
//				dbSession.delete(role);
//			}
		}else{
			//目前式停用狀態 則會將學校/廚房起用
			//將學校enable = 1
			schoolDao.updateSchoolStatus(school, 1);
			//將廚房enable = 1
//			kitchenDao.updateSchoolStatus(kitchen, 1);
			//處理role
//			if (role == null) {
//				role = new Userrole();
//				role.setRoletype("kSch");
//				role.setUsername(username);
//				dbSession.saveOrUpdate(role);
//			}
		}
		tx.commit();
		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");
	}

}
