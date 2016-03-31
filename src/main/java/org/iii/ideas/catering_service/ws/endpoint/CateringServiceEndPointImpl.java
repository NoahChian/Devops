package org.iii.ideas.catering_service.ws.endpoint;

import java.io.File;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.iii.ideas.catering_service.dao.Batchdata;
import org.iii.ideas.catering_service.dao.Dish;
import org.iii.ideas.catering_service.dao.Ingredient;
import org.iii.ideas.catering_service.dao.Ingredientbatchdata;
import org.iii.ideas.catering_service.dao.Kitchen;
import org.iii.ideas.catering_service.dao.School;
import org.iii.ideas.catering_service.dao.Supplier;
import org.iii.ideas.catering_service.dao.Useraccount;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;
import org.iii.ideas.catering_service.util.SystemConfiguration;
import org.iii.ideas.catering_service.util.ZipUtils;
import org.iii.ideas.catering_service.ws.schema.DeleteBatchProductRequestType;
import org.iii.ideas.catering_service.ws.schema.FoodResponseType;
import org.iii.ideas.catering_service.ws.schema.ObjectFactory;
import org.iii.ideas.catering_service.ws.schema.UploadBatchProductRequestType;
import org.iii.ideas.catering_service.ws.schema.UploadBatchProductRequestType.Sources;
import org.iii.ideas.catering_service.ws.schema.UploadBatchProductRequestType.Sources.SourceItem;
import org.iii.ideas.catering_service.ws.schema.UploadBatchProductRequestType.Sources.SourceItem.SourceInspectionFileList;
import org.iii.ideas.catering_service.ws.schema.UploadProductRequestType;
import org.iii.ideas.catering_service.ws.schemav2.CateringServiceEndPointv2Impl;
import org.iii.ideas.catering_service.ws.schemav2.DeleteMenuRequestType;
import org.iii.ideas.catering_service.ws.schemav2.DeleteSupplierRequestType;
import org.iii.ideas.catering_service.ws.schemav2.FoodResponseTypev2;
import org.iii.ideas.catering_service.ws.schemav2.UploadIngredientRequestType;
import org.iii.ideas.catering_service.ws.schemav2.UploadMenuRequestType;
import org.iii.ideas.catering_service.ws.schemav2.UploadSupplierRequestType;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.mime.Attachment;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.SoapMessage;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

/*
 * 團膳SOAP主程式
 */
/*
 * response code
 編號	說明
 0	上傳成功
 100	資料檢查失敗
 101	無法存取上傳資料
 102	產品一維條碼格式不正確
 103	產品重複
 104	其他錯誤

 */
@Endpoint
public class CateringServiceEndPointImpl extends CateringServiceEndPoint {
	static Logger log=Logger.getLogger("org.iii.ideas.catering_service.ws.endpoint.CateringServiceEndPointImpl");
	/**
	 * 檔案上傳路徑config path / UPLOAD_BATCH_PRODUCT_TMP_FLD
	 */
	private static final String UPLOAD_BATCH_PRODUCT_TMP_FLD = "upload_batch_product_tmp";

	/**
	 * 檔案命名範本
	 */
	private static final String FILE_NAME_FORMAT = "tmp%s.zip";
	private static final String MODIFY_SOURCE = "MODIFY_SOURCE";
	private static final String ADD_SOURCE = "ADD_SOURCE";
	private static final String MODIFY_MENU = "UPDATE_MENU";
	private static final String ADD_MENU = "ADD_MENU";
	private static final String DELETE_MENU = "DELETE_MENU";

	public CateringServiceEndPointImpl() {
		this.objectFactory = new ObjectFactory();

	}

	// 新增學校菜單
	@PayloadRoot(localPart = "UploadProductRequest", namespace = TARGET_NAMESPACE)
	@ResponsePayload
	public JAXBElement<FoodResponseType> handleUploadProduct(MessageContext ctx,
			@RequestPayload JAXBElement<UploadProductRequestType> soapRquest) throws Exception {
		UploadProductRequestType request = soapRquest.getValue();

		Date currentDate = new Date();
		String xml = this.objectToXml(UploadProductRequestType.class, request);
		FoodResponseType response = objectFactory.createFoodResponseType();
		response.setServiceDate(request.getServiceDate().replace("-", "/"));
		response.setSchoolId(request.getSchoolId());
		response.setMessageId(request.getMessageId());
		String wsfile = SystemConfiguration.writeReciveWS(request.getMessageId(), xml, request.getCompanyId()
				+ "-" + request.getSchoolId()+"-"+request.getAction(), currentDate,"REQ");
		// -------------------process---------------------------
		SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			if ( ADD_MENU.equals(request.getAction().toUpperCase())  != true &&  MODIFY_MENU.equals(request.getAction().toUpperCase())  != true) {
				throw new Exception("請確認Action參數內容 add_menu / update_menu");
			}

			// 查詢帳號資料
			Useraccount useraccount = HibernateUtil.queryUseraccountByName(session, request.getCompanyId());
			if (useraccount == null) {
				throw new Exception("找不到此帳號(業者統編/學校代碼):" + request.getCompanyId());
			}
			Kitchen kitchen = HibernateUtil.queryKitchenById(session, useraccount.getKitchenId());
			if (kitchen == null) {
				throw new Exception("找不到此團膳/自立廚房資料:" + request.getCompanyId());
			}

			Integer kitchenId = kitchen.getKitchenId();
			Integer schoolId = Integer.valueOf(request.getSchoolId());
			//查
			Batchdata batchdata = HibernateUtil.queryBatchdataByUK(session, kitchenId, schoolId, request
					.getServiceDate().replace("-", "/"), "");
			if (batchdata != null && ADD_MENU.equals(request.getAction().toUpperCase()) ) {
				throw new Exception("菜單資料已存在:" + request.getServiceDate().replace("-", "/") + " 學校ID:"
						+ request.getSchoolId());
			} else if (batchdata == null && MODIFY_MENU.equals(request.getAction().toUpperCase())) {
				throw new Exception("菜單資料不存在:" + request.getServiceDate().replace("-", "/") + " 學校ID:"
						+ request.getSchoolId());
			}

			List<School> schoolList = HibernateUtil.querySchoolListByKitchenId(session, kitchenId);
			Iterator<School> schoolIterator = schoolList.iterator();
			boolean schoolExists = false;
			while (schoolIterator.hasNext()) {
				School school = schoolIterator.next();
				// System.out.println("SchoolId:"+school.getSchoolId()+" request:"+schoolId);
				if (school.getSchoolId().equals(schoolId)) {
					// System.out.println("------------SchoolId:"+school.getSchoolId()+" request:"+schoolId);
					schoolExists = true;
				}
			}

			if (!schoolExists) {
				throw new Exception("此團膳/自立廚房找不到學校資料 ID:" + request.getSchoolId());
			}

			/*
			 * Kitchen kitchen = HibernateUtil.queryKitchenByCompanyId(session, request.getCompanyId());
			 * if(kitchen==null){ throw new Exception("請確認統編是否正確:"+request.getCompanyId()); } Integer kitchenId =
			 * kitchen.getKitchenId();
			 */
			if (batchdata == null) {
				batchdata = new Batchdata();
			}
			
			
			String attachmentStorePaths = processAttachments(ctx, FilenameUtils.getFullPath(wsfile)
					+ FilenameUtils.getBaseName(wsfile));
			List<String> fileList = new ArrayList<String>();
			HashMap<Integer,String> dishImgFileList = new HashMap<Integer,String>();
			if(!"".equals(attachmentStorePaths)){
				fileList = CateringServiceUtil.listFiles(attachmentStorePaths);
				for(String dishImgName : fileList){
					String ext =  FilenameUtils.getExtension(dishImgName);
					String dishName =  FilenameUtils.getBaseName(dishImgName);
					Dish dish = HibernateUtil.queryDishByName(session, kitchenId, dishName);
					if(dish==null){
						throw new Exception("此團膳/自立廚房找不到菜色:" + dishName+ " 圖檔名稱:"+dishImgName);
					}
					String targetFile = CateringServiceUtil.getDishImageFileName(kitchenId, dish.getDishId(), ext);
					File targetPath = new File(targetFile);
					Files.copy(Paths.get(attachmentStorePaths, dishImgName), targetPath.toPath() );
					CateringServiceUtil.getDishImageFileName(kitchenId, dish.getDishId(),CateringServiceUtil.DishImageSizeHigh ,CateringServiceUtil.DishImageSizeWidth, "png");
					log.info("WS 上傳菜色檔:"+targetPath.toURI());
				}
			}

			batchdata.setKitchenId(kitchenId);
			batchdata.setLotNumber(CateringServiceUtil.defaultLotNumber);// 預設logNumber 為 ""  多數學校只有一餐//  這個欄位有問題
			batchdata.setMenuDate(request.getServiceDate().replace("-", "/"));
			// School school = HibernateUtil.querySchoolByKitchenAndName(session, kitchenId, request.getSchoolId())
			batchdata.setSchoolId(Integer.valueOf(request.getSchoolId()));
			batchdata.setMainFoodId(QueryDishIdByName(session, kitchenId, request.getStaple1()));
			batchdata.setMainFood1id(QueryDishIdByName(session, kitchenId, request.getStaple2()));
			batchdata.setMainDishId(QueryDishIdByName(session, kitchenId, request.getMainCourse1()));
			batchdata.setMainDish1id(QueryDishIdByName(session, kitchenId, request.getMainCourse2()));
			batchdata.setMainDish2id(QueryDishIdByName(session, kitchenId, request.getMainCourse3()));
			// batchdata.setMainDish3id(QueryDishIdByName(session, kitchenId,request.getMainCourse3()));//少主菜問題
			batchdata.setMainDish3id((long)0);// 少主菜問題
			batchdata.setSubDish1id(QueryDishIdByName(session, kitchenId, request.getSideDish1()));
			batchdata.setSubDish2id(QueryDishIdByName(session, kitchenId, request.getSideDish2()));
			batchdata.setSubDish3id(QueryDishIdByName(session, kitchenId, request.getSideDish3()));
			batchdata.setSubDish4id(QueryDishIdByName(session, kitchenId, request.getSideDish4()));
			batchdata.setSubDish5id(QueryDishIdByName(session, kitchenId, request.getSideDish5()));
			batchdata.setSubDish6id(QueryDishIdByName(session, kitchenId, request.getSideDish6()));
			batchdata.setVegetableId(QueryDishIdByName(session, kitchenId, request.getVegetable()));
			batchdata.setSoupId(QueryDishIdByName(session, kitchenId, request.getSoup()));
			batchdata.setDessertId(QueryDishIdByName(session, kitchenId, request.getExtra1()));
			batchdata.setDessert1id(QueryDishIdByName(session, kitchenId, request.getExtra2()));
			batchdata.setCalorie(request.getCalories());
			batchdata.setTypeFruit(request.getFruits());
			batchdata.setTypeGrains(request.getGrains());
			batchdata.setTypeMeatBeans(request.getMeatBeans());
			batchdata.setTypeMilk(request.getMilk());
			batchdata.setTypeOil(request.getOils());
			batchdata.setTypeVegetable(request.getVegetables());
			batchdata.setUploadDateTime(CateringServiceUtil.getCurrentTimestamp());

			if ( ADD_MENU.equals(request.getAction().toUpperCase()) ) {
				HibernateUtil.saveBatchdata(session, batchdata);
			} else {
				HibernateUtil.updateBatchdata(session, batchdata);
			}
			tx.commit();
			session.close();
			response.setDescription(CateringServiceResponseType.getMessage(CateringServiceResponseType.SUCCESS));
			response.setStatus(CateringServiceResponseType.SUCCESS);
			// -------------------process---------------------------
		} catch (Exception e) {
			e.printStackTrace();
			if (!tx.wasRolledBack()) {
				tx.rollback();
			}
			session.close();
			e.printStackTrace();
			response.setDescription(e.getMessage());
			response.setStatus(CateringServiceResponseType.ERROR_UNKNOWN + ":" + e.getMessage());
		}
		
		xml = this.objectToXml(FoodResponseType.class, response);
		wsfile = SystemConfiguration.writeReciveWS(request.getMessageId(), xml, request.getCompanyId()
				+ "-" + request.getSchoolId()+"-"+request.getAction(), currentDate,"RET");
		
		QName qname = new QName(TARGET_NAMESPACE, "UploadProductResponse");
		return new JAXBElement<FoodResponseType>(qname, FoodResponseType.class, response);
	}

	private Long QueryDishIdByName(Session session, Integer kitchenId, String dishName) throws Exception {
		if (CateringServiceUtil.isEmpty(dishName)) {
			return (long)0;
		}
		Long dishId = HibernateUtil.queryDishIdByName(session, kitchenId, dishName);
		if (dishId == null) {
			throw new Exception("於菜色檔中找不到此菜色資料   名稱:" + dishName);
		}
		return dishId;
	}

	// 新增Batch 產品
	@PayloadRoot(localPart = "UploadBatchProductRequest", namespace = TARGET_NAMESPACE)
	@ResponsePayload
	public JAXBElement<FoodResponseType> handleUploadBatchProduct(MessageContext ctx,
			@RequestPayload JAXBElement<UploadBatchProductRequestType> soapRquest) throws Exception {

		UploadBatchProductRequestType request = soapRquest.getValue();

		Date currentDate = new Date();
		String xml = this.objectToXml(UploadBatchProductRequestType.class, request);
		FoodResponseType response = objectFactory.createFoodResponseType();

		Timestamp currentTime = CateringServiceUtil.getCurrentTimestamp();
		String currentTimeStr = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", currentTime);
		response.setServiceDate(request.getServiceDate().replace("-", "/"));
		response.setSchoolId(request.getSchoolId());
		response.setMessageId(request.getMessageId());

		if ( ADD_SOURCE.equals(request.getAction().toUpperCase())   != true && MODIFY_SOURCE.equals(request.getAction().toUpperCase()) != true) {
			response.setDescription("請確認Action參數內容 add_Source / modify_Source");
			response.setStatus(CateringServiceResponseType.ERROR_UNKNOWN);
			QName qname = new QName(TARGET_NAMESPACE, "UploadBatchProductRequest");
			return new JAXBElement<FoodResponseType>(qname, FoodResponseType.class, response);
		}
		// -------------------process---------------------------
		SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String wsfile = SystemConfiguration.writeReciveWS(request.getMessageId(), xml, request.getCompanyId()
				+ "-" + request.getSchoolId()+"-"+request.getAction(), currentDate,"REQ");
		try {
			
			// String ext = FilenameUtils.getPath(wsfile);
			// process attachment
			String attachmentStorePaths = processAttachments(ctx, FilenameUtils.getFullPath(wsfile)
					+ FilenameUtils.getBaseName(wsfile));
			List<String> fileList = new ArrayList<String>();
			if(!"".equals(attachmentStorePaths)){
				fileList = CateringServiceUtil.listFiles(attachmentStorePaths);
			}
			
			// 查詢帳號資料
			Useraccount useraccount = HibernateUtil.queryUseraccountByName(session, request.getUploaderAccount());
			if (useraccount == null) {
				throw new Exception("找不到此帳號:" + request.getUploaderAccount());
			}
			Kitchen kitchen = HibernateUtil.queryKitchenById(session, useraccount.getKitchenId());
			if (kitchen == null) {
				throw new Exception("找不到此團膳/自立廚房資料:" + request.getUploaderAccount());
			}

			Integer kitchenId = kitchen.getKitchenId();

			Integer schoolId = Integer.valueOf(request.getSchoolId());
			List<School> schoolListtmp = HibernateUtil.querySchoolListByKitchenId(session, kitchenId);
			Iterator<School> schoolIterator = schoolListtmp.iterator();
			boolean schoolExists = false;
			while (schoolIterator.hasNext()) {
				School school = schoolIterator.next();
				if (school.getSchoolId().equals(schoolId)) {
					schoolExists = true;
				}
			}

			if (!schoolExists) {
				throw new Exception("此團膳/自立廚房找不到學校資料 ID:" + request.getSchoolId());
			}

			request.getMessageId(); // 食品廠商上傳識別碼
			request.getSchoolId();// 學校編號
			request.getServiceDate();// 供餐日期
			request.getUploaderAccount();// 上傳帳號
			
			String lotNum = CateringServiceUtil.defaultLotNumber;// 有問題
			String menuDate = request.getServiceDate().replace("-", "/");
			// Integer schoolId = Integer.valueOf(request.getSchoolId());

			Batchdata batchdata = HibernateUtil.queryBatchdataByUK(session, kitchenId, schoolId, menuDate, lotNum);
			if (batchdata == null) {
				throw new Exception("找不到學校ID:" + schoolId + " 日期:" + menuDate + " 的菜單");
			}
			if(MODIFY_SOURCE.equals(request.getAction().toUpperCase())){
				HibernateUtil.deleteIngredientbatchdataByBatchdataId(session, batchdata.getBatchDataId());
			}

			Sources sources = request.getSources();// 食材資訊集
			List<SourceItem> sourceItemList = sources.getSourceItem();
			Iterator<SourceItem> sourceItemIterator = sourceItemList.iterator();
			while (sourceItemIterator.hasNext()) {
				SourceItem sourceItem = sourceItemIterator.next();
				List<School> schoolList = HibernateUtil.querySchoolListByKitchenId(session, kitchenId);
				Dish dish = HibernateUtil.queryDishByName(session, kitchenId, sourceItem.getDishesName());
				// sourceItem.getBrands();//品牌
				// sourceItem.getCertificationID();//認證編號
				// sourceItem.getDishesName();//菜色名稱
				String expirationDate = "";
				String stockDate = "";
				String manufactureDate = "";
				if (!CateringServiceUtil.isEmpty(sourceItem.getExpirationDate())) {
					expirationDate = sourceItem.getExpirationDate().replace("-", "/");// 有效日期
				}
				if (!CateringServiceUtil.isEmpty(sourceItem.getStockDate())) {
					stockDate = sourceItem.getStockDate().replace("-", "/");// 進貨日期
				}
				if (!CateringServiceUtil.isEmpty(sourceItem.getManufactureDate())) {
					manufactureDate = sourceItem.getManufactureDate().replace("-", "/");// 製造日期
				}
				// sourceItem.getOrigin();//產地
				// sourceItem.getSource();//來源地
				// sourceItem.getSourceCertification();//食材認證
				// sourceItem.getSourceName();//食材名稱
				// sourceItem.getLotNumber();//批次
				String supplierCompanyId = sourceItem.getSupplierId();// 供應商統一編號
				if (CateringServiceUtil.isEmpty(supplierCompanyId)) {
					throw new Exception("請填入供應商統編!");
				}
				Supplier supplier = HibernateUtil.querySupplierByCompanyId(session, kitchenId, supplierCompanyId);
				if (supplier == null) {
					throw new Exception("請確認登入供應商資料!");
				}
				// sourceItem.getSupplierName();//供應商名稱

				

				Long dishId = HibernateUtil.queryDishIdByName(session, kitchenId, sourceItem.getDishesName());
				if (dishId == null) {
					throw new Exception("找不到菜色:" + sourceItem.getDishesName());
				}
				// Supplier supplier = HibernateUtil.querySupplierByCompanyId(session, kitchenId, supplierCompanyId);

				Ingredient ingredient = HibernateUtil.queryIngredientByName(session, dishId,
						sourceItem.getSourceName());
				if (ingredient == null) {
					/*
					 * 採用自動新增食材資料 if(request.getAction().equals("modify_Source")){ throw new Exception
					 * ("資料不存在,請新增!"); }
					 */
					ingredient = HibernateUtil.saveIngredient(session, dishId, sourceItem.getSourceName(),
							sourceItem.getBrands(), supplier.getId().getSupplierId(), supplierCompanyId);
				} else {
					ingredient.setBrand(sourceItem.getBrands());
					ingredient.setSupplierId(supplier.getId().getSupplierId());
					ingredient.setSupplierCompanyId(supplierCompanyId);
					session.update(ingredient);
				}
				
				Ingredientbatchdata ingredientbatchdata = null;
				String lotNumOfIngredient=CateringServiceUtil.defaultLotNumber;
				if(sourceItem.getLotNumber()!=null){
					lotNumOfIngredient = sourceItem.getLotNumber();
				}
				
				ingredientbatchdata = HibernateUtil.queryIngredientbatchdataByBatchdataIdAndIngredient(session,
						batchdata.getBatchDataId(), dishId, ingredient.getIngredientId(),
						ingredient.getSupplierCompanyId(), lotNumOfIngredient);
				// 如菜單中食材資料已存在就刪除後再新增
				if (ingredientbatchdata != null) {
					if(ADD_SOURCE.equals(request.getAction().toUpperCase())){
						 throw new Exception ("資料己存在,請用修改功能!");
					 }else{
						 HibernateUtil.deleteIngredientbatchdataByBatchdataId(session,
									ingredientbatchdata.getIngredientBatchId());
					 }
				}
				ingredientbatchdata = HibernateUtil.saveIngredientbatchdata(session, batchdata.getBatchDataId(),
						ingredient.getIngredientId(), supplier.getId().getSupplierId(), dishId, lotNumOfIngredient,
						ingredient.getIngredientName(), sourceItem.getBrands(), stockDate, expirationDate,
						manufactureDate, sourceItem.getCertificationID(), sourceItem.getSourceCertification(),
						ingredient.getSupplierCompanyId(), sourceItem.getOrigin(), sourceItem.getSource());
				//copy file to inspection file
				SourceInspectionFileList sourceInspectionFileList = sourceItem.getSourceInspectionFileList();// 食材檢驗報告集
				sourceInspectionFileList.getSourceIngredientsInspectionLab();// 食材檢驗報告實驗室
				sourceInspectionFileList.getSourceIngredientsInspectionStatus();// 食材檢驗報告結果
				String inspectionFile = sourceInspectionFileList.getSourceInspectionFile();// 食材檢驗報告檔案名稱
				if(!CateringServiceUtil.isEmpty(inspectionFile)){
					log.info("WS 此食材應有的上傳檢驗檔:"+inspectionFile);
					String ext =  FilenameUtils.getExtension(inspectionFile);
					String targetFile = CateringServiceUtil.getInspectionFileName(kitchenId,Long.valueOf( ingredientbatchdata.getIngredientBatchId()), ext);
					if(fileList.contains(inspectionFile)){
						File targetPath = new File(targetFile);
						Files.copy(Paths.get(attachmentStorePaths, inspectionFile), targetPath.toPath() );
						log.info("WS 上傳檢驗檔:"+targetPath.toURI());
						System.out.println("WS 上傳檢驗檔:"+targetPath.toURI());
					}else{
						log.info("WS 找不到上傳之檔案:"+Paths.get(attachmentStorePaths, inspectionFile));
						System.out.println("WS 找不到上傳之檔案:"+Paths.get(attachmentStorePaths, inspectionFile));
					}
				}
			}

			tx.commit();
			session.close();
			response.setDescription(CateringServiceResponseType.getMessage(CateringServiceResponseType.SUCCESS));
			response.setStatus(CateringServiceResponseType.SUCCESS);
			// -------------------process---------------------------
		} catch (Exception e) {
			e.printStackTrace();
			if (!tx.wasRolledBack()) {
				tx.rollback();
			}
			session.close();
			e.printStackTrace();
			response.setDescription(e.getMessage());
			response.setStatus(CateringServiceResponseType.ERROR_UNKNOWN);
		}
		// -------------------process---------------------------
		xml = this.objectToXml(FoodResponseType.class, response);
		wsfile = SystemConfiguration.writeReciveWS(request.getMessageId(), xml, request.getCompanyId()
				+ "-" + request.getSchoolId()+"-"+request.getAction(), currentDate,"RET");
		QName qname = new QName(TARGET_NAMESPACE, "UploadBatchProductRequest");
		return new JAXBElement<FoodResponseType>(qname, FoodResponseType.class, response);
	}

	/**
	 * 儲存附件
	 * 
	 * @param ctx
	 * @return
	 * @throws Exception
	 */
	private String processAttachments(MessageContext ctx, String ukpath) throws Exception {
		//List<String> tmpZipFilePath = new ArrayList<String>();
		SoapMessage soapMsg = (SoapMessage) ctx.getRequest();
		Iterator<Attachment> iter = soapMsg.getAttachments();
		//List<String> resultPathList = new ArrayList<>();
		if(!soapMsg.getAttachments().hasNext()){
			return "";
		}

		File pathdest = new File(ukpath);
		if (!pathdest.exists()) {
			pathdest.mkdirs();
		}

		Attachment attch = null;

		OpenOption[] openOptions = { StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING,
				StandardOpenOption.WRITE };

		while (iter.hasNext()) {
			attch = iter.next();
			String origFilename = attch.getDataHandler().getName();
			
			if (origFilename != null) {
				CharsetDetector dec = new CharsetDetector();
				dec.setText(origFilename.getBytes());
				CharsetMatch match = dec.detect();
				String encoding = match.getName();
				String encodedFilename =  new String(origFilename.getBytes(encoding), "UTF-8");
				log.info("Write attachment to disk:"+encodedFilename);
				Path fileFullPath = Paths.get(ukpath, encodedFilename);
				// Save to tmp file folder
				String origExt = FilenameUtils.getExtension(encodedFilename);
				Files.write(fileFullPath, IOUtils.toByteArray(attch.getInputStream()), openOptions);
				// tmpZipFilePath.add(fileFullPath.toString());
				if ("zip".equals(origExt.toLowerCase())) {
					if(ZipUtils.unpack(fileFullPath.toFile(), ukpath)){
						Files.deleteIfExists(fileFullPath);
					}
				}
			}
		}
		return ukpath;

	}

	// 刪除Batch 產品
	@PayloadRoot(localPart = "DeleteBatchProductRequest", namespace = TARGET_NAMESPACE)
	@ResponsePayload
	public JAXBElement<FoodResponseType> handleDeleteBatchProduct(
			@RequestPayload JAXBElement<DeleteBatchProductRequestType> soapRquest) throws Exception {

		DeleteBatchProductRequestType request = soapRquest.getValue();

		Date currentDate = new Date();
		String xml = this.objectToXml(DeleteBatchProductRequestType.class, request);

		FoodResponseType response = objectFactory.createFoodResponseType();
		Timestamp currentTime = CateringServiceUtil.getCurrentTimestamp();
		String currentTimeStr = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", currentTime);
		response.setServiceDate(request.getServiceDate().replace("-", "/"));
		response.setSchoolId(request.getSchoolId());
		response.setMessageId(request.getMessageId());
		if (!  DELETE_MENU.equals(request.getAction().toUpperCase())) {
			response.setDescription("請確認Action參數內容 delete_menu");
			response.setStatus(CateringServiceResponseType.ERROR_UNKNOWN);
			QName qname = new QName(TARGET_NAMESPACE, "DeleteBatchProductRequest");
			return new JAXBElement<FoodResponseType>(qname, FoodResponseType.class, response);
		}
		// -------------------process---------------------------
		// System.out.println("UploadProduct WS");
		SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String wsfile = SystemConfiguration.writeReciveWS(request.getMessageId(), xml,request.getSchoolId()+"-"+request.getAction(), currentDate,"REQ");
		try {
			// 查詢帳號資料
			if (request.getUploaderAccount() == null) {
				throw new Exception("UploaderAccount 不可為空值!");
			}
			Useraccount useraccount = HibernateUtil.queryUseraccountByName(session, request.getUploaderAccount());// dennis
			if (useraccount == null) {
				throw new Exception("找不到此帳號:" + request.getUploaderAccount());
			}
			Kitchen kitchen = HibernateUtil.queryKitchenById(session, useraccount.getKitchenId());
			if (kitchen == null) {
				throw new Exception("找不到此團膳/自立廚房資料:" + request.getUploaderAccount());
			}

			Integer kitchenId = kitchen.getKitchenId();

			Integer schoolId = Integer.valueOf(request.getSchoolId());
			List<School> schoolListtmp = HibernateUtil.querySchoolListByKitchenId(session, kitchenId);
			Iterator<School> schoolIterator = schoolListtmp.iterator();
			boolean schoolExists = false;
			while (schoolIterator.hasNext()) {
				School school = schoolIterator.next();
				if (school.getSchoolId().equals(schoolId)) {
					schoolExists = true;
				}
			}

			if (!schoolExists) {
				throw new Exception("此團膳/自立廚房找不到學校資料 ID:" + request.getSchoolId());
			}

			request.getAction();// 刪除食材資訊：delete_Source
			request.getMessageId();// 食品廠商刪除識別碼
			request.getOriginalMessageId();// 食品廠商原始上傳識別碼 , 先前新增或更新菜單填寫之訊息識別碼
			// Integer schoolId = Integer.valueOf(request.getSchoolId());//欲刪除菜單之供餐學校編號
			String menuDate = request.getServiceDate().replace("-", "/");// 欲刪除菜單之日期，格式為YYYY-MM-DD範例：2013-01-01
			request.getUploaderAccount();// 食材資訊上傳帳號
			String lotNum = CateringServiceUtil.defaultLotNumber;//有問題
			HibernateUtil.deleteBatchdataByUK(session, kitchenId, schoolId, menuDate, lotNum);
			tx.commit();
			session.close();
			response.setDescription(CateringServiceResponseType.getMessage(CateringServiceResponseType.SUCCESS));
			response.setStatus(CateringServiceResponseType.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			if (!tx.wasRolledBack()) {
				tx.rollback();
			}
			session.close();
			response.setDescription(e.getMessage());
			response.setStatus(CateringServiceResponseType.ERROR_UNKNOWN);
		}
		// -------------------process---------------------------
		xml = this.objectToXml(FoodResponseType.class, response);
		wsfile = SystemConfiguration.writeReciveWS(request.getMessageId(), xml,request.getSchoolId()+"-"+request.getAction(), currentDate,"RET");
		QName qname = new QName(TARGET_NAMESPACE, "DeleteBatchProductRequest");
		return new JAXBElement<FoodResponseType>(qname, FoodResponseType.class, response);
	}

	// 將Object 轉XML String
	protected String objectToXml(Class classType, Object RequestObj) throws JAXBException {
		// 其它未知error code
		StringWriter strWriter = new StringWriter();
		JAXBContext context = null;
		context = JAXBContext.newInstance(classType);
		String Xml = "";
		Marshaller objMarshaller = context.createMarshaller();
		objMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		objMarshaller.marshal(RequestObj, strWriter);
		Xml = strWriter.toString();
		return Xml;

	}
	
	public static String convertToUTF8(String s,String encode) {
        String out = null;
        try {
            out = new String(s.getBytes("UTF-8"), encode);
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return out;
    }
	

	/* 以下為GTP公告版之soap 處理 
	 * 20140310 KC
	 */
	
	//新增學校菜單  (GTP)
	@PayloadRoot(localPart = "UploadMenuRequest", namespace = TARGET_NAMESPACE)
	@ResponsePayload
	public JAXBElement<FoodResponseTypev2> handleUploadMenu(MessageContext ctx,
			@RequestPayload JAXBElement<UploadMenuRequestType> soapRquest)  throws Exception {	
		synchronized(this){
			CateringServiceEndPointv2Impl impl=new CateringServiceEndPointv2Impl();
			return impl.handleUploadMenu(ctx, soapRquest);
		}
		//System.out.println("++++++++++++++++++++++++");

		//return null;
	}
	
	
	
	//刪除學校菜單 (GTP)
	@PayloadRoot(localPart = "DeleteMenuRequest", namespace = TARGET_NAMESPACE)
	@ResponsePayload
	public JAXBElement<FoodResponseTypev2> handleDeleteMenu(MessageContext ctx,
			@RequestPayload JAXBElement<DeleteMenuRequestType> soapRquest)  throws Exception {	
		CateringServiceEndPointv2Impl impl=new CateringServiceEndPointv2Impl();
		return impl.handleDeleteMenu(ctx, soapRquest);
	}
	
	
	//新增供應商 (GTP)
	@PayloadRoot(localPart = "UploadSupplierRequest", namespace = TARGET_NAMESPACE)
	@ResponsePayload
	public JAXBElement<FoodResponseTypev2> handleUploadSupplier(MessageContext ctx,
			@RequestPayload JAXBElement<UploadSupplierRequestType> soapRquest)  throws Exception {	
		CateringServiceEndPointv2Impl impl=new CateringServiceEndPointv2Impl();
		return impl.handleUploadSupplier(ctx, soapRquest);
	}
	
	//刪除供應商 (GTP)
	@PayloadRoot(localPart = "DeleteSupplierRequest", namespace = TARGET_NAMESPACE)
	@ResponsePayload
	public JAXBElement<FoodResponseTypev2> handleDeleteSupplier(MessageContext ctx,
			@RequestPayload JAXBElement<DeleteSupplierRequestType> soapRquest)  throws Exception {	

			CateringServiceEndPointv2Impl impl=new CateringServiceEndPointv2Impl();
			return impl.handleDeleteSupplier(ctx, soapRquest);
	}
	
	//上傳食材 (GTP)
	@PayloadRoot(localPart = "UploadIngredientRequest", namespace = TARGET_NAMESPACE)
	@ResponsePayload
	public JAXBElement<FoodResponseTypev2> handleUploadIngredient(MessageContext ctx,
			@RequestPayload JAXBElement<UploadIngredientRequestType> soapRquest)  throws Exception {	
		synchronized(this){
			CateringServiceEndPointv2Impl impl=new CateringServiceEndPointv2Impl();
			return impl.handleUploadIngredient(ctx, soapRquest);
		}
	}

}
