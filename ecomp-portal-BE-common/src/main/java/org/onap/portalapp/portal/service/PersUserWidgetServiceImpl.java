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
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.PersUserWidgetSelection;
import org.onap.portalapp.portal.logging.aop.EPMetricsLog;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.service.DataAccessService;

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
		
		List<PersUserWidgetSelection> persList = getUserWidgetSelction(user, widgetId);
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

	@SuppressWarnings("unchecked")
	private List<PersUserWidgetSelection> getUserWidgetSelction(EPUser user, Long widgetId) {
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion userIdCriterion = Restrictions.eq("userId", user.getId());
		Criterion widgetIdCriterion = Restrictions.eq("widgetId", widgetId);
		restrictionsList.add(Restrictions.and(userIdCriterion, widgetIdCriterion));
		return  (List<PersUserWidgetSelection>) dataAccessService.getList(PersUserWidgetSelection.class, null, restrictionsList, null);
	}

}
