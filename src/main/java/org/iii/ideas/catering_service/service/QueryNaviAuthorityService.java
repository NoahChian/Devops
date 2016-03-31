package org.iii.ideas.catering_service.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.iii.ideas.catering_service.dao.Navi;
import org.iii.ideas.catering_service.dao.NaviDAO;
import org.iii.ideas.catering_service.dao.Role;
import org.iii.ideas.catering_service.dao.RoleDAO;
import org.iii.ideas.catering_service.dao.RoleNaviDAO;
import org.iii.ideas.catering_service.dao.Userrole;
import org.iii.ideas.catering_service.dao.UserroleDAO;
import org.iii.ideas.catering_service.util.CateringServiceUtil;

public class QueryNaviAuthorityService extends BaseService{
	protected Logger log = Logger.getLogger(this.getClass());
	
	public QueryNaviAuthorityService(){};
	
	public QueryNaviAuthorityService(Session dbSession){
		setDbSession(dbSession);
	};
	
	@SuppressWarnings("unchecked")
	public List<Navi> queryNaviAuth(String userName){
		UserroleDAO userRoleDao = new UserroleDAO(dbSession);
		RoleDAO roleDao = new RoleDAO(dbSession);
		RoleNaviDAO roleNaviDao = new RoleNaviDAO(dbSession);
		NaviDAO naviDao = new NaviDAO(dbSession);
		List<Navi> naviList = null;			// 原始權限資料
		List<Navi> naviParents = null;		// 存放父功能名稱
		List<Navi> naviChildren = null;		// 存放子功能
		List<Navi> naviListOrdered = null;	// 重新排序後的功能清單	
		
		// 依username查詢對應的roleType
		List<Userrole> userRoleList = userRoleDao.findByProperty("username", userName);
		
		// 依roleTypet查詢對應的roleId <目前一個username只對應到一個roleType 故取第一筆即可>
		String roleType =  userRoleList.get(0).getRoletype();
		List<Role> roleList = roleDao.findByProperty("roletype", roleType);
		
		// 依roleId查詢對應的naviId
		List<Long> roleIdList = new ArrayList<Long>();
		for(int i = 0; i < roleList.size(); i++){
			// AbstractRole.java 裡的roleId屬性為Integer 需討論修改   暫時用Long.valueOf()轉型
			roleIdList.add(Long.valueOf(roleList.get(i).getRoleId()));
		}
		List<Long> naviIdList = roleNaviDao.findByProperty("roleId", roleIdList);

		// 依naviId查詢擁有的menuTree權限
		if(naviIdList.size() > 0){
			naviList = new ArrayList<Navi>();
			naviList = naviDao.findByProperty("naviId", naviIdList);
		}
		
		// 將功能歸類排序
		if(naviList.size() > 0){
			naviParents = new ArrayList<Navi>();
			naviChildren = new ArrayList<Navi>();
			naviListOrdered = new ArrayList<Navi>();
			for(int i = 0; i < naviList.size(); i++){
				if(naviList.get(i).getParents() == 0){
					naviParents.add(naviList.get(i));
				}else{
					naviChildren.add(naviList.get(i));
				}
			}
			for(int j = 0; j < naviParents.size(); j++){
				naviListOrdered.add(naviParents.get(j));
				for(int k = 0; k < naviChildren.size(); k++){
					if(naviParents.get(j).getNaviId() == naviChildren.get(k).getParents()){
						naviListOrdered.add(naviChildren.get(k));
					}
				}
			}
		}
		
		return !CateringServiceUtil.isNull(naviList) ? naviListOrdered : null ;
	}

}
