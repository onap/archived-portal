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
package org.onap.portalapp.portal.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONObject;
import org.onap.portalapp.portal.domain.CentralizedApp;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.logging.aop.EPMetricsLog;
import org.onap.portalsdk.core.domain.MenuData;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.service.DataAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("leftMenuService")
@Transactional
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPMetricsLog

public class EPLeftMenuServiceImpl implements EPLeftMenuService {

	private static final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(EPLeftMenuServiceImpl.class);

	
	@Autowired
	private ExternalAccessRolesService externalAccessRolesService;
	@Autowired
	private DataAccessService dataAccessService;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.onap.portalapp.portal.service.EPLeftMenuService#getLeftMenuItems
	 * (java.util.Set)
	 */
	@Override
	public String getLeftMenuItems(EPUser user, Set<MenuData> fullMenuSet, Set<String> roleFunctionSet) {
		final Map<String, JSONObject> defaultNavMap = new LinkedHashMap<String, JSONObject>();
		resetNavMap(defaultNavMap);
		loadDefaultNavMap(defaultNavMap);
		loadNavMapByUserAdminRole(defaultNavMap,user);
		loadNavMapByRole(defaultNavMap, fullMenuSet , user);
		return convertToSideBarModel(defaultNavMap);
	}

	/**
	 * Clears the map
	 * 
	 * @param defaultNavMap
	 */
	private void resetNavMap(Map<String, JSONObject> defaultNavMap) {
		defaultNavMap.clear();
	}

	/**
	 * 
	 * @param defaultNavMap
	 * @param fullMenuSet
	 */
	private void loadNavMapByRole(Map<String, JSONObject> defaultNavMap, Set<MenuData> fullMenuSet , EPUser user) {

		class SortOrderComparator implements Comparator<MenuData> {
			@Override
			public int compare(MenuData e1, MenuData e2) {
				return e1.getSortOrder().compareTo(e2.getSortOrder());
			}
		}

		SortedSet<MenuData> sortMenuSet = new TreeSet<MenuData>(new SortOrderComparator());
		for (MenuData mn : fullMenuSet) {
			sortMenuSet.add(mn);
		}
		
		// Remove Roles from left menu if user doesnt have admin access on
		// centralized application
		List<CentralizedApp> applicationsList = null;
		applicationsList = externalAccessRolesService.getCentralizedAppsOfUser(user.getOrgUserId());
		if (applicationsList.size() == 0)
			sortMenuSet.removeIf(x -> x.getLabel().contains("Roles"));

		for (MenuData mn : sortMenuSet) {
			JSONObject navItemsDetails = new JSONObject();
			navItemsDetails.put("name", mn.getLabel());
			navItemsDetails.put("state", mn.getAction());
			navItemsDetails.put("imageSrc", mn.getImageSrc());
			defaultNavMap.put(mn.getAction(), navItemsDetails);
		}
	}

	/**
	 * 
	 * @param defaultNavMap
	 * @return
	 */
	private String convertToSideBarModel(Map<String, JSONObject> defaultNavMap) {
		JSONObject sidebarModel = new JSONObject();
		JSONArray navItems = new JSONArray();
		Collection<JSONObject> jsonObjs = defaultNavMap.values();

		for (JSONObject navItemsDetail : jsonObjs)
			navItems.put(navItemsDetail);

		sidebarModel.put("label", "ECOMP portal");
		sidebarModel.put("navItems", navItems);
		return sidebarModel.toString();
	}

	/**
	 * Loads default entries for regular user.
	 * 
	 * @param defaultNavMap
	 */
	private void loadDefaultNavMap(Map<String, JSONObject> defaultNavMap) {

		JSONObject navItemsDetails1 = new JSONObject();
		navItemsDetails1.put("name", "Home");
		navItemsDetails1.put("state", "root.applicationsHome");
		navItemsDetails1.put("imageSrc", "icon-building-home");
		defaultNavMap.put("root.applicationsHome", navItemsDetails1);

		JSONObject navItemsDetails2 = new JSONObject();
		navItemsDetails2.put("name", "Application Catalog");
		navItemsDetails2.put("state", "root.appCatalog");
		navItemsDetails2.put("imageSrc", "icon-apps-marketplace");
		defaultNavMap.put("root.appCatalog", navItemsDetails2);

		JSONObject navItemsDetails3 = new JSONObject();
		navItemsDetails3.put("name", "Widget Catalog");
		navItemsDetails3.put("state", "root.widgetCatalog");
		navItemsDetails3.put("imageSrc", "icon-apps-marketplace");
		defaultNavMap.put("root.widgetCatalog", navItemsDetails3);

	}
	
	@SuppressWarnings("unchecked")
	private void loadNavMapByUserAdminRole(Map<String, JSONObject> defaultNavMap, EPUser user) {
	List<String> applicationsList = new ArrayList<>();
	final Map<String, Long> appParams = new HashMap<>();
	appParams.put("userId", user.getId());
	applicationsList = dataAccessService.executeNamedQuery("getAprroverRoleFunctionsOfUser", appParams, null);
	if (applicationsList.size() > 0) {
	JSONObject navItemsDetails = new JSONObject();
	navItemsDetails.put("name", "Users");
	navItemsDetails.put("state", "root.users");
	navItemsDetails.put("imageSrc", "icon-user");
	defaultNavMap.put("root.users", navItemsDetails);
	}
	}

}
