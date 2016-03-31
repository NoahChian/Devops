package org.iii.ideas.catering_service.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.iii.ideas.catering_service.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class DishBatchDataDAO {
	private static final Logger log = LoggerFactory
			.getLogger(CodeDAO.class);
	
	private static final String DISH_BATCH_DATA_ID="DishBatchDataId";
	private static final String BATCH_DATA_ID="BatchDataId";
	private static final String DISH_NAME="DishName";
	private static final String DISH_TYPE="DishType";
	private static final String DISH_ID="DishId";
	private static final String UPDATE_DATE_TIME="UpdateDateTime";
	private static final String DISH_SHOWNAME="DishShowName";
	private static final String TABLENAME="dishbatchdata";
	
	
	
	private SessionFactory sessionFactory;
	private Session dbSession;
	
	public void  openSessionFactory(){
		this.sessionFactory=HibernateUtil.buildSessionFactory();
		this.dbSession = sessionFactory.openSession();
	}	
	
	public void closeSession(){
		this.dbSession.close();
	}
		
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setSession(Session session){
		this.dbSession=session;
	}
	private Session getCurrentSession() {
		if (this.dbSession==null){
			return sessionFactory.getCurrentSession();
		}else{
			return this.dbSession;
		}
	}
	
	
	public void save(DishBatchData transientInstance) {
		log.debug("saving Counter instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(DishBatchData persistentInstance) {
		log.debug("deleting dishbatchdata instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	
	public void attachDirty(DishBatchData instance) {
		log.debug("attaching dirty dishbatchdata instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		//	instance.getDishId()
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List<DishBatchData> getDishBatchDataByBatchId(Long batchdataId) {
		String HQL="from DishBatchData where BatchDataId=:batchdataId";
		Query queryObj=this.getCurrentSession().createQuery(HQL);
		queryObj.setParameter("batchdataId", batchdataId);
		List<DishBatchData> result= queryObj.list();

		return result;
	}
	
	public List<DishBatchData> getDishBatchDataWithTypenameByBatchId(Long mid) {
		String HQL="from DishBatchData d,Code c "
				+ " where d.DishType=c.code and d.BatchDataId=:batchdataId "
				+ " and c.type=:type and d.DishType <> 'Seasoning' order by c.sort asc "; 				
		Query queryObj=this.getCurrentSession().createQuery(HQL);
		queryObj.setParameter("batchdataId", mid);
		queryObj.setParameter("type", CateringServiceCode.CODETYPE_DISHTYPE2); //20150505 shine mod 新的菜單儲存方式,要改用新的Type
		List<Object[]> result= queryObj.list();
		if(result.size()==0){  //20150505 shine add 如果沒資料,則改用舊的條件找看看
			queryObj.setParameter("type", CateringServiceCode.CODETYPE_DISHTYPE);
			result= queryObj.list();
		}
		List<DishBatchData> dishResult=new ArrayList<DishBatchData>();
		
		Iterator<Object[]> ir=result.iterator();
		while(ir.hasNext()){
			Object[] row=ir.next();
			DishBatchData rowdish=(DishBatchData)row[0];
			Code rowcode=(Code)row[1];
			rowdish.setDishType(rowcode.getName());
			dishResult.add(rowdish);
		}
		return dishResult;
	}
	
	public List<String> getDishNameListByBatchdataId(Integer batchdataId){
		String hql="select dishName from DishBatchData where BatchDataId=:batchdataId";
		try{
			Query queryObj=this.getCurrentSession().createQuery(hql);
			queryObj.setParameter("batchdataId", batchdataId);
			return (List <String>) queryObj.list();
		}catch (Exception ex){
			throw ex;
		}
	} 
	
	public void deleteDishBatchDataByBatchdataId(Long batchdataId){
		try{
			String hql="delete from DishBatchData where BatchDataId=:batchdataId";
			Query queryObj=this.getCurrentSession().createQuery(hql);
			queryObj.setParameter("batchdataId", batchdataId);
			queryObj.executeUpdate();
		}catch(Exception ex){		
			throw ex;
		}
	}
	//取得指定菜單的菜色
	public DishBatchData getSpecifiedDish(Long batchdataId, String dishType){
		try{
			String hql="from DishBatchData where BatchDataId=:batchdataId "
					+ " and DishType=:dishType ";
			Query queryObj=this.getCurrentSession().createQuery(hql);
			queryObj.setParameter("batchdataId", batchdataId);
			queryObj.setParameter("dishType", dishType);
			queryObj.setMaxResults(1);
			List result=queryObj.list();
			if (result.size()==0){
				return null;
			}else{
				return (DishBatchData) result.get(0);
			}
			
		}catch (Exception ex){
			throw ex;
		}
	}
	
	public DishBatchData getDishbatchdataByDishbatchdataId(Integer dishbatchdataId){
		String HQL="from DishBatchData where DishBatchDataId=:dishbatchdataId";
		try{

			Query queryObj=this.getCurrentSession().createQuery(HQL);
			queryObj.setParameter("dishbatchdataId", dishbatchdataId);
			queryObj.setMaxResults(1);
			DishBatchData result=(DishBatchData) queryObj.uniqueResult();
			return result;
		}catch (Exception ex){
			return null;
		}
	}
	
	public void deleteDishbatchdataNotInDishbatchdataIdList(Long batchdataId,List<Long> dishIdList){
		try{
			if(dishIdList.size()>0){
				//清楚非必要之dishId
				String HQL = "delete from DishBatchData where DishBatchDataId not in :dishIdList and batchDataId=:batchDataId";
				Query query = this.getCurrentSession().createQuery(HQL);
				query.setParameterList("dishIdList", dishIdList);
				query.setParameter("batchDataId", batchdataId);
				query.executeUpdate();
			}
		}catch(Exception ex){
			throw ex;
		}
	}
	
	/*
	//找出該廚房是否有有用過指定的菜色  dishId  20140328 KC
	
	public boolean isDishIdExistInBatchdata(Long dishId,Integer kitchenId){
		try{
			
			
		}catch (Exception ex){
			throw ex;
		}
		
		return false;
	}
	*/
	
	/*
	public void saveWithBatch(Object resultSet){
		if ("HashMap".equals(resultSet.getClass().getName())){
			HashMap<Object,DishBatchData> result=(HashMap<Object,DishBatchData>) resultSet;
			for(Entry<Object,DishBatchData> entry:result.entrySet()){
				this.getCurrentSession().save(entry.getValue());
				
			}
		}else if ("List".equals(resultSet.getClass().getName())){
			List<DishBatchData> result =(List<DishBatchData>) resultSet;
			
		}
		
	}
	*/
	
	
}
