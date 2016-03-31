package org.iii.ideas.catering_service.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.iii.ideas.catering_service.code.SourceTypeCode;
import org.iii.ideas.catering_service.dao.UploadFileDAO;
import org.iii.ideas.catering_service.dao.Uploadfile;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DownloadFileController {

	protected Logger log = Logger.getLogger(this.getClass());
	private String uploadPatch = "";

	// FileReturnObject fileReturnObj = new FileReturnObject();

	// ---------------------download jpg/pdf--------
	private static final int BUFFER_SIZE = 4096;

	@RequestMapping(value = {"/SHOW/{file_name}"}, method = RequestMethod.GET)
	public void doDownloadImg(@PathVariable("file_name") String func,HttpServletRequest request, HttpServletResponse response) throws Exception {
		doDownloadImg(func,"",request,response);
	}
	
	@RequestMapping(value = {"/SHOW/{file_name}/{param}"}, method = RequestMethod.GET)
	public void doDownloadImg(@PathVariable("file_name") String func,@PathVariable("param") String param ,HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession httpsession = request.getSession();

		Integer kid = null;
		if (httpsession.getAttribute("uName") != null) {
			kid = Integer.valueOf((String) httpsession.getAttribute("account"));
		}

		String[] funcArg = func.split("\\|");
		String funcname = funcArg[0].toString().trim().toUpperCase();

		SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
		Session session = sessionFactory.openSession();
		String filepath = "";
		// String fullPath="";
		String filetype = "image/jpeg";
		this.uploadPatch = CateringServiceUtil.getConfig("uploadPath");
		// 菜色圖檔下載
		if (funcname.equals("DISHID")) {
			Integer kitchenId = Integer.valueOf(funcArg[1].toString().trim());
			Long dishId = Long.valueOf(funcArg[2].toString().trim());
			Integer imgHigh = null;
			Integer imgWidth = null;
			// 如果介面有限定size 就同時產生一個相同size 的圖檔以控制流量
			if (funcArg.length > 3) {
				if (funcArg[3] != null) {
					imgHigh = Integer.valueOf(funcArg[3].toString().trim());
				}
				if (funcArg[4] != null) {
					imgWidth = Integer.valueOf(funcArg[4].toString().trim());
				}
			}

/** 		Joshua add 2014/10/14	*/
			UploadFileDAO ufDao = new UploadFileDAO(session);
			Uploadfile uf = ufDao.getSingleUploadfile(SourceTypeCode.DISH_DATA_MAINTENANCE, String.valueOf(dishId));
			if(uf != null){
				filepath = uf.getFilePath() + uf.getEncodeFileName();
				filetype = uf.getMimeType();
				
				// 進行resize image 首頁菜單查詢菜色示意圖顯示
				if (imgWidth != null && imgHigh != null) {
					if (CateringServiceUtil.fileExists(filepath)) {
						String resizeFilePath = CateringServiceUtil.getResizeDishImageFilePath(filepath, imgHigh, imgWidth);
						if (CateringServiceUtil.resizeImageWithHint(filepath, resizeFilePath, imgHigh, imgWidth)) {
							filepath = resizeFilePath;
						}
					}
				}
			}else{
/**			Joshua edit 2014/10/16  判斷圖示 新舊方法並存   */  
				String filepathOld = this.uploadPatch + "dish/" + kitchenId + "/" + kitchenId + "_" + dishId;
				String filepathNew = "";// CateringServiceUtil.getDishImageFileName(kitchenId,
										// dishId, ext);
	
				String resizeFileName = null;
				// 查詢是否有被resize 的 image file
				if (imgWidth != null && imgHigh != null) {
					resizeFileName = CateringServiceUtil.getDishImageFileName(kitchenId, dishId, imgHigh, imgWidth, "png");
					if (!CateringServiceUtil.fileExists(resizeFileName)) {
						resizeFileName = null;
					}
				}
				// 如果resize 檔不存在就 找原始檔並resize
				if (resizeFileName != null) {
					// 下載resize image file
					filepath = resizeFileName;
					filetype = "image/png";
				} else {
					if (CateringServiceUtil.fileExists(CateringServiceUtil.getDishImageFileName(kitchenId, dishId, "jpg"))) {
						filepath = CateringServiceUtil.getDishImageFileName(kitchenId, dishId, "jpg");
						filetype = "image/jpeg";
					} else if (CateringServiceUtil.fileExists(CateringServiceUtil.getDishImageFileName(kitchenId, dishId, "png"))) {
						filepath = CateringServiceUtil.getDishImageFileName(kitchenId, dishId, "png");
						filetype = "image/png";
					} else if (CateringServiceUtil.fileExists(CateringServiceUtil.getDishImageFileName(kitchenId, dishId, "gif"))) {
						filepath = CateringServiceUtil.getDishImageFileName(kitchenId, dishId, "gif");
						filetype = "image/gif";
					} else if (CateringServiceUtil.fileExists(filepathOld + ".jpg")) {
						filepath = filepathOld + ".jpg";
						filetype = "image/jpeg";
					} else if (CateringServiceUtil.fileExists(filepathOld + ".png")) {
						filepath = filepathOld + ".png";
						filetype = "image/png";
					} else if (CateringServiceUtil.fileExists(filepathOld + ".gif")) {
						filepath = filepathOld + ".gif";
						filetype = "image/gif";
					}
					// 進行resize image file
					if (imgWidth != null && imgHigh != null) {
						if (CateringServiceUtil.fileExists(filepath)) {
							resizeFileName = CateringServiceUtil.getDishImageFileName(kitchenId, dishId, imgHigh, imgWidth, "png");
							if (CateringServiceUtil.resizeImageWithHint(filepath, resizeFileName, imgHigh, imgWidth)) {
								filepath = resizeFileName;
								filetype = "image/png";
							}
						}
					}
				}
			}
			// 菜色圖檔下載
		} else if (funcname.equals("DISH")) {
			Long dishId = HibernateUtil.queryDishIdByName(session, kid, funcArg[2].toString().trim());
			if (dishId == null) {
				throw new Exception("找不到此菜色:" + funcname);
			}
			if (CateringServiceUtil.fileExists(this.uploadPatch + "dish/" + kid + "_" + dishId + ".gif")) {
				filepath = this.uploadPatch + "dish/" + kid + "_" + dishId + ".jpg";
			} else {
				filepath = this.uploadPatch + "dish/" + kid + "/" + kid + "_" + dishId + ".jpg";
			}
			// filepath = this.uploadPatch + "dish/"+ kid + "_" + dishId +
			// ".jpg";
			// 檢驗報告下載
		} else if (funcname.equals("INSPECT")) {
			filetype = "application/octet-stream";

			Integer kitchenId = Integer.valueOf(funcArg[1].toString().trim());
			Long ingredientBatchId = Long.valueOf(funcArg[2].toString().trim());
			// filepath = this.uploadPatch + "inspect/"+kid + "_" +
			// ingredientBatchId + ".pdf";

			if (CateringServiceUtil.fileExists(CateringServiceUtil.getInspectionFileName(kitchenId, ingredientBatchId, "pdf"))) {
				filepath = CateringServiceUtil.getInspectionFileName(kitchenId, ingredientBatchId, "pdf");
				filetype = "application/pdf";
			} else if (CateringServiceUtil.fileExists(CateringServiceUtil.getInspectionFileName(kitchenId, ingredientBatchId, "jpg"))) {
				filepath = CateringServiceUtil.getInspectionFileName(kitchenId, ingredientBatchId, "jpg");
				filetype = "image/jpeg";
			}

//			// 團膳業者圖檔下載
//		} else if (funcname.equals("KITCHEN_old")) {
//			
//			String filepathOld = this.uploadPatch + "kitchen/" + kid;
//			String filepathNew = "";// this.uploadPatch + "kitchen/"+kid+"/"+kid
//
//			if (CateringServiceUtil.fileExists(CateringServiceUtil.getKitchenImageFileName(kid, "jpg"))) {
//				filepath = CateringServiceUtil.getKitchenImageFileName(kid, "jpg");
//				filetype = "image/jpeg";
//			} else if (CateringServiceUtil.fileExists(CateringServiceUtil.getKitchenImageFileName(kid, "png"))) {
//				filepath = CateringServiceUtil.getKitchenImageFileName(kid, "png");
//				filetype = "image/png";
//			} else if (CateringServiceUtil.fileExists(CateringServiceUtil.getKitchenImageFileName(kid, "gif"))) {
//				filepath = CateringServiceUtil.getKitchenImageFileName(kid, "gif");
//				filetype = "image/gif";
//			} else if (CateringServiceUtil.fileExists(filepathOld + ".jpg")) {
//				filepath = filepathOld + ".jpg";
//				filetype = "image/jpeg";
//			} else if (CateringServiceUtil.fileExists(filepathOld + ".png")) {
//				filepath = filepathOld + ".png";
//				filetype = "image/png";
//			} else if (CateringServiceUtil.fileExists(filepathOld + ".gif")) {
//				filepath = filepathOld + ".gif";
//				filetype = "image/gif";
//			}

			// 團膳業者圖檔下載 version II
		} else if (funcname.equals("KITCHEN")){
			//20160104 shine add 如有多傳參數,則為kitchenId
			if(param != null && !param.equals("")){
				try{
					kid = Integer.parseInt(param);
				}catch(Exception e){
					
				}
			}
			// 20141009 路徑改成撈uploadFile.filePath(sourceType,targetID) by Joshua
			UploadFileDAO ufDao = new UploadFileDAO(session);
			Uploadfile uf = ufDao.getSingleUploadfile(SourceTypeCode.CATERING_COMPANY_LOGO, String.valueOf(kid));
			if(uf != null){
				filepath = uf.getFilePath() + uf.getEncodeFileName();
				filetype = uf.getMimeType();
			}else{
				
				/** 團膳業者Logo舊方法判斷   2014/10/16 Joshua*/
				String filepathOld = this.uploadPatch + "kitchen/" + kid;
				String filepathNew = "";// this.uploadPatch + "kitchen/"+kid+"/"+kid

				if (CateringServiceUtil.fileExists(CateringServiceUtil.getKitchenImageFileName(kid, "jpg"))) {
					filepath = CateringServiceUtil.getKitchenImageFileName(kid, "jpg");
					filetype = "image/jpeg";
				} else if (CateringServiceUtil.fileExists(CateringServiceUtil.getKitchenImageFileName(kid, "png"))) {
					filepath = CateringServiceUtil.getKitchenImageFileName(kid, "png");
					filetype = "image/png";
				} else if (CateringServiceUtil.fileExists(CateringServiceUtil.getKitchenImageFileName(kid, "gif"))) {
					filepath = CateringServiceUtil.getKitchenImageFileName(kid, "gif");
					filetype = "image/gif";
				} else if (CateringServiceUtil.fileExists(filepathOld + ".jpg")) {
					filepath = filepathOld + ".jpg";
					filetype = "image/jpeg";
				} else if (CateringServiceUtil.fileExists(filepathOld + ".png")) {
					filepath = filepathOld + ".png";
					filetype = "image/png";
				} else if (CateringServiceUtil.fileExists(filepathOld + ".gif")) {
					filepath = filepathOld + ".gif";
					filetype = "image/gif";
				}
			}
		}
		
		// 檢驗報告第二版
		else if (funcname.equals("INSPECT_V2")) {
			String ingredientBatchId = funcArg[1].toString().trim();
			UploadFileDAO ufDao = new UploadFileDAO(session);
			Uploadfile uf = ufDao.getSingleUploadfile(SourceTypeCode.INGREDIENT_INSPECTION, ingredientBatchId);
			if (uf != null) {
				filepath = uf.getFilePath() + uf.getEncodeFileName();
				filetype = uf.getMimeType();
			}

		// 員生消費合作社（商品圖），預設抓取各縣市底下之各商品的圖檔(gif、jpg、png)，如果都沒有圖檔時，則抓預設圖(default資料夾)下的圖檔。
		} else if (funcname.equals("SCHOOLFOODIMG")) {
			//System.out.println("員生消費合作社商品圖");
			// TODO
			Integer countyId = Integer.valueOf(funcArg[1].toString().trim());
			Integer productId = Integer.valueOf(funcArg[2].toString().trim());
			//System.out.println("員生消費合作社_countId:"+countyId);
			//System.out.println("員生消費合作社_productId:"+productId);
			//System.out.println(this.uploadPatch + "schoolfoodimg/" + countyId + "/" + productId + ".xxx");
			if (CateringServiceUtil.fileExists(this.uploadPatch + "schoolfoodimg/" + countyId + "/" + productId + ".gif")) {
				filepath = this.uploadPatch + "schoolfoodimg/" + countyId + "/" + productId + ".gif";
			} else if (CateringServiceUtil.fileExists(this.uploadPatch + "schoolfoodimg/" + countyId + "/" + productId + ".jpg")) {
				filepath = this.uploadPatch + "schoolfoodimg/" + countyId + "/" + productId + ".jpg";
			} else if (CateringServiceUtil.fileExists(this.uploadPatch + "schoolfoodimg/" + countyId + "/" + productId + ".png")) {
				filepath = this.uploadPatch + "schoolfoodimg/" + countyId + "/" + productId + ".png";
			} else {
				filepath = this.uploadPatch + "schoolfoodimg/default/" + productId + ".jpg";
			}
			
		} else {
			throw new Exception("Wrong function name:" + filepath);
		}
		File downloadFile = null;
		if (CateringServiceUtil.fileExists(filepath)) {
			downloadFile = new File(filepath);
		} else {
			ServletContext context = request.getServletContext();
			String path = context.getRealPath("/");
			// System.out.println("------------------------------"+path);
			downloadFile = new File(path + "/images/noimage.jpg");
			filetype = "image/jpeg";
		}

		FileInputStream inputStream = new FileInputStream(downloadFile);
		// set content attributes for the response
		response.setContentType(filetype);
		response.setContentLength((int) downloadFile.length());

		// set headers for the response
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
		response.setHeader(headerKey, headerValue);

		// get output stream of the response
		OutputStream outStream = response.getOutputStream();

		byte[] buffer = new byte[BUFFER_SIZE];
		int bytesRead = -1;

		// write bytes read from the input stream into the output stream
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}
		inputStream.close();
		outStream.close();
		session.close();
	}

	/*
	 * 增加excel下載 20140520 KC
	 */
	@RequestMapping(value = "/SYSFILE/{file_name}", method = RequestMethod.GET)
	public void doDownloadSysFile(@PathVariable("file_name") String filepath, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession httpsession = request.getSession();
		System.out.println("SYSFILE");

		Integer kid = null;
		if (httpsession.getAttribute("uName") != null) {
			kid = Integer.valueOf((String) httpsession.getAttribute("account"));
		}

		//SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
		//Session session = sessionFactory.openSession();
		String filetype = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"; // xlsx

		filepath = filepath.replace("|", "/");
		filepath = filepath.replace("-", ".");
		File downloadFile = null;
		if (CateringServiceUtil.fileExists(filepath)) {
			downloadFile = new File(filepath);
			// System.out.println("*"+downloadFile.getName());
		} else {
			// System.out.println(filepath);
			return;
		}

		System.out.println(downloadFile.getName());

		FileInputStream inputStream = new FileInputStream(downloadFile);
		// set content attributes for the response
		response.setContentType(filetype);
		response.setContentLength((int) downloadFile.length());

		// set headers for the response
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName().replace("/", "_"));
		response.setHeader(headerKey, headerValue);

		// get output stream of the response
		OutputStream outStream = response.getOutputStream();

		byte[] buffer = new byte[BUFFER_SIZE];
		int bytesRead = -1;

		// write bytes read from the input stream into the output stream
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}
		inputStream.close();
		outStream.close();
		//session.close();
	}
	
	/*
	 * 增加excel下載 20140910 KC 
	 */
	@RequestMapping(value = "/USER/{download_type}/{fileKey}/", method = RequestMethod.GET)
	public void doDownloadUserFile(@PathVariable("download_type") String download_type,@PathVariable("fileKey") String fileKey,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("test");
		HttpSession httpsession = request.getSession();
		Integer kid =-1;
		if (httpsession.getAttribute("uName") != null) {
			kid = Integer.valueOf((String) httpsession.getAttribute("account"));
		}
		String filepath=CateringServiceUtil.getFilePath(download_type, kid, fileKey);
		

		//SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
		//Session session = sessionFactory.openSession();
		String filetype = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"; // xlsx

		File downloadFile = null;
		if (CateringServiceUtil.fileExists(filepath)) {
			downloadFile = new File(filepath);
			// System.out.println("*"+downloadFile.getName());
		} else {
			// System.out.println(filepath);
			//應該要回傳 file unavalibale page 以後再補  KC
			return;
		}

		//System.out.println(downloadFile.getName());

		FileInputStream inputStream = new FileInputStream(downloadFile);
		// set content attributes for the response
		response.setContentType(filetype);
		response.setContentLength((int) downloadFile.length());

		//下載檔案檔名 (副檔名之後在修 先寫死 KC)		
		//String downloadFileName="file_"+CateringServiceUtil.getCurrentTimestamp().toString()+".xlsx";
		String downloadFileName="download_"+CateringServiceUtil.getCurrentTimestamp().toString()+".xlsx";
		
		// set headers for the response
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", downloadFileName);
		response.setHeader(headerKey, headerValue);

		// get output stream of the response
		OutputStream outStream = response.getOutputStream();

		byte[] buffer = new byte[BUFFER_SIZE];
		int bytesRead = -1;

		// write bytes read from the input stream into the output stream
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}
		inputStream.close();
		outStream.close();
		//session.close();
	}
	
}
