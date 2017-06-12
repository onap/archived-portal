package org.openecomp.portalapp.portal.test.controller;

import static org.junit.Assert.assertTrue;

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
import org.openecomp.portalapp.portal.controller.ConsulClientController;
import org.openecomp.portalapp.portal.domain.BEProperty;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestResponse;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.openecomp.portalapp.portal.service.ConsulHealthService;
import org.openecomp.portalapp.portal.service.ConsulHealthServiceImpl;
import org.openecomp.portalapp.test.framework.MockitoTestSuite;

import com.orbitz.consul.ConsulException;
import com.orbitz.consul.model.health.ServiceHealth;

import io.searchbox.client.config.exception.NoServerConfiguredException;

public class ConsulClientControllerTest {

	@Mock
	ConsulHealthService consulHealthService = new ConsulHealthServiceImpl();

	@InjectMocks
	ConsulClientController consulClientController = new ConsulClientController();

	NoServerConfiguredException noServerConfiguredException = new NoServerConfiguredException(null);

	String service = "Test";

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();
	ConsulException consulException = new ConsulException(nullPointerException);

	@Test
	public void getServiceLocationTest() {
		PortalRestResponse<BEProperty> ecpectedPortalRestResponse = new PortalRestResponse<BEProperty>();
		ecpectedPortalRestResponse.setMessage("Success!");
		ecpectedPortalRestResponse.setResponse(null);
		ecpectedPortalRestResponse.setStatus(PortalRestStatusEnum.OK);
		PortalRestResponse<String> actualPortalRestRespone = new PortalRestResponse<String>();
		actualPortalRestRespone = consulClientController.getServiceLocation(mockedRequest, mockedResponse, service);
		assertTrue(actualPortalRestRespone.equals(ecpectedPortalRestResponse));
	}

	@Test
	public void getServiceLocationExceptionTest() {
		PortalRestResponse<BEProperty> ecpectedPortalRestResponse = new PortalRestResponse<BEProperty>();
		ecpectedPortalRestResponse.setMessage("Warning!");
		ecpectedPortalRestResponse.setStatus(PortalRestStatusEnum.WARN);
		PortalRestResponse<String> actualPortalRestRespone = new PortalRestResponse<String>();
		Mockito.when(consulHealthService.getServiceLocation(service, null)).thenThrow(noServerConfiguredException);
		actualPortalRestRespone = consulClientController.getServiceLocation(mockedRequest, mockedResponse, service);
		assertTrue(actualPortalRestRespone.getMessage().equals(ecpectedPortalRestResponse.getMessage()));
		assertTrue(actualPortalRestRespone.getStatus().equals(ecpectedPortalRestResponse.getStatus()));

	}

	@Test
	public void getServiceLocationExceptionConsulExceptionTest() {
		PortalRestResponse<BEProperty> ecpectedPortalRestResponse = new PortalRestResponse<BEProperty>();
		ecpectedPortalRestResponse.setMessage("Error!");
		ecpectedPortalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		PortalRestResponse<String> actualPortalRestRespone = new PortalRestResponse<String>();
		Mockito.when(consulHealthService.getServiceLocation(service, null)).thenThrow(consulException);
		actualPortalRestRespone = consulClientController.getServiceLocation(mockedRequest, mockedResponse, service);
		assertTrue(actualPortalRestRespone.getMessage().equals(ecpectedPortalRestResponse.getMessage()));
		assertTrue(actualPortalRestRespone.getStatus().equals(ecpectedPortalRestResponse.getStatus()));
	}

	public PortalRestResponse<List<ServiceHealth>> successResponse() {
		PortalRestResponse<List<ServiceHealth>> ecpectedPortalRestResponse = new PortalRestResponse<List<ServiceHealth>>();
		List<ServiceHealth> healths = new ArrayList<ServiceHealth>();
		ecpectedPortalRestResponse.setMessage("Success!");
		ecpectedPortalRestResponse.setResponse(healths);
		ecpectedPortalRestResponse.setStatus(PortalRestStatusEnum.OK);
		return ecpectedPortalRestResponse;
	}

	public PortalRestResponse<List<ServiceHealth>> errorResponse() {
		PortalRestResponse<List<ServiceHealth>> ecpectedPortalRestResponse = new PortalRestResponse<List<ServiceHealth>>();
		List<ServiceHealth> healths = new ArrayList<ServiceHealth>();
		ecpectedPortalRestResponse.setMessage("Error!");
		ecpectedPortalRestResponse.setResponse(healths);
		ecpectedPortalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		return ecpectedPortalRestResponse;
	}

	@Test
	public void getAllHealthyNodesTest() {
		PortalRestResponse<List<ServiceHealth>> ecpectedPortalRestResponse = successResponse();
		PortalRestResponse<List<ServiceHealth>> actualPortalRestRespone = new PortalRestResponse<List<ServiceHealth>>();
		actualPortalRestRespone = consulClientController.getAllHealthyNodes(mockedRequest, mockedResponse, service);
		assertTrue(actualPortalRestRespone.equals(ecpectedPortalRestResponse));

	}

	@Test
	public void getAllHealthyNodesExceptionTest() {
		PortalRestResponse<List<ServiceHealth>> ecpectedPortalRestResponse = errorResponse();
		PortalRestResponse<List<ServiceHealth>> actualPortalRestRespone = new PortalRestResponse<List<ServiceHealth>>();
		Mockito.when(consulHealthService.getAllHealthyNodes(service)).thenThrow(consulException);
		actualPortalRestRespone = consulClientController.getAllHealthyNodes(mockedRequest, mockedResponse, service);
		assertTrue(actualPortalRestRespone.equals(ecpectedPortalRestResponse));
	}

	@Test
	public void getAllNodesTest() {
		PortalRestResponse<List<ServiceHealth>> ecpectedPortalRestResponse = successResponse();
		PortalRestResponse<List<ServiceHealth>> actualPortalRestRespone = new PortalRestResponse<List<ServiceHealth>>();
		actualPortalRestRespone = consulClientController.getAllNodes(mockedRequest, mockedResponse, service);
		assertTrue(actualPortalRestRespone.equals(ecpectedPortalRestResponse));
	}

	@Test
	public void getAllNodesExceptionTest() {
		PortalRestResponse<List<ServiceHealth>> ecpectedPortalRestResponse = errorResponse();
		PortalRestResponse<List<ServiceHealth>> actualPortalRestRespone = new PortalRestResponse<List<ServiceHealth>>();
		Mockito.when(consulHealthService.getAllNodes(service)).thenThrow(consulException);
		actualPortalRestRespone = consulClientController.getAllNodes(mockedRequest, mockedResponse, service);
		assertTrue(actualPortalRestRespone.equals(ecpectedPortalRestResponse));
	}
}
