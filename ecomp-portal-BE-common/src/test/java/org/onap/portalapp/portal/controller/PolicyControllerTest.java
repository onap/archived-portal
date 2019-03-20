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
 * 
 */
package org.onap.portalapp.portal.controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.scheduler.SchedulerProperties;
import org.onap.portalapp.portal.scheduler.SchedulerRestInterface;
import org.onap.portalapp.portal.scheduleraux.RestObject;
import org.onap.portalapp.portal.scheduleraux.SchedulerAuxResponseWrapper;
import org.onap.portalapp.portal.scheduleraux.SchedulerAuxRestInterfaceFactory;
import org.onap.portalapp.portal.scheduleraux.SchedulerAuxRestInterfaceIfc;
import org.onap.portalapp.portal.scheduleraux.SchedulerAuxUtil;
import org.onap.portalsdk.core.web.support.UserUtils;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@RunWith(PowerMockRunner.class)
@PrepareForTest({ UserUtils.class, SchedulerProperties.class, SchedulerAuxRestInterfaceFactory.class,SchedulerAuxUtil.class })
public class PolicyControllerTest {
	@Mock
	SchedulerRestInterface schedulerRestInterface;

	@InjectMocks
	SchedulerAuxController policyController = new SchedulerAuxController();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	MockEPUser mockUser = new MockEPUser();
	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();

	@Before
	public void setUp() {

		PowerMockito.mockStatic(SchedulerProperties.class);

		Mockito.when(SchedulerProperties.getProperty(SchedulerProperties.SCHEDULERAUX_GET_CONFIG_VAL)).thenReturn("/api/getConfig");
		Mockito.when(SchedulerProperties.getProperty(SchedulerProperties.SCHEDULERAUX_CLIENT_MECHID_VAL))
				.thenReturn("m06814@controller.dcae.ecomp.att.com");
		Mockito.when(SchedulerProperties.getProperty(SchedulerProperties.SCHEDULERAUX_CLIENT_PASSWORD_VAL))
				.thenReturn("OBF:1ffu1qvu1t2z1l161fuk1i801nz91ro41xf71xfv1rqi1nx51i7y1fuq1kxw1t371qxw1fh0");
		Mockito.when(SchedulerProperties.getProperty(SchedulerProperties.SCHEDULERAUX_USERNAME_VAL)).thenReturn("testpdp");
		Mockito.when(SchedulerProperties.getProperty(SchedulerProperties.SCHEDULERAUX_PASSWORD_VAL))
				.thenReturn("OBF:1igd1kft1l1a1sw61svs1kxs1kcl1idt");
		Mockito.when(SchedulerProperties.getProperty(SchedulerProperties.SCHEDULERAUX_ENVIRONMENT_VAL)).thenReturn("TEST");
		Mockito.when(SchedulerProperties.getProperty(SchedulerProperties.SCHEDULERAUX_SERVER_URL_VAL))
				.thenReturn("https://policypdp-conexus-e2e.ecomp.cci.att.com:8081/pdp");

	}

	@SuppressWarnings({ "unchecked" })
	@Test
	public void getPolicyInfoTest1() throws Exception {

		JSONObject jsonObject = Mockito.mock(JSONObject.class);
		PowerMockito.mockStatic(SchedulerAuxRestInterfaceFactory.class);
		PowerMockito.mockStatic(SchedulerProperties.class);
		SchedulerAuxRestInterfaceIfc policyRestInterface = Mockito.mock(SchedulerAuxRestInterfaceIfc.class);
		PowerMockito.mockStatic(SchedulerAuxUtil.class);
		// RestObject restObj=Mockito.mock(RestObject.class);
		SchedulerAuxResponseWrapper policyWrapper = Mockito.mock(SchedulerAuxResponseWrapper.class);
		PowerMockito.when(SchedulerAuxUtil.wrapResponse(Matchers.any(RestObject.class))).thenReturn(policyWrapper);
		Mockito.when(policyWrapper.getResponse()).thenReturn("Success");
		Mockito.when(policyWrapper.getStatus()).thenReturn(200);

		PowerMockito.when(SchedulerAuxRestInterfaceFactory.getInstance()).thenReturn(policyRestInterface);
		Mockito.doNothing().when(policyRestInterface).Post(Matchers.anyString(), Matchers.anyObject(),
				Matchers.anyString(), Matchers.anyString(), Matchers.anyObject());

		ResponseEntity<String> responsePolicy = policyController.getPolicyInfo(mockedRequest);
		Assert.assertEquals(responsePolicy.getStatusCode(), HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getPolicyInfoTestexpected() throws Exception {

		JSONObject jsonObject = Mockito.mock(JSONObject.class);
		PowerMockito.mockStatic(SchedulerAuxRestInterfaceFactory.class);
		PowerMockito.mockStatic(SchedulerProperties.class);
		Mockito.when(SchedulerProperties.getProperty(SchedulerProperties.SCHEDULERAUX_GET_CONFIG_VAL)).thenThrow(nullPointerException);
		ResponseEntity<String> responsePolicy = policyController.getPolicyInfo(mockedRequest);
		Assert.assertEquals(responsePolicy.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);

	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void getPolicyInfoTestException() throws Exception {

		JSONObject jsonObject = Mockito.mock(JSONObject.class);
		PowerMockito.mockStatic(SchedulerAuxRestInterfaceFactory.class);
		PowerMockito.mockStatic(SchedulerProperties.class);
		SchedulerAuxRestInterfaceIfc policyRestInterface = Mockito.mock(SchedulerAuxRestInterfaceIfc.class);
		PowerMockito.mockStatic(SchedulerAuxUtil.class);
		// RestObject restObj=Mockito.mock(RestObject.class);
		SchedulerAuxResponseWrapper policyWrapper = Mockito.mock(SchedulerAuxResponseWrapper.class);
		PowerMockito.when(SchedulerAuxUtil.wrapResponse(Matchers.any(RestObject.class))).thenReturn(policyWrapper);
		Mockito.when(policyWrapper.getResponse()).thenReturn("Success");
		Mockito.when(policyWrapper.getStatus()).thenReturn(200);

		PowerMockito.when(SchedulerAuxRestInterfaceFactory.getInstance()).thenReturn(policyRestInterface);
		/*Mockito.doNothing().when(policyRestInterface).Post(Matchers.anyString(), Matchers.anyObject(),
				Matchers.anyString(), Matchers.anyString(), Matchers.anyObject());*/
		Mockito.doThrow(new NullPointerException()).when(policyRestInterface).Post(Matchers.anyString(), Matchers.anyObject(),
				Matchers.anyString(), Matchers.anyString(), Matchers.anyObject());


		ResponseEntity<String> responsePolicy = policyController.getPolicyInfo(mockedRequest);
		Assert.assertEquals(responsePolicy.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getPolicyInfoTestException1() throws Exception {

		JSONObject jsonObject = Mockito.mock(JSONObject.class);
		PowerMockito.mockStatic(SchedulerAuxRestInterfaceFactory.class);
		PowerMockito.mockStatic(SchedulerProperties.class);
		SchedulerAuxRestInterfaceIfc policyRestInterface = Mockito.mock(SchedulerAuxRestInterfaceIfc.class);
		PowerMockito.mockStatic(SchedulerAuxUtil.class);
		// RestObject restObj=Mockito.mock(RestObject.class);
		SchedulerAuxResponseWrapper policyWrapper = Mockito.mock(SchedulerAuxResponseWrapper.class);
		PowerMockito.when(SchedulerAuxUtil.wrapResponse(Matchers.any(RestObject.class))).thenReturn(policyWrapper);
		Mockito.when(policyWrapper.getResponse()).thenReturn("Bad Request");
		Mockito.when(policyWrapper.getStatus()).thenReturn(400);

		PowerMockito.when(SchedulerAuxRestInterfaceFactory.getInstance()).thenReturn(policyRestInterface);
		Mockito.doNothing().when(policyRestInterface).Post(Matchers.anyString(), Matchers.anyObject(),
				Matchers.anyString(), Matchers.anyString(), Matchers.anyObject());
		/*Mockito.doThrow(new NullPointerException()).when(policyRestInterface).Post(Matchers.anyString(), Matchers.anyObject(),
				Matchers.anyString(), Matchers.anyString(), Matchers.anyObject());*/


		ResponseEntity<String> responsePolicy = policyController.getPolicyInfo(mockedRequest);
		Assert.assertEquals(responsePolicy.getStatusCode(), HttpStatus.BAD_REQUEST);
	}

}
