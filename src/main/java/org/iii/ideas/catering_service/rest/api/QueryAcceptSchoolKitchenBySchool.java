package org.iii.ideas.catering_service.rest.api;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.AcceptSchoolKitchen;
import org.iii.ideas.catering_service.dao.School;
import org.iii.ideas.catering_service.dao.SchoolDAO;
import org.iii.ideas.catering_service.rest.bo.AcceptschoolkitchenBO;
import org.iii.ideas.catering_service.util.HibernateUtil;

/*
 * 依查詢條件查詢acceptSchoolKitchen中的資料
 */
public class QueryAcceptSchoolKitchenBySchool extends
		AbstractApiInterface<QueryAcceptSchoolKitchenBySchoolRequest, QueryAcceptSchoolKitchenBySchoolResponse> {
	private static final long serialVersionUID = -9115574829368651090L;

	@Override
	public void process() throws NamingException, ParseException {
		Integer schoolId = this.requestObj.getSchoolId();
		String status = this.requestObj.getApproveStatus();
		String _startDate = this.requestObj.getStartDate() + " 00:00:00";
		String _endDate = this.requestObj.getEndDate() + " 23:59:59";		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		List<AcceptschoolkitchenBO> acceptschoolkitchenboList = processQuery(schoolId, status, _startDate, _endDate, sdf);
		this.responseObj.setAcceptschoolkitchenList(acceptschoolkitchenboList);
		this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);
	}

	/*
	 * 依查詢條件查詢acceptSchoolKitchen中的資料,並開放給匯出功能使用
	 */
	public List<AcceptschoolkitchenBO> processQuery(Integer schoolId, String status, String _startDate, String _endDate, SimpleDateFormat sdf) throws ParseException{
		List<AcceptschoolkitchenBO> acceptschoolkitchenboList = null;
		Timestamp startDate = new Timestamp(sdf.parse(_startDate).getTime()); 
		Timestamp endDate = new Timestamp(sdf.parse(_endDate).getTime());
		
		Criteria criteriaASK = HibernateUtil.buildSessionFactory().openSession().createCriteria(AcceptSchoolKitchen.class);
		criteriaASK.add(Restrictions.eq("schoolId", schoolId)); // 學校代碼
		if("all".equals(status)){ // 狀態(0:待審核, 1審核通過, 2:否決)
		} else if("waitApprove".equals(status)){
			criteriaASK.add(Restrictions.eq("status", "0"));
		} else if("approved".equals(status)){
			criteriaASK.add(Restrictions.eq("status", "1"));
		} else if("rejected".equals(status)){
			criteriaASK.add(Restrictions.eq("status", "2"));
		}
		criteriaASK.add(Restrictions.ge("createDateTime", startDate)); // 開始日期
		criteriaASK.add(Restrictions.le("createDateTime", endDate)); // 結束日期
		criteriaASK.addOrder(Order.desc("createDateTime"));
		List<AcceptSchoolKitchen> acceptschoolkitchenList = criteriaASK.list();
		SchoolDAO schoolDao = new SchoolDAO(HibernateUtil.buildSessionFactory().openSession());
		if (acceptschoolkitchenList != null && acceptschoolkitchenList.size() > 0) {
			acceptschoolkitchenboList = new ArrayList<AcceptschoolkitchenBO>(acceptschoolkitchenList.size());
			for (int i = 0; i < acceptschoolkitchenList.size(); i++) {
				AcceptSchoolKitchen record = acceptschoolkitchenList.get(i);
				AcceptschoolkitchenBO acceptschoolkitchenBO = new AcceptschoolkitchenBO();
				acceptschoolkitchenBO.setId(new Integer(i + 1));
				acceptschoolkitchenBO.setSchoolId(record.getSchoolId());
				School school = schoolDao.findById(record.getSchoolId());
				acceptschoolkitchenBO.setSchoolCode(school.getSchoolCode());
				acceptschoolkitchenBO.setSchoolName(school.getSchoolName());
				acceptschoolkitchenBO.setKitchenId(record.getKitchenId());
				acceptschoolkitchenBO.setQuantity(record.getQuantity());
				if ("0".equals(record.getStatus())) {
					acceptschoolkitchenBO.setStatus("審核中");
				} else if ("1".equals(record.getStatus())) {
					acceptschoolkitchenBO.setStatus("審核通過");
				} else if ("2".equals(record.getStatus())) {
					acceptschoolkitchenBO.setStatus("否決");
				}
				if ("1".equals(record.getAction())) {
					acceptschoolkitchenBO.setAction("申請供餐");
				} else if ("2".equals(record.getAction())) {
					acceptschoolkitchenBO.setAction("取消供餐");
				} else if ("3".equals(record.getAction())) {
					acceptschoolkitchenBO.setAction("商品上架");
				}
				acceptschoolkitchenBO.setCreateUser(record.getCreateUser());
				SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
				acceptschoolkitchenBO.setCreateDateTime(df.format(record.getCreateDateTime()));
				acceptschoolkitchenBO.setModifyUser(record.getModifyUser());
				acceptschoolkitchenBO.setModifyDateTime(df.format(record.getModifyDateTime()));
				acceptschoolkitchenboList.add(i, acceptschoolkitchenBO);
			}
		} else {
			acceptschoolkitchenboList = new ArrayList<AcceptschoolkitchenBO>(0);
		}
		return acceptschoolkitchenboList;
	}
}
