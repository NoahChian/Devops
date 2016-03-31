package org.iii.ideas.catering_service.dao;
import java.io.Serializable;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.Assigned;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerationException;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.type.Type;
import org.iii.ideas.catering_service.service.CateringServiceLoader;

public class IGeneratorUUID implements IdentifierGenerator, Configurable{


	private String entityName;

	public Serializable generate(SessionImplementor session, Object obj) throws HibernateException {
		//TODO: cache the persister, this shows up in yourkit
 
		try{
			 return  CateringServiceLoader.getSeqNo();
		}catch(Exception ex){
			return 0;
		}
	}

	public void configure(Type type, Properties params, Dialect d) throws MappingException {
		entityName = params.getProperty(ENTITY_NAME);
		if ( entityName == null ) {
			throw new MappingException("no entity name");
		}
	}
 
	
}
