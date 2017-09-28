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
import org.openecomp.portalapp.portal.framework.MockitoTestSuite;
import org.openecomp.portalapp.portal.controller.GetAccessController;
import org.openecomp.portalapp.portal.core.MockEPUser;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.domain.GetAccessResult;
import org.openecomp.portalapp.portal.service.GetAccessService;
import org.openecomp.portalapp.portal.service.GetAccessServiceImpl;
import org.openecomp.portalapp.util.EPUserUtils;

public class GetAccessControllerTest {

	@Mock
	GetAccessService getAccessService = new GetAccessServiceImpl();

	@InjectMocks
	GetAccessController getAccessController = new GetAccessController();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	MockEPUser mockUser = new MockEPUser();
	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();
	
	
	@Test
	public void getAppList() throws IOException
	{
		List<GetAccessResult> expectedAppsList = new ArrayList<GetAccessResult>();
		EPUser user = mockUser.mockEPUser();
		GetAccessResult getAccessResult = new GetAccessResult();
		getAccessResult.setRowId("1");
		getAccessResult.setRoleId((long) 1);
		getAccessResult.setEcompFunction("test");
		getAccessResult.setAppName("Test_App");
		getAccessResult.setAppMotsId(1);
		getAccessResult.setRoleName("Test_role");
		getAccessResult.setRoleActive("N");
		getAccessResult.setReqType("test");
		
		expectedAppsList.add(getAccessResult);
		
		List<GetAccessResult> actualAppsList = null;
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(getAccessService.getAppAccessList(user)).thenReturn(expectedAppsList);
		actualAppsList = getAccessController.getAppList(mockedRequest);
		assertTrue(actualAppsList.contains(getAccessResult));
	}

}
