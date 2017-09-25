package org.openecomp.portalapp.portal.test.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openecomp.portalapp.portal.controller.AuditLogController;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.test.core.MockEPUser;
import org.openecomp.portalapp.test.framework.MockitoTestSuite;
import org.openecomp.portalapp.util.EPUserUtils;
import org.openecomp.portalsdk.core.service.AuditService;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EPUserUtils.class)
public class AuditLogControllerTest {

	
	@Mock
	AuditService auditService;
	
	@InjectMocks
     AuditLogController auditLogController = new AuditLogController();

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
	public void auditLogTest()
	{
		PowerMockito.mockStatic(EPUserUtils.class);
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(EPUserUtils.getUserId(mockedRequest)).thenReturn((int)1);
		auditLogController.auditLog(mockedRequest, "1", "app", "test");
	}
	
	@Test
	public void auditLogTabTest()
	{
		PowerMockito.mockStatic(EPUserUtils.class);
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(EPUserUtils.getUserId(mockedRequest)).thenReturn((int)1);
		auditLogController.auditLog(mockedRequest, "1", "tab", "test");
	}
	
	@Test
	public void auditLogfunctionalTest()
	{
		PowerMockito.mockStatic(EPUserUtils.class);
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(EPUserUtils.getUserId(mockedRequest)).thenReturn((int)1);
		auditLogController.auditLog(mockedRequest, "1", "functional", "test");
	}
	
	@Test
	public void auditLogleftMenuTest()
	{
		PowerMockito.mockStatic(EPUserUtils.class);
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(EPUserUtils.getUserId(mockedRequest)).thenReturn((int)1);
		auditLogController.auditLog(mockedRequest, "1", "leftMenu", "test");
	}
	
	@Test(expected = NumberFormatException.class)
	public void auditLogExceptionTest()
	{
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(EPUserUtils.getUserId(mockedRequest)).thenReturn((int)1);
		auditLogController.auditLog(mockedRequest, "1", "app", "test");
	}
	
	@Test
	public void auditLogerrorTest()
	{
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenThrow(nullPointerException);
		auditLogController.auditLog(mockedRequest, "1", "app", "test");
	}
}
