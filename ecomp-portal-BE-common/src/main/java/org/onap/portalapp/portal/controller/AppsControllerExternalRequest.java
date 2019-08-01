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

import java.util.List;

import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.onap.portalapp.portal.logging.aop.EPAuditLog;
import org.onap.portalapp.portal.service.AdminRolesService;
import org.onap.portalapp.portal.service.EPAppService;
import org.onap.portalapp.portal.service.PortalAdminService;
import org.onap.portalapp.portal.service.UserService;
import org.onap.portalapp.portal.transport.FieldsValidator;
import org.onap.portalapp.portal.transport.OnboardingApp;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.portal.utils.PortalConstants;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

/**
 * Processes requests from external systems (i.e., not the front-end web UI).
 * First use case is ONAP Controller, which has to create an admin and onboard
 * itself upon launch of a fresh Portal.
 * 
 * Listens on the "auxapi" path prefix. Provides alternate implementations of
 * methods in several existing controllers because an EPUser object is not
 * available in the session for these requests.
 * 
 * Checks credentials sent via HTTP Basic Authentication. The Portal's basic
 * HTTP authentication system requires that the user names and endpoints are
 * registered together.
 */
@RestController
@RequestMapping(PortalConstants.REST_AUX_API)
@Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class AppsControllerExternalRequest implements BasicAuthenticationController {
	private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();

	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(AppsControllerExternalRequest.class);

	private static final String ONBOARD_APP = "/onboardApp";

	/**
	 * For testing whether a user is a superadmin.
	 */
	@Autowired
	private AdminRolesService adminRolesService;

	/**
	 * For onboarding or updating an app
	 */
	@Autowired
	private EPAppService appService;

	/**
	 * For promoting a user to Portal admin
	 */
	@Autowired
	private PortalAdminService portalAdminService;

	/**
	 * For creating a new user
	 */
	@Autowired
	private UserService userService;

	/**
	 * Creates a new user as a Portal administrator.
	 * 
	 * <PRE>
	 { 
		"loginId" : "abc123",
		"loginPwd": "",
		"email":"ecomp@controller" 
	 }
	 * </PRE>
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param epUser
	 *            User details; the email and orgUserId fields are mandatory
	 * @param response
	 *            HttpServletResponse
	 * @return PortalRestResponse with success or failure
	 */
	@ApiOperation(value = "Creates a new user as a Portal administrator.", response = PortalRestResponse.class)
	@RequestMapping(value = "/portalAdmin", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public PortalRestResponse<String> postPortalAdmin(HttpServletRequest request, HttpServletResponse response,
			@Valid @RequestBody EPUser epUser) {
		EcompPortalUtils.logAndSerializeObject(logger, "postPortalAdmin", "request", epUser);
		PortalRestResponse<String> portalResponse = new PortalRestResponse<>();

        if (epUser != null) {
            Validator validator = VALIDATOR_FACTORY.getValidator();
            Set<ConstraintViolation<EPUser>> constraintViolations = validator.validate(epUser);
            if (!constraintViolations.isEmpty()) {
                portalResponse.setStatus(PortalRestStatusEnum.ERROR);
                portalResponse.setMessage("Data is not valid");
                return portalResponse;
            }
        }

        // Check mandatory fields.
        if (epUser != null && (epUser.getEmail() == null || epUser.getEmail().trim().length() == 0 //
                || epUser.getLoginId() == null || epUser.getLoginId().trim().length() == 0 //
                || epUser.getLoginPwd() == null)) {
            portalResponse.setStatus(PortalRestStatusEnum.ERROR);
            portalResponse.setMessage("Missing required field: email, loginId, or loginPwd");
            return portalResponse;
        }

		try {
            // Check for existing user; create if not found.
            List<EPUser> userList = null;
            if (epUser != null) {
                userList = userService.getUserByUserId(epUser.getOrgUserId());
            }

			if (userList == null || userList.isEmpty()) {
				// Create user with first, last names etc.; do check for
				// duplicates.
				String userCreateResult = userService.saveNewUser(epUser, "Yes");
				if (!"success".equals(userCreateResult)) {
					portalResponse.setStatus(PortalRestStatusEnum.ERROR);
					portalResponse.setMessage(userCreateResult);
					return portalResponse;
				}
			}

			// Check for Portal admin status; promote if not.
            if (adminRolesService.isSuperAdmin(epUser)) {
                portalResponse.setStatus(PortalRestStatusEnum.OK);
            } else {
                FieldsValidator fv = null;
                if (epUser != null) {
                    fv = portalAdminService.createPortalAdmin(epUser.getOrgUserId());
                }
                if (fv != null && fv.httpStatusCode.intValue() == HttpServletResponse.SC_OK) {
                    portalResponse.setStatus(PortalRestStatusEnum.OK);
                } else {
                    portalResponse.setStatus(PortalRestStatusEnum.ERROR);
                    if (fv != null) {
                        portalResponse.setMessage(fv.toString());
                    }
                }
            }
		} catch (Exception ex) {
			// Uncaught exceptions yield 404 and an empty error page
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			portalResponse.setStatus(PortalRestStatusEnum.ERROR);
			portalResponse.setMessage(ex.toString());
		}

		EcompPortalUtils.logAndSerializeObject(logger, "postPortalAdmin", "response", portalResponse);
		return portalResponse;
	}

	/**
	 * Gets the specified application that is on-boarded in Portal.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param appId
	 *            Application ID to get
	 * @param response
	 *            httpServletResponse
	 * @return OnboardingApp objects
	 */
	@ApiOperation(value = "Gets the specified application that is on-boarded in Portal.", response = OnboardingApp.class)
	@RequestMapping(value = { ONBOARD_APP + "/{appId}" }, method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public OnboardingApp getOnboardAppExternal(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("appId") Long appId) {
		EPApp epApp = appService.getApp(appId);
		OnboardingApp obApp = new OnboardingApp();
		epApp.setAppPassword(EPCommonSystemProperties.APP_DISPLAY_PASSWORD); //to hide password from get request
		appService.createOnboardingFromApp(epApp, obApp);
		EcompPortalUtils.logAndSerializeObject(logger, "getOnboardAppExternal", "response", obApp);
		return obApp;
	}

	/**
	 * Adds a new application to Portal. The My Logins App Owner in the request
	 * must be the organization user ID of a person who is a Portal
	 * administrator.
	 * 
	 * <pre>
	 * { 
		"myLoginsAppOwner" : "abc123",
		"name": "dashboard",
		"url": "http://k8s/something",
		"restUrl" : "http://targeturl.com",
		"restrictedApp" : true,
		"isOpen" : true,
		"isEnabled": false
		}
	 * </pre>
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            httpServletResponse
	 * @param newOnboardApp
	 *            Message with details about the app to add
	 * @return PortalRestResponse
	 */
	@ApiOperation(value = "Adds a new application to Portal.", response = PortalRestResponse.class)
	@RequestMapping(value = { ONBOARD_APP }, method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public PortalRestResponse<String> postOnboardAppExternal(HttpServletRequest request, HttpServletResponse response,
			@Valid @RequestBody OnboardingApp newOnboardApp) {
		EcompPortalUtils.logAndSerializeObject(logger, "postOnboardAppExternal", "request", newOnboardApp);
		PortalRestResponse<String> portalResponse = new PortalRestResponse<>();
		if (newOnboardApp != null){
			Validator validator = VALIDATOR_FACTORY.getValidator();
			Set<ConstraintViolation<OnboardingApp>> constraintViolations = validator.validate(newOnboardApp);
			if (!constraintViolations.isEmpty()){
				portalResponse.setStatus(PortalRestStatusEnum.ERROR);
				portalResponse.setMessage("Data is not valid");
				return portalResponse;
			}
		}
		// Validate fields
		if (newOnboardApp != null && newOnboardApp.id != null) {
			portalResponse.setStatus(PortalRestStatusEnum.ERROR);
			portalResponse.setMessage("Unexpected field: id");
			return portalResponse;
		}
        if (newOnboardApp != null && (newOnboardApp.name == null || newOnboardApp.name.trim().length() == 0 //
                || newOnboardApp.url == null || newOnboardApp.url.trim().length() == 0 //
                || newOnboardApp.restUrl == null || newOnboardApp.restUrl.trim().length() == 0
                || newOnboardApp.myLoginsAppOwner == null || newOnboardApp.myLoginsAppOwner.trim().length() == 0
                || newOnboardApp.restrictedApp == null //
                || newOnboardApp.isOpen == null //
                || newOnboardApp.isEnabled == null)) {
            portalResponse.setStatus(PortalRestStatusEnum.ERROR);
            portalResponse.setMessage(
                    "Missing required field: name, url, restUrl, restrictedApp, isOpen, isEnabled, myLoginsAppOwner");
            return portalResponse;
        }

		try {
		    List<EPUser> userList = null;
            if (newOnboardApp != null) {
                userList = userService.getUserByUserId(newOnboardApp.myLoginsAppOwner);
            }
            if (userList == null || userList.size() != 1) {
                portalResponse.setStatus(PortalRestStatusEnum.ERROR);
                if (newOnboardApp != null) {
                    portalResponse.setMessage("Failed to find user: " + newOnboardApp.myLoginsAppOwner);
                } else {
                    portalResponse.setMessage("Failed to find user");
                }

				return portalResponse;
			}

			EPUser epUser = userList.get(0);
			// Check for Portal admin status
			if (! adminRolesService.isSuperAdmin(epUser)) {
				portalResponse.setStatus(PortalRestStatusEnum.ERROR);
				portalResponse.setMessage("User lacks Portal admin role: " + epUser.getLoginId());
				return portalResponse;				
			}
				
			newOnboardApp.normalize();
			FieldsValidator fv = appService.addOnboardingApp(newOnboardApp, epUser);
			if (fv.httpStatusCode.intValue() == HttpServletResponse.SC_OK) {
				portalResponse.setStatus(PortalRestStatusEnum.OK);
			} else {
				portalResponse.setStatus(PortalRestStatusEnum.ERROR);
				portalResponse.setMessage(fv.toString());
			}
		} catch (Exception ex) {
			// Uncaught exceptions yield 404 and an empty error page
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			portalResponse.setStatus(PortalRestStatusEnum.ERROR);
			portalResponse.setMessage(ex.toString());
		}
		EcompPortalUtils.logAndSerializeObject(logger, "postOnboardAppExternal", "response", portalResponse);
		return portalResponse;
	}

	/**
	 * Updates information about an on-boarded application in Portal. The My
	 * Logins App Owner in the request must be the organization user ID of a
	 * person who is a Portal administrator.
	 * <pre>
	   { 
		"id" : 123,
		"myLoginsAppOwner" : "abc123",
		"name": "dashboard",
		"url": "http://k8s/something",
		"restUrl" : "http://targeturl.com",
		"restrictedApp" : true,
		"isOpen" : true,
		"isEnabled": false
		}
		</pre>
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            httpServletResponse
	 * @param appId
	 *            application id
	 * @param oldOnboardApp
	 *            Message with details about the app to add
	 * @return PortalRestResponse
	 */
	@ApiOperation(value = "Updates information about an on-boarded application in Portal.", response = PortalRestResponse.class)
	@RequestMapping(value = { ONBOARD_APP + "/{appId}" }, method = RequestMethod.PUT, produces = "application/json")
	@ResponseBody
	public PortalRestResponse<String> putOnboardAppExternal(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("appId") Long appId, @Valid @RequestBody OnboardingApp oldOnboardApp) {
		EcompPortalUtils.logAndSerializeObject(logger, "putOnboardAppExternal", "request", oldOnboardApp);
		PortalRestResponse<String> portalResponse = new PortalRestResponse<>();

		if (oldOnboardApp != null){
			Validator validator = VALIDATOR_FACTORY.getValidator();
			Set<ConstraintViolation<OnboardingApp>> constraintViolations = validator.validate(oldOnboardApp);
			if (!constraintViolations.isEmpty()){
				portalResponse.setStatus(PortalRestStatusEnum.ERROR);
				portalResponse.setMessage("Data is not valid");
				return portalResponse;
			}
		}

		// Validate fields.
		if (oldOnboardApp !=null && (oldOnboardApp.id == null || !appId.equals(oldOnboardApp.id))) {
			portalResponse.setStatus(PortalRestStatusEnum.ERROR);
			portalResponse.setMessage("Unexpected value for field: id");
			return portalResponse;
		}
		if (oldOnboardApp !=null && (oldOnboardApp.name == null || oldOnboardApp.name.trim().length() == 0 //
				|| oldOnboardApp.url == null || oldOnboardApp.url.trim().length() == 0 //
				|| oldOnboardApp.restUrl == null || oldOnboardApp.restUrl.trim().length() == 0
				|| oldOnboardApp.myLoginsAppOwner == null || oldOnboardApp.myLoginsAppOwner.trim().length() == 0
				|| oldOnboardApp.restrictedApp == null //
				|| oldOnboardApp.isOpen == null //
				|| oldOnboardApp.isEnabled == null)) {
			portalResponse.setStatus(PortalRestStatusEnum.ERROR);
			portalResponse.setMessage(
					"Missing required field: name, url, restUrl, restrictedApp, isOpen, isEnabled, myLoginsAppOwner");
			return portalResponse;
		}

		try {
            List<EPUser> userList = null;
            if (oldOnboardApp != null) {
                userList = userService.getUserByUserId(oldOnboardApp.myLoginsAppOwner);
            }
            if (userList == null || userList.size() != 1) {
                portalResponse.setStatus(PortalRestStatusEnum.ERROR);
                if (oldOnboardApp != null) {
                    portalResponse.setMessage("Failed to find user: " + oldOnboardApp.myLoginsAppOwner);
                } else {
                    portalResponse.setMessage("Failed to find user");
                }

                return portalResponse;
            }

			EPUser epUser = userList.get(0);
			// Check for Portal admin status
			if (! adminRolesService.isSuperAdmin(epUser)) {
				portalResponse.setStatus(PortalRestStatusEnum.ERROR);
				portalResponse.setMessage("User lacks Portal admin role: " + epUser.getLoginId());
				return portalResponse;				
			}

			oldOnboardApp.normalize();
			FieldsValidator fv = appService.modifyOnboardingApp(oldOnboardApp, epUser);
			if (fv.httpStatusCode.intValue() == HttpServletResponse.SC_OK) {
				portalResponse.setStatus(PortalRestStatusEnum.OK);
			} else {
				portalResponse.setStatus(PortalRestStatusEnum.ERROR);
				portalResponse.setMessage(fv.toString());
			}
		} catch (Exception ex) {
			// Uncaught exceptions yield 404 and an empty error page
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			portalResponse.setStatus(PortalRestStatusEnum.ERROR);
			portalResponse.setMessage(ex.toString());
		}
		EcompPortalUtils.logAndSerializeObject(logger, "putOnboardAppExternal", "response", portalResponse);
		return portalResponse;
	}

}
