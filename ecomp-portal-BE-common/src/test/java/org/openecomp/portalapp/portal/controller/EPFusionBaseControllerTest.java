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
package org.openecomp.portalapp.portal.controller;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openecomp.portalapp.controller.EPFusionBaseController;
import org.openecomp.portalapp.portal.core.MockEPUser;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.framework.MockitoTestSuite;
import org.openecomp.portalapp.portal.service.DashboardSearchService;
import org.openecomp.portalapp.portal.service.DashboardSearchServiceImpl;
import org.openecomp.portalapp.portal.utils.EPCommonSystemProperties;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalapp.util.EPUserUtils;
import org.openecomp.portalsdk.core.domain.MenuData;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EPUserUtils.class, SystemProperties.class, EPCommonSystemProperties.class, EcompPortalUtils.class})
public class EPFusionBaseControllerTest {

	@Mock
	DashboardSearchService searchService = new DashboardSearchServiceImpl();
	
	@InjectMocks
	EPFusionBaseController epFusionBaseController = new EPFusionBaseController() {
	};


	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();

	NullPointerException nullPointerException = new NullPointerException();
	
	MockEPUser mockUser = new MockEPUser();
	
	@Test
	public void messagesExceptionTest(){
		Map<String, Object> expectedData = new HashMap<String, Object>();
		Map<String, Object> actualData = null;
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.APPLICATION_MENU_ATTRIBUTE_NAME)).thenReturn("test"); 
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Set<MenuData> menuResult = null;
		HttpSession  session = mockedRequest.getSession();
		Mockito.when(session
				.getAttribute(SystemProperties.getProperty(SystemProperties.APPLICATION_MENU_ATTRIBUTE_NAME))).thenReturn(menuResult);
		actualData = epFusionBaseController.messages(mockedRequest);
		assertEquals(expectedData,actualData );
		System.out.println();
		
	}
	
	@Test
	public void messagesTest(){
		Map<String, Object> expectedData = new HashMap<String, Object>();
		Map<String, Object> actualData = null;
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);

		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.APPLICATION_MENU_ATTRIBUTE_NAME)).thenReturn("test"); 
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Set<MenuData> menuResult = new HashSet<>();
		MenuData menuData= new MenuData();
		menuResult.add(menuData);
		menuData.setChildMenus(menuResult);
		HttpSession  session = mockedRequest.getSession();
		Mockito.when(session
				.getAttribute(SystemProperties.getProperty(SystemProperties.APPLICATION_MENU_ATTRIBUTE_NAME))).thenReturn(menuResult);
		actualData = epFusionBaseController.messages(mockedRequest);
		assertEquals(actualData.size(), 2);
	}
	
	@Test
	public void isAccessibleTest()
	{
		assertTrue(epFusionBaseController.isAccessible());
	}
	@Test
	public void isRESTfulCallTest()
	{
		assertTrue(epFusionBaseController.isRESTfulCall());
	}
}
