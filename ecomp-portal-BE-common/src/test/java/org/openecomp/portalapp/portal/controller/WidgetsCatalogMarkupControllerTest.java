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
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openecomp.portalapp.portal.domain.WidgetServiceHeaders;
import org.openecomp.portalapp.portal.framework.MockitoTestSuite;
import org.openecomp.portalapp.portal.service.ConsulHealthService;
import org.openecomp.portalapp.portal.service.ConsulHealthServiceImpl;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalsdk.core.onboarding.util.CipherUtil;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RunWith(PowerMockRunner.class)
@PrepareForTest({WidgetServiceHeaders.class, EcompPortalUtils.class})
public class WidgetsCatalogMarkupControllerTest extends MockitoTestSuite {
	
	@InjectMocks
	WidgetsCatalogMarkupController widgetsCatalogMarkupController = new WidgetsCatalogMarkupController();
	
	@Mock
	ConsulHealthService consulHealthService = new ConsulHealthServiceImpl();
	
	@Mock
	RestTemplate template = new RestTemplate();
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
    @Mock
    CipherUtil cipherUtil= new CipherUtil();
    
    @Mock
    EcompPortalUtils ecompPortalUtils =new EcompPortalUtils();
	
	@Mock
	WidgetServiceHeaders WidgetServiceHeaders ;
	
	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();

	@SuppressWarnings("static-access")
	@Test
	public void getWidgetMarkupTest() throws RestClientException, Exception
	{
		String whatService = "widgets-service";
		PowerMockito.mockStatic(WidgetServiceHeaders.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		Mockito.when(template.getForObject(EcompPortalUtils.widgetMsProtocol() + "://" + consulHealthService.getServiceLocation(whatService, null) + "/widget/microservices/markup/" + 1, String.class,
				WidgetServiceHeaders.getInstance())).thenReturn("Success");
		String response = widgetsCatalogMarkupController.getWidgetMarkup(mockedRequest, mockedResponse, 1);
		assertTrue(response.equals("Success"));	
	}
	
}
