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

package org.onap.portalapp.uebhandler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import org.onap.portalapp.portal.service.AdminRolesService;
import org.onap.portalapp.portal.service.FunctionalMenuService;
import org.onap.portalapp.portal.service.SearchService;
import org.onap.portalapp.portal.transport.FunctionalMenuItem;
import org.onap.portalsdk.core.onboarding.ueb.UebMsg;

public class FunctionalMenuHandlerTest {

	@InjectMocks
	FunctionalMenuHandler functionalMenuHandler = new FunctionalMenuHandler();
	
	@Mock
	private AdminRolesService adminRolesService;
	
	@Mock
	private FunctionalMenuService functionalMenuService;

	@Mock
	private SearchService searchSvc;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();

	NullPointerException nullPointerException = new NullPointerException();
	
	MockEPUser mockUser = new MockEPUser();
	
	//@Ignore
	@Test
	public void getFunctionalMenuTest() throws IOException {
		
		UebMsg uebMsg=new UebMsg();
		uebMsg.putMsgId("1");
		
		uebMsg.putMsgType("testType");
		
		Boolean actualResponse = 	functionalMenuHandler.getFunctionalMenu(null);
		
	assertFalse(actualResponse);
	uebMsg.putSourceTopicName("test");
	 actualResponse = 	functionalMenuHandler.getFunctionalMenu(uebMsg);
	uebMsg.putUserId("123");
	
	EPUser user = new EPUser();
	user.setOrgUserId("123");
	user.setFirstName("TestFirstName");
	user.setLastName("TestLastName");	
	List<FunctionalMenuItem> menuItems=new ArrayList<>();
	FunctionalMenuItem menu=new FunctionalMenuItem();
	menu.setUrl("test");
	menu.setRestrictedApp(true);
	menuItems.add(menu);
	
	Mockito.when(searchSvc.searchUserByUserId(uebMsg.getUserId())).thenReturn(user);
	Mockito.when(functionalMenuService.getFunctionalMenuItems()).thenReturn(menuItems);
	Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
	actualResponse = 	functionalMenuHandler.getFunctionalMenu(uebMsg);
	assertTrue(actualResponse);
	
	uebMsg.putUserId("245");
	Mockito.when(searchSvc.searchUserByUserId(uebMsg.getUserId())).thenReturn(null);
	 actualResponse = 	functionalMenuHandler.getFunctionalMenu(uebMsg);
	}	
	
}
