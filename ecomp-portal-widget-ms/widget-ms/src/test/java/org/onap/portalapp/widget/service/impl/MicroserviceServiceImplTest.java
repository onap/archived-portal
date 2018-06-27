/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2018 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the "License");
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
 * under the Creative Commons License, Attribution 4.0 Intl. (the "License");
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
 * 
 */
package org.onap.portalapp.widget.service.impl;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.widget.domain.MicroserviceData;
import org.onap.portalapp.widget.domain.MicroserviceParameter;

public class MicroserviceServiceImplTest {
	
	private static final String TEST="test";
	@InjectMocks
	MicroserviceServiceImpl microserviceServiceImpl;
	
	@Mock
	Session session;
	@Mock
	SessionFactory sessionFactory;
	@Mock
	Transaction transaction;
	@Mock
	Criteria criteria;
	
	@Before
	public void init() {
	    MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testSaveMicroserivce() {
		MicroserviceData microserviceData=buildData();
		when(sessionFactory.openSession()).thenReturn(session);
		when(session.beginTransaction()).thenReturn(transaction);
	Long data=	microserviceServiceImpl.saveMicroserivce(microserviceData);
		assertEquals(1l, data.longValue());
		
	}
	
	@Test
	public void testSaveMicroserivceException() {
		MicroserviceData microserviceData=buildData();
		when(sessionFactory.openSession()).thenReturn(session);
	
	Long data=	microserviceServiceImpl.saveMicroserivce(microserviceData);
	assertEquals(1l, data.longValue());
		
	}
	
	@Test
	public void testSaveMicroserviceParameterException() {
		when(sessionFactory.openSession()).thenReturn(session);
		microserviceServiceImpl.saveMicroserviceParameter(new MicroserviceParameter());
		
	}
	
	@Test(expected=NullPointerException.class)
	public void testSaveMicroserviceParameter() {
		when(sessionFactory.openSession()).thenReturn(session);
		when(session.beginTransaction()).thenReturn(transaction);
		microserviceServiceImpl.saveMicroserviceParameter(new MicroserviceParameter());
		
		when(session.createCriteria(MicroserviceData.class)).thenReturn(criteria);
		when(criteria.add(Restrictions.eq("name", TEST))).thenReturn(criteria);
		when(criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)).thenReturn(criteria);
		microserviceServiceImpl.getMicroserviceIdByName(TEST);
			
	}
	
	
	
	public MicroserviceData buildData() {
		MicroserviceData microserviceData=new MicroserviceData();
		microserviceData.setId((long)1);
		microserviceData.setName(TEST);
		microserviceData.setActive(TEST);
		microserviceData.setDesc(TEST);
		microserviceData.setAppId((long)1);
		microserviceData.setUrl(TEST);
		microserviceData.setSecurityType(TEST);
		microserviceData.setUsername(TEST);
		microserviceData.setPassword(TEST);
		
		return microserviceData;
	}

}
