/*-
 * ================================================================================
 * ECOMP Portal
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
package org.openecomp.portalapp.scheduler;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.openecomp.portalapp.portal.listener.UserSessionListener;
import org.openecomp.portalapp.service.sessionmgt.TimeoutHandler;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.scheduler.CronRegistry;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.stereotype.Component;

/**
 * Extra depends-on annotation tells Spring that the system properties object
 * will be used in the constructor.
 */
@Component
// @DependsOn({ "manageService", "epAppService", "systemProperties" })
@DependsOn({ "systemProperties" })
public class SessionMgtRegistry extends CronRegistry implements ApplicationContextAware {

	EELFLoggerDelegate logger = null;
	
	private static final String groupName = "AppGroup";
	private static final String jobName = "PortalSessionTimeoutFeedJob";
	private static final String triggerName = "PortalSessionTimeoutFeedTrigger";

	// Not strictly necessary, but preparing for the day
	// when the getProperty method is not static.
	@Autowired
	private SystemProperties systemProperties;

	private ApplicationContext applicationContext;

	public JobDetailFactoryBean jobDetailFactoryBean() {
		logger = EELFLoggerDelegate.getLogger(SessionMgtRegistry.class);
		Map<String, Object> map = new HashMap<String, Object>();
		return jobDetailFactoryBean(groupName, jobName, TimeoutHandler.class, map);
	}

	@SuppressWarnings("static-access")
	public CronTriggerFactoryBean cronTriggerFactoryBean() throws ParseException {
		logger = EELFLoggerDelegate.getLogger(SessionMgtRegistry.class);
		String property = "* * * * * ? 2099";
		try {
			property = systemProperties.getProperty(SystemProperties.SESSIONTIMEOUT_FEED_CRON);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, 
					"Failed to retrieve " + SystemProperties.SESSIONTIMEOUT_FEED_CRON + ", defaulting to " + property,
					e);
		}
		return cronTriggerFactoryBean(groupName, triggerName, property);
	}

	@Override
	public void setApplicationContext(ApplicationContext _applicationContext) throws BeansException {
		applicationContext = _applicationContext;
		TimeoutHandler.setApplicationContext(applicationContext);
		UserSessionListener.setApplicationContext(_applicationContext);
	}

}
