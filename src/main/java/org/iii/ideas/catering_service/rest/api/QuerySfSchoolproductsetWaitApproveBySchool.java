package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.SfCompany;
import org.iii.ideas.catering_service.dao.SfProduct;
import org.iii.ideas.catering_service.dao.SfSchoolproductset;
import org.iii.ideas.catering_service.rest.bo.SfSchoolproductsetBO;

/*
 * Query sf_schoolproductset中待審核的資料
 */
public class QuerySfSchoolproductsetWaitApproveBySchool extends AbstractApiInterface<QuerySfSchoolproductsetWaitApproveBySchoolRequest, QuerySfSchoolproductsetWaitApproveBySchoolResponse> {
	private static final long serialVersionUID = -9115574829368651090L;

	@Override
	public void process() throws NamingException, ParseException {
		List<SfSchoolproductsetBO> sfschoolproductsetboList = null;
		Criteria criteriaASK = this.dbSession.createCriteria(SfSchoolproductset.class);
		criteriaASK.add(Restrictions.eq("schoolId", this.requestObj.getSchoolId()));
		criteriaASK.add(Restrictions.eq("status", "0"));
		criteriaASK.addOrder(Order.desc("createDateTime"));
		List<SfSchoolproductset> sfschoolproductsetList = criteriaASK.list();

		if (sfschoolproductsetList != null && sfschoolproductsetList.size() > 0) {
			sfschoolproductsetboList = new ArrayList<SfSchoolproductsetBO>(sfschoolproductsetList.size());
			SimpleDateFormat dfDate = new SimpleDateFormat("yyyy/MM/dd");
			SimpleDateFormat dfDateTime = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			for (int i = 0; i < sfschoolproductsetList.size(); i++) {
				SfSchoolproductset record = sfschoolproductsetList.get(i);
				SfSchoolproductsetBO sfschoolproductsetBO = new SfSchoolproductsetBO();
				// 序號
				sfschoolproductsetBO.setId(record.getId());

				Criteria criteriaSfProduct = this.dbSession.createCriteria(SfProduct.class);
				criteriaSfProduct.add(Restrictions.eq("id", record.getProductId()));
				List<SfProduct> sfproductList = criteriaSfProduct.list();
				if (sfproductList != null && sfproductList.size() > 0) {
					SfProduct sfproduct = sfproductList.get(0);
					if (sfproduct != null) {
						// 商品名稱
						sfschoolproductsetBO.setProductName(sfproduct.getName());
						// 供應商名稱
						Criteria criteriaSuppliercompany = this.dbSession.createCriteria(SfCompany.class);
						criteriaSuppliercompany.add(Restrictions.eq("id", sfproduct.getSupplierCompanyId()));
						List<SfCompany> suppliercompanyList = criteriaSuppliercompany.list();
						if (suppliercompanyList != null && suppliercompanyList.size() > 0) {
							SfCompany suppliercompany = suppliercompanyList.get(0);
							if (suppliercompany != null) {
								sfschoolproductsetBO.setSuppliercompanyName(suppliercompany.getName());
							} else {
								sfschoolproductsetBO.setSuppliercompanyName("");
							}
						}
						// 製造商名稱
						Criteria criteriaManufacturer = this.dbSession.createCriteria(SfCompany.class);
						criteriaManufacturer.add(Restrictions.eq("id", sfproduct.getManufacturerId()));
						List<SfCompany> manufacturerList = criteriaManufacturer.list();
						if (manufacturerList != null && manufacturerList.size() > 0) {
							SfCompany manufacturer = manufacturerList.get(0);
							if (manufacturer != null) {
								sfschoolproductsetBO.setManufacturerName(manufacturer.getName());
							} else {
								sfschoolproductsetBO.setManufacturerName("");
							}
						}
						// 包裝資訊
						if (sfproduct.getPackages().trim().length() > 0) {
							sfschoolproductsetBO.setPackageType(sfproduct.getPackages());
						} else {
							sfschoolproductsetBO.setPackageType("");
						}
						// 認證標章
						if (sfproduct.getCertification().trim().length() > 0) {
							sfschoolproductsetBO.setCertification(sfproduct.getCertification());
						} else {
							sfschoolproductsetBO.setCertification("");
						}
						// 認證編號
						if (sfproduct.getCertificationId().trim().length() > 0) {
							sfschoolproductsetBO.setCertificationId(sfproduct.getCertificationId());
						} else {
							sfschoolproductsetBO.setCertificationId("");
						}
					} else {
						sfschoolproductsetBO.setProductName("");
					}
				}
				sfschoolproductsetboList.add(sfschoolproductsetBO);
			}
		} else {
			sfschoolproductsetboList = new ArrayList<SfSchoolproductsetBO>(0);
		}
		this.responseObj.setSfschoolproductsetwaitapprovebyschoolList(sfschoolproductsetboList);
		this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);
	}

}
