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
