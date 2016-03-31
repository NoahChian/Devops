package org.iii.ideas.catering_service.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


public class Common 
{	
	/***
	 * 
	 * @return
	 * 取得現在時間
	 */
	public Timestamp getNowTime()
	{	
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Timestamp nowTime = new Timestamp(System.currentTimeMillis());
		return nowTime;
	}
	
	/***
	 * 
	 * @return
	 * 取得現在時間的前一天
	 */
	public Timestamp getNowTimeYesterday()
	{	
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Timestamp nowTime = new Timestamp(System.currentTimeMillis()-24*60*60*1000);
		return nowTime;
	}
	
	/***
	 * 
	 * @return
	 * @throws UnknownHostException
	 * 取得IP
	 */
	public String getRemoteIp() throws UnknownHostException 
	{
		InetAddress addr = InetAddress.getLocalHost();
		String loginIp = addr.getHostAddress().toString();
		return loginIp;	
	}
	
	/**
	 * 這個md5 的算法有問題，如果算出來的md5字串，開頭為0的話，會自動省略
	 * @param passWd
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 * MD5編碼
	 */	
	
	/*
	public String getEncoderByMd5(String passWd) throws NoSuchAlgorithmException, UnsupportedEncodingException 
	{
		MessageDigest md = MessageDigest.getInstance("MD5");
	    md.update(passWd.getBytes());
	    BigInteger hash = new BigInteger(1, md.digest());
	    return hash.toString(16);		    
	}
	*/
	
	/*
	 *新的 md5 演算法  
	 * 
	 */
	public String getEncoderByMd5(String str) {
		String encType="md5";
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance(encType);
			md.update(str.getBytes());
			result = toHexString(md.digest());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
 
	private String toHexString(byte[] in) {
		StringBuilder hexString = new StringBuilder();
		for (int i = 0; i < in.length; i++){
			String hex = Integer.toHexString(0xFF & in[i]);
			if (hex.length() == 1){
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}
	
	/**
	 * 
	 * @return
	 * 取得現在時間的去年值
	 */
	public Timestamp getLastYearDate() 
	{
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, -1);
		String lastYear = sdf.format(calendar.getTime());  
		Timestamp ts = Timestamp.valueOf(lastYear);
		return ts;					
	}
	
	/***
	 * 取得昨天日期字串
	 * @return
	 */
	public String getYesterdayStr(){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		String lastYear = sdf.format(calendar.getTime());  
		return lastYear;
	}
	
	
	/**
	 * 
	 * @return
	 * 取得現在時間的去年值
	 */
	public String geteStirngLastYearDat() 
	{
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		String lastYear = sdf.format(calendar.getTime());	
		return lastYear;					
	}
	
	/**
	 * 
	 * @param out
	 * @param hrefLink
	 * 輸出訊息
	 */
	public void setWritePrinter(PrintWriter out,String hrefLink)
	{
		out.print(hrefLink);
		out.flush();
		out.close();
	}
	
	/**
	 * 
	 * @param out
	 * @param hrefLink
	 * 輸出訊息
	 */
	public void setWritePrinterByInt(PrintWriter out,int hrefLink)
	{
		out.print(hrefLink);
		out.flush();
		out.close();
	}
	/***
	 * 取得今天日期字串
	 * @return
	 */
	public String getCurrentDateStr(){
		//目前時間
		Date date = new Date();
		//設定日期格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
		//進行轉換
		
		return sdf.format(date);
	}
	
	public Timestamp getStrDate(String strDate){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");		
		Timestamp ts = Timestamp.valueOf(strDate);
		return ts;
	}
	
	/**
	 * 
	 * @param imagesName
	 * @return
	 * 取得認證圖片
	 */
	public String getImages(String imagesName)
	{
		HashMap<String, Boolean> imagesMap = new HashMap<String, Boolean>();
		HashMap<String, String> imagesNameMap = new HashMap<String, String>();
		HashMap<String, String> imagesTitleMap = new HashMap<String, String>();
		
		imagesMap.put("GMP", true);
		imagesMap.put("CAS", true);
		imagesMap.put("TAP", true);
		imagesMap.put("HEALTH", true);
		imagesMap.put("MILK", true);
		imagesMap.put("CASMOA", true);
		imagesMap.put("TRACE", true);
		imagesMap.put("ISO9001", true);
		imagesMap.put("ISO22000", true);
		imagesMap.put("ISO22005", true);
		imagesMap.put("Halal", true);
		
		//final String imageFolder = "images/auth/";
		imagesNameMap.put("GMP", "GMP1.jpg");
		imagesNameMap.put("CAS", "CAS1.jpg");
		imagesNameMap.put("TAP", "TAP1.jpg");
		imagesNameMap.put("HEALTH", "HEALTH1.jpg");
		imagesNameMap.put("MILK", "MILK1.jpg");
		imagesNameMap.put("CASMOA", "CASMOA1.jpg");
		imagesNameMap.put("TRACE", "TRACERESUME.png");
		
		imagesTitleMap.put("GMP", "食品GMP");
		imagesTitleMap.put("CAS", "CAS台灣優良農產品");
		imagesTitleMap.put("TAP", "TAP產銷履歷農產品");
		imagesTitleMap.put("HEALTH", "健康食品");
		imagesTitleMap.put("MILK", "鮮乳標章");
		imagesTitleMap.put("CASMOA", "CASMOA有機認證");
		imagesTitleMap.put("TRACE", "安心食品履歷追溯");

		String imagesPath = "";
		if(imagesMap.containsKey(imagesName) && !imagesName.equals("ISO9001") && !imagesName.equals("ISO22000")&& !imagesName.equals("Halal"))
		{
			imagesPath += "<img class=\"imgContent\" border=\"0\"; src=";
			imagesPath += ("\"images/auth/" + imagesNameMap.get(imagesName)+ "\"" + " title=");
			imagesPath += ("\"" + imagesTitleMap.get(imagesName)+ "\">");
		}
				
		return imagesPath;		
	}
	
	/**
	 * 
	 * @param standard
	 * @return
	 * 重新排序認證順序
	 */
	public ArrayList getSortImage(String[] standard)
	{
		ArrayList standardList = new ArrayList();
		ArrayList standardSort = new ArrayList();
		
		standardList.add("TRACE");
		standardList.add("GMP");
		standardList.add("CAS");
		standardList.add("TAP");
		standardList.add("HEALTH");
		standardList.add("MILK");
		standardList.add("CASMOA");	
		standardList.add("HACCP");
		standardList.add("ISO9001");
		standardList.add("ISO22000");
		standardList.add("ISO22005");
		standardList.add("Halal");
		
		for(int i=0;i<standardList.size();i++)
		{
			for(int j=0;j<standard.length;j++)
			{
				if(standardList.get(i).equals(standard[j]))
				{
					standardSort.add(standard[j]);
				}
			}
		}		
		return standardSort;
	}
	
	/**
	 * 
	 * @param dividendString
	 * @param divisorString
	 * @return
	 * 計算營養成本百分比
	 */
	public String calculatePercent(String dividendString,String divisorString)
	{
		BigDecimal dividend = new BigDecimal((dividendString));
	    BigDecimal divisor = new BigDecimal(divisorString);
	    String number = Double.toString(dividend.divide(divisor,2,BigDecimal.ROUND_HALF_UP).doubleValue()*100);	 	 		 
        String[] numberArray = number.split("\\.");	 	 		      
        return (numberArray[0]+"%");	 	 
	}
	
	//aes & base64 加密
	public String getencrypt(String key,String str) 
	{
		String encrypted = "";
		try 
		{
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(key.getBytes()));
			SecretKey skey = kgen.generateKey();
			byte[] raw = skey.getEncoded();
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			byte[] encrypt = cipher.doFinal(str.getBytes("UTF-8"));
			encrypted = new BASE64Encoder().encodeBuffer(encrypt);
			// System.out.println("encrypted="+encrypted);
		}
		catch (Exception e) 
		{
			System.out.println(e);
		}
		return encrypted; 
	}
	
	//aes & base64 解密
	public static String getdecrypt(String key,String base64) throws Exception
	{
	    String decrypted="";
	    try
	    {
		    byte[] b = new BASE64Decoder().decodeBuffer(base64);
		    KeyGenerator kgen2 = KeyGenerator.getInstance("AES");
		    kgen2.init(128,new SecureRandom(key.getBytes()));
		    SecretKey skey2 = kgen2.generateKey();
		    byte[] raw2 = skey2.getEncoded();
		    SecretKeySpec skeySpec2 = new SecretKeySpec(raw2, "AES");
		    Cipher cipher2 = Cipher.getInstance("AES");
		    cipher2.init(Cipher.DECRYPT_MODE, skeySpec2);
		    byte[] decrypt = cipher2.doFinal(b);
		    decrypted=new String(decrypt);	 		    
	    }
	    catch(Exception e)
	    {
	    	System.out.println(e);
	    }
	    
	    return decrypted;
	}
	
	
	//星號取代
	public String changeStar(String str)
	{
		char[] tmpStrChar = str.toCharArray();
		StringBuilder strBuilder = new StringBuilder();
		int strLength = tmpStrChar.length;
		for(int i=0;i<strLength;i++)
		{
			if(i%5 == 0 || i%3 == 0)
				strBuilder.append('*');
			else
				strBuilder.append(tmpStrChar[i]);			
		}
		return strBuilder.toString();
	}
	
	
	/**
	 * 
	 * @param fileBytes
	 * @return
	 * 將檔案的byte array作MD5 encode
	 */
	public String getFileEncoderByMd5(byte[] fileBytes) {
		String encType="md5";
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance(encType);
			md.update(fileBytes);
			result = toHexString(md.digest());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * 
	 * @param sourcePath
	 * @param destPath
	 * 複製資料夾底下所有檔案
	 */
	public void copyFolders(String sourcePath, String destPath){
		try { 
			//log.info(" copy " + sourcePath + " to " + destPath);

            boolean process = true;

            File sourceFile = new File(sourcePath);
            File destFile = new File(destPath);
            
            if(!sourceFile.exists()){
            	//logger.warn(" 來源資料夾不存在");
            	process = false;
            }
            if(!destFile.exists() && (process == true) ){
            	//logger.warn(" 目的資料夾不存在, 嚐試建立");
            	try{
            		destFile.mkdirs();
            	}
            	catch(Exception e){
            		//logger.warn(" 目的資料夾建立失敗");
            		process = false;
            	}
            	if(!destFile.exists()){
            		process = false;
            	}
            }
            
            if(!sourceFile.isDirectory() && process){
            	//logger.warn(" 輸入來源路徑並非資料夾");
            	process = false;
            }
            if(!destFile.isDirectory() && process){
            	//logger.warn(" 輸入目的路徑並非資料夾");
            	process = false;
            }
            
            if(process == false){
            	
            }
            else{
            	File[] sourceFileList = sourceFile.listFiles();
            	for(File f : sourceFileList){
            		String sourceFileName = f.getName();
            		String destFilePath = destFile.getPath()+File.separator+sourceFileName;
            		if(f.isDirectory()){
            			copyFolders(f.getPath(), destFilePath);
            		}
            		else{
            			copyFile(f.getPath(), destFilePath);
            		}
            	}
            }
        } 
        catch(ArrayIndexOutOfBoundsException e) { 
            e.printStackTrace();
        } 
        catch(Exception e) { 
            e.printStackTrace(); 
        } 
	}
	
	
	/**
	 * 
	 * @param sourcePath
	 * @param destPath
	 * @return
	 * 複製檔案
	 */
	public boolean copyFile(String sourcePath, String destPath){
		//logger.info(" copy " + sourcePath + " to " + destPath);
		try{
			
			byte[] buffer = new byte[1024];
			boolean process = true;
			
			File sourceFile = new File(sourcePath);
            File destFile = new File(destPath);
            
			if(!sourceFile.exists()){
				//logger.warn("來源檔案不存在");
				process = false;
			}
			
			if(destFile.getParentFile() == null){
				//logger.warn("目標資料夾不正確");
				process = false;
			}
			else if(!destFile.getParentFile().exists()){
				//logger.warn("目標資料夾不存在, 嚐試建立");
				try{
					destFile.getParentFile().mkdirs();
            	}
            	catch(Exception e){
            		//logger.warn(" 目的資料夾建立失敗");
            		process = false;
            	}
            	if(!destFile.getParentFile().exists()){
            		process = false;
            	}
			}
			
			if(process == false){
				
			}
			else{
				FileInputStream fileInputStream = new FileInputStream(sourceFile); 
	            FileOutputStream fileOutputStream = new FileOutputStream(destFile); 

	            int length = -1;
	            // 從來源檔案讀取資料至緩衝區 
	            while((length = fileInputStream.read(buffer)) != -1) { 
	                // 將陣列資料寫入目的檔案 
	                fileOutputStream.write(buffer, 0, length);
	            } 
	            // 關閉串流 
	            fileInputStream.close(); 
	            fileOutputStream.close(); 

	            //logger.info("複製完成");
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	/**
	 * 
	 * @param path
	 * 刪除資料夾底下所有檔案
	 */
	public void deleteFolderFiles(String path){
			//log.info(" copy " + sourcePath + " to " + destPath);

            boolean process = true;

            File sourcePath = new File(path);
            
            if(!sourcePath.exists()){
            	//logger.warn(" 來源資料夾不存在");
            	process = false;
            }
           
            
            if(!sourcePath.isDirectory() && process){
            	//logger.warn(" 輸入來源路徑並非資料夾");
            	process = false;
            }
            
            if(process == false){
            	
            }
            else{
            	File[] sourceFileList = sourcePath.listFiles();
            	for(File f : sourceFileList){
            		f.delete();
            	}
            }
	} 
	
	
	public Timestamp getLastMonthStartDate() 
	{
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 000);
		
		Timestamp ts = new Timestamp(calendar.getTime().getTime());
		return ts;					
	}
	
	public Timestamp getLastMonthStartDateLatestTime() 
	{
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		
		Timestamp ts = new Timestamp(calendar.getTime().getTime());
		return ts;					
	}
	
	public Timestamp getLastMonthEndDate() 
	{
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -1);
		int lastMonthMaxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		//calendar.add(Calendar.DATE, -1);
		calendar.set(Calendar.DAY_OF_MONTH, lastMonthMaxDay);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		
		Timestamp ts = new Timestamp(calendar.getTime().getTime());
		return ts;					
	}
	
	public Timestamp getTheDateBeforeLastMonthEndDate() 
	{
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -1);
		int lastMonthMaxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		//calendar.add(Calendar.DATE, -1);
		calendar.set(Calendar.DAY_OF_MONTH, lastMonthMaxDay-1);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		
		Timestamp ts = new Timestamp(calendar.getTime().getTime());
		return ts;					
	}
	
	public int getLastMonthTotalDays()
	{
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -1);
		int lastMonthMaxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		return lastMonthMaxDay;
	}
	
	public int getDayOfMonthByMillisecond(long millisecond)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millisecond);
		return(cal.get(Calendar.DAY_OF_MONTH));
	}
	
    public HashMap<String, Double> unitsRatio(List<String> units)
    {
    	HashMap<String, Double> unitsMap = new HashMap<String, Double>();
		for(String sTmp : units)
		{
			switch(sTmp){
				case "byt":
					unitsMap.put(sTmp, 1.0);
					break;
				case "kb":
					unitsMap.put(sTmp, 1024.0);
					break;
				case "mb":
					unitsMap.put(sTmp, Math.pow(1024,2));
					break;
				case "gb":
					unitsMap.put(sTmp, Math.pow(1024,3));
					break;
				case "tb":
					unitsMap.put(sTmp, Math.pow(1024,4));
					break;
				case "mcs":
					unitsMap.put(sTmp, 1.0);
					break;
				case "mse":
					unitsMap.put(sTmp, Math.pow(10,3));
					break;
				case "sec":
					unitsMap.put(sTmp, Math.pow(10,6));
					break;
				case "hr":
					unitsMap.put(sTmp, Math.pow(10,6)*3600.0);
					break;
				case "unt":
					unitsMap.put(sTmp, 1.0);
					break;
			}
		}
		
		return unitsMap;
    }
    
    
	public double unitConvert(String original, String target, HashMap<String, Double> unitsMap)
	{
		//final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
		//String[] sortedDiskUnits = new String[diskUnits.size()];
		
		double oValue = 0.0;
		double tValue = 0.0;
		
		
		 Iterator it = unitsMap.entrySet().iterator();
		    while (it.hasNext()){
		        Map.Entry pairs = (Map.Entry)it.next();
		        String unit = pairs.getKey().toString();
		        double unitValue = (double)pairs.getValue();
		        
		        if(original.equalsIgnoreCase(unit))
		        	oValue = unitValue;
		        
		        if(target.equalsIgnoreCase(unit))
		        	tValue = unitValue;
		        
		        }
		    
		return (oValue/tValue); 
		/*
		int oIndex = 0;
		int targetIndex = 0;
		
		for(int i = 0 ; i < sortedDiskUnits.length ; i++)
		{
			if(sortedDiskUnits[i].equalsIgnoreCase(original))
				oIndex = i;
			
			if(sortedDiskUnits[i].equalsIgnoreCase(target))
				targetIndex = i;
		}
		int difference = oIndex-targetIndex;
		double result = 1.0;
		
		if(difference > 0)
		{
			for(int i = 0 ; i < difference ; i++)
				result = result * 1024.0;
		}else if(difference < 0)
		{
			for(int i = 0 ; i < Math.abs(difference) ; i++)
				result = result/1024.0;
		}
		*/
		
	}
	
	/**
	 * 
	 * @param folderPath
	 * 刪除資料夾
	 */
	 public void delFolder(String folderPath) {  
		 
		   deleteFolderFiles(folderPath);
		   String filePath = folderPath;  
		   filePath = filePath.toString();  
		   File myFilePath = new File(filePath);  
		   myFilePath.delete();
		    
		 } 
	
	 
	 /**
		 * 
		 * @param sourcePath
		 * @param destPath
		 * 複製資料夾底下除了資料夾外的所有檔案
		 */
		public void copyFolderFiles(String sourcePath, String destPath){
			try { 
				//log.info(" copy " + sourcePath + " to " + destPath);

	            boolean process = true;

	            File sourceFile = new File(sourcePath);
	            File destFile = new File(destPath);
	            
	            if(!sourceFile.exists()){
	            	//logger.warn(" 來源資料夾不存在");
	            	process = false;
	            }
	            if(!destFile.exists() && (process == true) ){
	            	//logger.warn(" 目的資料夾不存在, 嚐試建立");
	            	try{
	            		destFile.mkdirs();
	            	}
	            	catch(Exception e){
	            		//logger.warn(" 目的資料夾建立失敗");
	            		process = false;
	            	}
	            	if(!destFile.exists()){
	            		process = false;
	            	}
	            }
	            
	            if(!sourceFile.isDirectory() && process){
	            	//logger.warn(" 輸入來源路徑並非資料夾");
	            	process = false;
	            }
	            if(!destFile.isDirectory() && process){
	            	//logger.warn(" 輸入目的路徑並非資料夾");
	            	process = false;
	            }
	            
	            if(process == false){
	            	
	            }
	            else{
	            	File[] sourceFileList = sourceFile.listFiles();
	            	for(File f : sourceFileList){
	            		String sourceFileName = f.getName();
	            		String destFilePath = destFile.getPath()+File.separator+sourceFileName;
	            		if(!f.isDirectory()){
	            			copyFile(f.getPath(), destFilePath);
	            		}
	            	}
	            }
	        } 
	        catch(ArrayIndexOutOfBoundsException e) { 
	            e.printStackTrace();
	        } 
	        catch(Exception e) { 
	            e.printStackTrace(); 
	        } 
		}
		
		/*
		 * 輸出msg
		 */
		public void printMsg(PrintWriter out,String hrefLink)
		{
			out.print(hrefLink);
			out.flush();
			out.close();
		}

		//縣市英文代碼轉數字 取第三碼
	public String getCountryNumByType(String uType){
		
		String county ="";
		String countyCode =uType.substring(2,3);
		
		switch (countyCode){
		case "A": county  = "17";break; //臺北市
		case "C": county  = "18";break; //基隆市
		case "F": county  = "19";break; //新北市
		case "Z": county  = "20";break; //連江縣
		case "G": county  = "21";break; //宜蘭縣
		case "B": county  = "27";break; //臺中市 沒有台中縣
		case "O": county  = "23";break; //新竹市
		case "J": county  = "24";break; //新竹縣
		case "H": county  = "25";break; //桃園縣
		case "K": county  = "26";break; //苗栗縣
		case "L": county  = "27";break; //臺中市
		case "N": county  = "28";break; //彰化縣
		case "M": county  = "29";break; //南投縣
		case "I": county  = "30";break; //嘉義市
		case "Q": county  = "31";break; //嘉義縣
		case "P": county  = "32";break; //雲林縣
		case "R": county  = "33";break; //臺南市
		case "E": county  = "34";break; //高雄市
		case "D": county  = "33";break; //臺南市	沒有台南縣	
		case "X": county  = "36";break; //澎湖縣
		case "W": county  = "37";break; //金門縣
		case "T": county  = "38";break; //屏東縣
		case "V": county  = "39";break; //台東縣
		case "U": county  = "40";break; //花蓮縣
		case "5": county  = "555";break; //業者
		case "6": county  = "666";break; //學校
		}
		
		return county.toString();
	}
	
}
