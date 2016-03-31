package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.AcceptSchoolKitchen;
import org.iii.ideas.catering_service.dao.Kitchen;
import org.iii.ideas.catering_service.dao.KitchenDAO;
import org.iii.ideas.catering_service.rest.bo.AcceptschoolkitchenBO;
import org.iii.ideas.catering_service.util.HibernateUtil;

/*
 * Query acceptSchoolKitchen中待審核的資料 from schoolId
 */
public class QueryAcceptSchoolKitchenWaitApproveBySchool extends
AbstractApiInterface<QueryAcceptSchoolKitchenWaitApproveBySchoolRequest, QueryAcceptSchoolKitchenWaitApproveBySchoolResponse> {

	@Override
	public void process() throws NamingException, ParseException {
		Session dbSession = HibernateUtil.buildSessionFactory().openSession();
		Query companyIdQuery = dbSession.createQuery("select companyId from Kitchen where kitchenId=:kid ");
		companyIdQuery.setParameter("kid", this.requestObj.getKitchenId());
		String companyId = companyIdQuery.uniqueResult().toString();
		companyId = companyId.substring(1); // remove first char of column named companyId of table kitchen 
		Query schoolIdQuery = dbSession.createQuery("select schoolId from School where schoolCode=:sCode");
		schoolIdQuery.setParameter("sCode", companyId);
		String schoolId = "" + schoolIdQuery.uniqueResult();

		List<AcceptschoolkitchenBO> acceptschoolkitchenBOList = null;
		Criteria criteriaASK = this.dbSession.createCriteria(AcceptSchoolKitchen.class);
		criteriaASK.add(Restrictions.eq("schoolId", Integer.parseInt(schoolId)));
		criteriaASK.add(Restrictions.eq("status", "0"));
		List<AcceptSchoolKitchen> notapprovemealapplyList = criteriaASK.list();
		KitchenDAO kitchenDao = new KitchenDAO(dbSession);
		
		if (notapprovemealapplyList != null && notapprovemealapplyList.size() > 0) {
			acceptschoolkitchenBOList = new ArrayList<AcceptschoolkitchenBO>(notapprovemealapplyList.size());
			for (int i = 0; i < notapprovemealapplyList.size(); i++) {
				AcceptSchoolKitchen record = notapprovemealapplyList.get(i);
				AcceptschoolkitchenBO acceptschoolkitchenBO = new AcceptschoolkitchenBO();
				Kitchen kitchen= kitchenDao.findById(record.getKitchenId());
				acceptschoolkitchenBO.setId(record.getId());
				acceptschoolkitchenBO.setKitchenName(kitchen.getKitchenName());
				acceptschoolkitchenBO.setQuantity(record.getQuantity());
				acceptschoolkitchenBO.setKitchenOwnner(kitchen.getOwnner());
				acceptschoolkitchenBO.setKitchenAddress(kitchen.getAddress());
				acceptschoolkitchenBO.setKitchenTel(kitchen.getTel());
				acceptschoolkitchenBOList.add(i, acceptschoolkitchenBO);
			}
		} else {
			acceptschoolkitchenBOList = new ArrayList<AcceptschoolkitchenBO>(0);
		}
		this.responseObj.setAcceptschoolkitchenwaitapprovebyschooList(acceptschoolkitchenBOList);

		this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);
		
	}
}
