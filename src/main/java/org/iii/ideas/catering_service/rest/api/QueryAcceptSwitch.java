package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.AcceptSwitch;
import org.iii.ideas.catering_service.dao.School;
import org.iii.ideas.catering_service.dao.SchoolDAO;
import org.iii.ideas.catering_service.rest.bo.AcceptSwitchBO;
import org.iii.ideas.catering_service.util.HibernateUtil;

/*
 * Query acceptSwitch的資料 by schoolId and acceptType
 */
public class QueryAcceptSwitch extends AbstractApiInterface<QueryAcceptSwitchRequest, QueryAcceptSwitchResponse> {
	private static final long serialVersionUID = -9115574829368651090L;

	@Override
	public void process() throws NamingException, ParseException {
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		
		List<AcceptSwitchBO> acceptswitchboList = null;
		Criteria criteriaASK = this.dbSession.createCriteria(AcceptSwitch.class);
		criteriaASK.add(Restrictions.eq("schoolId", this.requestObj.getSchoolId()));
		criteriaASK.add(Restrictions.eq("acceptType", this.requestObj.getAcceptType()));
		List<AcceptSwitch> acceptswitchList = criteriaASK.list();
		SchoolDAO schoolDao = new SchoolDAO(HibernateUtil.buildSessionFactory().openSession());
		if (acceptswitchList != null && acceptswitchList.size() > 0) {
			acceptswitchboList = new ArrayList<AcceptSwitchBO>(acceptswitchList.size());
			SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			for (int i = 0; i < acceptswitchList.size(); i++) {
				AcceptSwitch record = acceptswitchList.get(i);
				AcceptSwitchBO acceptswitchBO = new AcceptSwitchBO();
				acceptswitchBO.setId(record.getId());
				acceptswitchBO.setSchoolId(record.getSchoolId());
				School school = schoolDao.findById(record.getSchoolId());
				acceptswitchBO.setSchoolCode(school.getSchoolCode());
				acceptswitchBO.setAcceptType(record.getAcceptType());
				acceptswitchBO.setStatus("" + record.getStatus());
				if (record.getCreateUser() != null && record.getCreateUser().length() > 0) {
					acceptswitchBO.setCreateUser(record.getCreateUser());
				} else {
					acceptswitchBO.setCreateUser("");
				}
				acceptswitchBO.setCreateDate(df.format(record.getCreateDate()));
				if (record.getModifyUser() != null && record.getModifyUser().length() > 0) {
					acceptswitchBO.setModifyUser(record.getModifyUser());
				} else {
					acceptswitchBO.setModifyUser("");
				}
				acceptswitchBO.setModifyDate(df.format(record.getModifyDate()));
				acceptswitchboList.add(i, acceptswitchBO);
			}
		} else {
			acceptswitchboList = new ArrayList<AcceptSwitchBO>(0);
		}
		this.responseObj.setAcceptswitchList(acceptswitchboList);
		this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);
	}

}
