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

package org.onap.portalapp.portal.controller;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.portalsdk.core.onboarding.util.CipherUtil;
import org.onap.portalsdk.core.service.DataAccessService;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ CipherUtil.class})
public class EncryptAdminControllerTest {

	@InjectMocks
	EncryptAdminController encryptAdminController=new EncryptAdminController();
	
	@Mock
	HttpServletRequest request;
	@Mock
	HttpServletResponse response;
	@Mock
	Session session;
	@Mock
	SessionFactory sessionFactory;
	@Mock
	Transaction transaction;
	@Mock
	SQLQuery query;
	@Mock
	SQLQuery basicQuery;
	
	@Mock
	SQLQuery microQuery;
	@Mock
	DataAccessService dataAccessService;
	
	@Before
	public void init() {
	    MockitoAnnotations.initMocks(this);
	    PowerMockito.mockStatic(CipherUtil.class);
	}
	
	@Test
	public void testExecuteEncrypt()throws Exception {
		List appPassword=new ArrayList<>();
		Object[] array= new Object[2];
		array[0]=new Long(3l);
		array[1]="test";
		appPassword.add(array);
	
		when(sessionFactory.openSession()).thenReturn(session);
		when(dataAccessService.executeNamedQuery("getAppPassword", null, null)).thenReturn(appPassword);
		when(CipherUtil.decrypt("test")).thenReturn("testDecrypt");
		when(CipherUtil.encrypt("testDecrypt")).thenReturn("test");
		when(session.getTransaction()).thenReturn(transaction);
		when(session.createSQLQuery("UPDATE fn_app m SET m.app_password= :pass " + " where m.app_id = :app_id")).thenReturn(query);
		when(dataAccessService.executeNamedQuery("getBasicauthAccount", null, null)).thenReturn(appPassword);
		when(dataAccessService.executeNamedQuery("getMicroserviceInfo", null, null)).thenReturn(appPassword);
		when(session.createSQLQuery("UPDATE ep_basic_auth_account m SET m.password= :pass" + " where m.id  = :app_id")).thenReturn(basicQuery);
		when(session.createSQLQuery("UPDATE ep_microservice m SET m.password= :pass" + " where m.id  = :app_id")).thenReturn(microQuery);
		
		encryptAdminController.executeEncrypt(request, response);
		
	}
	
	
}
