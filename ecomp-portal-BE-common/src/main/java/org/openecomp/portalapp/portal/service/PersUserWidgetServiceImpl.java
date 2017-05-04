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
package org.openecomp.portalapp.portal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.domain.PersUserWidgetSelection;
import org.openecomp.portalapp.portal.logging.aop.EPMetricsLog;

@Service("persUserWidgetService")
@Transactional
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPMetricsLog
public class PersUserWidgetServiceImpl implements PersUserWidgetService{
	
	private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(PersUserAppServiceImpl.class);

	@Autowired
	private DataAccessService dataAccessService;
	
	@Override
	public void setPersUserAppValue(EPUser user, Long widgetId, boolean select) {
		if (user == null || widgetId == null)
			throw new IllegalArgumentException("setPersUserAppValue: Null values");

		String filter = " where user_id = " + Long.toString(user.getId()) + " and widget_id = "
				+ Long.toString(widgetId);
		@SuppressWarnings("unchecked")
		List<PersUserWidgetSelection> persList = dataAccessService.getList(PersUserWidgetSelection.class, filter, null, null);

		// Key constraint limits to 1 row
		PersUserWidgetSelection persRow = null;
		if (persList.size() == 1){
			persRow = persList.get(0);
		}
		else {
			persRow = new PersUserWidgetSelection(null, user.getId(), widgetId, null);
		}			
		if(select){
			if (persRow.getId() != null){
				dataAccessService.deleteDomainObject(persRow, null);				
			}
			persRow.setStatusCode("S"); // show
			dataAccessService.saveDomainObject(persRow, null);			
		} else{
			if (persRow.getId() != null){
				dataAccessService.deleteDomainObject(persRow, null);
			} 
			persRow.setStatusCode("H"); // Hide
			dataAccessService.saveDomainObject(persRow, null);			
		}
		
	}

}
