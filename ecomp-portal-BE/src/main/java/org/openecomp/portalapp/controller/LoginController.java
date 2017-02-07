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
package org.openecomp.portalapp.controller;

import static com.att.eelf.configuration.Configuration.MDC_KEY_REQUEST_ID;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.openecomp.portalapp.command.EPLoginBean;
import org.openecomp.portalapp.portal.domain.SharedContext;
import org.openecomp.portalapp.portal.service.EPLoginService;
import org.openecomp.portalapp.portal.service.SharedContextService;
import org.openecomp.portalapp.portal.utils.EPSystemProperties;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalapp.service.EPProfileService;
import org.openecomp.portalapp.util.EPUserUtils;
import org.openecomp.portalapp.util.SessionCookieUtil;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.menu.MenuProperties;
import org.openecomp.portalsdk.core.onboarding.crossapi.PortalTimeoutHandler;
import org.openecomp.portalsdk.core.util.CipherUtil;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.openecomp.portalsdk.core.web.support.AppUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/")
public class LoginController extends EPUnRestrictedBaseController implements LoginService{
	
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(LoginController.class);
	
	public static final String DEFAULT_SUCCESS_VIEW = "applicationsHome";
	public static final String DEFAULT_FAILURE_VIEW = "login";
	public static final String ERROR_MESSAGE_KEY    = "error";
	public static final String REDIRECT_URL = "redirectUrl";
	
	@Autowired
	EPProfileService service;
	@Autowired
	private EPLoginService loginService;
	@Autowired
	private SharedContextService sharedContextService;
	
	String viewName = "login";
	private String welcomeView;

    public String getWelcomeView() {
        return welcomeView;
    }

    public void setWelcomeView(String welcomeView) {
        this.welcomeView = welcomeView;
    }
    
	@RequestMapping(value = {"/login.htm" }, method = RequestMethod.GET)
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> model = new HashMap<String, Object>();
		
		String authentication = SystemProperties.getProperty(SystemProperties.AUTHENTICATION_MECHANISM);
		
		String loginPage;
		
		if (authentication == null || authentication.equals("") || authentication.trim().equals("OIDC"))				
			loginPage = "openIdLogin";
		else
			loginPage =  getViewName();
		
		return new ModelAndView(loginPage,"model", model);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = {"/open_source/login" }, method = RequestMethod.POST)
	public @ResponseBody String loginValidate(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		JsonNode root = mapper.readTree(request.getReader());
		
	      EPLoginBean commandBean = new EPLoginBean();
	      String        loginId = root.get("loginId").textValue(); 
	      String        password = root.get("password").textValue();
	      commandBean.setLoginId(loginId);
	      commandBean.setLoginPwd(CipherUtil.encrypt(password));
	      HashMap additionalParamsMap = new HashMap();
	      StringBuilder sbAdditionalInfo = new StringBuilder();
	      
	      commandBean = getLoginService().findUser(commandBean, (String)request.getAttribute(MenuProperties.MENU_PROPERTIES_FILENAME_KEY), 
	    		  additionalParamsMap);
	      String fullURL = EPUserUtils.getFullURL(request);
	      if (commandBean.getUser() == null) {
	        String loginErrorMessage = (commandBean.getLoginErrorMessage() != null) ? commandBean.getLoginErrorMessage() 
	        		: "login.error.external.invalid";

			logger.info(EELFLoggerDelegate.debugLogger, "loginId = " + loginId + " does not exist in the the DB.");
			logger.info(EELFLoggerDelegate.errorLogger, "loginId = " + loginId + " does not exist in the the DB.");
			sbAdditionalInfo.append(String.format("But the Login-Id: %s doesn't exist in the Database. Request-URL: %s", 
					loginId, fullURL));
			return loginErrorMessage;
	      }
	      else {
	        // store the currently logged in user's information in the session
	        EPUserUtils.setUserSession(request, commandBean.getUser(), commandBean.getMenu(), commandBean.getBusinessDirectMenu(), 
	        		null);
	        
	        try{
		    	logger.info(EELFLoggerDelegate.debugLogger, "******************* store user info into share context begins");
		    	String sessionId = request.getSession().getId();			    	
		    	List<SharedContext> existingSC = getSharedContextService().getSharedContexts(sessionId);
		    	if(existingSC==null || existingSC.size()==0){
		    		getSharedContextService().addSharedContext(sessionId, EPSystemProperties.USER_FIRST_NAME, commandBean.getUser().getFirstName());
				    getSharedContextService().addSharedContext(sessionId, EPSystemProperties.USER_LAST_NAME, commandBean.getUser().getLastName());
				    getSharedContextService().addSharedContext(sessionId, EPSystemProperties.USER_EMAIL, commandBean.getUser().getEmail());
				    getSharedContextService().addSharedContext(sessionId, EPSystemProperties.USER_ORG_USERID, commandBean.getLoginId());
		    	}
			    
		    }catch(Exception e){
		    	logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
				logger.info(EELFLoggerDelegate.errorLogger, "failed the shared context adding process " + e.getMessage());
				logger.info(EELFLoggerDelegate.debugLogger, "********************** failed the shared context adding process " + e.getMessage());
		    }
			logger.info(EELFLoggerDelegate.debugLogger, "********************* PresetUp the EP service cookie and intial sessionManagement");

		    SessionCookieUtil.preSetUp(request, response); 
		    SessionCookieUtil.setUpUserIdCookie(request, response, loginId);
  
		    JSONObject j = new JSONObject("{success: success}");
		    
	        return j.toString();
	       
	      }
	
	}
	
	@RequestMapping(value = {"/processSingleSignOn" }, method = RequestMethod.GET)
	public ModelAndView processSingelSignOn(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		Map<Object, Object>             model = new HashMap<Object, Object>();
		HashMap<Object, Object> additionalParamsMap = new HashMap<Object, Object>();
		EPLoginBean commandBean = new EPLoginBean();
		MDC.put(MDC_KEY_REQUEST_ID, EPUserUtils.getRequestId(request));
		String  orgUserId = "";
		//get userId from cookie
		orgUserId = SessionCookieUtil.getUserIdFromCookie(request, response);
		logger.info(EELFLoggerDelegate.debugLogger, "******************** process_singelSignOn process begins");
		logger.info(EELFLoggerDelegate.debugLogger, "******************* We get the orgUserId " + orgUserId);

		StringBuilder sbAdditionalInfo = new StringBuilder();
		if ((orgUserId == null || orgUserId.length() == 0)) {
			model.put(ERROR_MESSAGE_KEY, SystemProperties.MESSAGE_KEY_LOGIN_ERROR_COOKIE_EMPTY);
			if(request.getParameter("redirectUrl")!=null && request.getParameter("redirectUrl").length()!=0){
			     return new ModelAndView("redirect:" + DEFAULT_FAILURE_VIEW + ".htm" + "?redirectUrl=" + request.getParameter("redirectUrl"));
			}else{
				 return new ModelAndView("redirect:" + DEFAULT_FAILURE_VIEW + ".htm");
			}
		}
		else {
			
			StopWatch stopWatch = new StopWatch("LoginController.Login");
			stopWatch.start();
						
			try {
				logger.info(EELFLoggerDelegate.metricsLogger, "Operation findUser is started to locate " + orgUserId + " in the database.");
				logger.info(EELFLoggerDelegate.debugLogger, "Operation findUser is started to locate " + orgUserId + " in the database.");
				commandBean.setLoginId(orgUserId);
				commandBean.setOrgUserId(orgUserId);
				commandBean = getLoginService().findUserWithoutPassword(commandBean, (String)request.getAttribute(MenuProperties.MENU_PROPERTIES_FILENAME_KEY), additionalParamsMap);
				
				stopWatch.stop();
				MDC.put(EPSystemProperties.MDC_TIMER, stopWatch.getTotalTimeMillis() + "ms");
				logger.info(EELFLoggerDelegate.metricsLogger, "Operation findUser is completed.");
				logger.info(EELFLoggerDelegate.debugLogger, "Operation findUser is completed.");
			} catch(Exception e) {
				stopWatch.stop();
				MDC.put(EPSystemProperties.MDC_TIMER, stopWatch.getTotalTimeMillis() + "ms");
				logger.info(EELFLoggerDelegate.errorLogger, "Exception occurred while performing findUser " + orgUserId + ". Details: " + EcompPortalUtils.getStackTrace(e));
				logger.info(EELFLoggerDelegate.debugLogger, "Exception occurred while performing findUser " + orgUserId + ". Details: " + EcompPortalUtils.getStackTrace(e));
				logger.info(EELFLoggerDelegate.metricsLogger, "Operation findUser is failed.");
			} finally {
				MDC.remove(EPSystemProperties.MDC_TIMER);
			}
			
			sbAdditionalInfo.append("Login attempt is succeeded. ");
			String fullURL = EPUserUtils.getFullURL(request);
			if (commandBean.getUser() == null) {
				logger.info(EELFLoggerDelegate.debugLogger, "loginId = " + orgUserId + " does not exist in the the DB.");
				logger.info(EELFLoggerDelegate.errorLogger, "loginId = " + orgUserId + " does not exist in the the DB.");
				logger.info(EELFLoggerDelegate.debugLogger, "loginId = " + orgUserId + " does not exist in the the DB.");

				sbAdditionalInfo.append(String.format("But the Login-Id: %s doesn't exist in the Database. Created a Guest Session. Request-URL: %s", 
						orgUserId, fullURL));
				if(request.getParameter("redirectUrl")!=null && request.getParameter("redirectUrl").length()!=0){
				     return new ModelAndView("redirect:" + DEFAULT_FAILURE_VIEW + ".htm" + "?redirectUrl=" + request.getParameter("redirectUrl"));
				}else{
					 return new ModelAndView("redirect:" + DEFAULT_FAILURE_VIEW + ".htm");
				}
			}
			else {
		    
			    sbAdditionalInfo.append(String.format("Login-Id: %s, Login-Method: %s, Request-URL: %s", orgUserId, "", fullURL));
				logger.info(EELFLoggerDelegate.debugLogger, "*********************** now set up user session for " + orgUserId);

			    EPUserUtils.setUserSession(request, commandBean.getUser(), commandBean.getMenu(), commandBean.getBusinessDirectMenu(), "");
				logger.info(EELFLoggerDelegate.debugLogger, "*********************** now set up user session for " + orgUserId + " finished");

			    //Store user's information into share context	
			    try{
					logger.info(EELFLoggerDelegate.debugLogger, "******************* store user info into share context begins");

			    	String sessionId = request.getSession().getId();			    	
			    	List<SharedContext> existingSC = getSharedContextService().getSharedContexts(sessionId);
			    	if(existingSC==null || existingSC.size()==0){
			    		getSharedContextService().addSharedContext(sessionId, EPSystemProperties.USER_FIRST_NAME, commandBean.getUser().getFirstName());
					    getSharedContextService().addSharedContext(sessionId, EPSystemProperties.USER_LAST_NAME, commandBean.getUser().getLastName());
					    getSharedContextService().addSharedContext(sessionId, EPSystemProperties.USER_EMAIL, commandBean.getUser().getEmail());
					    getSharedContextService().addSharedContext(sessionId, EPSystemProperties.USER_ORG_USERID, commandBean.getLoginId());
			    	}
				    
			    }catch(Exception e){
			    	logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
					logger.info(EELFLoggerDelegate.errorLogger, "failed the shared context adding process " + e.getMessage());
					logger.info(EELFLoggerDelegate.debugLogger, "********************** failed the shared context adding process " + e.getMessage());

			    }
			    
				logger.info(EELFLoggerDelegate.debugLogger, "********************* PresetUp the EP service cookie and intial sessionManagement");

			    SessionCookieUtil.preSetUp(request, response); 
			    SessionCookieUtil.setUpUserIdCookie(request, response, orgUserId);
				logger.info(EELFLoggerDelegate.debugLogger, "********************* PresetUp the EP service cookie and intial sessionManagement completed");
				logger.info(EELFLoggerDelegate.errorLogger, commandBean.getUser().getOrgUserId() + " exists in the the system.");
				logger.info(EELFLoggerDelegate.debugLogger, commandBean.getUser().getOrgUserId() + " exists in the the system.");

			    String redirect = "redirectUrl";
			    
				//get redirectUrl from URL parameter
			    if(request.getParameter(redirect)!=null && request.getParameter(redirect).length()!=0){
			    	String forwardUrl = URLDecoder.decode(request.getParameter(redirect),"UTF-8");
			    	//clean cookie
			    	Cookie cookie2 = new Cookie(redirect, "");
			        cookie2.setMaxAge(0);
			        cookie2.setDomain(EPSystemProperties.getProperty(EPSystemProperties.COOKIE_DOMAIN));
			        cookie2.setPath("/");
			        response.addCookie(cookie2);
			    	return new ModelAndView("redirect:" + forwardUrl);
			    }
			    
			    //first check if redirectUrl exists or not
				if(WebUtils.getCookie(request, redirect)!=null){
		        	String forwardUrl = WebUtils.getCookie(request, redirect).getValue();
		        	//clean cookie
		        	Cookie cookie2 = new Cookie(redirect, "");
		            cookie2.setMaxAge(0);
		            cookie2.setDomain(EPSystemProperties.getProperty(EPSystemProperties.COOKIE_DOMAIN));
		            cookie2.setPath("/");
		            response.addCookie(cookie2);	       
		        	
		        	return new ModelAndView("redirect:" + forwardUrl);
		        }
			}
		}
		
		// if user has been authenticated, now take them to the welcome page.
		//return new ModelAndView("redirect:" + DEFAULT_SUCCESS_VIEW + ".htm");
		logger.info(EELFLoggerDelegate.debugLogger, "********************** Now return to application home page");

		return new ModelAndView("redirect:" + SystemProperties.getProperty(EPSystemProperties.FE_URL));
		
		//
		// Re-enable for BE/FE separation.  For 1607, at last minute we decided to go out
		// without BE/FE separation.
		//
		//return new ModelAndView("redirect:" + SystemProperties.getProperty(EPSystemProperties.FE_URL));
		
	}
	
    public String getJessionId(HttpServletRequest request){
		
		return request.getSession().getId();
		/*
		Cookie ep = WebUtils.getCookie(request, JSESSIONID);
		if(ep==null){
			return request.getSession().getId();
		}
		return ep.getValue();
		*/
	}
	
	
	protected void initateSessionMgtHandler(HttpServletRequest request) {
		String jSessionId = getJessionId(request);
		PortalTimeoutHandler.sessionCreated(jSessionId, jSessionId, AppUtils.getSession(request));
	}
	

	public String getViewName() {
		return viewName;
	}
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
    


}
