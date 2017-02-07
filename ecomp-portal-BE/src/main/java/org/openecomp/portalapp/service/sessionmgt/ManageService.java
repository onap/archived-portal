/*-
 * ================================================================================
 * eCOMP Portal
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
package org.openecomp.portalapp.service.sessionmgt;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openecomp.portalapp.portal.logging.aop.EPMetricsLog;
import org.openecomp.portalapp.portal.service.EPAppService;
import org.openecomp.portalapp.portal.transport.OnboardingApp;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.onboarding.crossapi.PortalTimeoutHandler;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service("manageService")
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPMetricsLog
public class ManageService implements PortalTimeoutHandler.SessionCommInf {
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(ManageService.class);
	
	@Autowired
	EPAppService appService;
	
	@Autowired
	SessionCommunication sessionCommunication;

	public Integer fetchSessionSlotCheckInterval(String... params) throws Exception {

		String defaultCronExpressionStr = "0 0/5 * * * ? *";
		String cronExpressionStr = SystemProperties.getProperty(SystemProperties.SESSIONTIMEOUT_FEED_CRON);

		if (cronExpressionStr == null) {
			cronExpressionStr = defaultCronExpressionStr;
		}

		CronExpression cal = new CronExpression(cronExpressionStr);
		final Date nowTime = Calendar.getInstance().getTime();
		Date nextTime = cal.getNextValidTimeAfter(nowTime);
		Date nextNextTime = cal.getNextValidTimeAfter(nextTime);

		final int timeDiff = (int)(nextNextTime.getTime()-nextTime.getTime());
		logger.debug(EELFLoggerDelegate.debugLogger, "Time interval between subsequent session checks " + timeDiff);

		return timeDiff;
	}

	public void extendSessionTimeOuts(String... params) throws Exception {
		try {
			String sessionMap = params[3];
	
			logger.debug(EELFLoggerDelegate.debugLogger, "Extending the App sessions for last minute request: " + sessionMap);
			
			if (StringUtils.isEmpty(sessionMap)) {
				logger.error(EELFLoggerDelegate.errorLogger, "extendSessionTimeOuts: Skipping session updates since the portal session value is empty.");
			} else {
				List<OnboardingApp> appList = appService.getEnabledNonOpenOnboardingApps();
				for (OnboardingApp onApp : appList) {
					sessionCommunication.pingSession(onApp, sessionMap);
				}
				updateSessionExtensions(sessionMap);
				sessionCommunication.clear(false);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred in extendSessionTimeOuts(). Details: " + EcompPortalUtils.getStackTrace(e)); 
		}
	}

	public String gatherSessionExtenstions() {
		return PortalTimeoutHandler.gatherSessionExtensions();
	}

	public void updateSessionExtensions(String sessionTimeoutMapStr) throws Exception {
		PortalTimeoutHandler.updateSessionExtensions(sessionTimeoutMapStr);
	}

}
