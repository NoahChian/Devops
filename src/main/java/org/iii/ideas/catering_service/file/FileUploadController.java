package org.iii.ideas.catering_service.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.iii.ideas.catering_service.dao.Dish;
import org.iii.ideas.catering_service.file.object.FileReturnObject;
import org.iii.ideas.catering_service.service.IngredientBatchDataXlsService;
import org.iii.ideas.catering_service.service.SchKitchenUserService;
import org.iii.ideas.catering_service.service.UploadFileService;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.DateUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;
import org.iii.ideas.catering_service.util.LogUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadController {

	protected Logger log = Logger.getLogger(this.getClass());
	// private String uploadPatch = "";
	private int retStatus = 0;
	private String retMsg = "";
	private String uName = "";
	private String filename = "";
	FileReturnObject fileReturnObj = new FileReturnObject();
	// private Integer imageSizeLimit = 1024 * 500; //500k
	private Integer imageSizeLimit = 1024 * 1024 * 3; // 3MB 2014/09/01 Raymond
														// 上線後改限制為3MB
	private Integer fileSizeLimit = 1024 * 1024 * 2; // 2MB
														// 避免檔案過大導致POI讀取時間拉長而系統癱瘓，限制2MB
														// add by ellis 20150316
	private Integer inspectionSizeLimit = 1024 * 1024 * 30; // 30M
	private ArrayList<String> ExcelExtenstion = new ArrayList<String>(
			Arrays.asList(CateringServiceCode.FileTypeOfMenuExcel,
					CateringServiceCode.FileTypeOfSupplierExcel,
					CateringServiceCode.FileTypeOfIngredientExcel,
					CateringServiceCode.FileTypeOfDishExcel,
					CateringServiceCode.FileTypeOfSchoolKitchenAccountExcel));
	private ArrayList<String> ImageExtenstion = new ArrayList<String>(
			Arrays.asList(CateringServiceCode.FileTypeOfDishIdImage,
					CateringServiceCode.FileTypeOfDishImage,
					CateringServiceCode.FileTypeOfKitchenImage));
	private ArrayList<String> InspectionExtenstion = new ArrayList<String>(
			Arrays.asList(CateringServiceCode.FileTypeOfInspection,
					CateringServiceCode.FileTypeOfInspectionV2));
	private ArrayList<String> NewsExtenstion = new ArrayList<String>(
			Arrays.asList(CateringServiceCode.FileTypeOfNews));

	// private static Integer testInt=0;

	@ResponseBody
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public FileReturnObject handleFormUpload(@RequestParam("func") String func, @RequestParam("overWrite") String overWrite, @RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws IOException {
		// testInt=testInt+1;
		// System.out.println("進入file upload controller : "+Integer.toString(testInt));

		// 每次client process都要有自己的returnObj 20140410 KC
		FileReturnObject impFileReturnObj = new FileReturnObject();
		impFileReturnObj.setRetMsg("");
		impFileReturnObj.setRetStatus(1);
		this.fileReturnObj.setTestInt(this.fileReturnObj.getTestInt() + 1);

		String userType = "";
		String userName = "";
		String funcname = "";
		Integer county = 0;
		HttpSession httpsession = request.getSession();
		log.info("***Upload File:" + file.getOriginalFilename());
		Integer kitchenId = null;
		if (httpsession.getAttribute("uName") != null) {
			String userkitchenId = (String) httpsession.getAttribute("account");
			if (userkitchenId == null) {
				return saveReturnString(0, "使用者未登入");
			}
			kitchenId = Integer.valueOf(userkitchenId);
			uName = httpsession.getAttribute("uName").toString();
		} else {
			return saveReturnString(0, "使用者未登入");
		}
		//userName = (String) httpsession.getAttribute("userName");
		userName = (String) httpsession.getAttribute("uName"); //modify by ellis 20141128
		userType = (String) httpsession.getAttribute("uType");
		county = Integer.valueOf((String) httpsession.getAttribute("county"));
		// 20140513_Raymond_改只限定007不可上傳 其他都可以
//		if (userType.equals(CateringServiceUtil.KitchenType007)) {
//			log.debug("請確認使用者身份:" + userName + " 身份:" + userType);
//			return saveReturnString(0, "請確認使用者身份"); // 統一由這個method回傳 20140410 KC
//			// return returnString();
//		}

		SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		java.util.Date date = new java.util.Date();
		UploadFileService ufService = new UploadFileService(session);

		try {
			Timestamp ts = new Timestamp(date.getTime());
			String currentDay = CateringServiceUtil.converTimestampToStr("yyyyMMddHHmmssSSS", ts);

			log.debug("upload file func:" + func);
			String[] funcArg = func.split("\\|");
			funcname = funcArg[0].toString().trim().toUpperCase();

			// String funcname="";
			this.returnString().setRetMsg("");
			String filepath = "";
			// this.uploadPatch = CateringServiceUtil.getConfig("uploadPath");
			String ext = FilenameUtils.getExtension(file.getOriginalFilename());
			log.info("***Upload 功能:" + funcname + " 原始檔名:" + file.getOriginalFilename());
			// 是否為Excel 格式
			if (ExcelExtenstion.contains(funcname)) {
				if (ext.toLowerCase().equals("xlsx") == false) {
					LogUtil.writeFileUploadLog("0", uName, "確認檔案格式為  xlsx 目前格式:" + ext, filename, funcname);
					return saveReturnString(0, "確認檔案格式為  xlsx 目前格式:" + ext);
				}
				ExcelParser excelParser = new ExcelParser();

				if (CateringServiceCode.FileTypeOfMenuExcel.equals(funcname)) {// 學校菜單excel檔上傳
					if (file.getSize() > this.fileSizeLimit) {
						LogUtil.writeFileUploadLog("0", uName, "檔案大小請勿超過 2MB", filename, funcname);
						return saveReturnString(0, "檔案大小請勿超過 2MB");
					}
					filename = CateringServiceUtil.getSchoolMenuExcelFileName(kitchenId, currentDay, ext);
					// 將檔案存入主機指定目錄
					if (overWrite.trim().equals("1")) {
						filename = CateringServiceUtil.getSchoolMenuExcelFileName(kitchenId, currentDay + "ow", ext);
					} else {
						filename = CateringServiceUtil.getSchoolMenuExcelFileName(kitchenId, currentDay, ext);
					}
					// 如果不是overwrite 就存檔,如果是就不再存
					saveFile(file, filename);

					excelParser.validExcelType(filepath + filename, funcname); // 驗證excel表頭
																				// 20140418
																				// KC
					excelParser.setKitchenId(kitchenId);
					excelParser.setUserName(userName);
					excelParser.setUserType(userType);
					excelParser.setSession(session);
					// 確認excel 檔內的資料是否要覆蓋 1=yes other=no
					if (overWrite.trim().equals("1")) {
						excelParser.setOverwrite(true);
					}
					FileReturnObject retInsDish = excelParser.impSchoolMenuXLSXFile(filepath + filename);
					// 如果status == -1 代表上傳有誤(ex.檔案內容重覆)
					if (retInsDish.getRetStatus() == -1) {
						session.close();
						// LogUtil.writeFileUploadLog(String.valueOf(retInsDish.getRetStatus()),
						// uName, retInsDish.getRetMsg(), filename, funcname);
						LogUtil.writeFileUploadLog(filename, uName, retInsDish.getRetMsg(), filename, funcname);
						return saveReturnString(retInsDish.getRetStatus(), retInsDish.getRetMsg());
					}
					// 增加回傳成功總行數訊息 20140418 KC
					retInsDish.setRetMsg(retInsDish.getRetMsg() + "菜單上傳成功,處理成功總行數:" + excelParser.successRowCount.toString());

					impFileReturnObj = saveReturnString(retInsDish.getRetStatus(), retInsDish.getRetMsg());
				} else if (CateringServiceCode.FileTypeOfSupplierExcel.equals(funcname)) { // 供應商excel檔上傳
					filename = CateringServiceUtil.getSchoolSupplierExcelFileName(kitchenId, currentDay, ext);
					// 將檔案存入主機指定目錄
					saveFile(file, filename);

					excelParser.validExcelType(filepath + filename, funcname); // 驗證excel表頭
																				// 20140418
																				// KC

					// 確認excel 檔內的資料是否要覆蓋 1=yes other=no
					if (overWrite.trim().equals("1")) {
						excelParser.setOverwrite(true);
					}
					excelParser.impSupplierXLSXFile(session, kitchenId, filepath + filename,funcArg);
					impFileReturnObj = saveReturnString(1, "供應商上傳成功,處理成功總行數:" + excelParser.successRowCount.toString());

				} else if (CateringServiceCode.FileTypeOfDishExcel.equals(funcname)) {// 菜色上傳
					filename = CateringServiceUtil.getSchoolVegetableExcelFileName(kitchenId, currentDay, ext);
					// 將檔案存入主機指定目錄
					saveFile(file, filename);
					excelParser.validExcelType(filepath + filename, funcname); // 驗證excel表頭
																				// 20140418
																				// KC
					// 確認excel 檔內的資料是否要覆蓋 1=yes other=no
					if (overWrite.trim().equals("1")) {
						excelParser.setOverwrite(true);
					}
					excelParser.impVegetableXLSXFile(session, kitchenId, filepath + filename);
					impFileReturnObj = saveReturnString(1, "菜色上傳成功,處理成功總行數:" + excelParser.successRowCount.toString());
				} else if (CateringServiceCode.FileTypeOfIngredientExcel.equals(funcname)) {// 學校食材上傳
					if (file.getSize() > this.fileSizeLimit) {
						LogUtil.writeFileUploadLog("0", uName, "檔案大小請勿超過 2MB", filename, funcname);
						return saveReturnString(0, "檔案大小請勿超過 2MB");
					}
					
					filename = CateringServiceUtil.getSchoolIngredientExcelFileName(kitchenId, currentDay, ext);
					System.out.println(file.getSize());
					// 將檔案存入主機指定目錄
					saveFile(file, filename);
					excelParser.validExcelType(filepath + filename, funcname); // 驗證excel表頭0140418KC
					
					//判斷是否為V2版本
					boolean version2 = excelParser.isIngredientV2(filepath + filename);
					
					//判斷是否為V3版本
					boolean version3 = excelParser.isIngredientV3(filepath + filename);
					
					//判斷是否為V3版本
					boolean version4 = excelParser.isIngredientV4(filepath + filename);
					
					
					// 確認excel 檔內的資料是否要覆蓋 1=yes other=no
					if (overWrite.trim().equals("1")) {
						excelParser.setOverwrite(true);
					}
					// Raymond 2014/05/12
					// excelParser.impSchoolIngredientXLSXFile(session,
					// userType, kitchenId, filepath + filename);

					IngredientBatchDataXlsService ibdXlsService = new IngredientBatchDataXlsService(session);
					//20140724設定Excel版本
					ibdXlsService.setVer2(version2);
					ibdXlsService.setVer3(version3);
					ibdXlsService.setVer4(version4);
					
					String errMsg = ibdXlsService.uploadXlsFile(filepath + filename, kitchenId);
					int success = ibdXlsService.getSuccess();
					int fail = ibdXlsService.getFail();
					impFileReturnObj = saveReturnString(1, "食材上傳完畢,處理成功總行數:" + String.valueOf(success) + " 失敗: " + String.valueOf(fail) + "</br>" + errMsg);
				} else if (CateringServiceCode.FileTypeOfSchoolKitchenAccountExcel.equals(funcname)) {
					filename = CateringServiceUtil.getSchoolIngredientExcelFileName(kitchenId, currentDay, ext);
					// 將檔案存入主機指定目錄
					saveFile(file, filename);
					excelParser.validExcelType(filepath + filename, funcname); // 驗證excel表頭20140418KC

					SchKitchenUserService skUserService = new SchKitchenUserService(session);
					String errMsg = skUserService.uploadSchKitUserFromXls(filepath + filename, county, userName, userType);
					int success = skUserService.getSuccess();
					int fail = skUserService.getFail();
					impFileReturnObj = saveReturnString(1, "學校廚房帳號上傳完畢,處理成功總行數:" + String.valueOf(success) + " 失敗: " + String.valueOf(fail) + "</br>" + errMsg);

				}
			} else if (ImageExtenstion.contains(funcname)) { // 是否為Image 格式
				if ((ext.toLowerCase().equals("jpg") || ext.toLowerCase().equals("png") || ext.toLowerCase().equals("gif")) == false) {
					LogUtil.writeFileUploadLog("0", uName, "確認檔案格式為 jpg/png/gif 目前格式:" + ext.toLowerCase(), filename, funcname);
					return saveReturnString(0, "確認檔案格式為 jpg/png/gif 目前格式:" + ext.toLowerCase());
				}

				if (file.getSize() > this.imageSizeLimit) {
					LogUtil.writeFileUploadLog("0", uName, "檔案大小請勿超過 3MB", filename, funcname);
					return saveReturnString(0, "檔案大小請勿超過 3MB");
				}

				if (CateringServiceCode.FileTypeOfDishImage.equals(funcname) || CateringServiceCode.FileTypeOfDishIdImage.equals(funcname)) {// 菜色圖檔上傳
					Long dishId = (long) 0;
					if (CateringServiceCode.FileTypeOfDishImage.equals(funcname)) {
						dishId = HibernateUtil.queryDishIdByName(session, kitchenId, funcArg[1].toString().trim());
					} else {
						dishId = Long.valueOf(funcArg[1].toString().trim());
					}

					Dish dish = HibernateUtil.queryDishById(session, dishId);
					if (dish == null) {
						LogUtil.writeFileUploadLog("0", uName, "找不到此菜色:" + funcname, filename, funcname);
						return saveReturnString(0, "找不到此菜色:" + funcname);
					}
/**					add by Joshua 2014/10/13   */
					impFileReturnObj = ufService.uploadDishImg(file, dishId, kitchenId);
					/** add by Ellis 2014/11/28 **/
					filename = impFileReturnObj.getFullFileName();
/**		edit by Joshua  2014/10/13
					filename = CateringServiceUtil.getDishImageFileName(kitchenId, dishId, ext);
					// 存入檔案代碼,確認檔案已上傳
					dish.setPicturePath(filename);
					session.update(dish);
					saveFile(file, filename);
					// 新增一個菜色小圖給日曆介面使用
					String resizeFileName = CateringServiceUtil.getDishImageFileName(kitchenId, dishId, CateringServiceUtil.DishImageSizeHigh, CateringServiceUtil.DishImageSizeWidth, "png");
					CateringServiceUtil.resizeImageWithHint(filename, resizeFileName, CateringServiceUtil.DishImageSizeHigh, CateringServiceUtil.DishImageSizeWidth);

					impFileReturnObj = saveReturnString(this.returnString().getRetMsg().equals("") ? 1 : 0, this.returnString().getRetMsg());
*/
/**		重複判斷
				} else if (CateringServiceCode.FileTypeOfDishIdImage.equals(funcname)) {// 菜色圖檔上傳
					Long dishId = Long.valueOf(funcArg[1].toString().trim());
					Dish dish = HibernateUtil.queryDishById(session, dishId);
					if (dishId == null) {
						LogUtil.writeFileUploadLog("0", uName, "找不到此菜色:" + funcname, filename, funcname);
						return saveReturnString(0, "找不到此菜色:" + funcname);
					}
					filename = CateringServiceUtil.getDishImageFileName(kitchenId, dishId, ext);
					// 存入檔案代碼,確認檔案已上傳
					dish.setPicturePath(dishId.toString());
					session.update(dish);
					saveFile(file, filename);
					// 新增一個菜色小圖給日曆介面使用
					String resizeFileName = CateringServiceUtil.getDishImageFileName(kitchenId, dishId, CateringServiceUtil.DishImageSizeHigh, CateringServiceUtil.DishImageSizeWidth, "png");
					CateringServiceUtil.resizeImageWithHint(filename, resizeFileName, CateringServiceUtil.DishImageSizeHigh, CateringServiceUtil.DishImageSizeWidth);
					impFileReturnObj = saveReturnString(this.returnString().getRetMsg().equals("") ? 1 : 0, this.returnString().getRetMsg());
*/
				} else if (CateringServiceCode.FileTypeOfKitchenImage.equals(funcname)) {
					//20160104 shine add 如有多傳參數,則為kitchenId
					if(funcArg.length>1){
						try{
							kitchenId = Integer.parseInt(funcArg[1]);
						}catch(Exception e){
							
						}
					}
					// add by Joshua
					impFileReturnObj = ufService.uploadCateringCompanyLogo(file, kitchenId);
					/** add by Ellis 2014/11/28 **/
					filename = impFileReturnObj.getFullFileName();
					// 舊method 2014/10/13註解  Joshua
//					filename = CateringServiceUtil.getKitchenImageFileName(kitchenId, ext);
//					saveFile(file, filename);
//					impFileReturnObj = saveReturnString(1, "廚房上傳成功");
				}
			} else if (InspectionExtenstion.contains(funcname)) { // 是否為檢驗檔格式
				if (file.getSize() > this.inspectionSizeLimit) {
					LogUtil.writeFileUploadLog("0", uName, "檔案大小請勿超過 30MB", filename, funcname);
					return saveReturnString(0, "檔案大小請勿超過 30MB");
				}
				
				switch (funcname) {
				case CateringServiceCode.FileTypeOfInspection://舊版檢驗報告上傳
					if ((ext.toLowerCase().equals("jpg") || ext.toLowerCase().equals("pdf")) == false) {
						LogUtil.writeFileUploadLog("0", uName, "確認檔案格式為 jpg/pdf 目前格式:" + ext.toLowerCase(), filename, funcname);
						return saveReturnString(0, "確認檔案格式為 jpg/pdf 目前格式:" + ext);
					}
					String ingredientBatchId = funcArg[1].toString().trim();
					filename = CateringServiceUtil.getInspectionFileName(kitchenId, Long.valueOf(ingredientBatchId), ext);
					// 備份檔案
					backupFile(filename);
					// 存檔案
					saveFile(file, filename);
					impFileReturnObj = saveReturnString(this.returnString().getRetMsg().equals("") ? 1 : 0, this.returnString().getRetMsg());
					break;

				case CateringServiceCode.FileTypeOfInspectionV2:
					impFileReturnObj = ufService.uploadInspectionReportV2(funcArg, file, kitchenId);
					/** add by Ellis 2014/11/28 **/
					filename = impFileReturnObj.getFullFileName();
					break;

				}
				
			} else if (NewsExtenstion.contains(funcname)) { // 是否為檢驗檔格式
				//#11344 新聞公告管理附件上傳
				if (file.getSize() > this.imageSizeLimit) {
					LogUtil.writeFileUploadLog("0", uName, "檔案大小請勿超過 "+this.imageSizeLimit+"MB", filename, funcname);
					return saveReturnString(0, "檔案大小請勿超過 "+this.imageSizeLimit+"MB");
				}
				
				String newsId = funcArg[1].toString().trim();
//				filename = CateringServiceUtil.getNewsFileName(Integer.valueOf(newsId), currentDay, ext);
				
				// 存檔案
//				saveFile(file, filename);
				
//				impFileReturnObj = ufService.uploadDishImg(file, dishId, kitchenId);
				impFileReturnObj = ufService.uploadNewsFile(file, Integer.valueOf(newsId));
				
				filename = impFileReturnObj.getFullFileName();
//				impFileReturnObj = saveReturnString(this.returnString().getRetMsg().equals("") ? 1 : 0, this.returnString().getRetMsg());
			} else {

				LogUtil.writeFileUploadLog("0", uName, "Wrong function name:" + funcname, filename, funcname);
				throw new Exception("Wrong function name:" + funcname);
			}

			if (!tx.wasCommitted()) {
				tx.commit();
			}
			session.close();

			LogUtil.writeFileUploadLog(String.valueOf(impFileReturnObj.getRetStatus()), uName, impFileReturnObj.getRetMsg(), filename, funcname);
			// testInt=testInt+1;
			// System.out.println("離開file upload controller : "+Integer.toString(testInt));
			return impFileReturnObj;
			// return this.returnString();
		} catch (Exception e) {
			e.printStackTrace();
			if (!tx.wasRolledBack()) {
				tx.rollback();
			}
			session.close();
			LogUtil.writeFileUploadLog("0", uName, e.getMessage(), filename, funcname);
			return saveReturnString(0, e.getMessage());
		}
	}

	/*
	 * 將資料存入磁碟中
	 */
	private void saveFile(MultipartFile file, String filename) throws Exception {

		if (!file.isEmpty()) {
			byte[] bytes = file.getBytes();
			if (file == null) {
				saveReturnString(0, "redirect:uploadFailure file is null");
				return;
			}
			if (file.getSize() > 30000000) {
				saveReturnString(0,
						"redirect:uploadFailure filesize is more then 30M");
				return;
			}
			// 得到文件檔名
			// String filename = file.getOriginalFilename();

			if (file.getSize() > 0) {
				try {
					// 如果無此目錄就新增一筆
					/*
					 * File f = new File(filePatch); if (f.exists() &&
					 * f.isDirectory()) { //new File(filePatch).mkdirs(); }else{
					 * new File(filePatch).mkdirs(); }
					 */

					// 實際把binary stream 存到系統上
					System.out.println("test:" + filename);
					SaveFileFromInputStream(file.getInputStream(), filename);
				} catch (IOException e) {
					saveReturnString(0, e.getMessage());
					return;
				}
			} else {
				// return "redirect:uploadFailure";
				saveReturnString(0, "redirect:uploadFailure");
				return;
			}
			saveReturnString(0, "");
			return;
		} else {
			// return "save file error!";
			saveReturnString(0, "save file error!");
			return;
		}
	}

	public File backupFile(String filename) {
		// 備份舊檔案
		String oldFileName = "";

		File bkFile = new File(filename);
		if (bkFile.exists()) {
			oldFileName = bkFile.getName();
			String[] oldFile = oldFileName.split("\\.");
			if (oldFile.length > 0) {
				// 原始檔名+時間+副檔名
				String bkFileName = oldFile[0] + "_"
						+ DateUtil.getSystemDate("yyyyMMddHHmmssSSS") + "."
						+ oldFile[1];
				// 重新命名
				bkFile.renameTo(new File(bkFile.getParent() + File.separator
						+ bkFileName));
			}
		}
		return bkFile;
	}

	public FileReturnObject returnString() {
		String msg = "{\"resStatus\": " + this.retStatus + ",\"msg\": \""
				+ this.retMsg + "\"}";
		// return msg;
		return this.fileReturnObj;
	}

	public FileReturnObject saveReturnString(int i, String message) {
		/*
		 * 20140410 KC 此controller為spring註冊之bean 其叫用應該為static 在此修改回傳物件統一new
		 * FileReturnObject 回傳
		 * 避免不同檔案上傳取得FileReturnObject的方式不同，而造成不同client回傳到相同物件
		 */
		this.retStatus = i;
		this.retMsg = message;
		/*
		 * this.fileReturnObj.setRetMsg(message);
		 * this.fileReturnObj.setRetStatus(i); return this.returnString();
		 */

		FileReturnObject fileReturnObj = new FileReturnObject();
		fileReturnObj.setRetMsg(message);
		fileReturnObj.setRetStatus(i);
		this.fileReturnObj = fileReturnObj;
		return this.fileReturnObj;

	}

	public void SaveFileFromInputStream(InputStream stream, String filename)
			throws IOException {
		FileOutputStream fs = new FileOutputStream(filename);
		byte[] buffer = new byte[1024 * 1024];
		int bytesum = 0;
		int byteread = 0;
		while ((byteread = stream.read(buffer)) != -1) {
			bytesum += byteread;
			fs.write(buffer, 0, byteread);
			fs.flush();
		}
		fs.close();
		stream.close();
	}
}
