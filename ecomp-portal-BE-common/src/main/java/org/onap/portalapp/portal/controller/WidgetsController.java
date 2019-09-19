/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright © 2017 AT&T Intellectual Property. All rights reserved.
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

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.common.util.StringUtils;
import org.onap.portalapp.controller.EPRestrictedBaseController;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.logging.aop.EPAuditLog;
import org.onap.portalapp.portal.service.AdminRolesService;
import org.onap.portalapp.portal.service.PersUserWidgetService;
import org.onap.portalapp.portal.service.WidgetService;
import org.onap.portalapp.portal.transport.FieldsValidator;
import org.onap.portalapp.portal.transport.OnboardingWidget;
import org.onap.portalapp.portal.transport.WidgetCatalogPersonalization;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.util.EPUserUtils;
import org.onap.portalapp.validation.DataValidator;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class WidgetsController extends EPRestrictedBaseController {
	private static final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(WidgetsController.class);
	private static final DataValidator dataValidator = new DataValidator();

	private AdminRolesService adminRolesService;
	private WidgetService widgetService;
	private PersUserWidgetService persUserWidgetService;

	@Autowired
	public WidgetsController(AdminRolesService adminRolesService,
		WidgetService widgetService, PersUserWidgetService persUserWidgetService) {
		this.adminRolesService = adminRolesService;
		this.widgetService = widgetService;
		this.persUserWidgetService = persUserWidgetService;
	}

	@RequestMapping(value = { "/portalApi/widgets" }, method = RequestMethod.GET, produces = "application/json")
	public List<OnboardingWidget> getOnboardingWidgets(HttpServletRequest request, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		List<OnboardingWidget> onboardingWidgets = null;

		if (user == null || user.isGuest()) {
			EcompPortalUtils.setBadPermissions(user, response, "getOnboardingWidgets");
		} else {
			String getType = request.getHeader("X-Widgets-Type");
			if (!StringUtils.isEmpty(getType) && ("managed".equals(getType) || "all".equals(getType))) {
				onboardingWidgets = widgetService.getOnboardingWidgets(user, "managed".equals(getType));
			} else {
				logger.debug(EELFLoggerDelegate.debugLogger, "WidgetsController.getOnboardingApps - request must contain header 'X-Widgets-Type' with 'all' or 'managed'");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		}
		
		EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/widgets", "GET result =", response.getStatus());
		return onboardingWidgets;
	}

	private boolean userHasPermissions(EPUser user, HttpServletResponse response, String invocator) {
		if (!adminRolesService.isSuperAdmin(user) && !adminRolesService.isAccountAdmin(user)) {
			EcompPortalUtils.setBadPermissions(user, response, invocator);
			return false;
		}
		return true;
	}

	// Attention: real json has all OnboardingWidget fields except "id", we use OnboardingWidget for not to create new class for parsing
	@RequestMapping(value = { "/portalApi/widgets/{widgetId}" }, method = { RequestMethod.PUT }, produces = "application/json")
	public FieldsValidator putOnboardingWidget(HttpServletRequest request, @PathVariable("widgetId") Long widgetId,
			@RequestBody OnboardingWidget onboardingWidget, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		FieldsValidator fieldsValidator = null;
		if (onboardingWidget!=null && !dataValidator.isValid(onboardingWidget)){
				fieldsValidator = new FieldsValidator();
				fieldsValidator.setHttpStatusCode((long)HttpServletResponse.SC_NOT_ACCEPTABLE);
				return fieldsValidator;
		}

		if (userHasPermissions(user, response, "putOnboardingWidget")) {
            if (onboardingWidget != null) {
                onboardingWidget.id = widgetId; // !
                onboardingWidget.normalize();
            }

			fieldsValidator = widgetService.setOnboardingWidget(user, onboardingWidget);
			response.setStatus(fieldsValidator.httpStatusCode.intValue());
		}
		EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/widgets/" + widgetId, "GET result =", response.getStatus());

		return fieldsValidator;
	}

	// Attention: real json has all OnboardingWidget fields except "id", we use OnboardingWidget for not to create new class for parsing
	@RequestMapping(value = { "/portalApi/widgets" }, method = { RequestMethod.POST }, produces = "application/json")
	public FieldsValidator postOnboardingWidget(HttpServletRequest request, @RequestBody OnboardingWidget onboardingWidget, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		FieldsValidator fieldsValidator = null;

		if (onboardingWidget!=null && !dataValidator.isValid(onboardingWidget)){
				fieldsValidator = new FieldsValidator();
				fieldsValidator.setHttpStatusCode((long)HttpServletResponse.SC_NOT_ACCEPTABLE);
				return fieldsValidator;
		}

		if (userHasPermissions(user, response, "postOnboardingWidget")) {
		    
            if (onboardingWidget != null) {
                onboardingWidget.id = null; // !
                onboardingWidget.normalize();
            }
			fieldsValidator = widgetService.setOnboardingWidget(user, onboardingWidget);
			response.setStatus(fieldsValidator.httpStatusCode.intValue());
		}

		EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/widgets", "POST result =", response.getStatus());
		return fieldsValidator;
	}

	@RequestMapping(value = { "/portalApi/widgets/{widgetId}" }, method = { RequestMethod.DELETE }, produces = "application/json")
	public FieldsValidator deleteOnboardingWidget(HttpServletRequest request, @PathVariable("widgetId") Long widgetId, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		FieldsValidator fieldsValidator = null;

		if (userHasPermissions(user, response, "deleteOnboardingWidget")) {
			fieldsValidator = widgetService.deleteOnboardingWidget(user, widgetId);
			response.setStatus(fieldsValidator.httpStatusCode.intValue());
		}

		EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/widgets/" + widgetId, "DELETE result =", response.getStatus());
		return fieldsValidator;
	}

	/**
	 * service to accept a user's action made on the application
	 * catalog.
	 * 
	 * @param request
	 * @param selectRequest
	 *            JSON with data including application ID
	 * @param response
	 * @return FieldsValidator
	 * @throws IOException
	 */
	@RequestMapping(value = { "portalApi/widgetCatalogSelection" }, method = RequestMethod.PUT, produces = "application/json")
	public FieldsValidator putWidgetCatalogSelection(HttpServletRequest request,
			@RequestBody WidgetCatalogPersonalization persRequest, HttpServletResponse response) throws IOException {
		FieldsValidator result = new FieldsValidator();
		EPUser user = EPUserUtils.getUserSession(request);

		if (persRequest!=null){
			if(!dataValidator.isValid(persRequest)){
				result.httpStatusCode = (long)HttpServletResponse.SC_NOT_ACCEPTABLE;
				return result;
			}
		}


		try {
			if (persRequest.getWidgetId() == null || user == null) {
				EcompPortalUtils.setBadPermissions(user, response, "putWidgetCatalogSelection");
			} else {
				persUserWidgetService.setPersUserAppValue(user, persRequest.getWidgetId(), persRequest.getSelect());
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "Failed in putAppCatalogSelection", e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
		}
		result.httpStatusCode = (long) HttpServletResponse.SC_OK;
		return result;
	}
}