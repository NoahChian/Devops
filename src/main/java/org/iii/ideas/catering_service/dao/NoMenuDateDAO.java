package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class NoMenuDateDAO extends AbstractNoMenuDate implements java.io.Serializable {
	
	private Long nmDateId;
	private String schoolId;
	private Timestamp startdate;
	private Timestamp enddate;
	private Long menuType;
	private Long nmdescId;
	private String note;
	private String createUser;
	private Timestamp createDateTime;
	private String updateUser;
	private Timestamp updateDateTime;
	
	public NoMenuDateDAO(){
	}
	public NoMenuDateDAO(Session dbSession){
		this.dbSession = dbSession;
	}
	
	public NoMenuDateDAO(Long nmDateId, String schoolId,
			Timestamp startdate, Timestamp enddate,Integer menuType,
			Integer nmdescId,String note,String createUser,Timestamp createDateTime,
			String updateUser,Timestamp updateDateTime){
		super(nmDateId, schoolId,startdate, enddate,menuType, nmdescId, note, createUser, createDateTime,
				 updateUser, updateDateTime);
	}
	
	private SessionFactory sessionFactory;
	private Session dbSession;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session getCurrentSession() {
		try{
			return sessionFactory.getCurrentSession();		
		}catch(Exception ex){
			return sessionFactory.openSession();
		}

	}
	
	public List<NoMenuDate> queryNoMenuDateBySchoolId(Integer schoolId){
//		NoMenuDate nm = new NoMenuDate();
		List<NoMenuDate> nm = null;
		String hql ="FROM NoMenuDate where schoolId=:schoolId ";
		Query queryObj= dbSession.createQuery(hql);
		queryObj.setParameter("schoolId", schoolId);
		nm = queryObj.list();
		return nm ;
	}
	/**
	 * 透過kid取得供餐對象，並撈出當天有登錄不供餐之學校
	 * @param kid
	 * @return
	 */
	public StringBuilder queryNoMenuDateByKid(Integer kid,String Date){
		//取得廚房供餐學校
		SchoolkitchenDAO skDAO = new SchoolkitchenDAO(dbSession);
		List<Integer> offerschoollist = skDAO.querySchoolListByKitchen(kid);
		SchoolDAO sDAO = new SchoolDAO(dbSession);
		
		
		//建立搜尋schoolid字串
		StringBuilder queryschoolid  = new StringBuilder();
		Iterator<Integer> iter = offerschoollist.iterator();
		while(iter.hasNext())
		{
			queryschoolid.append(iter.next());
			if(iter.hasNext()){
	        	queryschoolid.append(",");
			}
		}
//	    Date a = new Date(Date);
//		String hql ="SELECT schoolId FROM NoMenuDate where schoolId in (:schoolId) "
//				+ " AND startdate <= :date and enddate >= :date ";
		String hql ="SELECT c.name , s.schoolName FROM NoMenuDate nm , Code c , School s "
				+ " where nm.nmdescId = c.code AND s.schoolId = nm.schoolId AND c.type = 'NoMenuType' "
				+ " AND nm.schoolId in ("+ queryschoolid.toString() +") " 
				+ " AND :date between nm.startdate and nm.enddate ";
		Query queryObj= dbSession.createQuery(hql);
//		queryObj.setParameter("schoolId", queryschoolid.toString()); //因hibernate 使用in語法與一般結果有異，更改為直接從SQL塞入字元。 modify by Ellis 20150818
		queryObj.setParameter("date", new Date(Date));

		StringBuilder totalschoolname  = new StringBuilder();
		List<String> nm_school = queryObj.list();
		if(nm_school.size() == 0 ){
			totalschoolname.setLength(0);
		}else{
			Iterator iter_schoolname = nm_school.iterator();
			while(iter_schoolname.hasNext())
			{
				Object[] obj = (Object[]) iter_schoolname.next();
				totalschoolname.append("["+obj[0]+"]"+obj[1]);
				if(iter_schoolname.hasNext()){
					totalschoolname.append(",");
				}
			}
		}
		return totalschoolname ;
	}
	
	//20151117 chu query by sid
		public StringBuilder queryNoMenuDateBySid(String sid, String Date){
			String hql ="SELECT c.name , s.schoolName FROM NoMenuDate nm , Code c , School s "
					+ " where nm.nmdescId = c.code AND s.schoolId = nm.schoolId AND c.type = 'NoMenuType' "
					+ " AND nm.schoolId =:sid " 
					+ " AND :date between nm.startdate and nm.enddate ";
			Query queryObj= dbSession.createQuery(hql);
			queryObj.setParameter("date", new Date(Date));
			queryObj.setParameter("sid", sid);
			
			StringBuilder totalschoolname  = new StringBuilder();
			List<String> nm_school = queryObj.list();
			if(nm_school.size() == 0 ){
				totalschoolname.setLength(0);
			}else{
				Iterator iter_schoolname = nm_school.iterator();
				while(iter_schoolname.hasNext())
				{
					Object[] obj = (Object[]) iter_schoolname.next();
					totalschoolname.append("["+obj[0]+"]"+obj[1]);
					if(iter_schoolname.hasNext()){
						totalschoolname.append(",");
					}
				}
			}
			return totalschoolname ;

		}
}