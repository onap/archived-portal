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
package org.openecomp.portalapp.portal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openecomp.portalapp.portal.ecomp.model.SearchResultItem;
import org.openecomp.portalapp.portal.transport.CommonWidget;
import org.openecomp.portalapp.portal.transport.CommonWidgetMeta;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class DashboardSearchServiceImpl implements DashboardSearchService{
	
	private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(DashboardSearchServiceImpl.class);
	
	@Autowired
	private DataAccessService dataAccessService;
	
	@SuppressWarnings("unchecked")
	public Map<String, List<SearchResultItem>> searchResults(String orgUserId, String searchString){
		
		Map<String, String> params = new HashMap<>();
		params.put("userId", orgUserId);
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getRelatedUsers(String orgUserId) {
		
		Map<String, String> params = new HashMap<>();
		
		params.put("org_user_id", orgUserId);
		
		List<String> activeUsers = null;
		
		try{
			activeUsers = dataAccessService.executeNamedQuery("relatedUsers", params, null);
		}
		catch(Exception e){
			logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
		}
		
		return activeUsers;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getRelatedUserVOs(String orgUserId) {
		
		Map<String, String> params = new HashMap<>();
		
		params.put("org_user_id", orgUserId);
		
		List<Object[]> activeUsers = null;
		
		try{
			activeUsers = dataAccessService.executeNamedQuery("relatedUserVOs", params, null);
		}
		catch(Exception e){
			logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
		}
		
		return activeUsers;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public CommonWidgetMeta getWidgetData(String resourceType) {

		List<CommonWidget> widgetItems = null;
		try{
			Map<String, String> params = new HashMap<>();
			params.put("cat", resourceType);
			widgetItems = (List<CommonWidget>)dataAccessService.executeNamedQuery("getCommonWidgetItem", params, null);

			/*widgetItems2  = new ArrayList<CommonWidget2>();

			for(int i = 0; i < widgetItems.size(); i++){
				CommonWidget2 item = (CommonWidget2)widgetItems.get(i);
				widgetItems2.add(new CommonWidget2(item.getCategory(), item.getHref(), item.getTitle(), item.getSortOrder()));
			}*/
		}
		catch(Exception e){
			logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
		}
		return new CommonWidgetMeta(resourceType, widgetItems);
	}
	
	@Override
	public String saveWidgetDataBulk(CommonWidgetMeta commonMetaWidgetData) {

		try{
			for(CommonWidget widgetData : commonMetaWidgetData.getItems()){
				widgetData.setCategory(commonMetaWidgetData.getCategory());
				dataAccessService.saveDomainObject(widgetData, null);
			}
			
			return "success";
		}
		catch(Exception e){
			logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
			return e.getMessage();
		}
	}
	
	@Override
	public String saveWidgetData(CommonWidget commonWidgetData) {

		try{
			
			dataAccessService.saveDomainObject(commonWidgetData, null);
			return "success";
		}
		catch(Exception e){
			logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
			return e.getMessage();
		}
	}

	@Override
	public String deleteWidgetData(CommonWidget eventWidget) {
		try{
			dataAccessService.deleteDomainObject(eventWidget, null);
			return "success";
		}
		catch(Exception e){
			logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
			return e.getMessage();
		}
	}

/*	@Override
	public EventWidgetMeta getEventWidgetData() {
		List<EventWidget> widgetItems = null;
		try{
			Map<String, String> params = new HashMap<>();
			widgetItems = (List<EventWidget>)dataAccessService.executeNamedQuery("getEventWidgetData", params, null);

			widgetItems2  = new ArrayList<CommonWidget2>();

			for(int i = 0; i < widgetItems.size(); i++){
				CommonWidget2 item = (CommonWidget2)widgetItems.get(i);
				widgetItems2.add(new CommonWidget2(item.getCategory(), item.getHref(), item.getTitle(), item.getSortOrder()));
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return new EventWidgetMeta("EVENTS", widgetItems);
	}

	@Override
	public String saveEventWidgetDataBulk(EventWidgetMeta eventsWidgetData) {
		try{
			for(EventWidget widgetData : eventsWidgetData.getItems()){
				widgetData.setCategory(eventsWidgetData.getCategory());
				dataAccessService.saveDomainObject(widgetData, null);
			}
			
			return "success";
		}
		catch(Exception e){
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	@Override
	public String saveEventWidgetData(EventWidget eventWidget) {
		try{
			dataAccessService.saveDomainObject(eventWidget, null);
			return "success";
		}
		catch(Exception e){
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	@Override
	public String deleteEventWidgetData(EventWidget eventWidget) {
		try{
			dataAccessService.deleteDomainObject(eventWidget, null);
			return "success";
		}
		catch(Exception e){
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	*/
}
