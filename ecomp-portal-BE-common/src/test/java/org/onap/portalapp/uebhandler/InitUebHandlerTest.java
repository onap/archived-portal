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

package org.onap.portalapp.uebhandler;

import static org.junit.Assert.assertEquals;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.onap.portalapp.portal.controller.ExternalAccessRolesController;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.portal.utils.PortalConstants;
import org.onap.portalsdk.core.onboarding.ueb.UebManager;
import org.onap.portalsdk.core.onboarding.ueb.UebMsg;
import org.onap.portalsdk.core.onboarding.util.PortalApiConstants;
import org.onap.portalsdk.core.onboarding.util.PortalApiProperties;
import org.onap.portalsdk.core.util.SystemProperties;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ EcompPortalUtils.class, PortalConstants.class, SystemProperties.class,
		EPCommonSystemProperties.class, PortalApiProperties.class,PortalApiConstants.class,UebManager.class })
public class InitUebHandlerTest {

	@Mock
	MainUebHandler mainUebHandler;
	
	@InjectMocks
	InitUebHandler initUebHandler=new InitUebHandler();
	
	@Mock
	UebManager uebManager1;
	
	@Test
	public void initUebTestWithException() throws Exception {
		PowerMockito.mockStatic(PortalApiProperties.class);
		initUebHandler.initUeb();
	}
	
	@Test
	public void initUebTest() throws Exception {
		PowerMockito.mockStatic(PortalApiProperties.class);
		PowerMockito.mockStatic(PortalApiConstants.class);
		Mockito.when(PortalApiProperties.getProperty(Matchers.any())).thenReturn("test");
		//Mockito.when(methodCall)
		initUebHandler.initUeb();
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void initUebTest1() throws Exception {
		PowerMockito.mockStatic(PortalApiProperties.class);
		PowerMockito.mockStatic(PortalApiConstants.class);
		PowerMockito.mockStatic(UebManager.class);
		UebMsg uebMsg=new UebMsg();
		uebMsg.putMsgId("12");
		uebMsg.putMsgType("test");
		uebMsg.putPayload("samplePayload");
		uebMsg.putUserId("13");
        ConcurrentLinkedQueue<UebMsg> inboxQueue = new ConcurrentLinkedQueue<UebMsg>();
		Mockito.when(PortalApiProperties.getProperty(Matchers.any())).thenReturn("TRUE");
		Mockito.when(UebManager.getInstance()).thenReturn(uebManager1);
		initUebHandler.initUeb();
	}
}
