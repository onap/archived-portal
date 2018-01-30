/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Unless otherwise specified, all documentation contained herein is licensed
 * under the Creative Commons License, Attribution 4.0 Intl. (the "License");
 * you may not use this documentation except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             https://creativecommons.org/licenses/by/4.0/
 *
 * Unless required by applicable law or agreed to in writing, documentation
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ============LICENSE_END============================================
 *
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
package org.onap.portalapp.portal.controller;

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
import org.onap.portalapp.portal.controller.ConsulClientController;
import org.onap.portalapp.portal.domain.BEProperty;
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.service.ConsulHealthService;
import org.onap.portalapp.portal.service.ConsulHealthServiceImpl;

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
