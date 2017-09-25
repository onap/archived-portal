package org.openecomp.portalapp.portal.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openecomp.portalsdk.core.util.SystemProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openecomp.portalapp.portal.domain.EPApp;
import org.openecomp.portalapp.portal.framework.MockitoTestSuite;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalapp.service.RemoteWebServiceCallServiceImpl;
import org.openecomp.portalsdk.core.onboarding.util.CipherUtil;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ CipherUtil.class , SystemProperties.class})
public class RemoteWebServiceCallServiceImplTest {
	

	@InjectMocks
	RemoteWebServiceCallServiceImpl remoteWebServiceCallServiceImpl = new RemoteWebServiceCallServiceImpl();

	@Mock
	AppsCacheService appCacheService;
	
	@Mock
	DataAccessService dataAccessService;
	

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();

	NullPointerException nullPointerException = new NullPointerException();
	
	@Test
	public void verifyRESTCredentialTest() throws Exception
	{
		PowerMockito.mockStatic(CipherUtil.class);
		PowerMockito.mockStatic(SystemProperties.class);
		String criteria= " where ueb_key = 'requestUebKey'";
		List<EPApp> appList = new ArrayList<>();
		EPApp app = new EPApp();
		app.setAppPassword("password");
		appList.add(app);
		Mockito.when(dataAccessService.getList(EPApp.class, criteria.toString(), null, null)).thenReturn(appList);
		String secretKey = null;
		Mockito.when(SystemProperties.getProperty(SystemProperties.Decryption_Key)).thenReturn(secretKey);
		Mockito.when(CipherUtil.decrypt("password",
				secretKey == null ? null : secretKey)).thenReturn("pwd");
		assertFalse(remoteWebServiceCallServiceImpl.verifyRESTCredential(secretKey,"requestUebKey","requestAppName","requestPassword"));
	}
	
	@Test
	public void verifyRESTCredentialExceptionTest() throws Exception
	{
		PowerMockito.mockStatic(CipherUtil.class);
		PowerMockito.mockStatic(SystemProperties.class);
		String criteria= " where ueb_key = 'requestUebKey'";
		List<EPApp> appList = new ArrayList<>();
		EPApp app = new EPApp();
		app.setAppPassword("password");
		app.setUsername("requestAppName");
		appList.add(app);
		Mockito.when(dataAccessService.getList(EPApp.class, criteria.toString(), null, null)).thenReturn(appList);
		String secretKey = null;
		Mockito.when(SystemProperties.getProperty(SystemProperties.Decryption_Key)).thenReturn(secretKey);
		Mockito.when(CipherUtil.decrypt("password",
				secretKey == null ? null : secretKey)).thenReturn("pwd");
		assertTrue(remoteWebServiceCallServiceImpl.verifyRESTCredential(secretKey,"requestUebKey","requestAppName","pwd"));
	}
	
	@Test
	public void verifyRESTCredentialIfAppNullTest() throws Exception
	{
		PowerMockito.mockStatic(CipherUtil.class);
		PowerMockito.mockStatic(SystemProperties.class);
		String criteria= " where ueb_key = 'requestUebKey'";
		List<EPApp> appList = new ArrayList<>();
		EPApp app = new EPApp();
		app.setAppPassword("password");
		app.setUsername("requestAppName");
		appList.add(app);
		Mockito.when(dataAccessService.getList(EPApp.class, criteria.toString(), null, null)).thenReturn(null);
		String secretKey = null;
		Mockito.when(SystemProperties.getProperty(SystemProperties.Decryption_Key)).thenReturn(secretKey);
		Mockito.when(CipherUtil.decrypt("password",
				secretKey == null ? null : secretKey)).thenReturn("pwd");
		assertFalse(remoteWebServiceCallServiceImpl.verifyRESTCredential(secretKey,"requestUebKey","requestAppName","pwd"));
	}
	
	@Test
	public void verifyAppKeyCredentialIfKeyIsNullTest() throws Exception
	{
		assertFalse(remoteWebServiceCallServiceImpl.verifyAppKeyCredential(null));
	}
	
	@Test
	public void verifyAppKeyCredentialTest() throws Exception
	{
		PowerMockito.mockStatic(CipherUtil.class);
		PowerMockito.mockStatic(SystemProperties.class);
		StringBuffer criteria = new  StringBuffer("where ueb_key = 'requestUebKey'");
//		String criteria= " where ueb_key = 'requestUebKey'";
		List<EPApp> appList = new ArrayList<>();
		EPApp app = new EPApp();
		app.setAppPassword("password");
		app.setUsername("requestAppName");
		appList.add(app);
		Mockito.when(dataAccessService.getList(EPApp.class, criteria.toString(), null, null)).thenReturn(null);
		assertFalse(remoteWebServiceCallServiceImpl.verifyAppKeyCredential("test"));
	}
	
	@Test
	public void verifyAppKeyCredentialSuccessTest() throws Exception
	{
		PowerMockito.mockStatic(CipherUtil.class);
		PowerMockito.mockStatic(SystemProperties.class);
		String criteria= " where ueb_key = 'test'";
		List<EPApp> appList = new ArrayList<>();
		EPApp app = new EPApp();
		app.setAppPassword("password");
		app.setUsername("requestAppName");
		appList.add(app);
		Mockito.when(dataAccessService.getList(EPApp.class, criteria.toString(), null, null)).thenReturn(appList);
		assertTrue(remoteWebServiceCallServiceImpl.verifyAppKeyCredential("test"));
	}
}
