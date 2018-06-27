/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2018 AT&T Intellectual Property. All rights reserved.
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

package org.onap.portalapp.service.sessionmgt;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalsdk.core.domain.sessionmgt.TimeoutVO;
import org.onap.portalsdk.core.onboarding.util.PortalApiConstants;

import com.fasterxml.jackson.databind.ObjectMapper;


public class CoreTimeoutHandlerTest {
	
	@Mock
	HttpSession session;
	@InjectMocks
	CoreTimeoutHandler coreTimeoutHandler;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		
	}
	
	
	
	@Test
	public void testSessionCreated() {
		Mockito.when(session.getAttribute(PortalApiConstants.PORTAL_JSESSION_ID)).thenReturn("testPortal");
		coreTimeoutHandler.sessionCreated("testPortal", "testSession", session);
	}
	
	@Test
	public void testGatherSessionExtenstions()throws Exception {
		Mockito.when(session.getAttribute(PortalApiConstants.PORTAL_JSESSION_ID)).thenReturn("testPortal-testSession");
		coreTimeoutHandler.sessionCreated("testPortal", "testSession", session);
		coreTimeoutHandler.gatherSessionExtenstions();
		TimeoutVO timeoutVO=new TimeoutVO("testSession", 1800l);
		ObjectMapper mapper = new ObjectMapper();
		Map<String, TimeoutVO> sessionTimeoutMap=new HashMap<>();
		sessionTimeoutMap.put("testPortal", timeoutVO);
	String sessionTimeoutMapStr=	mapper.writeValueAsString(sessionTimeoutMap);
	coreTimeoutHandler.updateSessionExtensions(sessionTimeoutMapStr);
		
	}

}
