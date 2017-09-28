/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright © 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the “License”);
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
 * under the Creative Commons License, Attribution 4.0 Intl. (the “License”);
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
package org.openecomp.portalapp.portal.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BadRequestException;

import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openecomp.portalapp.portal.core.MockEPUser;
import org.openecomp.portalapp.portal.framework.MockitoTestSuite;
import org.openecomp.portalapp.portal.scheduler.SchedulerRestInterface;
import org.openecomp.portalapp.portal.scheduler.policy.PolicyProperties;
import org.openecomp.portalapp.portal.scheduler.policy.PolicyResponseWrapper;
import org.openecomp.portalapp.portal.scheduler.policy.PolicyRestInterfaceFactory;
import org.openecomp.portalapp.portal.scheduler.policy.PolicyRestInterfaceIfc;
import org.openecomp.portalapp.portal.scheduler.policy.PolicyUtil;
import org.openecomp.portalapp.portal.scheduler.policy.RestObject;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.openecomp.portalsdk.core.web.support.UserUtils;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import junit.framework.Assert;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ UserUtils.class, SystemProperties.class, PolicyProperties.class, PolicyRestInterfaceFactory.class,
		PolicyUtil.class })
public class PolicyControllerTest {

	@Mock
	SchedulerRestInterface schedulerRestInterface;

	@InjectMocks
	PolicyController policyController = new PolicyController();

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

		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(PolicyProperties.class);
		Mockito.when(SystemProperties.getProperty(PolicyProperties.POLICY_GET_CONFIG_VAL)).thenReturn("/api/getConfig");
		Mockito.when(SystemProperties.getProperty(PolicyProperties.POLICY_CLIENT_MECHID_VAL))
				.thenReturn("m06814@controller.dcae.ecomp.att.com");
		Mockito.when(SystemProperties.getProperty(PolicyProperties.POLICY_CLIENT_PASSWORD_VAL))
				.thenReturn("OBF:1ffu1qvu1t2z1l161fuk1i801nz91ro41xf71xfv1rqi1nx51i7y1fuq1kxw1t371qxw1fh0");
		Mockito.when(SystemProperties.getProperty(PolicyProperties.POLICY_USERNAME_VAL)).thenReturn("testpdp");
		Mockito.when(SystemProperties.getProperty(PolicyProperties.POLICY_PASSWORD_VAL))
				.thenReturn("OBF:1igd1kft1l1a1sw61svs1kxs1kcl1idt");
		Mockito.when(SystemProperties.getProperty(PolicyProperties.POLICY_ENVIRONMENT_VAL)).thenReturn("TEST");
		Mockito.when(SystemProperties.getProperty(PolicyProperties.POLICY_SERVER_URL_VAL))
				.thenReturn("https://policypdp-conexus-e2e.ecomp.cci.att.com:8081/pdp");

	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Test
	public void getPolicyInfoTest1() throws Exception {

		JSONObject jsonObject = Mockito.mock(JSONObject.class);
		PowerMockito.mockStatic(PolicyRestInterfaceFactory.class);
		PowerMockito.mockStatic(SystemProperties.class);
		PolicyRestInterfaceIfc policyRestInterface = Mockito.mock(PolicyRestInterfaceIfc.class);
		PowerMockito.mockStatic(PolicyUtil.class);
		// RestObject restObj=Mockito.mock(RestObject.class);
		PolicyResponseWrapper policyWrapper = Mockito.mock(PolicyResponseWrapper.class);
		PowerMockito.when(PolicyUtil.wrapResponse(Matchers.any(RestObject.class))).thenReturn(policyWrapper);
		Mockito.when(policyWrapper.getResponse()).thenReturn("Success");
		Mockito.when(policyWrapper.getStatus()).thenReturn(200);

		PowerMockito.when(PolicyRestInterfaceFactory.getInstance()).thenReturn(policyRestInterface);
		Mockito.doNothing().when(policyRestInterface).Post(Matchers.anyString(), Matchers.anyObject(),
				Matchers.anyString(), Matchers.anyString(), Matchers.anyObject());

		ResponseEntity<String> responsePolicy = policyController.getPolicyInfo(mockedRequest, jsonObject);
		Assert.assertEquals(responsePolicy.getStatusCode(), HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	@Test(expected = Exception.class)
	public void getPolicyInfoTestexpected() throws Exception {

		JSONObject jsonObject = Mockito.mock(JSONObject.class);
		PowerMockito.mockStatic(PolicyRestInterfaceFactory.class);
		PowerMockito.mockStatic(SystemProperties.class);
		PolicyRestInterfaceIfc policyRestInterface = Mockito.mock(PolicyRestInterfaceIfc.class);
		PowerMockito.mockStatic(PolicyUtil.class);
		// RestObject restObj=Mockito.mock(RestObject.class);
		PolicyResponseWrapper policyWrapper = Mockito.mock(PolicyResponseWrapper.class);
		PowerMockito.when(PolicyUtil.wrapResponse(Matchers.any(RestObject.class))).thenReturn(policyWrapper);
		Mockito.when(policyWrapper.getResponse()).thenThrow(new BadRequestException());
		Mockito.when(policyWrapper.getStatus()).thenThrow(new BadRequestException());

		PowerMockito.when(PolicyRestInterfaceFactory.getInstance()).thenReturn(policyRestInterface);
		Mockito.doNothing().when(policyRestInterface).Post(Matchers.anyString(), Matchers.anyObject(),
				Matchers.anyString(), Matchers.anyString(), Matchers.anyObject());

		policyController.getPolicyInfo(mockedRequest, jsonObject);
	}

}
