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
 */
package org.onap.portalapp.portal.controller;

import static org.junit.Assert.assertNull;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.transport.OnboardingApp;

public class AppsControllerExternalVersionRequestTest {

	@InjectMocks
	AppsControllerExternalVersionRequest appsControllerExternalVersionRequest = new AppsControllerExternalVersionRequest();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Mock
	AppsControllerExternalRequest appsControllerExternalRequest;

	MockEPUser mockUser = new MockEPUser();
	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();

	@Test
	public void postPortalAdminTest() throws Exception {
		EPUser user = new EPUser();
		Mockito.when(appsControllerExternalRequest.postPortalAdmin(mockedRequest, mockedResponse, user))
				.thenReturn(null);
		assertNull(appsControllerExternalVersionRequest.postPortalAdmin(mockedRequest, mockedResponse, user));
	}

	@Test
	public void getOnboardAppExternalTest() throws Exception {
		Mockito.when(appsControllerExternalRequest.getOnboardAppExternal(mockedRequest, mockedResponse, (long) 1))
				.thenReturn(null);
		assertNull(appsControllerExternalVersionRequest.getOnboardAppExternal(mockedRequest, mockedResponse, (long) 1));
	}

	@Test
	public void postOnboardAppExternalTest() throws Exception {
		OnboardingApp newOnboardApp = new OnboardingApp();
		Mockito.when(appsControllerExternalRequest.postOnboardAppExternal(mockedRequest, mockedResponse, newOnboardApp))
				.thenReturn(null);
		assertNull(appsControllerExternalVersionRequest.postOnboardAppExternal(mockedRequest, mockedResponse,
				newOnboardApp));
	}

	@Test
	public void putOnboardAppExternalTest() throws Exception {
		OnboardingApp newOnboardApp = new OnboardingApp();
		Mockito.when(appsControllerExternalRequest.putOnboardAppExternal(mockedRequest, mockedResponse, (long) 1,
				newOnboardApp)).thenReturn(null);
		assertNull(appsControllerExternalVersionRequest.putOnboardAppExternal(mockedRequest, mockedResponse, (long) 1,
				newOnboardApp));
	}

}
