package org.openecomp.portalapp.portal.controller;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
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
import org.openecomp.portalapp.portal.framework.MockitoTestSuite;
import org.openecomp.portalapp.portal.controller.GetAccessController;
import org.openecomp.portalapp.portal.core.MockEPUser;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.domain.GetAccessResult;
import org.openecomp.portalapp.portal.service.GetAccessService;
import org.openecomp.portalapp.portal.service.GetAccessServiceImpl;
import org.openecomp.portalapp.util.EPUserUtils;

public class GetAccessControllerTest {

	@Mock
	GetAccessService getAccessService = new GetAccessServiceImpl();

	@InjectMocks
	GetAccessController getAccessController = new GetAccessController();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	MockEPUser mockUser = new MockEPUser();
	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();
	
	
	@Test
	public void getAppList() throws IOException
	{
		List<GetAccessResult> expectedAppsList = new ArrayList<GetAccessResult>();
		EPUser user = mockUser.mockEPUser();
		GetAccessResult getAccessResult = new GetAccessResult();
		getAccessResult.setRowId("1");
		getAccessResult.setRoleId((long) 1);
		getAccessResult.setEcompFunction("test");
		getAccessResult.setAppName("Test_App");
		getAccessResult.setAppMotsId(1);
		getAccessResult.setRoleName("Test_role");
		getAccessResult.setRoleActive("N");
		getAccessResult.setReqType("test");
		
		expectedAppsList.add(getAccessResult);
		
		List<GetAccessResult> actualAppsList = null;
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(getAccessService.getAppAccessList(user)).thenReturn(expectedAppsList);
		actualAppsList = getAccessController.getAppList(mockedRequest);
		assertTrue(actualAppsList.contains(getAccessResult));
	}

}
