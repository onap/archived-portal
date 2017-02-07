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
package org.openecomp.portalapp.controller.sessionmgt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openecomp.portalapp.controller.EPRestrictedRESTfulBaseController;
import org.openecomp.portalapp.portal.logging.aop.EPAuditLog;
import org.openecomp.portalapp.service.sessionmgt.ManageService;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auxapi")
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class SessionCommunicationController  extends EPRestrictedRESTfulBaseController {
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(SessionCommunicationController.class);
	
	@Autowired
	ManageService manageService;
		
	protected boolean isAuxRESTfulCall(){
		return true;
	}
	
	@RequestMapping(value={"/getSessionSlotCheckInterval"}, method = RequestMethod.GET, produces = "application/json")
	public Integer getSessionSlotCheckInterval(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return manageService.fetchSessionSlotCheckInterval();
	}
	
	@RequestMapping(value={"/extendSessionTimeOuts"}, method = RequestMethod.POST)
	public Boolean extendSessionTimeOuts(HttpServletRequest request, HttpServletResponse response, @RequestParam String sessionMap) throws Exception {
		manageService.extendSessionTimeOuts(sessionMap);
		
		return true;
	}

}
