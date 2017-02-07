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
package org.openecomp.portalapp.portal.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.common.util.StringUtils;
import org.openecomp.portalapp.controller.EPRestrictedBaseController;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.logging.aop.EPAuditLog;
import org.openecomp.portalapp.portal.service.AdminRolesService;
import org.openecomp.portalapp.portal.service.WidgetService;
import org.openecomp.portalapp.portal.transport.FieldsValidator;
import org.openecomp.portalapp.portal.transport.OnboardingWidget;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalapp.util.EPUserUtils;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class WidgetsController extends EPRestrictedBaseController {
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(WidgetsController.class);
	
	@Autowired
	AdminRolesService adminRolesService;
	@Autowired
	WidgetService widgetService;

	@RequestMapping(value = { "/portalApi/widgets" }, method = RequestMethod.GET, produces = "application/json")
	public List<OnboardingWidget> getOnboardingWidgets(HttpServletRequest request, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		List<OnboardingWidget> onboardingWidgets = null;
		
		if (user == null || user.isGuest()) {
			EcompPortalUtils.setBadPermissions(user, response, "getOnboardingWidgets");
		} else {
			String getType = request.getHeader("X-Widgets-Type");
			if (!StringUtils.isEmpty(getType) && (getType.equals("managed") || getType.equals("all"))) {
				onboardingWidgets = widgetService.getOnboardingWidgets(user, getType.equals("managed"));
			} else {
				logger.debug(EELFLoggerDelegate.debugLogger, "WidgetsController.getOnboardingApps - request must contain header 'X-Widgets-Type' with 'all' or 'managed'");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		}
		
		EcompPortalUtils.logAndSerializeObject("/portalApi/widgets", "GET result =", response.getStatus());
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
		if (userHasPermissions(user, response, "putOnboardingWidget")) {
			onboardingWidget.id = widgetId; // !
			onboardingWidget.normalize();
			fieldsValidator = widgetService.setOnboardingWidget(user, onboardingWidget);
			response.setStatus(fieldsValidator.httpStatusCode.intValue());
		}
		EcompPortalUtils.logAndSerializeObject("/portalApi/widgets/" + widgetId, "GET result =", response.getStatus());
		
		return fieldsValidator;
	}

	// Attention: real json has all OnboardingWidget fields except "id", we use OnboardingWidget for not to create new class for parsing
	@RequestMapping(value = { "/portalApi/widgets" }, method = { RequestMethod.POST }, produces = "application/json")
	public FieldsValidator postOnboardingWidget(HttpServletRequest request, @RequestBody OnboardingWidget onboardingWidget, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		FieldsValidator fieldsValidator = null; ;
		
		if (userHasPermissions(user, response, "postOnboardingWidget")) {
			onboardingWidget.id = null; // !
			onboardingWidget.normalize();
			fieldsValidator = widgetService.setOnboardingWidget(user, onboardingWidget);
			response.setStatus(fieldsValidator.httpStatusCode.intValue());
		}
		
		EcompPortalUtils.logAndSerializeObject("/portalApi/widgets", "POST result =", response.getStatus());
		return fieldsValidator;
	}

	@RequestMapping(value = { "/portalApi/widgets/{widgetId}" }, method = { RequestMethod.DELETE }, produces = "application/json")
	public FieldsValidator deleteOnboardingWidget(HttpServletRequest request, @PathVariable("widgetId") Long widgetId, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		FieldsValidator fieldsValidator = null; ;
		
		if (userHasPermissions(user, response, "deleteOnboardingWidget")) {
			fieldsValidator = widgetService.deleteOnboardingWidget(user, widgetId);
			response.setStatus(fieldsValidator.httpStatusCode.intValue());
		}
		
		EcompPortalUtils.logAndSerializeObject("/portalApi/widgets/" + widgetId, "DELETE result =", response.getStatus());
		return fieldsValidator;
	}
}
