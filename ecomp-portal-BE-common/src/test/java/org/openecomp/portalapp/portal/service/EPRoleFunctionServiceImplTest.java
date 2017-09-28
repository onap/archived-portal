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
package org.openecomp.portalapp.portal.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openecomp.portalapp.portal.domain.EPRole;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.service.EPRoleFunctionServiceImpl;
import org.openecomp.portalapp.portal.core.MockEPUser;
import org.openecomp.portalapp.portal.framework.MockitoTestSuite;
import org.openecomp.portalapp.util.EPUserUtils;
import org.openecomp.portalsdk.core.domain.RoleFunction;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ SystemProperties.class, EPUserUtils.class })
public class EPRoleFunctionServiceImplTest {

	@Mock
	DataAccessService dataAccessService;

	@InjectMocks
	EPRoleFunctionServiceImpl ePRoleFunctionServiceImpl = new EPRoleFunctionServiceImpl();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	NullPointerException nullPointerException = new NullPointerException();
	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();
	MockEPUser mockUser = new MockEPUser();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();

	@Test
	public void getRoleFunctionsTest() {
		List<RoleFunction> functions = new ArrayList<>();
		Mockito.when(dataAccessService.getList(RoleFunction.class, null)).thenReturn(functions);
		List<RoleFunction> expectedFunctions = ePRoleFunctionServiceImpl.getRoleFunctions();
		assertEquals(expectedFunctions, functions);
	}

	@Test
	public void getRoleFunctionsRequestTest() {
		EPUser user = mockUser.mockEPUser();
		HashSet roleFunctions = new HashSet<>();
		PowerMockito.mockStatic(SystemProperties.class);
		HttpSession session = mockedRequest.getSession();
		Mockito.when(session.getAttribute(SystemProperties.getProperty(SystemProperties.ROLE_FUNCTIONS_ATTRIBUTE_NAME)))
				.thenReturn(roleFunctions);
		HashSet expectedRoleFunctions = (HashSet) ePRoleFunctionServiceImpl.getRoleFunctions(mockedRequest, user);
		assertEquals(expectedRoleFunctions, roleFunctions);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getRoleFunctionsRequestIfNullTest() {
		EPUser user = mockUser.mockEPUser();
		HashSet roleFunctions = null;
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(EPUserUtils.class);
		HttpSession session = mockedRequest.getSession();
		Mockito.when(session.getAttribute(SystemProperties.getProperty(SystemProperties.ROLE_FUNCTIONS_ATTRIBUTE_NAME)))
				.thenReturn(roleFunctions);
		HashMap roles = new HashMap<>();
		EPRole role = new EPRole();
		SortedSet<RoleFunction> roleFunctionSet = new TreeSet<RoleFunction>();
		RoleFunction rolefun = new RoleFunction();
		roleFunctionSet.add(rolefun);
		role.setRoleFunctions(roleFunctionSet);
		roles.put((long) 1, role);
		Mockito.when(EPUserUtils.getRoles(mockedRequest)).thenReturn(roles);
		HashSet expectedRoleFunctions = (HashSet) ePRoleFunctionServiceImpl.getRoleFunctions(mockedRequest, user);
		assertTrue(expectedRoleFunctions.size() == 1);

	}
}
