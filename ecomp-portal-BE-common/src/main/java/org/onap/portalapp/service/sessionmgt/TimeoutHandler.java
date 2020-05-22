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
package org.onap.portalapp.service.sessionmgt;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.onap.portalapp.portal.logging.aop.EPMetricsLog;
import org.onap.portalapp.portal.logging.format.EPAppMessagesEnum;
import org.onap.portalapp.portal.logging.logic.EPLogUtil;
import org.onap.portalapp.portal.service.AppsCacheService;
import org.onap.portalapp.portal.service.EPAppService;
import org.onap.portalapp.portal.transport.OnboardingApp;
import org.onap.portalsdk.core.domain.sessionmgt.TimeoutVO;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.onboarding.util.PortalApiConstants;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Executed periodically by Quartz to discover remote application sessions and
 * update timeouts suitably.
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPMetricsLog
public class TimeoutHandler extends QuartzJobBean {
	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(TimeoutHandler.class);
	
	private ObjectMapper mapper = new ObjectMapper();

	/**
	 * Supports static call {@link #timeoutSessions(HttpSession)}
	 */
	private static List<OnboardingApp> onboardedAppList = null;
	
	@Autowired
	private SessionCommunication sessionCommunication;
	
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		try {
			//Create a request id if there is none available,
			//and which will internally be used when making
			//session extended timeout calls to the partner applications.
			if (getSessionCommunication()!=null) {
				getSessionCommunication().setRequestId();
			}
			logger.info(EELFLoggerDelegate.debugLogger, "Quartz Cronjob for Session Management begins");
			
			ManageService manageService = (ManageService) applicationContext.getBean("manageService");
			EPAppService appService = (EPAppService) applicationContext.getBean("epAppService");
			AppsCacheService appsCacheService = (AppsCacheService)applicationContext.getBean("appsCacheService");
			
			List<OnboardingApp> appList = appsCacheService.getAppsFullList();
			onboardedAppList = appList;
			TypeReference<Hashtable<String, TimeoutVO>> typeRef = new TypeReference<Hashtable<String, TimeoutVO>>() {
			};
			String portalJsonSessionStr;
			Map<String, TimeoutVO> portalSessionTimeoutMap = null;

			portalJsonSessionStr = manageService.gatherSessionExtenstions();
			if (portalJsonSessionStr == null || portalJsonSessionStr == "") {
				logger.error(EELFLoggerDelegate.errorLogger, "Session Management: Portal session information is empty.");
				return;
			}
			
			try {
				portalSessionTimeoutMap = mapper.readValue(portalJsonSessionStr, typeRef);
			} catch (JsonMappingException | JsonParseException je) {
				EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeInvalidJsonInput, je);
				logger.error(EELFLoggerDelegate.errorLogger, "Session Management: JSON Mapping Exception occurred while gathering the Session", je);
				return;
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "Session Management: Error while gather Session from portal", e);
				return;
			}
			
			Map<Long, Map<String, TimeoutVO>> appSessionTimeOutMap = new Hashtable<Long, Map<String, TimeoutVO>>();
			// determine the Max TimeOut Time for each of the managed sessions
			for (OnboardingApp app : appList) {
				if (app.getRestUrl() == null) {
					logger.info(EELFLoggerDelegate.debugLogger, "Session Management: null restUrl, not fetching from app " + app.getAppName());
					continue;
				}
				logger.info(EELFLoggerDelegate.debugLogger, "Session Management: Calling App " + app.getAppName() + " at URL " + app.getRestUrl());
				String jsonSessionStr = fetchAppSessions(app);
				logger.info(EELFLoggerDelegate.debugLogger, "Session Management: App " + app.getAppName() + " returned  " + jsonSessionStr);
				if (jsonSessionStr == null || jsonSessionStr.isEmpty())
					continue;

				try {
					Map<String, TimeoutVO> sessionTimeoutMap = mapper.readValue(jsonSessionStr, typeRef);
					appSessionTimeOutMap.put(app.getId(), sessionTimeoutMap);
					for (String portalJSessionId : sessionTimeoutMap.keySet()) {
						final TimeoutVO maxTimeoutVO = portalSessionTimeoutMap.get(portalJSessionId);
						final TimeoutVO compareTimeoutVO = sessionTimeoutMap.get(portalJSessionId);
						if (maxTimeoutVO != null && compareTimeoutVO != null) {
							if (maxTimeoutVO.compareTo(compareTimeoutVO) < 0)
								portalSessionTimeoutMap.get(portalJSessionId)
										.setSessionTimOutMilliSec(compareTimeoutVO.getSessionTimOutMilliSec());
						}
					}
				} catch (JsonParseException | JsonMappingException e) {
					EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeInvalidJsonInput, e);
					logger.error(EELFLoggerDelegate.errorLogger, 
							"JSON Mapping/Processing Exception occurred while mapping/parsing the jsonSessionStr", e);
					continue;
				} catch (Exception e) {
					logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while mapping/parsing the jsonSessionStr", e);
					continue;
				}

			}

			// post the updated session timeouts back to the Apps
			for (OnboardingApp app : appList) {
				if (app.getRestUrl() == null) {
					logger.warn(EELFLoggerDelegate.errorLogger, "Session Management: null restUrl, not posting back to app " + app.getAppName());
					continue;
				}

				Map<String, TimeoutVO> sessionTimeoutMap = appSessionTimeOutMap.get(app.getId());
				if (sessionTimeoutMap == null || sessionTimeoutMap.isEmpty())
					continue;

				for (String portalJSessionId : sessionTimeoutMap.keySet()) {
					try {
						final TimeoutVO maxTimeoutVO = portalSessionTimeoutMap.get(portalJSessionId);
						final TimeoutVO setTimeoutVO = sessionTimeoutMap.get(portalJSessionId);
						if (maxTimeoutVO == null || setTimeoutVO == null) {
							String message = String.format(
									"Session Management: Failed to update the session timeouts for the app: %s and the sessionId: %s.",
									app.getAppName(), portalJSessionId);
							logger.warn(EELFLoggerDelegate.errorLogger, message);
							continue;
						}
						setTimeoutVO.setSessionTimOutMilliSec(maxTimeoutVO.getSessionTimOutMilliSec());
					} catch (Exception e) {
						logger.error(EELFLoggerDelegate.errorLogger, "Session Management:  error while updating the session timeout map", e);
						continue;
					}
				}
				logger.info(EELFLoggerDelegate.debugLogger, "Session Management: Updating App " + app.getRestUrl());
				String sessionTimeoutMapStr = "";
				try {
					sessionTimeoutMapStr = mapper.writeValueAsString(sessionTimeoutMap);
				} catch (JsonProcessingException je) {
					logger.error(EELFLoggerDelegate.errorLogger, "executeInternal failed while processing sessionTimeOutMap object to a String", je);
					EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeInvalidJsonInput, je);
				}
				pingAppSessions(app, sessionTimeoutMapStr);
			}
			String portalSessionTimeoutMapStr = "";
			try {
				portalSessionTimeoutMapStr = mapper.writeValueAsString(portalSessionTimeoutMap);
			} catch (JsonProcessingException je) {
				logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while processing portalSessionTimeOutMap object to a String", je);
				EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeInvalidJsonInput, je);
			}
			manageService.updateSessionExtensions(portalSessionTimeoutMapStr);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "************************ Session Management:  error in managing session timeouts", e);
		} finally {
			getSessionCommunication().clear(true);
		}
	}

	private String fetchAppSessions(OnboardingApp app) throws Exception {
		String jsonSessionValue = getSessionCommunication().sendGet(app);
		getSessionCommunication().clear(false);
		return jsonSessionValue;
	}

	private void pingAppSessions(OnboardingApp app, String sessionTimeoutMapStr) throws Exception {
		getSessionCommunication().pingSession(app, sessionTimeoutMapStr);
		getSessionCommunication().clear(false);
	}

	public void timeoutSessions(HttpSession session) throws Exception {
		String portalJSessionId = portalJSessionId(session);
		if (onboardedAppList == null)
			return;

		for (OnboardingApp app : onboardedAppList) {
			getSessionCommunication().timeoutSession(app, portalJSessionId);
			getSessionCommunication().clear(false);
		}
	}

	protected static String portalJSessionId(HttpSession session) {
		final Object attribute = session.getAttribute(PortalApiConstants.PORTAL_JSESSION_ID);
		if (attribute == null)
			return "";
		String jSessionKey = (String) attribute;
		return jSessionKey.split("-")[0];
		//return jSessionKey;
	}

	private static ApplicationContext applicationContext;

	public static void setApplicationContext(ApplicationContext _applicationContext) {
		applicationContext = _applicationContext;
	}

	public SessionCommunication getSessionCommunication() {
		if(sessionCommunication == null){
			if (applicationContext != null)				
				sessionCommunication = (SessionCommunication)applicationContext.getBean("sessionCommunication");
		}
		
		return sessionCommunication;
	}

	public void setSessionCommunication(SessionCommunication sessionCommunication) {
		this.sessionCommunication = sessionCommunication;
	}

}