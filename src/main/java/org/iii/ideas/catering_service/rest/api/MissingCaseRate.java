package org.iii.ideas.catering_service.rest.api;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
//import javax.persistence.Query;








import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.util.AuthenUtil;
import org.iii.ideas.catering_service.util.CateringServiceUtil;

//----縣市缺漏率與上線率---chu 20151201
public final class MissingCaseRate extends AbstractApiInterface<MissingCaseRateRequest, MissingCaseRateResponse> {

	private static final long serialVersionUID = -898989873426899758L;
	@Override
	public void process() throws NamingException, ParseException {
		if (this.getUsername() == null) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		String inDate = this.requestObj.getInDate();
		String schoolType = this.requestObj.getSchoolType();
		Integer county = this.getCounty();//來判斷中央OR地方
				
		inDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", inDate));
		
	
		if (CateringServiceUtil.isEmpty(inDate)) {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("請確認日期填寫正確!!");
			return;
		}
		
//		int ST = Integer.valueOf(schoolType);
		boolean trigger = true;
		if(county == 9999){//中央9999
			trigger = false;
		}
		String CidStr = "";
		if(trigger){//若為地方，則加入縣市查詢
			CidStr = "AND c.countyid='"+county+"'";
		}else{
			CidStr = "";
		}
		System.out.print(trigger);
		System.out.print(CidStr);
		//SQL切得很怪, but it's work finally!!
		//query all
		//修改Query機制 modify by Ellis 20160114
		String sqla = "select a.countyName, IFNULL(bcount,0)"
			+" AS haveMenu, total-IFNULL(nomenutotal,0) AS mustMenu,"
			+"((IF(IFNULL(ROUND(bcount / (total-IFNULL(nomenutotal,0)), 4),0) >1, 1,"
			+" IFNULL(ROUND(bcount / (total - IFNULL(nomenutotal,0)), 4),0)))*100) "
			+"AS rateR2,total, IFNULL(nomenutotal,0) "
			+"AS noMenu FROM( SELECT COUNT(*) AS total, c.countyid, c.county "
			+"AS countyName FROM school s, county c, code cd "
			+"WHERE c.countyid = s.countyid AND cd.name = c.countyid AND c.enable = 1 AND s.enable = 1 "
			+"AND length(s.SchoolCode)=6 "
			+"AND (SUBSTRING(s.SchoolCode, 4, 1) IN ("+schoolType+") )"+CidStr
			+"GROUP BY countyid ORDER BY cd.sort ASC) AS a "
			+"LEFT JOIN ( SELECT c.county, s.countyid, COUNT(DISTINCT b.schoolid) "
			+"AS bcount FROM batchdata b, school s, county c, code cd WHERE b.schoolid = s.schoolid "
			+"AND b.MenuDate ='"+inDate+"' "
			+"AND s.countyid = c.countyid AND c.countyid = cd.name AND c.enable = 1 AND s.enable = 1 AND b.enable = 1 and b.SchoolId not in (select SchoolId from noMenuDate nm where "
			+"nm.startdate <='"+inDate+"' AND nm.enddate >='"+inDate+"') "
			+"AND length(s.SchoolCode)=6 "
			+"AND (SUBSTRING(s.SchoolCode, 4, 1) IN ("+schoolType+")) "+CidStr
			+"GROUP BY s.countyid ORDER BY cd.sort ASC) AS b ON a.countyid = b.countyid "
			+"LEFT JOIN ( SELECT c.county, c.CountyId,COUNT(DISTINCT nm.SchoolId) AS nomenutotal from noMenuDate nm , school s , county c WHERE nm.schoolId = s.SchoolId "
			+"and (nm.startdate <='"+inDate+"' AND nm.enddate >='"+inDate+"') and s.CountyId = c.CountyId AND c.enable = 1 AND s.enable = 1 "
			+" AND length(s.SchoolCode)=6 "
			+ " AND substring(s.SchoolCode,4,1) in ("+schoolType+") "+CidStr
			+"GROUP BY s.countyid) AS c ON a.countyid = c.countyid";
		String sqlb ="select s.schoolName, k.kitchenName, k.Ownner, k.email, k.tel from school s, schoolkitchen sk, kitchen k "
			+ "where s.SchoolId = sk.schoolid and sk.KitchenId = k.kitchenId and s.SchoolId "
			+ "not in( select SchoolId from batchdata b where b.MenuDate = '"+inDate+"' "
			+ "and b.enable = 1) "
			+" and length(s.SchoolCode)=6 "
			+ "and substring(s.SchoolCode,4,1) in ("+schoolType+") "
			+ "and s.schoolId not in(select nmd.schoolid from noMenuDate nmd where nmd.startdate <= '"+inDate+"' "
			+ "AND nmd.enddate >= '"+inDate+"' "
			+ ") "
			+ "and CountyId ='"+county+"'"
			+ "and s.enable = 1 "
			+ "group by s.schoolName";
		
		//JHSchool國中
//		String sql2 = "select a.countyName, IFNULL(bcount,0)"
//			+" AS haveMenu, total-IFNULL(nomenutotal,0) AS mustMenu,"
//			+"((IF(IFNULL(ROUND(bcount / (total-IFNULL(nomenutotal,0)), 4),0) >1, 1,"
//			+" IFNULL(ROUND(bcount / (total - IFNULL(nomenutotal,0)), 4),0)))*100) "
//			+"AS rateR2,total, IFNULL(nomenutotal,0) "
//			+"AS noMenu FROM( SELECT COUNT(*) AS total, c.countyid, c.county "
//			+"AS countyName FROM school s, county c, code cd "
//			+"WHERE c.countyid = s.countyid AND cd.name = c.countyid AND c.enable = 1 AND s.enable = 1 "
//			+"AND (SUBSTRING(s.SchoolCode, 4, 1) IN (5) )"+CidStr
//			+"GROUP BY countyid ORDER BY cd.sort ASC) AS a "
//			+"LEFT JOIN ( SELECT c.county, s.countyid, COUNT(DISTINCT b.schoolid) "
//			+"AS bcount FROM batchdata b, school s, county c, code cd WHERE b.schoolid = s.schoolid "
//			+"AND b.MenuDate ='"+inDate+"' "
//			+"AND s.countyid = c.countyid AND c.countyid = cd.name AND c.enable = 1 AND s.enable = 1 AND b.enable = 1 and b.SchoolId not in (select SchoolId from noMenuDate nm where "
//			+"nm.startdate <='"+inDate+"' AND nm.enddate >='"+inDate+"') "
//			+"AND (SUBSTRING(s.SchoolCode, 4, 1) IN (5)) "+CidStr
//			+"GROUP BY s.countyid ORDER BY cd.sort ASC) AS b ON a.countyid = b.countyid "
//			+"LEFT JOIN ( SELECT c.county, c.CountyId,COUNT(DISTINCT nm.SchoolId) AS nomenutotal from noMenuDate nm , school s , county c WHERE nm.schoolId = s.SchoolId "
//			+"and (nm.startdate <='"+inDate+"' AND nm.enddate >='"+inDate+"') and s.CountyId = c.CountyId AND c.enable = 1 AND s.enable = 1 AND substring(s.SchoolCode,4,1) in (5) "+CidStr
//			+"GROUP BY s.countyid) AS c ON a.countyid = c.countyid";
//		//ESchool國小
//		String sql3 = "select a.countyName, IFNULL(bcount,0)"
//			+" AS haveMenu, total-IFNULL(nomenutotal,0) AS mustMenu,"
//			+"((IF(IFNULL(ROUND(bcount / (total-IFNULL(nomenutotal,0)), 4),0) >1, 1,"
//			+" IFNULL(ROUND(bcount / (total - IFNULL(nomenutotal,0)), 4),0)))*100) "
//			+"AS rateR2,total, IFNULL(nomenutotal,0) "
//			+"AS noMenu FROM( SELECT COUNT(*) AS total, c.countyid, c.county "
//			+"AS countyName FROM school s, county c, code cd "
//			+"WHERE c.countyid = s.countyid AND cd.name = c.countyid AND c.enable = 1 AND s.enable = 1 "
//			+"AND (SUBSTRING(s.SchoolCode, 4, 1) IN (6, 7, 8) )"+CidStr
//			+"GROUP BY countyid ORDER BY cd.sort ASC) AS a "
//			+"LEFT JOIN ( SELECT c.county, s.countyid, COUNT(DISTINCT b.schoolid) "
//			+"AS bcount FROM batchdata b, school s, county c, code cd WHERE b.schoolid = s.schoolid "
//			+"AND b.MenuDate ='"+inDate+"' "
//			+"AND s.countyid = c.countyid AND c.countyid = cd.name AND c.enable = 1 AND s.enable = 1 AND b.enable = 1 and b.SchoolId not in (select SchoolId from noMenuDate nm where "
//			+"nm.startdate <='"+inDate+"' AND nm.enddate >='"+inDate+"') "
//			+"AND (SUBSTRING(s.SchoolCode, 4, 1) IN (6, 7, 8)) "+CidStr
//			+"GROUP BY s.countyid ORDER BY cd.sort ASC) AS b ON a.countyid = b.countyid "
//			+"LEFT JOIN ( SELECT c.county, c.CountyId,COUNT(DISTINCT nm.SchoolId) AS nomenutotal from noMenuDate nm , school s , county c WHERE nm.schoolId = s.SchoolId "
//			+"and (nm.startdate <='"+inDate+"' AND nm.enddate >='"+inDate+"') and s.CountyId = c.CountyId AND c.enable = 1 AND s.enable = 1 AND substring(s.SchoolCode,4,1) in (6, 7, 8) "+CidStr
//			+"GROUP BY s.countyid) AS c ON a.countyid = c.countyid";
//		//PSchool幼兒園
//		String sql4 = "select a.countyName, IFNULL(bcount,0)"
//			+" AS haveMenu, total-IFNULL(nomenutotal,0) AS mustMenu,"
//			+"((IF(IFNULL(ROUND(bcount / (total-IFNULL(nomenutotal,0)), 4),0) >1, 1,"
//			+" IFNULL(ROUND(bcount / (total - IFNULL(nomenutotal,0)), 4),0)))*100) "
//			+"AS rateR2,total, IFNULL(nomenutotal,0) "
//			+"AS noMenu FROM( SELECT COUNT(*) AS total, c.countyid, c.county "
//			+"AS countyName FROM school s, county c, code cd "
//			+"WHERE c.countyid = s.countyid AND cd.name = c.countyid AND c.enable = 1 AND s.enable = 1 "
//			+"AND (SUBSTRING(s.SchoolCode, 4, 1) IN ('K', 'W', 'X', 'Y', 'Z') )"+CidStr
//			+"GROUP BY countyid ORDER BY cd.sort ASC) AS a "
//			+"LEFT JOIN ( SELECT c.county, s.countyid, COUNT(DISTINCT b.schoolid) "
//			+"AS bcount FROM batchdata b, school s, county c, code cd WHERE b.schoolid = s.schoolid "
//			+"AND b.MenuDate ='"+inDate+"' "
//			+"AND s.countyid = c.countyid AND c.countyid = cd.name AND c.enable = 1 AND s.enable = 1 AND b.enable = 1 and b.SchoolId not in (select SchoolId from noMenuDate nm where "
//			+"nm.startdate <='"+inDate+"' AND nm.enddate >='"+inDate+"') "
//			+"AND (SUBSTRING(s.SchoolCode, 4, 1) IN ('K', 'W', 'X', 'Y', 'Z')) "+CidStr
//			+"GROUP BY s.countyid ORDER BY cd.sort ASC) AS b ON a.countyid = b.countyid "
//			+"LEFT JOIN ( SELECT c.county, c.CountyId,COUNT(DISTINCT nm.SchoolId) AS nomenutotal from noMenuDate nm , school s , county c WHERE nm.schoolId = s.SchoolId "
//			+"and (nm.startdate <='"+inDate+"' AND nm.enddate >='"+inDate+"') and s.CountyId = c.CountyId AND c.enable = 1 AND s.enable = 1 AND substring(s.SchoolCode,4,1) in ('K', 'W', 'X', 'Y', 'Z') "+CidStr
//			+ "GROUP BY s.countyid) AS c ON a.countyid = c.countyid";
//		
//		//SQL for query detail info --20151204chu--
//		//all
//		String sql5 ="select s.schoolName, k.kitchenName, k.Ownner, k.email, k.tel from school s, schoolkitchen sk, kitchen k "
//				+ "where s.SchoolId = sk.schoolid and sk.KitchenId = k.kitchenId and s.SchoolId "
//				+ "not in( select SchoolId from batchdata b where b.MenuDate = '"+inDate+"' "
//				+ "and b.enable = 1) "
//				+ "and substring(s.SchoolCode,4,1) in (5,6,7,8,'K', 'W', 'X', 'Y', 'Z') "
//				+ "and s.schoolId not in(select nmd.schoolid from noMenuDate nmd where nmd.startdate <= '"+inDate+"' "
//				+ "AND nmd.enddate >= '"+inDate+"' "
//				+ ") "
//				+ "and CountyId ='"+county+"'"
//				+ "and s.enable = 1 "
//				+ "group by s.schoolName";
//		//SHSchool
//		String sql6 ="select s.schoolName, k.kitchenName, k.Ownner, k.email, k.tel from school s, schoolkitchen sk, kitchen k "
//				+ "where s.SchoolId = sk.schoolid and sk.KitchenId = k.kitchenId and s.SchoolId "
//				+ "not in( select SchoolId from batchdata b where b.MenuDate = '"+inDate+"' "
//				+ "and b.enable = 1) "
//				+ "and substring(s.SchoolCode,4,1) in (5) "
//				+ "and s.schoolId not in(select nmd.schoolid from noMenuDate nmd where nmd.startdate <= '"+inDate+"' "
//				+ "AND nmd.enddate >= '"+inDate+"' "
//				+ ") "
//				+ "and CountyId ='"+county+"'"
//				+ "and s.enable = 1 "
//				+ "group by s.schoolName";
//		//ESchool
//		String sql7 ="select s.schoolName, k.kitchenName, k.Ownner, k.email, k.tel from school s, schoolkitchen sk, kitchen k "
//				+ "where s.SchoolId = sk.schoolid and sk.KitchenId = k.kitchenId and s.SchoolId "
//				+ "not in( select SchoolId from batchdata b where b.MenuDate = '"+inDate+"' "
//				+ "and b.enable = 1) "
//				+ "and substring(s.SchoolCode,4,1) in (6,7,8) "
//				+ "and s.schoolId not in(select nmd.schoolid from noMenuDate nmd where nmd.startdate <= '"+inDate+"' "
//				+ "AND nmd.enddate >= '"+inDate+"' "
//				+ ") "
//				+ "and CountyId ='"+county+"'"
//				+ "and s.enable = 1 "
//				+ "group by s.schoolName";
//		//PSchool
//		String sql8 ="select s.schoolName, k.kitchenName, k.Ownner, k.email, k.tel from school s, schoolkitchen sk, kitchen k "
//				+ "where s.SchoolId = sk.schoolid and sk.KitchenId = k.kitchenId and s.SchoolId "
//				+ "not in( select SchoolId from batchdata b where b.MenuDate = '"+inDate+"' "
//				+ "and b.enable = 1) "
//				+ "and substring(s.SchoolCode,4,1) in ('K', 'W', 'X', 'Y', 'Z') "
//				+ "and s.schoolId not in(select nmd.schoolid from noMenuDate nmd where nmd.startdate <= '"+inDate+"' "
//				+ "AND nmd.enddate >= '"+inDate+"' "
//				+ ") "
//				+ "and CountyId ='"+county+"'"
//				+ "and s.enable = 1 "
//				+ "group by s.schoolName";
		//set SQL
//		String sqla="";
//		String sqlb="";
//		if(ST==1){
//			sqla = sql1;
//			sqlb = sql5;
//		}else if(ST==2){
//			sqla = sql2;
//			sqlb = sql6;
//		}else if(ST==3){
//			sqla = sql3;
//			sqlb = sql7;
//		}else{
//			sqla = sql4;
//			sqlb = sql8;
//		}
		
		//row out
		Session session = this.dbSession;
		SQLQuery sqlQuery = session.createSQLQuery(sqla);
		Iterator<Object[]> iterator = sqlQuery.list().iterator();
		while(iterator.hasNext()) {
			Object[] row= iterator.next();
			MICRate MI = new MICRate();
			MI.setCountyName(row[0].toString());
			MI.setHaveMenu(row[1].toString());
			MI.setMustMenu(row[2].toString());
			Float f1 = Float.parseFloat(row[3].toString());
			MI.setRateR2(f1.toString());
			MI.setTotalSchool(row[4].toString());
			MI.setNoMenu(row[5].toString());
			this.responseObj.getMICRate().add(MI);
		}
		//若為地方, row out detail --20151204
		if(trigger){
			SQLQuery sqlQuery2 = session.createSQLQuery(sqlb);
			Iterator<Object[]> iterator2 = sqlQuery2.list().iterator();
			while(iterator2.hasNext()){
				Object[] row = iterator2.next();
				RateDetail RD = new RateDetail();
				RD.setSchoolName(row[0].toString());
				RD.setKitchenName(row[1].toString());
				if(row[2].toString().equals("")){
					RD.setOwnner("未填");
				}else{
					RD.setOwnner(row[2].toString());
				}
				if(row[3].toString().equals("")){
					RD.setEmail("未填");
				}else{
					RD.setEmail(row[3].toString());
				}
				if(row[4].toString().equals("")){
					RD.setTel("未填");
				}else{
					RD.setTel(row[4].toString());
				}
				this.responseObj.getRateDetail().add(RD);
			}
		}
		//this.responseObj.setMICRate(micr);
		this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);
	}
//  for export excel by java, keep it temp---20151210 chu---
//	public List<MICRate> micRate(String inDate, String schoolType) throws ParseException {
//		inDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", inDate));
//		int ST = Integer.valueOf(schoolType);
//		
//	}

/*	query(inDate, schoolType, county.toString());
	
	}
	
	public query(String inDate, String schoolType, String county) throws ParseException{
		inDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", inDate));
		int ST = Integer.valueOf(schoolType);
		boolean trigger = true;
		
		if(Integer.valueOf(county) == 9999){//中央9999
			trigger = false;
		}
		String CidStr = "";
		if(trigger){//若為地方，則加入縣市查詢
			CidStr = "AND c.countyid='"+county+"'";
		}else{
			CidStr = "";
		}
		System.out.print(trigger);
		System.out.print(CidStr);
		//SQL切得很怪, but it's work finally!!
		//query all
		String sql1 = "select a.countyName, IFNULL(bcount,0)"
			+" AS haveMenu, total-IFNULL(nomenutotal,0) AS mustMenu,"
			+"((IF(IFNULL(ROUND(bcount / (total-IFNULL(nomenutotal,0)), 4),0) >1, 1,"
			+" IFNULL(ROUND(bcount / (total - IFNULL(nomenutotal,0)), 4),0)))*100) "
			+"AS rateR2,total, IFNULL(nomenutotal,0) "
			+"AS noMenu FROM( SELECT COUNT(*) AS total, c.countyid, c.county "
			+"AS countyName FROM school s, county c, code cd "
			+"WHERE c.countyid = s.countyid AND cd.name = c.countyid AND c.enable = 1 AND s.enable = 1 "
			+"AND (SUBSTRING(s.SchoolCode, 4, 1) IN (5 , 6, 7, 8,'K', 'W', 'X', 'Y', 'Z') )"+CidStr
			+"GROUP BY countyid ORDER BY cd.sort ASC) AS a "
			+"LEFT JOIN ( SELECT c.county, s.countyid, COUNT(DISTINCT b.schoolid) "
			+"AS bcount FROM batchdata b, school s, county c, code cd WHERE b.schoolid = s.schoolid "
			+"AND b.MenuDate ='"+inDate+"' "
			+"AND s.countyid = c.countyid AND c.countyid = cd.name AND c.enable = 1 AND s.enable = 1 AND b.enable = 1 and b.SchoolId not in (select SchoolId from noMenuDate nm where "
			+"nm.startdate <='"+inDate+"' AND nm.enddate >='"+inDate+"') "
			+"AND (SUBSTRING(s.SchoolCode, 4, 1) IN (5 , 6, 7, 8,'K', 'W', 'X', 'Y', 'Z')) "+CidStr
			+"GROUP BY s.countyid ORDER BY cd.sort ASC) AS b ON a.countyid = b.countyid "
			+"LEFT JOIN ( SELECT c.county, c.CountyId,COUNT(DISTINCT nm.SchoolId) AS nomenutotal from noMenuDate nm , school s , county c WHERE nm.schoolId = s.SchoolId "
			+"and (nm.startdate <='"+inDate+"' AND nm.enddate >='"+inDate+"') and s.CountyId = c.CountyId AND c.enable = 1 AND s.enable = 1 AND substring(s.SchoolCode,4,1) in (5 , 6, 7, 8,'K', 'W', 'X', 'Y', 'Z') "+CidStr
			+"GROUP BY s.countyid) AS c ON a.countyid = c.countyid";
		//JHSchool國中
		String sql2 = "select a.countyName, IFNULL(bcount,0)"
			+" AS haveMenu, total-IFNULL(nomenutotal,0) AS mustMenu,"
			+"((IF(IFNULL(ROUND(bcount / (total-IFNULL(nomenutotal,0)), 4),0) >1, 1,"
			+" IFNULL(ROUND(bcount / (total - IFNULL(nomenutotal,0)), 4),0)))*100) "
			+"AS rateR2,total, IFNULL(nomenutotal,0) "
			+"AS noMenu FROM( SELECT COUNT(*) AS total, c.countyid, c.county "
			+"AS countyName FROM school s, county c, code cd "
			+"WHERE c.countyid = s.countyid AND cd.name = c.countyid AND c.enable = 1 AND s.enable = 1 "
			+"AND (SUBSTRING(s.SchoolCode, 4, 1) IN (5) )"+CidStr
			+"GROUP BY countyid ORDER BY cd.sort ASC) AS a "
			+"LEFT JOIN ( SELECT c.county, s.countyid, COUNT(DISTINCT b.schoolid) "
			+"AS bcount FROM batchdata b, school s, county c, code cd WHERE b.schoolid = s.schoolid "
			+"AND b.MenuDate ='"+inDate+"' "
			+"AND s.countyid = c.countyid AND c.countyid = cd.name AND c.enable = 1 AND s.enable = 1 AND b.enable = 1 and b.SchoolId not in (select SchoolId from noMenuDate nm where "
			+"nm.startdate <='"+inDate+"' AND nm.enddate >='"+inDate+"') "
			+"AND (SUBSTRING(s.SchoolCode, 4, 1) IN (5)) "+CidStr
			+"GROUP BY s.countyid ORDER BY cd.sort ASC) AS b ON a.countyid = b.countyid "
			+"LEFT JOIN ( SELECT c.county, c.CountyId,COUNT(DISTINCT nm.SchoolId) AS nomenutotal from noMenuDate nm , school s , county c WHERE nm.schoolId = s.SchoolId "
			+"and (nm.startdate <='"+inDate+"' AND nm.enddate >='"+inDate+"') and s.CountyId = c.CountyId AND c.enable = 1 AND s.enable = 1 AND substring(s.SchoolCode,4,1) in (5) "+CidStr
			+"GROUP BY s.countyid) AS c ON a.countyid = c.countyid";
		//ESchool國小
		String sql3 = "select a.countyName, IFNULL(bcount,0)"
			+" AS haveMenu, total-IFNULL(nomenutotal,0) AS mustMenu,"
			+"((IF(IFNULL(ROUND(bcount / (total-IFNULL(nomenutotal,0)), 4),0) >1, 1,"
			+" IFNULL(ROUND(bcount / (total - IFNULL(nomenutotal,0)), 4),0)))*100) "
			+"AS rateR2,total, IFNULL(nomenutotal,0) "
			+"AS noMenu FROM( SELECT COUNT(*) AS total, c.countyid, c.county "
			+"AS countyName FROM school s, county c, code cd "
			+"WHERE c.countyid = s.countyid AND cd.name = c.countyid AND c.enable = 1 AND s.enable = 1 "
			+"AND (SUBSTRING(s.SchoolCode, 4, 1) IN (6, 7, 8) )"+CidStr
			+"GROUP BY countyid ORDER BY cd.sort ASC) AS a "
			+"LEFT JOIN ( SELECT c.county, s.countyid, COUNT(DISTINCT b.schoolid) "
			+"AS bcount FROM batchdata b, school s, county c, code cd WHERE b.schoolid = s.schoolid "
			+"AND b.MenuDate ='"+inDate+"' "
			+"AND s.countyid = c.countyid AND c.countyid = cd.name AND c.enable = 1 AND s.enable = 1 AND b.enable = 1 and b.SchoolId not in (select SchoolId from noMenuDate nm where "
			+"nm.startdate <='"+inDate+"' AND nm.enddate >='"+inDate+"') "
			+"AND (SUBSTRING(s.SchoolCode, 4, 1) IN (6, 7, 8)) "+CidStr
			+"GROUP BY s.countyid ORDER BY cd.sort ASC) AS b ON a.countyid = b.countyid "
			+"LEFT JOIN ( SELECT c.county, c.CountyId,COUNT(DISTINCT nm.SchoolId) AS nomenutotal from noMenuDate nm , school s , county c WHERE nm.schoolId = s.SchoolId "
			+"and (nm.startdate <='"+inDate+"' AND nm.enddate >='"+inDate+"') and s.CountyId = c.CountyId AND c.enable = 1 AND s.enable = 1 AND substring(s.SchoolCode,4,1) in (6, 7, 8) "+CidStr
			+"GROUP BY s.countyid) AS c ON a.countyid = c.countyid";
		//PSchool幼兒園
		String sql4 = "select a.countyName, IFNULL(bcount,0)"
			+" AS haveMenu, total-IFNULL(nomenutotal,0) AS mustMenu,"
			+"((IF(IFNULL(ROUND(bcount / (total-IFNULL(nomenutotal,0)), 4),0) >1, 1,"
			+" IFNULL(ROUND(bcount / (total - IFNULL(nomenutotal,0)), 4),0)))*100) "
			+"AS rateR2,total, IFNULL(nomenutotal,0) "
			+"AS noMenu FROM( SELECT COUNT(*) AS total, c.countyid, c.county "
			+"AS countyName FROM school s, county c, code cd "
			+"WHERE c.countyid = s.countyid AND cd.name = c.countyid AND c.enable = 1 AND s.enable = 1 "
			+"AND (SUBSTRING(s.SchoolCode, 4, 1) IN ('K', 'W', 'X', 'Y', 'Z') )"+CidStr
			+"GROUP BY countyid ORDER BY cd.sort ASC) AS a "
			+"LEFT JOIN ( SELECT c.county, s.countyid, COUNT(DISTINCT b.schoolid) "
			+"AS bcount FROM batchdata b, school s, county c, code cd WHERE b.schoolid = s.schoolid "
			+"AND b.MenuDate ='"+inDate+"' "
			+"AND s.countyid = c.countyid AND c.countyid = cd.name AND c.enable = 1 AND s.enable = 1 AND b.enable = 1 and b.SchoolId not in (select SchoolId from noMenuDate nm where "
			+"nm.startdate <='"+inDate+"' AND nm.enddate >='"+inDate+"') "
			+"AND (SUBSTRING(s.SchoolCode, 4, 1) IN ('K', 'W', 'X', 'Y', 'Z')) "+CidStr
			+"GROUP BY s.countyid ORDER BY cd.sort ASC) AS b ON a.countyid = b.countyid "
			+"LEFT JOIN ( SELECT c.county, c.CountyId,COUNT(DISTINCT nm.SchoolId) AS nomenutotal from noMenuDate nm , school s , county c WHERE nm.schoolId = s.SchoolId "
			+"and (nm.startdate <='"+inDate+"' AND nm.enddate >='"+inDate+"') and s.CountyId = c.CountyId AND c.enable = 1 AND s.enable = 1 AND substring(s.SchoolCode,4,1) in ('K', 'W', 'X', 'Y', 'Z') "+CidStr
			+ "GROUP BY s.countyid) AS c ON a.countyid = c.countyid";
		
		//SQL for query detail info --20151204chu--
		//all
		String sql5 ="select s.schoolName, k.kitchenName, k.Ownner, k.email, k.tel from school s, schoolkitchen sk, kitchen k "
				+ "where s.SchoolId = sk.schoolid and sk.KitchenId = k.kitchenId and s.SchoolId "
				+ "not in( select SchoolId from batchdata b where b.MenuDate = '"+inDate+"' "
				+ "and b.enable = 1) "
				+ "and substring(s.SchoolCode,4,1) in (5,6,7,8,'K', 'W', 'X', 'Y', 'Z') "
				+ "and s.schoolId not in(select nmd.schoolid from noMenuDate nmd where nmd.startdate <= '"+inDate+"' "
				+ "AND nmd.enddate >= '"+inDate+"' "
				+ ") "
				+ "and CountyId ='"+county+"'"
				+ "and s.enable = 1 "
				+ "group by s.schoolName";
		//SHSchool
		String sql6 ="select s.schoolName, k.kitchenName, k.Ownner, k.email, k.tel from school s, schoolkitchen sk, kitchen k "
				+ "where s.SchoolId = sk.schoolid and sk.KitchenId = k.kitchenId and s.SchoolId "
				+ "not in( select SchoolId from batchdata b where b.MenuDate = '"+inDate+"' "
				+ "and b.enable = 1) "
				+ "and substring(s.SchoolCode,4,1) in (5) "
				+ "and s.schoolId not in(select nmd.schoolid from noMenuDate nmd where nmd.startdate <= '"+inDate+"' "
				+ "AND nmd.enddate >= '"+inDate+"' "
				+ ") "
				+ "and CountyId ='"+county+"'"
				+ "and s.enable = 1 "
				+ "group by s.schoolName";
		//ESchool
		String sql7 ="select s.schoolName, k.kitchenName, k.Ownner, k.email, k.tel from school s, schoolkitchen sk, kitchen k "
				+ "where s.SchoolId = sk.schoolid and sk.KitchenId = k.kitchenId and s.SchoolId "
				+ "not in( select SchoolId from batchdata b where b.MenuDate = '"+inDate+"' "
				+ "and b.enable = 1) "
				+ "and substring(s.SchoolCode,4,1) in (6,7,8) "
				+ "and s.schoolId not in(select nmd.schoolid from noMenuDate nmd where nmd.startdate <= '"+inDate+"' "
				+ "AND nmd.enddate >= '"+inDate+"' "
				+ ") "
				+ "and CountyId ='"+county+"'"
				+ "and s.enable = 1 "
				+ "group by s.schoolName";
		//PSchool
		String sql8 ="select s.schoolName, k.kitchenName, k.Ownner, k.email, k.tel from school s, schoolkitchen sk, kitchen k "
				+ "where s.SchoolId = sk.schoolid and sk.KitchenId = k.kitchenId and s.SchoolId "
				+ "not in( select SchoolId from batchdata b where b.MenuDate = '"+inDate+"' "
				+ "and b.enable = 1) "
				+ "and substring(s.SchoolCode,4,1) in ('K', 'W', 'X', 'Y', 'Z') "
				+ "and s.schoolId not in(select nmd.schoolid from noMenuDate nmd where nmd.startdate <= '"+inDate+"' "
				+ "AND nmd.enddate >= '"+inDate+"' "
				+ ") "
				+ "and CountyId ='"+county+"'"
				+ "and s.enable = 1 "
				+ "group by s.schoolName";
		//set SQL
		String sqla="";
		String sqlb="";
		if(ST==1){
			sqla = sql1;
			sqlb = sql5;
		}else if(ST==2){
			sqla = sql2;
			sqlb = sql6;
		}else if(ST==3){
			sqla = sql3;
			sqlb = sql7;
		}else{
			sqla = sql4;
			sqlb = sql8;
		}
		
		//row out
		Session session = this.dbSession;
		SQLQuery sqlQuery = session.createSQLQuery(sqla);
		Iterator<Object[]> iterator = sqlQuery.list().iterator();
		List<MICRate> micr = new ArrayList();
		while(iterator.hasNext()) {
			Object[] row= iterator.next();
			MICRate MI = new MICRate();
			MI.setCountyName(row[0].toString());
			MI.setHaveMenu(row[1].toString());
			MI.setMustMenu(row[2].toString());
			Float f1 = Float.parseFloat(row[3].toString());
			MI.setRateR2(f1.toString());
			MI.setTotalSchool(row[4].toString());
			MI.setNoMenu(row[5].toString());
			micr.add(MI);
			this.responseObj.setMICRate(micr);
			//this.responseObj.getMICRate().add(MI);
		}
		//若為地方, row out detail --20151204
		List<RateDetail> rDetail = new ArrayList();
		if(trigger){
			SQLQuery sqlQuery2 = session.createSQLQuery(sqlb);
			Iterator<Object[]> iterator2 = sqlQuery2.list().iterator();
			while(iterator2.hasNext()){
				Object[] row = iterator2.next();
				RateDetail RD = new RateDetail();
				RD.setSchoolName(row[0].toString());
				RD.setKitchenName(row[1].toString());
				if(row[2].toString().equals("")){
					RD.setOwnner("未填");
				}else{
					RD.setOwnner(row[2].toString());
				}
				if(row[3].toString().equals("")){
					RD.setEmail("未填");
				}else{
					RD.setEmail(row[3].toString());
				}
				if(row[4].toString().equals("")){
					RD.setTel("未填");
				}else{
					RD.setTel(row[4].toString());
				}
				System.out.print("["+row[2]+"]");
				System.out.print("["+row[3]+"]");
				System.out.print("["+row[4]+"]");
				rDetail.add(RD);
				this.responseObj.setRateDetail(rDetail);
			}
		}
		this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);
		return rDetail;
	}*/
}

