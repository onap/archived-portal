/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright © 2017-2018 AT&T Intellectual Property. All rights reserved.
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
package org.onap.portalapp.filter;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.onap.portalsdk.core.util.SystemProperties;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.codecs.Codec;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ESAPI.class, SystemProperties.class})
public class SecurityXssValidatorTest {
	@InjectMocks
	SecurityXssValidator securityXssValidator;

	@Test
	public void stripXSSTest() {
	 securityXssValidator=	SecurityXssValidator.getInstance();
		String value ="Test";
		securityXssValidator.stripXSS(value);
	}
	
	@Test
	public void testDenyXss() {
	 securityXssValidator=	SecurityXssValidator.getInstance();
		String value ="Test";
		securityXssValidator.denyXSS(value);
	}
	
	@Test
		public void getCodecMySqlTest() {
			PowerMockito.mockStatic(SystemProperties.class);
			Mockito.when(SystemProperties.getProperty(SystemProperties.DB_DRIVER)).thenReturn("mysql");
			SecurityXssValidator validator = SecurityXssValidator.getInstance();
			Codec codec = validator.getCodec();
			Assert.assertNotNull(codec);
		}
	
	/*//@Test
	public void stripXSSExceptionTest() {
		String value ="Test";
		SecurityXssValidator validator = SecurityXssValidator.getInstance();
		String reponse = validator.stripXSS(value);
		Assert.assertEquals(value, reponse);;
	}
	
	//@Test
	public void denyXSSTest() {
		String value ="<script>Test</script>";
		PowerMockito.mockStatic(ESAPI.class);
		Encoder mockEncoder = Mockito.mock(Encoder.class);
		Mockito.when(ESAPI.encoder()).thenReturn(mockEncoder);
		Mockito.when(mockEncoder.canonicalize(value)).thenReturn(value);
		SecurityXssValidator validator = SecurityXssValidator.getInstance();
		Boolean flag = validator.denyXSS(value);
		Assert.assertTrue(flag);
	}
	
	//@Test
	public void denyXSSFalseTest() {
		String value ="test";
		PowerMockito.mockStatic(ESAPI.class);
		Encoder mockEncoder = Mockito.mock(Encoder.class);
		Mockito.when(ESAPI.encoder()).thenReturn(mockEncoder);
		Mockito.when(mockEncoder.canonicalize(value)).thenReturn(value);
		SecurityXssValidator validator = SecurityXssValidator.getInstance();
		Boolean flag = validator.denyXSS(value);
		Assert.assertFalse(flag);
	}

	//@Test
	public void getCodecMySqlTest() {
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(SystemProperties.DB_DRIVER)).thenReturn("mysql");
		SecurityXssValidator validator = SecurityXssValidator.getInstance();
		Codec codec = validator.getCodec();
		Assert.assertNotNull(codec);
	}*/
				
}
