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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.att.eelf.configuration.Configuration;

import org.onap.portalapp.controller.EPRestrictedBaseController;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.logging.aop.EPEELFLoggerAdvice;
import org.onap.portalapp.portal.logging.logic.EPLogUtil;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.portal.utils.PortalConstants;
import org.onap.portalapp.util.EPUserUtils;
import org.onap.portalsdk.core.domain.AuditLog;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.service.AuditService;
import org.onap.portalsdk.core.util.SystemProperties;

@RestController
@RequestMapping("/portalApi/auditLog")
public class AuditLogController extends EPRestrictedBaseController {
	private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(DashboardController.class);

	@Autowired
	private AuditService auditService;

	/**
	 * Store audit log of the specified access type.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param affectedAppId
	 *            App ID
	 * @param type
	 *            Access type
	 * @param comment
	 *            Comment
	 */
	@RequestMapping(value = "/store", method = RequestMethod.GET, produces = "application/json")
	public void auditLog(HttpServletRequest request, @RequestParam String affectedAppId, @RequestParam String type,
			@RequestParam String comment) {
		logger.debug(EELFLoggerDelegate.debugLogger, "auditLog: appId {}, type {}, comment {}", affectedAppId, type,
				comment);
		String cd_type = null;
		try {
			EPUser user = EPUserUtils.getUserSession(request);
			/* Check type of Activity CD */
			if (type.equals("app")) {
				cd_type = AuditLog.CD_ACTIVITY_APP_ACCESS;
			} else if (type.equals("tab")) {
				cd_type = AuditLog.CD_ACTIVITY_TAB_ACCESS;
			} else if (type.equals("functional")) {
				cd_type = AuditLog.CD_ACTIVITY_FUNCTIONAL_ACCESS;
			} else if (type.equals("leftMenu")) {
				cd_type = AuditLog.CD_ACTIVITY_LEFT_MENU_ACCESS;
			} else {
				logger.error(EELFLoggerDelegate.errorLogger,
						"Storing auditLog failed! Activity CD type is not correct.");
			}
			/* Store the audit log only if it contains valid Activity CD */
			if (cd_type != null) {
				AuditLog auditLog = new AuditLog();
				auditLog.setActivityCode(cd_type);
				/*
				 * Check affectedAppId and comment and see if these two values
				 * are valid
				 */
				if (comment != null && !comment.equals("") && !comment.equals("undefined"))
					auditLog.setComments(
							EcompPortalUtils.truncateString(comment, PortalConstants.AUDIT_LOG_COMMENT_SIZE));
				if (affectedAppId != null && !affectedAppId.equals("") && !affectedAppId.equals("undefined"))
					auditLog.setAffectedRecordId(affectedAppId);
				long userId = EPUserUtils.getUserId(request);
				auditLog.setUserId(userId);
				MDC.put(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP, EPEELFLoggerAdvice.getCurrentDateTimeUTC());
				MDC.put(EPCommonSystemProperties.PARTNER_NAME, EPCommonSystemProperties.ECOMP_PORTAL_FE);
				MDC.put(Configuration.MDC_SERVICE_NAME, EPCommonSystemProperties.ECOMP_PORTAL_BE);
				if (MDC.get(Configuration.MDC_KEY_REQUEST_ID) == null) {
					String requestId = UUID.randomUUID().toString();
					MDC.put(Configuration.MDC_KEY_REQUEST_ID, requestId);
				}
				// Log file
				MDC.put(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP, EPEELFLoggerAdvice.getCurrentDateTimeUTC());
				SimpleDateFormat ecompLogDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
				String beginDateTime = MDC.get(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP);
				Date beginDate = ecompLogDateFormat.parse(beginDateTime);
				auditService.logActivity(auditLog, null);
				String endDateTime = MDC.get(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP);
				Date endDate = ecompLogDateFormat.parse(endDateTime);
				String timeDifference = String.format("%d", endDate.getTime() - beginDate.getTime());
				MDC.put(SystemProperties.MDC_TIMER, timeDifference);
				MDC.put(EPCommonSystemProperties.STATUS_CODE, "COMPLETE");
				logger.info(EELFLoggerDelegate.auditLogger, EPLogUtil.formatAuditLogMessage(
						"AuditLogController.auditLog", cd_type, user.getOrgUserId(), affectedAppId, comment));
				MDC.remove(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP);
				MDC.remove(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "auditLog failed", e);
			MDC.put(EPCommonSystemProperties.STATUS_CODE, "ERROR");
		} finally{
			MDC.remove(Configuration.MDC_SERVICE_NAME);
			MDC.remove(EPCommonSystemProperties.PARTNER_NAME);
			MDC.remove(SystemProperties.MDC_TIMER);
			MDC.remove(Configuration.MDC_KEY_REQUEST_ID);
			MDC.remove(EPCommonSystemProperties.STATUS_CODE);
		}
	}

}
