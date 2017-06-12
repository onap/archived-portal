/*-
 * ================================================================================
 * ECOMP Portal
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ================================================================================
 */
package org.openecomp.portalapp.test.framework;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MockitoTestSuite {


	public MockHttpServletRequestWrapper mockedRequest = new MockHttpServletRequestWrapper(
			Mockito.mock(HttpServletRequest.class));
	public HttpServletResponse mockedResponse = Mockito.mock(HttpServletResponse.class);

	public MockHttpServletRequestWrapper getMockedRequest() {
		return mockedRequest;
	}

	public HttpServletResponse getMockedResponse() {
		return mockedResponse;
	}

	public class MockHttpServletRequestWrapper extends HttpServletRequestWrapper {

		HttpSession session = Mockito.mock(HttpSession.class);

		public MockHttpServletRequestWrapper(HttpServletRequest request) {
			super(request);

		}

		@Override
		public HttpSession getSession() {

			return session;
		}

		@Override
		public HttpSession getSession(boolean create) {

			return session;
		}

	}

}
