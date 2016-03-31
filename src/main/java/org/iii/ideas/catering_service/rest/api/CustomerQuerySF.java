package org.iii.ideas.catering_service.rest.api;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.School;
import org.iii.ideas.catering_service.dao.SfCompany;
import org.iii.ideas.catering_service.dao.SfCustomerServiceInfo;
import org.iii.ideas.catering_service.dao.SfPrevSoldCode;
import org.iii.ideas.catering_service.dao.SfPrevSoldCodeDAO;
import org.iii.ideas.catering_service.dao.SfProduct;
import org.iii.ideas.catering_service.dao.SfSchoolproductset;
import org.iii.ideas.catering_service.rest.bo.CustomerQuerySFBO;
import org.iii.ideas.catering_service.rest.bo.CustomerQuerySFBoCompany;
import org.iii.ideas.catering_service.rest.bo.CustomerQuerySFBoCustService;
import org.iii.ideas.catering_service.rest.bo.CustomerQuerySFBoFactory;
import org.iii.ideas.catering_service.rest.bo.CustomerQuerySFBoProduct;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;

public class CustomerQuerySF extends AbstractApiInterface<CustomerQuerySFRequest, CustomerQuerySFResponse>  {

	@Override
	public void process() throws NamingException, ParseException {
		Integer pageNum = Integer.valueOf( this.requestObj.getPageNum());
		Integer pageLimit = Integer.valueOf( this.requestObj.getPageLimit());
		Integer sid = this.requestObj.getSid();
		// 取得當前日期
		Timestamp currentTS = CateringServiceUtil.getCurrentTimestamp();
		String currentTSStr = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd HH:mm:ss", currentTS);

		Criteria criteria = dbSession.createCriteria(SfSchoolproductset.class)
				.add( Restrictions.eq("schoolId", sid ) );

		Criterion offShelfDateCond01 = Restrictions.isNull("offShelfDate");
		Criterion offShelfDateCond02 = Restrictions.or(Restrictions.ge("offShelfDate", currentTS));
		//, Restrictions.geProperty("offShelfDate", "onShelfDate")
		criteria.add(Restrictions.or(offShelfDateCond01, offShelfDateCond02));
		//criteria.add(Restrictions.le("onShelfDate", currentTSStr));
		criteria.add(Restrictions.le("onShelfDate", currentTS));
		// 組出的SQL如下：
		/***
		    select
		        this_.Id as Id1_27_0_,
		        this_.SchoolId as SchoolId2_27_0_,
		        this_.ProductId as ProductI3_27_0_,
		        this_.OnShelfDate as OnShelfD4_27_0_,
		        this_.OffShelfDate as OffShelf5_27_0_ 
		    from
		        cateringservice.sf_schoolproductset this_ 
		    where
		        this_.SchoolId=? 
		        and (
		            this_.OffShelfDate is null 
		            or (
		                this_.OffShelfDate>=?
		            )
		        ) 
		        and this_.OnShelfDate<=?
		 *****/
		
		//TODO 依schoolId查出countyId
		School sfSchool = (School) HibernateUtil.getObjectByFieldId(dbSession, School.class, "schoolId", sid);
		
		List schoolProductSet = criteria.list();
		Iterator<SfSchoolproductset> iterator = schoolProductSet.iterator();
		List<Object> procList = new ArrayList<Object>();

		if(schoolProductSet.size()==0) {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("找不到資料");
			return;
		}

		while (iterator.hasNext()) {
			SfSchoolproductset scProductSet = iterator.next();
			// 依學校Id(SchoolId)取得『學校資料(School)』
			//School school = (School) HibernateUtil.getObjectByFieldId(dbSession, School.class, "schoolId", scProductSet.getSchoolId());
			// 依產品Id(ProductId)取得『產品資料(SfProduct)』
			//System.out.println("scProductSet.getProductId():"+scProductSet.getProductId());
			SfProduct sfProduct = (SfProduct) HibernateUtil.getObjectByFieldId(dbSession, SfProduct.class, "id", 
					scProductSet.getProductId());
			//System.out.println("sfProduct.getSupplierCompanyId():"+sfProduct.getSupplierCompanyId());			
			// 依(SupplierCompanyId)取得Company資料
			SfCompany company = (SfCompany) HibernateUtil.getObjectByFieldId(dbSession, SfCompany.class, "id", 
					sfProduct.getSupplierCompanyId());

			// 依(ManufacturerId)取得Factory資料
			SfCompany factory = (SfCompany) HibernateUtil.getObjectByFieldId(dbSession, SfCompany.class, "id", 
					sfProduct.getManufacturerId());
			//System.out.println("sfProduct.getManufacturerId():"+sfProduct.getManufacturerId());
			// 依供應商Id(SupplierCompanyId)取得『產品客服資訊(SfCustomerServiceInfo)』
//			SfCustomerServiceInfo customerService = (SfCustomerServiceInfo) HibernateUtil.getObjectByFieldId(dbSession, 
//					SfCustomerServiceInfo.class, "supplierCompanyId", company.getId());
			
			// 應該改為如下
			HashMap condCustomerServInfo = new HashMap();
			condCustomerServInfo.put("supplierCompanyId", sfProduct.getSupplierCompanyId());
			condCustomerServInfo.put("manufacturerId", sfProduct.getManufacturerId());
			SfCustomerServiceInfo customerService = (SfCustomerServiceInfo) HibernateUtil.getObjectListByFieldId(dbSession, 
					SfCustomerServiceInfo.class, condCustomerServInfo).get(0);
			//System.out.println("company.getId():"+company.getId());
			
			CustomerQuerySFBO cusBo = new CustomerQuerySFBO();
			CustomerQuerySFBoProduct cqSfBoProduct = new CustomerQuerySFBoProduct();
			CustomerQuerySFBoCompany cqSfBoCompany = new CustomerQuerySFBoCompany();
			CustomerQuerySFBoFactory cqSfBoFactory = new CustomerQuerySFBoFactory();
			CustomerQuerySFBoCustService cqSfBoCustService = new CustomerQuerySFBoCustService();
			
			// 塞入JSON product資料
			cqSfBoProduct.setProductId(sfProduct.getId());
			cqSfBoProduct.setName(sfProduct.getName());
			// 由sf_prevsoldcode table 中的 code 查出其『保存方式』
			// 依code找出該code的對應之保存方式
			HashMap condSfPrevSoldCodeInfo = new HashMap();
			condSfPrevSoldCodeInfo.put("code", sfProduct.getPreservedMethod());
			SfPrevSoldCode prevsoldCodeMethod = (SfPrevSoldCode) HibernateUtil.getObjectListByFieldId(dbSession, 
					SfPrevSoldCode.class, condSfPrevSoldCodeInfo).get(0);
			cqSfBoProduct.setPreservedMethod(prevsoldCodeMethod.getCodeName());
			
			HashMap condSoldway = new HashMap();
			condSoldway.put("code", sfProduct.getSoldway());
			SfPrevSoldCode prevsoldCodeSoldway = (SfPrevSoldCode) HibernateUtil.getObjectListByFieldId(dbSession, 
					SfPrevSoldCode.class, condSoldway).get(0);
			cqSfBoProduct.setSoldway(prevsoldCodeSoldway.getCodeName());
			
			cqSfBoProduct.setPackages(sfProduct.getPackages());
			cqSfBoProduct.setCertification(sfProduct.getCertification());
			cqSfBoProduct.setCertificationId(sfProduct.getCertificationId());
			// 組image REST Path
			String imagePath = "/cateringservice/file/SHOW/";
			String zImage = imagePath + "schoolFoodImg|" + sfSchool.getCountyId() + "|" + sfProduct.getId() + "|" + CateringServiceUtil.DishImageSizeHigh + "|" + CateringServiceUtil.DishImageSizeWidth;
			cqSfBoProduct.setImage(zImage);
			
			// 塞入JSON company資料
			cqSfBoCompany.setCom_id(company.getId());
			cqSfBoCompany.setCom_name(company.getName());
			cqSfBoCompany.setCom_add(company.getAddress());
			cqSfBoCompany.setCom_tel(company.getTel());
			cqSfBoCompany.setCom_owner(company.getOwner());
			
			// 塞入JSON factory資料
			cqSfBoFactory.setFac_id(factory.getId());
			cqSfBoFactory.setFac_name(factory.getName());
			cqSfBoFactory.setFac_add(factory.getAddress());
			cqSfBoFactory.setFac_tel(factory.getTel());

			// 塞入JSON customerService資料
			cqSfBoCustService.setCs_id(customerService.getId());
			cqSfBoCustService.setCs_hotline(customerService.getHotline());
			cqSfBoCustService.setCs_owner(customerService.getOwner());
			cqSfBoCustService.setCs_stuff(customerService.getContactStaff());
			cqSfBoCustService.setCs_Tel(customerService.getContactTel());
			cqSfBoCustService.setCs_Fax(customerService.getContactFax());
			
			cusBo.setProduct(cqSfBoProduct);
			cusBo.setCompany(cqSfBoCompany);
			cusBo.setFactory(cqSfBoFactory);
			cusBo.setCustomerService(cqSfBoCustService);
			procList.add(cusBo);
		}

		List resultList = procList;
		// 將全部資料及欲查詢之頁碼、每頁顯示之筆數等資訊放入該method處理，回傳畫面顯示之資料。
		HashMap<Integer, List> procHashMap = HibernateUtil.getObjList4PageResult(pageNum, pageLimit, procList);
		List<Object> processList = (List<Object>) procHashMap.get(pageNum);
		// 轉成欲輪出之物件
		List<CustomerQuerySFBO> rtnList = new ArrayList<CustomerQuerySFBO>();
		for(int i=0; i<processList.size(); i++) {
			rtnList.add((CustomerQuerySFBO) processList.get(i));
		}
		this.responseObj.setProductList(rtnList);

		this.responseObj.setTotalNum(schoolProductSet.size());
		this.responseObj.setSid(sid);
		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");
	}

}
