package org.iii.ideas.catering_service.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author Raymond
 * 
 */
public class DateUtil {

	/**
	 * 取每年最後一日
	 * 
	 * @return
	 */
	public static Calendar getEndOfThisYear() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, Calendar.DECEMBER);
		calendar.set(Calendar.DATE, 31);
		Calendar cal = (Calendar) calendar.clone();
		return cal;
	}

	/**
	 * 轉換 Calendar To Timestamp
	 * 
	 * @param cal
	 * @return
	 */
	public static Timestamp transCalendarToTimestamp(Calendar cal) {
		Date date = cal.getTime();
		Timestamp timestamp = new Timestamp(date.getTime());
		return timestamp;
	}

	/**
	 * 取得當日日期
	 * 
	 * @param format
	 * @return
	 */
	public static String getSystemDate(String format) {
		SimpleDateFormat sdFormat = new SimpleDateFormat(format);
		Date current = new Date();
		try {
			String dateString = sdFormat.format(current);
			return dateString;
		} catch (Exception ex) {
			return "";
		}
	}

	public static Date convertDataStringToDate(String dataString, String format) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Date date = formatter.parse(dataString);
		return date;
	}

	public static Calendar convertDataStringToCalendar(String dataString, String format) throws Exception {
		Date date = convertDataStringToDate(dataString, format);
		if (date == null)
			return null;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;

	}

	public static boolean isOverTime(Timestamp dateTS, String limitString) throws Exception {

		Date today = new Date();

		Timestamp todayLimit = CateringServiceUtil.convertStrToTimestamp("yyyy-MM-dd HH:mm:ss", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(today)));

		if (dateTS.getTime() > todayLimit.getTime()) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean isFailDateString(String dateString, String format) throws Exception {
		Calendar sysCal = Calendar.getInstance();
		Calendar cal = convertDataStringToCalendar(dateString, format);

		if(cal == null)
			return true;
		
		int sysYear = sysCal.get(Calendar.YEAR);
		int cellYear = cal.get(Calendar.YEAR);
		int minus = sysYear - cellYear;

		//不可超過20年 上下
		if (minus > 20 || minus < -20)
			return true;
		else
			return false;
	}
}
