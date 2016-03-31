package org.iii.ideas.catering_service.dao;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.iii.ideas.catering_service.code.SourceTypeCode;
import org.iii.ideas.catering_service.rest.bo.FileBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class UploadFileDAO {
	private static final Logger log = LoggerFactory.getLogger(UploadFileDAO.class);
	// property constants

	private Session dbSession;

	public UploadFileDAO() {
	};

	public UploadFileDAO(Session dbSession) {
		setDbSession(dbSession);
	}

	public Session getDbSession() {
		return dbSession;
	}

	public void setDbSession(Session dbSession) {
		this.dbSession = dbSession;
	}

	public Uploadfile getSingleUploadfile(String sourceType, String targetId) {
		String hql = "FROM Uploadfile WHERE targetId = :targetId AND sourceType = :sourceType ORDER BY createTime DESC";
		Query query = dbSession.createQuery(hql);
		query.setParameter("targetId", targetId);
		query.setParameter("sourceType", sourceType);
		query.setMaxResults(1);
		if (query.list() != null && query.list().size() > 0)
			return (Uploadfile) query.list().get(0);
		else
			return null;
	}

	public Uploadfile saveUploadFile(FileBO fileBo) {
		Uploadfile uf = new Uploadfile();
		uf.setEncodeFileName(fileBo.getEncodeFileName());
		uf.setMimeType(fileBo.getMimeType());
		uf.setExtType(fileBo.getExtType());
		uf.setFilePath(fileBo.getFilePath());
		uf.setTargetId(fileBo.getTargetId());
		uf.setSourceType(fileBo.getSourceType());
		uf.setCreateTime(Calendar.getInstance().getTime());
		uf.setUpdateTime(Calendar.getInstance().getTime());
		uf.setOriginalFileName(fileBo.getOriginalFileName());
		dbSession.save(uf);
		return uf;
	}

	public int deleteMutilUploadFile(String sourceType, List<String> targetIdList) {
		String hql = "DELETE FROM Uploadfile WHERE sourceType = :sourceType AND targetId IN :targetId ";
		Query query = dbSession.createQuery(hql);
		query.setParameter("sourceType", sourceType);
		query.setParameterList("targetId", targetIdList);
		return query.executeUpdate();
	}
	
	public Uploadfile copyInspectReport(Long sourceId,Long targetId){
		Uploadfile uf = new Uploadfile();
		uf = getSingleUploadfile(SourceTypeCode.INGREDIENT_INSPECTION, sourceId.toString());
		if(uf == null)
			return uf;
		dbSession.evict(uf);
		uf.setTargetId(targetId.toString());
		dbSession.save(uf);
		return uf;
	}
}