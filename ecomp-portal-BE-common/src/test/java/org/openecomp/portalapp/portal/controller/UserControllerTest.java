package org.openecomp.portalapp.portal.controller;

import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openecomp.portalapp.portal.framework.MockitoTestSuite;
import org.openecomp.portalapp.portal.controller.UserController;
import org.openecomp.portalapp.portal.core.MockEPUser;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestResponse;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.openecomp.portalapp.portal.service.UserService;
import org.openecomp.portalapp.portal.service.UserServiceImpl;
import org.openecomp.portalapp.portal.transport.ProfileDetail;
import org.openecomp.portalapp.util.EPUserUtils;
import org.openecomp.portalsdk.core.onboarding.util.CipherUtil;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CipherUtil.class)
public class UserControllerTest extends MockitoTestSuite {

	@InjectMocks
	UserController userController = new UserController();

	@Mock
	UserService userService = new UserServiceImpl();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Mock
	EPUserUtils ePUserUtils = new EPUserUtils();

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();

	MockEPUser mockUser = new MockEPUser();

	@Test
	public void getLoggedinUserExceptionTest() {
		EPUser epUser = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(epUser);
		PortalRestResponse<ProfileDetail> expectedResponse = new PortalRestResponse<ProfileDetail>();
		expectedResponse.setMessage(null);
		expectedResponse.setResponse(null);
		PortalRestStatusEnum enu = null;
		expectedResponse.setStatus(enu.ERROR);
		PortalRestResponse<ProfileDetail> response = userController.getLoggedinUser(mockedRequest);
		assertEquals(response, expectedResponse);
	}

	@Test
	public void getLoggedinUserTest() throws Exception {
		EPUser epUser = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(epUser);
		PortalRestResponse<ProfileDetail> expectedResponse = new PortalRestResponse<ProfileDetail>();
		expectedResponse.setMessage("success");
		ProfileDetail profileDetail = new ProfileDetail();
		expectedResponse.setResponse(profileDetail);
		PortalRestStatusEnum enu = null;
		expectedResponse.setStatus(enu.OK);
		PowerMockito.mockStatic(CipherUtil.class);
		Mockito.when(CipherUtil.decrypt(epUser.getLoginPwd())).thenReturn("Password");
		PortalRestResponse<ProfileDetail> response = userController.getLoggedinUser(mockedRequest);
		assertEquals(response.getMessage(), expectedResponse.getMessage());
		assertEquals(response.getStatus(), expectedResponse.getStatus());
	}

	@Test
	public void modifyLoggedinUserIfProfileNullTest() {
		PortalRestResponse<String> expectedResponse = new PortalRestResponse<String>();
		expectedResponse.setMessage("java.lang.NullPointerException");
		expectedResponse.setResponse(null);
		PortalRestStatusEnum enu = null;
		expectedResponse.setStatus(enu.ERROR);
		ProfileDetail profileDetail = null;
		PortalRestResponse<String> actualResponse = userController.modifyLoggedinUser(mockedRequest, profileDetail);
		assertEquals(actualResponse, expectedResponse);
		assertEquals(actualResponse.getStatus(), expectedResponse.getStatus());
	}

	@Test
	public void modifyLoggedinUserExceptionTest() {
		EPUser epUser = mockUser.mockEPUser();

		PortalRestResponse<String> expectedResponse = new PortalRestResponse<String>();
		expectedResponse.setMessage(
				"java.lang.ClassCastException: com.sun.crypto.provider.AESCipher$General cannot be cast to javax.crypto.CipherSpi");
		expectedResponse.setResponse(null);
		PortalRestStatusEnum enu = null;
		expectedResponse.setStatus(enu.ERROR);
		ProfileDetail profileDetail = new ProfileDetail();
		profileDetail.setFirstName("Test_FirstName");
		profileDetail.setLastName("Test_LastName");
		profileDetail.setEmail("Test_Email");
		profileDetail.setLoginId("Test_LoginId");
		profileDetail.setLoginPassword("Test_LoginPassword");
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(epUser);
		PortalRestResponse<String> actualResponse = userController.modifyLoggedinUser(mockedRequest, profileDetail);
		assertEquals(actualResponse, expectedResponse);

	}

	/*@Test
	public void modifyLoggedinUserTest() throws Exception {
		EPUser epUser = mockUser.mockEPUser();
		PortalRestResponse<String> expectedResponse = new PortalRestResponse<String>();
		expectedResponse.setMessage("success");
		expectedResponse.setResponse(null);
		PortalRestStatusEnum enu = null;
		expectedResponse.setStatus(enu.OK);
		ProfileDetail profileDetail = new ProfileDetail();
		profileDetail.setFirstName("Test_FirstName");
		profileDetail.setLastName("Test_LastName");
		profileDetail.setEmail("Test_Email");
		profileDetail.setLoginId("Test_LoginId");
		profileDetail.setLoginPassword("Test_LoginPassword");
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(epUser);
		PowerMockito.mockStatic(CipherUtil.class);
		Mockito.when(CipherUtil.decrypt(epUser.getLoginPwd())).thenReturn("Password");
		PortalRestResponse<String> actualResponse = userController.modifyLoggedinUser(mockedRequest, profileDetail);
		System.out.println(actualResponse);
	}*/
}
