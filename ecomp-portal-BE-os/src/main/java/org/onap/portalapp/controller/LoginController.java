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
package org.onap.portalapp.controller;

import static com.att.eelf.configuration.Configuration.MDC_KEY_REQUEST_ID;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.onap.portalapp.command.EPLoginBean;
import org.onap.portalapp.portal.domain.SharedContext;
import org.onap.portalapp.portal.service.EPLoginService;
import org.onap.portalapp.portal.service.EPRoleFunctionService;
import org.onap.portalapp.portal.service.SharedContextService;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.portal.utils.EPSystemProperties;
import org.onap.portalapp.util.EPUserUtils;
import org.onap.portalapp.util.SessionCookieUtil;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.menu.MenuProperties;
import org.onap.portalsdk.core.util.SystemProperties;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/")
public class LoginController extends EPUnRestrictedBaseController implements LoginService {

	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(LoginController.class);

	public static final String DEFAULT_SUCCESS_VIEW = "applicationsHome";
	public static final String DEFAULT_FAILURE_VIEW = "login";
	public static final String ERROR_MESSAGE_KEY = "error";
	public static final String REDIRECT_URL = "redirectUrl";
	public static final String REDIRECT_COLON = "redirect:";

	@Autowired
	private EPLoginService loginService;
	@Autowired
	private SharedContextService sharedContextService;
	@Autowired
	private EPRoleFunctionService ePRoleFunctionService;

	private String viewName = "login";

	private String welcomeView;

	@GetMapping(value = { "/login.htm" })
	public ModelAndView login(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		String authentication = SystemProperties.getProperty(SystemProperties.AUTHENTICATION_MECHANISM);
		String loginPage;
		if (authentication == null || "".equals(authentication) || "OICD".equals(authentication.trim()))
			loginPage = "openIdLogin";
		else
			loginPage = getViewName();
		return new ModelAndView(loginPage, "model", model);
	}

	@SuppressWarnings("rawtypes")
	@PostMapping(value = { "/open_source/login" })
	@ResponseBody
	public String loginValidate(HttpServletRequest request, HttpServletResponse response) throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		JsonNode root = mapper.readTree(request.getReader());

		EPLoginBean commandBean = new EPLoginBean();
		String loginId = root.get("loginId").textValue();
		String password = root.get("password").textValue();
		commandBean.setLoginId(loginId);
		commandBean.setLoginPwd(password);
		
		HashMap additionalParamsMap = new HashMap();
		StringBuilder sbAdditionalInfo = new StringBuilder();

		commandBean = getLoginService().findUser(commandBean,
				(String) request.getAttribute(MenuProperties.MENU_PROPERTIES_FILENAME_KEY), additionalParamsMap);
		String fullURL = getFullURL(request);
		if (commandBean.getUser() == null) {
			String loginErrorMessage = (commandBean.getLoginErrorMessage() != null) ? commandBean.getLoginErrorMessage()
					: "login.error.external.invalid";
			logger.info(EELFLoggerDelegate.debugLogger, "loginId {} does not exist in the the DB.", loginId);
			sbAdditionalInfo.append(String.format("But the Login-Id: %s doesn't exist in the Database. Request-URL: %s",
					loginId, fullURL));
			return loginErrorMessage;
		} else {
			// store the currently logged in user's information in the session
			EPUserUtils.setUserSession(request, commandBean.getUser(), commandBean.getMenu(),
					commandBean.getBusinessDirectMenu(), ePRoleFunctionService);

			try {
				logger.info(EELFLoggerDelegate.debugLogger, "loginValidate: store user info into share context begins");
				String sessionId = request.getSession().getId();
				List<SharedContext> existingSC = getSharedContextService().getSharedContexts(sessionId);
				if (existingSC == null || existingSC.isEmpty()) {
					getSharedContextService().addSharedContext(sessionId, EPSystemProperties.USER_FIRST_NAME,
							commandBean.getUser().getFirstName());
					getSharedContextService().addSharedContext(sessionId, EPSystemProperties.USER_LAST_NAME,
							commandBean.getUser().getLastName());
					getSharedContextService().addSharedContext(sessionId, EPSystemProperties.USER_EMAIL,
							commandBean.getUser().getEmail());
					getSharedContextService().addSharedContext(sessionId, EPSystemProperties.USER_ORG_USERID,
							commandBean.getLoginId());
				}

			} catch (Exception e) {
				logger.info(EELFLoggerDelegate.errorLogger, "loginValidate: failed the shared context adding process ",
						e);
			}
			logger.info(EELFLoggerDelegate.debugLogger,
					"loginValidate: PresetUp the EP service cookie and intial sessionManagement");

			SessionCookieUtil.preSetUp(request, response);
			SessionCookieUtil.setUpUserIdCookie(request, response, loginId);

			JSONObject j = new JSONObject("{success: success}");

			return j.toString();
		}
	}

	/*
	 * Work around a bug in ecompsdkos version 1.1.0 which hard-codes this endpoint.
	 */
	@RequestMapping(value = { "/process_csp" }, method = RequestMethod.GET)
	public ModelAndView processCsp(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return processSingleSignOn(request, response);
	}
	/*
	 * Remove this method after epsdk-app-common/.../SingleSignOnController.java is
	 * repaired.
	 */

	@GetMapping(value = { "/processSingleSignOn" })
	public ModelAndView processSingleSignOn(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<Object, Object> model = new HashMap<Object, Object>();
		HashMap<Object, Object> additionalParamsMap = new HashMap<Object, Object>();
		EPLoginBean commandBean = new EPLoginBean();
		MDC.put(MDC_KEY_REQUEST_ID, (getRequestId(request)==null || getRequestId(request).isEmpty()) ? UUID.randomUUID().toString():getRequestId(request));
		// get userId from cookie
		String orgUserId = SessionCookieUtil.getUserIdFromCookie(request, response);
		logger.info(EELFLoggerDelegate.debugLogger, "processSingleSignOn: begins with orgUserId {}", orgUserId);

		StringBuilder sbAdditionalInfo = new StringBuilder();
		validateDomain(request);
		if (orgUserId == null || orgUserId.length() == 0) {
			model.put(ERROR_MESSAGE_KEY, SystemProperties.MESSAGE_KEY_LOGIN_ERROR_COOKIE_EMPTY);
			if (request.getParameter(REDIRECT_URL) != null && request.getParameter(REDIRECT_URL).length() != 0) {
				return new ModelAndView(REDIRECT_COLON + DEFAULT_FAILURE_VIEW + ".htm" + "?redirectUrl="
						+ request.getParameter(REDIRECT_URL));
			} else {
				return new ModelAndView(REDIRECT_COLON + DEFAULT_FAILURE_VIEW + ".htm");
			}
		} else {

			StopWatch stopWatch = new StopWatch("LoginController.Login");
			stopWatch.start();

			try {
				logger.info(EELFLoggerDelegate.debugLogger,
						"Operation findUser is started to locate user {}  in the database.", orgUserId);
				commandBean.setLoginId(orgUserId);
				commandBean.setOrgUserId(orgUserId);
				commandBean = getLoginService().findUser(commandBean,
						(String) request.getAttribute(MenuProperties.MENU_PROPERTIES_FILENAME_KEY),
						additionalParamsMap);

				stopWatch.stop();
				MDC.put(EPSystemProperties.MDC_TIMER, String.valueOf(stopWatch.getTotalTimeMillis()));
				logger.info(EELFLoggerDelegate.debugLogger, "Operation findUser is completed.");
			} catch (Exception e) {
				stopWatch.stop();
				MDC.put(EPSystemProperties.MDC_TIMER, String.valueOf(stopWatch.getTotalTimeMillis()));
				logger.info(EELFLoggerDelegate.errorLogger, "processSingleSignOn failed on user " + orgUserId, e);
			} finally {
				MDC.remove(EPSystemProperties.MDC_TIMER);
			}

			sbAdditionalInfo.append("Login attempt is succeeded. ");
			String fullURL = getFullURL(request);
			if (commandBean.getUser() == null) {
				logger.info(EELFLoggerDelegate.debugLogger,
						"processSingleSignOn: loginId {} does not exist in the the DB.", orgUserId);

				sbAdditionalInfo.append(String.format(
						"But the Login-Id: %s doesn't exist in the Database. Created a Guest Session. Request-URL: %s",
						orgUserId, fullURL));
				validateDomain(request);
				if (request.getParameter(REDIRECT_URL) != null && request.getParameter(REDIRECT_URL).length() != 0) {
					return new ModelAndView(REDIRECT_COLON + DEFAULT_FAILURE_VIEW + ".htm" + "?redirectUrl="
							+ request.getParameter(REDIRECT_URL));
				} else {
					return new ModelAndView(REDIRECT_COLON + DEFAULT_FAILURE_VIEW + ".htm");
				}
			} else {

				sbAdditionalInfo.append(
						String.format("Login-Id: %s, Login-Method: %s, Request-URL: %s", orgUserId, "", fullURL));
				logger.info(EELFLoggerDelegate.debugLogger, "processSingleSignOn: now set up user session for {}",
						orgUserId);

				EPUserUtils.setUserSession(request, commandBean.getUser(), commandBean.getMenu(),
						commandBean.getBusinessDirectMenu(), ePRoleFunctionService);
				logger.info(EELFLoggerDelegate.debugLogger,
						"processSingleSignOn: now set up user session for {} finished", orgUserId);

				// Store user's information into share context
				try {
					logger.info(EELFLoggerDelegate.debugLogger,
							"processSingleSignOn: store user info into share context begins");
					String sessionId = request.getSession().getId();
					List<SharedContext> existingSC = getSharedContextService().getSharedContexts(sessionId);
					if (existingSC == null || existingSC.isEmpty()) {
						getSharedContextService().addSharedContext(sessionId, EPSystemProperties.USER_FIRST_NAME,
								commandBean.getUser().getFirstName());
						getSharedContextService().addSharedContext(sessionId, EPSystemProperties.USER_LAST_NAME,
								commandBean.getUser().getLastName());
						getSharedContextService().addSharedContext(sessionId, EPSystemProperties.USER_EMAIL,
								commandBean.getUser().getEmail());
						getSharedContextService().addSharedContext(sessionId, EPSystemProperties.USER_ORG_USERID,
								commandBean.getLoginId());
					}
				} catch (Exception e) {
					logger.info(EELFLoggerDelegate.errorLogger,
							"processSingleSignOn: failed the shared context adding process", e);
				}

				logger.info(EELFLoggerDelegate.debugLogger,
						"processSingleSignOn: PresetUp the EP service cookie and intial sessionManagement");
				SessionCookieUtil.preSetUp(request, response);
				SessionCookieUtil.setUpUserIdCookie(request, response, orgUserId);
				logger.info(EELFLoggerDelegate.debugLogger,
						"processSingleSignOn: PresetUp the EP service cookie and intial sessionManagement completed");
				logger.info(EELFLoggerDelegate.debugLogger,
						commandBean.getUser().getOrgUserId() + " exists in the the system.");

				// get redirectUrl from URL parameter
				validateDomain(request);
				if (request.getParameter(REDIRECT_URL) != null && request.getParameter(REDIRECT_URL).length() != 0) {
					String forwardUrl = URLDecoder.decode(request.getParameter(REDIRECT_URL), "UTF-8");
					// clean cookie
					Cookie cookie2 = new Cookie(REDIRECT_URL, "");
					// ONAP does not use https
					cookie2.setSecure(false);
					cookie2.setMaxAge(0);
					cookie2.setDomain(EPSystemProperties.getProperty(EPSystemProperties.COOKIE_DOMAIN));
					cookie2.setPath("/");
					response.addCookie(cookie2);
					return new ModelAndView(REDIRECT_COLON + forwardUrl);
				}

				// first check if redirectUrl exists or not
				if (WebUtils.getCookie(request, REDIRECT_URL) != null) {
					String forwardUrl = WebUtils.getCookie(request, REDIRECT_URL).getValue();
					// clean cookie
					Cookie cookie2 = new Cookie(REDIRECT_URL, "");
					// ONAP does not use https
					cookie2.setSecure(false);
					cookie2.setMaxAge(0);
					cookie2.setDomain(EPSystemProperties.getProperty(EPSystemProperties.COOKIE_DOMAIN));
					cookie2.setPath("/");
					response.addCookie(cookie2);

					return new ModelAndView(REDIRECT_COLON + forwardUrl);
				}
			}
		}

		// if user has been authenticated, now take them to the welcome page.
		logger.info(EELFLoggerDelegate.debugLogger, "processSingleSignOn: Now return to application home page");
		return new ModelAndView(REDIRECT_COLON + SystemProperties.getProperty(EPSystemProperties.FE_URL));
	}

	private void validateDomain(HttpServletRequest request) throws MalformedURLException {
		final String returnToAppUrl = request.getParameter(REDIRECT_URL);
		if (StringUtils.isNotBlank(returnToAppUrl)) {
			String hostName = new URL(returnToAppUrl).getHost();
			if (StringUtils.isNotBlank(hostName)
					&& !hostName.endsWith(EPSystemProperties.getProperty(EPCommonSystemProperties.COOKIE_DOMAIN))) {
				logger.debug(EELFLoggerDelegate.debugLogger,
						"processSingleSignOn ()  accessing Unauthorized url  :" + hostName);
				throw new SecurityException("accessing Unauthorized url : " + hostName);
			}
		}
	}

	private String getFullURL(HttpServletRequest request) {
		if (request != null) {
			String requestURL = request.getRequestURL().toString();
			String queryString = request.getQueryString();
			if (queryString == null) {
				return requestURL;
			} else {
				return requestURL + "?" + queryString;
			}
		}
		return "";
	}

	private String getRequestId(HttpServletRequest request) {
		Enumeration<String> headerNames = request.getHeaderNames();
		String requestId = "";
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			logger.debug(EELFLoggerDelegate.debugLogger, "getRequestId: header {} has value {}", headerName,
					request.getHeader(headerName));
			if (headerName.equalsIgnoreCase(SystemProperties.ECOMP_REQUEST_ID)) {
				requestId = request.getHeader(headerName);
				break;
			}
		}
		return requestId.isEmpty() ? UUID.randomUUID().toString() : requestId;
	}

	public String getWelcomeView() {
		return welcomeView;
	}

	public void setWelcomeView(String welcomeView) {
		this.welcomeView = welcomeView;
	}

	@Override
	public String getViewName() {
		return viewName;
	}

	@Override
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public EPLoginService getLoginService() {
		return loginService;
	}

	public void setLoginService(EPLoginService loginService) {
		this.loginService = loginService;
	}

	public SharedContextService getSharedContextService() {
		return sharedContextService;
	}

	public void setSharedContextService(SharedContextService sharedContextService) {
		this.sharedContextService = sharedContextService;
	}

	@ExceptionHandler(Exception.class)
	protected void handleBadRequests(Exception e, HttpServletResponse response) throws IOException {
		logger.warn(EELFLoggerDelegate.errorLogger, "Handling bad request", e);
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}
}
