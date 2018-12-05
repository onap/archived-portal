/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 *
 * Modifications Copyright © 2018 IBM.
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.util.EPUserUtils;
import org.onap.portalsdk.core.controller.FusionBaseController;
import org.onap.portalsdk.core.domain.MenuData;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public abstract class EPFusionBaseController extends FusionBaseController {
	
	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(EPFusionBaseController.class);
		
	@Override
	public boolean isAccessible() {
		return true;
	}

	public boolean isRESTfulCall() {
		return true;
	}

	@ModelAttribute("menu")
	public Map<String, Object> messages(HttpServletRequest request) {
		HttpSession session = null;
		Map<String, Object> model = new HashMap<String, Object>();
		session = request.getSession();
		EPUser user = EPUserUtils.getUserSession(request);
		if (session != null && user != null) {
			@SuppressWarnings("unchecked")
			Set<MenuData> menuResult = (Set<MenuData>) session
					.getAttribute(SystemProperties.getProperty(SystemProperties.APPLICATION_MENU_ATTRIBUTE_NAME));
			try {
				model = setMenu(menuResult);
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "messages failed", e);
			}
		}
		
		return model;
	}

	public Map<String, Object> setMenu(Set<MenuData> menuResult) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		List<List<MenuData>> childItemList = new ArrayList<>();
		
		List<MenuData> parentList = new ArrayList<MenuData>();
		
		Map<String, Object> model = new HashMap<String, Object>();
		for (MenuData menu : menuResult) {
			MenuData parentData = new MenuData();
			parentData.setLabel(menu.getLabel());
			parentData.setAction(menu.getAction());
			parentData.setImageSrc(menu.getImageSrc());
			parentList.add(parentData);
			List<MenuData> tempList = new ArrayList<MenuData>();
			for (Object o : menu.getChildMenus()) {
				MenuData m = (MenuData) o;
				MenuData data = new MenuData();
				data.setLabel(m.getLabel());
				data.setAction(m.getAction());
				data.setImageSrc(m.getImageSrc());
				tempList.add(data);
			}
			childItemList.add(tempList);
		}
		model.put("childItemList",  mapper.writeValueAsString(childItemList));
		model.put("parentList", mapper.writeValueAsString(parentList));
		return model;
	}
}
