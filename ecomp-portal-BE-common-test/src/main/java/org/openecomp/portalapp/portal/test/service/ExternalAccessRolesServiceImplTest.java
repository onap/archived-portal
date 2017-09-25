package org.openecomp.portalapp.portal.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openecomp.portalapp.portal.domain.CentralRoleFunction;
import org.openecomp.portalapp.portal.domain.EPApp;
import org.openecomp.portalapp.portal.domain.EPAppRoleFunction;
import org.openecomp.portalapp.portal.domain.EPRole;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.service.ExternalAccessRolesServiceImpl;
import org.openecomp.portalapp.portal.test.core.MockEPUser;
import org.openecomp.portalapp.portal.transport.ExternalAccessPerms;
import org.openecomp.portalapp.portal.utils.EPCommonSystemProperties;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalapp.test.framework.MockitoTestSuite;
import org.openecomp.portalsdk.core.domain.Role;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.openecomp.portalsdk.core.service.DataAccessServiceImpl;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ EcompPortalUtils.class, SystemProperties.class, EPCommonSystemProperties.class })
public class ExternalAccessRolesServiceImplTest {
	@Mock
	DataAccessService dataAccessService1 = new DataAccessServiceImpl();

	@Mock
	RestTemplate template = new RestTemplate();

	@InjectMocks
	ExternalAccessRolesServiceImpl externalAccessRolesServiceImpl = new ExternalAccessRolesServiceImpl();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();
	MockEPUser mockUser = new MockEPUser();

	public EPApp mockApp() {
		EPApp app = new EPApp();
		app.setName("Test");
		app.setImageUrl("test");
		app.setDescription("test");
		app.setNotes("test");
		app.setUrl("test");
		app.setId((long) 10);
		app.setAppRestEndpoint("test");
		app.setAlternateUrl("test");
		app.setName("test");
		app.setMlAppName("test");
		app.setMlAppAdminId("test");
		app.setUsername("test");
		app.setAppPassword("test");
		app.setOpen(false);
		app.setEnabled(false);
		app.setUebKey("test");
		app.setUebSecret("test");
		app.setUebTopicName("test");
		app.setAppType(1);
		return app;
	}

	@Test
	public void getAppRolesIfAppIsPortalTest() throws Exception {
		List<EPRole> applicationRoles = new ArrayList<>();
		Mockito.when(dataAccessService1.getList(EPRole.class, "test", null, null)).thenReturn(applicationRoles);
		List<EPRole> expectedApplicationRoles = externalAccessRolesServiceImpl.getAppRoles((long) 1);
		assertEquals(expectedApplicationRoles, applicationRoles);
	}

	@Test
	public void getAppRolesTest() throws Exception {
		List<EPRole> applicationRoles = new ArrayList<>();
		Mockito.when(dataAccessService1.getList(EPRole.class, "test", null, null)).thenReturn(applicationRoles);
		List<EPRole> expectedApplicationRoles = externalAccessRolesServiceImpl.getAppRoles((long) 10);
		assertEquals(expectedApplicationRoles, applicationRoles);
	}

	// @SuppressWarnings("null")
	// @Test(expected = java.lang.Exception.class)
	// public void getAppRolesExceptionTest() throws Exception{
	// List<EPRole> applicationRoles = new ArrayList<>();
	// DataAccessService dataAccessService = null ;
	// Mockito.when(dataAccessService.getList(EPRole.class, "where app_id = 10",
	// null, null)).thenThrow(nullPointerException);
	// List<EPRole> expectedApplicationRoles =
	// externalAccessRolesServiceImpl.getAppRoles((long) 10);
	// assertEquals(expectedApplicationRoles,applicationRoles);
	// }

	@Test(expected = java.lang.Exception.class)
	public void getAppExceptionTest() throws Exception {
		List<EPApp> app = new ArrayList<>();
		String uebKey = "test-ueb-key";
		Mockito.when(dataAccessService1.getList(EPApp.class, " where ueb_key = '" + uebKey + "'", null, null))
				.thenReturn(app);
		List<EPApp> expectedapp = externalAccessRolesServiceImpl.getApp(uebKey);
		assertEquals(app, expectedapp);
	}

	@Test(expected = java.lang.Exception.class)
	public void getAppTest() throws Exception {
		List<EPApp> appList = new ArrayList<>();
		EPApp app = mockApp();
		appList.add(app);
		String uebKey = "test-ueb-key";
		Mockito.when(dataAccessService1.getList(EPApp.class, " where ueb_key = '" + uebKey + "'", null, null))
				.thenReturn(appList);
		List<EPApp> expectedapp = externalAccessRolesServiceImpl.getApp(uebKey);
	}

	@Test
	public void getAppErrorTest() throws Exception {
		List<EPApp> appList = new ArrayList<>();
		EPApp app = mockApp();
		app.setId((long) 1);
		appList.add(app);
		String uebKey = "test-ueb-key";
		Mockito.when(dataAccessService1.getList(EPApp.class, " where ueb_key = '" + uebKey + "'", null, null))
				.thenReturn(appList);
		List<EPApp> expectedapp = externalAccessRolesServiceImpl.getApp(uebKey);
		assertEquals(appList, expectedapp);
	}

	@Test
	public void addRoleTest() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		String uebKey = "test-ueb-key";
		Role role = new Role();
		role.setId((long) 25);
		EPApp app = mockApp();
		app.setEnabled(true);
		app.setId((long) 10);
		app.setNameSpace("test_namesapce");
		Mockito.when(EcompPortalUtils.base64encodeKeyForAAFBasicAuth()).thenReturn(headers);
		List<EPApp> appList = new ArrayList<>();
		appList.add(app);
		List<EPRole> roleList = new ArrayList<>();
		EPRole ePRole = new EPRole();
		role.setName("Test Role");
		roleList.add(ePRole);
		Mockito.when(dataAccessService1.getList(EPApp.class, " where ueb_key = '" + uebKey + "'", null, null))
				.thenReturn(appList);
		Mockito.when(dataAccessService1.getList(EPRole.class, " where app_role_id = " + role.getId(), null, null))
				.thenReturn(roleList);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL))
				.thenReturn("Testurl");
		ResponseEntity<String> addResponse = new ResponseEntity<>(HttpStatus.CREATED);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.POST),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(addResponse);

		assertTrue(externalAccessRolesServiceImpl.addRole(role, uebKey));
	}

	@Test
	public void addRoleMethodNotAllowedTest() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(EPCommonSystemProperties.class);
		String uebKey = "test-ueb-key";
		Role role = new Role();
		role.setId((long) 25);
		EPApp app = mockApp();
		app.setEnabled(true);
		app.setId((long) 10);
		app.setNameSpace("test_namesapce");
		Mockito.when(EcompPortalUtils.base64encodeKeyForAAFBasicAuth()).thenReturn(headers);
		List<EPApp> appList = new ArrayList<>();
		appList.add(app);
		List<EPRole> roleList = new ArrayList<>();
		EPRole ePRole = new EPRole();
		role.setName("Test Role");
		roleList.add(ePRole);
		Mockito.when(dataAccessService1.getList(EPApp.class, " where ueb_key = '" + uebKey + "'", null, null))
				.thenReturn(appList);
		Mockito.when(dataAccessService1.getList(EPRole.class, " where app_role_id = " + role.getId(), null, null))
				.thenReturn(roleList);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL))
				.thenReturn("Testurl");
		ResponseEntity<String> addResponse = new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.POST),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(addResponse);

		assertFalse(externalAccessRolesServiceImpl.addRole(role, uebKey));
	}

	public EPApp getApp() {
		EPApp app = new EPApp();
		app.setName("Test");
		app.setImageUrl("test");
		app.setDescription("test");
		app.setNotes("test");
		app.setUrl("test");
		app.setId((long) 1);
		app.setAppRestEndpoint("test");
		app.setAlternateUrl("test");
		app.setName("test");
		app.setMlAppName("test");
		app.setMlAppAdminId("test");
		app.setUsername("test");
		app.setAppPassword("test");
		app.setOpen(true);
		app.setEnabled(false);
		app.setUebKey("test");
		app.setUebSecret("test");
		app.setUebTopicName("test");
		app.setAppType(1);
		return app;
	}

	@Test
	public void deleteCentralRoleFunctionTest() throws Exception {
		final Map<String, String> params = new HashMap<>();
		EPApp app = mockApp();
		params.put("functionCd", "menu_fun_code");
		params.put("appId", String.valueOf(10));
		List<CentralRoleFunction> centralRoleFunctionList = new ArrayList<>();
		CentralRoleFunction domainCentralRoleFunction = new CentralRoleFunction();
		domainCentralRoleFunction.setCode("menu_fun_code");
		centralRoleFunctionList.add(domainCentralRoleFunction);
		Mockito.when(dataAccessService1.executeNamedQuery("getAppFunctionDetails", params, null))
				.thenReturn(centralRoleFunctionList);
		ResponseEntity<String> addResponse = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.DELETE),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(addResponse);
		HttpHeaders headers = new HttpHeaders();
		PowerMockito.mockStatic(EcompPortalUtils.class);
		Mockito.when(EcompPortalUtils.base64encodeKeyForAAFBasicAuth()).thenReturn(headers);

		Mockito.doNothing().when(dataAccessService1).deleteDomainObjects(EPAppRoleFunction.class,
				"app_id = " + app.getId() + " and function_cd = '" + "menu_fun_code" + "'", null);

		assertTrue(externalAccessRolesServiceImpl.deleteCentralRoleFunction("menu_fun_code", app));
	}

	@Test
	public void deleteCentralRoleFunctionFailTest() throws Exception {
		final Map<String, String> params = new HashMap<>();
		EPApp app = mockApp();
		params.put("functionCd", "menu_fun_code");
		params.put("appId", String.valueOf(10));
		List<CentralRoleFunction> centralRoleFunctionList = new ArrayList<>();
		CentralRoleFunction domainCentralRoleFunction = new CentralRoleFunction();
		domainCentralRoleFunction.setCode("menu_fun_code");
		centralRoleFunctionList.add(domainCentralRoleFunction);
		HttpClientErrorException httpClientErrorException = new HttpClientErrorException(HttpStatus.NOT_FOUND);
		Mockito.when(dataAccessService1.executeNamedQuery("getAppFunctionDetails", params, null))
				.thenReturn(centralRoleFunctionList);
		ResponseEntity<String> addResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.DELETE),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenThrow(httpClientErrorException);
		HttpHeaders headers = new HttpHeaders();
		PowerMockito.mockStatic(EcompPortalUtils.class);
		Mockito.when(EcompPortalUtils.base64encodeKeyForAAFBasicAuth()).thenReturn(headers);

		Mockito.doNothing().when(dataAccessService1).deleteDomainObjects(EPAppRoleFunction.class,
				"app_id = " + app.getId() + " and function_cd = '" + "menu_fun_code" + "'", null);

		assertTrue(externalAccessRolesServiceImpl.deleteCentralRoleFunction("menu_fun_code", app));
	}

	@Test
	public void deleteCentralRoleFunctionExceptionTest() {
		final Map<String, String> params = new HashMap<>();
		EPApp app = mockApp();
		params.put("functionCd", "menu_fun_code");
		params.put("appId", String.valueOf(10));
		List<CentralRoleFunction> centralRoleFunctionList = new ArrayList<>();
		CentralRoleFunction domainCentralRoleFunction = new CentralRoleFunction();
		domainCentralRoleFunction.setCode("menu_fun_code");
		centralRoleFunctionList.add(domainCentralRoleFunction);
		Mockito.when(dataAccessService1.executeNamedQuery("getAppFunctionDetails", params, null))
				.thenThrow(nullPointerException);
		assertFalse(externalAccessRolesServiceImpl.deleteCentralRoleFunction("menu_fun_code", app));
	}

	@Test
	public void getUserTest() {
		List<EPUser> userList = new ArrayList<>();
		EPUser user = mockUser.mockEPUser();
		userList.add(user);
		Mockito.when(dataAccessService1.getList(EPUser.class, " where org_user_id = '" + "guestT" + "'", null, null))
				.thenReturn(userList);
		List<EPUser> expectedUserList = externalAccessRolesServiceImpl.getUser("guestT");
		assertEquals(expectedUserList, userList);
	}

	public void saveCentralRoleFunctionTest() throws Exception {
		CentralRoleFunction centralRoleFunction = new CentralRoleFunction();
		centralRoleFunction.setCode("menu_test");
		EPApp app = mockApp();
		app.setId((long) 1);
		final Map<String, String> params = new HashMap<>();
		params.put("functionCd", "menu_test");
		params.put("appId", String.valueOf(1));
		ExternalAccessPerms extPerms = new ExternalAccessPerms();
		PowerMockito.mockStatic(EcompPortalUtils.class);
		HttpHeaders headers = new HttpHeaders();
		Mockito.when(EcompPortalUtils.base64encodeKeyForAAFBasicAuth()).thenReturn(headers);
		List<CentralRoleFunction> appRoleFunc  = new ArrayList<>();
		appRoleFunc.add(centralRoleFunction);
		Mockito.when(dataAccessService1.executeNamedQuery("getAppFunctionDetails", params,
				null)).thenReturn(appRoleFunc);
		ResponseEntity<String> addResponse = new ResponseEntity<>(HttpStatus.OK);
		Mockito.when(template.exchange(Matchers.anyString(), Matchers.eq(HttpMethod.GET),
				Matchers.<HttpEntity<String>>any(), Matchers.eq(String.class))).thenReturn(addResponse);
	}
}
