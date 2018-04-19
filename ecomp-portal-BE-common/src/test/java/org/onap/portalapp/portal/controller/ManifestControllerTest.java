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

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.controller.ManifestController;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.service.ManifestService;
import org.onap.portalapp.portal.service.ManifestServiceImpl;

public class ManifestControllerTest extends MockitoTestSuite{

	
	@Mock
	ManifestService manifestService = new ManifestServiceImpl();
	
	@InjectMocks
	ManifestController  manifestController = new  ManifestController();
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	NullPointerException nullPointerException = new NullPointerException();
	
	@Test
	public void getManifestTest() throws IOException
	{
		Map<String, Object> expectedResponse = new HashMap<String, Object>();
		Attributes attributes  = new Attributes();
		expectedResponse.put("test", attributes);
		Mockito.when(manifestService.getWebappManifest()).thenReturn(attributes);
		Map<String, Object> actualResponse = manifestController.getManifest(mockedRequest);
		assertTrue((actualResponse.keySet().toArray().length) == 1);
		
	}
	
	@Test
	public void getManifestExceptionTest() throws IOException
	{
		Mockito.when(manifestService.getWebappManifest()).thenThrow(nullPointerException);
		Map<String, Object> actualResponse = manifestController.getManifest(mockedRequest);
		assertTrue((actualResponse.keySet().toArray().length) == 1);
		assertTrue(actualResponse.get("error").equals("failed to get manifest: java.lang.NullPointerException"));
	}
}
