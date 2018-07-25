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
package org.onap.portalapp.uebhandler;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.service.EPAppService;
import org.onap.portalapp.portal.service.SearchService;
import org.onap.portalsdk.core.onboarding.ueb.UebMsg;

public class WidgetNotificationHandlerTest {

	@InjectMocks
	WidgetNotificationHandler widgetNotificationHandler;
	@Mock
	EPAppService appSvc;

	@Mock
	SearchService searchSvc;
	
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void handleWidgetNotification() {
		
		UebMsg uebMsg=new UebMsg();
		uebMsg.putMsgId("1");
		uebMsg.putUserId("13");
		uebMsg.putMsgType("testType");
		EPUser user = new EPUser();
		user.setOrgUserId("123");
		user.setFirstName("TestFirstName");
		user.setLastName("TestLastName");
		EPApp app=new EPApp();
		app.setUebTopicName("test");
		List<EPApp> list=new ArrayList<>();
		list.add(app);
		
		
		Mockito.when(searchSvc.searchUserByUserId(uebMsg.getUserId())).thenReturn(user);
		Mockito.when(appSvc.getUserApps(user)).thenReturn(list);
		widgetNotificationHandler.handleWidgetNotification(uebMsg);
		
	}
}
