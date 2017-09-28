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
package org.openecomp.portalapp.config;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openecomp.portalapp.portal.service.UserNotificationService;
import org.openecomp.portalapp.portal.service.UserNotificationServiceImpl;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.context.ApplicationContext;

@RunWith(PowerMockRunner.class)
@PrepareForTest(NotificationCleanupConfig.class)
public class NotificationCleanupTest {

	@InjectMocks
	NotificationCleanup notificationCleanup = new NotificationCleanup();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Mock
	UserNotificationService userNotificationService = new UserNotificationServiceImpl();
	@Mock
	ApplicationContext applicationContext;

	@Test
	public void runTest() {
		PowerMockito.mockStatic(NotificationCleanupConfig.class);
		Mockito.when(NotificationCleanupConfig.getApplicationContext()).thenReturn(applicationContext);
		Mockito.when((applicationContext).getBean(UserNotificationService.class)).thenReturn(userNotificationService);
		Mockito.doNothing().when(userNotificationService).deleteNotificationsFromEpUserNotificationTable();
		Mockito.doNothing().when(userNotificationService).deleteNotificationsFromEpRoleNotificationTable();

		Mockito.doNothing().when(userNotificationService).deleteNotificationsFromEpNotificationTable();
		notificationCleanup.run();

	}

}
