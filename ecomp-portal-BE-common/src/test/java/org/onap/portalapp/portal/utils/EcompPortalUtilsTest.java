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
package org.onap.portalapp.portal.utils;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.framework.MockitoTestSuite;

public class EcompPortalUtilsTest {

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	MockEPUser mockUser = new MockEPUser();

	
	@Test
	public void legitimateAttuidSuccessTest() {
		assertEquals(true, EcompPortalUtils.legitimateUserId("mm016f"));
	}

	@Test
	public void legitimateAttuidFailureTest() {
		assertEquals(false, EcompPortalUtils.legitimateUserId("1#@23456"));
	}
	
	
	@Test
	public void parsingByRegularExpressionTest() {
		List<String> expected =  new ArrayList<>();
		expected.add("test");
		expected.add("123");
		assertEquals(expected,EcompPortalUtils.parsingByRegularExpression("test 123"," "));
	}
	
	@Test 
	public void jsonErrorMessageResponseTest() {
		String expected = "{\"error\":{\"code\":" + 200 + "," + "\"message\":\"" + "test" + "\"}}";
		assertEquals(expected,EcompPortalUtils.jsonErrorMessageResponse(200, "test"));
	}
	
	@Test 
	public void jsonMessageResponseTest() {
		String expected = "{\"message\":\"test\"}";
		assertEquals(expected,EcompPortalUtils.jsonMessageResponse("test"));
	}
	
	@Test
	public void logAndSerializeObjectTest() {
		EcompPortalUtils.logAndSerializeObject("test", "test", EcompPortalUtils.class);
	}
	
	@Test
	public void setBadPermissionsForEmptyUserTest() {
		EcompPortalUtils.setBadPermissions(new EPUser(), mockedResponse, "test");
	}

}
