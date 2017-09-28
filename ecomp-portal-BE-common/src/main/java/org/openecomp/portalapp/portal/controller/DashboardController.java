/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright © 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the “License”);
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
 * under the Creative Commons License, Attribution 4.0 Intl. (the “License”);
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
package org.openecomp.portalapp.portal.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.openecomp.portalapp.controller.EPRestrictedBaseController;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.domain.EcompAuditLog;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestResponse;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.openecomp.portalapp.portal.ecomp.model.SearchResultItem;
import org.openecomp.portalapp.portal.logging.aop.EPAuditLog;
import org.openecomp.portalapp.portal.logging.aop.EPEELFLoggerAdvice;
import org.openecomp.portalapp.portal.logging.logic.EPLogUtil;
import org.openecomp.portalapp.portal.service.DashboardSearchService;
import org.openecomp.portalapp.portal.transport.CommonWidget;
import org.openecomp.portalapp.portal.transport.CommonWidgetMeta;
import org.openecomp.portalapp.portal.utils.EPCommonSystemProperties;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalapp.portal.utils.PortalConstants;
import org.openecomp.portalapp.util.EPUserUtils;
import org.openecomp.portalsdk.core.domain.AuditLog;
import org.openecomp.portalsdk.core.domain.support.CollaborateList;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.service.AuditService;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller supplies data to Angular services on the dashboard page.
 */
@Configuration
@RestController
@RequestMapping("/portalApi/dashboard")
public class DashboardController extends EPRestrictedBaseController {

	private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(DashboardController.class);

	@Autowired
	private DashboardSearchService searchService;
	@Autowired
	private AuditService auditService;
	
	public enum WidgetCategory {
		EVENTS, NEWS, IMPORTANTRESOURCES;
	}

	/**
	 * Validates the resource type parameter.
	 * 
	 * @param resourceType
	 * @return True if known in the enum WidgetCategory, else false.
	 */
	private boolean isValidResourceType(String resourceType) {
		if (resourceType == null)
			return false;
		for (WidgetCategory wc : WidgetCategory.values())
			if (wc.name().equals(resourceType))
				return true;
		return false;
	}

	/**
	 * Gets all widgets of the specified resource type. 
	 * In iteration 41 (when widget will utilized service onboarding), this method can be removed, instead we will use CommonWidgetController.java (basic auth based)
	 * 
	 * @param request
	 * @param resourceType
	 *            Request parameter.
	 * @return Rest response wrapped around a CommonWidgetMeta object.
	 */
	@RequestMapping(value = "/widgetData", method = RequestMethod.GET, produces = "application/json")
	public PortalRestResponse<CommonWidgetMeta> getWidgetData(HttpServletRequest request,
			@RequestParam String resourceType) {
		if (!isValidResourceType(resourceType))
			return new PortalRestResponse<CommonWidgetMeta>(PortalRestStatusEnum.ERROR,
					"Unexpected resource type " + resourceType, null);
		return new PortalRestResponse<CommonWidgetMeta>(PortalRestStatusEnum.OK, "success",
				searchService.getWidgetData(resourceType));
	}
	
	
	/**
	 * Saves a batch of events, news or resources.
	 * 
	 * @param commonWidgetMeta
	 *            read from POST body.
	 * @return Rest response wrapped around a String; e.g., "success" or "ERROR"
	 */
	@RequestMapping(value = "/widgetDataBulk", method = RequestMethod.POST, produces = "application/json")
	public PortalRestResponse<String> saveWidgetDataBulk(@RequestBody CommonWidgetMeta commonWidgetMeta) {
		logger.debug(EELFLoggerDelegate.debugLogger, "saveWidgetDataBulk: argument is {}", commonWidgetMeta);
		if (commonWidgetMeta.getCategory() == null || commonWidgetMeta.getCategory().trim().equals(""))
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "ERROR",
					"Category cannot be null or empty");
		if (!isValidResourceType(commonWidgetMeta.getCategory()))
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR,
					"Unexpected resource type " + commonWidgetMeta.getCategory(), null);
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
	 * Saves one: event, news or resource
	 * 
	 * @param commonWidget
	 *            read from POST body
	 * @return Rest response wrapped around a String; e.g., "success" or "ERROR"
	 */
	@RequestMapping(value = "/widgetData", method = RequestMethod.POST, produces = "application/json")
	public PortalRestResponse<String> saveWidgetData(@RequestBody CommonWidget commonWidget) {
		logger.debug(EELFLoggerDelegate.debugLogger, "saveWidgetData: argument is {}", commonWidget);
		if (commonWidget.getCategory() == null || commonWidget.getCategory().trim().equals(""))
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "ERROR",
					"Category cannot be null or empty");
		String err = validateCommonWidget(commonWidget);
		if (err != null)
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, err, null);
		return new PortalRestResponse<String>(PortalRestStatusEnum.OK, "success",
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
		if (!isValidResourceType(cw.getCategory()))
			return "Invalid category: " + cw.getCategory();
		if (cw.getTitle() == null || cw.getTitle().trim().length() == 0)
			return "Title is missing";
		if (cw.getHref() == null || cw.getHref().trim().length() == 0)
			return "HREF is missing";
		if (!cw.getHref().toLowerCase().startsWith("http"))
			return "HREF does not start with http";
		if (cw.getSortOrder() == null)
			return "Sort order is null";
		if (WidgetCategory.EVENTS.name().equals(cw.getCategory())) {
			if (cw.getEventDate() == null || cw.getEventDate().trim().length() == 0)
				return "Date is missing";
			try {
				yearMonthDayFormat.setLenient(false);
				Date date = yearMonthDayFormat.parse(cw.getEventDate());
				if (date == null)
					return "Failed to parse date " + cw.getEventDate();
			} catch (ParseException ex) {
				return ex.toString();
			}
		}
		return null;
	}

	/**
	 * Deletes one: event, news or resource
	 * 
	 * @param commonWidget
	 *            read from POST body
	 * @return Rest response wrapped around a String; e.g., "success" or "ERROR"
	 */
	@RequestMapping(value = "/deleteData", method = RequestMethod.POST, produces = "application/json")
	public PortalRestResponse<String> deleteWidgetData(@RequestBody CommonWidget commonWidget) {
		logger.debug(EELFLoggerDelegate.debugLogger, "deleteWidgetData: argument is {}", commonWidget);
		return new PortalRestResponse<String>(PortalRestStatusEnum.OK, "success",
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
	@EPAuditLog
	@RequestMapping(value = "/search", method = RequestMethod.GET, produces = "application/json")
	public PortalRestResponse<Map<String, List<SearchResultItem>>> searchPortal(HttpServletRequest request,
			@RequestParam String searchString) {

		if (searchString != null)
			searchString = searchString.trim();
		EPUser user = EPUserUtils.getUserSession(request);
		try {
			if (user == null) {
				return new PortalRestResponse<>(PortalRestStatusEnum.ERROR,
						"searchPortal: User object is null? - check logs",
						new HashMap<String, List<SearchResultItem>>());
			} else if (searchString == null || searchString.length() == 0) {
				return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "searchPortal: String string is null",
						new HashMap<String, List<SearchResultItem>>());
			} else {
				logger.debug(EELFLoggerDelegate.debugLogger, "searchPortal: user {}, search string '{}'",
						user.getLoginId(), searchString);
				Map<String, List<SearchResultItem>> results = searchService.searchResults(user.getLoginId(),
						searchString);
				/*Audit log the search*/
				AuditLog auditLog = new AuditLog();
				auditLog.setUserId(user.getId());
				auditLog.setActivityCode(EcompAuditLog.CD_ACTIVITY_SEARCH);
				auditLog.setComments(EcompPortalUtils.truncateString(searchString, PortalConstants.AUDIT_LOG_COMMENT_SIZE));
				MDC.put(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP,EPEELFLoggerAdvice.getCurrentDateTimeUTC());		
				auditService.logActivity(auditLog, null);
				MDC.put(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP,EPEELFLoggerAdvice.getCurrentDateTimeUTC());
				EcompPortalUtils.calculateDateTimeDifferenceForLog(MDC.get(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP),MDC.get(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP));
				logger.info(EELFLoggerDelegate.auditLogger, EPLogUtil.formatAuditLogMessage("DashboardController.PortalRestResponse", 
						EcompAuditLog.CD_ACTIVITY_SEARCH, user.getOrgUserId(), null, searchString));	
				MDC.remove(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP);
				MDC.remove(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP);
				MDC.remove(SystemProperties.MDC_TIMER);
				return new PortalRestResponse<>(PortalRestStatusEnum.OK, "success", results);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "searchPortal failed", e);
			return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, e.getMessage() + " - check logs.",
					new HashMap<String, List<SearchResultItem>>());
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
	@RequestMapping(value = "/activeUsers", method = RequestMethod.GET, produces = "application/json")
	public List<String> getActiveUsers(HttpServletRequest request) {
		List<String> activeUsers = null;
		List<String> onlineUsers = new ArrayList<>();
		try {
			EPUser user = EPUserUtils.getUserSession(request);
			String userId = user.getOrgUserId();

			activeUsers = searchService.getRelatedUsers(userId);
			HashSet<String> usersSet = CollaborateList.getInstance().getAllUserName();
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
	 * Gets the refresh interval and duration of a cycle of continuous refreshing for the online users side panel, both in milliseconds.
	 * 
	 * @param request
	 * @return Rest response wrapped around a number that is the number of milliseconds.
	 */
	@RequestMapping(value = "/onlineUserUpdateRate", method = RequestMethod.GET, produces = "application/json")
	public PortalRestResponse<Map<String, String>> getOnlineUserUpdateRate(HttpServletRequest request) {
		try {
			String updateRate = SystemProperties.getProperty(EPCommonSystemProperties.ONLINE_USER_UPDATE_RATE);	
			String updateDuration = SystemProperties.getProperty(EPCommonSystemProperties.ONLINE_USER_UPDATE_DURATION);				
			Integer rateInMiliSec = Integer.valueOf(updateRate)*1000;
			Integer durationInMiliSec = Integer.valueOf(updateDuration)*1000;
			Map<String, String> results = new HashMap<String,String>();
			results.put("onlineUserUpdateRate", String.valueOf(rateInMiliSec));
			results.put("onlineUserUpdateDuration", String.valueOf(durationInMiliSec));			
			return new PortalRestResponse<>(PortalRestStatusEnum.OK, "success", results);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getOnlineUserUpdateRate failed", e);
			return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, e.toString(), null);
		}		
	}

	/**
	 * Gets the window width threshold for collapsing right menu from system.properties.
	 * 
	 * @param request
	 * @return Rest response wrapped around a number that is the window width threshold to collapse right menu.
	 */
	@RequestMapping(value = "/windowWidthThresholdRightMenu", method = RequestMethod.GET, produces = "application/json")
	public PortalRestResponse<Map<String, String>> getWindowWidthThresholdForRightMenu(HttpServletRequest request) {
		try {
			String windowWidthString = SystemProperties.getProperty(EPCommonSystemProperties.WINDOW_WIDTH_THRESHOLD_RIGHT_MENU);	
			Integer windowWidth = Integer.valueOf(windowWidthString);
			Map<String, String> results = new HashMap<String,String>();
			results.put("windowWidth", String.valueOf(windowWidth));
			return new PortalRestResponse<>(PortalRestStatusEnum.OK, "success", results);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getWindowWidthThresholdForRightMenu failed", e);
			return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, e.toString(), null);
		}		
	}


	/**
	 * Gets the window width threshold for collapsing left menu from system.properties.
	 * 
	 * @param request
	 * @return Rest response wrapped around a number that is the window width threshold to collapse the left menu.
	 */
	@RequestMapping(value = "/windowWidthThresholdLeftMenu", method = RequestMethod.GET, produces = "application/json")
	public PortalRestResponse<Map<String, String>> getWindowWidthThresholdForLeftMenu(HttpServletRequest request) {
		try {
			String windowWidthString = SystemProperties.getProperty(EPCommonSystemProperties.WINDOW_WIDTH_THRESHOLD_LEFT_MENU);	
			Integer windowWidth = Integer.valueOf(windowWidthString);
			Map<String, String> results = new HashMap<String,String>();
			results.put("windowWidth", String.valueOf(windowWidth));
			return new PortalRestResponse<>(PortalRestStatusEnum.OK, "success", results);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getWindowWidthThresholdForLeftMenu failed", e);
			return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, e.toString(), null);
		}		
	}
	
	/**
	 * Gets only those users that are 'related' to the currently logged-in user.
	 * 
	 * @param request
	 * @return Rest response wrapped around a List of String
	 */
	@RequestMapping(value = "/relatedUsers", method = RequestMethod.GET, produces = "application/json")
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
