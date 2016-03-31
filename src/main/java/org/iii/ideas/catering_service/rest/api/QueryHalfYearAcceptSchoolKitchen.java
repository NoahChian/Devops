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
 * Query acceptSchoolKitchen中半年內的資料
 */
public class QueryHalfYearAcceptSchoolKitchen extends
		AbstractApiInterface<QueryHalfYearAcceptSchoolKitchenRequest, QueryHalfYearAcceptSchoolKitchenResponse> {
	private static final long serialVersionUID = -9115574829368651090L;

	@Override
	public void process() throws NamingException, ParseException {
		List<AcceptschoolkitchenBO> acceptschoolkitchenboList = null;
		Timestamp halfYear = new Timestamp(System.currentTimeMillis() - (long) (182.5 * 24 * 60 * 60 * 1000));
		Criteria criteriaASK = this.dbSession.createCriteria(AcceptSchoolKitchen.class);
		criteriaASK.add(Restrictions.eq("kitchenId", this.requestObj.getKitchenId()));
		criteriaASK.add(Restrictions.ge("createDateTime", halfYear));
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
		} else{
			acceptschoolkitchenboList = new ArrayList<AcceptschoolkitchenBO>(0);
		}
		this.responseObj.setHalfyearacceptschoolkitchenList(acceptschoolkitchenboList);

		this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);
	}

}
