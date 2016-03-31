package org.iii.ideas.catering_service.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.iii.ideas.catering_service.code.SourceTypeCode;
import org.iii.ideas.catering_service.common.Common;
import org.iii.ideas.catering_service.dao.IngredientbatchdataDAO;
import org.iii.ideas.catering_service.dao.NewsattachfilesDAO;
import org.iii.ideas.catering_service.dao.UploadFileDAO;
import org.iii.ideas.catering_service.file.object.FileReturnObject;
import org.iii.ideas.catering_service.rest.bo.FileBO;
import org.iii.ideas.catering_service.rest.bo.FileNewsBO;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.DateUtil;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * @author 2014/06/26 Raymond
 * 
 */
public class UploadFileService extends BaseService {
	protected Logger log = Logger.getLogger(this.getClass());

	public UploadFileService() {
	};

	public UploadFileService(Session dbSession) {
		setDbSession(dbSession);
	};
	
	UploadFileDAO ufDao;
	NewsattachfilesDAO nafDao;
	Common comm;

	public FileReturnObject uploadInspectionReportV2(String[] param, MultipartFile file, Integer kitchenId) throws Exception {
		IngredientbatchdataDAO ibdDao = new IngredientbatchdataDAO(dbSession);

		if (param.length != 7) {
			return saveReturnString(0, "傳入參數錯誤(001)");
		}

		String menuDate = param[1];
		Long ingredientId = Long.parseLong(param[2]);
		String supplierCompanyId = param[3];
		String lotNumber = param[4];
		String stockDate = param[5];

		if (CateringServiceUtil.isEmpty(menuDate))
			return saveReturnString(0, "傳入參數錯誤(菜單日期)");
		if (CateringServiceUtil.isNull(ingredientId))
			return saveReturnString(0, "傳入參數錯誤(食材資訊)");
		if (CateringServiceUtil.isEmpty(supplierCompanyId))
			return saveReturnString(0, "傳入參數錯誤(供應商資訊)");
//		if (CateringServiceUtil.isEmpty(stockDate))
//			return saveReturnString(0, "傳入參數錯誤(進貨日期)");

		// file name role = MD5(kitchenId + supplierCompanyId + ingredientId +
		// timestamp)
		comm = new Common();
		
		String mimeType = file.getContentType();
		String extType = FilenameUtils.getExtension(file.getOriginalFilename());
		String uploadPatch = CateringServiceUtil.getConfig("uploadPath") + "inspect/" + kitchenId + "/";
		String encodeFileName = comm.getEncoderByMd5(kitchenId + supplierCompanyId + ingredientId + Calendar.getInstance().getTimeInMillis()) + "." + extType.toLowerCase();
		String fullFileName = CateringServiceUtil.getInspectionFileNameV2(uploadPatch, encodeFileName);
		String originalFileName = FilenameUtils.getName(file.getOriginalFilename()); //add originalFileName add by 
		// 存檔案
		saveFile(file, fullFileName);

		// 取所有的ingredientbatchid
		List<Long> batchdataIdList = ibdDao.queryIngredientbatchIdList(kitchenId, menuDate, ingredientId, supplierCompanyId, lotNumber, stockDate);
		if (batchdataIdList == null)
			return null;

		// insert uploadfile data

		try {
			ufDao = new UploadFileDAO(dbSession);
			for (Long batchdataId : batchdataIdList) {
				FileBO fileBo = new FileBO();
				fileBo.setFilePath(uploadPatch);
				fileBo.setMimeType(mimeType);
				fileBo.setExtType(extType);
				fileBo.setEncodeFileName(encodeFileName);
				fileBo.setSourceType(SourceTypeCode.INGREDIENT_INSPECTION);
				fileBo.setTargetId(String.valueOf(batchdataId));
				fileBo.setOriginalFileName(originalFileName);
				ufDao.saveUploadFile(fileBo);
			}
//			tx.commit();
		} catch (Exception e) {
//			tx.rollback();
			e.printStackTrace();
		}

		// 回傳正確訊息
		return saveReturnString(1, "",fullFileName);
	}
	
	/** add by Joshua 2014/10 */
	public FileReturnObject uploadCateringCompanyLogo(MultipartFile file, Integer kitchenId) throws Exception {
		comm = new Common();
		String mimeType = file.getContentType();
		String extType = FilenameUtils.getExtension(file.getOriginalFilename());
		String uploadPatch = CateringServiceUtil.getConfig("uploadPath") + "kitchenLogo/" + kitchenId + "/";
		String encodeFileName = comm.getEncoderByMd5(String.valueOf(kitchenId) + Calendar.getInstance().getTimeInMillis()) + "." + extType.toLowerCase();
		String fullFileName = CateringServiceUtil.getInspectionFileNameV2(uploadPatch, encodeFileName);
		String originalFileName = FilenameUtils.getName(file.getOriginalFilename()); //add originalFileName add by 
		
		// 存檔案
		saveFile(file, fullFileName);
		
		try {
			ufDao = new UploadFileDAO(dbSession);
			FileBO fileBo = new FileBO();
			fileBo.setFilePath(uploadPatch);
			fileBo.setMimeType(mimeType);
			fileBo.setExtType(extType);
			fileBo.setEncodeFileName(encodeFileName);
			fileBo.setSourceType(SourceTypeCode.CATERING_COMPANY_LOGO);
			fileBo.setTargetId(String.valueOf(kitchenId));
			fileBo.setOriginalFileName(originalFileName);
			ufDao.saveUploadFile(fileBo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 回傳正確訊息
		return saveReturnString(1, "",fullFileName);		
	}
	/** add by Joshua 2014/10 */
	public FileReturnObject uploadDishImg(MultipartFile file, Long dishId, Integer kitchenId) throws Exception {
		comm = new Common();
		String mimeType = file.getContentType();
		String extType = FilenameUtils.getExtension(file.getOriginalFilename());
		String uploadPatch = CateringServiceUtil.getConfig("uploadPath") + "dishImage/" + kitchenId + "/";
		String encodeFileName = comm.getEncoderByMd5(String.valueOf(dishId) + Calendar.getInstance().getTimeInMillis()) + "." + extType.toLowerCase();
		String fullFileName = CateringServiceUtil.getInspectionFileNameV2(uploadPatch, encodeFileName);
		String originalFileName = FilenameUtils.getName(file.getOriginalFilename()); //add originalFileName add by 
		
		// 存檔案
		saveFile(file, fullFileName);
		
		try {
			ufDao = new UploadFileDAO(dbSession);
			FileBO fileBo = new FileBO();
			fileBo.setFilePath(uploadPatch);
			fileBo.setMimeType(mimeType);
			fileBo.setExtType(extType);
			fileBo.setEncodeFileName(encodeFileName);
			fileBo.setSourceType(SourceTypeCode.DISH_DATA_MAINTENANCE);
			fileBo.setTargetId(String.valueOf(dishId));
			fileBo.setOriginalFileName(originalFileName);
			ufDao.saveUploadFile(fileBo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 回傳正確訊息
		return saveReturnString(1, "",fullFileName);	
	}
	
	/** add by s5130112ie 2015/11 */
	public FileReturnObject uploadNewsFile(MultipartFile file, Integer newsId) throws Exception {
		comm = new Common();
//		String mimeType = file.getContentType();
		String extType = FilenameUtils.getExtension(file.getOriginalFilename());
		String uploadPatch = CateringServiceUtil.getConfig("uploadPath") + "news/" + newsId + "/";
		
		//TODO encodeFileName需要改
		String encodeFileName = comm.getEncoderByMd5(String.valueOf(Calendar.getInstance().getTimeInMillis())) + "." + extType.toLowerCase();
		String fullFileName = CateringServiceUtil.getInspectionFileNameV2(uploadPatch, encodeFileName);
		String originalFileName = FilenameUtils.getName(file.getOriginalFilename()); //add originalFileName add by 
		
		// 存檔案
		saveFile(file, fullFileName);
		
		try {
			nafDao = new NewsattachfilesDAO(dbSession);
			FileNewsBO fileNewsBo = new FileNewsBO();
			fileNewsBo.setFileDesc(originalFileName);
			fileNewsBo.setFilePath(uploadPatch);
			fileNewsBo.setFileSize((int)file.getSize());//TODO
			fileNewsBo.setName(encodeFileName);
			fileNewsBo.setNewsId(newsId);
			nafDao.saveNewsattachfiles(fileNewsBo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 回傳正確訊息
		return saveReturnString(1, "",fullFileName);	
	}
	
	/**
	 * 備份舊檔案
	 * 
	 * @param filename
	 * @return
	 */
	public static File backupFile(String filename) {
		String oldFileName = "";
		File bkFile = new File(filename);
		if (bkFile.exists()) {
			oldFileName = bkFile.getName();
			String[] oldFile = oldFileName.split("\\.");
			if (oldFile.length > 0) {
				// 原始檔名+時間+副檔名
				String bkFileName = oldFile[0] + "_" + DateUtil.getSystemDate("yyyyMMddHHmmssSSS") + "." + oldFile[1];
				// 重新命名
				bkFile.renameTo(new File(bkFile.getParent() + File.separator + bkFileName));
			}
		}
		return bkFile;
	}

	/*
	 * 將資料存入磁碟中
	 */
	@SuppressWarnings("unused")
	private void saveFile(MultipartFile file, String filename) throws Exception {

		if (!file.isEmpty()) {
			byte[] bytes = file.getBytes();
			if (file == null) {
				saveReturnString(0, "redirect:uploadFailure file is null");
				return;
			}
			if (file.getSize() > 30000000) {
				saveReturnString(0, "redirect:uploadFailure filesize is more then 30M");
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

	public FileReturnObject saveReturnString(int i, String message) {
		/*
		 * 20140410 KC 此controller為spring註冊之bean 其叫用應該為static 在此修改回傳物件統一new
		 * FileReturnObject 回傳
		 * 避免不同檔案上傳取得FileReturnObject的方式不同，而造成不同client回傳到相同物件
		 */
		FileReturnObject fileReturnObj = new FileReturnObject();
		fileReturnObj.setRetMsg(message);
		fileReturnObj.setRetStatus(i);
		return fileReturnObj;

	}
	/** add by Ellis 2014/11/28 **/
	public FileReturnObject saveReturnString(int i, String message,String fullFileName) {
		/*
		 * 20140410 KC 此controller為spring註冊之bean 其叫用應該為static 在此修改回傳物件統一new
		 * FileReturnObject 回傳
		 * 避免不同檔案上傳取得FileReturnObject的方式不同，而造成不同client回傳到相同物件
		 */
		FileReturnObject fileReturnObj = new FileReturnObject();
		fileReturnObj.setRetMsg(message);
		fileReturnObj.setRetStatus(i);
		fileReturnObj.setFullFileName(fullFileName);
		return fileReturnObj;

	}
	
	

	public void SaveFileFromInputStream(InputStream stream, String filename) throws IOException {
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
