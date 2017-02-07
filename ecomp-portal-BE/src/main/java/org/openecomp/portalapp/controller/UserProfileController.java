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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.openecomp.portalapp.portal.logging.aop.EPAuditLog;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalapp.service.EPProfileService;
import org.openecomp.portalsdk.core.domain.Profile;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/")
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class UserProfileController extends EPRestrictedBaseController {

   @Autowired
   EPProfileService service;
   
   EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(UserProfileController.class);
   
   @RequestMapping(value = {"/user_profile" }, method = RequestMethod.GET)
   public ModelAndView ProfileSearch(HttpServletRequest request) {
	   Map<String, Object> model = new HashMap<String, Object>();
	   ObjectMapper mapper = new ObjectMapper();
	   	   
	   try {
		   List<Profile> profileList = service.findAll();
		   model.put("customerInfo", mapper.writeValueAsString(profileList));		   
		} catch (JsonGenerationException e) {
			logger.error(EELFLoggerDelegate.errorLogger, "Encountered an JsonGenerationException while performing the ProfileSearch, Details:" + EcompPortalUtils.getStackTrace(e));
		} catch (JsonMappingException e) {
			logger.error(EELFLoggerDelegate.errorLogger, "Encountered an JsonMappingException while performing the ProfileSearch, Details:" + EcompPortalUtils.getStackTrace(e));
		} catch (IOException e) {
			logger.error(EELFLoggerDelegate.errorLogger, "Encountered an IOException while performing the ProfileSearch, Details:" + EcompPortalUtils.getStackTrace(e));
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "Encountered an Exception while performing the ProfileSearch, Details:" + EcompPortalUtils.getStackTrace(e));
		}
     
       	return new ModelAndView("user_profile", "model", model);
   }
}
