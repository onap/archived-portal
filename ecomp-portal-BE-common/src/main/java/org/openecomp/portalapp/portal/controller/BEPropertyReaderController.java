/*-
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

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.openecomp.portalsdk.core.controller.FusionBaseController;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.openecomp.portalapp.portal.domain.BEProperty;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestResponse;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestStatusEnum;

@RestController
@RequestMapping("/portalApi/properties")
public class BEPropertyReaderController extends FusionBaseController{
	@RequestMapping(value = "/readProperty", method = RequestMethod.GET, produces = "application/json")
	public PortalRestResponse<BEProperty> readProperty(HttpServletRequest request, @RequestParam String key) {
		try {
			return new PortalRestResponse<>(PortalRestStatusEnum.OK, "success", new BEProperty(key, SystemProperties.getProperty(key)));
		} catch (Exception e) {
			return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, e.toString(), null);
		}		
	}
}
