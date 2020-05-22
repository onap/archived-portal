/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017-2018 AT&T Intellectual Property. All rights reserved.
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
package org.onap.portalapp.service.sessionmgt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.service.AppsCacheService;
import org.onap.portalapp.portal.service.AppsCacheServiceImple;
import org.onap.portalapp.portal.transport.OnboardingApp;
import org.onap.portalsdk.core.onboarding.util.CipherUtil;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({URL.class, HttpURLConnection.class,CipherUtil.class})
public class SessionCommunicationTest {
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@InjectMocks
	SessionCommunication sessionCommunication = new SessionCommunication();
	
	@Mock
	AppsCacheService appsCacheService = new AppsCacheServiceImple();
	
	@Test
	public void sendGetConnectionRefusedTest() throws Exception {
		OnboardingApp app = new OnboardingApp();
		app.setRestrictedApp(false);
		app.setUebKey("test");
		app.setUebSecret("test");
		app.setUebTopicName("test");
		app.setRolesInAAF(true);
		app.setIsEnabled(true);
		app.setIsOpen(false);
		app.setAppName("test");
		app.setRestUrl("http://localhost:1234");
		app.setAppBasicAuthUsername("test");
		app.setAppBasicAuthPassword("xyz");
		URL u = PowerMockito.mock(URL.class);
		HttpURLConnection huc = PowerMockito.mock(HttpURLConnection.class);
		String url = "http://localhost:1234/sessionTimeOuts";
		PowerMockito.whenNew(URL.class).withArguments(url).thenReturn(u);
		PowerMockito.whenNew(HttpURLConnection.class).withAnyArguments().thenReturn(huc);
		PowerMockito.when(huc.getResponseCode()).thenReturn(200);
		String actual = sessionCommunication.sendGet(app);
		assertEquals("", actual);
	}
	
	@Test
	public void sendGetConnectionRefusedTest1() throws Exception {
		OnboardingApp app = new OnboardingApp();
		app.setRestrictedApp(false);
		app.setUebKey("test");
		app.setUebSecret("test");
		app.setUebTopicName("test");
		app.setRolesInAAF(true);
		app.setIsEnabled(true);
		app.setIsOpen(false);
		app.setAppName("test");
		app.setRestUrl("http://localhost:1234");
		app.setAppBasicAuthUsername("test");
		app.setAppBasicAuthPassword("");
		EPApp epApp = new EPApp();
		epApp.setAppBasicAuthUsername("test");
		epApp.setAppBasicAuthPassword("xyz1234");
		PowerMockito.mockStatic(CipherUtil.class);
		PowerMockito.when(CipherUtil.decryptPKC(Matchers.anyString(),Matchers.anyString())).thenReturn("test");
		Mockito.when(appsCacheService.getApp(1L)).thenReturn(epApp);
		URL u = PowerMockito.mock(URL.class);
		HttpURLConnection huc = PowerMockito.mock(HttpURLConnection.class);
		String url = "http://localhost:1234/sessionTimeOuts";
		PowerMockito.whenNew(URL.class).withArguments(url).thenReturn(u);
		PowerMockito.whenNew(HttpURLConnection.class).withAnyArguments().thenReturn(huc);
		PowerMockito.when(huc.getResponseCode()).thenReturn(200);
		String actual = sessionCommunication.sendGet(app);
		assertEquals("", actual);
	}
	
	@Test
	public void pingSessionConnectionRefusedTest() throws Exception {
		OnboardingApp app = new OnboardingApp();
		app.setRestrictedApp(false);
		app.setUebKey("test");
		app.setUebSecret("test");
		app.setUebTopicName("test");
		app.setRolesInAAF(true);
		app.setIsEnabled(true);
		app.setIsOpen(false);
		app.setAppName("test");
		app.setRestUrl("http://localhost:1234");
		app.setAppBasicAuthUsername("test");
		app.setAppBasicAuthPassword("xyz");
		URL u = PowerMockito.mock(URL.class);
		HttpURLConnection huc = PowerMockito.mock(HttpURLConnection.class);
		String url = "http://localhost:1234/sessionTimeOuts";
		PowerMockito.whenNew(URL.class).withArguments(url).thenReturn(u);
		PowerMockito.whenNew(HttpURLConnection.class).withAnyArguments().thenReturn(huc);
		PowerMockito.when(huc.getResponseCode()).thenReturn(200);
		Boolean actual = sessionCommunication.pingSession(app, "test");
		assertTrue(actual);
	}
	
	
	@Test
	public void timeoutSessionConnectionRefusedTest() throws Exception {
		OnboardingApp app = new OnboardingApp();
		app.setRestrictedApp(false);
		app.setUebKey("test");
		app.setUebSecret("test");
		app.setUebTopicName("test");
		app.setRolesInAAF(true);
		app.setIsEnabled(true);
		app.setIsOpen(false);
		app.setAppName("test");
		app.setRestUrl("http://localhost:1234");
		app.setAppBasicAuthUsername("test");
		app.setAppBasicAuthPassword("xyz");
		URL u = PowerMockito.mock(URL.class);
		HttpURLConnection huc = PowerMockito.mock(HttpURLConnection.class);
		String url = "http://localhost:1234/sessionTimeOuts";
		PowerMockito.whenNew(URL.class).withArguments(url).thenReturn(u);
		PowerMockito.whenNew(HttpURLConnection.class).withAnyArguments().thenReturn(huc);
		PowerMockito.when(huc.getResponseCode()).thenReturn(200);
		Boolean actual = sessionCommunication.timeoutSession(app, "test");
		assertTrue(actual);
	}
	
	@Test
	public void clear() {
		
		sessionCommunication.clear(true);
		
	}
}