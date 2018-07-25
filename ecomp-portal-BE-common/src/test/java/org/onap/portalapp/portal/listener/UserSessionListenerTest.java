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
package org.onap.portalapp.portal.listener;

import static org.mockito.Mockito.*;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

import org.apache.commons.collections4.map.HashedMap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.service.SharedContextService;

public class UserSessionListenerTest {
	
	@InjectMocks
	UserSessionListener userSessionListener;
	
	@Mock
	SharedContextService sharedContextService;
	@Mock
	HttpSessionEvent event;
	@Mock
	HttpSession session;
	@Mock
	ServletContext context;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testSessionCreate() {
	
		Mockito.when(event.getSession()).thenReturn(session);
		Mockito.when(session.getServletContext()).thenReturn(context);
		when(context.getAttribute("activeUsers")).thenReturn(new HashMap());
		userSessionListener.sessionCreated(event);
		
		
	}
	
	@Test
	public void testSessionDestroyed() {
		
		HashMap	activeUsers=new HashMap<>();
		
		when(session.getId()).thenReturn("TestSession");
		activeUsers.put("TestSession", session);
			Mockito.when(event.getSession()).thenReturn(session);
			Mockito.when(session.getServletContext()).thenReturn(context);
			when(context.getAttribute("activeUsers")).thenReturn(activeUsers);
			userSessionListener.sessionDestroyed(event);
	}

}
