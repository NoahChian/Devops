package org.iii.ideas.catering_service.util;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.code.SourceTypeCode;
import org.iii.ideas.catering_service.dao.DishBatchData;
import org.iii.ideas.catering_service.dao.Schoolkitchen;
import org.iii.ideas.catering_service.dao.UploadFileDAO;
import org.iii.ideas.catering_service.dao.Uploadfile;
import org.iii.ideas.catering_service.dao.Useraccount;
import org.iii.ideas.catering_service.dao.UseraccountDAO;
import org.iii.ideas.catering_service.rest.bo.IngredientAttributeBO;
import org.iii.ideas.catering_service.service.BaseService;

public class CateringServiceUtil extends BaseService {
	private static Logger log = Logger.getLogger(CateringServiceUtil.class);
	private static Logger apiLog = Logger.getLogger("apiLogger");
	public final static String ColumnNameOfSeasoning = "調味料";
	public final static Integer DishImageSizeHigh = 130;
	public final static Integer DishImageSizeWidth = 150;
	public final static String KitchenType005 = "005";
	public final static String KitchenType006 = "006";
	public final static String KitchenType007 = "007";
	public static final String defaultLotNumber = "";

	public CateringServiceUtil() {
	};

	public CateringServiceUtil(Session dbSession) {
		setDbSession(dbSession);
	};

	// @Autowired(required=true)
	// private static HttpServletRequest request;

	public static String strTrim(String str) {
		if (str == null)
			return "";
		return str.trim();
	}

	public static boolean isEmpty(String str) {
		if (CateringServiceUtil.strTrim(str).equals("")) {
			return true;
		}
		return false;
	}

	public static boolean isNull(Object obj) {
		if (obj == null) {
			return true;
		}
		return false;
	}

	public static boolean fileExists(String filename) {
		return new File(filename).isFile();
	}

	public static Timestamp convertStrToTimestamp(String fmt, String dateStr) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(fmt);
		Date parsedDate = dateFormat.parse(dateStr);
		Timestamp ts = new java.sql.Timestamp(parsedDate.getTime());
		return ts;
	}

	public static String converTimestampToStr(String fmt, Timestamp ts) throws ParseException {
		String Str = new SimpleDateFormat(fmt).format(ts);
		return Str;
	}

	public static String converTimestampToStr(String fmt, Timestamp ts, Locale locale) throws ParseException {
		String Str = new SimpleDateFormat(fmt, locale).format(ts);
		return Str;
	}

	public static List querySchoolIdByKitchenId(Session session, int ciid) {
		Criteria criteriaSK = session.createCriteria(Schoolkitchen.class).add(Restrictions.eq("id.kitchenId", ciid));
		List schoolkitchens = criteriaSK.list();
		return schoolkitchens;
	}

	public static String getConfig(String arg) throws Exception {
		String ret = null;
		try {
			InitialContext initialContext = new javax.naming.InitialContext();
			ret = (String) initialContext.lookup("java:comp/env/" + arg);
		} catch (NamingException e) {
			// log.warn(e.getMessage());
			throw new Exception("系統參數 context.xml 參數未設定:" + "<Environment name=\"" + arg + "\" value=\" value \" type=\"java.lang.String\" override=\"false\" />");
		}
		return ret;
	}

	public static Timestamp getCurrentTimestamp() {
		java.util.Date date = new java.util.Date();
		return new Timestamp(date.getTime());
	}

	/*
	 * public static List querySchoolByKIdAndName(Session session, int ciid,
	 * String name){
	 * 
	 * Query query = session.createQuery(
	 * "from Kitchen k  , Schoolkitchen sk , School s where sk.id.kitchenId = k.kitchenId  and sk.id.schoolId = s.schoolId and s.schoolName = :schoolname and k.kitchenId = :kid "
	 * );
	 * 
	 * List results = query.list(); Iterator<Object[]> iterator =
	 * results.iterator(); }
	 */

	public static String getDownloadPath(String func) throws Exception {
		String fPath;
		java.util.Date date = new java.util.Date();
		Timestamp ts = new Timestamp(date.getTime());
		String currentDay = CateringServiceUtil.converTimestampToStr("yyyyMMddHHmmss", ts);
		String uploadPatch = CateringServiceUtil.getConfig("uploadPath");
		fPath = uploadPatch + "download/" + func + "_" + currentDay + ".xlsx";
		return fPath;
	}

	public static String getDownloadPath(String func, int kid) throws Exception {
		String fPath;
		java.util.Date date = new java.util.Date();
		Timestamp ts = new Timestamp(date.getTime());
		String currentDay = CateringServiceUtil.converTimestampToStr("yyyyMMddHHmmss", ts);
		String uploadPatch = CateringServiceUtil.getConfig("uploadPath");
		fPath = uploadPatch + "download/" + func + "_" + kid + "_" + currentDay + ".xlsx";
		return fPath;
	}

	/*
	 * 取得下載網址
	 */
	public static String getFilePath(String func, int kid, String fileKey) throws Exception {
		String filePath = "";
		String uploadPath = CateringServiceUtil.getConfig("uploadPath");

		switch (func) {
		case "report":
			filePath = uploadPath + "/download/Excel_" + kid + "_" + fileKey + ".xlsx";
			break;
		default:
			break;
		}

		// fPath = uploadPatch + "download/" + func + "_" + currentDay +
		// ".xlsx";
		return filePath;
	}

	/*
	 * 取得file key
	 */
	public static String getFileKey(String filepath) {
		String key = "";
		try {
			String str[] = filepath.split("_");
			key = str[str.length - 1];
			key = key.replaceFirst(".xlsx", "");

		} catch (Exception ex) {
			//
		}
		return key;
	}

	public static void checkIngredientDate(String _menuDate, String _stockDate, String _validDatre, String _productDate) throws Exception {

		Timestamp stockDate = HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", _stockDate);
		Timestamp expirationDate = HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", _validDatre);
		Timestamp manufactureDate = HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", _productDate);
		// String currentDateStr =
		// CateringServiceUtil.converTimestampToStr("yyyy/MM/dd",
		// CateringServiceUtil.getCurrentTimestamp()) ;
		// Timestamp baseDateTime =
		// CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd",
		// currentDateStr);
		Timestamp baseDateTime = HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", _menuDate);

		if (stockDate.getTime() > baseDateTime.getTime()) {
			throw new Exception("進貨日期不可大於菜單   菜單日期:" + _menuDate + "  進貨日期:" + _stockDate);
		}
		if (expirationDate != null) {
			if (baseDateTime.getTime() > expirationDate.getTime()) {
				throw new Exception("有效日期不可小於菜單日期    菜單日期:" + _menuDate + " 菜單 日期:" + expirationDate);
			}
		}
		if (manufactureDate != null) {
			if (baseDateTime.getTime() < manufactureDate.getTime()) {
				throw new Exception("生產日期不可大於菜單日期   菜單日期:" + _menuDate + "  生產日期:" + manufactureDate);
			}
			if (stockDate.getTime() < manufactureDate.getTime()) {
				throw new Exception("生產日期 不可大於進貨日期進貨:" + _stockDate + " 生產日期:" + _productDate);
			}
		}

		if (manufactureDate != null && expirationDate != null) {
			if (manufactureDate.getTime() > expirationDate.getTime()) {
				throw new Exception("生產日期不可大於有效日期  有效:" + _validDatre + " 生產日期:" + _productDate);
			}
		}
	}

	/**
	 * 取得檢驗報告
	 * 
	 * @param kitchenId
	 * @param ingredientBatchId
	 * @return
	 * @throws Exception
	 */
	public static String getInspectionFileName(Integer kitchenId, Integer ingredientBatchId) {
		String uploadPatch = StringUtils.EMPTY;

		try {
			uploadPatch = CateringServiceUtil.getConfig("uploadPath");
		} catch (Exception e) {
			log.error("get uploadPath fail!", e);
			return StringUtils.EMPTY;
		}

		String filePath = uploadPatch + "inspect/" + kitchenId + "/";
		// 如果無此目錄就新增一筆
		File f = new File(filePath);
		if (!f.exists()) {
			new File(filePath).mkdirs();
		}

		if (ingredientBatchId != null) {
			f = new File(filePath + ingredientBatchId + ".pdf");

			if (f.exists()) {
				return filePath + ingredientBatchId + ".pdf";
			} else {
				f = new File(filePath + ingredientBatchId + ".jpg");
				if (f.exists()) {
					return filePath + ingredientBatchId + ".jpg";
				}
			}

		}

		return StringUtils.EMPTY;

	}

	public static String getInspectionFileName(Integer kitchenId, Long ingredientBatchId, String ext) throws Exception {
		String uploadPatch = CateringServiceUtil.getConfig("uploadPath");
		String filePath = uploadPatch + "inspect/" + kitchenId + "/";
		// 如果無此目錄就新增一筆
		File f = new File(filePath);
		if (f.exists() && f.isDirectory()) {
			// new File(filePatch).mkdirs();
		} else {
			new File(filePath).mkdirs();
		}
		String filename = kitchenId + "_" + ingredientBatchId + "." + ext.toLowerCase();
		filename = filePath + "/" + filename;
		return filename;
	}

	public static String getInspectionFileNameV2(String uploadPath, String encodeFileName) throws Exception {

		// 如果無此目錄就新增一筆
		File f = new File(uploadPath);
		if (f.exists() && f.isDirectory()) {
			// new File(filePatch).mkdirs();
		} else {
			new File(uploadPath).mkdirs();
		}

		String fileName = uploadPath + "/" + encodeFileName;

		return fileName;
	}

	public static String getDishImageFileName(Integer kitchenId, Long dishId, String ext) throws Exception {
		String uploadPatch = CateringServiceUtil.getConfig("uploadPath");
		String filePath = uploadPatch + "dish/" + kitchenId + "/";
		// 如果無此目錄就新增一筆
		File f = new File(filePath);
		if (f.exists() && f.isDirectory()) {
			// new File(filePatch).mkdirs();
		} else {
			new File(filePath).mkdirs();
		}
		String filename = kitchenId + "_" + dishId + "." + ext.toLowerCase();
		filename = filePath + "/" + filename;
		return filename;
	}

	/**
	 * 取得首頁菜色示意圖路徑 Joshua add at 2014/10/15
	 * 
	 * @param sourcePath
	 * @param high
	 * @param width
	 * @return resizeFilePath
	 */
	public static String getResizeDishImageFilePath(String sourcePath, Integer high, Integer width) {
		String src[] = sourcePath.split("\\.");
		String resizeFilePath = src[0] + "_" + high + "_" + width + src[1];
		return resizeFilePath;
	}

	/**
	 * 判斷檔案目錄是否存在(不存在即增加) Joshua add at 2014/10/15
	 * 
	 * @param sourcePath
	 */
	public static void searchFileFolder(String filePath) {
		File f = new File(filePath);
		if (!(f.exists() && f.isDirectory())) {
			new File(filePath).mkdirs();
		}
	}

	public static String getDishImageFileName(Integer kitchenId, Long dishId, Integer high, Integer width, String ext) throws Exception {
		String uploadPatch = CateringServiceUtil.getConfig("uploadPath");
		String filePath = uploadPatch + "dish/" + kitchenId + "/";
		// 如果無此目錄就新增一筆
		File f = new File(filePath);
		if (f.exists() && f.isDirectory()) {
			// new File(filePatch).mkdirs();
		} else {
			new File(filePath).mkdirs();
		}
		String filename = kitchenId + "_" + dishId + "_" + high + "_" + width + "." + ext.toLowerCase();
		filename = filePath + "/" + filename;
		return filename;
	}

	public boolean isDishImageFileNameExists(Integer kitchenId, Long dishId) throws Exception {
		// SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
		// Session session = sessionFactory.openSession();
		UploadFileDAO ufDao = new UploadFileDAO(dbSession);
		Uploadfile uf = ufDao.getSingleUploadfile(SourceTypeCode.DISH_DATA_MAINTENANCE, String.valueOf(dishId));
		String filePath = "";

		if (uf != null) {
			filePath = uf.getFilePath() + uf.getEncodeFileName();
			return CateringServiceUtil.fileExists(filePath);
		} else {
			/** Joshua 2014/10/16 圖片新舊方法判斷 */
			String uploadPatch = CateringServiceUtil.getConfig("uploadPath");
			String filePathOldMethod = uploadPatch + "dish/" + kitchenId + "/";
			// 如果無此目錄就新增一筆
			File f = new File(filePathOldMethod);
			if (f.exists() && f.isDirectory()) {
				// new File(filePatch).mkdirs();
			} else {
				return false;
				// new File(filePath).mkdirs();
			}
			String filename = kitchenId + "_" + dishId;// +
														// "."+ext.toLowerCase();
			filename = filePathOldMethod + "/" + filename;
			if (CateringServiceUtil.fileExists(filename + ".jpg")) {
				return true;
			} else if (CateringServiceUtil.fileExists(filename + ".png")) {
				return true;
			} else if (CateringServiceUtil.fileExists(filename + ".gif")) {
				return true;
			}

			return false;
		}

		// Joshua edit 2014/10/14
		// String uploadPatch = CateringServiceUtil.getConfig("uploadPath");
		// String filePath = uploadPatch + "dish/" + kitchenId + "/";
		// // 如果無此目錄就新增一筆
		// File f = new File(filePath);
		// if (f.exists() && f.isDirectory()) {
		// // new File(filePatch).mkdirs();
		// } else {
		// return false;
		// // new File(filePath).mkdirs();
		// }
		// String filename = kitchenId + "_" + dishId;// +
		// "."+ext.toLowerCase();
		// filename = filePath + "/" + filename;
		// if (CateringServiceUtil.fileExists(filename + ".jpg")) {
		// return true;
		// } else if (CateringServiceUtil.fileExists(filename + ".png")) {
		// return true;
		// } else if (CateringServiceUtil.fileExists(filename + ".gif")) {
		// return true;
		// }
		//
		// return false;
	}

	// currentDay format yyyyMMdd
	public static String getSchoolMenuExcelFileName(Integer kitchenId, String currentDay, String ext) throws Exception {
		String uploadPatch = CateringServiceUtil.getConfig("uploadPath");
		String filePath = uploadPatch + "menu/" + kitchenId + "/";

		// 如果無此目錄就新增一筆
		File f = new File(filePath);
		if (f.exists() && f.isDirectory()) {
			// new File(filePatch).mkdirs();
		} else {
			new File(filePath).mkdirs();
		}
		// String filename = kitchenId + "_" + dishId + "."+ext.toLowerCase();
		String filename = kitchenId + "_" + currentDay + "." + ext.toLowerCase();
		filename = filePath + "/" + filename;
		return filename;
	}

	// currentDay format yyyyMMdd
	public static String getSchoolSupplierExcelFileName(Integer kitchenId, String currentDay, String ext) throws Exception {
		String uploadPatch = CateringServiceUtil.getConfig("uploadPath");
		String filePath = uploadPatch + "menu/" + kitchenId + "/";

		// 如果無此目錄就新增一筆
		File f = new File(filePath);
		if (f.exists() && f.isDirectory()) {
			// new File(filePatch).mkdirs();
		} else {
			new File(filePath).mkdirs();
		}
		String filename = kitchenId + "_" + currentDay + "." + ext.toLowerCase();
		filename = filePath + "/" + filename;
		return filename;
	}

	public static String getSchoolVegetableExcelFileName(Integer kitchenId, String currentDay, String ext) throws Exception {
		String uploadPatch = CateringServiceUtil.getConfig("uploadPath");
		String filePath = uploadPatch + "ingredient/" + kitchenId + "/";
		// 如果無此目錄就新增一筆
		File f = new File(filePath);
		if (f.exists() && f.isDirectory()) {
			// new File(filePatch).mkdirs();
		} else {
			new File(filePath).mkdirs();
		}
		String filename = kitchenId + "_" + currentDay + "." + ext.toLowerCase();
		filename = filePath + "/" + filename;
		return filename;
	}

	public static String getSchoolIngredientExcelFileName(Integer kitchenId, String currentDay, String ext) throws Exception {
		String uploadPatch = CateringServiceUtil.getConfig("uploadPath");
		String filePath = uploadPatch + "ingredient/" + kitchenId + "/";
		// 如果無此目錄就新增一筆
		File f = new File(filePath);
		if (f.exists() && f.isDirectory()) {
			// new File(filePatch).mkdirs();
		} else {
			new File(filePath).mkdirs();
		}
		String filename = kitchenId + "_" + currentDay + "." + ext.toLowerCase();
		filename = filePath + "/" + filename;
		return filename;
	}
	
	public static String getNewsFileName(Integer newsId, String currentDay, String ext) throws Exception {
		String uploadPatch = CateringServiceUtil.getConfig("uploadPath");
		String filePath = uploadPatch + "news/" + newsId + "/";
		// 如果無此目錄就新增一筆
		File f = new File(filePath);
		if (f.exists() && f.isDirectory()) {
			// new File(filePatch).mkdirs();
		} else {
			new File(filePath).mkdirs();
		}
//		String filename = newsId + "_" + currentDay + "." + ext.toLowerCase();
		String filename = currentDay + "." + ext.toLowerCase();
		filename = filePath + "/" + filename;
		return filename;
	}

	public static String getKitchenImageFileName(Integer kitchenId, String ext) throws Exception {
		String uploadPatch = CateringServiceUtil.getConfig("uploadPath");
		String filePath = uploadPatch + "kitchen/" + kitchenId + "/";

		// 如果無此目錄就新增一筆
		File f = new File(filePath);
		if (f.exists() && f.isDirectory()) {
			// new File(filePatch).mkdirs();
		} else {
			new File(filePath).mkdirs();
		}
		String filename = kitchenId + "." + ext.toLowerCase();
		filename = filePath + "/" + filename;
		return filename;
	}

	/*
	 * 轉換excel數字 num 數字 fmt 格式 ex.#.#
	 */
	public static String excelNumber(String num, String fmt) {
		if (num.indexOf(".") > 0) {
			double value = Double.valueOf(num);
			DecimalFormat df = new DecimalFormat(fmt);
			return df.format(value);
		}
		return String.valueOf(num);
	}

	/*
	 * 圖檔轉檔
	 * 因應轉圖擋導致色差，新增buffer回收機制 add by ellis 20141217
	 */

	public static boolean resizeImageWithHint(String sourceFile, String targetFile, int IMG_HEIGHT, int IMG_WIDTH) {
		BufferedImage originalImage = null;
		BufferedImage resizedImage = null;
		try {
			originalImage = ImageIO.read(new File(sourceFile));
			int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

			resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
			Graphics2D g = resizedImage.createGraphics();
			g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
			g.dispose();
			g.setComposite(AlphaComposite.Src);
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			ImageIO.write(resizedImage, CateringServiceCode.DEFAULT_IMAGE_RESIZE_EXTENSION, new File(targetFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.debug("resizeImageWithHint source file:" + sourceFile + " target file:" + targetFile);
			e.printStackTrace();
			return false;
		}finally{
			originalImage = null;
			resizedImage = null;
			System.gc();
		}
		return true;
	}

	// 檢查是否超過查詢時間範圍
	public static boolean isOverQueryRange(String startDate, String endDate) {
		// 先不檢查日期格式，預設為yyyy/MM/dd
		try {
			String limitDays = CateringServiceUtil.getConfig("query_days_limit");
			Timestamp startDateTS = CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", startDate);
			Timestamp endDateTS = CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", endDate);
			if (((endDateTS.getTime() - startDateTS.getTime()) / 60000 / 60 / 24) > Integer.valueOf(limitDays)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			// return true;
		}
		return true;
	}

	public static List<String> listFiles(String directoryName) {
		List<String> listfile = new ArrayList<String>();
		File directory = new File(directoryName);

		// get all the files from a directory
		File[] fList = directory.listFiles();

		for (File file : fList) {
			if (file.isFile()) {
				System.out.println(file.getName());
				listfile.add(file.getName());
			}
		}
		return listfile;
	}

	// 回傳client端 ip 20140226 KC
	public static String getClientAddr(HttpServletRequest request) {
		// 先直接取ip ,proxy處理之後再做
		String ip = request.getRemoteAddr();
		if (!ip.matches("[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}")) {
			ip = CateringServiceUtil.ipv6Toipv4(ip);
		}

		return ip;
		/*
		 * String client_ip=""; try{ client_ip =
		 * request.getHeader("X-Forwarded-For"); if (client_ip.isEmpty() ||
		 * request.getHeader("X-Forwarded-For")==null){
		 * client_ip=request.getRemoteAddr(); } }catch (Exception ex){
		 * System.out.println(ex.getMessage()); }
		 * 
		 * return client_ip;
		 */
	}

	// 處理web log format 20140227 KC
	public static void weblogToFile(String funcName, String userName, HttpServletRequest request, String Msg) {

		List<String> log = new ArrayList<String>();
		if (request != null) {
			log.add(CateringServiceUtil.getCurrentTimestamp().toString());
			log.add(request.getSession().getId());
			// log.add(CateringServiceUtil.ipv6Toipv4(CateringServiceUtil.getClientAddr(request)));
			log.add(CateringServiceUtil.getClientAddr(request));
			log.add(userName);
			log.add(funcName);
			log.add(request.getHeader("User-Agent"));
			log.add(Msg);
		} else {
			log.add(CateringServiceUtil.getCurrentTimestamp().toString());
			log.add(funcName);
			log.add(Msg);
		}
		apiLog.info("[api] " + log.toString());
	}

	// ipv6轉ipv4 20140227 KC
	public static String ipv6Toipv4(String ipv6) {
		// 用ipConvert轉出Byte[16] 只取最後四個byte轉成ipv4
		String ipString = "";
		try {
			byte[] ipbytes = ipConvert.toByte(ipv6);
			ipString = (ipbytes[12] & 0xFF) + "." + (ipbytes[13] & 0xFF) + "." + (ipbytes[14] & 0xFF) + "." + (ipbytes[15] & 0xFF);
		} catch (Exception ex) {
			ipString = ipv6;
		}

		return ipString;
	}

	/*
	 * 測試是否為數字型字串
	 */
	public static boolean isNumeric(String str) {
		if (str == null) {
			return false;
		}
		try {
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	/*
	 * 用學校名稱取schoolType
	 */
	public static Integer getSchoolTypeBySchoolName(String schoolName) {
		if (isEmpty(schoolName))
			return 0;

		int schType = 0;
		if(schoolName.contains("幼兒園") || schoolName.contains("附幼")) //add by ellis 20150204 
			schType += 1;
		if (schoolName.contains("國小") || schoolName.contains("小學"))
			schType += 2;
		if (schoolName.contains("國中") || schoolName.contains("國民中小學") || schoolName.contains("國民中學"))
			schType += 4;
		if (schoolName.contains("高級") || schoolName.contains("高中") || schoolName.contains("女中"))
			schType += 8;
		if (schoolName.contains("大學"))
			schType += 16;
		if (schoolName.contains("國立"))
			schType += 32;
		if (schoolName.contains("私立"))
			schType += 64;
		if (!schoolName.contains("國立") || !schoolName.contains("私立"))
			schType += 128;
		if (schoolName.contains("完全"))
			schType += 6; 

		return schType;
	}

	public static IngredientAttributeBO getIngredientAttrBo(int ingredientAttr) {
		IngredientAttributeBO bo = new IngredientAttributeBO();
		String attr = Integer.toBinaryString(ingredientAttr);

		attr = String.format("%08d", Integer.parseInt(attr));

		// 基改黃豆
		if (String.valueOf(attr.charAt(attr.length() - 1)).equals("1"))
			bo.setGmbean(1);
		else
			bo.setGmbean(0);
		// 基改玉米
		if (String.valueOf(attr.charAt(attr.length() - 2)).equals("1"))
			bo.setGmcorn(1);
		else
			bo.setGmcorn(0);
		// 加工食品
		if (String.valueOf(attr.charAt(attr.length() - 3)).equals("1"))
			bo.setPsfood(1);
		else
			bo.setPsfood(0);

		return bo;
	}

	/*
	 * 20141013 Joshua 確認業者/廚房照片是否存在
	 */
	public boolean checkKitchenPicExist(int kitchenId) throws Exception {

		// SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
		// Session session = sessionFactory.openSession();
		UploadFileDAO ufDao = new UploadFileDAO(dbSession);
		Uploadfile uf = ufDao.getSingleUploadfile(SourceTypeCode.CATERING_COMPANY_LOGO, String.valueOf(kitchenId));
		String filePath = "";

		if (uf != null) {
			filePath = uf.getFilePath() + uf.getEncodeFileName();
			return CateringServiceUtil.fileExists(filePath);
		} else {

			/** Joshua 2014/10/16 圖片新舊方法並存判斷 */
			String uploadPatch = CateringServiceUtil.getConfig("uploadPath");
			String filePathOldMethod = uploadPatch + "kitchenLogo/" + kitchenId;
			String filename = filePathOldMethod + "/" + kitchenId;

			if (CateringServiceUtil.fileExists(filename + ".jpg")) {
				return true;
			} else if (CateringServiceUtil.fileExists(filename + ".png")) {
				return true;
			} else if (CateringServiceUtil.fileExists(filename + ".gif")) {
				return true;
			}
			return false;

		}
	}

	public static boolean deleteFile(String filePath) throws Exception {
		String uploadPatch = CateringServiceUtil.getConfig("uploadPath");
		String filePathName = uploadPatch + filePath;
		if (CateringServiceUtil.fileExists(filePathName + ".jpg")) {
			filePathName = filePathName + ".jpg";
			if (CateringServiceUtil.doDelete(filePathName)) {
				return true;
			}
			;
		} else if (CateringServiceUtil.fileExists(filePathName + ".png")) {
			filePathName = filePathName + ".png";
			if (CateringServiceUtil.doDelete(filePathName)) {
				return true;
			}
			;
		} else if (CateringServiceUtil.fileExists(filePathName + ".gif")) {
			filePathName = filePathName + ".gif";
			if (CateringServiceUtil.doDelete(filePathName)) {
				return true;
			}
			;
		}
		return false;
	}

	public static boolean doDelete(String fileName) throws Exception {
		if (new File(fileName).delete()) {
			return true;
		} else {
			return false;
		}
	}

	// add by Joshua 2014/10/13
	public static boolean deleteAndBackupFile(String type, String targetId) throws Exception {
		SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
		Session session = sessionFactory.openSession();
		UploadFileDAO ufDao = new UploadFileDAO(session);
		Uploadfile uf = ufDao.getSingleUploadfile(type, targetId);
		String filePath = "";
		String newFilePath = "";
		try {
			if (uf != null) {
				filePath = uf.getFilePath() + uf.getEncodeFileName();
			}

			java.util.Date date = new java.util.Date();
			Timestamp ts = new Timestamp(date.getTime());

			if (CateringServiceUtil.fileExists(filePath)) {
				String filePathStr[] = filePath.split("\\.");
				newFilePath = filePathStr[0] + "_" + CateringServiceUtil.converTimestampToStr("yyyyMMddHHmmss", ts) + "_backup" + "." + filePathStr[1];

				if (CateringServiceUtil.doFileBackupByFilePathAndNewFilePath(filePath, newFilePath)) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return true;
	}

	// edit by Joshua 2014/10/13
	// public static boolean deleteAndBackupFile_old(String filePath) throws
	// Exception {
	// String uploadPatch = CateringServiceUtil.getConfig("uploadPath");
	// String filePathName = uploadPatch + filePath;
	// String newPathName = uploadPatch + filePath;
	// java.util.Date date = new java.util.Date();
	// Timestamp ts = new Timestamp(date.getTime());
	// if (CateringServiceUtil.fileExists(filePathName + ".jpg")) {
	// newPathName = filePathName + "_" +
	// CateringServiceUtil.converTimestampToStr("yyyyMMddHHmmss", ts) +
	// "_backup" + ".jpg";
	// filePathName = filePathName + ".jpg";
	//
	// if(CateringServiceUtil.doFileBackupByFilePathAndNewFilePath(filePathName,newPathName)){
	// return true;
	// }
	// ;
	// } else if (CateringServiceUtil.fileExists(filePathName + ".png")) {
	// newPathName = filePathName + "_" +
	// CateringServiceUtil.converTimestampToStr("yyyyMMddHHmmss", ts) +
	// "_backup" + ".png";
	// filePathName = filePathName + ".png";
	// if(CateringServiceUtil.doFileBackupByFilePathAndNewFilePath(filePathName,newPathName)){
	//
	// return true;
	// }
	// ;
	// } else if (CateringServiceUtil.fileExists(filePathName + ".gif")) {
	// newPathName = filePathName + "_" +
	// CateringServiceUtil.converTimestampToStr("yyyyMMddHHmmss", ts) +
	// "_backup" + ".gif";
	// filePathName = filePathName + ".gif";
	// if(CateringServiceUtil.doFileBackupByFilePathAndNewFilePath(filePathName,newPathName)){
	// return true;
	// }
	// ;
	// }
	// return false;
	// }

	public static boolean doFileBackupByFilePathAndNewFilePath(String fileName, String newfileName) throws Exception {
		if (new File(fileName).renameTo(new File(newfileName))) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * 是否超過限制日期(菜單與食材的上傳時間檢查)
	 */
	public static boolean isUploadTime(Timestamp menuDate, Timestamp nowTS, String limitHours) {
		System.out.println(menuDate.getTime());
		System.out.println("---");
		System.out.println(nowTS.getTime());
		System.out.println("---");
		System.out.println(limitHours);
		if (!CateringServiceUtil.isNumeric(limitHours)) {
			return true;
		}
		Integer seconds = Integer.valueOf(limitHours) * 60 * 60 * 1000;
		if (menuDate.getTime() + seconds >= nowTS.getTime()) {
			return true;
		} else {
			return false;
		}

	}

	public static int getIngredientAttrVal(IngredientAttributeBO attrBo) {
		int attr = 0;
		if (attrBo == null)
			return 0;

		if (attrBo.getGmbean() == 1)
			attr += CateringServiceCode.INGREDIENT_ATTR_GMBEAM;

		if (attrBo.getGmcorn() == 1)
			attr += CateringServiceCode.INGREDIENT_ATTR_GMCORN;

		if (attrBo.getPsfood() == 1)
			attr += CateringServiceCode.INGREDIENT_ATTR_PSFOOD;

		return attr;
	}

	public static boolean isMeat(String ingredientName) {
		boolean r = false;
		// if(ingredientName.contains("肉") || ingredientName.contains("豬") ||
		// ingredientName.contains("雞") || ingredientName.contains("牛"))
		// r = true;

		return r;
	}
	
	/**
	 * 依日期取星期幾 Joshua add at 2014/11/10
	 * @param dateStr 日期字串
	 * @return String
	 */	
	public static String getWeekDay(String dateStr){
		SimpleDateFormat formatYMD = new SimpleDateFormat("yyyy/MM/dd");
	    SimpleDateFormat formatWeekDay = new SimpleDateFormat("E"); //"E"表示"day in week"
	    Date date = null;
	    String weekDay = "";
	    try{
	       date = formatYMD.parse(dateStr);
	       weekDay = formatWeekDay.format(date);
	    }catch (Exception e){
	       e.printStackTrace();
	    }
	    return weekDay;
	}
	
	/**
	 * 特殊字元判斷 Joshua add at 2014/11/11
	 * @param str
	 * @return
	 */
	public static boolean isVaildStr(String str){
		String reg = "[\"|>|<|'|%|;|&|+|\n\r?|\r\n?]";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(str);
		return m.find();
	}
	
	
	/**
	 * 特殊字元判斷
	 * @param str
	 * @return
	 * By Steven add at 2015/01/16
	 */
	public static boolean isVaildStr02(String str){
		String reg = "~!@#$%^&*()_+`{}\\|[];':\",./<>?";
		boolean rtnBoolean = false;
		for(char c: reg.toCharArray()) {
			//System.out.println(str.indexOf(c));
			// 大於0表示有找到這個字元則回傳false
			if( str.indexOf(c)>0 ) {
				rtnBoolean = false;
				break;
			} else {
				rtnBoolean = true;
			}
		}
		return rtnBoolean;
	}

	/**
	 * 判斷日期格式，日期格式為四位年/兩位月份/兩位日期，如:yyyy/MM/dd。
	 * @param str 日期格式之字串
	 * @return true: 表示是日期格式, false:表示為非日期格式。
	 * By Steven add at 2015/01/16
	 */
	public static boolean isValidDate(String str) {
		boolean convertSuccess=true;
		// 指定日期格式為四位年/兩位月份/兩位日期，如:yyyy/MM/dd。
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		try {
			// 設置lenient為false. 否則SimpleDateFormat會比較寬鬆的驗證日期，如2007/02/29會被接受，並轉換成2007/03/01。
			format.setLenient(false);
			format.parse(str);
		} catch (ParseException e) {
			// 如果throw java.text.ParseException或者NullPointerException，就說明格式不對。則回傳false。
			convertSuccess=false;
		}
		return convertSuccess;
	}
	
	/**
	 * 『預劃菜單之DishType』轉『正式菜單之DishType』
	 * 目前寫法只因應16欄位，邏輯待改。
	 * @param codeName 菜色類別『主食、主菜、副菜、蔬菜、附餐、湯品』等字樣。由PlanningDishBatchData中之DishType查出該菜色類別。
	 * @param dishOrder 菜色類別出現的次數。PlanningDishBatchData中之DishOrder欄位
	 * @return 轉換後正式菜單(DishBatchData table)中之DishType欄位值
	 */
	public static String planningDishBatchData_DishType2DishBatchData_DishType(String codeName, Integer dishOrder) {
		System.out.println("[CateringServiceUtil.java] codeName:"+codeName+", dishOrder:"+dishOrder);
		// 設定『預劃菜單之DishType』轉『正式菜單之DishType』的對應表
		HashMap<String, String[]> dishTypeMap = new HashMap<String, String[]>();
		dishTypeMap.put(CateringServiceCode.DISHTYPE_MAINFOOD, new String[] { "MainFoodId", "MainFood1Id" });	// 主食
		dishTypeMap.put(CateringServiceCode.DISHTYPE_MAINDISH, new String[] { "MainDishId", "MainDish1Id", 
				"MainDish2Id", "MainDish3Id" });																// 主菜
		dishTypeMap.put("DISHTYPE_SUBDISH", new String[] { "SubDish1Id", "SubDish2Id", "SubDish3Id", 
				"SubDish4Id", "SubDish5Id", "SubDish6Id" });													// 副菜
		dishTypeMap.put(CateringServiceCode.DISHTYPE_VEGETABLE, new String[] { "VegetableId" });				// 蔬菜
		dishTypeMap.put(CateringServiceCode.DISHTYPE_SEASONING, new String[] { "Seasoning" });					// 調味料
		dishTypeMap.put(CateringServiceCode.DISHTYPE_SOUP, new String[] { "SoupId" });							// 湯品
		dishTypeMap.put(CateringServiceCode.DISHTYPE_DESSERT, new String[] { "DessertId", "Dessert1Id" });		// 附餐(甜點)
		
		String[] officialMenuDishType = null;
		int strArySize = 0;
		switch(codeName) {
		case "主食":
			officialMenuDishType = dishTypeMap.get(CateringServiceCode.DISHTYPE_MAINFOOD);
			break;
		case "主菜":
			officialMenuDishType = dishTypeMap.get(CateringServiceCode.DISHTYPE_MAINDISH);
			break;
		case "副菜":
			officialMenuDishType = dishTypeMap.get("DISHTYPE_SUBDISH");
			break;
		case "蔬菜":
			officialMenuDishType = dishTypeMap.get(CateringServiceCode.DISHTYPE_VEGETABLE);
			break;
		case "附餐":
			officialMenuDishType = dishTypeMap.get(CateringServiceCode.DISHTYPE_DESSERT);
			break;
		case "湯品":
			officialMenuDishType = dishTypeMap.get(CateringServiceCode.DISHTYPE_SOUP);
			break;
		case "調味料":
			officialMenuDishType = dishTypeMap.get(CateringServiceCode.DISHTYPE_SEASONING);
			break;
		}
		
		//System.out.println("length:"+officialMenuDishType.length);
		//for(String fieldName: officialMenuDishType) {
		//	System.out.println("fieldName:"+fieldName);
		//}
		//System.out.println("--------------------------");
		
		//int nDishOrder = dishOrder < officialMenuDishType.length? dishOrder : 0;
		return officialMenuDishType[dishOrder];
	}

	// TODO
	/**
	 * 由[dishbatchdata] table的『DishType』欄位+『DishOrder』欄位，查出其原(batchdata table)對應之欄位。
	 * 規則:
	 * DishType --> 1:主食、2:主菜、3:副菜、4:蔬菜、5:附餐、6:湯品。  DishOrder --> 0~14
	 * DishOrder --> 0~1:主食  2~5:主菜  6~11:副菜
	 * @param batchDataHshMap : 為HashMap集合。key為String用以比對要處理的欄位，value為ArrayList有幾筆就有幾個這類欄位的資料。
	 * @return HashMap<String, DishBatchData>
	 */
	public static HashMap<String, DishBatchData> decDishType2Field(HashMap<String, ArrayList> batchDataHshMap) {
		HashMap rtnHashMap = new HashMap();
		for(String key: batchDataHshMap.keySet()) {
			ArrayList aryList = batchDataHshMap.get(key);

			switch(key) {
			case "MainFood":
				for(int i=0; i<aryList.size(); i++) {
					DishBatchData dishBatchDataObj = (DishBatchData) aryList.get(i);
					if(i==0) {
						rtnHashMap.put("MainFoodId", dishBatchDataObj);
					} else if(i==1) {
						rtnHashMap.put("MainFood1Id", dishBatchDataObj);
					}
				}
				break;
				
			case "MainDish":
				for(int i=0; i<aryList.size(); i++) {
					DishBatchData dishBatchDataObj = (DishBatchData) aryList.get(i);
					if(i==0) {
						rtnHashMap.put("MainDishId", dishBatchDataObj);
					} else if(i>=1) {
						rtnHashMap.put("MainDish"+i+"Id", dishBatchDataObj);
					}
				}
				break;
				
			case "SubDish":
				for(int i=0; i<aryList.size(); i++) {
					DishBatchData dishBatchDataObj = (DishBatchData) aryList.get(i);
					rtnHashMap.put("SubDish"+(i+1)+"Id", dishBatchDataObj);
				}
				break;
				
			case "Vegetable":
				for(int i=0; i<aryList.size(); i++) {
					DishBatchData dishBatchDataObj = (DishBatchData) aryList.get(i);
					rtnHashMap.put("VegetableId", dishBatchDataObj);
				}
				break;
				
			case "Soup":
				for(int i=0; i<aryList.size(); i++) {
					DishBatchData dishBatchDataObj = (DishBatchData) aryList.get(i);
					rtnHashMap.put("SoupId", dishBatchDataObj);
				}
				break;
				
			case "Dessert":
				for(int i=0; i<aryList.size(); i++) {
					DishBatchData dishBatchDataObj = (DishBatchData) aryList.get(i);
					if(i==0) {
						rtnHashMap.put("DessertId", dishBatchDataObj);
					} else if(i==1) {
						rtnHashMap.put("Dessert1Id", dishBatchDataObj);
					}
				}
				break;
				
			}
		}
		
		return rtnHashMap;
	}
	
	
	// 2015.05.01 Steven
	/**
	 * 依帳號取得是『國中小還是幼兒園』之代碼編號
	 * @param userName : 帳號
	 * @return String : 代表該員是國小還是國中還是幼兒園
	 */
	public static String getSchoolLevel(String userName) {
		String uSchoolCode = "";
		
		if(userName.length()>7 && userName.indexOf("-")>0) {
			userName = userName.split("-")[0];
		}
		
		if(userName.length()>=7) {
			switch(userName.substring(4, 5)) {
				// 高中職
				case "3":
				case "4":
				case "B":
				case "b":
				case "C":
				case "c":
					uSchoolCode = "SENIOR_HIGH_SCHOOL";
					break;
					
				// 國中
				case "5":
				case "D":
				case "d":
					uSchoolCode = "JUNIOR_HIGH_SCHOOL";
					break;
				
				// 國小
				case "6":
				case "7":
				case "8":
				case "E":
				case "e":
					uSchoolCode = "ELEMENTARY_SCHOOL";
					break;
					
				// 幼兒園
				case "K":
				case "W":
				case "X":
				case "Y":
				case "Z":
				case "k":
				case "w":
				case "x":
				case "y":
				case "z":
					uSchoolCode = "PRESCHOOL";
					break;
			}
		}
		
		System.out.println(uSchoolCode);
		return uSchoolCode;
	}
	
	/**
	 * 依帳號取得KitchenId,再依KitchenId與usertype=009查詢該校是否有員生消費合作社
	 * @param userName : 帳號
	 * @return  : 布林值true表該校有員生消費合作社 false則否
	 */
	public static boolean chkSchoolproduct(String userName) {
		try {
			SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
			Session session = sessionFactory.openSession();
			UseraccountDAO dao = new UseraccountDAO(session);
			Useraccount tmpAccount = dao.findById(userName);
			if (tmpAccount == null) {
				return false;
			}
			Useraccount account = new Useraccount();
			account.setKitchenId(tmpAccount.getKitchenId());
			account.setUsertype("009"); // usertype=009 為員生消費合作社
			List accountList = dao.findByExample(account);
			if (accountList != null && accountList.size() > 0) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
}