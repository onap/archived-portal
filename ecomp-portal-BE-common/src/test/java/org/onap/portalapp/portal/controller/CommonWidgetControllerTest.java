
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
 */package org.onap.portalapp.portal.controller;

import static org.junit.Assert.assertTrue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.controller.CommonWidgetController;
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.service.DashboardSearchService;
import org.onap.portalapp.portal.service.DashboardSearchServiceImpl;
import org.onap.portalapp.portal.transport.CommonWidgetMeta;

public class CommonWidgetControllerTest {

	@Mock
	DashboardSearchService dashboardSearchService = new DashboardSearchServiceImpl();

	@InjectMocks
	CommonWidgetController commonWidgetController = new CommonWidgetController();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();

	@SuppressWarnings("unchecked")
	@Test
	public void getWidgetDataTest() {
		String resourceType = "Test";
		PortalRestResponse<CommonWidgetMeta> acutualPoratlRestResponse = null;
		@SuppressWarnings("rawtypes")
		PortalRestResponse ecpectedPortalRestResponse = new PortalRestResponse();
		ecpectedPortalRestResponse.setMessage("Unexpected resource type Test");
		ecpectedPortalRestResponse.setResponse(null);
		ecpectedPortalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		acutualPoratlRestResponse = commonWidgetController.getWidgetData(mockedRequest, resourceType);
		assertTrue(acutualPoratlRestResponse.equals(ecpectedPortalRestResponse));

	}

	@SuppressWarnings("unchecked")
	@Test
	public void getWidgetDataTestNew() {
		String resourceType = "EVENTS";
		PortalRestResponse<CommonWidgetMeta> acutualPoratlRestResponse = null;
		@SuppressWarnings("rawtypes")
		PortalRestResponse ecpectedPortalRestResponse = new PortalRestResponse();
		ecpectedPortalRestResponse.setMessage("success");
		ecpectedPortalRestResponse.setResponse(null);
		ecpectedPortalRestResponse.setStatus(PortalRestStatusEnum.OK);
		acutualPoratlRestResponse = commonWidgetController.getWidgetData(mockedRequest, resourceType);
		assertTrue(acutualPoratlRestResponse.equals(ecpectedPortalRestResponse));

	}

	@SuppressWarnings("unchecked")
	@Test
	public void getWidgetDataExceptionTest() {
		String resourceType = "null";
		PortalRestResponse<CommonWidgetMeta> acutualPoratlRestResponse = null;
		@SuppressWarnings("rawtypes")
		PortalRestResponse ecpectedPortalRestResponse = new PortalRestResponse();
		ecpectedPortalRestResponse.setMessage("Unexpected resource type null");
		ecpectedPortalRestResponse.setResponse(null);
		ecpectedPortalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		acutualPoratlRestResponse = commonWidgetController.getWidgetData(mockedRequest, resourceType);
		assertTrue(acutualPoratlRestResponse.equals(ecpectedPortalRestResponse));

	}
}
