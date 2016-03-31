package org.iii.ideas.catering_service.rest.api;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.Batchdata;
import org.iii.ideas.catering_service.util.HibernateUtil;

/*
 * rule:
 * 1.同一個學校copy 的時間不可以重複
 * 2.要先確認資料有沒有重覆
 */
public class CopyMenu extends AbstractApiInterface<CopyMenuRequest, CopyMenuResponse> {

	@Override
	public void process() throws NamingException, ParseException {
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		int kid = this.getKitchenId();

		int oldSchoolId = this.requestObj.getSidOld();
		int newSchoolId = this.requestObj.getSidNew();
		String oldBegDate = this.requestObj.getStartDateOld();
		String oldEndDate = this.requestObj.getEndDateOld();
		String newBegDate = this.requestObj.getStartDateNew();
		Timestamp tsOldBegDate = HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", oldBegDate);
		Timestamp tsOldEndDate = HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", oldEndDate);
		Timestamp tsNewBegDate = HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", newBegDate);
		log.debug("copy Menu 來源學校ID:" + oldSchoolId + " 時間:" + this.requestObj.getStartDateOld() + "-"
				+ this.requestObj.getEndDateOld() + " 目的ID:" + newSchoolId + " 時間:"
				+ this.requestObj.getStartDateNew());
		this.responseObj.setMsg("");
		this.responseObj.setResStatus(0);
		if (tsNewBegDate.getTime() <= tsOldEndDate.getTime() && tsNewBegDate.getTime() >= tsOldBegDate.getTime()
				&& oldSchoolId == newSchoolId) {
			this.responseObj.setMsg("相同學校複製時間不可以重疊!");
			return;
		}

		Transaction tx = dbSession.beginTransaction();

		Timestamp timePoint = tsOldBegDate;
		while (timePoint.getTime() <= tsOldEndDate.getTime()) {
			String dayOfWeek = HibernateUtil.converTimestampToStr("E", timePoint, Locale.ENGLISH);
			// 如果不是六日就執行
			String dayOfWeekNew = HibernateUtil.converTimestampToStr("E", tsNewBegDate, Locale.ENGLISH);
			while (dayOfWeekNew.equals("Sat") || dayOfWeekNew.equals("Sun")) {
				log.debug("目的時間為  Sat/Sun:" + tsNewBegDate.toString() + " 來源日期:" + dayOfWeek + " 不新增菜單");
				tsNewBegDate = this.addDay(tsNewBegDate, 1);
				dayOfWeekNew = HibernateUtil.converTimestampToStr("E", tsNewBegDate, Locale.ENGLISH);
			}

			Criteria criteria = dbSession.createCriteria(Batchdata.class);
			String sourceDate = HibernateUtil.converTimestampToStr("yyyy/MM/dd", timePoint, Locale.ENGLISH);
			criteria.add(Restrictions.eq("menuDate", sourceDate));
			criteria.add(Restrictions.eq("schoolId", oldSchoolId));
			criteria.add(Restrictions.eq("kitchenId", kid));
			List Menus = criteria.list();
			//如果來源沒資料就自動改成下一天
			if (Menus.size() > 0) {
				Iterator<Batchdata> iterator = Menus.iterator();
				// 查詢是否有菜單就copy 一份到target 日期
				while (iterator.hasNext()) {
					Batchdata newBatchdata = iterator.next();
					String targetDate = HibernateUtil.converTimestampToStr("yyyy/MM/dd", tsNewBegDate,
							Locale.ENGLISH);
					// log.debug("CopyMenuRequest sid:"+newSchoolId+"source date:"+sourceDate+" target date:"+targetDate);
					if (HibernateUtil.queryBatchdataByUK(this.dbSession, kid, newSchoolId, targetDate,
							newBatchdata.getLotNumber()) == null) {
						// 如果menu資料不存在也同時自動新增一筆	
						try {
							HibernateUtil.copyBatchdata(dbSession, newBatchdata,targetDate,newSchoolId );
						} catch (Exception e) {
							this.responseObj.setMsg("日期:" + targetDate+" 問題:"+e.getMessage());
							if(!tx.wasRolledBack()){
								tx.rollback();
							}
							return;
						}
					} else {
						this.responseObj.setMsg("已有菜單資料存在於目的學校中 日期:" + targetDate);
						if(!tx.wasRolledBack()){
							tx.rollback();
						}
						return;
					}
				}
				
				tsNewBegDate = this.addDay(tsNewBegDate, 1);
				log.debug("Time:" + timePoint.toString() + " Point:" + tsNewBegDate.toString());
			}
			timePoint = this.addDay(timePoint, 1);
		}
		if (!tx.wasCommitted()) {
			tx.commit();
		}
		this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);

	}

	public Timestamp addDay(Timestamp current, long days) {
		long oneDay = days * 24 * 60 * 60 * 1000;
		current.setTime(current.getTime() + oneDay);
		return current;
	}

}
