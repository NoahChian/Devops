package org.iii.ideas.catering_service.ws.schemav2;

import java.io.File;
import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import javax.xml.namespace.QName;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.iii.ideas.catering_service.code.SourceTypeCode;
import org.iii.ideas.catering_service.common.Common;
import org.iii.ideas.catering_service.dao.Batchdata;
import org.iii.ideas.catering_service.dao.CodeDAO;
import org.iii.ideas.catering_service.dao.Dish;
import org.iii.ideas.catering_service.dao.DishBatchData;
import org.iii.ideas.catering_service.dao.DishBatchDataDAO;
import org.iii.ideas.catering_service.dao.Ingredient;
import org.iii.ideas.catering_service.dao.Ingredientbatchdata;
import org.iii.ideas.catering_service.dao.IngredientbatchdataDAO;
import org.iii.ideas.catering_service.dao.Kitchen;
import org.iii.ideas.catering_service.dao.SchoolDAO;
import org.iii.ideas.catering_service.dao.Schoolkitchen;
import org.iii.ideas.catering_service.dao.SchoolkitchenDAO;
import org.iii.ideas.catering_service.dao.SchoolkitchenId;
import org.iii.ideas.catering_service.dao.Supplier;
import org.iii.ideas.catering_service.dao.SupplierDAO;
import org.iii.ideas.catering_service.dao.SupplierId;
import org.iii.ideas.catering_service.dao.UploadFileDAO;
import org.iii.ideas.catering_service.dao.Useraccount;
import org.iii.ideas.catering_service.dao.WsLog;
import org.iii.ideas.catering_service.dao.WsLogDAO;
import org.iii.ideas.catering_service.rest.bo.FileBO;
import org.iii.ideas.catering_service.util.AuthenUtil;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;
import org.iii.ideas.catering_service.util.SchoolAndKitchenUtil;
import org.iii.ideas.catering_service.util.SystemConfiguration;
import org.iii.ideas.catering_service.util.ZipUtils;
import org.iii.ideas.catering_service.ws.schemav2.GeneticallyModifiedFoodType.GeneticallyModifiedFood;
import org.iii.ideas.catering_service.ws.schemav2.UploadIngredientRequestType.Sources;
import org.iii.ideas.catering_service.ws.schemav2.UploadIngredientRequestType.Sources.SourceItem;
import org.iii.ideas.catering_service.ws.schemav2.UploadIngredientRequestType.Sources.SourceItem.SourceInspectionFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.mime.Attachment;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.soap.SoapMessage;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

public class CateringServiceEndPointv2Impl extends CateringServiceEndPointv2 {
	private static final Logger log = LoggerFactory.getLogger(CateringServiceEndPointv2Impl.class);

	private static final String MODIFY_MENU = "modify_menu";
	private static final String ADD_MENU = "add_menu";
	private static final String DELETE_MENU = "delete_menu";
	private static final String ADD_SOURCE = "add_source";
	private static final String MODIFY_SOURCE = "modify_source";
	private static final String ADD_SUPPLIER = "add_supplier";
	private static final String MODIFY_SUPPLIER = "modify_supplier";
	private static final String DELETE_SUPPLIER = "delete_supplier";
	//員生消費合作社 add by Ellis 20150930
	private static final String ADD_SCHOOLPRODUCT = "add_schoolproduct";
	private static final String ADD_PRODUCT = "add_product";
	private static final String ADD_COMPANY = "add_company";
	private static final String MODIFY_COMPANY = "modify_company";
	private static final String ADD_SERVICEINFO = "add_serviceinfo";
	private static final String MODIFY_SERVICEINFO = "modify_serviceinfo";
	//不供餐日 add by Ellis 20150930
	private static final String ADD_NOMENUDATE = "add_nomenudate";
	private static final String MODIFY_NOMENUDATE = "modify_nomenudate";
	
	

	//將日期格式改為月和日可允許1~2個數字  20140930 KC
	//private static Pattern fmt_date = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");
	private static Pattern fmt_date = Pattern.compile("[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}");
	private List<String> errorMsg = new ArrayList<String>();
	private List<String> infoMsg = new ArrayList<String>();
	private static Integer kitchenId = 0;
	private String userPassword = "";
	private Batchdata batchdata;
	// private static List<DishBatchData> dishBatchDataList;
	private static Boolean openConsoleDebug = false;
	private static Boolean openUploaderTest = false;
	private static Boolean openSpecialPermitted = false;
	private static SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
	private static Integer loginCountyId = 0;

	// WS新增學校菜單
	public JAXBElement<FoodResponseTypev2> handleUploadMenu(MessageContext ctx, 
			@RequestPayload JAXBElement<UploadMenuRequestType> soapRquest) throws Exception {
		_printMsg("enter add_menu");
		QName qname = new QName(TARGET_NAMESPACE, "UploadMenuResponse");
		UploadMenuRequestType request = soapRquest.getValue();
		ObjectFactory objectFactory = new ObjectFactory();
		FoodResponseTypev2 response = objectFactory.createFoodResponseTypev2();
		response.setMessageId(request.getMessageId());
		Integer schoolId = 0;// Integer.valueOf((String)request.getSchoolId());
		List<Long> dishIdList = new ArrayList<Long>();

		errorMsg.clear();
		infoMsg.clear();
		// 本次處理的菜色名稱
		// HashMap<Integer,DishInfo> dishNameMap=new
		// HashMap<Integer,DishInfo>();

		// -------------------process---------------------------
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();

		// 處理附件圖檔 ： 1.解壓附件取得檔案清單 2.逐一比對菜單內容圖檔設定與檔案清單 3.更新dish內的圖檔路徑 4.移動檔案到指定位置
		String xml = this.objectToXml(UploadMenuRequestType.class, request);
		String wsfile = SystemConfiguration.writeReciveWS(request.getMessageId(), xml, request.getCompanyId() + "-" + request.getSchoolId() + "-" + request.getAction(), (new Date()), "RET");
		String attachmentStorePaths = processAttachments(ctx, FilenameUtils.getFullPath(wsfile) + "/" + request.getMessageId() + "/");
		List<String> fileList = new ArrayList<String>();
		if (!"".equals(attachmentStorePaths)) {
			fileList = CateringServiceUtil.listFiles(attachmentStorePaths);
		}

		System.out.println("ACTION: " + request.getAction().toLowerCase());

		try {
			if (ADD_MENU.equals(request.getAction().toLowerCase()) != true && MODIFY_MENU.equals(request.getAction().toLowerCase()) != true) {
				errorMsg.add("請確認Action參數內容 " + ADD_MENU + "/" + MODIFY_MENU);
				throw new Exception(StatusCode.WS_MSG_WRONG_ACTION);
			}
			
			// 共通欄位檢查
			checkCommonField(session, request.getUploaderAccount(), request.getMessageId(), request.sendTime, request.token, request.getCompanyId(), request.getServiceDate());

			String menuDate = request.getServiceDate().replace("-", "/");
			request.setServiceDate(CateringServiceUtil.converTimestampToStr("yyyy/mm/dd",CateringServiceUtil.convertStrToTimestamp("yyyy/mm/dd", menuDate)));
			
			// 檢核必填欄位Raymond 20140711
			//validateMenuFields(request);  改到下面檢核 20140925 KC
			
			// 轉換學校代碼為schoolId
			schoolId = getSchoolIdByCode(session, request.getSchoolId());
			if (schoolId == null || schoolId == 0) {
				errorMsg.add("無此學校代碼" + request.getSchoolId());
				throw new Exception(StatusCode.WS_MSG_NO_SCHOOL);
			}
			//檢查上傳日期限制  20140910 KC
			checkMenuUploadTime( session, request.getServiceDate(), schoolId);

			// 檢查學校是否屬於該廚房管轄
			if (!isSchoolExists(session, schoolId)) {
				errorMsg.add("非屬上傳廚房所轄學校" + request.getSchoolId());
				throw new Exception(StatusCode.WS_MSG_NO_SCHOOL);
			}
			
			//檢查菜單類型是否符合
//			CodeDAO cDAO = new CodeDAO(session);
//			if(CateringServiceUtil.isNull(cDAO.getCodeMsgByStatusCode(request.getMenuType(), "menuType"))){
//				errorMsg.add("不存在此菜單類型:" + request.getMenuType());
//				throw new Exception(StatusCode.WS_MSG_NO_MENUTYPE);
//			}
			
			// 菜單不可重複上傳 (順便抓出對應菜單)
			String menuMsg = checkAndGetBatchData(session, schoolId, request.getAction(), request.getServiceDate(),request.getMenuType());

			if (!menuMsg.isEmpty()) {
				throw new Exception(menuMsg);
			}

			/*--------Header區檢查結束  --------------------------*/
			if (batchdata == null) {
				batchdata = new Batchdata();
				batchdata.setMenuType(1);
//				batchdata.setMenuType(Integer.parseInt(request.getMenuType()));
				batchdata.setEnable(1);
				//20150922 增加ModifyUser資料
				batchdata.setModifyUser(request.getUploaderAccount());
			}

			if (MODIFY_MENU.equals(request.getAction().toLowerCase())) {
				_printMsg("modify menu 刪菜色資料:" + batchdata.getBatchDataId().toString());
				DishBatchDataDAO dao = new DishBatchDataDAO();
				dao.setSession(session);
				dao.deleteDishBatchDataByBatchdataId(batchdata.getBatchDataId());
			}

		
			// 存舊菜單batchdata資料 (20140529 Raymond 先不存資料)
			//setBatchDataValue(session, request, schoolId);  20140925 改到下面用新菜單陣列處理  KC

			// 新菜單陣列
			HashMap<String, DishInfo> dishbatchdataArray = new HashMap<String, DishInfo>();
			
			//將request 那16項菜色丟進菜色鎮列
			dishbatchdataArray=setBatchdataDishByDishListItem(batchdata,request);
			
			//將新的dishList物件丟進菜色陣列
			if (!CateringServiceUtil.isNull(request.getDishList())){
				dishbatchdataArray=setBatchdataAndDishbatchdataByDishListObj(request.getDishList().getDish(),dishbatchdataArray);
			}
			
			// 檢核必填欄位
			validateMenuFields(request,dishbatchdataArray);  
			
			//儲存舊菜單
			setBatchbataValueByDishArray(session,request,dishbatchdataArray,schoolId);
			
			
			// 處理新菜單表格   ---20140925 改到上面統一丟進菜色陣列
/*			
			if (!CateringServiceUtil.isNull(request.getStaple1())) {
				dishbatchdataArray.put(CateringServiceCode.DISHTYPE_MAINFOOD, request.getStaple1());
				// addDishToDishBatchData(session,batchdata.getBatchDataId(),request.getStaple1(),CateringServiceCode.DISHTYPE_MAINFOOD,fileList,attachmentStorePaths);
				// saveDishImage(session,request.getStaple1(),fileList,
				// attachmentStorePaths);
			}

			if (!CateringServiceUtil.isNull(request.getStaple2())) {
				dishbatchdataArray.put(CateringServiceCode.DISHTYPE_MAINFOOD1, request.getStaple2());
				// addDishToDishBatchData(session,batchdata.getBatchDataId(),request.getStaple2(),CateringServiceCode.DISHTYPE_MAINFOOD1,fileList,attachmentStorePaths);
			}

			if (!CateringServiceUtil.isNull(request.getMainCourse1())) {
				dishbatchdataArray.put(CateringServiceCode.DISHTYPE_MAINDISH, request.getMainCourse1());
				// addDishToDishBatchData(session,batchdata.getBatchDataId(),request.getMainCourse1(),CateringServiceCode.DISHTYPE_MAINDISH,fileList,attachmentStorePaths);
			}

			if (!CateringServiceUtil.isNull(request.getMainCourse2())) {
				dishbatchdataArray.put(CateringServiceCode.DISHTYPE_MAINDISH1, request.getMainCourse2());
				// addDishToDishBatchData(session,batchdata.getBatchDataId(),request.getMainCourse2(),CateringServiceCode.DISHTYPE_MAINDISH1,fileList,attachmentStorePaths);
			}

			if (!CateringServiceUtil.isNull(request.getMainCourse3())) {
				dishbatchdataArray.put(CateringServiceCode.DISHTYPE_MAINDISH2, request.getMainCourse3());
				// addDishToDishBatchData(session,batchdata.getBatchDataId(),request.getMainCourse3(),CateringServiceCode.DISHTYPE_MAINDISH2,fileList,attachmentStorePaths);
			}

			if (!CateringServiceUtil.isNull(request.getMainCourse4())) {
				dishbatchdataArray.put(CateringServiceCode.DISHTYPE_MAINDISH3, request.getMainCourse4());
				// addDishToDishBatchData(session,batchdata.getBatchDataId(),request.getMainCourse4(),CateringServiceCode.DISHTYPE_MAINDISH3,fileList,attachmentStorePaths);
			}

			if (!CateringServiceUtil.isNull(request.getSideDish1())) {
				dishbatchdataArray.put(CateringServiceCode.DISHTYPE_SUBDISH1, request.getSideDish1());
				// addDishToDishBatchData(session,batchdata.getBatchDataId(),request.getSideDish1(),CateringServiceCode.DISHTYPE_SUBDISH1,fileList,attachmentStorePaths);
			}

			if (!CateringServiceUtil.isNull(request.getSideDish2())) {
				dishbatchdataArray.put(CateringServiceCode.DISHTYPE_SUBDISH2, request.getSideDish2());
				// addDishToDishBatchData(session,batchdata.getBatchDataId(),request.getSideDish2(),CateringServiceCode.DISHTYPE_SUBDISH2,fileList,attachmentStorePaths);
			}

			if (!CateringServiceUtil.isNull(request.getSideDish3())) {
				dishbatchdataArray.put(CateringServiceCode.DISHTYPE_SUBDISH3, request.getSideDish3());
				// addDishToDishBatchData(session,batchdata.getBatchDataId(),request.getSideDish3(),CateringServiceCode.DISHTYPE_SUBDISH3,fileList,attachmentStorePaths);
			}

			if (!CateringServiceUtil.isNull(request.getSideDish4())) {
				dishbatchdataArray.put(CateringServiceCode.DISHTYPE_SUBDISH4, request.getSideDish4());
				// addDishToDishBatchData(session,batchdata.getBatchDataId(),request.getSideDish4(),CateringServiceCode.DISHTYPE_SUBDISH4,fileList,attachmentStorePaths);
			}

			if (!CateringServiceUtil.isNull(request.getSideDish5())) {
				dishbatchdataArray.put(CateringServiceCode.DISHTYPE_SUBDISH5, request.getSideDish5());
				// addDishToDishBatchData(session,batchdata.getBatchDataId(),request.getSideDish5(),CateringServiceCode.DISHTYPE_SUBDISH5,fileList,attachmentStorePaths);
			}

			if (!CateringServiceUtil.isNull(request.getSideDish6())) {
				dishbatchdataArray.put(CateringServiceCode.DISHTYPE_SUBDISH6, request.getSideDish6());
				// addDishToDishBatchData(session,batchdata.getBatchDataId(),request.getSideDish6(),CateringServiceCode.DISHTYPE_SUBDISH6,fileList,attachmentStorePaths);
			}

			if (!CateringServiceUtil.isNull(request.getVegetable())) {
				dishbatchdataArray.put(CateringServiceCode.DISHTYPE_VEGETABLE, request.getVegetable());
				// addDishToDishBatchData(session,batchdata.getBatchDataId(),request.getVegetable(),CateringServiceCode.DISHTYPE_VEGETABLE,fileList,attachmentStorePaths);
			}

			if (!CateringServiceUtil.isNull(request.getSoup())) {
				dishbatchdataArray.put(CateringServiceCode.DISHTYPE_SOUP, request.getSoup());
				// addDishToDishBatchData(session,batchdata.getBatchDataId(),request.getSoup(),CateringServiceCode.DISHTYPE_SOUP,fileList,attachmentStorePaths);
			}

			if (!CateringServiceUtil.isNull(request.getExtra1())) {
				dishbatchdataArray.put(CateringServiceCode.DISHTYPE_DESSERT, request.getExtra1());
				// addDishToDishBatchData(session,batchdata.getBatchDataId(),request.getExtra1(),CateringServiceCode.DISHTYPE_DESSERT,fileList,attachmentStorePaths);
			}

			if (!CateringServiceUtil.isNull(request.getExtra2())) {
				dishbatchdataArray.put(CateringServiceCode.DISHTYPE_DESSERT1, request.getExtra2());
				// addDishToDishBatchData(session,batchdata.getBatchDataId(),request.getExtra2(),CateringServiceCode.DISHTYPE_DESSERT1,fileList,attachmentStorePaths);
			}
*/
			// 20140529 Raymond 先不處理菜色資料 移至新增菜單後處理
			// Integer dishbatchdataId=0;
			// for(String key:dishbatchdataArray.keySet()){
			// if (openSpecialPermitted){ //台北市就先主動存dishbatchdata
			// dishbatchdataId=addDishToDishBatchData(session,batchdata.getBatchDataId(),dishbatchdataArray.get(key),key,fileList,attachmentStorePaths);
			// dishIdList.add(dishbatchdataId);
			// }else{ //其他縣市正常存，由batchdata.save同時處理dishbatchdata
			// saveDishImage(session,dishbatchdataArray.get(key),fileList,
			// attachmentStorePaths);
			// }
			// }

			if (ADD_MENU.equals(request.getAction().toLowerCase())) {
				// Raymond 20140523 增加參數saveIngredientbatchdata = false
				// 判斷是否需要新增菜色對應食材

				try {
					HibernateUtil.saveBatchdata(session, batchdata, false);
				} catch (Exception ex) {
					errorMsg.add(ex.getMessage());
					if (openConsoleDebug){
						ex.printStackTrace();
					}
					if (ex.getMessage().contains("菜單已存在")) {
						throw new Exception(StatusCode.WS_MSG_MENU_DUPLICATE);
					} else {
						System.out.println(ex.getMessage());
						throw new Exception(StatusCode.WS_MSG_OTHER);
						
					}

				}

				// HibernateUtil.saveBatchdata(session, batchdata);
				// Integer newId=batchdata.getBatchDataId();
				// 存batchdata之後，取得batchid再存dishbatchdata表
				// dishBatchDataList.add(new DishBatchData( 0, newId,
				// request.getStaple1(),"MainFoodId", QueryDishIdByName(session,
				// kitchenId, reqdishInfo), uploadDateTime, dishShowName));

			} else {
				if (openSpecialPermitted) {// 台北市的話要處理update的刪除部分
					HibernateUtil.deleteDishbatchdataNotInDishlist(session, batchdata.getBatchDataId(), dishIdList);
				}
				// Raymond 20140523 增加參數saveIngredientbatchdata = false
				// 判斷是否需要新增菜色對應食材
				HibernateUtil.updateBatchdata(session, batchdata, false);

				// HibernateUtil.updateBatchdata(session, batchdata);
			}

			// 往下移至新增完batchdata 後才處理菜色圖檔
			Long dishbatchdataId = (long) 0;
			for (String key : dishbatchdataArray.keySet()) {
				if (openSpecialPermitted) { // 台北市就先主動存dishbatchdata
					dishbatchdataId = addDishToDishBatchData(session, batchdata.getBatchDataId(), dishbatchdataArray.get(key), key, fileList, attachmentStorePaths);
					dishIdList.add(dishbatchdataId);
				} else { // 其他縣市正常存，由batchdata.save同時處理dishbatchdata
					saveDishImage(session, dishbatchdataArray.get(key), fileList, attachmentStorePaths);
				}
			}

			if (openSpecialPermitted) {
				batchdata.setSrcType("A");
			}

			// -------------------process---------------------------
			if (!openUploaderTest)
				tx.commit();

			setResponseData(session, response, request.getMessageId(), request.getSendTime(), request.getCompanyId(), StatusCode.WS_MSG_SUCCESS, listToString(errorMsg), request.getAction());
//			session.close();
			// unlinkFile(attachmentStorePaths);
			return new JAXBElement<FoodResponseTypev2>(qname, FoodResponseTypev2.class, response);
		} catch (Exception e) {
			if (openConsoleDebug) {
				e.printStackTrace();
			}

			if (!tx.wasRolledBack()) {
				tx.rollback();
			}

			setResponseData(session, response, request.getMessageId(), request.getSendTime(), request.getCompanyId(), e.getMessage(), listToString(errorMsg), request.getAction());
//			session.close();
			// unlinkFile(attachmentStorePaths);
			return new JAXBElement<FoodResponseTypev2>(qname, FoodResponseTypev2.class, response);
		}finally{
			session.close();
		}

	}

	// WS刪除學校菜單
	public JAXBElement<FoodResponseTypev2> handleDeleteMenu(MessageContext ctx, @RequestPayload JAXBElement<DeleteMenuRequestType> soapRquest) throws Exception {
		DeleteMenuRequestType request = soapRquest.getValue();
		ObjectFactory objectFactory = new ObjectFactory();

		FoodResponseTypev2 response = objectFactory.createFoodResponseTypev2();
		response.setMessageId(request.getMessageId());
		QName qname = new QName(TARGET_NAMESPACE, "DeleteMenuResponse");

		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		errorMsg.clear();
		infoMsg.clear();
		try {

			String xml = this.objectToXml(DeleteMenuRequestType.class, request);
			String xmlwsfile = SystemConfiguration.writeReciveWS(request.getMessageId(), xml, request.getCompanyId() + "-" + request.getSchoolId() + "-" + request.getAction(), (new Date()), "RET");

			if (!DELETE_MENU.equals(request.getAction().toLowerCase())) {
				errorMsg.add("請確認Action參數內容");
				throw new Exception(StatusCode.WS_MSG_WRONG_ACTION);
			}

//			Integer schoolId = Integer.valueOf(request.getSchoolId()); 因幼兒園會有英文字母，故直接投入下列getSchoolIdByCode函數進行轉換，不再先取出。 modify by Ellis 20150511

			// 共通欄位檢查
			checkCommonField(session, request.getUploaderAccount(), request.getMessageId(), request.sendTime, request.token, request.getCompanyId(), request.getServiceDate());

			// 轉換學校代碼為schoolId 
			Integer schoolId = getSchoolIdByCode(session, request.getSchoolId()); 
			if (schoolId == null || schoolId == 0) {
				errorMsg.add("無此學校代碼" + request.getSchoolId());
				throw new Exception(StatusCode.WS_MSG_NO_SCHOOL);
			}

			//檢查上傳日期限制  20140910 KC
			checkMenuUploadTime( session, request.getServiceDate(), schoolId);
			
			String menuDate = request.getServiceDate().replace("-", "/");
			request.setServiceDate(CateringServiceUtil.converTimestampToStr("yyyy/mm/dd",CateringServiceUtil.convertStrToTimestamp("yyyy/mm/dd", menuDate)));
			menuDate=request.getServiceDate();
			// 檢查學校是否屬於該廚房管轄
			if (!isSchoolExists(session, schoolId)) {
				errorMsg.add("團膳業者/自立廚房 未設定此供餐學校 : " + request.getSchoolId());
				throw new Exception(StatusCode.WS_MSG_NO_SCHOOL);
			}

			// 先檢查是否存在
			String menuMsg = checkAndGetBatchData(session, schoolId, request.getAction(), request.getServiceDate(),request.getMenuType());
			if (!menuMsg.isEmpty()) {
				errorMsg.add("菜單日期:" + request.getServiceDate() + " ,學校代碼:" + request.getSchoolId() + ",無此菜單");
				throw new Exception(StatusCode.WS_MSG_MENU_NO_EXIST);
			}

			//String menuDate = request.getServiceDate().replace("-", "/");// 欲刪除菜單之日期，格式為YYYY-MM-DD範例：2013-01-01
			String lotNum = CateringServiceUtil.defaultLotNumber;// 有問題

			// 刪菜單也要刪食材
			HibernateUtil.deleteBatchdataByUK(session, kitchenId, schoolId, menuDate, lotNum);
//			HibernateUtil.deleteBatchdataByUKv2(session, kitchenId, schoolId, menuDate, lotNum,request.getMenuType());
			HibernateUtil.deleteIngredientbatchdataByBatchdataId(session, batchdata.getBatchDataId());

			/*
			 * //刪新菜單菜色 20140408改由HibernateUtil統一執行
			 * _printMsg("modify menu 刪菜色資料:"
			 * +batchdata.getBatchDataId().toString()); DishBatchDataDAO dao=new
			 * DishBatchDataDAO(); dao.setSession(session);
			 * dao.deleteDishBatchDataByBatchdataId(batchdata.getBatchDataId());
			 */
			if (!openUploaderTest)
				tx.commit();

			setResponseData(session, response, request.getMessageId(), request.getSendTime(), request.getCompanyId(), StatusCode.WS_MSG_SUCCESS, "", request.getAction());


			return new JAXBElement<FoodResponseTypev2>(qname, FoodResponseTypev2.class, response);
		} catch (Exception ex) {
			if (openConsoleDebug) {
				ex.printStackTrace();
			}

			System.out.println(ex.getMessage());
			setResponseData(session, response, request.getMessageId(), request.getSendTime(), request.getCompanyId(), ex.getMessage(), listToString(errorMsg), request.getAction());
			

			return new JAXBElement<FoodResponseTypev2>(qname, FoodResponseTypev2.class, response);
		}finally{
			session.close();
		}

	}

	// WS新增、更新食材
	public JAXBElement<FoodResponseTypev2> handleUploadIngredient(MessageContext ctx, @RequestPayload JAXBElement<UploadIngredientRequestType> soapRquest) throws Exception {
		UploadIngredientRequestType request = soapRquest.getValue();
		ObjectFactory objectFactory = new ObjectFactory();
		FoodResponseTypev2 response = objectFactory.createFoodResponseTypev2();
		response.setMessageId(request.getMessageId());

		// Integer schoolId = Integer.valueOf(request.getSchoolId());
		QName qname = new QName(TARGET_NAMESPACE, "UploadIngredientResponse");
		errorMsg.clear();
		infoMsg.clear();

		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			// 轉換學校代碼為schoolId
			Integer schoolId = getSchoolIdByCode(session, request.getSchoolId());
			if (schoolId == null || schoolId == 0) {
				errorMsg.add("無此學校代碼" + request.getSchoolId());
				throw new Exception(StatusCode.WS_MSG_NO_SCHOOL);
			}

			// 抓附件檔案(檢驗報告)
			String xml = this.objectToXml(UploadIngredientRequestType.class, request);
			String wsfile = SystemConfiguration.writeReciveWS(request.getMessageId(), xml, request.getCompanyId() + "-" + request.getSchoolId() + "-" + request.getAction(), (new Date()), "RET");
			String attachmentStorePaths = processAttachments(ctx, FilenameUtils.getFullPath(wsfile) + "/" + request.getMessageId() + "/");
			List<String> fileList = new ArrayList<String>();
			if (!"".equals(attachmentStorePaths)) {
				fileList = CateringServiceUtil.listFiles(attachmentStorePaths);
			}
			
			//檢查上傳日期限制  20140910 KC
			checkMenuUploadTime( session, request.getServiceDate(), schoolId);
			
			


			if (ADD_SOURCE.equals(request.getAction().toLowerCase()) != true && MODIFY_SOURCE.equals(request.getAction().toLowerCase()) != true) {
				errorMsg.add("請確認Action參數內容 " + ADD_SOURCE + "/" + MODIFY_SOURCE);
				throw new Exception(StatusCode.WS_MSG_WRONG_ACTION);
			}
			// 共通欄位檢查
			checkCommonField(session, request.getUploaderAccount(), request.getMessageId(), request.sendTime, request.token, request.getCompanyId(), request.getServiceDate());

			// 必填欄位檢查
			validateIngredientFields(request);
			

			// 檢查學校是否屬於該廚房管轄
			if (!isSchoolExists(session, schoolId)) {
				throw new Exception(StatusCode.WS_MSG_NO_SCHOOL);
			}
			//String menuDate = request.getServiceDate().replace("-", "/");

			//處理菜單日期格式
			String menuDate = request.getServiceDate().replace("-", "/");
			request.setServiceDate(CateringServiceUtil.converTimestampToStr("yyyy/mm/dd",CateringServiceUtil.convertStrToTimestamp("yyyy/mm/dd", menuDate)));
			menuDate=request.getServiceDate();
			
			
			// 檢查是否有對應的菜單存在
			Batchdata batchdata = HibernateUtil.queryBatchdataByUK(session, kitchenId, schoolId, menuDate, CateringServiceUtil.defaultLotNumber);
//			Batchdata batchdata = HibernateUtil.queryBatchdataByUKv2(session, kitchenId, schoolId, menuDate, CateringServiceUtil.defaultLotNumber,request.getMenuType());
			
			if (batchdata == null) {
				errorMsg.add("找不到學校ID:" + request.getSchoolId() + " 日期:" + menuDate + " 的菜單 團膳代碼:" + kitchenId + " 自設/團膳:");
				throw new Exception(StatusCode.WS_MSG_MENU_NO_EXIST);
			}
			// ----處理收到的食材資料------------
			Sources sources = request.getSources();
			List<SourceItem> sourceItemList = sources.getSourceItem();
			Iterator<SourceItem> sourceItemIterator = sourceItemList.iterator();

			// 找出菜單內的所有菜色名稱
			HashMap<Long, String> dishNameMap = new HashMap<Long, String>();
			if (batchdata.getMainFoodId() != 0) { // 正常資料
				List<Long> dishIdArray = new ArrayList<Long>();
				dishIdArray = HibernateUtil.getDishIdByBatchdata(batchdata);
				Iterator<Long> iterator = dishIdArray.iterator();
				while (iterator.hasNext()) {
					Long dishId = iterator.next();
					if (dishId == 0) {
						continue;
					}
					dishNameMap.put(dishId, HibernateUtil.queryDishNameById(session, dishId));
				}

				// 特別加入調味料
				Long seasonDishId = HibernateUtil.queryDishIdByName(session, kitchenId, CateringServiceUtil.ColumnNameOfSeasoning);
				if (seasonDishId != null && seasonDishId != 0) {
					dishNameMap.put(seasonDishId, CateringServiceUtil.ColumnNameOfSeasoning);
				}

			} else { // 從新菜單中找菜名
				DishBatchDataDAO dishbatchdatadao = new DishBatchDataDAO();
				dishbatchdatadao.setSession(session);
				List<DishBatchData> dishlist = dishbatchdatadao.getDishBatchDataByBatchId(batchdata.getBatchDataId());
				Iterator<DishBatchData> ir = dishlist.iterator();
				while (ir.hasNext()) {
					DishBatchData row = ir.next();
					dishNameMap.put(row.getDishBatchDataId(), row.getDishName());
				}
				dishNameMap.put((long) 0, CateringServiceUtil.ColumnNameOfSeasoning);
			}

			List<String> sourceDishNameArray = new ArrayList<String>();
			List<Ingredientbatchdata> sourceDataArray = new ArrayList<Ingredientbatchdata>();
			HashMap<String, String> attachFileMap = new HashMap<String, String>();
			while (sourceItemIterator.hasNext()) {
				SourceItem sourceItem = sourceItemIterator.next();
				// List<School> schoolList =
				// HibernateUtil.querySchoolListByKitchenId(session, kitchenId);
				// Dish dish = HibernateUtil.queryDishByName(session, kitchenId,
				// sourceItem.getDishesName());

				if (!CateringServiceUtil.isEmpty(sourceItem.getDishesName())) {
					if (!dishNameMap.containsValue(sourceItem.getDishesName()) && !sourceItem.getDishesName().equals(CateringServiceUtil.ColumnNameOfSeasoning)) {
						errorMsg.add("菜單無對應的菜色名稱:" + sourceItem.getDishesName());
						continue;
					} else {
						sourceDishNameArray.add(sourceItem.getDishesName());
					}
				}

				String expirationDate = "";
				String stockDate = "";
				String manufactureDate = "";
				String lotNumOfIngredient = CateringServiceUtil.defaultLotNumber; // 預設批號

				// 若有錯誤的食材就註記錯誤訊息，最後再統一拋回敘述
				try {
					if (!CateringServiceUtil.isEmpty(sourceItem.getExpirationDate()) && isDateFormatCorrect(sourceItem.getExpirationDate())) {
						expirationDate = sourceItem.getExpirationDate().replace("-", "/");// 有效日期
					}

					if (!CateringServiceUtil.isEmpty(sourceItem.getManufactureDate()) && isDateFormatCorrect(sourceItem.getManufactureDate())) {
						manufactureDate = sourceItem.getManufactureDate().replace("-", "/");// 製造日期
					}
				} catch (Exception ex) {
					errorMsg.add(ex.getMessage() + "ExpirationDate或ManufactureDate進貨日期格式錯誤");
					continue;
				}
				if (!CateringServiceUtil.isEmpty(sourceItem.getStockDate()) && isDateFormatCorrect(sourceItem.getStockDate())) {
					stockDate = sourceItem.getStockDate().replace("-", "/");// 進貨日期(必填)
				} else {
					if (!CateringServiceUtil.ColumnNameOfSeasoning.equals(sourceItem.getDishesName())) {
						errorMsg.add(sourceItem.getDishesName() + "-" + sourceItem.getSourceName() + "  未填進貨日期或進貨日期格式錯誤(必填欄位)");
						continue;
					}
				}

				if (CateringServiceUtil.isEmpty(sourceItem.getSupplierId())) { // 供應商(必填)
					errorMsg.add(sourceItem.getDishesName() + "-" + sourceItem.getSourceName() + "  未填供應商(必填欄位)");
					continue;
				}

				if (CateringServiceUtil.isEmpty(sourceItem.getSource())) { // 來源(必填)
					errorMsg.add(sourceItem.getDishesName() + " 未填來源(必填欄位)");
					continue;
				}

				if (CateringServiceUtil.isEmpty(sourceItem.getBrands())) { // 品牌(非必填，但不填會出錯)
					sourceItem.setBrands("");
				}

				if (CateringServiceUtil.isEmpty(sourceItem.getBrandsBAN())) { // 品牌(非必填，但不填會出錯)
					sourceItem.setBrandsBAN("");
				}
				
				//----新增欄位  20140919 KC-------
				// BrandsBAN->ManufacturerBAN 往前相容 20140919 KC
				if (CateringServiceUtil.isEmpty(sourceItem.getManufacturerBAN())) { // 品牌(非必填，但不填會出錯)
					sourceItem.setManufacturerBAN("");
				}


				if (CateringServiceUtil.isEmpty(sourceItem.getProductName())) { // 商品名稱(非必填，但不填會出錯)
					sourceItem.setProductName("");
				}
				
				if (CateringServiceUtil.isEmpty(sourceItem.getManufacturer())) { // 製造商名稱(非必填，但不填會出錯)
					sourceItem.setManufacturer("");
				}
				
				if (CateringServiceUtil.isEmpty(sourceItem.getProcessedFood())) { // 加工食品(非必填，但不填會出錯)
					sourceItem.setProcessedFood("N");
				}
				
				if (CateringServiceUtil.isNull(sourceItem.getIngredientQuantity())) { // 食材數量(非必填，但不填會出錯)
					sourceItem.setIngredientQuantity(BigDecimal.valueOf(0));
				}
				
				if (CateringServiceUtil.isEmpty(sourceItem.getIngredientUnit())) { //食材單位 (非必填，但不填會出錯)
					sourceItem.setIngredientUnit("KG");
				}

				
				//---------------

				
				
				if (CateringServiceUtil.isEmpty(sourceItem.getOrigin())) { // 產地(非必填)
					sourceItem.setOrigin("");
				}

				if (CateringServiceUtil.isEmpty(sourceItem.getLotNumber())) { // 批次(非必填)
					sourceItem.setLotNumber(lotNumOfIngredient);
				}

				// -------認證標章與號碼的對應判斷-----
				if (CateringServiceUtil.isEmpty(sourceItem.getSourceCertification())) { // 標章名稱(非必填)
					sourceItem.setSourceCertification("");
				}

				if (CateringServiceUtil.isEmpty(sourceItem.getCertificationID())) { // 標章號碼(非必填)
					sourceItem.setCertificationID("");
				}
				// 認證標章與號碼全有全無XOR
				if (sourceItem.getCertificationID().isEmpty() ^ sourceItem.getSourceCertification().isEmpty()) {
					errorMsg.add("食材認證與認證編號必須同時填寫，不可缺一");
					continue;
				}

				Supplier supplier;
				Integer supplierId = 0;
				String supplierCompanyId = sourceItem.getSupplierId();

				// 台北市特殊處理: dishId設定為dishbatchdataId ,ingredientId設為0
				Long ingredientId = (long) 0;
				Long ingredientDishId = (long) 0;

				// ---- 基本菜色食材資料的自動處理 start --------------------
				if (!openSpecialPermitted) {
					supplier = HibernateUtil.querySupplierByCompanyId(session, kitchenId, sourceItem.getSupplierId());
					if (supplier == null) {
						errorMsg.add(sourceItem.getDishesName() + "-" + sourceItem.getSourceName() + ",供應商編號:" + sourceItem.getSupplierId() + " 未登錄於系統");
						continue;
					}
					supplierCompanyId = supplier.getCompanyId();
					supplierId = supplier.getId().getSupplierId();

					Long dishId = HibernateUtil.queryDishIdByName(session, kitchenId, sourceItem.getDishesName());

					if (dishId == null) {
						// 特別處理調味料，如果是調味料就自動新增菜色到dish
						if (CateringServiceUtil.ColumnNameOfSeasoning.equals(sourceItem.getDishesName())) {
							Dish dish = new Dish();
							// seasoning.setDishId(dishId);
							dish.setDishName(sourceItem.getDishesName());
							dish.setKitchenId(kitchenId);
							dish.setPicturePath("");
							session.save(dish);
							dishId = dish.getDishId();
							if (!dishNameMap.containsKey(dishId)) {
								dishNameMap.put(dishId, CateringServiceUtil.ColumnNameOfSeasoning);
							}
						} else {
							errorMsg.add(sourceItem.getDishesName() + "-" + sourceItem.getSourceName() + "找不到對應的菜色");
							continue;
						}
					}

					Ingredient ingredient = HibernateUtil.queryIngredientByName(session, dishId, sourceItem.getSourceName());

					// 食材資料庫不存在時，直接新增；否則就修改
					if (ingredient == null) {
						ingredient = HibernateUtil.saveIngredient(session, dishId, sourceItem.getSourceName(), sourceItem.getBrands(), supplierId, supplierCompanyId);
					} else {
						ingredient.setBrand(sourceItem.getBrands());
						
						
						//改欄位名稱  BrandsBAN->ManufacturerBAN 作相容性 20140919 KC
						ingredient.setBrandNo(sourceItem.getBrandsBAN());
						ingredient.setBrandNo(sourceItem.getManufacturerBAN());
						
						ingredient.setSupplierId(supplierId);
						ingredient.setSupplierCompanyId(supplierCompanyId);
						session.update(ingredient);
					}

					// 若有基本資料存在，就存基本資料的id
					ingredientId = ingredient.getIngredientId();
					ingredientDishId = ingredient.getDishId();
				} else { // 台北市特殊處理，dish表中找不到dishid的 改由dishbatchdataid取代
							// 調味料dishId=0
					if (sourceItem.getDishesName().equals(CateringServiceUtil.ColumnNameOfSeasoning)) {
						ingredientDishId = (long) 0;
					} else {
						for (Long dishbatchdataId : dishNameMap.keySet()) {
							if (dishNameMap.get(dishbatchdataId).equals(sourceItem.getDishesName())) {
								ingredientDishId = dishbatchdataId;
							}
						}
					}

				}
				// ---- 基本菜色食材資料的自動處理 end --------------------
				if (!CateringServiceUtil.isEmpty(sourceItem.getLotNumber())) {
					lotNumOfIngredient = sourceItem.getLotNumber();
				}

				// 食材通通用新增的，不修改，舊的存之前直接刪
				Ingredientbatchdata ingredientbatchdata = new Ingredientbatchdata();
				ingredientbatchdata.setBatchDataId(batchdata.getBatchDataId());

				ingredientbatchdata.setDishId(ingredientDishId);
				ingredientbatchdata.setIngredientId(ingredientId);
				ingredientbatchdata.setIngredientName(sourceItem.getSourceName());

				ingredientbatchdata.setBrand(sourceItem.getBrands());
				//原本的brandsBAN->ManufacturerBAN  20140919 KC
				ingredientbatchdata.setBrandNo(sourceItem.getBrandsBAN());
				ingredientbatchdata.setBrandNo(sourceItem.getManufacturerBAN());
				
				if (!expirationDate.isEmpty()) {
					ingredientbatchdata.setExpirationDate(CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", expirationDate));
				}

				if (!manufactureDate.isEmpty()) {
					ingredientbatchdata.setManufactureDate(CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", manufactureDate));
				}
				if (!stockDate.isEmpty()) {
					ingredientbatchdata.setStockDate(CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", stockDate));
				}
				ingredientbatchdata.setLotNumber(lotNumOfIngredient);
				ingredientbatchdata.setOrigin(sourceItem.getOrigin());
				ingredientbatchdata.setSource(sourceItem.getSource());

				ingredientbatchdata.setSourceCertification(sourceItem.getSourceCertification());
				ingredientbatchdata.setCertificationId(sourceItem.getCertificationID());

				ingredientbatchdata.setSupplierCompanyId(sourceItem.getSupplierId());
				ingredientbatchdata.setSupplierName(sourceItem.getSupplierName());
				ingredientbatchdata.setSupplierId(supplierId);

				//品名 製造商 新增欄位 20140919KC
				ingredientbatchdata.setProductName(sourceItem.getProductName());
				ingredientbatchdata.setManufacturer(sourceItem.getManufacturer());
				
				ingredientbatchdata.setIngredientQuantity(sourceItem.getIngredientQuantity().toString());
				ingredientbatchdata.setIngredientUnit(sourceItem.getIngredientUnit());
				
				
				
				//設定基改屬性 (新增欄位) 20140919 KC
				Integer foodProperty=0; //食材屬性值
				
				List<GeneticallyModifiedFood> geneList=new ArrayList<GeneticallyModifiedFood>();
				if (!CateringServiceUtil.isNull(sourceItem.getGeneticallyModifiedFoodList())){
					GeneticallyModifiedFoodType geneObj=sourceItem.getGeneticallyModifiedFoodList();
					if (!CateringServiceUtil.isNull(geneObj)){
						geneList =geneObj.getGeneticallyModifiedFood();
					}
					for (GeneticallyModifiedFood geneItem:geneList){
						if (FoodPropertyCode.WS_FOODCODE_GENE_List.containsKey(geneItem.getFoodCode())){
							foodProperty+=FoodPropertyCode.WS_FOODCODE_GENE_List.get(geneItem.getFoodCode());
						}
					}
				}
				//--加工品
				if (sourceItem.getProcessedFood().toUpperCase().equals("Y")){
					foodProperty+=CateringServiceCode.INGREDIENT_ATTR_PSFOOD;
				}
				ingredientbatchdata.setIngredientAttr(foodProperty);
				
				// session.save(ingredientbatchdata);
				sourceDataArray.add(ingredientbatchdata);

				// -----檢驗報告處理----------
				if (!CateringServiceUtil.isNull(sourceItem.getSourceInspectionFile())) {
					SourceInspectionFile sourceInspectionFileList = sourceItem.getSourceInspectionFile();// 食材檢驗報告集

					if (!CateringServiceUtil.isEmpty(sourceInspectionFileList.getSourceInspectionFileName())) {
						String inspectionFile = sourceInspectionFileList.getSourceInspectionFileName();// 食材檢驗報告檔案名稱

						if (fileList.contains(inspectionFile)) {
							attachFileMap.put(ingredientDishId.toString() + "-" + ingredientId.toString(), inspectionFile);
						} else {
							// log.info("WS 找不到上傳之檔案:"+Paths.get(attachmentStorePaths,
							// inspectionFile));
							// System.out.println("WS 找不到上傳之檔案:"+Paths.get(attachmentStorePaths,
							// inspectionFile));
						}
					}
				}

			}// -------------食材迴圈結束-----------

			if (errorMsg.size() > 0) {
				errorMsg.add("bid:" + batchdata.getBatchDataId().toString());
				_printMsg(errorMsg.toString());
				throw new Exception(StatusCode.WS_MSG_INGREDIENT_ERROR);
			} else { // 沒問題就可以先刪舊再存檔
				Iterator<String> irDish = sourceDishNameArray.iterator();
				while (irDish.hasNext()) {
					String irDishName = irDish.next();
					Long idishId = (Long) findHashmapKeyByValue(dishNameMap, irDishName);
					if (idishId == null) { // 有問題 不可能
						_printMsg("食材上傳: 找不到菜單內有對應的菜色(2)" + irDishName);
						continue;
					}
					HibernateUtil.deleteIngredientbatchdataByDishId(session, batchdata.getBatchDataId(), idishId);
				}
				Iterator<Ingredientbatchdata> irSource = sourceDataArray.iterator();
				while (irSource.hasNext()) {
					Ingredientbatchdata ingdata = irSource.next();
					session.save(ingdata);
					String inspectionFileName = attachFileMap.get(ingdata.getDishId() + "-" + ingdata.getIngredientId());
//					saveInspectionFile(attachmentStorePaths, inspectionFileName, fileList, kitchenId, ingdata.getIngredientBatchId());
					if(inspectionFileName!=null&&!CateringServiceUtil.isEmpty(inspectionFileName))
						saveInspectionFileV2(session,attachmentStorePaths, inspectionFileName,ingdata.getIngredientBatchId(),batchdata.getMenuDate(),ingdata.getIngredientId(),ingdata.getSupplierCompanyId(),ingdata.getLotNumber(),HibernateUtil.converTimestampToStr("yyyy/MM/dd", ingdata.getStockDate()));
				}

			}

			if (!openUploaderTest)
				tx.commit();
			// tx.commit();
			setResponseData(session, response, request.getMessageId(), request.getSendTime(), request.getCompanyId(), StatusCode.WS_MSG_SUCCESS, "", request.getAction());
//			session.close();
			// unlinkFile(attachmentStorePaths);
			return new JAXBElement<FoodResponseTypev2>(qname, FoodResponseTypev2.class, response);
		} catch (Exception e) {
			if (openConsoleDebug) {
				e.printStackTrace();
			}
			e.printStackTrace();
			if (!tx.wasRolledBack()) {
				tx.rollback();
			}

			setResponseData(session, response, request.getMessageId(), request.getSendTime(), request.getCompanyId(), e.getMessage(), listToString(errorMsg), request.getAction());
//			session.close();
			// unlinkFile(attachmentStorePaths);
			return new JAXBElement<FoodResponseTypev2>(qname, FoodResponseTypev2.class, response);
		}finally{
			session.close();
		}

	}

	// WS上傳供應商
	public JAXBElement<FoodResponseTypev2> handleUploadSupplier(MessageContext ctx, @RequestPayload JAXBElement<UploadSupplierRequestType> soapRquest) throws Exception {

		UploadSupplierRequestType request = soapRquest.getValue();
		ObjectFactory objectFactory = new ObjectFactory();

		FoodResponseTypev2 response = objectFactory.createFoodResponseTypev2();
		response.setMessageId(request.getMessageId());
		QName qname = new QName(TARGET_NAMESPACE, "UploadSupplierResponse");

		SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();

		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		errorMsg.clear();
		infoMsg.clear();
		try {

			String xml = this.objectToXml(UploadSupplierRequestType.class, request);
			String xmlwsfile = SystemConfiguration.writeReciveWS(request.getMessageId(), xml, request.getCompanyId() + "-" + request.getSupplierId() + "-" + request.getAction(), (new Date()), "RET");

			if (ADD_SUPPLIER.equals(request.getAction().toLowerCase()) != true && MODIFY_SUPPLIER.equals(request.getAction().toLowerCase()) != true) {
				errorMsg.add("請確認Action參數內容 " + ADD_SUPPLIER + "/" + MODIFY_SUPPLIER);
				throw new Exception(StatusCode.WS_MSG_WRONG_ACTION);
			}

			// 共通欄位檢查
			checkCommonField(session, request.getUploaderAccount(), request.getMessageId(), request.sendTime, request.token, request.getCompanyId(), null);

			// 檢查是否為特殊帳號 不可進行動作
			if (openSpecialPermitted) {
				errorMsg.add("您的帳號無權登錄供應商資料");
				throw new Exception(StatusCode.WS_MSG_OTHER);
			}

			Supplier supplier = null;
			Supplier supplier2 = null;

			String pCompanyId = "";
			String pSupplierId = "";
			String pSupplierName = "";
			String pOwner = "";
			String pAddress = "";
			String pSupplierTel = "";

			String pCertification = ""; // 已取消必填

			if (!CateringServiceUtil.isEmpty(request.getSupplierId())) {
				pSupplierId = request.getSupplierId();
			}

			if (!CateringServiceUtil.isEmpty(request.getCompanyId())) {
				pCompanyId = request.getCompanyId().trim();
			}

			if (!CateringServiceUtil.isEmpty(request.getOwner())) {
				pOwner = request.getOwner().trim();
			}

			if (!CateringServiceUtil.isEmpty(request.getSupplierAddress())) {
				pAddress = request.getSupplierAddress().trim();
			}

			if (!CateringServiceUtil.isEmpty(request.getSupplierTel())) {
				pSupplierTel = request.getSupplierTel().trim();
			}

			if (!CateringServiceUtil.isEmpty(request.getSupplierCertification())) {
				pCertification = request.getSupplierCertification().trim();
			}

			if (!CateringServiceUtil.isEmpty(request.getSupplierName())) {
				pSupplierName = request.getSupplierName().trim();
			}

			// 先檢查是否存在
			SupplierDAO dao = new SupplierDAO();
			// dao.setSessionFactory(sessionFactory);
			dao.setSession(session);
			if ((!dao.isUniqSupplier(kitchenId, pSupplierId, pSupplierName)) && ADD_SUPPLIER.equals(request.getAction().toLowerCase())) {
				errorMsg.add("供應商名稱: " + pSupplierName + ",統編:" + pSupplierId + " 已存在於資料庫(名稱與統編皆不得重複)");
				throw new Exception(StatusCode.WS_MSG_SUPPLIER_DUPLICATE);
			}

			if (ADD_SUPPLIER.equals(request.getAction().toLowerCase())) {
				supplier = new Supplier();
				supplier.setId(new SupplierId(0, kitchenId));
				supplier.setSupplierName(pSupplierName);
			} else {
				// List supplierList= dao.findBySupplierName(pSupplierName);
				// List supplierList=dao.findByCompanyId(pSupplierId);
				// 20140529 Raymond 搜尋supplier多一個條件 kitchenId
				Supplier tmpSupplier = dao.querySupplierByCompanyId(pSupplierId, kitchenId);
				if (tmpSupplier != null) {
					supplier = tmpSupplier;
				} else {
					errorMsg.add("供應商名稱: " + pSupplierName + ",統編:" + pSupplierId + "不存在，無法修改，請先新增資料");
					throw new Exception(StatusCode.WS_MSG_SUPPLIER_NO_EXIST);
				}
			}

			supplier.setAreaId(0); // 供應商areaId去哪抓? 跨區供應商問題? KC
			supplier.setCountyId(0);// 縣市countyId去哪抓? 跨區供應商問題? KC
			supplier.setCompanyId(pSupplierId);
			supplier.setOwnner(pOwner);

			supplier.setSupplierAdress(pAddress);
			supplier.setSupplierTel(pSupplierTel);
			supplier.setSupplierCertification(pCertification);
			/*
			 * if
			 * (CateringServiceUtil.isEmpty(request.getSupplierCertification()
			 * )){ supplier.setSupplierCertification(""); }else{
			 * supplier.setSupplierCertification
			 * (request.getSupplierCertification()); }
			 */

			// supplier.setId(0);

			try {
				dao.save(supplier);
				if (!openUploaderTest)
					tx.commit();
			} catch (Exception ex) {
				errorMsg.add("上傳失敗! 連線問題(1)");
				throw new Exception(StatusCode.WS_MSG_OTHER);
			}
			setResponseData(session, response, request.getMessageId(), request.getSendTime(), request.getCompanyId(), StatusCode.WS_MSG_SUCCESS, listToString(errorMsg), request.getAction());
//			session.close();

			return new JAXBElement<FoodResponseTypev2>(qname, FoodResponseTypev2.class, response);
		} catch (Exception ex) {
			if (openConsoleDebug) {
				ex.printStackTrace();
			}
			System.out.println(ex.getMessage());
			setResponseData(session, response, request.getMessageId(), request.getSendTime(), request.getCompanyId(), ex.getMessage(), listToString(errorMsg), request.getAction());
//			session.close();

			return new JAXBElement<FoodResponseTypev2>(qname, FoodResponseTypev2.class, response);
		}finally{
			session.close();
		}

	}

	// WS刪除供應商
	public JAXBElement<FoodResponseTypev2> handleDeleteSupplier(MessageContext ctx, @RequestPayload JAXBElement<DeleteSupplierRequestType> soapRquest) throws Exception {

		DeleteSupplierRequestType request = soapRquest.getValue();
		ObjectFactory objectFactory = new ObjectFactory();

		FoodResponseTypev2 response = objectFactory.createFoodResponseTypev2();
		response.setMessageId(request.getMessageId());
		QName qname = new QName(TARGET_NAMESPACE, "DeleteSupplierResponse");

		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		errorMsg.clear();
		infoMsg.clear();
		try {

			String xml = this.objectToXml(DeleteSupplierRequestType.class, request);
			String xmlwsfile = SystemConfiguration.writeReciveWS(request.getMessageId(), xml, request.getCompanyId() + "-" + request.getSupplierId() + "-" + request.getAction(), (new Date()), "RET");

			if (!DELETE_SUPPLIER.equals(request.getAction().toLowerCase())) {
				errorMsg.add("請確認Action參數內容");
				throw new Exception(StatusCode.WS_MSG_WRONG_ACTION);
			}

			// 共通欄位檢查
			checkCommonField(session, request.getUploaderAccount(), request.getMessageId(), request.sendTime, request.token, request.getCompanyId(), null);

			// 檢查是否為特殊帳號 不可進行動作
			if (openSpecialPermitted) {
				errorMsg.add("您的帳號無權登錄供應商資料");
				throw new Exception(StatusCode.WS_MSG_OTHER);
			}

			Supplier supplier = null;
			// 先檢查是否存在
			SupplierDAO dao = new SupplierDAO();
			dao.setSessionFactory(sessionFactory);
			dao.setSession(session);

			// List supplierList= dao.findByCompanyId(request.getSupplierId());
			// 20140529 Raymond 搜尋supplier多一個條件 kitchenId
			Supplier tmpSupplier = dao.querySupplierByCompanyId(request.getSupplierId(), kitchenId);
			if (tmpSupplier != null) {
				supplier = tmpSupplier;
			} else {
				errorMsg.add("供應商代碼:" + request.getSupplierId());
				throw new Exception(StatusCode.WS_MSG_SUPPLIER_NO_EXIST);
			}
			try {
				dao.delete(session, supplier);
				if (!openUploaderTest)
					tx.commit();
			} catch (Exception ex) {
				errorMsg.add("刪除失敗! 系統問題(1)");
				throw new Exception(StatusCode.WS_MSG_OTHER);
			}
			setResponseData(session, response, request.getMessageId(), request.getSendTime(), request.getCompanyId(), StatusCode.WS_MSG_SUCCESS, "", request.getAction());
//			session.close();

			return new JAXBElement<FoodResponseTypev2>(qname, FoodResponseTypev2.class, response);
		} catch (Exception ex) {
			if (openConsoleDebug) {
				ex.printStackTrace();
			}
			setResponseData(session, response, request.getMessageId(), request.getSendTime(), request.getCompanyId(), ex.getMessage(), listToString(errorMsg), request.getAction());
//			session.close();

			return new JAXBElement<FoodResponseTypev2>(qname, FoodResponseTypev2.class, response);
		}finally{
			session.close();
		}

	}
	

	// WS上下架商品
	public JAXBElement<FoodResponseTypev2> handleUploadSchoolProduct(MessageContext ctx, @RequestPayload JAXBElement<UploadSchoolProductRequestType> soapRquest) throws Exception {

		UploadSchoolProductRequestType request = soapRquest.getValue();
		ObjectFactory objectFactory = new ObjectFactory();

		FoodResponseTypev2 response = objectFactory.createFoodResponseTypev2();
		
//		response.setMessageId(request.getMessageId());
		QName qname = new QName(TARGET_NAMESPACE, "UploadSchoolProductResponse");

		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		errorMsg.clear();
		infoMsg.clear();
//		try {
//
//			String xml = this.objectToXml(UploadSchoolProductRequestType.class, request);
//			String xmlwsfile = SystemConfiguration.writeReciveWS(request.getMessageId(), xml, request.getCompanyId() + "-" + request.getSupplierId() + "-" + request.getAction(), (new Date()), "RET");
//
//			if (!ADD_SCHOOLPRODUCT.equals(request.getAction().toLowerCase())) {
//				errorMsg.add("請確認Action參數內容");
//				throw new Exception(StatusCode.WS_MSG_WRONG_ACTION);
//			}
//
//			// 共通欄位檢查
//			checkCommonField(session, request.getUploaderAccount(), request.getMessageId(), request.sendTime, request.token, request.getCompanyId(), null);
//
//			// 檢查是否為特殊帳號 不可進行動作
//			if (openSpecialPermitted) {
//				errorMsg.add("您的帳號無權登錄供應商資料");
//				throw new Exception(StatusCode.WS_MSG_OTHER);
//			}
//
//			Supplier supplier = null;
//			// 先檢查是否存在
//			SupplierDAO dao = new SupplierDAO();
//			dao.setSessionFactory(sessionFactory);
//			dao.setSession(session);
//
//			// List supplierList= dao.findByCompanyId(request.getSupplierId());
//			// 20140529 Raymond 搜尋supplier多一個條件 kitchenId
//			Supplier tmpSupplier = dao.querySupplierByCompanyId(request.getSupplierId(), kitchenId);
//			if (tmpSupplier != null) {
//				supplier = tmpSupplier;
//			} else {
//				errorMsg.add("供應商代碼:" + request.getSupplierId());
//				throw new Exception(StatusCode.WS_MSG_SUPPLIER_NO_EXIST);
//			}
//			try {
//				dao.delete(session, supplier);
//				if (!openUploaderTest)
//					tx.commit();
//			} catch (Exception ex) {
//				errorMsg.add("刪除失敗! 系統問題(1)");
//				throw new Exception(StatusCode.WS_MSG_OTHER);
//			}
//			setResponseData(session, response, request.getMessageId(), request.getSendTime(), request.getCompanyId(), StatusCode.WS_MSG_SUCCESS, "", request.getAction());
////			session.close();
//
//			return new JAXBElement<FoodResponseTypev2>(qname, FoodResponseTypev2.class, response);
//		} catch (Exception ex) {
//			if (openConsoleDebug) {
//				ex.printStackTrace();
//			}
//			setResponseData(session, response, request.getMessageId(), request.getSendTime(), request.getCompanyId(), ex.getMessage(), listToString(errorMsg), request.getAction());
////			session.close();
//
//			return new JAXBElement<FoodResponseTypev2>(qname, FoodResponseTypev2.class, response);
//		}finally{
//			session.close();
//		}
		return new JAXBElement<FoodResponseTypev2>(qname, FoodResponseTypev2.class, response);
	}

	/***************************************
	 ** 以下為共通Method
	 * 
	 */

	// 更新菜色圖檔路徑
	private static boolean updateDishImgPathByDishId(Session session, Long dishId, String path) {
		Integer result = HibernateUtil.updateDishImgpathByDishId(session, dishId, path);
		System.out.println("result:" + result + "/  dishId:" + dishId + "  /  path:" + path);
		if (result > 0) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * 查詢菜單id,無則新增菜色 20140313 增加菜色有show name，對應DB也增加欄位
	 */

	private static Long QueryDishIdByName(Session session, Integer kitchenId, DishInfo dishInfo) throws Exception {
		if (dishInfo == null || dishInfo.getValue().isEmpty() || openSpecialPermitted) {
			return (long) 0;
		}

		Long dishId = HibernateUtil.queryDishIdByName(session, kitchenId, dishInfo.getValue());
		if (dishId == null) {
			// throw new Exception("於菜色檔中找不到此菜色資料   名稱:" + dishInfo.getName());
			String imagefilename = "";
			String showname = "";
			if (!CateringServiceUtil.isEmpty(dishInfo.getImagefilename())) {
				imagefilename = dishInfo.getImagefilename();
			}
			if (!CateringServiceUtil.isEmpty(dishInfo.getShowname())) {
				showname = dishInfo.getShowname();
			}

			Dish dish = new Dish(kitchenId, dishInfo.getValue(), imagefilename, showname);
			try {
				HibernateUtil.saveNewDish(session, dish);
				dishId = dish.getDishId();
			} catch (Exception ex) {
				throw new Exception("無法新增菜色   名稱:" + dishInfo.getValue());
			}
		}
		return dishId;
	}

	// *確認MessageId對應動作的companyId必須只有一筆上傳成功
	private static boolean isMessageIdUniq(Session session, String companyId, String messageId) {
		WsLogDAO dao = new WsLogDAO();
		dao.setSession(session);
		return dao.isUniqSuccessMsgId(messageId, companyId);
	}

	// 確認SendTime與主機時間不能差距太大(Service Time sync)
	private static boolean isServerTimeSync(long sendTime) {
		Integer interval = 3600 * 5; // 5小時
		long currentTime = (new Date()).getTime() / 1000;
		if (Math.abs(sendTime - currentTime) > interval) {
			return false;
		} else {
			return true;
		}

	}

	// 取得錯誤訊息(from code table)
	private String getGtpMsg(Session session, String code) {
		if (code.length() > 3) {
			code = StatusCode.WS_MSG_OTHER;
		}

		CodeDAO dao = new CodeDAO();
		dao.setSession(session);
		return dao.getCodeMsgByStatusCode(code, StatusCode.WS_LOG_TYPE);
	}

	// 上傳學校是否屬於上傳者縣市
	private boolean isSchoolExists(Session session, Integer schoolId) {

		// 如果是台北市帳號，就檢查學校ID是否為台北市的學校
		if (openSpecialPermitted) {
			SchoolDAO schooldao = new SchoolDAO();
			schooldao.setSession(session);

			// 暫時寫這樣 以後要改成抓countyid KC 20140625
			if (loginCountyId == 0) {
				return true;
			} else {
				return schooldao.isSchoolInCounty(schoolId, loginCountyId);
			}
		}

		SchoolkitchenDAO dao = new SchoolkitchenDAO();
		dao.setSession(session);
		Schoolkitchen result = dao.findById(new SchoolkitchenId(schoolId, kitchenId));
		if (result == null) {
			// this.errorMsg.add("此團膳/自立廚房找不到學校資料 ID*:" +
			// schoolId+"("+kitchenId+")");
			return false;
		} else {
			return true;
		}

		/*
		 * SchoolDAO dao=new SchoolDAO(); dao.setSession(session); School
		 * result=dao.findById(schoolId); if (result!=null){ return true; }else{
		 * return false; }
		 */
	}

	// 取得學校代碼code
	private Integer getSchoolIdByCode(Session session, String schoolId) {
		Integer sid = 0;
		SchoolDAO schooldao = new SchoolDAO();
		schooldao.setSession(session);
		sid = schooldao.getSchoolIdByCode(schoolId.trim());
		return sid;
	}

	// 檢查Token正確性
	private boolean isTokenValid(String token, long sendTime, String uploaderAccount) throws Exception {

		// 測試用的token,若是token是指定字串，那就設定為測試模式，所有動作都不commit,msg log也不存
		if (openUploaderTest) {
			// token=md5(testforgtpschool)
			if ("0618010ddd9f7c741aff261d9094fcdf".equals(token.toLowerCase())) {
				openUploaderTest = true;
				return true;
			} else {
				openUploaderTest = false;
			}
		}

		MessageDigest md = MessageDigest.getInstance("MD5");
		String myTokenString = String.valueOf(sendTime) + "," + uploaderAccount + "," + userPassword;
		String hex = (new HexBinaryAdapter()).marshal(md.digest(myTokenString.getBytes()));
		if (hex.toLowerCase().equals(token.toLowerCase())) {
			return true;
		} else {
			this.errorMsg.add("token錯誤");
			return false;
		}
	}

	// 檢查日期格式正確性
	private boolean isDateFormatCorrect(String date) {
		if (!fmt_date.matcher(date).matches()) {
			this.errorMsg.add("日期格式錯誤，應為yyyy-MM-dd:" + date);
			return false;
		}
		return true;
	}

	// 檢查帳號(UploaderAccount)與廚房(CompanyId)的正確性
	private boolean checkAndSetIndentity(Session session, String uploaderAccount, String companyId) {
		/*
		 * 20140319 KC kitchenId=0 者 為單一帳號擁有多個廚房權限之用
		 * 台北市統一由台北市團膳系統丟soap進來，所以不檢查廚房，直接指定 帳號的kitchen為他上傳的companyId的身份
		 * 檢查:帳號本身的存在、上傳的companyId是否有存在於kitchen資料中
		 * GTP標準中，companyId為供餐廚房的統編或自立廚房的學校代碼
		 */
		try {
			Useraccount useraccount = HibernateUtil.queryUseraccountByName(session, uploaderAccount);
			if (useraccount == null) {
				this.errorMsg.add("找不到此帳號(業者統編/學校代碼):" + uploaderAccount);
				return false;
			}
			// --有被設定廚房的帳號，從帳號身分中找出廚房
			if (useraccount.getKitchenId() != 0) {
				Kitchen kitchen = HibernateUtil.queryKitchenById(session, useraccount.getKitchenId());
				if (kitchen == null) {
					this.errorMsg.add("找不到此團膳/自立廚房資料:" + companyId);
					return false;
				} else {
					if (!kitchen.getCompanyId().equals(companyId)) {
						this.errorMsg.add("上傳者帳號與團膳/自立廚房帳號無法對應,UploaderAccount:" + uploaderAccount + "  companyId:" + companyId);
						return false;
					}
				}
				kitchenId = kitchen.getKitchenId();
				openSpecialPermitted = false;
			}
			// --沒被設定到廚房的帳號，從上傳的companyId找廚房
			else {
				Kitchen kitchen = HibernateUtil.queryKitchenByCompanyId(session, companyId);
				if (kitchen == null) {
					errorMsg.add("找不到此團膳/自立廚房資料*:" + companyId);
					return false;
				}
				kitchenId = kitchen.getKitchenId();
				loginCountyId = AuthenUtil.getCountyNumByUsername(uploaderAccount);
				openSpecialPermitted = true;
			}
			userPassword = useraccount.getPassword();

			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	// 檢查菜單重複或存在
	private String checkAndGetBatchData(Session session, Integer schoolId, String action, String serviceDate,String menuType) {
		batchdata = HibernateUtil.queryBatchdataByUK(session, kitchenId, schoolId, serviceDate.replace("-", "/"), "");
//		batchdata = HibernateUtil.queryBatchdataByUKv2(session, kitchenId, schoolId, serviceDate.replace("-", "/"), "",menuType);
		String[] words = action.toLowerCase().split("_");
		_printMsg("[分離動作前綴]" + words[0]);
		if (batchdata != null && "add".equals(words[0])) {
			errorMsg.add("菜單資料已存在:" + serviceDate.replace("-", "/") + " 學校ID:" + schoolId);
			return StatusCode.WS_MSG_MENU_DUPLICATE;
		} else if (batchdata == null && ("modify".equals(words[0]) || "delete".equals(words[0]))) {
			errorMsg.add("菜單資料不存在:" + serviceDate.replace("-", "/") + " 學校ID:" + schoolId + "/ serviceDate:" + serviceDate + "/kitchenid:" + kitchenId);
			return StatusCode.WS_MSG_MENU_NO_EXIST;
		}
		return "";
	}

	// 設定回傳訊息
	private FoodResponseTypev2 setResponseData(Session session, FoodResponseTypev2 response, String messageId, long sendTime, String companyId, String statusCode, String description, String action) {
		response.setCompanyId(companyId);
		response.setMessage(getGtpMsg(session, statusCode));
		response.setMessageId(messageId);
		response.setStatus(statusCode);
		response.setUploadDate(String.valueOf(new java.util.Date().getTime() / 1000));

		if (openUploaderTest) {
			response.setDescription("[本上傳使用測試帳號，僅進行資料檢核，並未實際儲存!]\n" + description);
		} else {
			// response.setDescription(description+"\r\n"+
			// listToString(infoMsg));

			if (statusCode.length() > 3) { // 其他exception時,把stack存在db 回傳不顯示
				response.setDescription("發生系統問題或連線問題，請洽系統管理員");
				description = statusCode + ":" + description;
				response.setStatus(StatusCode.WS_MSG_OTHER);
			} else {
				response.setDescription(description + "\r\n" + listToString(infoMsg));
			}

		}

		// 把log存在db中

		saveUploadStatus(messageId, sendTime, companyId, statusCode, description, action);
		return response;
	}

	// 處理附檔
	private String processAttachments(MessageContext ctx, String ukpath) throws Exception {
		// List<String> tmpZipFilePath = new ArrayList<String>();
		SoapMessage soapMsg = (SoapMessage) ctx.getRequest();
		Iterator<Attachment> iter = soapMsg.getAttachments();
		// List<String> resultPathList = new ArrayList<>();
		if (!soapMsg.getAttachments().hasNext()) {
			return "";
		}

		File pathdest = new File(ukpath);
		if (!pathdest.exists()) {
			pathdest.mkdirs();
		}

		Attachment attch = null;

		OpenOption[] openOptions = { StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE };

		while (iter.hasNext()) {
			attch = iter.next();
			if (iter.hasNext()) {
				errorMsg.add("SOAP附檔只能有一個zip檔!");
				throw new Exception(StatusCode.WS_MSG_ATTACHMENT_ERROR);
			}

			String origFilename = attch.getDataHandler().getName();
			infoMsg.add("本次附件檔名為:" + origFilename);
			if (origFilename != null) {
				CharsetDetector dec = new CharsetDetector();
				dec.setText(origFilename.getBytes());
				CharsetMatch match = dec.detect();
				String encoding = match.getName();
				String encodedFilename = new String(origFilename.getBytes(encoding), "UTF-8");
				// log.info("Write attachment to disk:"+encodedFilename);
				Path fileFullPath = Paths.get(ukpath, encodedFilename);
				// Save to tmp file folder
				String origExt = FilenameUtils.getExtension(encodedFilename);
				Files.write(fileFullPath, IOUtils.toByteArray(attch.getInputStream()), openOptions);
				// tmpZipFilePath.add(fileFullPath.toString());
				if ("zip".equals(origExt.toLowerCase())) {
					if (ZipUtils.unpack(fileFullPath.toFile(), ukpath)) {
						Files.deleteIfExists(fileFullPath);
					}
				}
			}
		}
		return ukpath;

	}

	// debug用
	private static void _printMsg(String msg) {
		if (!openConsoleDebug) {
			return;
		}
		System.out.println("====[" + (new Date().toString()) + "]" + msg);
	}

	// 共通欄位檢查
	private void checkCommonField(Session session, String uploaderAccount, String messageId, long sendTime, String token, String companyId, String serviceDate) throws Exception {

		// 查詢UploaderAccount,companyId 對應
		if (!checkAndSetIndentity(session, uploaderAccount, companyId)) {
			throw new Exception(StatusCode.WS_MSG_AUTH_ERROR);
		}

		// 檢查Token
		if (!isTokenValid(token, sendTime, uploaderAccount)) {
			throw new Exception(StatusCode.WS_MSG_AUTH_ERROR);
		}

		// 檢查日期
		if (serviceDate != null) {
			if (!isDateFormatCorrect(serviceDate)) {
				throw new Exception(StatusCode.WS_MSG_DATA_FORMAT_ERROR);
			}
		}

		// 檢查MessageId
		if (!isMessageIdUniq(session, companyId, messageId)) {
			throw new Exception(StatusCode.WS_MSG_MESSAGEID_DUPLICATE);
		}

		// 檢查主機時間
		if (!isServerTimeSync(sendTime)) {
			throw new Exception(StatusCode.WS_MSG_SERVER_TIMEOUT);
		}

	}

	// 存新菜單表格
	private Long addDishToDishBatchData(Session session, Long batchdataId, DishInfo dishInfo, String dishType, List<String> fileList, String attachmentStorePaths) {
		if (dishInfo.getValue().isEmpty() || dishInfo == null) {
			return (long) 0;
		}

		Long dishbatchdataId = (long) 0;

		// 處理showName過渡期與菜名相同
		String showName = "";
		if (CateringServiceUtil.isEmpty(dishInfo.getShowname())) {
			showName = dishInfo.getValue();
		} else {
			showName = dishInfo.getShowname();
		}

		DishBatchDataDAO dao = new DishBatchDataDAO();
		dao.setSession(session);
		DishBatchData dishbatchdata;

		// 若為新增菜單時
		dishbatchdata = dao.getSpecifiedDish(batchdataId, dishType);
		if (dishbatchdata == null) {
			dishbatchdata = new DishBatchData();
		}

		dao.setSession(session);
		try {
			dishbatchdata.setDishBatchDataId(0);
			dishbatchdata.setBatchDataId(batchdataId);
			dishbatchdata.setDishId(QueryDishIdByName(session, kitchenId, dishInfo));
			dishbatchdata.setDishName(dishInfo.getValue());
			dishbatchdata.setDishShowName(showName);
			dishbatchdata.setDishType(dishType);
			dishbatchdata.setUpdateDateTime(CateringServiceUtil.getCurrentTimestamp());
			dao.save(dishbatchdata);
			dishbatchdataId = dishbatchdata.getDishBatchDataId();
		} catch (Exception ex) {
			// 表格更換過渡期暫時不做exception拋出 20140318 KC
			_printMsg("新表格菜色無法存  " + dishInfo.getValue() + ":" + ex.getMessage());
			if (openConsoleDebug)
				ex.printStackTrace();

		}

		// 順便處理菜色基本資料修改與圖檔儲存，台北市的不處理圖檔
		if (!openSpecialPermitted) {
			try {
				if (CateringServiceUtil.isEmpty(dishInfo.getImagefilename())) {
					// errorMsg.add("[info]菜名:"+dishInfo.getValue()+" 無附照片");
					return dishbatchdataId;
				} // 無設定圖檔時，不處理圖檔
				if (fileList.contains(dishInfo.getImagefilename())) {
					// 先處理檔案資訊
					String ext = FilenameUtils.getExtension(dishInfo.getImagefilename());
					String targetFile = CateringServiceUtil.getDishImageFileName(kitchenId, dishbatchdata.getDishId(), ext).toLowerCase();
					File targetPath = new File(targetFile);
					updateDishImgPathByDishId(session, dishbatchdata.getDishId(), targetFile);
					Path copyResult = Files.copy(Paths.get(attachmentStorePaths, dishInfo.getImagefilename()), targetPath.toPath(), StandardCopyOption.REPLACE_EXISTING);
					// 刪除原有的縮圖 20140402 KC
					String resizeFileName = CateringServiceUtil.getDishImageFileName(kitchenId, dishbatchdata.getDishId(), CateringServiceUtil.DishImageSizeHigh, CateringServiceUtil.DishImageSizeWidth, "png");
					File resizeFile = new File(resizeFileName);
					_printMsg("縮圖位址: " + resizeFileName);

					if (resizeFile.exists()) {
						resizeFile.delete();
					}
					infoMsg.add("已儲存上傳檔名: " + dishInfo.getImagefilename());
					log.debug("已移動檔案至:" + targetFile + "; 上傳檔名: " + dishInfo.getImagefilename());
				}
			} catch (Exception ex) {
				errorMsg.add("附件圖檔儲存失敗，請洽系統管理員。菜色設定檔名:" + dishInfo.getImagefilename());

			}
		}
		return dishbatchdataId;

	}

	// 處理菜色圖檔
	private void saveDishImage(Session session, DishInfo dishInfo, List<String> fileList, String attachmentStorePaths) {
		if (dishInfo.getValue().isEmpty() || dishInfo == null) {
			return;
		}

		// 台北市的不處理圖檔
		if (openSpecialPermitted) {
			return;
		}
		try {
			if (CateringServiceUtil.isEmpty(dishInfo.getImagefilename())) {
				// errorMsg.add("[info]菜名:"+dishInfo.getValue()+" 無附照片");
				return;
			} // 無設定圖檔時，不處理圖檔
			if (fileList.contains(dishInfo.getImagefilename())) {
				// 先處理檔案資訊
				String ext = FilenameUtils.getExtension(dishInfo.getImagefilename());
				Long dishId = QueryDishIdByName(session, kitchenId, dishInfo);
				String targetFile = CateringServiceUtil.getDishImageFileName(kitchenId, dishId, ext).toLowerCase();
				File targetPath = new File(targetFile);
				updateDishImgPathByDishId(session, dishId, targetFile);
				Path copyResult = Files.copy(Paths.get(attachmentStorePaths, dishInfo.getImagefilename()), targetPath.toPath(), StandardCopyOption.REPLACE_EXISTING);
				// 刪除原有的縮圖 20140402 KC
				String resizeFileName = CateringServiceUtil.getDishImageFileName(kitchenId, dishId, CateringServiceUtil.DishImageSizeHigh, CateringServiceUtil.DishImageSizeWidth, "png");
				File resizeFile = new File(resizeFileName);
				_printMsg("縮圖位址: " + resizeFileName);

				if (resizeFile.exists()) {
					resizeFile.delete();
				}
				infoMsg.add("已儲存上傳檔名: " + dishInfo.getImagefilename());
				log.debug("已移動檔案至:" + targetFile + "; 上傳檔名: " + dishInfo.getImagefilename());
			}
		} catch (Exception ex) {
			errorMsg.add("附件圖檔儲存失敗，請洽系統管理員。菜色設定檔名:" + dishInfo.getImagefilename());

		}

	}

	// 存舊菜單表格
	private void setBatchDataValue(Session session, UploadMenuRequestType request, Integer sid) throws Exception {
		batchdata.setKitchenId(kitchenId);
		batchdata.setLotNumber(CateringServiceUtil.defaultLotNumber);// 預設logNumber
																		// 為 ""
																		// 多數學校只有一餐//
																		// 這個欄位有問題
		batchdata.setMenuDate(request.getServiceDate());
		// batchdata.setSchoolId(Integer.valueOf((String)request.getSchoolId()));
		batchdata.setSchoolId(sid);
		if (!CateringServiceUtil.isNull(request.getStaple1())) {
			batchdata.setMainFoodId(QueryDishIdByName(session, kitchenId, request.getStaple1()));
		}

		if (!CateringServiceUtil.isNull(request.getStaple2())) {
			batchdata.setMainFood1id(QueryDishIdByName(session, kitchenId, request.getStaple2()));
		}

		if (!CateringServiceUtil.isNull(request.getMainCourse1())) {
			batchdata.setMainDishId(QueryDishIdByName(session, kitchenId, request.getMainCourse1()));
		}

		if (!CateringServiceUtil.isNull(request.getMainCourse2())) {
			batchdata.setMainDish1id(QueryDishIdByName(session, kitchenId, request.getMainCourse2()));
		}

		if (!CateringServiceUtil.isNull(request.getMainCourse3())) {
			batchdata.setMainDish2id(QueryDishIdByName(session, kitchenId, request.getMainCourse3()));
		}

		if (!CateringServiceUtil.isNull(request.getMainCourse4())) {
			batchdata.setMainDish3id(QueryDishIdByName(session, kitchenId, request.getMainCourse4()));
		}

		if (!CateringServiceUtil.isNull(request.getSideDish1())) {
			batchdata.setSubDish1id(QueryDishIdByName(session, kitchenId, request.getSideDish1()));
		}

		if (!CateringServiceUtil.isNull(request.getSideDish2())) {
			batchdata.setSubDish2id(QueryDishIdByName(session, kitchenId, request.getSideDish2()));
		}

		if (!CateringServiceUtil.isNull(request.getSideDish3())) {
			batchdata.setSubDish3id(QueryDishIdByName(session, kitchenId, request.getSideDish3()));
		}

		if (!CateringServiceUtil.isNull(request.getSideDish4())) {
			batchdata.setSubDish4id(QueryDishIdByName(session, kitchenId, request.getSideDish4()));
		}

		if (!CateringServiceUtil.isNull(request.getSideDish5())) {
			batchdata.setSubDish5id(QueryDishIdByName(session, kitchenId, request.getSideDish5()));
		}

		if (!CateringServiceUtil.isNull(request.getSideDish6())) {
			batchdata.setSubDish6id(QueryDishIdByName(session, kitchenId, request.getSideDish6()));
		}

		if (!CateringServiceUtil.isNull(request.getVegetable())) {
			batchdata.setVegetableId(QueryDishIdByName(session, kitchenId, request.getVegetable()));
		}

		if (!CateringServiceUtil.isNull(request.getSoup())) {
			batchdata.setSoupId(QueryDishIdByName(session, kitchenId, request.getSoup()));
		}

		if (!CateringServiceUtil.isNull(request.getExtra1())) {
			batchdata.setDessertId(QueryDishIdByName(session, kitchenId, request.getExtra1()));
		}

		if (!CateringServiceUtil.isNull(request.getExtra2())) {
			batchdata.setDessert1id(QueryDishIdByName(session, kitchenId, request.getExtra2()));
		}

		batchdata.setCalorie(request.getCalories().toString());
		batchdata.setTypeFruit(request.getFruits().toString());
		batchdata.setTypeGrains(request.getGrains().toString());
		batchdata.setTypeMeatBeans(request.getMeatBeans().toString());
		batchdata.setTypeMilk(request.getMilk().toString());
		batchdata.setTypeOil(request.getOils().toString());
		batchdata.setTypeVegetable(request.getVegetables().toString());
		batchdata.setUploadDateTime(CateringServiceUtil.getCurrentTimestamp());

		// 先存
		// session.save(batchdata);(20140529 Raymond 先不存資料)
	}

	//存舊菜單表格By新菜單陣列 20140925 KC
	private void setBatchbataValueByDishArray(Session session, UploadMenuRequestType request, HashMap<String, DishInfo>  dishbatchdataArray, Integer sid) throws Exception{
		batchdata.setKitchenId(kitchenId);
		batchdata.setLotNumber(CateringServiceUtil.defaultLotNumber);// 預設logNumber
																		// 為 ""
																		// 多數學校只有一餐//
																		// 這個欄位有問題
		batchdata.setMenuDate(request.getServiceDate());
		// batchdata.setSchoolId(Integer.valueOf((String)request.getSchoolId()));
		batchdata.setSchoolId(sid);
		
		if (!CateringServiceUtil.isNull(dishbatchdataArray.containsKey(CateringServiceCode.DISHTYPE_MAINFOOD))) {
			batchdata.setMainFoodId(QueryDishIdByName(session, kitchenId, dishbatchdataArray.get(CateringServiceCode.DISHTYPE_MAINFOOD)));
		}
		
		if (!CateringServiceUtil.isNull(dishbatchdataArray.containsKey(CateringServiceCode.DISHTYPE_MAINFOOD1))) {
			batchdata.setMainFood1id(QueryDishIdByName(session, kitchenId, dishbatchdataArray.get(CateringServiceCode.DISHTYPE_MAINFOOD1)));
		}

		if (!CateringServiceUtil.isNull(dishbatchdataArray.containsKey(CateringServiceCode.DISHTYPE_MAINDISH))) {
			batchdata.setMainDishId(QueryDishIdByName(session, kitchenId, dishbatchdataArray.get(CateringServiceCode.DISHTYPE_MAINDISH)));
		}
		
		if (!CateringServiceUtil.isNull(dishbatchdataArray.containsKey(CateringServiceCode.DISHTYPE_MAINDISH1))) {
			batchdata.setMainDish1id(QueryDishIdByName(session, kitchenId, dishbatchdataArray.get(CateringServiceCode.DISHTYPE_MAINDISH1)));
		}
		if (!CateringServiceUtil.isNull(dishbatchdataArray.containsKey(CateringServiceCode.DISHTYPE_MAINDISH2))) {
			batchdata.setMainDish2id(QueryDishIdByName(session, kitchenId, dishbatchdataArray.get(CateringServiceCode.DISHTYPE_MAINDISH2)));
		}	
		if (!CateringServiceUtil.isNull(dishbatchdataArray.containsKey(CateringServiceCode.DISHTYPE_MAINDISH3))) {
			batchdata.setMainDish3id(QueryDishIdByName(session, kitchenId, dishbatchdataArray.get(CateringServiceCode.DISHTYPE_MAINDISH3)));
		}

		if (!CateringServiceUtil.isNull(dishbatchdataArray.containsKey(CateringServiceCode.DISHTYPE_SUBDISH1))) {
			batchdata.setSubDish1id(QueryDishIdByName(session, kitchenId, dishbatchdataArray.get(CateringServiceCode.DISHTYPE_SUBDISH1)));
		}
		
		if (!CateringServiceUtil.isNull(dishbatchdataArray.containsKey(CateringServiceCode.DISHTYPE_SUBDISH2))) {
			batchdata.setSubDish2id(QueryDishIdByName(session, kitchenId, dishbatchdataArray.get(CateringServiceCode.DISHTYPE_SUBDISH2)));
		}
		
		if (!CateringServiceUtil.isNull(dishbatchdataArray.containsKey(CateringServiceCode.DISHTYPE_SUBDISH3))) {
			batchdata.setSubDish3id(QueryDishIdByName(session, kitchenId, dishbatchdataArray.get(CateringServiceCode.DISHTYPE_SUBDISH3)));
		}	
		
		if (!CateringServiceUtil.isNull(dishbatchdataArray.containsKey(CateringServiceCode.DISHTYPE_SUBDISH4))) {
			batchdata.setSubDish4id(QueryDishIdByName(session, kitchenId, dishbatchdataArray.get(CateringServiceCode.DISHTYPE_SUBDISH4)));
		}
		
		if (!CateringServiceUtil.isNull(dishbatchdataArray.containsKey(CateringServiceCode.DISHTYPE_SUBDISH5))) {
			batchdata.setSubDish5id(QueryDishIdByName(session, kitchenId, dishbatchdataArray.get(CateringServiceCode.DISHTYPE_SUBDISH5)));
		}
		
		if (!CateringServiceUtil.isNull(dishbatchdataArray.containsKey(CateringServiceCode.DISHTYPE_SUBDISH6))) {
			batchdata.setSubDish6id(QueryDishIdByName(session, kitchenId, dishbatchdataArray.get(CateringServiceCode.DISHTYPE_SUBDISH6)));
		}
		
		if (!CateringServiceUtil.isNull(dishbatchdataArray.containsKey(CateringServiceCode.DISHTYPE_VEGETABLE))) {
			batchdata.setVegetableId(QueryDishIdByName(session, kitchenId, dishbatchdataArray.get(CateringServiceCode.DISHTYPE_VEGETABLE)));
		}
		
		if (!CateringServiceUtil.isNull(dishbatchdataArray.containsKey(CateringServiceCode.DISHTYPE_SOUP))) {
			batchdata.setSoupId(QueryDishIdByName(session, kitchenId, dishbatchdataArray.get(CateringServiceCode.DISHTYPE_SOUP)));
		}

		if (!CateringServiceUtil.isNull(dishbatchdataArray.containsKey(CateringServiceCode.DISHTYPE_DESSERT))) {
			batchdata.setDessertId(QueryDishIdByName(session, kitchenId, dishbatchdataArray.get(CateringServiceCode.DISHTYPE_DESSERT)));
		}
		
		if (!CateringServiceUtil.isNull(dishbatchdataArray.containsKey(CateringServiceCode.DISHTYPE_DESSERT1))) {
			batchdata.setDessert1id(QueryDishIdByName(session, kitchenId, dishbatchdataArray.get(CateringServiceCode.DISHTYPE_DESSERT1)));
		}
		


		batchdata.setCalorie(request.getCalories().toString());
		batchdata.setTypeFruit(request.getFruits().toString());
		batchdata.setTypeGrains(request.getGrains().toString());
		batchdata.setTypeMeatBeans(request.getMeatBeans().toString());
		batchdata.setTypeMilk(request.getMilk().toString());
		batchdata.setTypeOil(request.getOils().toString());
		batchdata.setTypeVegetable(request.getVegetables().toString());
		batchdata.setUploadDateTime(CateringServiceUtil.getCurrentTimestamp());
	}
	
	// 處理新版DishList物件轉成菜色陣列  20140924 KC
		private HashMap<String, DishInfo> setBatchdataAndDishbatchdataByDishListObj(List<org.iii.ideas.catering_service.ws.schemav2.DishesType.Dish> menuDishList,HashMap<String, DishInfo>  dishbatchdataArray){
			/*
			//各菜色種類計算
			HashMap<String ,Integer> dishIndexMap=initDishTypeMap();
			
			
			// 新菜單陣列   直接覆蓋傳進來的陣列有的項目  KC
			//HashMap<String, DishInfo> dishbatchdataArray = new HashMap<String, DishInfo>();
			for(org.iii.ideas.catering_service.ws.schemav2.DishesType.Dish itemDish : menuDishList){
				
				if (!FoodPropertyCode.WS_FOODCODE_DISHTYPE_LIST.containsKey(itemDish.getDishType())){
					//非菜色代碼者 不處理 
					continue;
				}
				//反正先塞使用者指定的類型
				String itemDishTypeString=FoodPropertyCode.WS_FOODCODE_DISHTYPE_LIST.get(itemDish.getDishType());
				/**舊表格16欄位特別處理  20140924*
				//必填欄位 主食 MainFood
				switch (itemDish.getDishType()){
				case "1": //主食	
					switch(dishIndexMap.get(itemDish.getDishType()).toString()){
					case "0":
						itemDishTypeString=CateringServiceCode.DISHTYPE_MAINFOOD;
						break;
					case "1":
						itemDishTypeString=CateringServiceCode.DISHTYPE_MAINFOOD1;
						break;
					default:
							break;
					}
					break;
				case "2" : //主菜
					if (dishIndexMap.get(itemDish.getDishType())==0){
						itemDishTypeString=CateringServiceCode.DISHTYPE_MAINDISH;
					}else if (dishIndexMap.get(itemDish.getDishType())<4){
						itemDishTypeString="MainDish"+dishIndexMap.get(itemDish.getDishType()).toString()+"Id";
					}else{
						//user input 
					}
					break;
				case "3":  //副菜
					if(dishIndexMap.get(itemDish.getDishType())==0){ //副菜從1開始
						dishIndexMap.put(itemDish.getDishType(),dishIndexMap.get(itemDish.getDishType())+1);
					}
					if (dishIndexMap.get(itemDish.getDishType())<7){
						itemDishTypeString=FoodPropertyCode.WS_FOODCODE_DISHTYPE_LIST.get(itemDish.getDishType())+dishIndexMap.get(itemDish.getDishType()).toString()+"Id";
					}
					break;
				case "6":
					switch(dishIndexMap.get(itemDish.getDishType()).toString()){
					case "0":
						itemDishTypeString=CateringServiceCode.DISHTYPE_DESSERT;
						break;
					case "1":
						itemDishTypeString=CateringServiceCode.DISHTYPE_DESSERT1;
						break;
					default:
							break;
					}
					break;
				default:
					break;
				
				}
				//累加菜色類型數量
				dishIndexMap.put(itemDish.getDishType(),dishIndexMap.get(itemDish.getDishType())+1);
				dishbatchdataArray.put(FoodPropertyCode.WS_FOODCODE_DISHTYPE_LIST.get(itemDish.getDishType()), itemDish.getDishInfo());
				}
		*/
			/**
			 * 目前16欄位資料庫除SubDish外，
			 * 其餘DishType皆從無編號開始(ex. MainDishId,MainDish1Id,MainDish2Id)
			 * 因此，拼湊時除了SubDish外，皆將itemDish.getDisplayOrder()減1，再將0replace掉。
			 * 20141113 add by Ellis
			 */
			dishbatchdataArray.clear(); //清除舊版資訊
			String dishType = ""; //拼湊dishType
			for(org.iii.ideas.catering_service.ws.schemav2.DishesType.Dish itemDish : menuDishList){
				if(!FoodPropertyCode.WS_FOODCODE_DISHTYPE_LIST.get(itemDish.getDishType()).equals("SubDish")){
					dishType = FoodPropertyCode.WS_FOODCODE_DISHTYPE_LIST.get(itemDish.getDishType()).toString()+itemDish.getDisplayOrder().subtract(new BigDecimal(1))+"Id";
					dishType = dishType.replace("0","");
				}else{
					dishType = FoodPropertyCode.WS_FOODCODE_DISHTYPE_LIST.get(itemDish.getDishType()).toString()+itemDish.getDisplayOrder()+"Id";
					
				}
				dishbatchdataArray.put(dishType,itemDish.getDishInfo());
			}
			return dishbatchdataArray;
		}
	

	//各菜色的數量init 20140924  KC
	private HashMap<String ,Integer> initDishTypeMap(){
		HashMap<String ,Integer> dishIndexMap=new HashMap<String,Integer>();
		for (Entry<String ,String> item: FoodPropertyCode.WS_FOODCODE_DISHTYPE_LIST.entrySet()){
			dishIndexMap.put(item.getValue(),0);			
		}
	
		return dishIndexMap;
	}
	
	
	//將 16欄位放入菜色鎮列處理  20140924 KC
	private HashMap<String, DishInfo> setBatchdataDishByDishListItem(Batchdata batchdata,UploadMenuRequestType request  ){
		HashMap<String, DishInfo> dishbatchdataArray = new HashMap<String, DishInfo>();
		
		// 依據16個欄位處理資料(舊格式)
		if (!CateringServiceUtil.isNull(request.getStaple1())) {
			dishbatchdataArray.put(CateringServiceCode.DISHTYPE_MAINFOOD, request.getStaple1());

		}

		if (!CateringServiceUtil.isNull(request.getStaple2())) {
			dishbatchdataArray.put(CateringServiceCode.DISHTYPE_MAINFOOD1, request.getStaple2());
			// addDishToDishBatchData(session,batchdata.getBatchDataId(),request.getStaple2(),CateringServiceCode.DISHTYPE_MAINFOOD1,fileList,attachmentStorePaths);
		}

		if (!CateringServiceUtil.isNull(request.getMainCourse1())) {
			dishbatchdataArray.put(CateringServiceCode.DISHTYPE_MAINDISH, request.getMainCourse1());
			// addDishToDishBatchData(session,batchdata.getBatchDataId(),request.getMainCourse1(),CateringServiceCode.DISHTYPE_MAINDISH,fileList,attachmentStorePaths);
		}

		if (!CateringServiceUtil.isNull(request.getMainCourse2())) {
			dishbatchdataArray.put(CateringServiceCode.DISHTYPE_MAINDISH1, request.getMainCourse2());
			// addDishToDishBatchData(session,batchdata.getBatchDataId(),request.getMainCourse2(),CateringServiceCode.DISHTYPE_MAINDISH1,fileList,attachmentStorePaths);
		}

		if (!CateringServiceUtil.isNull(request.getMainCourse3())) {
			dishbatchdataArray.put(CateringServiceCode.DISHTYPE_MAINDISH2, request.getMainCourse3());
			// addDishToDishBatchData(session,batchdata.getBatchDataId(),request.getMainCourse3(),CateringServiceCode.DISHTYPE_MAINDISH2,fileList,attachmentStorePaths);
		}

		if (!CateringServiceUtil.isNull(request.getMainCourse4())) {
			dishbatchdataArray.put(CateringServiceCode.DISHTYPE_MAINDISH3, request.getMainCourse4());
			// addDishToDishBatchData(session,batchdata.getBatchDataId(),request.getMainCourse4(),CateringServiceCode.DISHTYPE_MAINDISH3,fileList,attachmentStorePaths);
		}

		if (!CateringServiceUtil.isNull(request.getSideDish1())) {
			dishbatchdataArray.put(CateringServiceCode.DISHTYPE_SUBDISH1, request.getSideDish1());
			// addDishToDishBatchData(session,batchdata.getBatchDataId(),request.getSideDish1(),CateringServiceCode.DISHTYPE_SUBDISH1,fileList,attachmentStorePaths);
		}

		if (!CateringServiceUtil.isNull(request.getSideDish2())) {
			dishbatchdataArray.put(CateringServiceCode.DISHTYPE_SUBDISH2, request.getSideDish2());
			// addDishToDishBatchData(session,batchdata.getBatchDataId(),request.getSideDish2(),CateringServiceCode.DISHTYPE_SUBDISH2,fileList,attachmentStorePaths);
		}

		if (!CateringServiceUtil.isNull(request.getSideDish3())) {
			dishbatchdataArray.put(CateringServiceCode.DISHTYPE_SUBDISH3, request.getSideDish3());
			// addDishToDishBatchData(session,batchdata.getBatchDataId(),request.getSideDish3(),CateringServiceCode.DISHTYPE_SUBDISH3,fileList,attachmentStorePaths);
		}

		if (!CateringServiceUtil.isNull(request.getSideDish4())) {
			dishbatchdataArray.put(CateringServiceCode.DISHTYPE_SUBDISH4, request.getSideDish4());
			// addDishToDishBatchData(session,batchdata.getBatchDataId(),request.getSideDish4(),CateringServiceCode.DISHTYPE_SUBDISH4,fileList,attachmentStorePaths);
		}

		if (!CateringServiceUtil.isNull(request.getSideDish5())) {
			dishbatchdataArray.put(CateringServiceCode.DISHTYPE_SUBDISH5, request.getSideDish5());
			// addDishToDishBatchData(session,batchdata.getBatchDataId(),request.getSideDish5(),CateringServiceCode.DISHTYPE_SUBDISH5,fileList,attachmentStorePaths);
		}

		if (!CateringServiceUtil.isNull(request.getSideDish6())) {
			dishbatchdataArray.put(CateringServiceCode.DISHTYPE_SUBDISH6, request.getSideDish6());
			// addDishToDishBatchData(session,batchdata.getBatchDataId(),request.getSideDish6(),CateringServiceCode.DISHTYPE_SUBDISH6,fileList,attachmentStorePaths);
		}

		if (!CateringServiceUtil.isNull(request.getVegetable())) {
			dishbatchdataArray.put(CateringServiceCode.DISHTYPE_VEGETABLE, request.getVegetable());
			// addDishToDishBatchData(session,batchdata.getBatchDataId(),request.getVegetable(),CateringServiceCode.DISHTYPE_VEGETABLE,fileList,attachmentStorePaths);
		}

		if (!CateringServiceUtil.isNull(request.getSoup())) {
			dishbatchdataArray.put(CateringServiceCode.DISHTYPE_SOUP, request.getSoup());
			// addDishToDishBatchData(session,batchdata.getBatchDataId(),request.getSoup(),CateringServiceCode.DISHTYPE_SOUP,fileList,attachmentStorePaths);
		}

		if (!CateringServiceUtil.isNull(request.getExtra1())) {
			dishbatchdataArray.put(CateringServiceCode.DISHTYPE_DESSERT, request.getExtra1());
			// addDishToDishBatchData(session,batchdata.getBatchDataId(),request.getExtra1(),CateringServiceCode.DISHTYPE_DESSERT,fileList,attachmentStorePaths);
		}

		if (!CateringServiceUtil.isNull(request.getExtra2())) {
			dishbatchdataArray.put(CateringServiceCode.DISHTYPE_DESSERT1, request.getExtra2());
			// addDishToDishBatchData(session,batchdata.getBatchDataId(),request.getExtra2(),CateringServiceCode.DISHTYPE_DESSERT1,fileList,attachmentStorePaths);
		}
		return dishbatchdataArray;
	}
	
	// 存messageId以及本次結果
	private void saveUploadStatus(String messageId, long sendTime, String companyId, String statusCode, String description, String action) {
		if (openUploaderTest) {
			return;
		}
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			if (statusCode.length() > 3) {
				errorMsg.add(statusCode);
				statusCode = StatusCode.WS_MSG_OTHER;
			}

			// WsLogDAO dao=new WsLogDAO();
			// dao.setSession(session);
			WsLog log = new WsLog();
			log.setId(0);
			log.setCompanyId(companyId);
			log.setDescription(errorMsg.toString());
			log.setMessageId(messageId);
			log.setSendTime(new java.sql.Timestamp(sendTime * 1000));
			log.setUpdateTime(CateringServiceUtil.getCurrentTimestamp());
			log.setStatusCode(statusCode);
			log.setAction(action);
			// dao.save(log);
			session.save(log);
			tx.commit();
			// 存訊息使用獨立 session

		} catch (Exception ex) {
			_printMsg("ws log (" + messageId + ")儲存失敗" + ex.getMessage());
		} finally {
			session.close();

		}
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

	// 刪除檔案
	private static void unlinkFile(String path) {
		try {
			File target = new File(path);
			if (target.isDirectory()) {
				FileUtils.deleteDirectory(target);
			} else {
				target.delete();
			}
			log.debug("WS暫存檔案刪除成功! 路徑:" + path);
			_printMsg("WS暫存檔案刪除成功! 路徑:" + path);
		} catch (Exception ex) {
			log.debug("WS暫存檔案刪除失敗! 路徑:" + path + ", 原因" + ex.getMessage());
			_printMsg("WS暫存檔案刪除失敗! 路徑:" + path + ", 原因" + ex.getMessage());
			ex.printStackTrace();
		}
	}

	// 轉出msg string
	private String listToString(List list) {
		String msg = "";
		Iterator ir = list.iterator();
		while (ir.hasNext()) {
			msg = msg + ir.next().toString();
			if (ir.hasNext()) {
				msg = msg + "\r\n";
			}
		}

		return msg;
	}

	// hashmap 找key
	private Object findHashmapKeyByValue(HashMap map, Object value) {
		for (Object key : map.keySet()) {
			if (map.get(key).toString().equals(value.toString())) {
				return key;
			}
		}
		return null;
	}

	private void saveInspectionFile(String attachmentStorePaths, String inspectionFile, List<String> fileList, Integer kitchenId, Long ingredientBatchId) {
		// -----檢驗報告處理----------
		String ext = FilenameUtils.getExtension(inspectionFile);

		try {
			String targetFile = CateringServiceUtil.getInspectionFileName(kitchenId, ingredientBatchId, ext);
			File targetPath = new File(targetFile);
			Files.copy(Paths.get(attachmentStorePaths, inspectionFile), targetPath.toPath());
			
			// log.info("WS 上傳檢驗檔:"+targetPath.toURI());
			System.out.println("WS 上傳檢驗檔:" + targetPath.toURI());
		} catch (Exception e) {
			System.out.println("WS 上傳檢驗檔失敗:" + inspectionFile);
		}

	}
	
	private void saveInspectionFileV2(Session dbSession,String attachmentStorePaths, String inspectionFile, Long ingredientBatchId, String menuDate , Long ingredientId,String supplierCompanyId,String lotNumber,String stockDate) {
		// -----檢驗報告處理----------
		
		File insFile = new File(attachmentStorePaths+"/"+ inspectionFile);
		if(!insFile.exists())
			System.out.println("WS 上傳檢驗檔失敗:" + inspectionFile);
			
		String ext = FilenameUtils.getExtension(inspectionFile);

		try {
//			String targetFile = CateringServiceUtil.getInspectionFileName(kitchenId, ingredientBatchId, ext);
			Common comm = new Common();
			String encodeFileName = comm.getEncoderByMd5(kitchenId + supplierCompanyId + ingredientId + Calendar.getInstance().getTimeInMillis()) + "." + ext.toLowerCase();
			String uploadPatch = CateringServiceUtil.getConfig("uploadPath") + "inspect/" + kitchenId + "/";
			String fullFileName = CateringServiceUtil.getInspectionFileNameV2(uploadPatch, encodeFileName);
			File targetPath = new File(fullFileName);
			Files.copy(Paths.get(attachmentStorePaths, inspectionFile), targetPath.toPath());

			// 取所有的ingredientbatchid
			IngredientbatchdataDAO ibdDao = new IngredientbatchdataDAO(dbSession);
			List<Long> batchdataIdList = ibdDao.queryIngredientbatchIdList(kitchenId, menuDate, ingredientId, supplierCompanyId, lotNumber, stockDate);
			if (batchdataIdList == null)
				return;

			// insert uploadfile data
			try {
				UploadFileDAO ufDao = new UploadFileDAO(dbSession);
				
				String mimeType = "";
				FileNameMap fileNameMap = URLConnection.getFileNameMap();
				mimeType = fileNameMap.getContentTypeFor(targetPath.toString());
				
				for (Long batchdataId : batchdataIdList) {
					FileBO fileBo = new FileBO();
					fileBo.setFilePath(uploadPatch);
					fileBo.setMimeType(mimeType);
					fileBo.setExtType(ext);
					fileBo.setEncodeFileName(encodeFileName);
					fileBo.setSourceType(SourceTypeCode.INGREDIENT_INSPECTION);
					fileBo.setTargetId(String.valueOf(batchdataId));
					ufDao.saveUploadFile(fileBo);
				}
//				tx.commit();
			} catch (Exception e) {
//				tx.rollback();
				e.printStackTrace();
			}
			
			// log.info("WS 上傳檢驗檔:"+targetPath.toURI());
			System.out.println("WS 上傳檢驗檔:" + targetPath.toURI());
		} catch (Exception e) {
			System.out.println("WS 上傳檢驗檔失敗:" + inspectionFile);
		}

	}

	public static void main(String args[]) throws Exception {
		// unlinkFile("D:\\temp\\kitchen");
	}

	/**
	 * 驗證菜單必填欄位
	 * 
	 * @param request
	 */
	private void validateMenuFields(UploadMenuRequestType request,HashMap<String, DishInfo> dishbatchdataArray) {
		if (request == null) {
			errorMsg.add("request物件為空");
			return;
		}

		if (CateringServiceUtil.isEmpty(request.getServiceDate())) {
			errorMsg.add("缺少必填欄位[供餐日期]");
		}

		if (CateringServiceUtil.isEmpty(request.getSchoolId())) {
			errorMsg.add("缺少必填欄位[學校編號]");
		}
		//增加dishlist誤件的判斷  20140925 KC
		//if (CateringServiceUtil.isNull(request.getStaple1())){
		if (CateringServiceUtil.isNull(request.getStaple1()) && !dishbatchdataArray.containsKey(CateringServiceCode.DISHTYPE_MAINFOOD)) {
			errorMsg.add("缺少必填欄位[主食1]");
		}
		//增加dishlist誤件的判斷  20140925 KC
		//if (CateringServiceUtil.isNull(request.getMainCourse1())) {
		if (CateringServiceUtil.isNull(request.getMainCourse1()) && !dishbatchdataArray.containsKey(CateringServiceCode.DISHTYPE_MAINDISH)) {
			errorMsg.add("缺少必填欄位[主菜1]");
		}

		if (CateringServiceUtil.isNull(request.getGrains())) {
			errorMsg.add("缺少必填欄位[全榖根莖類]");
		}

		if (CateringServiceUtil.isNull(request.getMeatBeans())) {
			errorMsg.add("缺少必填欄位[豆魚肉蛋類]");
		}

		if (CateringServiceUtil.isNull(request.getVegetables())) {
			errorMsg.add("缺少必填欄位[蔬菜類]");
		}

		if (CateringServiceUtil.isNull(request.getOils())) {
			errorMsg.add("缺少必填欄位[油脂及堅果種子]");
		}

		if (CateringServiceUtil.isNull(request.getFruits())) {
			errorMsg.add("缺少必填欄位[水果類]");
		}

		if (CateringServiceUtil.isNull(request.getMilk())) {
			errorMsg.add("缺少必填欄位[乳品類]");
		}

		if (CateringServiceUtil.isNull(request.getCalories())) {
			errorMsg.add("缺少必填欄位[熱量]");
		}
	}

	/**
	 * 驗證食材必填欄位
	 */
	private void validateIngredientFields(UploadIngredientRequestType request) {
		if (CateringServiceUtil.isNull(request.getSources())) {
			errorMsg.add("缺少必填欄位[食材資訊集]");
			return;
		}

		Sources source = request.getSources();
		if (CateringServiceUtil.isNull(source.getSourceItem())) {
			errorMsg.add("缺少必填欄位[食材資訊]");
			return;
		}

		for (int i = 0; i < source.getSourceItem().size(); i++) {
			SourceItem item = source.getSourceItem().get(i);
			
			int rownum = (i+1);
			
			if(CateringServiceUtil.isEmpty(item.getDishesName())){
				errorMsg.add("第["+ rownum +"]筆食材資訊,缺少必填欄位[菜色名稱]");
			}
			
			if(CateringServiceUtil.isEmpty(item.getSourceName())){
				errorMsg.add("第["+ rownum +"]筆食材資訊,缺少必填欄位[食材名稱]");
			}
			
			if(CateringServiceUtil.isEmpty(item.getSource())){
				errorMsg.add("第["+ rownum +"]筆食材資訊,缺少必填欄位[來源地]");
			}
			
			if(CateringServiceUtil.isEmpty(item.getSupplierId())){
				errorMsg.add("第["+ rownum +"]筆食材資訊,缺少必填欄位[供應商代碼]");
			}
			
			if(CateringServiceUtil.isEmpty(item.getSupplierName())){
				errorMsg.add("第["+ rownum +"]筆食材資訊,缺少必填欄位[供應商名稱]");
			}
			
			//20140924 Raymond 過濾如果為調味料就不檢查進貨日期
			if(CateringServiceUtil.isEmpty(item.getStockDate())){
				if(!CateringServiceUtil.isEmpty(item.getDishesName()) && !item.getDishesName().equals(CateringServiceCode.CODETYPE_SEASONING))
					errorMsg.add("第["+ rownum +"]筆食材資訊,缺少必填欄位[食材進貨日期]");
			}
	
		}
	}
	
	/*
	 * 檢查是否超過上傳時間  20140910 KC 
	 * */
	public void checkMenuUploadTime(Session session,String menuDate,Integer schoolId) throws Exception {
		Timestamp nowTS=CateringServiceUtil.getCurrentTimestamp();
		String limitString=SchoolAndKitchenUtil.queryUploadLimitTimeBySchoolid(session,schoolId);
		if ("".equals(limitString)){
			return;
		}
		menuDate = menuDate.replace("/", "-"); //因應SOAP傳入值日期格式為yyyy/mm/dd 先行轉換成yyyy-mm-dd 以利後續判斷 add by ellis 20150119
		Boolean flagTimelimit= CateringServiceUtil.isUploadTime(CateringServiceUtil.convertStrToTimestamp("yyyy-MM-dd", menuDate),nowTS, limitString);
		if (!flagTimelimit){
			System.out.println("test");
			errorMsg.add("已超過資料上傳時間! 學校所屬縣市上傳限制時間為菜單日期後"+limitString+"小時! 您所上傳的資料菜單日期為"+menuDate+"已無法上傳。");
			throw new Exception (StatusCode.WS_MSG_SERVER_TIMEOUT);
		}
	}
}
