/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright © 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the “License”);
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
 * under the Creative Commons License, Attribution 4.0 Intl. (the “License”);
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
package org.openecomp.portalapp.portal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.openecomp.portalapp.portal.domain.WidgetCatalogParameter;
import org.openecomp.portalapp.portal.logging.aop.EPMetricsLog;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;

@Service("widgetParameterService")
@EnableAspectJAutoProxy
@EPMetricsLog
public class WidgetParameterServiceImpl implements WidgetParameterService{
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(WidgetParameterServiceImpl.class);

	@Autowired
	private DataAccessService dataAccessService;

	@SuppressWarnings("unchecked")
	@Override
	public WidgetCatalogParameter getUserParamById(Long widgetId, Long userId, Long paramId) {
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion widgetIdCrit = Restrictions.eq("widgetId", widgetId);
		restrictionsList.add(widgetIdCrit);
		Criterion userIdCrit = Restrictions.eq("userId", userId);
		restrictionsList.add(userIdCrit);
		Criterion paramIdCrit = Restrictions.eq("paramId", paramId);
		restrictionsList.add(paramIdCrit);
		
		
		WidgetCatalogParameter widgetParam = null;
		List<WidgetCatalogParameter> list = (List<WidgetCatalogParameter>) dataAccessService
				.getList(WidgetCatalogParameter.class, null, restrictionsList, null);
		if(list.size() != 0)
			widgetParam = list.get(0);
		logger.debug(EELFLoggerDelegate.debugLogger,
				"getUserParamById: widget parameters: " + widgetParam);
		return widgetParam;
	}

	@Override
	public void saveUserParameter(WidgetCatalogParameter newParameter) {
		dataAccessService.saveDomainObject(newParameter, null);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<WidgetCatalogParameter> getUserParameterById(Long paramId) {
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion paramIdCrit = Restrictions.eq("paramId", paramId);
		restrictionsList.add(paramIdCrit);
		List<WidgetCatalogParameter> list = (List<WidgetCatalogParameter>) dataAccessService
				.getList(WidgetCatalogParameter.class, null, restrictionsList, null);
		return list;
	}

	@Override
	public void deleteUserParameterById(Long paramId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("paramId", Long.toString(paramId));
		dataAccessService.executeNamedQuery("deleteWidgetCatalogParameter", params, null);
		dataAccessService.executeNamedQuery("deleteMicroserviceParameterById", params, null);
	}
}
