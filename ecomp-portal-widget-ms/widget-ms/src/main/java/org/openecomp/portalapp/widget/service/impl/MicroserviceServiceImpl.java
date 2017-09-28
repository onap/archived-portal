/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright © 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the “License”);
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Unless otherwise specified, all documentation contained herein is licensed
 * under the Creative Commons License, Attribution 4.0 Intl. (the “License”);
 * you may not use this documentation except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             https://creativecommons.org/licenses/by/4.0/
 *
 * Unless required by applicable law or agreed to in writing, documentation
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ============LICENSE_END============================================
 *
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
package org.openecomp.portalapp.widget.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.openecomp.portalapp.widget.domain.MicroserviceData;
import org.openecomp.portalapp.widget.domain.MicroserviceParameter;
import org.openecomp.portalapp.widget.service.MicroserviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;

/**
 * TODO: moved all microservice-related code (domain, controller, service)
 * from ecomp portal Backend to widget microservice
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
