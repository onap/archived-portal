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
package org.onap.portalapp.portal.domain;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.onap.portalapp.portal.domain.EPUserNotification;

public class EPUserNotificationTest {

	public EPUserNotification mockEPUserNotification(){
		
		EPUserNotification epUserNotification = new EPUserNotification();
				
		epUserNotification.setUserId((long)1);
		epUserNotification.setNotificationId((long)1);
		epUserNotification.setViewed("test");
		epUserNotification.setUpdateTime(new Date());		
		
		return epUserNotification;
	}
	
	@Test
	public void epUserNotificationTest(){
		EPUserNotification epUserNotification = mockEPUserNotification();
		
		assertEquals(epUserNotification.getUserId(), new Long(1));
		assertEquals(epUserNotification.getNotificationId(), new Long(1));
		assertEquals(epUserNotification.getViewed(), "test");
	//	assertEquals(epUserNotification.getUpdateTime(), new Date());
		
	}
}
