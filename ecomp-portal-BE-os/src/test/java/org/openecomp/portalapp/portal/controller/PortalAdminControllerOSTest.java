/*package org.openecomp.portalapp.portal.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
import org.openecomp.portalapp.portal.domain.EPRole;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.service.AdminRolesService;
import org.openecomp.portalapp.portal.service.AdminRolesServiceImpl;
import org.openecomp.portalapp.portal.service.PortalAdminService;
import org.openecomp.portalapp.portal.service.PortalAdminServiceImpl;
import org.openecomp.portalapp.portal.test.core.MockEPUser;
import org.openecomp.portalapp.portal.transport.FieldsValidator;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalapp.test.framework.MockitoTestSuite;
import org.openecomp.portalapp.util.EPUserUtils;
import org.openecomp.portalsdk.core.service.AuditService;
import org.openecomp.portalsdk.core.service.AuditServiceImpl;

public class PortalAdminControllerOSTest {

	@InjectMocks
	PortalAdminController portalAdminController = new PortalAdminController();

	@Mock
	AdminRolesService adminRolesService = new AdminRolesServiceImpl();
	
	@Mock
	PortalAdminService portalAdminService = new PortalAdminServiceImpl();

	@Mock
	AuditService auditService = new AuditServiceImpl();

	 
	@Mock
	EcompPortalUtils ecompPortalUtils = new EcompPortalUtils();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();

	@Mock
	EPUserUtils ePUserUtils = new EPUserUtils();

	MockEPUser mockUser = new MockEPUser();
	
	@Test
	public void deletePortalAdminIfUserIsSuperAdminTest()
	{
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);
		String userInfo = "1-test";
		assertNull(portalAdminController.deletePortalAdmin(mockedRequest, userInfo, mockedResponse));

	}
	
	@Test
	public void deletePortalAdminTest()
	{
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
	
		FieldsValidator expectedFieldValidator = new FieldsValidator();
		expectedFieldValidator.setHttpStatusCode((long) 200);
		expectedFieldValidator.setFields(null);
		expectedFieldValidator.setErrorCode(null);
		FieldsValidator actualFieldValidator = new FieldsValidator();
		String userInfo = "1-test";
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(portalAdminService.deletePortalAdmin((long) 1)).thenReturn(expectedFieldValidator);
		actualFieldValidator = portalAdminController.deletePortalAdmin(mockedRequest, userInfo, mockedResponse);
       assertEquals(actualFieldValidator,expectedFieldValidator);

	}
	
	@Test
	public void deletePortalAdminWithNoUserInfoTest()
	{
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);
		String userInfo = "";
		assertNull(portalAdminController.deletePortalAdmin(mockedRequest, userInfo, mockedResponse));
	}
	
	@Test
	public void getRolesByAppExceptionTest()
	{
		EPUser user = mockUser.mockEPUser();;
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		List<EPRole> expectedRoleList = new ArrayList<EPRole>();	
		EPRole ePRole = new EPRole();
		expectedRoleList.add(ePRole);
		Long appId = (long) 1;
		Mockito.when(adminRolesService.getRolesByApp(user, appId)).thenThrow(nullPointerException);
		assertNull(portalAdminController.getRolesByApp(mockedRequest, appId, mockedResponse));
	}
	
	@Test
	public void getRolesByAppIfUserNullTest()
	{
		EPUser user = null;
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Long appId = (long) 1;
		assertNull(portalAdminController.getRolesByApp(mockedRequest, appId, mockedResponse));
	}
	
	@Test
	public void getRolesByAppTest()
	{
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		List<EPRole> expectedRoleList = new ArrayList<EPRole>();	
		EPRole ePRole = new EPRole();
		expectedRoleList.add(ePRole);
		Long appId = (long) 1;
		Mockito.when(adminRolesService.getRolesByApp(user, appId)).thenReturn(expectedRoleList);
		List<EPRole> actualRoleList = 	portalAdminController.getRolesByApp(mockedRequest, appId, mockedResponse);
		assertEquals(actualRoleList,expectedRoleList);
	}
}
*/