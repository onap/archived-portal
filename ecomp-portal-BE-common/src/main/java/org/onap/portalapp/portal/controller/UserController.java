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

import javax.servlet.http.HttpServletRequest;

import org.onap.portalapp.controller.EPRestrictedBaseController;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.onap.portalapp.portal.logging.aop.EPAuditLog;
import org.onap.portalapp.portal.service.UserService;
import org.onap.portalapp.portal.transport.ProfileDetail;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.util.EPUserUtils;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.onboarding.util.CipherUtil;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class UserController extends EPRestrictedBaseController {

	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	private static final String HIDDEN_DEFAULT_PASSWORD = "*****";

	/**
	 * RESTful service method to get ONAP Logged in User details.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * 
	 * @return PortalRestResponse of EPUser
	 */
	@RequestMapping(value = { "/portalApi/loggedinUser" }, method = RequestMethod.GET, produces = "application/json")
	public PortalRestResponse<ProfileDetail> getLoggedinUser(HttpServletRequest request) {
		PortalRestResponse<ProfileDetail> portalRestResponse = null;
		try {
			EPUser user = EPUserUtils.getUserSession(request);
			ProfileDetail profileDetail = new ProfileDetail(user.getFirstName(), user.getLastName(),
					user.getMiddleInitial(), user.getEmail(), user.getLoginId(),  HIDDEN_DEFAULT_PASSWORD);
			portalRestResponse = new PortalRestResponse<ProfileDetail>(PortalRestStatusEnum.OK, "success",
					profileDetail);
			EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/loggedinUser", "result =", profileDetail);
		} catch (Exception e) {
			portalRestResponse = new PortalRestResponse<ProfileDetail>(PortalRestStatusEnum.ERROR, e.getMessage(),
					null);
			logger.error(EELFLoggerDelegate.errorLogger, "getLoggedinUser failed", e);
		}
		return portalRestResponse;
	}

	/**
	 * RESTful service method to update ONAP Logged in User in DB.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param profileDetail
	 *            Body with user information
	 * @return PortalRestResponse of String
	 */
	@RequestMapping(value = {
			"/portalApi/modifyLoggedinUser" }, method = RequestMethod.PUT, produces = "application/json")
	public PortalRestResponse<String> modifyLoggedinUser(HttpServletRequest request,
			@RequestBody ProfileDetail profileDetail) {
		PortalRestResponse<String> portalRestResponse = null;
		try {
			String errorMsg = "";
			if (profileDetail.getFirstName().equals("") || profileDetail.getLastName().equals("")
					|| profileDetail.getEmail().equals("") || profileDetail.getLoginId().equals("")
					|| profileDetail.getLoginPassword().equals("")) {
				errorMsg = "Required field(s) is missing";
				portalRestResponse = new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, errorMsg, null);
				logger.error(EELFLoggerDelegate.errorLogger, "modifyLoggedinUser failed", errorMsg);
			} else {
				EPUser user = EPUserUtils.getUserSession(request);
				user.setFirstName(profileDetail.getFirstName());
				user.setLastName(profileDetail.getLastName());
				user.setEmail(profileDetail.getEmail());
				user.setMiddleInitial(profileDetail.getMiddleName());
				user.setLoginId(profileDetail.getLoginId());
				if (!HIDDEN_DEFAULT_PASSWORD.equals(profileDetail.getLoginPassword())){
					user.setLoginPwd(CipherUtil.encryptPKC(profileDetail.getLoginPassword()));
				}
				userService.saveUser(user);
				// Update user info in the session
				request.getSession().setAttribute(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME),
						user);
				portalRestResponse = new PortalRestResponse<String>(PortalRestStatusEnum.OK, "success", null);
				EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/modifyLoggedinUser", "result =", user);
			}
		} catch (Exception e) {
			portalRestResponse = new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, e.toString(), null);
			logger.error(EELFLoggerDelegate.errorLogger, "modifyLoggedinUser failed", e);
		}
		return portalRestResponse;
	}
}
