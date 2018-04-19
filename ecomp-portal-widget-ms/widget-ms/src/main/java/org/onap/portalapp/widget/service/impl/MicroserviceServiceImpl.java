package org.onap.portalapp.widget.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.onap.portalapp.widget.domain.MicroserviceData;
import org.onap.portalapp.widget.domain.MicroserviceParameter;
import org.onap.portalapp.widget.service.MicroserviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;

/**
 * TODO: moved all microservice-related code (domain, controller, service)
 * from onap portal Backend to widget microservice
 */
@Service("microserviceService")
@Transactional
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
public class MicroserviceServiceImpl implements MicroserviceService{

	private static final Logger logger = LoggerFactory.getLogger(MicroserviceServiceImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public Long saveMicroserivce(MicroserviceData newService) {
		try{
			logger.debug("MicroserviceServiceImpl.saveMicroserivce: microservice={}", newService);
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();		
			session.save(newService);
			tx.commit();
			session.flush();
			session.close();
		}
		catch(Exception e){
			logger.error("Exception occurred while performing MicroserviceServiceImpl.saveMicroserivce in widget microservices. Details:" + e.getMessage());
		}
		return newService.getId();
	}

	@Override
	public void saveMicroserviceParameter(MicroserviceParameter newParameter) {
		try{
			logger.debug("MicroserviceServiceImpl.saveMicroserviceData: microservice={}", newParameter);
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();		
			session.save(newParameter);
			tx.commit();
			session.flush();
			session.close();
		}
		catch(Exception e){
			logger.error("Exception occurred while performing MicroserviceServiceImpl.saveMicroserviceData in widget microservices. Details:" + e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Long getMicroserviceIdByName(String newServiceName) {
		
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(MicroserviceData.class)
				.add(Restrictions.eq("name", newServiceName))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		List<MicroserviceData> services = criteria.list();
		logger.debug("MicroserviceServiceImpl.getMicroserviceByName: result={}", services);
		session.flush();
		session.close();
		
		return (services.size() > 0) ? services.get(0).getId() : null;
	}
	
}
