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
package org.onap.portalapp.portal.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.DecoderException;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.CentralV2RoleFunction;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.exceptions.RoleFunctionException;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.service.EPRoleFunctionServiceCentralizedImpl;
import org.onap.portalsdk.core.domain.RoleFunction;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.util.SystemProperties;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SystemProperties.class)
@Ignore
public class EPRoleFunctionServiceCentralizedImplTest {

	
	@Mock
	DataAccessService dataAccessService;
	
	@Mock
	SessionFactory sessionFactory;

	@InjectMocks
	EPRoleFunctionServiceCentralizedImpl ePRoleFunctionServiceCentralizedImpl = new EPRoleFunctionServiceCentralizedImpl();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	NullPointerException nullPointerException = new NullPointerException();
	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();
	MockEPUser mockUser = new MockEPUser();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	
	@Test(expected = NullPointerException.class)
	public void getRoleFunctions()
	{
		List<CentralV2RoleFunction> getRoleFuncList = new ArrayList<>();
		CentralV2RoleFunction centralV2RoleFunction = new CentralV2RoleFunction();
		getRoleFuncList.add(centralV2RoleFunction);
		List<RoleFunction> getRoleFuncListOfPortal = new ArrayList<>();
		RoleFunction roleFunction = new RoleFunction();
		getRoleFuncListOfPortal.add(roleFunction);
		final Map<String, Long> params = new HashMap<>();
		params.put("appId", (long) 1);
		Mockito.when(dataAccessService.executeNamedQuery("getAllRoleFunctions", params, null)).thenReturn(getRoleFuncList);
		List<RoleFunction> expectedGetRoleFuncListOfPortal = ePRoleFunctionServiceCentralizedImpl.getRoleFunctions();
		assertEquals(expectedGetRoleFuncListOfPortal.size(),getRoleFuncListOfPortal.size());
	}
	
	@Test
	public void getRoleFunctionsNewTest() throws RoleFunctionException
	{
		HttpSession session = mockedRequest.getSession();
		EPUser user = mockUser.mockEPUser();
		user.setId((long) 1);
		String userId = user.getId().toString();
		final Map<String, String> params = new HashMap<>();
		params.put("userId", userId);		
		@SuppressWarnings("unused")
		List getRoleFuncListOfPortal = new ArrayList<>();
		Mockito.when(dataAccessService.executeNamedQuery("getRoleFunctionsOfUser", params, null)).thenReturn(getRoleFuncListOfPortal);
		Set<String> getRoleFuncListOfPortalSet = ePRoleFunctionServiceCentralizedImpl.getRoleFunctions(mockedRequest, user);
		assertTrue(getRoleFuncListOfPortalSet.size() == 0);
	}
}
