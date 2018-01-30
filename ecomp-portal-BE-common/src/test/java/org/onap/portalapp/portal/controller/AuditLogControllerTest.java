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
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
package org.onap.portalapp.portal.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.controller.AuditLogController;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.util.EPUserUtils;
import org.onap.portalsdk.core.service.AuditService;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EPUserUtils.class)
public class AuditLogControllerTest {

	
	@Mock
	AuditService auditService;
	
	@InjectMocks
     AuditLogController auditLogController = new AuditLogController();

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
	public void auditLogTest()
	{
		PowerMockito.mockStatic(EPUserUtils.class);
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(EPUserUtils.getUserId(mockedRequest)).thenReturn((int)1);
		auditLogController.auditLog(mockedRequest, "1", "app", "test");
	}
	
	@Test
	public void auditLogTabTest()
	{
		PowerMockito.mockStatic(EPUserUtils.class);
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(EPUserUtils.getUserId(mockedRequest)).thenReturn((int)1);
		auditLogController.auditLog(mockedRequest, "1", "tab", "test");
	}
	
	@Test
	public void auditLogfunctionalTest()
	{
		PowerMockito.mockStatic(EPUserUtils.class);
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(EPUserUtils.getUserId(mockedRequest)).thenReturn((int)1);
		auditLogController.auditLog(mockedRequest, "1", "functional", "test");
	}
	
	@Test
	public void auditLogleftMenuTest()
	{
		PowerMockito.mockStatic(EPUserUtils.class);
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(EPUserUtils.getUserId(mockedRequest)).thenReturn((int)1);
		auditLogController.auditLog(mockedRequest, "1", "leftMenu", "test");
	}
	
	@Test(expected = NumberFormatException.class)
	public void auditLogExceptionTest()
	{
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(EPUserUtils.getUserId(mockedRequest)).thenReturn((int)1);
		auditLogController.auditLog(mockedRequest, "1", "app", "test");
	}
	
	@Test
	public void auditLogerrorTest()
	{
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenThrow(nullPointerException);
		auditLogController.auditLog(mockedRequest, "1", "app", "test");
	}
}
