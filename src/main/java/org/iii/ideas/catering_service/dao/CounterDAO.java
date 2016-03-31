package org.iii.ideas.catering_service.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;


@Transactional
public class CounterDAO {
	private static final Logger log = LoggerFactory.getLogger(CounterDAO.class);
	public static final String ID="id";
	public static final String FUNC_NAME="funcName";
	public static final String DATE="date";
	public static final String COUNT="count";
	public static final String KITCHENID="kitchenid";
	public static final String TYPE="type";
	public static final String TABLENAME="counter";
	
	private static Connection conn_jdbc; 
	
	private SessionFactory sessionFactory;
	private Session dbSession;
	
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session getCurrentSession() {
		if (this.dbSession==null){
			return sessionFactory.getCurrentSession();
		}else{
			return this.dbSession;
		}
	}
	
	public void closeSession(){
		this.dbSession.close();
	}
	public void  openSessionFactory(){
		this.sessionFactory=HibernateUtil.buildSessionFactory();
		this.dbSession = sessionFactory.openSession();
	}

	public void openJDBC(){
		
	}
	

	public void save(Counter transientInstance) {
		log.debug("saving Counter instance");
		Session dbsession = getCurrentSession();
		Transaction tx = dbsession.beginTransaction();
		try {
			
			dbsession.save(transientInstance);
			tx.commit();
			
			log.debug("save successful");
		} catch (RuntimeException re) {
			tx.rollback();
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Counter persistentInstance) {
		log.debug("deleting Counter instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	
	//------
	//計數使用function 及log to file 
	public void increaseCounterByFuncNameAndDate(String funcName,String date,Integer kitchenId) throws Exception{
		log.debug("increase counter by function:  "+funcName);
		
		//	
		try {
			Configuration config =new Configuration().configure();			
			String connstr=config.getProperty("connection.url");
			CounterDAO.conn_jdbc=DriverManager.getConnection(connstr,config.getProperty("connection.username"),config.getProperty("connection.password"));
			Statement st=CounterDAO.conn_jdbc.createStatement();
			int result=st.executeUpdate("Update cateringservice.counter set count=count+1 where "+CounterDAO.FUNC_NAME+"='"+
						funcName+"' and "+CounterDAO.DATE+"='"+this.getTodayString()+"' and "+CounterDAO.KITCHENID+"="+kitchenId+";");

			//System.out.println("update result: "+result);
			
			//如果沒有此function的計數，就新增一條record		
			if (result==0){
				//System.out.println("start insert");
				Counter record=new Counter();
				//record.setId(0);
				record.setDate(this.getTodayString());
				record.setFuncName(funcName);
				record.setType("");
				record.setCount(1);
				record.setKitchenid(kitchenId);
				this.save(record);				
			}
			CounterDAO.conn_jdbc.close();
			
		} catch (SQLException ex) {
			log.error("increase counter error, func:"+funcName+", kitchenid="+kitchenId +"/"+ex.getMessage());
			throw ex;
		}
		
	}

	//依日期及學校計數
	public List countByFuncNameAndDateRange(String funcName,String startDate,String endDate,Integer sid) {
		String queryString="";
		queryString = "select m.date,m.count  from Counter m where m.funcName=:funcName and m.date between :from and :to";
		
		//queryString="select model.date,model.count from Counter model where model.funcName=:funcName ";
		System.out.println(queryString);
		if (sid!=0){
			queryString +=" and m.kitchenid=:sid";
		}	
		try {
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setString("funcName",funcName);
			queryObject.setString("from", startDate);
			queryObject.setString("to", endDate);
			if (sid!=0){
				queryObject.setInteger("sid",sid);
			}			
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("countByFuncNameAndDateRange error", re);
			CateringServiceUtil.weblogToFile("countByFuncNameAndDateRange error", "null", null, "fail add counter");
			throw re;
		}	
	}
	
	public List listQueryCountByFuncAndCountyAndDateRange(String funcName,Integer countyId,String startDate,String endDate){
		String queryString="";
		/*queryString = "select m.date,m.count ,m.kitchenId ,s.schoolName from Counter m right join School s on m.kitchenId=s.schoolId "+
						" where s.countyId=:countyId and  m.funcName=:funcName and m.date between :from and :to"+
						" order by m.kitchenId ASC, m.date ASC";
		*/
		queryString = "select m.date,m.count ,m.kitchenid ,s.schoolName from Counter m , School s "+
				" where m.kitchenid=s.schoolId and  s.countyId=:countyId and  m.funcName=:funcName and m.date between :from and :to"+
				" order by m.kitchenid ASC, m.date ASC";
		//queryString="select model.date,model.count from Counter model where model.funcName=:funcName ";
		System.out.println(queryString);
		try {
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setString("funcName",funcName);
			queryObject.setString("from", startDate);
			queryObject.setString("to", endDate);
			queryObject.setInteger("countyId", countyId);
			return queryObject.list();
			
			
		} catch (RuntimeException re) {
			//log.error("listQueryCountByFuncAndCountyAndDateRange error", re);
			CateringServiceUtil.weblogToFile("listQueryCountByFuncAndCountyAndDateRange", "null", null, "fail add counter");
			throw re;
		}	
	}
	
	
	/*
	public int getCountByTypeAndDate(String type,String startDate,String endDate){
		int total=0;
		return total;
		
	}
	*/

	//依type及日期找資料
	public List findByTypeAndDate(String type,String startDate,String endDate){
		log.debug("");
		try {
			String queryString = "from "+CounterDAO.TABLENAME+" as model where model."+
								CounterDAO.TYPE+"=? and "+CounterDAO.DATE +"between :from and :to";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(CounterDAO.TYPE,type);
			queryObject.setParameter("from", startDate);
			queryObject.setParameter("to", endDate);
			
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("findByTypeAndDate error", re);
			throw re;
		}
	}
	//批次刪除資料 
	public void deleteByFuncNameAndDate(String funcName,String startDate,String endDate){
		log.debug("");
		try {
			String queryString = "delete from "+CounterDAO.TABLENAME +" where "+
								CounterDAO.FUNC_NAME +"=? and "+CounterDAO.DATE +" between :form and :to";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(CounterDAO.FUNC_NAME,funcName);
			queryObject.setParameter("from",startDate);
			queryObject.setParameter("to",endDate);
			queryObject.executeUpdate();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	
	public boolean deleteByDate(String startDate,String endDate){
		return true;
		
	}
	
	public boolean deleteByTypeAndDate(String type,String startDate,String endDate){
		return true;
		
	}
	public String getTodayString(){

		Calendar calendar=Calendar.getInstance();
		String dateString=calendar.get(Calendar.YEAR)
							+String.format("%02d", calendar.get(Calendar.MONTH)+1)
							+String.format("%02d",calendar.get(Calendar.DATE));
		return dateString;
		
	}


	
	
}
