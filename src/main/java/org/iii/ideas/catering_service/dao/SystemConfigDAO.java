package org.iii.ideas.catering_service.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class SystemConfigDAO {
	private static final Logger log = LoggerFactory.getLogger(SystemConfigDAO.class);
	private SessionFactory sessionFactory=null;
	private Session dbSession=null;
	public final String CONFIG_ENABLE="1";
	public final String CONFIG_DISABLE="0";
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public void setSession(Session session) {
		this.dbSession = session;
	}

	private Session getCurrentSession() {
	/*
		if (sessionFactory==null){
			openSessionFactory();
			System.out.println("******* no db session(2) ");
		}
	*/	
		if (dbSession==null){
				System.out.println("******* no db session(1) ");
				dbSession=sessionFactory.openSession();
		}
			return dbSession;

	}
	
	public void closeSession(){
		this.dbSession.close();
	}
	public void  openSessionFactory(){
		if (sessionFactory==null){
			System.out.println("******* no db session(3) ");
			sessionFactory=HibernateUtil.buildSessionFactory();
			//dbSession = sessionFactory.openSession();
		}
		if (dbSession==null){
			dbSession = sessionFactory.openSession();
		}
		
	}

	public void save(SystemConfig transientInstance) {
		log.debug("saving SystemConfig instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(SystemConfig persistentInstance) {
		log.debug("deleting SystemConfig instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	
	public String getValue( String ptype,String pname){
		return getValue(ptype,pname,CONFIG_ENABLE);
	}
	
	public String getValue(String ptype,String pname,String penable){
		String hql="From SystemConfig s where s.ptype=:ptype and s.pname=:pname and s.penable=:penable ";
		Query queryObj=getCurrentSession().createQuery(hql);
		queryObj.setParameter("ptype", ptype);
		queryObj.setParameter("pname", pname);
		queryObj.setParameter("penable", penable);
		SystemConfig result=(SystemConfig) queryObj.uniqueResult();
		if (result==null){
			return null;
		}else{
			return result.getPvalue();
		}
	}
	
	public Boolean setValue(String pname,String ptype,String pvalue,String penable){
		String hql="From SystemConfig s where s.ptype=:ptype and s.pname=:pname ";
		try{
			Query queryObj=this.getCurrentSession().createQuery(hql);
			queryObj.setParameter("ptype", ptype);
			queryObj.setParameter("pname", pname);
			SystemConfig result=(SystemConfig) queryObj.uniqueResult();
			if (result==null){ //無則直接新增
				result=new SystemConfig();
				result.setPname(pname);
				result.setPtype(ptype);
			}
			result.setPenable(penable);
			result.setPvalue(pvalue);
	
			this.getCurrentSession().saveOrUpdate(result);
			return true;
		}catch (Exception ex){
			throw ex;
		}
	}
	
	public Boolean setDisable(String pname,String ptype){
		return this.setPenable( pname, ptype,CONFIG_DISABLE);
	}
	
	public Boolean setEnable(String pname,String ptype){
		return this.setPenable( pname, ptype,CONFIG_ENABLE);
	}
	
	private  Boolean setPenable(String pname,String ptype,String penable){
		String hql="from SystemConfig s where s.ptype=:ptype and s.pname=:pname ";
		try{
			Query queryObj=this.getCurrentSession().createQuery(hql);
			queryObj.setParameter("ptype", ptype);
			queryObj.setParameter("pname", pname);
			SystemConfig result=(SystemConfig) queryObj.uniqueResult();
			if (result==null){ 
				return false;
			}
			result.setPenable(penable);
	
			this.getCurrentSession().saveOrUpdate(result);
			return true;
		}catch (Exception ex){
			throw ex;
		}
	}
	
}
