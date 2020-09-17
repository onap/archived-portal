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

package org.onap.portalapp.portal.logging.logic;

import org.junit.Test;
import org.mockito.Mock;
import org.onap.portalapp.portal.interceptor.PortalResourceInterceptor;
import org.onap.portalapp.portal.logging.format.EPAppMessagesEnum;
import org.onap.portalsdk.core.logging.format.AlarmSeverityEnum;
import org.onap.portalsdk.core.logging.format.ErrorSeverityEnum;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.http.HttpStatus;

public class EPLogUtilTest {

	@Mock
	private EELFLoggerDelegate eelfLoggerDelegate;

	EPAppMessagesEnum epAppMessagesEnum;
	
	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(EPLogUtilTest.class);

//	@Test
//	public void testLogEcompError() {
//		
//
//		EPAppMessagesEnum epAppMessagesEnum=	EPAppMessagesEnum.BeHealthCheckRecovery;
//		AlarmSeverityEnum alarmSeverityEnum=AlarmSeverityEnum.INFORMATIONAL;
//		epAppMessagesEnum.setAlarmSeverity(alarmSeverityEnum);
//		epAppMessagesEnum.setErrorSeverity(ErrorSeverityEnum.INFO);
//		
////		EPLogUtil.logEcompError( epAppMessagesEnum,"testError");
//		
//
//	}
	
	@Test
	public void testLogEcompErrorWithDelegate() {

		EPAppMessagesEnum epAppMessagesEnum=	EPAppMessagesEnum.BeHttpConnectionError;
		AlarmSeverityEnum alarmSeverityEnum=AlarmSeverityEnum.INFORMATIONAL;
		epAppMessagesEnum.setAlarmSeverity(alarmSeverityEnum);
		epAppMessagesEnum.setErrorSeverity(ErrorSeverityEnum.WARN);
		
		EPLogUtil.logEcompError(logger, epAppMessagesEnum,"testError");
		

	}
	@Test
	public void testFormatAuditLogMessage() {
		
		EPLogUtil.formatAuditLogMessage("test", "operation", "test", "test", "testinfo");
		
	}
	
	@Test
	public void formatStoreAnalyticsAuditLogMessage() {
		EPLogUtil.formatStoreAnalyticsAuditLogMessage("test", "operation", "test", "test", "testinfo", "testPage", "testFunction", "testType");
	}
	
	@Test
	public void logExternalAuthAccessAlarm() {
		EPLogUtil.logExternalAuthAccessAlarm(logger, HttpStatus.BAD_REQUEST);
		EPLogUtil.logExternalAuthAccessAlarm(logger, HttpStatus.BANDWIDTH_LIMIT_EXCEEDED);
		EPLogUtil.schedulerAccessAlarm(logger, 404);
		EPLogUtil.schedulerAccessAlarm(logger, 403);
		EPLogUtil.schedulerAccessAlarm(logger, 200);
		
	}

}
