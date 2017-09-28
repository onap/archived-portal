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

import static org.junit.Assert.assertEquals;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openecomp.portalapp.controller.ONAPWelcomeController;
import org.openecomp.portalapp.portal.framework.MockitoTestSuite;
import org.springframework.web.servlet.ModelAndView;

public class ONAPWelcomeControllerTest {

	@InjectMocks
	ONAPWelcomeController oNAPWelcomeController = new ONAPWelcomeController();
	
	@Mock
	Principal p;
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();
	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();
	
	@Test
	public void getIndexPageTest()
	{
		assertEquals(oNAPWelcomeController.getIndexPage(mockedRequest), "/index");
	}
	
	@Test
	public void getEcompSinglePageTest()
	{
		assertEquals(oNAPWelcomeController.getEcompSinglePage(mockedRequest, mockedResponse), "forward:/index.html");
	}
	
	@Test
	public void userTest()
	{
		assertEquals(oNAPWelcomeController.user(null), "oid-user");
	}
	
	@Test
	public void loginTest()
	{
		ModelAndView expectedView = new ModelAndView();
		expectedView.setViewName("openIdLogin");
		ModelAndView md = oNAPWelcomeController.login(p);
		System.out.println(md.getViewName());
		assertEquals(md.getViewName(), expectedView.getViewName());
	}
}
