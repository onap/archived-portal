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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.onap.portalapp.controller.EPRestrictedBaseController;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.onap.portalapp.portal.ecomp.model.SearchResultItem;
import org.onap.portalapp.portal.service.DashboardSearchService;
import org.onap.portalapp.portal.transport.CommonWidget;
import org.onap.portalapp.portal.transport.CommonWidgetMeta;
import org.onap.portalapp.util.EPUserUtils;
import org.onap.portalapp.validation.DataValidator;
import org.onap.portalapp.validation.SecureString;
import org.onap.portalsdk.core.domain.support.CollaborateList;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/portalApi/search")
public class DashboardSearchResultController extends EPRestrictedBaseController {

	private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(DashboardSearchResultController.class);
	private DataValidator dataValidator = new DataValidator();

	@Autowired
	private DashboardSearchService searchService;

	/**
	 * Gets all widgets by type: NEW or RESOURCE
	 * 
	 * @param request
	 * @param resourceType
	 *            Request parameter.
	 * @return Rest response wrapped around a CommonWidgetMeta object.
	 */
	@GetMapping(value = "/widgetData", produces = "application/json")
	public PortalRestResponse<CommonWidgetMeta> getWidgetData(HttpServletRequest request,
			@RequestParam String resourceType) {
		if (resourceType !=null){
			SecureString secureString = new SecureString(resourceType);
			if (!dataValidator.isValid(secureString))
				return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "Provided data is invalid", null);
		}
		return new PortalRestResponse<>(PortalRestStatusEnum.OK, "success",
				searchService.getWidgetData(resourceType));
	}

	/**
	 * Saves all: news and resources
	 * 
	 * @param commonWidgetMeta
	 *            read from POST body.
	 * @return Rest response wrapped around a String; e.g., "success" or "ERROR"
	 */
	@PostMapping(value = "/widgetDataBulk", produces = "application/json")
	public PortalRestResponse<String> saveWidgetDataBulk(@RequestBody CommonWidgetMeta commonWidgetMeta) {
		logger.debug(EELFLoggerDelegate.debugLogger, "saveWidgetDataBulk: argument is {}", commonWidgetMeta);
		if (commonWidgetMeta.getCategory() == null || commonWidgetMeta.getCategory().trim().equals("")){
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "ERROR",
					"Category cannot be null or empty");
		}else {
			if(!dataValidator.isValid(commonWidgetMeta))
				return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "ERROR",
					"Category is not valid");
		}
		// validate dates
		for (CommonWidget cw : commonWidgetMeta.getItems()) {
			String err = validateCommonWidget(cw);
			if (err != null)
				return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, err, null);
		}
		return new PortalRestResponse<String>(PortalRestStatusEnum.OK, "success",
				searchService.saveWidgetDataBulk(commonWidgetMeta));
	}

	/**
	 * Saves one: news or resource
	 * 
	 * @param commonWidget
	 *            read from POST body
	 * @return Rest response wrapped around a String; e.g., "success" or "ERROR"
	 */
	@PostMapping(value = "/widgetData", produces = "application/json")
	public PortalRestResponse<String> saveWidgetData(@RequestBody CommonWidget commonWidget) {
		logger.debug(EELFLoggerDelegate.debugLogger, "saveWidgetData: argument is {}", commonWidget);
		if (commonWidget.getCategory() == null || commonWidget.getCategory().trim().equals("")){
			return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "ERROR",
					"Cateogry cannot be null or empty");
		}else {
			if(!dataValidator.isValid(commonWidget))
				return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "ERROR",
					"Category is not valid");
		}
		String err = validateCommonWidget(commonWidget);
		if (err != null)
			return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, err, null);
		return new PortalRestResponse<>(PortalRestStatusEnum.OK, "success",
				searchService.saveWidgetData(commonWidget));
	}

	/**
	 * Used by the validate function
	 */
	private final SimpleDateFormat yearMonthDayFormat = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * Validates the content of a common widget.
	 * 
	 * @param cw
	 * @return null on success; an error message if validation fails.
	 * @throws Exception
	 */
	private String validateCommonWidget(CommonWidget cw) {
		try {
			if (cw.getEventDate() != null && cw.getEventDate().trim().length() > 0)
				yearMonthDayFormat.parse(cw.getEventDate());
		} catch (ParseException ex) {
			return ex.toString();
		}
		return null;
	}

	/**
	 * Deletes one: news or resource
	 * 
	 * @param commonWidget
	 *            read from POST body
	 * @return Rest response wrapped around a String; e.g., "success" or "ERROR"
	 */
	@PostMapping(value = "/deleteData", produces = "application/json")
	public PortalRestResponse<String> deleteWidgetData(@RequestBody CommonWidget commonWidget) {
		logger.debug(EELFLoggerDelegate.debugLogger, "deleteWidgetData: argument is {}", commonWidget);
		if(!dataValidator.isValid(commonWidget))
			return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "ERROR",
				"Data is not valid");
		return new PortalRestResponse<>(PortalRestStatusEnum.OK, "success",
				searchService.deleteWidgetData(commonWidget));
	}

	/**
	 * Searches all portal for the input string.
	 * 
	 * @param request
	 * @param searchString
	 * @return Rest response wrapped around a Map of String to List of Search
	 *         Result Item.
	 */
	@GetMapping(value = "/allPortal", produces = "application/json")
	public PortalRestResponse<Map<String, List<SearchResultItem>>> searchPortal(HttpServletRequest request,
			@RequestParam String searchString) {
		if(searchString!=null){
			SecureString secureString = new SecureString(searchString);
			if(!dataValidator.isValid(secureString)){
				return new PortalRestResponse<>(PortalRestStatusEnum.ERROR,
					"searchPortal: User object is invalid",
					null);
			}
		}

		EPUser user = EPUserUtils.getUserSession(request);
		try {
			if (user == null) {
				return new PortalRestResponse<>(PortalRestStatusEnum.ERROR,
						"searchPortal: User object is null? - check logs",
						new HashMap<>());
			} else if (searchString == null || searchString.trim().length() == 0) {
				return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "searchPortal: String string is null",
						new HashMap<>());
			} else {
				logger.debug(EELFLoggerDelegate.debugLogger, "searchPortal: user {}, search string '{}'",
						user.getLoginId(), searchString);
				Map<String, List<SearchResultItem>> results = searchService.searchResults(user.getLoginId(),
						searchString);
				return new PortalRestResponse<>(PortalRestStatusEnum.OK, "success", results);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "searchPortal failed", e);
			return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, e.getMessage() + " - check logs.",
					new HashMap<>());
		}
	}

	/**
	 * Gets all active users.
	 * 
	 * TODO: should only the superuser be allowed to use this API?
	 * 
	 * @param request
	 * @return Rest response wrapped around a list of String
	 */
	@GetMapping(value = "/activeUsers", produces = "application/json")
	public List<String> getActiveUsers(HttpServletRequest request) {
		List<String> activeUsers = null;
		List<String> onlineUsers = new ArrayList<>();
		try {
			EPUser user = EPUserUtils.getUserSession(request);
			String userId = user.getOrgUserId();

			activeUsers = searchService.getRelatedUsers(userId);
			HashSet<String> usersSet = (HashSet<String>) CollaborateList.getInstance().getAllUserName();
			for (String users : activeUsers) {
				if (usersSet.contains(users)) {
					onlineUsers.add(users);
				}
			}

		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getActiveUsers failed", e);
		}
		return onlineUsers;
	}

	/**
	 * Gets only those users that are 'related' to the currently logged-in user.
	 * 
	 * @param request
	 * @return Rest response wrapped around a List of String
	 */
	@GetMapping(value = "/relatedUsers", produces = "application/json")
	public PortalRestResponse<List<String>> activeUsers(HttpServletRequest request) {
		EPUser user = EPUserUtils.getUserSession(request);
		try {
			if (user == null) {
				return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "User object is null? - check logs",
						new ArrayList<>());
			} else {
				logger.debug(EELFLoggerDelegate.debugLogger, "activeUsers: searching for user {}", user.getLoginId());
				return new PortalRestResponse<>(PortalRestStatusEnum.OK, "success",
						searchService.getRelatedUsers(user.getLoginId()));
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "activeUsers failed", e);
			return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, e.getMessage() + " - check logs.",
					new ArrayList<>());
		}
	}

}
