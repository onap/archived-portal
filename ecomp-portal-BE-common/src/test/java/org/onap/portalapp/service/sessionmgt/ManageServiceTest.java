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

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.service.EPAppService;
import org.onap.portalsdk.core.domain.sessionmgt.TimeoutVO;
import org.onap.portalsdk.core.util.SystemProperties;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
@RunWith(PowerMockRunner.class)
@PrepareForTest({ SystemProperties.class })
public class ManageServiceTest {
	
	private static final String TEST="test";
	
	@InjectMocks
	ManageService manageService;
	@Mock
private EPAppService appService;
	
	@Mock
	private SessionCommunication sessionCommunication;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	@Test
	public void testFetchSessionSlotCheckInterval() {
		
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(SystemProperties.SESSIONTIMEOUT_FEED_CRON)).thenReturn(null);
	Integer data=	manageService.fetchSessionSlotCheckInterval(TEST);
	assertNotNull(data);
	}
	
	@Test
	public void testExtendSessionTimeOuts()throws Exception {
		TimeoutVO timeoutVO=new TimeoutVO("testSession", 1800l);
		ObjectMapper mapper = new ObjectMapper();
		Map<String, TimeoutVO> sessionTimeoutMap=new HashMap<>();
		sessionTimeoutMap.put("testPortal", timeoutVO);
	String sessionTimeoutMapStr=	mapper.writeValueAsString(sessionTimeoutMap);
		
		manageService.extendSessionTimeOuts(TEST,TEST,"1000",sessionTimeoutMapStr);
		
	}
	

}
