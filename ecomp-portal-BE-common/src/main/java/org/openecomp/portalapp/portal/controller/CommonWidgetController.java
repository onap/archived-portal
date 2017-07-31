/*
 * ================================================================================
 * ECOMP Portal
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

import javax.servlet.http.HttpServletRequest;

import org.openecomp.portalapp.controller.EPRestrictedBaseController;
import org.openecomp.portalapp.portal.controller.DashboardController.WidgetCategory;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestResponse;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.openecomp.portalapp.portal.service.DashboardSearchService;
import org.openecomp.portalapp.portal.transport.CommonWidgetMeta;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommonWidgetController extends EPRestrictedBaseController implements BasicAuthenticationController{
	
	
	private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(CommonWidgetController.class);
	@Autowired
	private DashboardSearchService searchService;
	
	/**
	 * Validates the resource type parameter.
	 * 
	 * @param resourceType
	 * @return True if known in the enum WidgetCategory, else false.
	 */
	private boolean isValidResourceType(String resourceType) {
		if (resourceType == null)
			return false;
		for (WidgetCategory wc : WidgetCategory.values())
			if (wc.name().equals(resourceType))
				return true;
		return false;
	}
	
	/**
	 * Gets all widgets of the specified resource type.
	 * 
	 * @param request
	 * @param resourceType
	 *            Request parameter.
	 * @return Rest response wrapped around a CommonWidgetMeta object.
	 */
	@RequestMapping(value = "/commonWidgets", method = RequestMethod.GET, produces = "application/json")
	public PortalRestResponse<CommonWidgetMeta> getWidgetData(HttpServletRequest request,
			@RequestParam String resourceType) {
		if (!isValidResourceType(resourceType)){
			logger.debug(EELFLoggerDelegate.debugLogger, "Unexpected resource type {}", resourceType);
			return new PortalRestResponse<CommonWidgetMeta>(PortalRestStatusEnum.ERROR,
					"Unexpected resource type " + resourceType, null);
		}
		return new PortalRestResponse<CommonWidgetMeta>(PortalRestStatusEnum.OK, "success",
				searchService.getWidgetData(resourceType));
	}
		

}
