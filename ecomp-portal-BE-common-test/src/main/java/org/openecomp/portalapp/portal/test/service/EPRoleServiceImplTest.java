package org.openecomp.portalapp.portal.test.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openecomp.portalapp.portal.domain.EPRole;
import org.openecomp.portalapp.portal.service.EPRoleServiceImpl;
import org.openecomp.portalapp.portal.test.core.MockEPUser;
import org.openecomp.portalapp.test.framework.MockitoTestSuite;
import org.openecomp.portalsdk.core.domain.RoleFunction;
import org.openecomp.portalsdk.core.service.DataAccessService;

public class EPRoleServiceImplTest {

	@Mock
	DataAccessService dataAccessService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@InjectMocks
	EPRoleServiceImpl ePRoleServiceImpl = new EPRoleServiceImpl();

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();
	MockEPUser mockUser = new MockEPUser();

	@Test
	public void getRoleFunctionsTest() {
		List<RoleFunction> roleFunctionList = new ArrayList<>();
		Mockito.when(dataAccessService.getList(RoleFunction.class, null)).thenReturn(roleFunctionList);
		List<RoleFunction> expectedRoleFunctionList = ePRoleServiceImpl.getRoleFunctions();
		assertEquals(roleFunctionList, expectedRoleFunctionList);
	}

	@Test
	public void getAvailableChildRolesIfRoleIdIsNullTest() {
		List<EPRole> roleList = new ArrayList<>();
		EPRole role = new EPRole();
		EPRole role1 = new EPRole();
		role.addChildRole(role1);
		roleList.add(role);
		Mockito.when(dataAccessService.getList(EPRole.class, null)).thenReturn(roleList);
		List<EPRole> expectedRoleList = ePRoleServiceImpl.getAvailableChildRoles(null);
		assertEquals(roleList, expectedRoleList);
	}

	// @Test
	// public void getAvailableChildRolesIfRoleIdNotNullTest()
	// {
	// List<EPRole> roleList = new ArrayList<>();
	// EPRole role = new EPRole();
	// EPRole role1= new EPRole();
	// role.addChildRole(role1);
	// roleList.add(role);
	// Mockito.when(dataAccessService.getDomainObject(EPRole.class, 1,
	// null)).thenReturn(role);
	// Mockito.when(dataAccessService.getList(EPRole.class,
	// null)).thenReturn(roleList);
	//
	// List<EPRole> expectedRoleList =
	// ePRoleServiceImpl.getAvailableChildRoles((long) 1);
	// System.out.println(expectedRoleList);
	// assertEquals(roleList,expectedRoleList);
	// }
	//
	@Test
	public void getRoleFunctionTest() {
		RoleFunction roleFunction = new RoleFunction();
		Mockito.when(dataAccessService.getDomainObject(RoleFunction.class, "test", null)).thenReturn(roleFunction);
		RoleFunction expectedRoleFunction = ePRoleServiceImpl.getRoleFunction("test");
		assertEquals(expectedRoleFunction, roleFunction);
	}

	@Test
	public void saveRoleFunctionTest() {
		EPRole role = new EPRole();
		Mockito.doNothing().when(dataAccessService).saveDomainObject(role, null);
		ePRoleServiceImpl.saveRole(role);
	}

	@Test
	public void deleteRoleFunctionTest() {
		RoleFunction roleFunction = new RoleFunction();
		Mockito.doNothing().when(dataAccessService).deleteDomainObject(roleFunction, null);
		ePRoleServiceImpl.deleteRoleFunction(roleFunction);
	}

	@Test
	public void getRoleTest() {
		EPRole role = null;
		Mockito.when(dataAccessService.getDomainObject(EPRole.class, 1, null)).thenReturn(role);
		EPRole expectedRole = ePRoleServiceImpl.getRole((long) 1);
		assertEquals(expectedRole, role);
	}

	@Test
	public void getRoleIfappIdNullTest() {
		assertNull(ePRoleServiceImpl.getRole(null, null));

	}

	@Test
	public void getRoleIfappIdNotNullTest() {
		List<EPRole> roles = new ArrayList<>();
		EPRole role = new EPRole();
		roles.add(role);
		String sql = "SELECT * FROM fn_role where APP_ID = 1 AND APP_ROLE_ID = 1";
		Mockito.when(dataAccessService.executeSQLQuery(sql, EPRole.class, null)).thenReturn(roles);
		EPRole expectedRole = ePRoleServiceImpl.getRole((long) 1, (long) 1);
		assertEquals(expectedRole, role);

	}

	@Test
	public void getRoleIfListSizeIsMoreThan1Test() {
		List<EPRole> roles = new ArrayList<>();
		EPRole role = new EPRole();
		EPRole role1 = new EPRole();
		roles.add(role);
		roles.add(role1);
		String sql = "SELECT * FROM fn_role where APP_ID = 1 AND APP_ROLE_ID = 1";
		Mockito.when(dataAccessService.executeSQLQuery(sql, EPRole.class, null)).thenReturn(roles);
		EPRole expectedRole = ePRoleServiceImpl.getRole((long) 1, (long) 1);
		assertEquals(expectedRole, role);

	}

	@Test
	public void getRoleIfListSizeIsEmptyTest() {
		List<EPRole> roles = new ArrayList<>();
		String sql = "SELECT * FROM fn_role where APP_ID = 1 AND APP_ROLE_ID = 1";
		Mockito.when(dataAccessService.executeSQLQuery(sql, EPRole.class, null)).thenReturn(roles);
		assertNull(ePRoleServiceImpl.getRole((long) 1, (long) 1));

	}

	@Test
	public void saveRoleTest() {
		EPRole role = new EPRole();
		Mockito.doNothing().when(dataAccessService).saveDomainObject(role, null);
		ePRoleServiceImpl.saveRole(role);
	}

	@Test
	public void deleteRoleTest() {
		EPRole role = new EPRole();
		Mockito.doNothing().when(dataAccessService).deleteDomainObject(role, null);
		ePRoleServiceImpl.deleteRole(role);
	}

	@Test
	public void getAvailableRolesTest() {
		List<EPRole> roleList = new ArrayList<>();
		Mockito.when(dataAccessService.getList(EPRole.class, null)).thenReturn(roleList);
		List<EPRole> expectedRoleList = ePRoleServiceImpl.getAvailableRoles();
		assertEquals(expectedRoleList, roleList);
	}

	@Test
	public void getAppRolesTest() {
		final Map<String, String> portalParams = null;
		List<EPRole> roleList = new ArrayList<>();
		Mockito.when(dataAccessService.executeNamedQuery("getPortalAppRoles", portalParams, null)).thenReturn(roleList);
		assertNull(ePRoleServiceImpl.getAppRole("test", (long) 1));

	}

	@SuppressWarnings("unchecked")
	@Test
	public void getAppRolesIfNotPortalTest() {
		final Map<String, String> params = null;
		List<EPRole> roleList = new ArrayList<>();
		EPRole role = new EPRole();
		EPRole role1 = new EPRole();
		roleList.add(role);
		roleList.add(role1);
		Mockito.when((List<EPRole>) dataAccessService.executeNamedQuery("getAppRoles", params, null))
				.thenReturn(roleList);
		List<EPRole> expectedRoleList = (List<EPRole>) ePRoleServiceImpl.getAppRole("test", (long) 10);
		System.out.println(expectedRoleList);

	}

	@Test
	public void saveRoleFunction() {
		RoleFunction domainRoleFunction = new RoleFunction();
		Mockito.doNothing().when(dataAccessService).saveDomainObject(domainRoleFunction, null);
		ePRoleServiceImpl.saveRoleFunction(domainRoleFunction);
	}
}
