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
package org.onap.portalapp.portal.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.onap.portalapp.portal.controller.MicroserviceProxyController;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.service.MicroserviceProxyService;
import org.onap.portalapp.portal.service.MicroserviceProxyServiceImpl;
import org.onap.portalapp.util.EPUserUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.databind.ObjectMapper;



public class MicroserviceProxyControllerTest extends MockitoTestSuite {

	@Mock
	MicroserviceProxyService microserviceProxyService = new MicroserviceProxyServiceImpl();

	@InjectMocks
	MicroserviceProxyController microserviceProxyController = new MicroserviceProxyController();

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();

	@Mock
	EPUserUtils ePUserUtils = new EPUserUtils();
	@Mock
	ObjectMapper objectMapper = new ObjectMapper();
	MockEPUser mockUser = new MockEPUser();

	@Test
	public void getMicroserviceProxyTest() throws Exception {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(microserviceProxyService.proxyToDestination(1, user, mockedRequest)).thenReturn("Success");
		String acutualString = microserviceProxyController.getMicroserviceProxy(mockedRequest, getMockedResponse(), 1);
		assertTrue(acutualString.equals("{\"error\":\"Success\"}"));
	}

	@Test(expected = NullPointerException.class)
	public void getMicroserviceProxyNullPoniterExceptionTest() throws Exception {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(microserviceProxyService.proxyToDestination(1, user, mockedRequest))
				.thenThrow(nullPointerException);
		microserviceProxyController.getMicroserviceProxy(mockedRequest, getMockedResponse(), 1);
	}

	@Test
	public void getMicroserviceProxyExceptionTest() throws Exception {
		HttpClientErrorException httpClientErrorException = new HttpClientErrorException(HttpStatus.OK, "Success");
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(microserviceProxyService.proxyToDestination(1, user, mockedRequest))
				.thenThrow(httpClientErrorException);
		String acutualString = microserviceProxyController.getMicroserviceProxy(mockedRequest, getMockedResponse(), 1);
		assertTrue(acutualString.equals("{\"error\":\"\"}"));
	}

	@Test
	public void getMicroserviceProxyByWidgetIdTest() throws Exception {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(microserviceProxyService.proxyToDestinationByWidgetId(1, user, mockedRequest))
				.thenReturn("Success");
		String acutualString = microserviceProxyController.getMicroserviceProxyByWidgetId(mockedRequest,
				getMockedResponse(), 1);
		assertTrue(acutualString.equals("{\"error\":\"Success\"}"));
	}

	@Test(expected = NullPointerException.class)
	public void getMicroserviceProxyByWidgetIdNullPointerExceptionTest() throws Exception {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(microserviceProxyService.proxyToDestinationByWidgetId(1, user, mockedRequest))
				.thenThrow(nullPointerException);
		microserviceProxyController.getMicroserviceProxyByWidgetId(mockedRequest, getMockedResponse(), 1);
	}

	@Test
	public void getMicroserviceProxyByWidgetIdExceptionTest() throws Exception {
		HttpClientErrorException httpClientErrorException = new HttpClientErrorException(HttpStatus.OK, "Success");
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(microserviceProxyService.proxyToDestinationByWidgetId(1, user, mockedRequest))
				.thenThrow(httpClientErrorException);
		String acutualString = microserviceProxyController.getMicroserviceProxyByWidgetId(mockedRequest,
				getMockedResponse(), 1);
		assertTrue(acutualString.equals("{\"error\":\"\"}"));
		}
}
