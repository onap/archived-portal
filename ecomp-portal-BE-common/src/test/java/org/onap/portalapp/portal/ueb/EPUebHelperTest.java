/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
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
package org.onap.portalapp.portal.ueb;

import static org.junit.Assert.assertFalse;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.domain.EcompApp;
import org.onap.portalapp.portal.service.EPAppCommonServiceImpl;
import org.onap.portalapp.portal.utils.PortalConstants;
import org.onap.portalsdk.core.onboarding.util.PortalApiConstants;
import org.onap.portalsdk.core.onboarding.util.PortalApiProperties;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.att.nsa.apiClient.http.HttpClient;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PortalApiProperties.class, HttpClient.class, URL.class, PortalConstants.class})
public class EPUebHelperTest {

	@Mock
	EPAppCommonServiceImpl epAppCommonServiceImpl = new EPAppCommonServiceImpl();
	
	@Mock
	SessionFactory sessionFactory;

	@Mock
	Session session;

	@Mock
	Transaction transaction;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		Mockito.when(sessionFactory.openSession()).thenReturn(session);
	}
	
	@InjectMocks
	EPUebHelper epUebHelper = new EPUebHelper();
	
	@Test
	public void refreshPublisherForPortalListTest() {
		List<EcompApp> ecompApps = new ArrayList<>();
		EcompApp ecompApp = new EcompApp();
		ecompApp.setCentralAuth(true);
		ecompApp.setId(1l);
		ecompApp.setName("test");
		ecompApp.setEnabled(true);
		ecompApp.setUebTopicName("ECOMP-PORTAL-INBOX");
		ecompApps.add(ecompApp);
		Mockito.when(epAppCommonServiceImpl.getEcompAppAppsFullList()).thenReturn(ecompApps);
		epUebHelper.refreshPublisherList();
	}
	
	@Test
	public void refreshPublisherForPartnersListTest() {
		PowerMockito.mockStatic(PortalConstants.class);
		PowerMockito.mockStatic(PortalApiProperties.class);
		List<EcompApp> ecompApps = new ArrayList<>();
		EcompApp ecompApp = new EcompApp();
		ecompApp.setCentralAuth(true);
		ecompApp.setId(2l);
		ecompApp.setName("test");
		ecompApp.setEnabled(true);
		ecompApp.setUebTopicName("Test");
		ecompApps.add(ecompApp);
		Mockito.when(epAppCommonServiceImpl.getEcompAppAppsFullList()).thenReturn(ecompApps);
		Mockito.when(PortalApiProperties.getProperty(PortalApiConstants.ECOMP_PORTAL_INBOX_NAME)).thenReturn("ecomp_portal_inbox_name");
		Mockito.when(PortalApiProperties.getProperty(PortalApiConstants.UEB_APP_INBOUND_MAILBOX_NAME)).thenReturn("ueb_app_mailbox_name");
		Mockito.when(PortalApiProperties.getProperty(PortalApiConstants.UEB_APP_KEY)).thenReturn("ueb_app_key");
		Mockito.when(PortalApiProperties.getProperty(PortalApiConstants.UEB_APP_SECRET)).thenReturn("ueb_app_secret");
		Mockito.when(PortalApiProperties.getProperty(PortalApiConstants.UEB_APP_CONSUMER_GROUP_NAME)).thenReturn("ueb_app_consumer_group_name");
		Mockito.when(PortalApiProperties.getProperty(PortalApiConstants.UEB_URL_LIST)).thenReturn("ueb_url_list");
		epUebHelper.refreshPublisherList();
	}
	
	@Test
	public void refreshPublisherForExceptionListTest() {
		List<EcompApp> ecompApps = new ArrayList<>();
		EcompApp ecompApp = new EcompApp();
		ecompApp.setCentralAuth(true);
		ecompApp.setId(2l);
		ecompApp.setName("test");
		ecompApp.setEnabled(true);
		ecompApp.setUebTopicName("Test");
		ecompApps.add(ecompApp);
		Mockito.doThrow(new NullPointerException()).when(epAppCommonServiceImpl).getEcompAppAppsFullList();
		epUebHelper.refreshPublisherList();
	}
	
	@Test
	public void checkAvailabilityConectionRefusedTest() throws Exception {
		PowerMockito.mockStatic(PortalConstants.class);
		PowerMockito.mockStatic(PortalApiProperties.class);
		Mockito.when(PortalApiProperties.getProperty(PortalApiConstants.UEB_URL_LIST)).thenReturn("localhost");
		URL u = PowerMockito.mock(URL.class);
		String url = "http://localhost:3904/topics/null";
		PowerMockito.whenNew(URL.class).withArguments(url).thenReturn(u);
		HttpURLConnection huc = PowerMockito.mock(HttpURLConnection.class);
		PowerMockito.when(u.openConnection()).thenReturn(huc);
		PowerMockito.when(huc.getResponseCode()).thenReturn(200);
		boolean actual = epUebHelper.checkAvailability();
		assertFalse(actual);
	}
}
