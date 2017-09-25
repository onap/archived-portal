//package org.openecomp.portalapp.portal.service;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.openecomp.portalapp.portal.framework.MockitoTestSuite;
//import org.openecomp.portalapp.portal.utils.EPSystemProperties;
//import org.openecomp.portalsdk.core.service.DataAccessService;
//import org.openecomp.portalsdk.core.util.SystemProperties;
//import org.powermock.api.mockito.PowerMockito;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.UnsupportedEncodingException;
//import java.net.HttpURLConnection;
//import org.powermock.core.classloader.annotations.PrepareForTest;
//import org.powermock.modules.junit4.PowerMockRunner;
//
//
//@RunWith(PowerMockRunner.class)
//@PrepareForTest({ SystemProperties.class , EPSystemProperties.class , SystemProperties.class})
//public class UserServiceImplTest {
//
//	
//	@InjectMocks
//	UserServiceImpl userServiceImpl = new UserServiceImpl();
//
//	@Mock
//	DataAccessService dataAccessService;
//	
//	@Mock
//	HttpURLConnection con;
//	
//	@Before
//	public void setup() {
//		MockitoAnnotations.initMocks(this);
//	}
//
//	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();
//
//	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
//	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
//	
//	@Test
//	public void getUserByUserIdTest() throws UnsupportedEncodingException, IOException
//	{
//		 BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
//		PowerMockito.mockStatic(SystemProperties.class);
//		PowerMockito.mockStatic(EPSystemProperties.class);
//		Mockito.when(SystemProperties.getProperty(SystemProperties.AUTHENTICATION_MECHANISM)).thenReturn("OIDC");
//		Mockito.when(EPSystemProperties.getProperty(EPSystemProperties.AUTH_USER_SERVER)).thenReturn("http://www.google.com");
//		Mockito.when(new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"))).thenReturn(reader).thenReturn(reader);
//		userServiceImpl.getUserByUserId("guestT");
//	}
//}
