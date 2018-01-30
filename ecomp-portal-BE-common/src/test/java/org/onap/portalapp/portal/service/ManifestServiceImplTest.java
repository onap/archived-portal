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
package org.onap.portalapp.portal.service;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;

import javax.servlet.ServletContext;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.service.ManifestServiceImpl;

public class ManifestServiceImplTest {

	@Mock
	ServletContext context;

	@Mock
	ServletContext context1 = null;

	@InjectMocks
	ManifestServiceImpl manifestServiceImpl = new ManifestServiceImpl();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	NullPointerException nullPointerException = new NullPointerException();

	@Test
	public void getWebappManifestTest() throws IOException {
		final String MANIFEST_RESOURCE_PATH = "/META-INF/MANIFEST.MF";
		InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
		Mockito.when(context.getResourceAsStream(MANIFEST_RESOURCE_PATH)).thenReturn(inputStream);
		Attributes attributes = manifestServiceImpl.getWebappManifest();
		assertTrue(attributes.size() == 0);
	}

	@Test(expected = java.lang.NullPointerException.class)
	public void getWebappManifestExceptionTest() throws IOException {
		final String MANIFEST_RESOURCE_PATH = "/META-INF/MANIFEST.MF";
		InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
		Mockito.when(context1.getResourceAsStream(MANIFEST_RESOURCE_PATH)).thenThrow(nullPointerException);
		Attributes attributes = manifestServiceImpl.getWebappManifest();
		assertTrue(attributes.size() == 0);
	}
}
