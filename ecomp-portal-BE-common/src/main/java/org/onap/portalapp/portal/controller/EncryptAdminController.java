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
package org.onap.portalapp.portal.controller;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.onap.portalapp.portal.logging.aop.EPAuditLog;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.portal.utils.PortalConstants;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.onboarding.exception.CipherUtilException;
import org.onap.portalsdk.core.onboarding.util.CipherUtil;
import org.onap.portalsdk.core.service.DataAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PortalConstants.REST_AUX_API)
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class EncryptAdminController implements BasicAuthenticationController {
	@Autowired
	DataAccessService dataAccessService;
	@Autowired
	protected SessionFactory sessionFactory;

	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(EncryptAdminController.class);

	@PostMapping(value = { "/executeEncryptQuery" })
	public Map<Long, String> executeEncrypt(HttpServletRequest request, HttpServletResponse response)
			throws CipherUtilException {

		return fetchRecords();
	}

	public Map<Long, String> fetchRecords() throws CipherUtilException {
		List appPassword = null;
		Session localSession = null;
		Map<Long, String> responsemap;
		try {
			localSession = sessionFactory.openSession();
			responsemap = new LinkedHashMap<Long, String>();
			appPassword = dataAccessService.executeNamedQuery("getAppPassword", null, null);
			if (appPassword != null) {
				Iterator i = appPassword.iterator();
				while (i.hasNext()) {
					Object[] user = (Object[]) i.next();
					Long app_id = (Long) user[0];
					String app_password = (String) user[1];
					if (app_id != null && StringUtils.isNotEmpty(app_password)) {
						try {
							final String pass = CipherUtil.encryptPKC(CipherUtil.decrypt(app_password));
							Query query = null;
							try {
								localSession.getTransaction().begin();
								query = localSession.createSQLQuery(
										"UPDATE fn_app m SET m.app_password= :pass " + " where m.app_id = :app_id");
								query.setParameter("pass", pass);
								query.setParameter("app_id", app_id);
								int result = query.executeUpdate();
								localSession.getTransaction().commit();
								logger.debug(EELFLoggerDelegate.debugLogger,
										"--------------getAppPassword-------query successfull------------------"
												+ query);

							} catch (Exception e) {
								localSession.getTransaction().rollback();
								logger.debug(EELFLoggerDelegate.debugLogger,
										"--------------getAppPassword--------query failed-----------------" + query);
								responsemap.put(app_id, "-------query failed-----------------" + query);
								logger.error(EELFLoggerDelegate.errorLogger,
										"getAppPassword error while executing  the query", e);

							}
						} catch (Exception e) {
							logger.error(EELFLoggerDelegate.errorLogger,
									"getAppPassword error while executing  the query", e);
						}
					}
				}
			}
			appPassword = dataAccessService.executeNamedQuery("getBasicauthAccount", null, null);
			if (appPassword != null) {
				Iterator i = appPassword.iterator();
				while (i.hasNext()) {
					Object[] user = (Object[]) i.next();
					Long app_id = (Long) user[0];
					String password = (String) user[1];
					if (app_id != null && StringUtils.isNotEmpty(password)) {
						try {
							final String pass = CipherUtil.encryptPKC(CipherUtil.decrypt(password));
							Query query = null;
							try {
								localSession.getTransaction().begin();
								query = localSession
										.createSQLQuery("UPDATE ep_basic_auth_account m SET m.password = :pass"
												+ " where m.id  = :app_id");
								query.setParameter("pass", pass);
								query.setParameter("app_id", app_id);
								int result = query.executeUpdate();
								localSession.getTransaction().commit();
								logger.debug(EELFLoggerDelegate.debugLogger,
										"--------------getAppPassword-------query successfull------------------"
												+ query);

							} catch (Exception e) {
								localSession.getTransaction().rollback();
								logger.debug(EELFLoggerDelegate.debugLogger,
										"--------------getAppPassword--------query failed-----------------" + query);
								responsemap.put(app_id, "-------query failed-----------------" + query);
								logger.error(EELFLoggerDelegate.errorLogger,
										"getAppPassword error while executing  the query", e);

							}
						} catch (Exception e) {
							logger.error(EELFLoggerDelegate.errorLogger,
									"getAppPassword error while executing  the query", e);
						}

					}

				}
			}
			appPassword = dataAccessService.executeNamedQuery("getMicroserviceInfo", null, null);
			if (appPassword != null) {
				Iterator i = appPassword.iterator();
				while (i.hasNext()) {
					Object[] user = (Object[]) i.next();
					Long app_id = (Long) user[0];
					String password = (String) user[1];
					if (app_id != null && StringUtils.isNotEmpty(password)) {
						try {
							final String pass = CipherUtil.encryptPKC(CipherUtil.decrypt(password));
							Query query = null;
							try {
								localSession.getTransaction().begin();
								query = localSession.createSQLQuery(
										"UPDATE ep_microservice m SET m.password = :pass" + " WHERE m.id = :app_id");
								query.setParameter("pass", pass);
								query.setParameter("app_id", app_id);
								int result = query.executeUpdate();
								localSession.getTransaction().commit();
								logger.debug(EELFLoggerDelegate.debugLogger,
										"--------------getAppPassword-------query successfull------------------"
												+ query);

							} catch (Exception e) {
								localSession.getTransaction().rollback();
								logger.debug(EELFLoggerDelegate.debugLogger,
										"--------------getAppPassword--------query failed-----------------" + query);
								responsemap.put(app_id, "-------query failed-----------------" + query);
								logger.error(EELFLoggerDelegate.errorLogger,
										"getAppPassword error while executing  the query", e);

							}
						} catch (Exception e) {
							logger.error(EELFLoggerDelegate.errorLogger,
									"getAppPassword error while executing  the query", e);
						}

					}

				}
			}
			
		} finally {
			EcompPortalUtils.closeLocalSession(localSession, "updateRecords");
		}
		return responsemap;
	}
}
