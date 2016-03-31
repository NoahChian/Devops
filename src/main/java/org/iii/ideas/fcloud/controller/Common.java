package org.iii.ideas.fcloud.controller;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


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
}
