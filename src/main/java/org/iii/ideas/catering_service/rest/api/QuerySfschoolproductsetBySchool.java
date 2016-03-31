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
import org.iii.ideas.catering_service.util.HibernateUtil;

/*
 * 依查詢條件查詢sf_schoolproductset中的資料
 */
public class QuerySfschoolproductsetBySchool extends
		AbstractApiInterface<QuerySfschoolproductsetBySchoolRequest, QuerySfschoolproductsetBySchoolResponse> {
	private static final long serialVersionUID = -9115574829368651090L;

	@Override
	public void process() throws NamingException, ParseException {
		Integer schoolId = this.requestObj.getSchoolId();
		String status = this.requestObj.getApproveStatus();
		String _startDate = this.requestObj.getStartDate() + " 00:00:00";
		String _endDate = this.requestObj.getEndDate() + " 23:59:59";		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		List<SfSchoolproductsetBO> sfschoolproductsetboList = processQuery(schoolId, status, _startDate, _endDate, sdf);
		this.responseObj.setSfschoolproductsetList(sfschoolproductsetboList);
		this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);
	}
	
	/*
	 * 依查詢條件查詢sf_schoolproductset中的資料,並開放給匯出功能使用
	 */
	public List<SfSchoolproductsetBO> processQuery(Integer schoolId, String status, String _startDate, String _endDate, SimpleDateFormat sdf) throws ParseException {
		List<SfSchoolproductsetBO> sfschoolproductsetboList = null;
		Timestamp startDate = new Timestamp(sdf.parse(_startDate).getTime());
		Timestamp endDate = new Timestamp(sdf.parse(_endDate).getTime());

		Criteria criteriaASK = HibernateUtil.buildSessionFactory().openSession().createCriteria(SfSchoolproductset.class);
		criteriaASK.add(Restrictions.eq("schoolId", schoolId)); // 學校代碼
		if ("all".equals(status)) { // 狀態(0:待審核, 1審核通過, 2:否決)
		} else if ("waitApprove".equals(status)) {
			criteriaASK.add(Restrictions.eq("status", "0"));
		} else if ("approved".equals(status)) {
			criteriaASK.add(Restrictions.eq("status", "1"));
		} else if ("rejected".equals(status)) {
			criteriaASK.add(Restrictions.eq("status", "2"));
		}
		criteriaASK.add(Restrictions.ge("createDateTime", startDate)); // 開始日期
		criteriaASK.add(Restrictions.le("createDateTime", endDate)); // 結束日期
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
				Criteria criteriaSfProduct = HibernateUtil.buildSessionFactory().openSession().createCriteria(SfProduct.class);
				criteriaSfProduct.add(Restrictions.eq("id", record.getProductId()));
				List<SfProduct> sfproductList = criteriaSfProduct.list();
				if (sfproductList != null && sfproductList.size() > 0) {
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
		return sfschoolproductsetboList;
	}
}
