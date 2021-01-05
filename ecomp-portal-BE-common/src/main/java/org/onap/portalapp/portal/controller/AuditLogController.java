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

import org.onap.portalapp.validation.DataValidator;
import org.onap.portalapp.validation.SecureString;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.att.eelf.configuration.Configuration;

import org.onap.portalapp.controller.EPRestrictedBaseController;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.EcompAuditLog;
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
	private static final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(AuditLogController.class);
	private static final DataValidator dataValidator = new DataValidator();
	

	private AuditService auditService;
	@Autowired
	public AuditLogController(AuditService auditService) {
		this.auditService = auditService;
	}

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
	@GetMapping(value = "/store", produces = "application/json")
	public void auditLog(HttpServletRequest request, @RequestParam String affectedAppId, @RequestParam String type,
			@RequestParam String comment) {
		logger.debug(EELFLoggerDelegate.debugLogger, "auditLog: appId {}, type {}, comment {}", affectedAppId, type,
				comment);
		String cdType = null;

		SecureString secureString0 = new SecureString(affectedAppId);
		SecureString secureString1 = new SecureString(type);
		SecureString secureString2 = new SecureString(comment);
		if (  !dataValidator.isValid(secureString0)
			||!dataValidator.isValid(secureString1)
			||!dataValidator.isValid(secureString2)){
			return;
		}

		try {
			EPUser user = EPUserUtils.getUserSession(request);
			/* Check type of Activity CD */
			switch (type) {
				case "app":
					cdType = AuditLog.CD_ACTIVITY_APP_ACCESS;
					break;
				case "tab":
					cdType = AuditLog.CD_ACTIVITY_TAB_ACCESS;
					break;
				case "functional":
					cdType = AuditLog.CD_ACTIVITY_FUNCTIONAL_ACCESS;
					break;
				case "leftMenu":
					cdType = AuditLog.CD_ACTIVITY_LEFT_MENU_ACCESS;
					break;
				default:
					logger.error(EELFLoggerDelegate.errorLogger,
						"Storing auditLog failed! Activity CD type is not correct.");
					break;
			}
			/* Store the audit log only if it contains valid Activity CD */
			if (cdType != null) {
				AuditLog auditLog = new AuditLog();
				auditLog.setActivityCode(cdType);
				/*
				 * Check affectedAppId and comment and see if these two values
				 * are valid
				 */
				if (comment != null && !comment.isEmpty() && !"undefined".equals(comment))
					auditLog.setComments(
							EcompPortalUtils.truncateString(comment, PortalConstants.AUDIT_LOG_COMMENT_SIZE));
				if (affectedAppId != null && !affectedAppId.isEmpty() && !"undefined".equals(affectedAppId))
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
				auditService.logActivity(auditLog, null);
				String auditMessageInfo = EPLogUtil.formatAuditLogMessage(
						"AuditLogController.auditLog", cdType, user.getOrgUserId(), affectedAppId, comment);		
		
				EPLogUtil.logAuditMessage(logger, auditMessageInfo);				

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
