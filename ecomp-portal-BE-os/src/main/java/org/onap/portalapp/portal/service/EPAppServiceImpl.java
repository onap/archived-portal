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
package org.onap.portalapp.portal.service;

import java.net.MalformedURLException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.logging.aop.EPMetricsLog;
import org.onap.portalapp.portal.logging.format.EPAppMessagesEnum;
import org.onap.portalapp.portal.logging.logic.EPLogUtil;
import org.onap.portalapp.portal.service.EPAppCommonServiceImpl;
import org.onap.portalapp.portal.service.EPAppService;
import org.onap.portalapp.portal.transport.FieldsValidator;
import org.onap.portalapp.portal.transport.OnboardingApp;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.service.DataAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.att.nsa.cambria.client.CambriaClientFactory;
import com.att.nsa.cambria.client.CambriaTopicManager;

@Service("epAppService")
@Transactional
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPMetricsLog
public class EPAppServiceImpl extends EPAppCommonServiceImpl implements EPAppService {

	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(EPAppServiceImpl.class);

	private static Object syncRests = new Object();

	@Autowired
	private DataAccessService dataAccessService;

	@Override
	public List<EPApp> getUserRemoteApps(String id) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM FN_APP join FN_USER_ROLE ON FN_USER_ROLE.APP_ID = FN_APP.APP_ID where ");
		query.append("FN_USER_ROLE.USER_ID = " + id + " AND FN_USER_ROLE.ROLE_ID != " + SUPER_ADMIN_ROLE_ID);
		query.append(" AND FN_APP.ENABLED = 'Y'");
		TreeSet<EPApp> distinctApps = new TreeSet<EPApp>();
		@SuppressWarnings("unchecked")
		List<EPApp> adminApps = dataAccessService.executeSQLQuery(query.toString(), EPApp.class, null);
		for (EPApp app : adminApps) {
			distinctApps.add(app);
		}
		List<EPApp> userApps = new ArrayList<EPApp>();
		userApps.addAll(distinctApps);
		return userApps;
	}

	@Override
	protected void updateRestrictedApp(Long appId, OnboardingApp onboardingApp, FieldsValidator fieldsValidator,
			EPUser user) {
		synchronized (syncRests) {
			boolean result = false;
			Session localSession = null;
			Transaction transaction = null;
			try {
				localSession = sessionFactory.openSession();
				transaction = localSession.beginTransaction();
				EPApp app;
				if (appId == null) {
					app = new EPApp();
					/*
					 * In the parent class, the UEB code is responsible for generating the
					 * keys/secret/mailbox but UEB Messaging is not actually being used currently;
					 * may be used in future at which point we can just remove this method and
					 * depend on parent class's method So, using UUID generator to generate the
					 * unique key instead.
					 */
					String uuidStr = UUID.randomUUID().toString();
					String appKey = uuidStr;
					String appSecret = uuidStr;
					String appMailboxName = "ECOMP-PORTAL-OUTBOX";
					onboardingApp.setUebTopicName(appMailboxName);
					onboardingApp.setUebKey(appKey);
					onboardingApp.setUebSecret(appSecret);
				} else {
					app = (EPApp) localSession.get(EPApp.class, appId);
					if (app == null || app.getId() == null) {
						// App is already deleted!
						transaction.commit();
						localSession.close();
						fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_NOT_FOUND);
						return;
					}
				}
				logger.debug(EELFLoggerDelegate.debugLogger,
						"updateRestrictedApp: about to call createAppFromOnboarding");
				createAppFromOnboarding(app, onboardingApp, localSession);
				logger.debug(EELFLoggerDelegate.debugLogger,
						"updateRestrictedApp: finished calling createAppFromOnboarding");
				localSession.saveOrUpdate(app);
				logger.debug(EELFLoggerDelegate.debugLogger,
						"updateRestrictedApp: finished calling localSession.saveOrUpdate");
				// Enable or disable all menu items associated with this app
				setFunctionalMenuItemsEnabled(localSession, onboardingApp.isEnabled, appId);
				logger.debug(EELFLoggerDelegate.debugLogger,
						"updateRestrictedApp: finished calling setFunctionalMenuItemsEnabled");
				transaction.commit();
				logger.debug(EELFLoggerDelegate.debugLogger,
						"updateRestrictedApp: finished calling transaction.commit");
				result = true;
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "updateRestrictedApp failed", e);
				EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeUebRegisterOnboardingAppError, e);
				EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError, e);
				EcompPortalUtils.rollbackTransaction(transaction,
						"updateRestrictedApp rollback, exception = " + e.toString());
			} finally {
				EcompPortalUtils.closeLocalSession(localSession, "updateRestrictedApp");
			}
			if (!result) {
				fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		}

	}

	@Override
	public CambriaTopicManager getTopicManager(List<String> urlList, String key, String secret)
			throws MalformedURLException, GeneralSecurityException {
		return CambriaClientFactory.createTopicManager(null, urlList, key, secret);
	}

}