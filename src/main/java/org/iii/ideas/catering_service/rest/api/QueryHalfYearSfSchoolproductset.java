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
import org.iii.ideas.catering_service.dao.SfProduct;
import org.iii.ideas.catering_service.dao.SfSchoolproductset;
import org.iii.ideas.catering_service.rest.bo.SfSchoolproductsetBO;

/*
 * Query sf_schoolproductset中半年內的資料
 */
public class QueryHalfYearSfSchoolproductset extends
		AbstractApiInterface<QueryHalfYearSfSchoolproductsetRequest, QueryHalfYearSfSchoolproductsetResponse> {
	private static final long serialVersionUID = -9115574829368651090L;

	@Override
	public void process() throws NamingException, ParseException {
		List<SfSchoolproductsetBO> sfschoolproductsetboList = null;
		Timestamp halfYear = new Timestamp(System.currentTimeMillis() - (long) (182.5 * 24 * 60 * 60 * 1000));
		Criteria criteriaASK = this.dbSession.createCriteria(SfSchoolproductset.class);
		criteriaASK.add(Restrictions.eq("schoolId", this.requestObj.getSchoolId()));
		criteriaASK.add(Restrictions.ge("createDateTime", halfYear));
		criteriaASK.addOrder(Order.desc("createDateTime"));
		List<SfSchoolproductset> sfschoolproductsetList = criteriaASK.list();

		if (sfschoolproductsetList != null && sfschoolproductsetList.size() > 0) {
			sfschoolproductsetboList = new ArrayList<SfSchoolproductsetBO>(sfschoolproductsetList.size());
			SimpleDateFormat dfDate = new SimpleDateFormat("yyyy/MM/dd");
			SimpleDateFormat dfDateTime = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			for (int i = 0; i < sfschoolproductsetList.size(); i++) {
				SfSchoolproductset record = sfschoolproductsetList.get(i);
				SfSchoolproductsetBO sfschoolproductsetBO = new SfSchoolproductsetBO();
				sfschoolproductsetBO.setId(new Long((long) (i + 1)));
				sfschoolproductsetBO.setSchoolId(record.getSchoolId());
				sfschoolproductsetBO.setProductId(record.getProductId());
				if (record.getOnShelfDate() != null && "1".equals(record.getStatus())) {
					sfschoolproductsetBO.setOnShelfDate(dfDate.format(record.getOnShelfDate()));
				} else {
					sfschoolproductsetBO.setOnShelfDate("");
				}
				if (record.getOffShelfDate() != null) {
					sfschoolproductsetBO.setOffShelfDate(dfDate.format(record.getOffShelfDate()));
				} else {
					sfschoolproductsetBO.setOffShelfDate("");
				}
				sfschoolproductsetBO.setCreateUser(record.getCreateUser());
				sfschoolproductsetBO.setCreateDateTime(dfDateTime.format(record.getCreateDateTime()));
				sfschoolproductsetBO.setModifyUser(record.getModifyUser());
				sfschoolproductsetBO.setModifyDateTime(dfDateTime.format(record.getModifyDateTime()));
				// 取得產品名稱
				Criteria criteriaSfProduct = this.dbSession.createCriteria(SfProduct.class);
				criteriaSfProduct.add(Restrictions.eq("id", record.getProductId()));
				List<SfProduct> sfproductList = criteriaSfProduct.list();
				if(sfproductList!=null && sfproductList.size() > 0){
					SfProduct sfproduct = sfproductList.get(0);
					if (sfproduct != null) {
						sfschoolproductsetBO.setProductName(sfproduct.getName());
					}
				}
				// 顯示狀態 : 0:待審核, 1審核通過, 2:否決
				if ("0".equals(record.getStatus())) {
					sfschoolproductsetBO.setStatus("審核中");
				} else if ("1".equals(record.getStatus())) {
					sfschoolproductsetBO.setStatus("審核通過");
				} else if ("2".equals(record.getStatus())) {
					sfschoolproductsetBO.setStatus("否決");
				}
				sfschoolproductsetboList.add(sfschoolproductsetBO);
			}
		} else {
			sfschoolproductsetboList = new ArrayList<SfSchoolproductsetBO>(0);
		}
		this.responseObj.setHalfyearsfschoolproductsetList(sfschoolproductsetboList);

		this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);
	}

}
