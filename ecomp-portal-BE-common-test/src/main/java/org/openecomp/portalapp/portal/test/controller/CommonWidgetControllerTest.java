package org.openecomp.portalapp.portal.test.controller;

import static org.junit.Assert.assertTrue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openecomp.portalapp.portal.controller.CommonWidgetController;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestResponse;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.openecomp.portalapp.portal.service.DashboardSearchService;
import org.openecomp.portalapp.portal.service.DashboardSearchServiceImpl;
import org.openecomp.portalapp.portal.transport.CommonWidgetMeta;
import org.openecomp.portalapp.test.framework.MockitoTestSuite;

public class CommonWidgetControllerTest {

	@Mock
	DashboardSearchService dashboardSearchService = new DashboardSearchServiceImpl();

	@InjectMocks
	CommonWidgetController commonWidgetController = new CommonWidgetController();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();

	@SuppressWarnings("unchecked")
	@Test
	public void getWidgetDataTest() {
		String resourceType = "Test";
		PortalRestResponse<CommonWidgetMeta> acutualPoratlRestResponse = null;
		@SuppressWarnings("rawtypes")
		PortalRestResponse ecpectedPortalRestResponse = new PortalRestResponse();
		ecpectedPortalRestResponse.setMessage("Unexpected resource type Test");
		ecpectedPortalRestResponse.setResponse(null);
		ecpectedPortalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		acutualPoratlRestResponse = commonWidgetController.getWidgetData(mockedRequest, resourceType);
		assertTrue(acutualPoratlRestResponse.equals(ecpectedPortalRestResponse));

	}

	@SuppressWarnings("unchecked")
	@Test
	public void getWidgetDataTestNew() {
		String resourceType = "EVENTS";
		PortalRestResponse<CommonWidgetMeta> acutualPoratlRestResponse = null;
		@SuppressWarnings("rawtypes")
		PortalRestResponse ecpectedPortalRestResponse = new PortalRestResponse();
		ecpectedPortalRestResponse.setMessage("success");
		ecpectedPortalRestResponse.setResponse(null);
		ecpectedPortalRestResponse.setStatus(PortalRestStatusEnum.OK);
		acutualPoratlRestResponse = commonWidgetController.getWidgetData(mockedRequest, resourceType);
		assertTrue(acutualPoratlRestResponse.equals(ecpectedPortalRestResponse));

	}

	@SuppressWarnings("unchecked")
	@Test
	public void getWidgetDataExceptionTest() {
		String resourceType = "null";
		PortalRestResponse<CommonWidgetMeta> acutualPoratlRestResponse = null;
		@SuppressWarnings("rawtypes")
		PortalRestResponse ecpectedPortalRestResponse = new PortalRestResponse();
		ecpectedPortalRestResponse.setMessage("Unexpected resource type null");
		ecpectedPortalRestResponse.setResponse(null);
		ecpectedPortalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		acutualPoratlRestResponse = commonWidgetController.getWidgetData(mockedRequest, resourceType);
		assertTrue(acutualPoratlRestResponse.equals(ecpectedPortalRestResponse));

	}
}
