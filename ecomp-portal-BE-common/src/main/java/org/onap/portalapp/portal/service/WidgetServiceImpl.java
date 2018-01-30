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
package org.onap.portalapp.portal.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.EPUserApp;
import org.onap.portalapp.portal.domain.Widget;
import org.onap.portalapp.portal.logging.aop.EPMetricsLog;
import org.onap.portalapp.portal.logging.format.EPAppMessagesEnum;
import org.onap.portalapp.portal.logging.logic.EPLogUtil;
import org.onap.portalapp.portal.transport.FieldsValidator;
import org.onap.portalapp.portal.transport.OnboardingWidget;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.util.SystemProperties;

@Service("widgetService")
@Transactional
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPMetricsLog
public class WidgetServiceImpl implements WidgetService {

	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(WidgetServiceImpl.class);

	private static final String baseSqlToken = " widget.WIDGET_ID, widget.WDG_NAME, widget.APP_ID, app.APP_NAME, widget.WDG_WIDTH, widget.WDG_HEIGHT, widget.WDG_URL"
			+ " from FN_WIDGET widget join FN_APP app ON widget.APP_ID = app.APP_ID";

	private String validAppsFilter = "";

	private Long LONG_ECOMP_APP_ID = 1L;
	private Long ACCOUNT_ADMIN_ROLE_ID = 999L;
	private static final Long DUBLICATED_FIELD_VALUE_ECOMP_ERROR = new Long(EPCommonSystemProperties.DUBLICATED_FIELD_VALUE_ECOMP_ERROR);

	private static final String urlField = "url";

	private static final String nameField = "name";
	@Autowired
	AdminRolesService adminRolesService;
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private DataAccessService dataAccessService;

	@PostConstruct
	private void init() {
		try {
			validAppsFilter = " AND app.ENABLED = 'Y' AND app.APP_ID != " + SystemProperties.getProperty(EPCommonSystemProperties.ECOMP_APP_ID);
			ACCOUNT_ADMIN_ROLE_ID = Long.valueOf(SystemProperties.getProperty(EPCommonSystemProperties.ACCOUNT_ADMIN_ROLE_ID));
			LONG_ECOMP_APP_ID = Long.valueOf(SystemProperties.getProperty(EPCommonSystemProperties.ECOMP_APP_ID));
		} catch(Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "init failed", e);
		}
	}
	
	private String sqlWidgetsForAllApps() {
		return "SELECT" + baseSqlToken + validAppsFilter;
	}

	private String sqlWidgetsForAllAppsWhereUserIsAdmin(Long userId) {
		return "SELECT" + baseSqlToken + " join FN_USER_ROLE ON FN_USER_ROLE.APP_ID = app.APP_ID where FN_USER_ROLE.USER_ID = " + userId
				+ " AND FN_USER_ROLE.ROLE_ID = " + ACCOUNT_ADMIN_ROLE_ID + validAppsFilter;
	}

	private String sqlWidgetsForAllAppsWhereUserHasAnyRole(Long userId) {
		return "SELECT DISTINCT" + baseSqlToken + " join FN_USER_ROLE ON FN_USER_ROLE.APP_ID = app.APP_ID where FN_USER_ROLE.USER_ID = "
				+ userId + validAppsFilter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OnboardingWidget> getOnboardingWidgets(EPUser user, boolean managed) {
		List<OnboardingWidget> onboardingWidgets = new ArrayList<OnboardingWidget>();
		String sql = null;
		if (adminRolesService.isSuperAdmin(user)) {
			sql = this.sqlWidgetsForAllApps();
		} else if (managed) {
			if (adminRolesService.isAccountAdmin(user)) {
				sql = this.sqlWidgetsForAllAppsWhereUserIsAdmin(user.getId());
			}
		} else if (adminRolesService.isAccountAdmin(user) || adminRolesService.isUser(user)) {
			sql = this.sqlWidgetsForAllAppsWhereUserHasAnyRole(user.getId());
		}
		if (sql != null) {
			onboardingWidgets = dataAccessService.executeSQLQuery(sql, OnboardingWidget.class, null);
		}
		return onboardingWidgets;
	}

	private static final Object syncRests = new Object();

	private boolean isUserAdminOfAppForWidget(boolean superAdmin, Long userId, Long appId) {
		if (!superAdmin) {
			List<EPUserApp> userRoles = getAdminUserRoles(userId, appId);
			return (userRoles.size() > 0);
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	private List<EPUserApp> getAdminUserRoles(Long userId, Long appId) {
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion userIdCriterion = Restrictions.eq("userId",userId);
		Criterion roleIDCriterion = Restrictions.eq("role.id",  ACCOUNT_ADMIN_ROLE_ID);
		Criterion appIDCriterion  = Restrictions.eq("app.id",  appId);
		restrictionsList.add(Restrictions.and(userIdCriterion, roleIDCriterion,appIDCriterion));
		return (List<EPUserApp>) dataAccessService.getList(EPUserApp.class, null, restrictionsList, null);
	}

	private void validateOnboardingWidget(OnboardingWidget onboardingWidget, FieldsValidator fieldsValidator) {
		
		List<Widget> widgets = getWidgets(onboardingWidget);
		boolean dublicatedUrl = false;
		boolean dublicatedName = false;
		for (Widget widget : widgets) {
			if (onboardingWidget.id != null && onboardingWidget.id.equals(widget.getId())) {
				// widget should not be compared with itself
				continue;
			}
			if (!dublicatedUrl && widget.getUrl().equals(onboardingWidget.url)) {
				dublicatedUrl = true;
				if (dublicatedName) {
					break;
				}
			}
			if (!dublicatedName && widget.getName().equalsIgnoreCase(onboardingWidget.name) && widget.getAppId().equals(onboardingWidget.appId)) {
				dublicatedName = true;
				if (dublicatedUrl) {
					break;
				}
			}
		}
		if (dublicatedUrl || dublicatedName) {
			if (dublicatedUrl) {
				fieldsValidator.addProblematicFieldName(urlField);
			}
			if (dublicatedName) {
				fieldsValidator.addProblematicFieldName(nameField);
			}
			fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_CONFLICT);
			fieldsValidator.errorCode = DUBLICATED_FIELD_VALUE_ECOMP_ERROR;
		}
	}

	@SuppressWarnings("unchecked")
	private List<Widget> getWidgets(OnboardingWidget onboardingWidget) {
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion urlCriterion = Restrictions.eq("url", onboardingWidget.url);
		Criterion nameCriterion = Restrictions.eq("name", onboardingWidget.name);
		restrictionsList.add(Restrictions.or(urlCriterion, nameCriterion));
		return (List<Widget>) dataAccessService.getList(Widget.class, null, restrictionsList, null);
	}

	private void applyOnboardingWidget(OnboardingWidget onboardingWidget, FieldsValidator fieldsValidator) {
		boolean result = false;
		Session localSession = null;
		Transaction transaction = null;
		try {
			localSession = sessionFactory.openSession();
			transaction = localSession.beginTransaction();
			Widget widget;
			if (onboardingWidget.id == null) {
				widget = new Widget();
			} else {
				widget = (Widget) localSession.get(Widget.class, onboardingWidget.id);
			}
			widget.setAppId(onboardingWidget.appId);
			widget.setName(onboardingWidget.name);
			widget.setWidth(onboardingWidget.width);
			widget.setHeight(onboardingWidget.height);
			widget.setUrl(onboardingWidget.url);
			localSession.saveOrUpdate(widget);
			transaction.commit();
			result = true;
		} catch (Exception e) {
			EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError, e);
			EcompPortalUtils.rollbackTransaction(transaction, "applyOnboardingWidget rollback, exception = " + e);
		} finally {
			EcompPortalUtils.closeLocalSession(localSession, "applyOnboardingWidget");
		}
		if (!result) {
			fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	private FieldsValidator updateOrSaveWidget(boolean superAdmin, Long userId, OnboardingWidget onboardingWidget) {
		FieldsValidator fieldsValidator = new FieldsValidator();
		if (!this.isUserAdminOfAppForWidget(superAdmin, userId, onboardingWidget.appId)) {
			fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_FORBIDDEN);
			return fieldsValidator;
		}
		synchronized (syncRests) {
			// onboardingWidget.id is null for POST and not null for PUT
			if (onboardingWidget.id == null) {
				this.validateOnboardingWidget(onboardingWidget, fieldsValidator);
			} else {
				Widget widget = (Widget) dataAccessService.getDomainObject(Widget.class, onboardingWidget.id, null);
				if (widget == null || widget.getId() == null) {
					// Widget not found
					fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_NOT_FOUND);
					return fieldsValidator;
				}
				this.validateOnboardingWidget(onboardingWidget, fieldsValidator);
			}
			if (fieldsValidator.httpStatusCode.intValue() == HttpServletResponse.SC_OK) {
				this.applyOnboardingWidget(onboardingWidget, fieldsValidator);
			}
		}
		return fieldsValidator;
	}

	@Override
	public FieldsValidator setOnboardingWidget(EPUser user, OnboardingWidget onboardingWidget) {
		if (onboardingWidget.name.length() == 0 || onboardingWidget.url.length() == 0 || onboardingWidget.appId == null
				|| onboardingWidget.appId.equals(LONG_ECOMP_APP_ID) || onboardingWidget.width.intValue() <= 0 || onboardingWidget.height.intValue() <= 0) {
			if (onboardingWidget.appId.equals(LONG_ECOMP_APP_ID)) {
			}
			FieldsValidator fieldsValidator = new FieldsValidator();
			fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_BAD_REQUEST);
			return fieldsValidator;
		}
		return this.updateOrSaveWidget(adminRolesService.isSuperAdmin(user), user.getId(), onboardingWidget);
	}

	@Override
	public FieldsValidator deleteOnboardingWidget(EPUser user, Long onboardingWidgetId) {
		FieldsValidator fieldsValidator = new FieldsValidator();
		synchronized (syncRests) {
			Widget widget = (Widget) dataAccessService.getDomainObject(Widget.class, onboardingWidgetId, null);
			if (widget != null && widget.getId() != null) { // widget exists
				if (!this.isUserAdminOfAppForWidget(adminRolesService.isSuperAdmin(user), user.getId(), widget.getAppId())) {
					fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_FORBIDDEN);
				} else {
					boolean result = false;
					Session localSession = null;
					Transaction transaction = null;
					try {
						localSession = sessionFactory.openSession();
						transaction = localSession.beginTransaction();
						localSession.delete(localSession.get(Widget.class, onboardingWidgetId));
						transaction.commit();
						result = true;
					} catch (Exception e) {
						EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeDaoSystemError, e);
						EcompPortalUtils.rollbackTransaction(transaction, "deleteOnboardingWidget rollback, exception = " + e);
					} finally {
						EcompPortalUtils.closeLocalSession(localSession, "deleteOnboardingWidget");
					}
					if (!result) {
						fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					}
				}
			}
		}
		return fieldsValidator;
	}

}
