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
package org.onap.portalapp.portal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.onap.portalapp.portal.ecomp.model.SearchResultItem;
import org.onap.portalapp.portal.transport.CommonWidget;
import org.onap.portalapp.portal.transport.CommonWidgetMeta;
import org.onap.portalsdk.core.service.DataAccessService;

@Component
public class DashboardSearchServiceImpl implements DashboardSearchService {

	@Autowired
	private DataAccessService dataAccessService;

	public Map<String, List<SearchResultItem>> searchResults(String userId, String searchString) {
		Map<String, String> params = new HashMap<>();
		params.put("userId", userId);
		params.put("searchQuery", searchString);
		// Named query is stored in a *.hbm.xml file, mapped to SearchResultItem
		@SuppressWarnings("unchecked")
		List<SearchResultItem> list = dataAccessService.executeNamedQuery("searchPortal", params, null);
		Map<String, List<SearchResultItem>> finalJson = null;
		if (list.size() > 0) {
			finalJson = new HashMap<String, List<SearchResultItem>>();
			for (SearchResultItem thisResult : list) {
				List<SearchResultItem> thisList = finalJson.get(thisResult.getCategory().toLowerCase());
				if (thisList == null)
					thisList = new ArrayList<SearchResultItem>();
				thisList.add(thisResult);
				finalJson.put(thisResult.getCategory().toLowerCase(), thisList);
			}
		}
		return finalJson;
	}

	@Override
	public List<String> getRelatedUsers(String userId) {
		Map<String, String> params = new HashMap<>();
		params.put("userId", userId);
		@SuppressWarnings("unchecked")
		List<String> activeUsers = dataAccessService.executeNamedQuery("relatedUsers", params, null);
		return activeUsers;
	}

	@Override
	public CommonWidgetMeta getWidgetData(String resourceType) {
		Map<String, String> params = new HashMap<>();
		params.put("cat", resourceType);
		@SuppressWarnings("unchecked")
		List<CommonWidget> widgetItems = (List<CommonWidget>) dataAccessService.executeNamedQuery("getCommonWidgetItem", params, null);
		return new CommonWidgetMeta(resourceType, widgetItems);
	}

	@Override
	public String saveWidgetDataBulk(CommonWidgetMeta commonMetaWidgetData) {
		for (CommonWidget widgetData : commonMetaWidgetData.getItems()) {
			widgetData.setCategory(commonMetaWidgetData.getCategory());
			dataAccessService.saveDomainObject(widgetData, null);
		}
		return "success";
	}

	@Override
	public String saveWidgetData(CommonWidget commonWidgetData) {
		dataAccessService.saveDomainObject(commonWidgetData, null);
		return "success";
	}

	@Override
	public String deleteWidgetData(CommonWidget eventWidget) {
		dataAccessService.deleteDomainObject(eventWidget, null);
		return "success";
	}
}
