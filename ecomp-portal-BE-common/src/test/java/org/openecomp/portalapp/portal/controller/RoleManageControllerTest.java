package org.openecomp.portalapp.portal.controller;

import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openecomp.portalapp.controller.core.RoleController;
import org.openecomp.portalapp.controller.core.RoleFunctionListController;
import org.openecomp.portalapp.controller.core.RoleListController;
import org.openecomp.portalapp.portal.framework.MockitoTestSuite;
import org.openecomp.portalapp.portal.controller.RoleManageController;
import org.openecomp.portalapp.portal.core.MockEPUser;
import org.openecomp.portalapp.portal.domain.EPApp;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestResponse;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.openecomp.portalapp.portal.service.ExternalAccessRolesService;
import org.springframework.web.servlet.ModelAndView;

public class RoleManageControllerTest {

	
	
	@Mock
	 RoleController roleController;

	@Mock
    RoleListController roleListController;

	@Mock
    RoleFunctionListController roleFunctionListController;


	@Mock
	ExternalAccessRolesService externalAccessRolesService;
	
	@Mock
	ExternalAccessRolesService externalAccessRolesService1 = null;
	
	@InjectMocks
	RoleManageController roleManageController = new RoleManageController(); 

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
	public void toggleRoleTest()
	{
		PortalRestResponse<String> portalRestResponse = new PortalRestResponse<String>();
		portalRestResponse.setMessage("success");
		portalRestResponse.setResponse(null);
		PortalRestStatusEnum portalRestStatusEnum = null;
		portalRestResponse.setStatus(portalRestStatusEnum.OK);
		
		PortalRestResponse<String> expectedpPortalRestResponse=roleManageController.toggleRole(mockedRequest, mockedResponse);
		assertEquals(portalRestResponse,expectedpPortalRestResponse);
		
	}
	
//	@Test
//	public void toggleRoleExceptionTest()
//	{
//		PortalRestResponse<String> portalRestResponse = new PortalRestResponse<String>();
//		portalRestResponse.setMessage("success");
//		portalRestResponse.setResponse(null);
//		PortalRestStatusEnum portalRestStatusEnum = null;
//		portalRestResponse.setStatus(portalRestStatusEnum.OK);
//		Mockito.doNothing().when(roleListController).toggleRole(mockedRequest, mockedResponse))).th
//		getRoleListController().toggleRole(request, response)
//		
//		PortalRestResponse<String> expectedpPortalRestResponse=roleManageController.toggleRole(mockedRequest, mockedResponse);
//		assertEquals(portalRestResponse,expectedpPortalRestResponse);
//		
//	}
	@Test
	public void removeRoleTest() throws Exception
	{
		ModelAndView modelandView = new ModelAndView("login.htm");
		Mockito.when(roleListController.removeRole(mockedRequest, mockedResponse)).thenReturn(modelandView);
		ModelAndView expectedModelandView =	roleManageController.removeRole(mockedRequest, mockedResponse);
		assertEquals(expectedModelandView, modelandView);
	}
	
	@Test
	public void saveRoleTest() throws Exception
	{
		ModelAndView modelandView = new ModelAndView("login.htm");
		Mockito.when(roleController.saveRole(mockedRequest, mockedResponse)).thenReturn(modelandView);
		ModelAndView expectedModelandView =	roleManageController.saveRole(mockedRequest, mockedResponse);
		assertEquals(expectedModelandView, modelandView);
	}
	
	@Test
	public void removeRoleRoleFunctionTest() throws Exception
	{
		ModelAndView modelandView = new ModelAndView("login.htm");
		Mockito.when(roleController.removeRoleFunction(mockedRequest, mockedResponse)).thenReturn(modelandView);
		ModelAndView expectedModelandView =	roleManageController.removeRoleRoleFunction(mockedRequest, mockedResponse);
		assertEquals(expectedModelandView, modelandView);
	}
	
	@Test
	public void addRoleRoRoleFunctionTest() throws Exception
	{
		ModelAndView modelandView = new ModelAndView("login.htm");
		Mockito.when(roleController.addRoleFunction(mockedRequest, mockedResponse)).thenReturn(modelandView);
		ModelAndView expectedModelandView =	roleManageController.addRoleRoRoleFunction(mockedRequest, mockedResponse);
		assertEquals(expectedModelandView, modelandView);
	}
	
	@Test
	public void removeChildRoleTest() throws Exception
	{
		ModelAndView modelandView = new ModelAndView("login.htm");
		Mockito.when(roleController.removeChildRole(mockedRequest, mockedResponse)).thenReturn(modelandView);
		ModelAndView expectedModelandView =	roleManageController.removeChildRole(mockedRequest, mockedResponse);
		assertEquals(expectedModelandView, modelandView);
	}
	
	
	@Test
	public void getRoleTest() throws Exception
	{
		Mockito.doNothing().when(roleController).getRole(mockedRequest, mockedResponse);
		roleManageController.getRole(mockedRequest, mockedResponse);
	}
	
	@Test
	public void getRolesTest() throws Exception
	{
		Mockito.doNothing().when(roleListController).getRoles(mockedRequest, mockedResponse);
		roleManageController.getRoles(mockedRequest, mockedResponse);
	}
	
	@Test
	public void getRoleFunctionListTest() throws Exception
	{
		Mockito.doNothing().when(roleFunctionListController).getRoleFunctionList(mockedRequest, mockedResponse);
		roleManageController.getRoleFunctionList(mockedRequest, mockedResponse);
	}
	
	@Test
	public void saveRoleFunctionTest() throws Exception
	{
		Mockito.doNothing().when(roleFunctionListController).saveRoleFunction(mockedRequest, mockedResponse, "test");
		roleManageController.saveRoleFunction(mockedRequest, mockedResponse, "test");
	}
	
	@Test
	public void removeRoleFunctionTest() throws Exception
	{
		Mockito.doNothing().when(roleFunctionListController).removeRoleFunction(mockedRequest, mockedResponse, "test");
		roleManageController.removeRoleFunction(mockedRequest, mockedResponse, "test");
	}
	
	@Test
	public void syncRolesTest() throws Exception
	{
		EPApp app = new EPApp();
		Mockito.doNothing().when(externalAccessRolesService).syncApplicationRolesWithEcompDB(app);
		roleManageController.syncRoles(app);
	}
	
	
	@Test
	public void addeChildRoleTest() throws Exception
	{
		ModelAndView modelandView = new ModelAndView("login.htm");
		Mockito.when(roleController.addChildRole(mockedRequest, mockedResponse)).thenReturn(modelandView);
		ModelAndView expectedModelandView =	roleManageController.addChildRole(mockedRequest, mockedResponse);
		assertEquals(expectedModelandView, modelandView);
	}
}
