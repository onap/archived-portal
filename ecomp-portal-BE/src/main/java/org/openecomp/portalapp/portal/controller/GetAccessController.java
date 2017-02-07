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
package org.openecomp.portalapp.portal.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.openecomp.portalapp.controller.EPUnRestrictedBaseController;
import org.openecomp.portalapp.portal.domain.GetAccessResult;
import org.openecomp.portalapp.portal.logging.aop.EPAuditLog;
import org.openecomp.portalapp.portal.service.GetAccessService;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class GetAccessController extends EPUnRestrictedBaseController {

	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(GetAccessController.class);

	@Autowired
	GetAccessService getAccessService;

	/**
	 * Sorts the list by ECOMP function name.
	 */
	private Comparator<GetAccessResult> getAccessComparator = new Comparator<GetAccessResult>() {
		public int compare(GetAccessResult o1, GetAccessResult o2) {
			return o1.getEcompFunction().compareTo(o2.getEcompFunction());
		}
	};
	
	@RequestMapping(value = { "/portalApi/getAppList" }, method = RequestMethod.GET, produces = "application/json")
	public List<GetAccessResult> getAppList(HttpServletRequest request) throws IOException {
		List<GetAccessResult> appsList = null;
		appsList = getAccessService.getAppAccessList();
		Collections.sort(appsList, getAccessComparator);
		EcompPortalUtils.logAndSerializeObject("/portalApi/getAppList", "result =", appsList);
		return appsList;
	}
}