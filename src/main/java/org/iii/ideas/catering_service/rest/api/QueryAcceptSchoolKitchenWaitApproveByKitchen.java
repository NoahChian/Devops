package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.AcceptSchoolKitchen;
import org.iii.ideas.catering_service.dao.School;
import org.iii.ideas.catering_service.dao.SchoolDAO;
import org.iii.ideas.catering_service.rest.bo.AcceptschoolkitchenBO;
import org.iii.ideas.catering_service.util.HibernateUtil;

/*
 * Query acceptSchoolKitchen中待審核的資料 from kitchenId
 */
public class QueryAcceptSchoolKitchenWaitApproveByKitchen extends
		AbstractApiInterface<QueryAcceptSchoolKitchenWaitApproveByKitchenRequest, QueryAcceptSchoolKitchenWaitApproveByKitchenResponse> {
	private static final long serialVersionUID = -9115574829368651090L;

	@Override
	public void process() throws NamingException, ParseException {
		List<AcceptschoolkitchenBO> askList = null;
		Criteria criteriaASK = this.dbSession.createCriteria(AcceptSchoolKitchen.class);
		criteriaASK.add(Restrictions.eq("kitchenId", this.requestObj.getKitchenId()));
		criteriaASK.add(Restrictions.eq("status", "0"));
		List<AcceptSchoolKitchen> notapprovemealapplyList = criteriaASK.list();
		SchoolDAO schoolDao = new SchoolDAO(HibernateUtil.buildSessionFactory().openSession());

		if (notapprovemealapplyList != null && notapprovemealapplyList.size() > 0) {
			askList = new ArrayList<AcceptschoolkitchenBO>(notapprovemealapplyList.size());
			for (int i = 0; i < notapprovemealapplyList.size(); i++) {
				AcceptSchoolKitchen record = notapprovemealapplyList.get(i);
				AcceptschoolkitchenBO acceptschoolkitchenBO = new AcceptschoolkitchenBO();
				acceptschoolkitchenBO.setId(record.getId());
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
				acceptschoolkitchenBO.setAction(record.getAction());
				acceptschoolkitchenBO.setCreateUser(record.getCreateUser());
				SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
				acceptschoolkitchenBO.setCreateDateTime(df.format(record.getCreateDateTime()));
				acceptschoolkitchenBO.setModifyUser(record.getModifyUser());
				acceptschoolkitchenBO.setModifyDateTime(df.format(record.getModifyDateTime()));
				askList.add(i, acceptschoolkitchenBO);
			}
		} else{
			askList = new ArrayList<AcceptschoolkitchenBO>(0);
		}
		this.responseObj.setAskList(askList);

		this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);
	}

}
