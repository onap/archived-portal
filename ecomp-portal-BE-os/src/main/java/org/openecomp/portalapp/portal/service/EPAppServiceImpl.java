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


import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import org.openecomp.portalapp.portal.domain.EPApp;
import org.openecomp.portalapp.portal.logging.aop.EPMetricsLog;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.att.nsa.cambria.client.CambriaClientFactory;
import com.att.nsa.cambria.client.CambriaTopicManager;

@Service("epAppService")
@Transactional
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPMetricsLog
public class EPAppServiceImpl extends EPAppCommonServiceImpl implements EPAppService {

	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(EPAppServiceImpl.class);

	@Autowired
	private DataAccessService dataAccessService;
	
	@Override
	public List<EPApp> getUserRemoteApps(String id) {
		
			StringBuilder query = new StringBuilder();
		
			query.append("SELECT * FROM FN_APP join FN_USER_ROLE ON FN_USER_ROLE.APP_ID = FN_APP.APP_ID where ");
			query.append(
						"FN_USER_ROLE.USER_ID = " + id + " AND FN_USER_ROLE.ROLE_ID != " + SUPER_ADMIN_ROLE_ID);
			query.append(" AND FN_APP.ENABLED = 'Y'");

			TreeSet<EPApp> distinctApps = new TreeSet<EPApp>();

			@SuppressWarnings("unchecked")
			List<EPApp> adminApps = dataAccessService.executeSQLQuery(query.toString(), EPApp.class, null);
			for (EPApp app : adminApps) {
				distinctApps.add(app);
			}

			List<EPApp> userApps = new ArrayList<EPApp>();
			userApps.addAll(distinctApps);
			return userApps;
	
	}
	
	public CambriaTopicManager getTopicManager(LinkedList<String> urlList, String key, String secret) throws GeneralSecurityException, Exception{
		return CambriaClientFactory.createTopicManager( null, urlList, key, secret);
	}

}