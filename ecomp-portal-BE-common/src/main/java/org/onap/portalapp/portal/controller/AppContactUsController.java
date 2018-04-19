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

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.onap.portalapp.controller.EPRestrictedBaseController;
import org.onap.portalapp.portal.ecomp.model.AppCategoryFunctionsItem;
import org.onap.portalapp.portal.ecomp.model.AppContactUsItem;
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.onap.portalapp.portal.logging.aop.EPAuditLog;
import org.onap.portalapp.portal.service.AppContactUsService;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/portalApi/contactus")
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class AppContactUsController extends EPRestrictedBaseController {

	static final String FAILURE = "failure";

	private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(AppContactUsController.class);

	@Autowired
	private AppContactUsService contactUsService;

	/**
	 * Answers a JSON object with three items from the system.properties file:
	 * user self-help ticket URL, email for feedback, and Portal info link.
	 * 
	 * @param request HttpServletRequest
	 * @return PortalRestResponse
	 */
	@RequestMapping(value = "/feedback", method = RequestMethod.GET, produces = "application/json")
	public PortalRestResponse<String> getPortalDetails(HttpServletRequest request) {
		PortalRestResponse<String> portalRestResponse = null;
		try {
			final String ticketUrl = SystemProperties.getProperty(EPCommonSystemProperties.USH_TICKET_URL);
			final String portalInfoUrl = SystemProperties.getProperty(EPCommonSystemProperties.PORTAL_INFO_URL);
			final String feedbackEmail = SystemProperties.getProperty(EPCommonSystemProperties.FEEDBACK_EMAIL_ADDRESS);
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(EPCommonSystemProperties.USH_TICKET_URL, ticketUrl);
			map.put(EPCommonSystemProperties.PORTAL_INFO_URL, portalInfoUrl);
			map.put(EPCommonSystemProperties.FEEDBACK_EMAIL_ADDRESS, feedbackEmail);
			JSONObject j = new JSONObject(map);
			String contactUsPortalResponse = j.toString();
			portalRestResponse = new PortalRestResponse<String>(PortalRestStatusEnum.OK, "success",
					contactUsPortalResponse);
		} catch (Exception e) {
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, FAILURE, e.getMessage());
		}
		return portalRestResponse;
	}

	/**
	 * Answers the contents of the contact-us table, extended with the
	 * application name.
	 * 
	 * @param request HttpServletRequest
	 * @return PortalRestResponse<List<AppContactUsItem>>
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
	public PortalRestResponse<List<AppContactUsItem>> getAppContactUsList(HttpServletRequest request) {
		PortalRestResponse<List<AppContactUsItem>> portalRestResponse = null;
		try {
			List<AppContactUsItem> contents = contactUsService.getAppContactUs();
			portalRestResponse = new PortalRestResponse<List<AppContactUsItem>>(PortalRestStatusEnum.OK, "success",
					contents);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getAppContactUsList failed", e);
			portalRestResponse = new PortalRestResponse<List<AppContactUsItem>>(PortalRestStatusEnum.ERROR,
					e.getMessage(), null);
		}
		return portalRestResponse;
	}

	/**
	 * Answers a list of objects, one per application, extended with available
	 * data on how to contact that app's organization (possibly none).
	 * 
	 * @param request HttpServletRequest
	 * @return PortalRestResponse<List<AppContactUsItem>>
	 */
	@RequestMapping(value = "/allapps", method = RequestMethod.GET, produces = "application/json")
	public PortalRestResponse<List<AppContactUsItem>> getAppsAndContacts(HttpServletRequest request) {
		PortalRestResponse<List<AppContactUsItem>> portalRestResponse = null;
		try {
			List<AppContactUsItem> contents = contactUsService.getAppsAndContacts();
			portalRestResponse = new PortalRestResponse<List<AppContactUsItem>>(PortalRestStatusEnum.OK, "success",
					contents);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getAllAppsAndContacts failed", e);
			portalRestResponse = new PortalRestResponse<List<AppContactUsItem>>(PortalRestStatusEnum.ERROR,
					e.getMessage(), null);
		}
		return portalRestResponse;
	}

	/**
	 * Sorts by category name.
	 */
	private Comparator<AppCategoryFunctionsItem> appCategoryFunctionsItemComparator = new Comparator<AppCategoryFunctionsItem>() {
		@Override
		public int compare(AppCategoryFunctionsItem o1, AppCategoryFunctionsItem o2) {
			return o1.getCategory().compareTo(o2.getCategory());
		}
	};
	
	/**
	 * Answers a list of objects with category-application-function details. Not
	 * all applications participate in the functional menu.
	 * 
	 * @param request HttpServletRequest
	 * @return PortalRestResponse<List<AppCategoryFunctionsItem>>
	 */
	@RequestMapping(value = "/functions", method = RequestMethod.GET, produces = "application/json")
	public PortalRestResponse<List<AppCategoryFunctionsItem>> getAppCategoryFunctions(HttpServletRequest request) {
		PortalRestResponse<List<AppCategoryFunctionsItem>> portalRestResponse = null;
		try {
			List<AppCategoryFunctionsItem> contents = contactUsService.getAppCategoryFunctions();
			// logger.debug(EELFLoggerDelegate.debugLogger,
			// "getAppCategoryFunctions: result list size is " +
			// contents.size());
			Collections.sort(contents, appCategoryFunctionsItemComparator);
			portalRestResponse = new PortalRestResponse<List<AppCategoryFunctionsItem>>(PortalRestStatusEnum.OK,
					"success", contents);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getAppCategoryFunctions failed", e);
			// TODO build JSON error
			portalRestResponse = new PortalRestResponse<List<AppCategoryFunctionsItem>>(PortalRestStatusEnum.ERROR,
					e.getMessage(), null);
		}
		return portalRestResponse;
	}

	/**
	 * Accepts a new application's contact us details.
	 * 
	 * @param contactUs AppContactUsItem
	 * @return PortalRestResponse<String>
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json")
	public PortalRestResponse<String> save(@RequestBody AppContactUsItem contactUs) {

		if (contactUs == null || contactUs.getAppName() == null)
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, FAILURE,
					"AppName cannot be null or empty");

		String saveAppContactUs = FAILURE;
		try {
			saveAppContactUs = contactUsService.saveAppContactUs(contactUs);
		} catch (Exception e) {
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, saveAppContactUs, e.getMessage());
		}
		return new PortalRestResponse<String>(PortalRestStatusEnum.OK, saveAppContactUs, "");
	}

	@RequestMapping(value = "/saveAll", method = RequestMethod.POST, produces = "application/json")
	public PortalRestResponse<String> save(@RequestBody List<AppContactUsItem> contactUsList) {

		String saveAppContactUs = FAILURE;
		try {
			saveAppContactUs = contactUsService.saveAppContactUs(contactUsList);
		} catch (Exception e) {
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, saveAppContactUs, e.getMessage());
		}
		return new PortalRestResponse<String>(PortalRestStatusEnum.OK, saveAppContactUs, "");
	}

	/**
	 * Deletes the specified application's contact-us details entry from the
	 * table.
	 * 
	 * @param id app ID
	 * @return PortalRestResponse<String>
	 */
	@RequestMapping(value = "/delete/{appid}", method = RequestMethod.POST, produces = "application/json")
	public PortalRestResponse<String> delete(@PathVariable("appid") Long id) {

		String saveAppContactUs = FAILURE;
		try {
			saveAppContactUs = contactUsService.deleteContactUs(id);
		} catch (Exception e) {
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, saveAppContactUs, e.getMessage());
		}
		return new PortalRestResponse<String>(PortalRestStatusEnum.OK, saveAppContactUs, "");
	}

}