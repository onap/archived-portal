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

import static org.junit.Assert.assertTrue;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openecomp.portalapp.portal.framework.MockitoTestSuite;
import org.openecomp.portalapp.portal.controller.BEPropertyReaderController;
import org.openecomp.portalapp.portal.domain.BEProperty;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestResponse;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SystemProperties.class)
public class BEPropertyReaderControllerTest extends MockitoTestSuite {

	@InjectMocks
	BEPropertyReaderController bEPropertyReaderController = new BEPropertyReaderController();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();

	NullPointerException nullPointerException = new NullPointerException();

	@Test
	public void readPropertyTest() {
		String key = "DOMAIN_CLASS_LOCATION";
		BEProperty beProperty = new BEProperty("DOMAIN_CLASS_LOCATION", "domain_class_location");
		PortalRestResponse<BEProperty> ecpectedPortalRestResponse = new PortalRestResponse<BEProperty>();
		ecpectedPortalRestResponse.setMessage("success");
		ecpectedPortalRestResponse.setResponse(beProperty);
		ecpectedPortalRestResponse.setStatus(PortalRestStatusEnum.OK);
		PortalRestResponse<BEProperty> actualPortalRestResponse = null;
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty("DOMAIN_CLASS_LOCATION")).thenReturn("domain_class_location");
		actualPortalRestResponse = bEPropertyReaderController.readProperty(mockedRequest, key);
		assertTrue(actualPortalRestResponse.equals(ecpectedPortalRestResponse));

	}
	
	@Test
	public void readPropertyExceptionTest() {
		String key =null;
		//BEProperty beProperty = new BEProperty("DOMAIN_CLASS_LOCATION", "domain_class_location");
		PortalRestResponse<BEProperty> ecpectedPortalRestResponse = new PortalRestResponse<BEProperty>();
		ecpectedPortalRestResponse.setMessage("java.lang.NullPointerException");
		ecpectedPortalRestResponse.setResponse(null);
		ecpectedPortalRestResponse.setStatus(PortalRestStatusEnum.ERROR);
		PortalRestResponse<BEProperty> actualPortalRestResponse = null;
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(new BEProperty(key, SystemProperties.getProperty(key))).thenThrow(nullPointerException);
		actualPortalRestResponse = bEPropertyReaderController.readProperty(mockedRequest, key);
		assertTrue(actualPortalRestResponse.equals(ecpectedPortalRestResponse));

	}
}
