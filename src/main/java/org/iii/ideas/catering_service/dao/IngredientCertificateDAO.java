package org.iii.ideas.catering_service.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.DateUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class IngredientCertificateDAO{
	private static final Logger log = LoggerFactory.getLogger(IngredientCertificateDAO.class);
	private SessionFactory sessionFactory;
	private Session dbSession;
	
	public IngredientCertificateDAO(){
		this.sessionFactory = HibernateUtil.buildSessionFactory();
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	
	public void setSession(Session session){
		this.dbSession=session;
	}

	protected void initDao() {
		// do nothing
	}
	
	public IngredientCertificate queryIngredientCertificateByCertNo(String certNo,String certType){
		IngredientCertificate ingredientCertificate = null;
		dbSession = sessionFactory.openSession();
		Criteria criteria = dbSession.createCriteria(IngredientCertificate.class);
		criteria.add(Restrictions.eq("certNo", certNo));
		criteria.add(Restrictions.eq("certType", certType));
		
		//@SuppressWarnings("unchecked")
//		System.out.println(certNo + " || " + certType);
		List<IngredientCertificate> schoolList = criteria.list();
		if (schoolList.size() == 0) {
			dbSession.close();
			return null;
		}
		ingredientCertificate = schoolList.get(0);
		
		dbSession.close();
		return ingredientCertificate;
	}
	
	public void updateIngredientCertificate(IngredientCertificate ingredientCertificate){
		dbSession = sessionFactory.openSession();
		Transaction tx = dbSession.beginTransaction();
		try{
			if(ingredientCertificate.getCertId()==null){
				ingredientCertificate.setCertId(0);
				ingredientCertificate.setCreateDate(CateringServiceUtil.getCurrentTimestamp());
				ingredientCertificate.setExpiryDate(DateUtil.transCalendarToTimestamp(DateUtil.getEndOfThisYear()));
			}				
			ingredientCertificate.setUpdateDate(CateringServiceUtil.getCurrentTimestamp());
		    dbSession.save(ingredientCertificate);
		    tx.commit();
		}catch (Exception e){
			log.error("Update Ingredient Certificate error",e);
			e.printStackTrace();
			tx.rollback();
		}finally{
			dbSession.close();
		}

	}

	

}
