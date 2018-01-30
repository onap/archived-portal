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
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
package org.onap.portalapp.controller.sessionmgt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onap.portalapp.controller.EPRestrictedRESTfulBaseController;
import org.onap.portalapp.portal.logging.aop.EPAuditLog;
import org.onap.portalapp.portal.utils.PortalConstants;
import org.onap.portalapp.service.sessionmgt.ManageService;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping(PortalConstants.REST_AUX_API)
@Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class SessionCommunicationController  extends EPRestrictedRESTfulBaseController {
	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(SessionCommunicationController.class);
	
	@Autowired
	private ManageService manageService;
		
	protected boolean isAuxRESTfulCall(){
		return true;
	}
	
	@ApiOperation(value = "Gets session slot-check interval, a duration in milliseconds.",
    response = Integer.class)
 	@RequestMapping(value={"/getSessionSlotCheckInterval"}, method = RequestMethod.GET, produces = "application/json")
	public Integer getSessionSlotCheckInterval(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return manageService.fetchSessionSlotCheckInterval();
	}
	
	@ApiOperation(value = "Extends session timeout values for all on-boarded applications.",
    response = Boolean.class)
	@RequestMapping(value={"/extendSessionTimeOuts"}, method = RequestMethod.POST)
	public Boolean extendSessionTimeOuts(HttpServletRequest request, HttpServletResponse response, @RequestParam String sessionMap) throws Exception {
		manageService.extendSessionTimeOuts(sessionMap);
		
		return true;
	}

}
