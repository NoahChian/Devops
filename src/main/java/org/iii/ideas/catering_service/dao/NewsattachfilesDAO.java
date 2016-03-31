package org.iii.ideas.catering_service.dao;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.iii.ideas.catering_service.rest.bo.FileNewsBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class NewsattachfilesDAO {
	private static final Logger log = LoggerFactory.getLogger(NewsattachfilesDAO.class);
	// property constants

	private Session dbSession;

	public NewsattachfilesDAO() {
	};

	public NewsattachfilesDAO(Session dbSession) {
		setDbSession(dbSession);
	}

	public Session getDbSession() {
		return dbSession;
	}

	public void setDbSession(Session dbSession) {
		this.dbSession = dbSession;
	}
	
	public List<Newsattachfiles> getNewsattachfilesList(Integer newsId) {
		String hql = "FROM Newsattachfiles WHERE newsId = :newsId ORDER BY id DESC";
		Query query = dbSession.createQuery(hql);
		query.setParameter("newsId", newsId);
		if (query.list() != null && query.list().size() > 0)
			return query.list();
		else
			return null;
	}
	
//	public Newsattachfiles getSingleNewsattachfiles(String targetId) {
//		String hql = "FROM Newsattachfiles WHERE targetId = :targetId ORDER BY createTime DESC";
//		Query query = dbSession.createQuery(hql);
//		query.setParameter("targetId", targetId);
//		query.setMaxResults(1);
//		if (query.list() != null && query.list().size() > 0)
//			return (Newsattachfiles) query.list().get(0);
//		else
//			return null;
//	}

	public Newsattachfiles saveNewsattachfiles(FileNewsBO fileNewsBo) {
		Newsattachfiles uf = new Newsattachfiles();
//		uf.setId(fileNewsBo.getId());
		uf.setNewsId(fileNewsBo.getNewsId());
		uf.setName(fileNewsBo.getName());
		uf.setFileDesc(fileNewsBo.getFileDesc());
		uf.setFilePath(fileNewsBo.getFilePath());
		uf.setFileSize(fileNewsBo.getFileSize());
		uf.setCreateTime(Calendar.getInstance().getTime());
		uf.setUpdateTime(Calendar.getInstance().getTime());
		dbSession.save(uf);
		return uf;
	}
	
	public Newsattachfiles getNewsattachfiles(Integer fileId) {
		String hql = "FROM Newsattachfiles WHERE id = :id";
		Query query = dbSession.createQuery(hql);
		query.setParameter("id", fileId);
		if (query.list() != null && query.list().size() > 0)
			return (Newsattachfiles)query.list().get(0);
		else
			return null;
	}

	public int deleteMutilNewsattachfiles(List<String> attachIdList) {
		String hql = "DELETE FROM Newsattachfiles WHERE id IN :id ";
		Query query = dbSession.createQuery(hql);
		query.setParameterList("id", attachIdList);
		return query.executeUpdate();
	}
	
//	public Newsattachfiles copyInspectReport(Long sourceId,Long targetId){
//		Newsattachfiles uf = new Newsattachfiles();
//		uf = getSingleNewsattachfiles(SourceTypeCode.INGREDIENT_INSPECTION, sourceId.toString());
//		if(uf == null)
//			return uf;
//		dbSession.evict(uf);
//		uf.setTargetId(targetId.toString());
//		dbSession.save(uf);
//		return uf;
//	}
}